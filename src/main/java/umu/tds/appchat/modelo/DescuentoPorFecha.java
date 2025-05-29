package umu.tds.appchat.modelo;

import java.time.LocalDate;
import java.time.Period;

/**
* Estrategia de descuento basada en la antigüedad del usuario.
* Calcula un descuento basado en la fecha de registro del usuario.
*/
public class DescuentoPorFecha implements Descuento {
    
    @Override
    public double calcularDescuento(Usuario usuario) {
        LocalDate fechaRegistro = usuario.getFechaRegistro(); 
        LocalDate ahora = LocalDate.now();
        Period periodo = Period.between(fechaRegistro, ahora);
        
        int meses = periodo.getYears() * 12 + periodo.getMonths();
        
        if (meses >= 12) return 0.20; 
        if (meses >= 6) return 0.15;  
        if (meses >= 3) return 0.10;  
        return 0.0; 
    }
}
