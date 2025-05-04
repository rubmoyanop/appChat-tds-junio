package umu.tds.appchat.persistencia;

import java.util.LinkedList;
import java.util.List;

import beans.Entidad;
import beans.Propiedad;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import umu.tds.appchat.dao.ContactoIndividualDAO;
import umu.tds.appchat.dao.DAOExcepcion;
import umu.tds.appchat.dao.UsuarioDAO;
import umu.tds.appchat.modelo.ContactoIndividual;
import umu.tds.appchat.modelo.Usuario;

// Implementar la interfaz ContactoIndividualDAO
public class ContactoIndividualDAO_TDS implements ContactoIndividualDAO {

    private ServicioPersistencia servPersistencia;
    private UsuarioDAO usuarioDAO;

    public ContactoIndividualDAO_TDS(ServicioPersistencia servPersistencia, UsuarioDAO usuarioDAO) {
        this.servPersistencia = servPersistencia;
        this.usuarioDAO = usuarioDAO;
    }

    public ContactoIndividualDAO_TDS() {
        this(FactoriaServicioPersistencia.getInstance().getServicioPersistencia());
    }

    private ContactoIndividualDAO_TDS(ServicioPersistencia servPersistencia) {
        this.servPersistencia = servPersistencia;
        try {
             this.usuarioDAO = FactoriaDAO_TDS.getInstancia().getUsuarioDAO();
        } catch (DAOExcepcion e) {
             throw new RuntimeException("No se pudo obtener UsuarioDAO en ContactoIndividualDAO_TDS", e);
        }
    }

    /**
     * Convierte un ContactoIndividual a Entidad.
     */
    private Entidad contactoIndividualToEntidad(ContactoIndividual contacto) {
        Entidad entidad = new Entidad();
        entidad.setNombre("ContactoIndividual");
        List<Propiedad> propiedades = new LinkedList<>();
        propiedades.add(new Propiedad("nombre", contacto.getNombre()));
        // Guardar el ID del Usuario asociado
        if (contacto.getUsuario() == null || contacto.getUsuario().getId() == 0) {
             // Lanzar excepción o manejar el caso donde el usuario asociado no es válido o no está persistido
             throw new IllegalStateException("El usuario asociado al ContactoIndividual debe estar persistido y tener un ID válido.");
        }
        propiedades.add(new Propiedad("usuarioId", String.valueOf(contacto.getUsuario().getId())));
        // Podríamos añadir más propiedades si ContactoIndividual las tuviera (ej. mensajes)
        entidad.setPropiedades(propiedades);
        return entidad;
    }

    /**
     * Convierte una Entidad a ContactoIndividual.
     */
    private ContactoIndividual entidadToContactoIndividual(Entidad entidad) throws DAOExcepcion {
        if (entidad == null || !"ContactoIndividual".equals(entidad.getNombre())) {
            return null; 
        }

        String nombre = servPersistencia.recuperarPropiedadEntidad(entidad, "nombre");
        String usuarioIdStr = servPersistencia.recuperarPropiedadEntidad(entidad, "usuarioId");

        if (nombre == null || usuarioIdStr == null) {
            throw new DAOExcepcion("Propiedades 'nombre' o 'usuarioId' faltantes en Entidad ContactoIndividual con ID: " + entidad.getId());
        }

        Usuario usuarioAsociado = null;
        try {
            int usuarioId = Integer.parseInt(usuarioIdStr);
            usuarioAsociado = usuarioDAO.recuperarUsuario(usuarioId); // Recuperar el usuario
            if (usuarioAsociado == null) {
                 throw new DAOExcepcion("No se encontró el Usuario con ID " + usuarioId + " asociado al ContactoIndividual " + entidad.getId());
            }
        } catch (NumberFormatException e) {
            throw new DAOExcepcion("Error al parsear usuarioId: " + usuarioIdStr + " para ContactoIndividual " + entidad.getId(), e);
        }

        ContactoIndividual contacto = new ContactoIndividual(entidad.getId(), nombre, usuarioAsociado);
        // TODO: Aquí cargaremos la lista de mensajes cuando toque

        return contacto;
    }


    @Override
    public void registrarContactoIndividual(ContactoIndividual contacto) throws DAOExcepcion {
        try {
            // Comprobar si ya existe (por ID) antes de registrar
             if (contacto.getId() != 0) {
                 Entidad existente = servPersistencia.recuperarEntidad(contacto.getId());
                 if (existente != null) {
                     System.err.println("Advertencia: Intentando registrar un ContactoIndividual que ya existe (ID: " + contacto.getId() + "). Se procederá a modificar.");
                     modificarContactoIndividual(contacto); // Llamar a modificar si ya existe
                     return;
                 }
             }
             Entidad entidad = contactoIndividualToEntidad(contacto);
            entidad = servPersistencia.registrarEntidad(entidad);
            // Establecer el ID generado por la persistencia en el objeto
            contacto.setId(entidad.getId());
        } catch (Exception e) {
            throw new DAOExcepcion("Error al registrar ContactoIndividual: " + e.getMessage(), e);
        }
    }

    @Override
    public ContactoIndividual recuperarContactoIndividual(int id) throws DAOExcepcion {
        try {
            Entidad entidad = servPersistencia.recuperarEntidad(id);
            return entidadToContactoIndividual(entidad);
        } catch (Exception e) {
            // Si la entidad no se encuentra, recuperarEntidad devuelve null, y entidadToContactoIndividual devolverá null.
            // Si ocurre otro error, se lanza DAOExcepcion.
            throw new DAOExcepcion("Error al recuperar ContactoIndividual con ID: " + id, e);
        }
    }

    @Override
    public void modificarContactoIndividual(ContactoIndividual contacto) throws DAOExcepcion {
        try {
            Entidad entidad = servPersistencia.recuperarEntidad(contacto.getId());
            if (entidad == null) {
                throw new DAOExcepcion("ContactoIndividual no encontrado para modificar (ID: " + contacto.getId() + ")");
            }
            // Actualizar propiedades
            for (Propiedad prop : entidad.getPropiedades()) {
                String nombreProp = prop.getNombre();
                String nuevoValor = null;
                switch (nombreProp) {
                    case "nombre":
                        nuevoValor = contacto.getNombre();
                        break;
                    case "usuarioId": 
                         if (contacto.getUsuario() == null || contacto.getUsuario().getId() == 0) {
                             throw new IllegalStateException("El usuario asociado al ContactoIndividual debe estar persistido y tener un ID válido para modificar.");
                         }
                         nuevoValor = String.valueOf(contacto.getUsuario().getId());
                         break;
                    // TODO: Añadir la lista de IDs de mensajes
                    default: continue;
                }
                 String valorActual = prop.getValor();
                 if (nuevoValor != null && !nuevoValor.equals(valorActual)) {
                    prop.setValor(nuevoValor);
                    servPersistencia.modificarPropiedad(prop);
                } else if (nuevoValor == null && valorActual != null && !valorActual.isEmpty()) { // Para limpiar una propiedad
                     prop.setValor("");
                     servPersistencia.modificarPropiedad(prop);
                 }
            }
        } catch (Exception e) {
            throw new DAOExcepcion("Error al modificar ContactoIndividual con ID: " + contacto.getId(), e);
        }
    }

    @Override
    public void borrarContactoIndividual(ContactoIndividual contacto) throws DAOExcepcion {
         try {
            Entidad entidad = servPersistencia.recuperarEntidad(contacto.getId());
            if (entidad != null) {
                servPersistencia.borrarEntidad(entidad);
            } else {
                 System.err.println("Advertencia: Intentando borrar un ContactoIndividual que no existe (ID: " + contacto.getId() + ")");
            }
        } catch (Exception e) {
            throw new DAOExcepcion("Error al borrar ContactoIndividual con ID: " + contacto.getId(), e);
        }
    }
}
