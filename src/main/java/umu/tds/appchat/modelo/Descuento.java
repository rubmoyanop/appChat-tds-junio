package umu.tds.appchat.modelo;

/**
 * Interfaz para las estrategias de cálculo de descuentos.
 */
public interface Descuento {
    /**
     * Calcula el porcentaje de descuento para un usuario dado.
     * @param usuario El usuario para el cual calcular el descuento
     * @return El porcentaje de descuento (0.0 a 1.0)
     */
    double calcularDescuento(Usuario usuario);
}
