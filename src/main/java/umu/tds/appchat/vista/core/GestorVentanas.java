package umu.tds.appchat.vista.core;

import java.util.HashMap;
import java.util.Map;

import javax.swing.*;
import umu.tds.appchat.vista.pantallas.VentanaBienvenida;
import umu.tds.appchat.vista.pantallas.VentanaLogin;
import umu.tds.appchat.vista.pantallas.VentanaRegistro;

public enum GestorVentanas {
    INSTANCIA;
    
    private Map<TipoVentana, Ventana> ventanas;
    private Ventana ventanaActual;
    
    GestorVentanas() {
        ventanas = new HashMap<>();
    }
    
    public void inicializarAplicacion() {
        // Registrar las ventanas de la aplicación
        registrarVentana(new VentanaBienvenida());
        registrarVentana(new VentanaLogin());
        registrarVentana(new VentanaRegistro());
        
        // Mostrar la ventana inicial
        mostrarVentana(TipoVentana.BIENVENIDA);
    }
    
    public void registrarVentana(Ventana ventana) {
        ventanas.put(ventana.getTipo(), ventana);
    }
    
    public void mostrarVentana(TipoVentana tipo) {
        // Obtener la nueva ventana
        Ventana nuevaVentana = ventanas.get(tipo);
        
        if (nuevaVentana == null) {
            throw new IllegalArgumentException("La ventana solicitada no está registrada: " + tipo);
        }
        
        // Caso especial para LOGIN y REGISTRO (mostrar encima de BIENVENIDA)
        if (tipo == TipoVentana.LOGIN || tipo == TipoVentana.REGISTRO) {
            // Si estamos en LOGIN y queremos ir a REGISTRO, o viceversa, 
            // ocultar la ventana actual
            if (ventanaActual != null && 
                (ventanaActual.getTipo() == TipoVentana.LOGIN || 
                 ventanaActual.getTipo() == TipoVentana.REGISTRO)) {
                ventanaActual.alOcultar();
                ventanaActual.getPanelPrincipal().setVisible(false);
            }
            
            // No ocultar la ventana de bienvenida, la dejamos visible de fondo
            JFrame nuevaFrame = nuevaVentana.getPanelPrincipal();
            nuevaVentana.alMostrar();
            
            // Mostrar ventana centrada en la pantalla
            nuevaFrame.setLocationRelativeTo(null);
            nuevaFrame.setVisible(true);
            
            // Definimos la ventana actual como la nueva ventana
            ventanaActual = nuevaVentana;
        } else {
            // Para el resto de ventanas, comportamiento normal
            if (ventanaActual != null) {
                ventanaActual.alOcultar();
                ventanaActual.getPanelPrincipal().setVisible(false);
            }
            
            ventanaActual = nuevaVentana;
            ventanaActual.alMostrar();
            JFrame frame = ventanaActual.getPanelPrincipal();
            frame.setVisible(true);
            
            // Centrar en pantalla
            frame.setLocationRelativeTo(null);
        }
    }
    
    public Ventana getVentanaActual() {
        return ventanaActual;
    }
}
    

