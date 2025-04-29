package umu.tds.appchat.vista.core;

import java.util.HashMap;
import java.util.Map;

import javax.swing.*;
import umu.tds.appchat.vista.pantallas.VentanaBienvenida;
import umu.tds.appchat.vista.pantallas.VentanaLogin;

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
        
        // Caso especial para LOGIN (mostrar encima de BIENVENIDA)
        if (tipo == TipoVentana.LOGIN) {
            // No ocultar la ventana de bienvenida, la dejamos visible de fondo
            JFrame loginFrame = nuevaVentana.getPanelPrincipal();
            nuevaVentana.alMostrar();
            
            // Mostrar login centrado en la pantalla
            loginFrame.setLocationRelativeTo(null);
            loginFrame.setVisible(true);
            
            // Definimos la ventana actual como la de login
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
