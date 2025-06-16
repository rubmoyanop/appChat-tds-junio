package umu.tds.appchat.modelo;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Clase utilitaria para generar archivos PDF de exportación de chats.
 */
public class GeneradorPDF {
    
    private static final DateTimeFormatter FORMATTER_FECHA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    
    // Información de la tabla 
    private record InfoRow(String etiqueta, String valor) {}
    
    /**
     * Genera un archivo PDF con la conversación de chat.
     * @param datos Los datos de exportación que contienen toda la información necesaria.
     * @param rutaArchivo La ruta completa donde guardar el archivo PDF.
     * @throws IOException Si ocurre un error al crear o escribir el archivo.
     */
    public static void generarPDF(DatosExportacionPDF datos, String rutaArchivo) throws IOException {
        try {
            // Crear el documento PDF
            PdfWriter writer = new PdfWriter(rutaArchivo);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);
            
            // Configurar fuentes
            PdfFont fontRegular = PdfFontFactory.createFont(StandardFonts.HELVETICA);
            PdfFont fontBold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
            
            // Título del documento
            document.add(new Paragraph("Exportación de Chat")
                .setFont(fontBold)
                .setFontSize(18)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(20));
            
            // Información del chat usando streams
            Table infoTable = crearTablaInformacion(datos, fontRegular, fontBold);
            document.add(infoTable);
            
            // Línea separadora
            document.add(new Paragraph("─".repeat(80))
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(15));
            
            // Título de mensajes
            document.add(new Paragraph("Historial de Mensajes")
                .setFont(fontBold)
                .setFontSize(14)
                .setMarginBottom(10));
            
            // Crear y poblar tabla de mensajes
            Table mensajesTable = crearTablaMensajes(datos, fontRegular, fontBold);
            document.add(mensajesTable);
            
            // Cerrar el documento
            document.close();
            
        } catch (FileNotFoundException e) {
            throw new IOException("No se pudo crear el archivo en la ruta especificada: " + rutaArchivo, e);
        } catch (Exception e) {
            throw new IOException("Error al generar el PDF: " + e.getMessage(), e);
        }
    }
    
    /**
     * Crea la tabla de información del chat
     */
    private static Table crearTablaInformacion(DatosExportacionPDF datos, PdfFont fontRegular, PdfFont fontBold) {
        Table infoTable = new Table(2);
        infoTable.setWidth(UnitValue.createPercentValue(100));
        infoTable.setMarginBottom(20);
        
        // Crear lista de información 
        List<InfoRow> infoRows = List.of(
            new InfoRow("Contacto:", datos.getNombreContacto()),
            new InfoRow("Usuario:", datos.getNombreUsuarioExportador()),
            new InfoRow("Fecha de exportación:", datos.getFechaExportacion().format(FORMATTER_FECHA_HORA)),
            new InfoRow("Total de mensajes:", String.valueOf(datos.getMensajes().size()))
        );
        
        infoRows.stream()
            .forEach(row -> {
                infoTable.addCell(new Cell().add(new Paragraph(row.etiqueta()).setFont(fontBold))
                    .setBorder(null));
                infoTable.addCell(new Cell().add(new Paragraph(row.valor()).setFont(fontRegular))
                    .setBorder(null));
            });
        
        return infoTable;
    }
    
    /**
     * Crea la tabla de mensajes 
     */
    private static Table crearTablaMensajes(DatosExportacionPDF datos, PdfFont fontRegular, PdfFont fontBold) {
        Table mensajesTable = new Table(3);
        mensajesTable.setWidth(UnitValue.createPercentValue(100));
        
        // Crear cabeceras 
        List<String> headers = List.of("Fecha/Hora", "Emisor", "Mensaje");
        headers.stream()
            .map(header -> new Cell().add(new Paragraph(header).setFont(fontBold))
                .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                .setTextAlignment(TextAlignment.CENTER))
            .forEach(mensajesTable::addHeaderCell);
        
        // Procesar mensajes 
        datos.getMensajes().stream()
            .forEach(mensaje -> {
                // Fecha y hora
                mensajesTable.addCell(new Cell().add(new Paragraph(mensaje.getFechaHora().format(FORMATTER_FECHA_HORA))
                    .setFont(fontRegular)
                    .setFontSize(9)));
                
                // Emisor con color
                String emisor = (mensaje.getTipo() == TipoMensaje.ENVIADO) ? 
                    datos.getNombreUsuarioExportador() : datos.getNombreContacto();
                
                Cell celdaEmisor = new Cell().add(new Paragraph(emisor)
                    .setFont(fontRegular)
                    .setFontSize(10))
                    .setBackgroundColor(mensaje.getTipo() == TipoMensaje.ENVIADO ? 
                        ColorConstants.CYAN : ColorConstants.GREEN);
                mensajesTable.addCell(celdaEmisor);
                
                // Contenido del mensaje
                String contenido = mensaje.isEmoji() ? 
                    getFormatoEmoji(mensaje.getCodigoEmoji()) : 
                    mensaje.getTexto();
                
                mensajesTable.addCell(new Cell().add(new Paragraph(contenido)
                    .setFont(fontRegular)
                    .setFontSize(10)));
            });
        
        return mensajesTable;
    }
    
    /**
     * Convierte un código de emoji a su representación de texto
     * @param codigoEmoji El código del emoji.
     * @return La representación textual del emoji.
     */
    private static String getFormatoEmoji(int codigoEmoji) {
        return "Emoji #" + codigoEmoji;
    }
}