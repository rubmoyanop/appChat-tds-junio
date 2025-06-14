package umu.tds.appchat.persistencia;

import beans.Entidad;
import beans.Propiedad;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import umu.tds.appchat.dao.DAOExcepcion;
import umu.tds.appchat.dao.MensajeDAO;
import umu.tds.appchat.modelo.Mensaje;
import umu.tds.appchat.modelo.TipoMensaje;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.LinkedList;
import java.util.List;

public class MensajeDAO_TDS implements MensajeDAO {

    private ServicioPersistencia servPersistencia;
    private PoolMensajes poolMensajes;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public MensajeDAO_TDS() {
        this.servPersistencia = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
        this.poolMensajes = PoolMensajes.INSTANCE;
    }

    /**
     * Convierte un objeto Mensaje a una Entidad para persistencia.
     */
    private Entidad mensajeToEntidad(Mensaje mensaje) {
        Entidad entidad = new Entidad();
        entidad.setNombre("Mensaje");

        List<Propiedad> propiedades = new LinkedList<>();
        propiedades.add(new Propiedad("texto", mensaje.isEmoji() ? "" : mensaje.getTexto())); // Store empty string for emojis
        String fechaHoraStr = mensaje.getFechaHora() != null ? mensaje.getFechaHora().format(DATE_TIME_FORMATTER) : "";
        propiedades.add(new Propiedad("fechaHora", fechaHoraStr));
        propiedades.add(new Propiedad("tipo", mensaje.getTipo().name()));
        propiedades.add(new Propiedad("codigoEmoji", String.valueOf(mensaje.getCodigoEmoji()))); // Store emoji code
        propiedades.add(new Propiedad("mensajeDeGrupo", String.valueOf(mensaje.isMensajeDeGrupo())));


        entidad.setPropiedades(propiedades);
        return entidad;
    }

    /**
     * Convierte una Entidad a un objeto Mensaje.
     */
    private Mensaje entidadToMensaje(Entidad entidad) throws DAOExcepcion {
        if (entidad == null) {
            return null;
        }
        if (!"Mensaje".equals(entidad.getNombre())) {
            throw new DAOExcepcion("Entidad no es del tipo Mensaje.");
        }
        if (entidad.getId() == 0) {
            throw new DAOExcepcion("Entidad Mensaje sin ID.");
        }

        // Comprobar si ya está en el pool
        if (poolMensajes.contains(entidad.getId())) {
            return poolMensajes.getMensaje(entidad.getId());
        }

        String texto = null;
        LocalDateTime fechaHora = null;
        TipoMensaje tipo = null;
        int codigoEmoji = -1; // -1 indica que no es un emoji
        boolean mensajeDeGrupo = false; 

        try {
            texto = servPersistencia.recuperarPropiedadEntidad(entidad, "texto");
            String fechaHoraStr = servPersistencia.recuperarPropiedadEntidad(entidad, "fechaHora");
            if (fechaHoraStr != null && !fechaHoraStr.isEmpty()) {
                try {
                    fechaHora = LocalDateTime.parse(fechaHoraStr, DATE_TIME_FORMATTER);
                } catch (DateTimeParseException e) {
                    System.err.println("Error parseando fechaHora para mensaje ID " + entidad.getId() + ": " + fechaHoraStr);
                    throw new DAOExcepcion("Error parseando fechaHora para mensaje ID " + entidad.getId(), e);
                }
            }
            String tipoStr = servPersistencia.recuperarPropiedadEntidad(entidad, "tipo");
            if (tipoStr != null) {
                try {
                    tipo = TipoMensaje.valueOf(tipoStr);
                } catch (IllegalArgumentException e) {
                    System.err.println("Error parseando TipoMensaje para mensaje ID " + entidad.getId() + ": " + tipoStr);
                    throw new DAOExcepcion("Error parseando TipoMensaje para mensaje ID " + entidad.getId(), e);
                }
            }
            String codigoEmojiStr = servPersistencia.recuperarPropiedadEntidad(entidad, "codigoEmoji");
            if (codigoEmojiStr != null && !codigoEmojiStr.isEmpty()) {
                try {
                    codigoEmoji = Integer.parseInt(codigoEmojiStr);
                } catch (NumberFormatException e) {
                    System.err.println("Error parseando codigoEmoji para mensaje ID " + entidad.getId() + ": " + codigoEmojiStr);
                }
            }
            String mensajeDeGrupoStr = servPersistencia.recuperarPropiedadEntidad(entidad, "mensajeDeGrupo");
            if (mensajeDeGrupoStr != null) {
                try{
                    mensajeDeGrupo = Boolean.parseBoolean(mensajeDeGrupoStr);
                } catch (Exception e){
                    System.err.println("Error parseando mensajeDeGrupo para mensaje ID " + entidad.getId() + ": " + mensajeDeGrupoStr);
                    throw new DAOExcepcion("Error parseando mensajeDeGrupo para mensaje ID " + entidad.getId(), e);
                }
            }

        } catch (Exception e) {
            throw new DAOExcepcion("Error al recuperar propiedad de Entidad Mensaje con ID: " + entidad.getId(), e);
        }

        if (fechaHora == null || tipo == null) {
             throw new DAOExcepcion("Datos incompletos (fechaHora o tipo) al recuperar Mensaje con ID: " + entidad.getId());
        }

        Mensaje mensaje;
        if (codigoEmoji != -1) {
            mensaje = new Mensaje(codigoEmoji, fechaHora, tipo);
        } else {
            mensaje = new Mensaje(texto != null ? texto : "", fechaHora, tipo);
        }
        mensaje.setId(entidad.getId());
        mensaje.setMensajeDeGrupo(mensajeDeGrupo);

        // Añadir al pool
        poolMensajes.addMensaje(mensaje);

        return mensaje;
    }

    @Override
    public void registrarMensaje(Mensaje mensaje) throws DAOExcepcion {
        Entidad entidadMensaje;
        try {
            if (mensaje.getId() != 0) {
                entidadMensaje = servPersistencia.recuperarEntidad(mensaje.getId());
                if (entidadMensaje != null) {
                    System.err.println("MensajeDAO_TDS: Intentando registrar un mensaje que ya existe (ID: " + mensaje.getId() + ").");
                    return;
                }
            }
            entidadMensaje = mensajeToEntidad(mensaje);
            entidadMensaje = servPersistencia.registrarEntidad(entidadMensaje);
            mensaje.setId(entidadMensaje.getId());
            poolMensajes.addMensaje(mensaje); // Añadir al pool después de registrar y obtener ID
        } catch (Exception e) {
            throw new DAOExcepcion("Error al registrar el Mensaje: " + e.getMessage(), e);
        }
    }

    @Override
    public Mensaje recuperarMensaje(int id) throws DAOExcepcion {
        // Primero buscamos en el pool
        if (poolMensajes.contains(id)) {
            return poolMensajes.getMensaje(id);
        }
        try {
            Entidad entidad = servPersistencia.recuperarEntidad(id);
            return entidadToMensaje(entidad); 
        } catch (Exception e) {
            throw new DAOExcepcion("Error al recuperar Mensaje con ID: " + id, e);
        }
    }

    @Override
    public void modificarMensaje(Mensaje mensaje) throws DAOExcepcion {
        try {
            Entidad entidad = servPersistencia.recuperarEntidad(mensaje.getId());
            if (entidad == null) {
                throw new DAOExcepcion("Mensaje no encontrado para modificar (ID: " + mensaje.getId() + ")");
            }

            servPersistencia.eliminarPropiedadEntidad(entidad, "texto");
            servPersistencia.anadirPropiedadEntidad(entidad, "texto", mensaje.isEmoji() ? "" : mensaje.getTexto());

            servPersistencia.eliminarPropiedadEntidad(entidad, "fechaHora");
            String fechaHoraStr = mensaje.getFechaHora() != null ? mensaje.getFechaHora().format(DATE_TIME_FORMATTER) : "";
            servPersistencia.anadirPropiedadEntidad(entidad, "fechaHora", fechaHoraStr);

            servPersistencia.eliminarPropiedadEntidad(entidad, "codigoEmoji");
            servPersistencia.anadirPropiedadEntidad(entidad, "codigoEmoji", String.valueOf(mensaje.getCodigoEmoji()));

            servPersistencia.eliminarPropiedadEntidad(entidad, "tipo");
            servPersistencia.anadirPropiedadEntidad(entidad, "tipo", mensaje.getTipo().name());

            servPersistencia.eliminarPropiedadEntidad(entidad, "mensajeDeGrupo");
            servPersistencia.anadirPropiedadEntidad(entidad, "mensajeDeGrupo", String.valueOf(mensaje.isMensajeDeGrupo()));

            // Actualizar en el pool si existe
            if (poolMensajes.contains(mensaje.getId())) {
                poolMensajes.addMensaje(mensaje);
            }

        } catch (Exception e) {
            throw new DAOExcepcion("Error al modificar el Mensaje con ID: " + mensaje.getId(), e);
        }
    }

    @Override
    public void borrarMensaje(Mensaje mensaje) throws DAOExcepcion {
        try {
            Entidad entidad = servPersistencia.recuperarEntidad(mensaje.getId());
            if (entidad != null) {
                servPersistencia.borrarEntidad(entidad);
                // Eliminar del pool
                poolMensajes.removeMensaje(mensaje.getId());
            } else {
                System.err.println("MensajeDAO_TDS: Intentando borrar un mensaje que no existe (ID: " + mensaje.getId() + ")");
            }
        } catch (Exception e) {
            throw new DAOExcepcion("Error al borrar el Mensaje con ID: " + mensaje.getId(), e);
        }
    }

    @Override
    public List<Mensaje> recuperarTodosMensajes() throws DAOExcepcion {
        List<Mensaje> todos = new LinkedList<>();
        try {
            List<Entidad> entidades = servPersistencia.recuperarEntidades("Mensaje");
            for (Entidad entidad : entidades) {
                todos.add(entidadToMensaje(entidad));
            }
        } catch (Exception e) {
            throw new DAOExcepcion("Error al recuperar todos los Mensajes", e);
        }
        return todos;
    }
}
