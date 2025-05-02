package umu.tds.appchat.vista.pantallas;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import umu.tds.appchat.controlador.AppChat;
import umu.tds.appchat.modelo.Usuario;
import umu.tds.appchat.vista.core.GestorVentanas;
import umu.tds.appchat.vista.core.TipoVentana;
import umu.tds.appchat.vista.core.Ventana;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Ventana principal de la aplicación después del login.
 */
public class VentanaPrincipal implements Ventana {
    private JFrame frame;
    private JPanel mainPanel;
    private JLabel lblNombreUsuario;
    private JLabel lblFotoUsuario; 

    public VentanaPrincipal() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("AppChat - Principal");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        frame.getContentPane().add(mainPanel);

        // Crear el panel de cabecera
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Panel central (para contenido futuro, como lista de chats, mensajes, etc.)
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(new Color(230, 230, 230)); // Un color de fondo diferente
        centerPanel.add(new JLabel("Contenido principal de la aplicación irá aquí"));
        mainPanel.add(centerPanel, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        // BoxLayout para alinear elementos horizontalmente
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.X_AXIS));
        headerPanel.setBackground(new Color(240, 240, 240));
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY), // Borde inferior
            new EmptyBorder(5, 10, 5, 10) // Padding
        ));

        // Botones a la izquierda
        JButton btnAddContacto = new JButton("Agregar nuevo contacto");
        JButton btnAddGrupo = new JButton("Crear nuevo grupo");
        JButton btnVerContactos = new JButton("Ver Contactos");
        JButton btnPremium = new JButton("Premium");
        JButton btnBuscarMensajes = new JButton("Buscar Mensajes");

        headerPanel.add(btnAddContacto);
        headerPanel.add(Box.createRigidArea(new Dimension(5, 0))); 
        headerPanel.add(btnAddGrupo);
        headerPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        headerPanel.add(btnVerContactos);
        headerPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        headerPanel.add(btnPremium);
        headerPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        headerPanel.add(btnBuscarMensajes);

        // Espacio flexible para empujar los elementos de la derecha
        headerPanel.add(Box.createHorizontalGlue());

        // Elementos a la derecha (Nombre, Foto, Logout)
        lblNombreUsuario = new JLabel("Nombre Usuario");
        lblNombreUsuario.setFont(new Font("Arial", Font.BOLD, 14));

        // Placeholder para la foto del usuario
        lblFotoUsuario = new JLabel();
        lblFotoUsuario.setPreferredSize(new Dimension(40, 30));
        lblFotoUsuario.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        lblFotoUsuario.setHorizontalAlignment(SwingConstants.CENTER);
        lblFotoUsuario.setText("Foto");

        JButton btnLogout = new JButton("Logout");
        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AppChat.INSTANCE.logout();
                GestorVentanas.INSTANCIA.mostrarVentana(TipoVentana.LOGIN);
            }
        });

        // Añadir elementos de la derecha
        headerPanel.add(lblNombreUsuario);
        headerPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        headerPanel.add(lblFotoUsuario);
        headerPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        headerPanel.add(btnLogout);

        // Configurar tooltips (opcional)
        btnAddContacto.setToolTipText("Añadir nuevo contacto individual");
        btnAddGrupo.setToolTipText("Crear nuevo grupo");
        btnVerContactos.setToolTipText("Mostrar lista de contactos y grupos");
        btnPremium.setToolTipText("Gestionar suscripción Premium");
        btnBuscarMensajes.setToolTipText("Buscar mensajes en todas las conversaciones");
        btnLogout.setToolTipText("Cerrar sesión");

        return headerPanel;
    }

    @Override
    public JFrame getPanelPrincipal() {
        return frame;
    }

    @Override
    public void alMostrar() {
        // Actualizar datos del usuario cuando la ventana se muestra
        Usuario usuario = AppChat.INSTANCE.getUsuarioActual(); // Necesitarás este método en AppChat
        if (usuario != null) {
            lblNombreUsuario.setText(usuario.getNombre());
            // Simply set the text for the photo placeholder
            lblFotoUsuario.setText("Foto");
            lblFotoUsuario.setIcon(null); // Ensure no icon is displayed
        } else {
            // Manejar caso donde no hay usuario (aunque no debería pasar si se llega desde login)
            lblNombreUsuario.setText("Usuario Desconocido"); // Corrected placeholder text
            lblFotoUsuario.setText("Foto"); // Consistent placeholder
            lblFotoUsuario.setIcon(null);
        }
    }

    @Override
    public void alOcultar() {
        // No hay acciones especiales al ocultar por ahora
    }

    @Override
    public TipoVentana getTipo() {
        return TipoVentana.PRINCIPAL;
    }
}
