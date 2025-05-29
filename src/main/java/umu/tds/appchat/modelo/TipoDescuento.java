package umu.tds.appchat.modelo;

/**
 * Enumerado que define los tipos de descuento disponibles en el sistema.
 */
public enum TipoDescuento {
    ANTIGUEDAD("umu.tds.appchat.modelo.DescuentoPorFecha"),
    MENSAJES("umu.tds.appchat.modelo.DescuentoPorMensaje"),
    COMBINADO("umu.tds.appchat.modelo.DescuentoCombinado");
    
    private final String className;
    
    TipoDescuento(String className) {
        this.className = className;
    }
    
    /**
     * Obtiene el nombre de la clase asociada al tipo de descuento.
     * @return Nombre completo de la clase
     */
    public String getClassName() {
        return className;
    }
}
