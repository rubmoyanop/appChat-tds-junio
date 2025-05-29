package umu.tds.appchat.persistencia;

import umu.tds.appchat.dao.DAOExcepcion;
import umu.tds.appchat.dao.UsuarioDAO;
import umu.tds.appchat.modelo.Contacto;
import umu.tds.appchat.modelo.Usuario;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import beans.Entidad;
import beans.Propiedad;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class UsuarioDAO_TDS implements UsuarioDAO {

    private ServicioPersistencia servPersistencia;
    private PoolUsuarios poolUsuarios;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    private ContactoIndividualDAO_TDS contactoIndividualDAO;
    private GrupoDAO_TDS grupoDAO;

    public UsuarioDAO_TDS() {
        servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
        poolUsuarios = PoolUsuarios.INSTANCE;
    }

    public void setContactoIndividualDAO(ContactoIndividualDAO_TDS dao) {
        this.contactoIndividualDAO = dao;
    }

    public void setGrupoDAO(GrupoDAO_TDS dao) {
        this.grupoDAO = dao;
    }

    /**
     * Convierte un objeto Usuario a una Entidad para persistencia.
     * @param usuario El objeto Usuario a convertir.
     * @return La Entidad correspondiente al Usuario.
     */
    private Entidad usuarioToEntidad(Usuario usuario) {
        Entidad entidad = new Entidad();
        entidad.setNombre("Usuario");

        List<Propiedad> propiedades = new LinkedList<>();
        propiedades.add(new Propiedad("nombre", usuario.getNombre()));
        propiedades.add(new Propiedad("email", usuario.getEmail() != null ? usuario.getEmail() : ""));
        propiedades.add(new Propiedad("movil", usuario.getMovil()));
        propiedades.add(new Propiedad("contrasena", usuario.getContrasena()));
        propiedades.add(new Propiedad("imagen", usuario.getImagen() != null ? usuario.getImagen() : ""));
        String fechaNacStr = usuario.getFechaNacimiento() != null ? usuario.getFechaNacimiento().format(DATE_FORMATTER) : "";
        propiedades.add(new Propiedad("fechaNacimiento", fechaNacStr));
        String fechaRegistroStr = usuario.getFechaRegistro() != null ? usuario.getFechaRegistro().format(DATE_FORMATTER) : "";
        propiedades.add(new Propiedad("fechaRegistro", fechaRegistroStr));
        propiedades.add(new Propiedad("saludo", usuario.getSaludo() != null ? usuario.getSaludo() : ""));
        propiedades.add(new Propiedad("isPremium", String.valueOf(usuario.isPremium())));

        String idsContactos = usuario.getContactos().stream()
                                     .map(c -> String.valueOf(c.getId()))
                                     .collect(Collectors.joining(","));
        propiedades.add(new Propiedad("contactosIds", idsContactos));

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
        if (entidad == null) {
            throw new DAOExcepcion("Entidad nula al convertir a Usuario.");
        }
        if (!"Usuario".equals(entidad.getNombre())) {
            throw new DAOExcepcion("Entidad no es del tipo Usuario.");
        }
        if (entidad.getId() == 0) {
            throw new DAOExcepcion("Entidad Usuario sin ID.");
        }
        if (poolUsuarios.contains(entidad.getId())) {
            return poolUsuarios.getUsuario(entidad.getId());
        }

        String nombre = null;
        String email = null;
        String movil = null;
        String contrasena = null;
        String imagen = null;
        LocalDate fechaNacimiento = null;
        LocalDate fechaRegistro = null; 
        String saludo = null;
        boolean isPremium = false;
        String contactosIdsStr = null;

        try {
            nombre = servPersistencia.recuperarPropiedadEntidad(entidad, "nombre");
            email = servPersistencia.recuperarPropiedadEntidad(entidad, "email");
            movil = servPersistencia.recuperarPropiedadEntidad(entidad, "movil");
            contrasena = servPersistencia.recuperarPropiedadEntidad(entidad, "contrasena");
            imagen = servPersistencia.recuperarPropiedadEntidad(entidad, "imagen");
            String fechaNacStr = servPersistencia.recuperarPropiedadEntidad(entidad, "fechaNacimiento");
            if (fechaNacStr != null && !fechaNacStr.isEmpty()) {
                try {
                    fechaNacimiento = LocalDate.parse(fechaNacStr, DATE_FORMATTER);
                } catch (DateTimeParseException e) {
                    System.err.println("Error parseando fecha de nacimiento para usuario ID " + entidad.getId() + ": " + fechaNacStr);
                }
            }
            fechaRegistro = servPersistencia.recuperarPropiedadEntidad(entidad, "fechaRegistro") != null ?
                            LocalDate.parse(servPersistencia.recuperarPropiedadEntidad(entidad, "fechaRegistro"), DATE_FORMATTER) : null;
            saludo = servPersistencia.recuperarPropiedadEntidad(entidad, "saludo");
            String premiumStr = servPersistencia.recuperarPropiedadEntidad(entidad, "isPremium");
            isPremium = Boolean.parseBoolean(premiumStr);
            contactosIdsStr = servPersistencia.recuperarPropiedadEntidad(entidad, "contactosIds");
        } catch (Exception e) {
            throw new DAOExcepcion("Error al recuperar propiedad de Entidad Usuario con ID: " + entidad.getId(), e);
        }

        Usuario usuario = new Usuario(nombre, email, fechaNacimiento, fechaRegistro, movil, contrasena, imagen, saludo, isPremium);
        usuario.setId(entidad.getId());

        poolUsuarios.addUsuario(usuario);

        if (contactosIdsStr != null && !contactosIdsStr.isEmpty()) {
            List<Contacto> contactos = new ArrayList<>();
            List<String> idsList = Arrays.asList(contactosIdsStr.split(","));
            for (String idStr : idsList) {
                try {
                    int contactoId = Integer.parseInt(idStr.trim());
                    Entidad entidadContacto = servPersistencia.recuperarEntidad(contactoId);
                    if (entidadContacto == null) {
                        System.err.println("Advertencia: No existe entidad con ID " + contactoId + " para el usuario " + usuario.getId());
                        continue;
                    }
                    String tipo = entidadContacto.getNombre();
                    Contacto contacto = null;
                    if ("ContactoIndividual".equals(tipo)) {
                        contacto = contactoIndividualDAO.recuperarContactoIndividual(contactoId);
                    } else if ("Grupo".equals(tipo) && grupoDAO != null) {
                        contacto = grupoDAO.recuperarGrupo(contactoId);
                    }
                    if (contacto != null) {
                        contactos.add(contacto);
                    } else {
                        System.err.println("Advertencia: No se pudo recuperar el contacto/grupo con ID " + contactoId + " para el usuario " + usuario.getId());
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Error parseando ID de contacto: " + idStr + " para usuario " + usuario.getId());
                } catch (DAOExcepcion e) {
                    System.err.println("Error DAO al recuperar contacto/grupo con ID " + idStr + " para usuario " + usuario.getId() + ": " + e.getMessage());
                }
            }
            usuario.setContactos(contactos);
        }

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

            boolean propiedadContactosModificada = false;

            for (Propiedad prop : entidad.getPropiedades()) {
                String nombreProp = prop.getNombre();
                String nuevoValor = null;
                switch (nombreProp) {
                    case "nombre":      nuevoValor = usuario.getNombre(); break;
                    case "email":       nuevoValor = usuario.getEmail() != null ? usuario.getEmail() : ""; break;
                    case "movil":       nuevoValor = usuario.getMovil(); break;
                    case "contrasena":  nuevoValor = usuario.getContrasena(); break;
                    case "imagen":      nuevoValor = usuario.getImagen() != null ? usuario.getImagen() : ""; break;
                    case "fechaNacimiento":
                        nuevoValor = usuario.getFechaNacimiento() != null ? usuario.getFechaNacimiento().format(DATE_FORMATTER) : "";
                        break;
                    case "saludo":      nuevoValor = usuario.getSaludo() != null ? usuario.getSaludo() : ""; break;
                    case "isPremium":   nuevoValor = String.valueOf(usuario.isPremium()); break;
                    case "contactosIds":
                        nuevoValor = usuario.getContactos().stream()
                                            .map(c -> String.valueOf(c.getId()))
                                            .collect(Collectors.joining(","));
                        propiedadContactosModificada = true;
                        break;
                    default: continue;
                }
                String valorActual = prop.getValor();
                if (nuevoValor != null && !nuevoValor.equals(valorActual)) {
                    prop.setValor(nuevoValor);
                    servPersistencia.modificarPropiedad(prop);
                } else if (nuevoValor == null && valorActual != null) {
                    prop.setValor("");
                    servPersistencia.modificarPropiedad(prop);
                }
            }

            if (!propiedadContactosModificada) {
                String idsContactos = usuario.getContactos().stream()
                                             .map(c -> String.valueOf(c.getId()))
                                             .collect(Collectors.joining(","));
                if (!idsContactos.isEmpty()) {
                    servPersistencia.anadirPropiedadEntidad(entidad, "contactosIds", idsContactos);
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
