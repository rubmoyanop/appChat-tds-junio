package umu.tds.appchat.vista.core;

import java.util.HashMap;
import java.util.Map;

import javax.swing.*;
import umu.tds.appchat.vista.pantallas.VentanaBienvenida;

public enum GestorVentanas {
    INSTANCIA;
    
    private Map<TipoVentana, Ventana> ventanas;
    private Ventana ventanaActual;
    
    GestorVentanas() {
        ventanas = new HashMap<>();
    }
    
    public void inicializarAplicacion() {
        // Registrar solo la ventana de bienvenida
        registrarVentana(new VentanaBienvenida());
        
        // Mostrar la ventana inicial
        mostrarVentana(TipoVentana.BIENVENIDA);
    }
    
    public void registrarVentana(Ventana ventana) {
        ventanas.put(ventana.getTipo(), ventana);
    }
    
    public void mostrarVentana(TipoVentana tipo) {
        // Ocultar ventana actual si existe
        if (ventanaActual != null) {
            ventanaActual.alOcultar();
            ventanaActual.getPanelPrincipal().setVisible(false);
        }
        
        // Mostrar nueva ventana
        ventanaActual = ventanas.get(tipo);
        
        if (ventanaActual != null) {
            ventanaActual.alMostrar();
            JFrame frame = ventanaActual.getPanelPrincipal();
            frame.setVisible(true);
            
            // Centrar en pantalla
            frame.setLocationRelativeTo(null);
        } else {
            throw new IllegalArgumentException("La ventana solicitada no está registrada: " + tipo);
        }
    }
    
    public Ventana getVentanaActual() {
        return ventanaActual;
    }
}
