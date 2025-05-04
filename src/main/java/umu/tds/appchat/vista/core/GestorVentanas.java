package umu.tds.appchat.vista.core;

import java.util.HashMap;
import java.util.Map;

import javax.swing.*;
import umu.tds.appchat.vista.pantallas.VentanaBienvenida;
import umu.tds.appchat.vista.pantallas.VentanaLogin;
import umu.tds.appchat.vista.pantallas.VentanaPrincipal; 
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
        registrarVentana(new VentanaPrincipal()); 
        
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
        
        // Ocultar la ventana actual si existe
        if (ventanaActual != null) {
             //Si vamos a LOGIN o REGISTRO desde BIENVENIDA, no ocultamos BIENVENIDA
            boolean ocultarActual = !(
                (tipo == TipoVentana.LOGIN || tipo == TipoVentana.REGISTRO) &&
                ventanaActual.getTipo() == TipoVentana.BIENVENIDA
            );
             //Si vamos a PRINCIPAL desde LOGIN o REGISTRO, ocultamos la anterior
            if (tipo == TipoVentana.PRINCIPAL && (ventanaActual.getTipo() == TipoVentana.LOGIN || ventanaActual.getTipo() == TipoVentana.REGISTRO)) {
                 ocultarActual = true;
            }
             //Si vamos a LOGIN desde REGISTRO (o viceversa), ocultamos la anterior
            if ((tipo == TipoVentana.LOGIN && ventanaActual.getTipo() == TipoVentana.REGISTRO) ||
                (tipo == TipoVentana.REGISTRO && ventanaActual.getTipo() == TipoVentana.LOGIN)) {
                 ocultarActual = true;
            }
             // Ocultar la ventana de bienvenida si vamos a la principal
            if (tipo == TipoVentana.PRINCIPAL && ventanaActual.getTipo() == TipoVentana.BIENVENIDA) {
                 ocultarActual = true;
            }


            if (ocultarActual) {
                ventanaActual.alOcultar();
                ventanaActual.getPanelPrincipal().setVisible(false);
            }
        }

        // Mostrar la nueva ventana
        ventanaActual = nuevaVentana;
        ventanaActual.alMostrar();
        JFrame frame = ventanaActual.getPanelPrincipal();

        // Centrar y hacer visible
        // Para LOGIN y REGISTRO, ya se centran al mostrarse como diálogos modales "flotantes"
        // Para BIENVENIDA y PRINCIPAL, centramos aquí.
        if (tipo == TipoVentana.BIENVENIDA || tipo == TipoVentana.PRINCIPAL) {
             frame.setLocationRelativeTo(null); // Centrar ventanas principales
             if (tipo == TipoVentana.BIENVENIDA) {
                 frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Bienvenida maximizada
             } else {
                 frame.setSize(800, 600); // Tamaño por defecto para Principal
             }
        } else {
             // Para Login y Registro, asegurarse de que estén centradas también
             frame.setLocationRelativeTo(null);
        }

        frame.setVisible(true);
        frame.toFront(); 
        frame.requestFocus();
    }
    
    public Ventana getVentanaActual() {
        return ventanaActual;
    }
}


