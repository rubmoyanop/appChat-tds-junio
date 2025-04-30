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
    private JLabel lblImagenSeleccionada;
    private String rutaImagen;
    
    public VentanaRegistro() {
        initialize();
    }
    
    private void initialize() {
        frame = new JFrame();
        frame.setUndecorated(true); // Sin bordes de ventana
        frame.setSize(500, 600); // Tamaño adecuado para el formulario
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
        
        // Panel central con formulario (usando JScrollPane para pantallas pequeñas)
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        
        // Crear un JScrollPane para permitir desplazamiento
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(null); // Eliminar borde del scrollPane
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 5, 8, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1.0;
        int gridy = 0;
        
        // Campo Nombre (obligatorio)
        addLabel(formPanel, "Nombre: *", gbc, gridy++);
        txtNombre = new JTextField();
        txtNombre.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridy = gridy++;
        formPanel.add(txtNombre, gbc);
        
        // Campo Email (opcional)
        addLabel(formPanel, "Email: (opcional)", gbc, gridy++);
        txtEmail = new JTextField();
        txtEmail.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridy = gridy++;
        formPanel.add(txtEmail, gbc);
        
        // Campo Teléfono (obligatorio)
        addLabel(formPanel, "Teléfono: *", gbc, gridy++);
        txtTelefono = new JTextField();
        txtTelefono.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridy = gridy++;
        formPanel.add(txtTelefono, gbc);
        
        // Campo Contraseña (obligatorio)
        addLabel(formPanel, "Contraseña: *", gbc, gridy++);
        txtContrasena = new JPasswordField();
        txtContrasena.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridy = gridy++;
        formPanel.add(txtContrasena, gbc);
        
        // Campo Confirmar contraseña (obligatorio)
        addLabel(formPanel, "Confirmar contraseña: *", gbc, gridy++);
        txtConfirmarContrasena = new JPasswordField();
        txtConfirmarContrasena.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridy = gridy++;
        formPanel.add(txtConfirmarContrasena, gbc);
        
        // Campo Fecha de nacimiento (obligatorio)
        addLabel(formPanel, "Fecha de nacimiento: *", gbc, gridy++);
        dateChooser = new JDateChooser();
        dateChooser.setFont(new Font("Arial", Font.PLAIN, 14));
        dateChooser.setDate(new Date()); // Pone la fecha actual por defecto
        gbc.gridy = gridy++;
        formPanel.add(dateChooser, gbc);
        
        // Campo Saludo (opcional)
        addLabel(formPanel, "Saludo: (opcional)", gbc, gridy++);
        txtSaludo = new JTextField();
        txtSaludo.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridy = gridy++;
        formPanel.add(txtSaludo, gbc);
        
        // Campo Imagen (opcional)
        addLabel(formPanel, "Imagen de perfil: (opcional)", gbc, gridy++);
        
        JPanel imagenPanel = new JPanel(new BorderLayout(10, 0));
        imagenPanel.setBackground(Color.WHITE);
        
        // Etiqueta para mostrar la imagen seleccionada o un texto predeterminado
        lblImagenSeleccionada = new JLabel("Ninguna imagen seleccionada");
        lblImagenSeleccionada.setFont(new Font("Arial", Font.ITALIC, 12));
        imagenPanel.add(lblImagenSeleccionada, BorderLayout.CENTER);
        
        // Botón para seleccionar imagen
        JButton btnSeleccionarImagen = new JButton("Seleccionar imagen");
        btnSeleccionarImagen.setFont(new Font("Arial", Font.PLAIN, 12));
        btnSeleccionarImagen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mostrarError("En desarrollo");
            }
        });
        imagenPanel.add(btnSeleccionarImagen, BorderLayout.EAST);
        
        gbc.gridy = gridy++;
        formPanel.add(imagenPanel, gbc);
        
        // Información campos obligatorios
        JLabel lblCamposObligatorios = new JLabel("* Campos obligatorios");
        lblCamposObligatorios.setFont(new Font("Arial", Font.ITALIC, 12));
        lblCamposObligatorios.setForeground(Color.RED);
        gbc.gridy = gridy++;
        gbc.insets = new Insets(15, 5, 8, 5);
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

                    try {
                        // Llamar al controlador para registrar el usuario
                        boolean registroExitoso = AppChat.INSTANCE.registrarUsuario(
                                nombre, email, fechaNacimiento, telefono, contrasena,
                                confirmarContrasena, rutaImagen, saludo, false); 

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
    
    private void addLabel(JPanel panel, String text, GridBagConstraints gbc, int y) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridy = y;
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
        lblImagenSeleccionada.setText("Ninguna imagen seleccionada");
        rutaImagen = null;
        
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
