package umu.tds.appchat.vista.pantallas;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import com.toedter.calendar.JDateChooser;

import umu.tds.appchat.controlador.AppChat;
import umu.tds.appchat.dao.DAOExcepcion;
import umu.tds.appchat.vista.core.GestorVentanas;
import umu.tds.appchat.vista.core.TipoVentana;
import umu.tds.appchat.vista.core.Ventana;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;

/**
 * Ventana de registro para nuevos usuarios.
 */
public class VentanaRegistro implements Ventana {
    private JFrame frame;
    private JTextField txtNombre;
    private JTextField txtEmail;
    private JTextField txtTelefono;
    private JPasswordField txtContrasena;
    private JPasswordField txtConfirmarContrasena;
    private JDateChooser dateChooser;
    private JTextField txtSaludo;
    private JTextField txtImagenURL; // Field for image URL

    public VentanaRegistro() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setUndecorated(true); // Sin bordes de ventana
        frame.setSize(650, 550); // Aumentar ligeramente la altura para asegurar que todo quepa
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

        JLabel lblTitle = new JLabel("Registro de usuario");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        titlePanel.add(lblTitle, BorderLayout.WEST);

        JButton btnClose = new JButton("X");
        btnClose.setFocusPainted(false);
        btnClose.setBorderPainted(false);
        btnClose.setContentAreaFilled(false);
        btnClose.setFont(new Font("Arial", Font.BOLD, 14));
        btnClose.setForeground(Color.GRAY);
        btnClose.addActionListener(e -> {
            // Volver a la ventana de login
            GestorVentanas.INSTANCIA.mostrarVentana(TipoVentana.LOGIN);
        });
        titlePanel.add(btnClose, BorderLayout.EAST);

        // Panel central con formulario (sin JScrollPane)
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        mainPanel.add(formPanel, BorderLayout.CENTER); // Añadir directamente al centro

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0; // Permitir que los componentes se expandan horizontalmente

        // Fila 0: Etiqueta Nombre
        addLabel(formPanel, "Nombre: *", gbc, 0, 0, 2); // Ocupa 2 columnas

        // Fila 1: Campo Nombre
        txtNombre = new JTextField();
        txtNombre.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        formPanel.add(txtNombre, gbc);

        // Fila 2: Etiqueta Email
        addLabel(formPanel, "Email: (opcional)", gbc, 0, 2, 2);

        // Fila 3: Campo Email
        txtEmail = new JTextField();
        txtEmail.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        formPanel.add(txtEmail, gbc);

        // Fila 4: Etiqueta Teléfono
        addLabel(formPanel, "Teléfono: *", gbc, 0, 4, 2);

        // Fila 5: Campo Teléfono
        txtTelefono = new JTextField();
        txtTelefono.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        formPanel.add(txtTelefono, gbc);

        // Fila 6: Etiquetas Contraseña y Confirmar Contraseña
        addLabel(formPanel, "Contraseña: *", gbc, 0, 6, 1); // Columna 0
        addLabel(formPanel, "Confirmar contraseña: *", gbc, 1, 6, 1); // Columna 1

        // Fila 7: Campos Contraseña y Confirmar Contraseña
        txtContrasena = new JPasswordField();
        txtContrasena.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 1;
        gbc.weightx = 0.5; // Mitad del ancho
        formPanel.add(txtContrasena, gbc);

        txtConfirmarContrasena = new JPasswordField();
        txtConfirmarContrasena.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.gridwidth = 1;
        gbc.weightx = 0.5; // Mitad del ancho
        formPanel.add(txtConfirmarContrasena, gbc);
        gbc.weightx = 1.0; // Restaurar peso completo para las siguientes filas

        // Fila 8: Etiqueta Fecha Nacimiento
        addLabel(formPanel, "Fecha de nacimiento: *", gbc, 0, 8, 2);

        // Fila 9: Campo Fecha Nacimiento
        dateChooser = new JDateChooser();
        dateChooser.setFont(new Font("Arial", Font.PLAIN, 14));
        dateChooser.setDate(new Date()); // Pone la fecha actual por defecto
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        formPanel.add(dateChooser, gbc);

        // Fila 10: Etiquetas Saludo e Imagen URL
        addLabel(formPanel, "Saludo: (opcional)", gbc, 0, 10, 1); // Columna 0
        addLabel(formPanel, "URL Imagen de perfil: (opcional)", gbc, 1, 10, 1); // Columna 1

        // Fila 11 y 12: Campos Saludo e Imagen URL
        txtSaludo = new JTextField();
        txtSaludo.setFont(new Font("Arial", Font.PLAIN, 14));
        // Hacer el campo de saludo más alto
        txtSaludo.setPreferredSize(new Dimension(txtSaludo.getPreferredSize().width, 60));
        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.gridwidth = 1;
        gbc.gridheight = 2; // Ocupa dos filas de alto
        gbc.weightx = 0.5;
        gbc.fill = GridBagConstraints.BOTH; // Relleno vertical y horizontal
        formPanel.add(txtSaludo, gbc);

        // Campo de texto para la URL de la imagen (a la derecha del saludo)
        txtImagenURL = new JTextField();
        txtImagenURL.setFont(new Font("Arial", Font.PLAIN, 14));
        txtImagenURL.setToolTipText("Introduce la URL de una imagen (ej: https://ejemplo.com/imagen.jpg)");
        gbc.gridx = 1;
        gbc.gridy = 11;
        gbc.gridwidth = 1;
        gbc.gridheight = 2; // Ocupa dos filas como el saludo
        gbc.weightx = 0.5;
        gbc.fill = GridBagConstraints.BOTH; // Relleno vertical y horizontal
        formPanel.add(txtImagenURL, gbc);

        // Restaurar valores por defecto de gbc para las siguientes filas
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridheight = 1;
        gbc.weightx = 1.0;

        // Fila 13: Información campos obligatorios
        JLabel lblCamposObligatorios = new JLabel("* Campos obligatorios");
        lblCamposObligatorios.setFont(new Font("Arial", Font.ITALIC, 12));
        lblCamposObligatorios.setForeground(Color.RED);
        gbc.gridx = 0;
        gbc.gridy = 13;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 5, 8, 5); // Más espacio arriba
        formPanel.add(lblCamposObligatorios, gbc);

        // Panel para botones
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.WHITE);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Botón Cancelar
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Arial", Font.PLAIN, 14));
        btnCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                GestorVentanas.INSTANCIA.mostrarVentana(TipoVentana.LOGIN);
            }
        });
        buttonPanel.add(btnCancelar);

        // Botón Registrar
        JButton btnRegistrar = new JButton("Registrar");
        btnRegistrar.setFont(new Font("Arial", Font.BOLD, 14));
        btnRegistrar.setBackground(new Color(66, 133, 244));
        btnRegistrar.setForeground(Color.BLACK);
        btnRegistrar.setOpaque(true);
        btnRegistrar.setContentAreaFilled(true);
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (validarFormulario()) {
                    // Extraer datos del formulario
                    String nombre = txtNombre.getText().trim();
                    String email = txtEmail.getText().trim();
                    String telefono = txtTelefono.getText().trim();
                    String contrasena = new String(txtContrasena.getPassword());
                    String confirmarContrasena = new String(txtConfirmarContrasena.getPassword());
                    Date fechaNacimiento = dateChooser.getDate();
                    String saludo = txtSaludo.getText().trim();
                    String imagenURL = txtImagenURL.getText().trim(); // Obtener la URL del campo de texto

                    try {
                        // Llamar al controlador para registrar el usuario, pasando la URL
                        boolean registroExitoso = AppChat.INSTANCE.registrarUsuario(
                                nombre, email, fechaNacimiento, telefono, contrasena,
                                confirmarContrasena, imagenURL, saludo, false); // Usar imagenURL

                        if (registroExitoso) {
                            // Mostrar mensaje de éxito
                            JOptionPane.showMessageDialog(frame,
                                    "Usuario registrado correctamente.",
                                    "Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);
                            // Volver a la ventana de login
                            GestorVentanas.INSTANCIA.mostrarVentana(TipoVentana.LOGIN);
                        } else {
                            // Mostrar error específico si el móvil ya existe
                            mostrarError("El número de teléfono ya está registrado.");
                        }

                    } catch (IllegalArgumentException ex) {
                        // Mostrar error de validación de datos de entrada
                        mostrarError(ex.getMessage());
                    } catch (DAOExcepcion ex) {
                        // Mostrar error de persistencia
                        mostrarError("Error al guardar los datos: " + ex.getMessage());
                        ex.printStackTrace(); // Para depuración
                    } catch (Exception ex) {
                        // Capturar cualquier otro error inesperado
                        mostrarError("Ocurrió un error inesperado durante el registro: " + ex.getMessage());
                        ex.printStackTrace(); // Para depuración
                    }
                }
            }
        });
        buttonPanel.add(btnRegistrar);

        // Para hacer dragable la ventana
        FrameDragListener frameDragListener = new FrameDragListener(frame);
        titlePanel.addMouseListener(frameDragListener);
        titlePanel.addMouseMotionListener(frameDragListener);
    }

    // Método auxiliar addLabel modificado para aceptar coordenadas y ancho
    private void addLabel(JPanel panel, String text, GridBagConstraints gbc, int x, int y, int width) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = width;
        panel.add(label, gbc);
    }

    private boolean validarFormulario() {
        // Validar campos obligatorios
        if (txtNombre.getText().trim().isEmpty()) {
            mostrarError("El nombre es obligatorio");
            return false;
        }

        if (txtTelefono.getText().trim().isEmpty()) {
            mostrarError("El teléfono es obligatorio");
            return false;
        }

        if (new String(txtContrasena.getPassword()).isEmpty()) {
            mostrarError("La contraseña es obligatoria");
            return false;
        }

        if (new String(txtConfirmarContrasena.getPassword()).isEmpty()) {
            mostrarError("Debe confirmar la contraseña");
            return false;
        }

        // Validar que las contraseñas coincidan
        if (!new String(txtContrasena.getPassword()).equals(new String(txtConfirmarContrasena.getPassword()))) {
            mostrarError("Las contraseñas no coinciden");
            return false;
        }

        // Validar que se haya seleccionado una fecha
        if (dateChooser.getDate() == null) {
            mostrarError("La fecha de nacimiento es obligatoria");
            return false;
        }

        // Validar formato de teléfono (solo números)
        if (!txtTelefono.getText().matches("\\d+")) {
            mostrarError("El teléfono debe contener solo números");
            return false;
        }

        // Validar formato de email si se ha proporcionado
        String email = txtEmail.getText().trim();
        if (!email.isEmpty() && !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            mostrarError("El formato del email no es válido");
            return false;
        }

        return true;
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(frame, mensaje, "Error de validación", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public JFrame getPanelPrincipal() {
        return frame;
    }

    @Override
    public void alMostrar() {
        // Limpiar campos
        txtNombre.setText("");
        txtEmail.setText("");
        txtTelefono.setText("");
        txtContrasena.setText("");
        txtConfirmarContrasena.setText("");
        dateChooser.setDate(new Date()); // Fecha actual
        txtSaludo.setText("");
        txtImagenURL.setText(""); // Clear the URL field

        // Poner el foco en el primer campo
        txtNombre.requestFocus();
    }

    @Override
    public void alOcultar() {
        // Limpiar datos sensibles
        txtContrasena.setText("");
        txtConfirmarContrasena.setText("");
    }

    @Override
    public TipoVentana getTipo() {
        return TipoVentana.REGISTRO;
    }

    // Clase interna para permitir arrastrar la ventana sin bordes
    private class FrameDragListener extends MouseAdapter {
        private final JFrame frame;
        private Point mouseDownCompCoords = null;

        public FrameDragListener(JFrame frame) {
            this.frame = frame;
        }

        public void mousePressed(MouseEvent e) {
            mouseDownCompCoords = e.getPoint();
        }

        public void mouseReleased(MouseEvent e) {
            mouseDownCompCoords = null;
        }

        public void mouseDragged(MouseEvent e) {
            if (mouseDownCompCoords != null) {
                Point currCoords = e.getLocationOnScreen();
                frame.setLocation(currCoords.x - mouseDownCompCoords.x,
                                 currCoords.y - mouseDownCompCoords.y);
            }
        }
    }
}
