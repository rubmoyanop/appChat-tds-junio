package umu.tds.appchat.vista.pantallas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import umu.tds.appchat.vista.core.GestorVentanas;
import umu.tds.appchat.vista.core.TipoVentana;
import umu.tds.appchat.vista.core.Ventana;

/**
 * Ventana de login que se abre encima de la ventana de bienvenida
 */
public class VentanaLogin implements Ventana {
    private JFrame frame;
    private JTextField txtTelefono;
    private JPasswordField txtContrasena;
    
    public VentanaLogin() {
        initialize();
    }
    
    private void initialize() {
        frame = new JFrame();
        frame.setUndecorated(true); // Sin bordes de ventana
        frame.setSize(400, 350); // Aumentado el tamaño para acomodar el botón de registro
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Controlamos el cierre manualmente
        
        // Panel principal con borde
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(100, 100, 100), 1),
            new EmptyBorder(10, 10, 10, 10)));
        mainPanel.setBackground(Color.WHITE);
        frame.getContentPane().add(mainPanel);
        
        // Panel de título
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setBackground(new Color(240, 240, 240));
        titlePanel.setBorder(new EmptyBorder(5, 10, 5, 10));
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        
        JLabel lblTitle = new JLabel("Iniciar sesión");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        titlePanel.add(lblTitle, BorderLayout.WEST);
        
        JButton btnClose = new JButton("X");
        btnClose.setFocusPainted(false);
        btnClose.setBorderPainted(false);
        btnClose.setContentAreaFilled(false);
        btnClose.setFont(new Font("Arial", Font.BOLD, 14));
        btnClose.setForeground(Color.GRAY);
        btnClose.addActionListener(e -> {
            // Volver a la ventana de bienvenida
            GestorVentanas.INSTANCIA.mostrarVentana(TipoVentana.BIENVENIDA);
        });
        titlePanel.add(btnClose, BorderLayout.EAST);
        
        // Panel central con formulario
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Etiqueta Teléfono
        JLabel lblTelefono = new JLabel("Teléfono:");
        lblTelefono.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(lblTelefono, gbc);
        
        // Campo Teléfono
        txtTelefono = new JTextField();
        txtTelefono.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        formPanel.add(txtTelefono, gbc);
        
        // Etiqueta Contraseña
        JLabel lblContrasena = new JLabel("Contraseña:");
        lblContrasena.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        formPanel.add(lblContrasena, gbc);
        
        // Campo Contraseña
        txtContrasena = new JPasswordField();
        txtContrasena.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        formPanel.add(txtContrasena, gbc);
        
        // Panel para botones - Mantener centrado
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        JButton btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogin.setBackground(new Color(66, 133, 244));
        btnLogin.setForeground(Color.BLACK);
        btnLogin.setMargin(new Insets(5, 15, 10, 15));
        
        // Asegurar que el botón sea opaco para mostrar el color de fondo
        btnLogin.setOpaque(true);
        btnLogin.setContentAreaFilled(true);
        
        btnLogin.setFocusPainted(false);
        btnLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String telefono = txtTelefono.getText();
                String contrasena = new String(txtContrasena.getPassword());
                
                if (telefono.isEmpty() || contrasena.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, 
                            "Por favor, complete todos los campos",
                            "Campos incompletos", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                // Aquí iría la validación real de credenciales
                JOptionPane.showMessageDialog(frame, 
                        "En desarrollo",
                        "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        buttonPanel.add(btnLogin);
        
        // Añadir un panel separado para el botón de registro en la parte inferior
        JPanel registroPanel = new JPanel();
        registroPanel.setBackground(Color.WHITE);
        registroPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        registroPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        
        // Etiqueta para texto "¿No tienes cuenta?"
        JLabel lblNoAccount = new JLabel("¿No tienes cuenta?");
        lblNoAccount.setFont(new Font("Arial", Font.PLAIN, 12));
        registroPanel.add(lblNoAccount);
        
        // Botón de Registro (estilo enlace) - Modificar para abrir ventana de registro
        JButton btnRegistro = new JButton("Regístrate aquí");
        btnRegistro.setFont(new Font("Arial", Font.BOLD, 12));
        btnRegistro.setBorderPainted(false);
        btnRegistro.setContentAreaFilled(false);
        btnRegistro.setForeground(new Color(66, 133, 244));
        btnRegistro.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegistro.setFocusPainted(false);
        btnRegistro.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Abrir la ventana de registro
                GestorVentanas.INSTANCIA.mostrarVentana(TipoVentana.REGISTRO);
            }
        });
        registroPanel.add(btnRegistro);
        
        // Añadir el panel de registro debajo del panel de botones
        mainPanel.add(registroPanel, BorderLayout.SOUTH);
        
        // Reorganizar para que ambos paneles aparezcan
        mainPanel.remove(buttonPanel);
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setBackground(Color.WHITE);
        southPanel.add(buttonPanel, BorderLayout.NORTH);
        southPanel.add(registroPanel, BorderLayout.SOUTH);
        mainPanel.add(southPanel, BorderLayout.SOUTH);
        
        // Para hacer dragable la ventana
        FrameDragListener frameDragListener = new FrameDragListener(frame);
        titlePanel.addMouseListener(frameDragListener);
        titlePanel.addMouseMotionListener(frameDragListener);
    }

    @Override
    public JFrame getPanelPrincipal() {
        return frame;
    }

    @Override
    public void alMostrar() {
        txtTelefono.setText("");
        txtContrasena.setText("");
        txtTelefono.requestFocus();
    }

    @Override
    public void alOcultar() {
        // Limpiar datos sensibles
        txtContrasena.setText("");
    }

    @Override
    public TipoVentana getTipo() {
        return TipoVentana.LOGIN;
    }
    
    // Clase interna para permitir arrastrar la ventana sin bordes
    private class FrameDragListener extends MouseAdapter {
        private final JFrame frame;
        private Point mouseDownCompCoords = null;

        public FrameDragListener(JFrame frame) {
            this.frame = frame;
        }

        public void mousePressed(java.awt.event.MouseEvent e) {
            mouseDownCompCoords = e.getPoint();
        }

        public void mouseReleased(java.awt.event.MouseEvent e) {
            mouseDownCompCoords = null;
        }

        public void mouseDragged(java.awt.event.MouseEvent e) {
            if (mouseDownCompCoords != null) {
                Point currCoords = e.getLocationOnScreen();
                frame.setLocation(currCoords.x - mouseDownCompCoords.x, 
                                 currCoords.y - mouseDownCompCoords.y);
            }
        }
    }
}
