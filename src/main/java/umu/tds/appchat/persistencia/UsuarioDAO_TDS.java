package umu.tds.appchat.persistencia;

import umu.tds.appchat.dao.DAOExcepcion;
import umu.tds.appchat.dao.UsuarioDAO;
import umu.tds.appchat.modelo.Usuario;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import beans.Entidad;
import beans.Propiedad;

import java.util.LinkedList;
import java.util.List;

public class UsuarioDAO_TDS implements UsuarioDAO {

    private ServicioPersistencia servPersistencia;
    private PoolUsuarios poolUsuarios;

    public UsuarioDAO_TDS() {
        servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
        poolUsuarios = PoolUsuarios.INSTANCE;
    }

    /**
     * Convierte un objeto Usuario a una Entidad para persistencia.
     * @param usuario El objeto Usuario a convertir.
     * @return La Entidad correspondiente al Usuario.
     */
    private Entidad usuarioToEntidad(Usuario usuario) {
        Entidad entidad = new Entidad();
        entidad.setNombre("Usuario"); // Tipo de entidad

        List<Propiedad> propiedades = new LinkedList<>();
        propiedades.add(new Propiedad("nombre", usuario.getNombre()));
        propiedades.add(new Propiedad("movil", usuario.getMovil()));
        propiedades.add(new Propiedad("contrasena", usuario.getContrasena()));
        propiedades.add(new Propiedad("imagen", usuario.getImagen() != null ? usuario.getImagen() : ""));
        propiedades.add(new Propiedad("isPremium", String.valueOf(usuario.isPremium())));
        // TODO: Mapear lista de contactos cuando se implemente la relación
        // propiedades.add(new Propiedad("contactos", ...));
        entidad.setPropiedades(propiedades);
        return entidad;
    }

    /**
     * Convierte una Entidad a un objeto Usuario.
     * @param entidad La Entidad a convertir.
     * @return El objeto Usuario correspondiente a la Entidad.
     * @throws DAOExcepcion Si ocurre un error durante la conversión.
     */
    private Usuario entidadToUsuario(Entidad entidad) throws DAOExcepcion {
        // Comprobar si la entidad es nula
        if (entidad == null) {
            throw new DAOExcepcion("Entidad nula al convertir a Usuario.");
        }

        // Comprobar si la entidad es del tipo correcto
        if (!"Usuario".equals(entidad.getNombre())) {
            throw new DAOExcepcion("Entidad no es del tipo Usuario.");
        }

        // Comprobar si la entidad tiene ID
        if (entidad.getId() == 0) {
            throw new DAOExcepcion("Entidad Usuario sin ID.");
        }

        // Comprobar si ya existe en el Pool
        if (poolUsuarios.contains(entidad.getId())) {
            return poolUsuarios.getUsuario(entidad.getId());
        }

        // Recuperamos propiedades de la entidad
        String nombre = null;
        String movil = null;
        String contrasena = null;
        String imagen = null;
        boolean isPremium = false;

        try {
            nombre = servPersistencia.recuperarPropiedadEntidad(entidad, "nombre");
            movil = servPersistencia.recuperarPropiedadEntidad(entidad, "movil");
            contrasena = servPersistencia.recuperarPropiedadEntidad(entidad, "contrasena");
            imagen = servPersistencia.recuperarPropiedadEntidad(entidad, "imagen");
            String premiumStr = servPersistencia.recuperarPropiedadEntidad(entidad, "isPremium");
            isPremium = Boolean.parseBoolean(premiumStr);
        } catch (Exception e) {
            throw new DAOExcepcion("Error al recuperar propiedad de Entidad Usuario con ID: " + entidad.getId(), e);
        }

        // Creamos una instancia del Usuario
        Usuario usuario = new Usuario(nombre, movil, contrasena, imagen, isPremium);
        usuario.setId(entidad.getId());

        // Añadimos al Pool ANTES de recuperar relaciones para evitar ciclos
        poolUsuarios.addUsuario(usuario);

        // TODO: Recuperar relaciones (Contactos) Cuando se haga la implementación DAO de contacto

        return usuario;
    }

    @Override
    public void registrarUsuario(Usuario usuario) throws DAOExcepcion {
        Entidad entidadUsuario;
        try {
            if (usuario.getId() != 0) {
                entidadUsuario = servPersistencia.recuperarEntidad(usuario.getId());
                if (entidadUsuario != null) {
                    System.err.println("UsuarioDAO_TDS: Intentando registrar un usuario que ya existe (ID: " + usuario.getId() + "). Se procederá a modificar en su lugar.");
                    modificarUsuario(usuario);
                    return;
                }
            }
            entidadUsuario = usuarioToEntidad(usuario);
            entidadUsuario = servPersistencia.registrarEntidad(entidadUsuario);
            usuario.setId(entidadUsuario.getId());
        } catch (Exception e) {
            throw new DAOExcepcion("Error al registrar el Usuario: " + e.getMessage(), e);
        }
    }

    @Override
    public Usuario recuperarUsuario(int id) throws DAOExcepcion {
        try {
            Entidad entidad = servPersistencia.recuperarEntidad(id);
            if (entidad == null) {
                return null;
            }
            return entidadToUsuario(entidad);
        } catch (Exception e) {
            throw new DAOExcepcion("Error al recuperar Usuario con ID: " + id, e);
        }
    }

    @Override
    public Usuario recuperarUsuarioPorMovil(String movil) throws DAOExcepcion {
        try {
            List<Entidad> entidades = servPersistencia.recuperarEntidades("Usuario");
            for (Entidad entidad : entidades) {
                String movilEntidad = servPersistencia.recuperarPropiedadEntidad(entidad, "movil");
                if (movilEntidad != null && movilEntidad.equals(movil)) {
                    return entidadToUsuario(entidad);
                }
            }
            return null;
        } catch (Exception e) {
            throw new DAOExcepcion("Error al recuperar Usuario por móvil: " + movil, e);
        }
    }

    @Override
    public void modificarUsuario(Usuario usuario) throws DAOExcepcion {
        try {
            Entidad entidad = servPersistencia.recuperarEntidad(usuario.getId());
            if (entidad == null) {
                throw new DAOExcepcion("Usuario no encontrado para modificar (ID: " + usuario.getId() + ")");
            }
            for (Propiedad prop : entidad.getPropiedades()) {
                String nombreProp = prop.getNombre();
                String nuevoValor = null;
                switch (nombreProp) {
                    case "nombre":      nuevoValor = usuario.getNombre(); break;
                    case "movil":       nuevoValor = usuario.getMovil(); break;
                    case "contrasena":  nuevoValor = usuario.getContrasena(); break;
                    case "imagen":      nuevoValor = usuario.getImagen() != null ? usuario.getImagen() : ""; break;
                    case "isPremium":   nuevoValor = String.valueOf(usuario.isPremium()); break;
                    default: continue;
                }
                if (nuevoValor != null && !nuevoValor.equals(prop.getValor())) {
                    prop.setValor(nuevoValor);
                    servPersistencia.modificarPropiedad(prop);
                }
            }
            if (poolUsuarios.contains(usuario.getId())) {
                poolUsuarios.addUsuario(usuario);
            }
        } catch (Exception e) {
            throw new DAOExcepcion("Error al modificar el Usuario con ID: " + usuario.getId(), e);
        }
    }

    @Override
    public void borrarUsuario(Usuario usuario) throws DAOExcepcion {
        try {
            Entidad entidad = servPersistencia.recuperarEntidad(usuario.getId());
            if (entidad != null) {
                servPersistencia.borrarEntidad(entidad);
                if (poolUsuarios.contains(usuario.getId())) {
                    poolUsuarios.removeUsuario(usuario.getId());
                }
            } else {
                System.err.println("UsuarioDAO_TDS: Intentando borrar un usuario que no existe (ID: " + usuario.getId() + ")");
            }
        } catch (Exception e) {
            throw new DAOExcepcion("Error al borrar el Usuario con ID: " + usuario.getId(), e);
        }
    }

    @Override
    public List<Usuario> recuperarTodosUsuarios() throws DAOExcepcion {
        List<Usuario> todos = new LinkedList<>();
        try {
            List<Entidad> entidades = servPersistencia.recuperarEntidades("Usuario");
            for (Entidad entidad : entidades) {
                todos.add(entidadToUsuario(entidad));
            }
        } catch (Exception e) {
            throw new DAOExcepcion("Error al recuperar todos los Usuarios", e);
        }
        return todos;
    }
}
