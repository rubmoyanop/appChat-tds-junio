package umu.tds.appchat.persistencia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import beans.Entidad;
import beans.Propiedad;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import umu.tds.appchat.dao.ContactoIndividualDAO;
import umu.tds.appchat.dao.DAOExcepcion;
import umu.tds.appchat.dao.FactoriaDAO;
import umu.tds.appchat.dao.MensajeDAO;
import umu.tds.appchat.dao.UsuarioDAO;
import umu.tds.appchat.modelo.ContactoIndividual;
import umu.tds.appchat.modelo.Mensaje;
import umu.tds.appchat.modelo.Usuario;

// Implementar la interfaz ContactoIndividualDAO
public class ContactoIndividualDAO_TDS implements ContactoIndividualDAO {

    private ServicioPersistencia servPersistencia;
    private UsuarioDAO usuarioDAO;
    private MensajeDAO mensajeDAO;

    public ContactoIndividualDAO_TDS(ServicioPersistencia servPersistencia, UsuarioDAO usuarioDAO, MensajeDAO mensajeDAO) {
        this.servPersistencia = servPersistencia;
        this.usuarioDAO = usuarioDAO;
        this.mensajeDAO = mensajeDAO;
    }

    public ContactoIndividualDAO_TDS() {
        this(FactoriaServicioPersistencia.getInstance().getServicioPersistencia());
    }

    private ContactoIndividualDAO_TDS(ServicioPersistencia servPersistencia) {
        this.servPersistencia = servPersistencia;
        try {
            FactoriaDAO factoria = FactoriaDAO_TDS.getInstancia();
            this.usuarioDAO = factoria.getUsuarioDAO();
            this.mensajeDAO = factoria.getMensajeDAO();
        } catch (DAOExcepcion e) {
            throw new RuntimeException("No se pudo obtener DAOs en ContactoIndividualDAO_TDS", e);
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
        if (contacto.getUsuario() == null || contacto.getUsuario().getId() == 0) {
            throw new IllegalStateException("El usuario asociado al ContactoIndividual debe estar persistido y tener un ID válido.");
        }
        propiedades.add(new Propiedad("usuarioId", String.valueOf(contacto.getUsuario().getId())));

        String idsMensajes = contacto.getMensajes().stream()
                                     .map(m -> String.valueOf(m.getId()))
                                     .filter(idStr -> !idStr.equals("0"))
                                     .collect(Collectors.joining(","));
        propiedades.add(new Propiedad("mensajesIds", idsMensajes));

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
        String mensajesIdsStr = servPersistencia.recuperarPropiedadEntidad(entidad, "mensajesIds");

        if (nombre == null || usuarioIdStr == null) {
            throw new DAOExcepcion("Propiedades 'nombre' o 'usuarioId' faltantes en Entidad ContactoIndividual con ID: " + entidad.getId());
        }

        Usuario usuarioAsociado = null;
        try {
            int usuarioId = Integer.parseInt(usuarioIdStr);
            usuarioAsociado = usuarioDAO.recuperarUsuario(usuarioId);
            if (usuarioAsociado == null) {
                throw new DAOExcepcion("No se encontró el Usuario con ID " + usuarioId + " asociado al ContactoIndividual " + entidad.getId());
            }
        } catch (NumberFormatException e) {
            throw new DAOExcepcion("Error al parsear usuarioId: " + usuarioIdStr + " para ContactoIndividual " + entidad.getId(), e);
        }

        ContactoIndividual contacto = new ContactoIndividual(entidad.getId(), nombre, usuarioAsociado);

        if (mensajesIdsStr != null && !mensajesIdsStr.isEmpty()) {
            List<Mensaje> mensajes = new ArrayList<>();
            List<String> idsList = Arrays.asList(mensajesIdsStr.split(","));
            for (String idStr : idsList) {
                try {
                    int mensajeId = Integer.parseInt(idStr.trim());
                    Mensaje mensaje = mensajeDAO.recuperarMensaje(mensajeId);
                    if (mensaje != null) {
                        mensajes.add(mensaje);
                    } else {
                        System.err.println("Advertencia: No se pudo recuperar el mensaje con ID " + mensajeId + " para el contacto " + entidad.getId());
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Error parseando ID de mensaje: " + idStr + " para contacto " + entidad.getId());
                } catch (DAOExcepcion e) {
                    System.err.println("Error DAO al recuperar mensaje con ID " + idStr + " para contacto " + entidad.getId() + ": " + e.getMessage());
                }
            }
            contacto.setMensajes(mensajes);
        }

        return contacto;
    }

    @Override
    public void registrarContactoIndividual(ContactoIndividual contacto) throws DAOExcepcion {
        try {
            if (contacto.getId() != 0) {
                Entidad existente = servPersistencia.recuperarEntidad(contacto.getId());
                if (existente != null) {
                    System.err.println("Advertencia: Intentando registrar un ContactoIndividual que ya existe (ID: " + contacto.getId() + "). Se procederá a modificar.");
                    modificarContactoIndividual(contacto);
                    return;
                }
            }
            Entidad entidad = contactoIndividualToEntidad(contacto);
            entidad = servPersistencia.registrarEntidad(entidad);
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

            boolean propiedadMensajesModificada = false;

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
                    case "mensajesIds":
                        nuevoValor = contacto.getMensajes().stream()
                                             .map(m -> String.valueOf(m.getId()))
                                             .filter(idStr -> !idStr.equals("0"))
                                             .collect(Collectors.joining(","));
                        propiedadMensajesModificada = true;
                        break;
                    default:
                        continue;
                }
                String valorActual = prop.getValor();
                if (nuevoValor != null && !nuevoValor.equals(valorActual)) {
                    prop.setValor(nuevoValor);
                    servPersistencia.modificarPropiedad(prop);
                } else if (nuevoValor == null && valorActual != null && !valorActual.isEmpty()) {
                    prop.setValor("");
                    servPersistencia.modificarPropiedad(prop);
                }
            }

            if (!propiedadMensajesModificada) {
                String idsMensajes = contacto.getMensajes().stream()
                                             .map(m -> String.valueOf(m.getId()))
                                             .filter(idStr -> !idStr.equals("0"))
                                             .collect(Collectors.joining(","));
                if (!idsMensajes.isEmpty()) {
                    boolean exists = entidad.getPropiedades().stream().anyMatch(p -> p.getNombre().equals("mensajesIds"));
                    if (!exists) {
                        servPersistencia.anadirPropiedadEntidad(entidad, "mensajesIds", idsMensajes);
                    } else {
                        Propiedad existingProp = entidad.getPropiedades().stream().filter(p -> p.getNombre().equals("mensajesIds")).findFirst().orElse(null);
                        if (existingProp != null && !idsMensajes.equals(existingProp.getValor())) {
                            existingProp.setValor(idsMensajes);
                            servPersistencia.modificarPropiedad(existingProp);
                        }
                    }
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
