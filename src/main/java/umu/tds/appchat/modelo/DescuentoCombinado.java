package umu.tds.appchat.modelo;

/**
 * Estrategia de descuento combinado que aplica el mejor descuento disponible.
 */
public class DescuentoCombinado implements Descuento {
    
    @Override
    public double calcularDescuento(Usuario usuario) {
        DescuentoPorFecha descuentoFecha = new DescuentoPorFecha();
        DescuentoPorMensaje descuentoMensaje = new DescuentoPorMensaje();
        
        double descuentoAntiguedad = descuentoFecha.calcularDescuento(usuario);
        double descuentoMensajes = descuentoMensaje.calcularDescuento(usuario);
        
        return Math.max(descuentoAntiguedad, descuentoMensajes);
    }
}
