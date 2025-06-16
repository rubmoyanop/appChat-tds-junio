package umu.tds.appchat.modelo;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Clase que encapsula los datos necesarios para exportar un chat a PDF.
 */
public class DatosExportacionPDF {
    private final String nombreContacto;
    private final String nombreUsuarioExportador;
    private final LocalDateTime fechaExportacion;
    private final List<Mensaje> mensajes;

    /**
     * Constructor para crear los datos de exportación PDF.
     * @param nombreContacto Nombre del contacto o su móvil si no tiene nombre.
     * @param nombreUsuarioExportador Nombre del usuario que exporta el chat.
     * @param fechaExportacion Fecha y hora de la exportación.
     * @param mensajes Lista de mensajes filtrados y ordenados para incluir en el PDF.
     */
    public DatosExportacionPDF(String nombreContacto, String nombreUsuarioExportador, 
                              LocalDateTime fechaExportacion, List<Mensaje> mensajes) {
        this.nombreContacto = nombreContacto;
        this.nombreUsuarioExportador = nombreUsuarioExportador;
        this.fechaExportacion = fechaExportacion;
        this.mensajes = mensajes;
    }

    /**
     * @return El nombre del contacto para mostrar en el PDF.
     */
    public String getNombreContacto() { 
        return nombreContacto; 
    }

    /**
     * @return El nombre del usuario que exporta el chat.
     */
    public String getNombreUsuarioExportador() { 
        return nombreUsuarioExportador; 
    }

    /**
     * @return La fecha y hora de exportación.
     */
    public LocalDateTime getFechaExportacion() { 
        return fechaExportacion; 
    }

    /**
     * @return La lista de mensajes a incluir en el PDF.
     */
    public List<Mensaje> getMensajes() { 
        return mensajes; 
    }
}