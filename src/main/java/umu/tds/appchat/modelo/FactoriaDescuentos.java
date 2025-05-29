package umu.tds.appchat.modelo;

import java.util.Map;
import java.util.HashMap;
import java.util.function.Supplier;

/**
 * Factoría para crear instancias de estrategias de descuento.
*/
public class FactoriaDescuentos {
    private static final Map<TipoDescuento, Supplier<Descuento>> factoriaDescuentos = new HashMap<>();
    
    static {
        for (TipoDescuento tipo : TipoDescuento.values()) {
            factoriaDescuentos.put(tipo, () -> {
                try {
                    Class<?> clazz = Class.forName(tipo.getClassName());
                    return (Descuento) clazz.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    throw new RuntimeException("Error creando " + tipo.getClassName(), e);
                }
            });
        }
    }
    
    /**
     * Crea una instancia de EstrategiaDescuento según el tipo especificado.
     * @param tipo Tipo de descuento a crear
     * @return Instancia de EstrategiaDescuento correspondiente al tipo
     * @throws IllegalArgumentException Si el tipo no es soportado
     */
    public static Descuento crearDescuento(TipoDescuento tipo) {
        Supplier<Descuento> supplier = factoriaDescuentos.get(tipo);
        if (supplier == null) {
            throw new IllegalArgumentException("Tipo de descuento no soportado: " + tipo);
        }
        return supplier.get();
    }
}
