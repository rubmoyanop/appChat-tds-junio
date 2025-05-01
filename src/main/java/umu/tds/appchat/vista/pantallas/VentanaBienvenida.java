package umu.tds.appchat.vista.pantallas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import umu.tds.appchat.vista.core.GestorVentanas;
import umu.tds.appchat.vista.core.TipoVentana;
import umu.tds.appchat.vista.core.Ventana;

/**
 * Ventana principal que se muestra al iniciar la aplicación.
 * Ocupa toda la pantalla y muestra un mensaje de bienvenida.
 */
public class VentanaBienvenida implements Ventana {
    private JFrame frame;
    
    public VentanaBienvenida() {
        initialize();
    }
    
    private void initialize() {
        frame = new JFrame();
        frame.setTitle("AppChat - Bienvenido");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Configurar para pantalla completa
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        // Panel principal con BorderLayout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 250));
        frame.getContentPane().add(mainPanel);
        
        // Panel central con mensaje de bienvenida (usando BoxLayout para centrar verticalmente)
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(100, 50, 100, 50));
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        // Agregar espacio flexible arriba
        contentPanel.add(Box.createVerticalGlue());
        
        // Título principal
        JLabel lblTitle = new JLabel("Bienvenido a AppChat");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 48));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(lblTitle);
        
        // Espacio entre elementos
        contentPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        
        // Mensaje descriptivo
        JLabel lblDescription = new JLabel("Tu aplicación de mensajería simple y segura");
        lblDescription.setFont(new Font("Arial", Font.PLAIN, 24));
        lblDescription.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(lblDescription);
        
        // Espacio entre elementos
        contentPanel.add(Box.createRigidArea(new Dimension(0, 60)));
        
        // Botón para iniciar sesión
        JButton btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setFont(new Font("Arial", Font.BOLD, 16));
        btnLogin.setBackground(new Color(66, 133, 244));
        btnLogin.setForeground(Color.BLACK);
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.setMaximumSize(new Dimension(200, 40));
        btnLogin.setFocusPainted(false);
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Abrir la ventana de login
                GestorVentanas.INSTANCIA.mostrarVentana(TipoVentana.LOGIN);
            }
        });
        contentPanel.add(btnLogin);
        
        // Espacio entre elementos
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Mensaje de estado
        JLabel lblStatus = new JLabel("Versión 1.0 - Desarrollo");
        lblStatus.setFont(new Font("Arial", Font.ITALIC, 16));
        lblStatus.setForeground(new Color(100, 100, 100));
        lblStatus.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(lblStatus);
        
        // Agregar espacio flexible abajo
        contentPanel.add(Box.createVerticalGlue());
    }

    @Override
    public JFrame getPanelPrincipal() {
        return frame;
    }

    @Override
    public void alMostrar() {
        // No hay operaciones especiales al mostrar
    }

    @Override
    public void alOcultar() {
        // No hay operaciones especiales al ocultar
    }

    @Override
    public TipoVentana getTipo() {
        return TipoVentana.BIENVENIDA;
    }
}
