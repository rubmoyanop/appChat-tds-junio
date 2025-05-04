package umu.tds.appchat.vista.pantallas;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import umu.tds.appchat.controlador.AppChat;
import umu.tds.appchat.modelo.Contacto;        
import umu.tds.appchat.modelo.Usuario;
import umu.tds.appchat.vista.componentes.PanelContactos;
import umu.tds.appchat.vista.componentes.PanelMensajes;
import umu.tds.appchat.vista.core.GestorVentanas;
import umu.tds.appchat.vista.core.TipoVentana;
import umu.tds.appchat.vista.core.Ventana;
import umu.tds.appchat.modelo.ContactoIndividual;

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
    private PanelContactos panelContactos;
    private PanelMensajes panelMensajes;
    private Contacto contactoSeleccionado;  

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

        // Crear el panel de la izquierda (Contactos)
        panelContactos = new PanelContactos();

        // Crear el panel de la derecha (Mensajes)
        panelMensajes = new PanelMensajes();

        // Crear el JSplitPane para dividir los dos paneles
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelContactos, panelMensajes);
        splitPane.setResizeWeight(0.25);
        splitPane.setEnabled(false);
        splitPane.setBorder(null);

        mainPanel.add(splitPane, BorderLayout.CENTER);

        // Al cambiar selección, limpiar y cargar chat del contacto
        panelContactos.getContactosList().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Contacto c = panelContactos.getContactosList().getSelectedValue();
                this.contactoSeleccionado = c;
                mostrarMensajesDeContacto();
            }
        });
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.X_AXIS));
        headerPanel.setBackground(new Color(240, 240, 240));
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
            new EmptyBorder(5, 10, 5, 10)
        ));

        // Botones a la izquierda
        JButton btnAddContacto = new JButton("Agregar nuevo contacto");
        JButton btnAddGrupo = new JButton("Crear nuevo grupo");
        JButton btnVerContactos = new JButton("Ver Contactos");
        JButton btnPremium = new JButton("Premium");
        JButton btnBuscarMensajes = new JButton("Buscar Mensajes");

        btnAddContacto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarDialogoAgregarContacto();
            }
        });

        headerPanel.add(btnAddContacto);
        headerPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        headerPanel.add(btnAddGrupo);
        headerPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        headerPanel.add(btnVerContactos);
        headerPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        headerPanel.add(btnPremium);
        headerPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        headerPanel.add(btnBuscarMensajes);

        headerPanel.add(Box.createHorizontalGlue());

        // Elementos a la derecha (Nombre, Foto, Logout)
        lblNombreUsuario = new JLabel("Nombre Usuario");
        lblNombreUsuario.setFont(new Font("Arial", Font.BOLD, 14));

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

        headerPanel.add(lblNombreUsuario);
        headerPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        headerPanel.add(lblFotoUsuario);
        headerPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        headerPanel.add(btnLogout);

        btnAddContacto.setToolTipText("Añadir nuevo contacto individual");
        btnAddGrupo.setToolTipText("Crear nuevo grupo");
        btnVerContactos.setToolTipText("Mostrar lista de contactos y grupos");
        btnPremium.setToolTipText("Gestionar suscripción Premium");
        btnBuscarMensajes.setToolTipText("Buscar mensajes en todas las conversaciones");
        btnLogout.setToolTipText("Cerrar sesión");

        return headerPanel;
    }

    private void mostrarDialogoAgregarContacto() {
        JDialog dialogo = new JDialog(frame, "Agregar Nuevo Contacto", true);
        dialogo.setSize(350, 200);
        dialogo.setLocationRelativeTo(frame);
        dialogo.setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(new JLabel("Nombre:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        JTextField txtNombreContacto = new JTextField();
        formPanel.add(txtNombreContacto, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Teléfono:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        JTextField txtTelefonoContacto = new JTextField();
        formPanel.add(txtTelefonoContacto, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnCancelar = new JButton("Cancelar");
        JButton btnAceptar = new JButton("Aceptar");

        btnCancelar.addActionListener(ev -> dialogo.dispose());

        btnAceptar.addActionListener(ev -> {
            String nombre = txtNombreContacto.getText().trim();
            String telefono = txtTelefonoContacto.getText().trim();

            if (nombre.isEmpty() || telefono.isEmpty()) {
                JOptionPane.showMessageDialog(dialogo, "Nombre y Teléfono son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                boolean exito = AppChat.INSTANCE.agregarContactoIndividual(nombre, telefono);
                if (exito) {
                    JOptionPane.showMessageDialog(dialogo, "Contacto agregado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    dialogo.dispose();
                    panelContactos.actualizarListaContactos();
                } else {
                    JOptionPane.showMessageDialog(dialogo, "No existe ningún usuario registrado con ese teléfono.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(dialogo, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialogo, "Error inesperado al agregar contacto.", "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        buttonPanel.add(btnCancelar);
        buttonPanel.add(btnAceptar);

        dialogo.add(formPanel, BorderLayout.CENTER);
        dialogo.add(buttonPanel, BorderLayout.SOUTH);
        dialogo.setVisible(true);
    }

    @Override
    public JFrame getPanelPrincipal() {
        return frame;
    }

    @Override
    public void alMostrar() {
        Usuario usuario = AppChat.INSTANCE.getUsuarioActual();
        if (usuario != null) {
            lblNombreUsuario.setText(usuario.getNombre());
            lblFotoUsuario.setText("Foto");
            lblFotoUsuario.setIcon(null);
        } else {
            lblNombreUsuario.setText("Usuario Desconocido");
            lblFotoUsuario.setText("Foto");
            lblFotoUsuario.setIcon(null);
        }
        panelContactos.actualizarListaContactos();
    }

    @Override
    public void alOcultar() {
        // No hay acciones especiales al ocultar por ahora
    }

    @Override
    public TipoVentana getTipo() {
        return TipoVentana.PRINCIPAL;
    }

    private void mostrarMensajesDeContacto() {
        panelMensajes.clearMensajes();
        if (contactoSeleccionado instanceof ContactoIndividual) {
            panelMensajes.setContactoDestino((ContactoIndividual) contactoSeleccionado);
            panelMensajes.addMensajesContacto(contactoSeleccionado);
        } else {
            panelMensajes.setContactoDestino(null);
        }
    }
}
