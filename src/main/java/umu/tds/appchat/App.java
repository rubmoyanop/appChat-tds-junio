package umu.tds.appchat;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import umu.tds.appchat.vista.core.GestorVentanas;

/**
 * Clase principal que inicia la aplicación AppChat
 */
public class App
{
    public static void main( String[] args )
    {
        // Ejecutar en el EDT (Event Dispatch Thread)
        SwingUtilities.invokeLater(() -> {
            try {
                // Establecer un Look and Feel más moderno
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                System.err.println("No se pudo establecer el look and feel del sistema: " + e.getMessage());
                // Continuar con el Look and Feel por defecto
            }
            
            // Inicializar la aplicación con la ventana de bienvenida
            GestorVentanas.INSTANCIA.inicializarAplicacion();
            //AppChatDriver.main(args);
        });
    }
}
