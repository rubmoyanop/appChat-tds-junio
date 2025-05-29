package umu.tds.appchat.modelo;

import java.time.LocalDateTime;

/**
 * Estrategia de descuento basada en la cantidad de mensajes enviados por el usuario en el último mes.
 */
public class DescuentoPorMensaje implements Descuento {
    
    @Override
    public double calcularDescuento(Usuario usuario) {
        LocalDateTime haceUnMes = LocalDateTime.now().minusMonths(1);
        
        long mensajesUltimoMes = usuario.getContactos().stream()
            .flatMap(contacto -> contacto.getMensajes().stream())
            .filter(mensaje -> mensaje.getTipo() == TipoMensaje.ENVIADO)
            .filter(mensaje -> mensaje.getFechaHora().isAfter(haceUnMes))
            .count();
        
        if (mensajesUltimoMes >= 50) return 0.25; 
        if (mensajesUltimoMes >= 20) return 0.15; 
        if (mensajesUltimoMes >= 5) return 0.10;  
        return 0.0; 
    }
}
