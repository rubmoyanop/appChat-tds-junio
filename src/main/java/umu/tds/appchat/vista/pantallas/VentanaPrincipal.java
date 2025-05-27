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
import umu.tds.appchat.modelo.Grupo;
import umu.tds.appchat.modelo.ResultadoBusqueda;
import umu.tds.appchat.vista.componentes.ContactoGrupoCellRenderer;
import umu.tds.appchat.vista.componentes.ListaContactosPanel;
import umu.tds.appchat.vista.componentes.MensajeListCellRenderer;
import umu.tds.appchat.vista.componentes.ModeloTablaContactos; 


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;

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
        JButton btnModificarGrupo = new JButton("Modificar Grupo");

        btnAddContacto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarDialogoAgregarContacto();
            }
        });

        btnAddGrupo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarDialogoCrearGrupo();
            }
        });

        btnVerContactos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarListaContactos();
            }
        });

        btnBuscarMensajes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarDialogoBuscarMensajes();
            }
        });

        btnModificarGrupo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (contactoSeleccionado == null) {
                    JOptionPane.showMessageDialog(frame, "Debe seleccionar un contacto primero.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (!(contactoSeleccionado instanceof Grupo)) {
                    JOptionPane.showMessageDialog(frame, "El contacto seleccionado no es un grupo.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                mostrarDialogoModificarGrupo((Grupo) contactoSeleccionado);
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
        headerPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        headerPanel.add(btnModificarGrupo);

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

    private void mostrarDialogoCambiarNombreContacto() {
        JDialog dialogo = new JDialog(frame, "Asignar Nombre", true);
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
        formPanel.add(new JLabel("Asignar Nombre al Contacto Seleccionado:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        JTextField txtNombreContacto = new JTextField();
        formPanel.add(txtNombreContacto, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnCancelar = new JButton("Cancelar");
        JButton btnAceptar = new JButton("Aceptar");

        btnCancelar.addActionListener(ev -> dialogo.dispose());

        btnAceptar.addActionListener(ev -> {
            String nombre = txtNombreContacto.getText().trim();

            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(dialogo, "El nombre es obligatorio.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                boolean exito = AppChat.INSTANCE.actualizarNombreContacto(contactoSeleccionado, nombre);
                if (exito) {
                    JOptionPane.showMessageDialog(dialogo, "Nombre agregado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    dialogo.dispose();
                    panelContactos.actualizarListaContactos();
                } else {
                    JOptionPane.showMessageDialog(dialogo, "Error al actualizar el nombre.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(dialogo, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialogo, "Error inesperado al actualizar el nombre.", "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        buttonPanel.add(btnCancelar);
        buttonPanel.add(btnAceptar);

        dialogo.add(formPanel, BorderLayout.CENTER);
        dialogo.add(buttonPanel, BorderLayout.SOUTH);
        dialogo.setVisible(true);
    }

    private ListaContactosPanel getListasContactosGrupos(Grupo g){ //Si es null, estamos creando un grupo
        // Modelos para las listas
        DefaultListModel<ContactoIndividual> contactosDisponibles = new DefaultListModel<>();
        DefaultListModel<ContactoIndividual> contactosGrupo = new DefaultListModel<>();

        List<ContactoIndividual> disponibles = AppChat.INSTANCE.getContactosDisponiblesParaGrupo(g);
        List<ContactoIndividual> miembros = AppChat.INSTANCE.getMiembrosGrupo(g);

        for (ContactoIndividual contacto : disponibles) {
            contactosDisponibles.addElement(contacto);
        }
        
        for (ContactoIndividual miembro : miembros) {
            contactosGrupo.addElement(miembro);
        }

        // Listas
        JList<ContactoIndividual> listaContactosDisponibles = new JList<>(contactosDisponibles);
        listaContactosDisponibles.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listaContactosDisponibles.setCellRenderer(new ContactoGrupoCellRenderer());
        JScrollPane scrollPaneAvailable = new JScrollPane(listaContactosDisponibles);
        scrollPaneAvailable.setBorder(BorderFactory.createTitledBorder("Contactos Disponibles"));

        JList<ContactoIndividual> listaContactosGrupo = new JList<>(contactosGrupo);
        listaContactosGrupo.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listaContactosGrupo.setCellRenderer(new ContactoGrupoCellRenderer());
        JScrollPane scrollPaneSelected = new JScrollPane(listaContactosGrupo);
        scrollPaneSelected.setBorder(BorderFactory.createTitledBorder("Miembros del Grupo"));

        // Botones de transferencia
        JButton btnMoveToSelected = new JButton(">>");
        JButton btnMoveToAvailable = new JButton("<<");

        JPanel panelBotonesTransferencia = new JPanel();
        panelBotonesTransferencia.setLayout(new BoxLayout(panelBotonesTransferencia, BoxLayout.Y_AXIS));
        panelBotonesTransferencia.add(Box.createVerticalGlue());
        panelBotonesTransferencia.add(btnMoveToSelected);
        panelBotonesTransferencia.add(Box.createRigidArea(new Dimension(0, 10)));
        panelBotonesTransferencia.add(btnMoveToAvailable);
        panelBotonesTransferencia.add(Box.createVerticalGlue());

        // Panel central con listas y botones de transferencia
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(5, 10, 5, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.45;
        panel.add(scrollPaneAvailable, gbc);

        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.1; gbc.fill = GridBagConstraints.VERTICAL;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(panelBotonesTransferencia, gbc);

        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0.45; gbc.fill = GridBagConstraints.BOTH;
        panel.add(scrollPaneSelected, gbc);

        // Lógica de los botones de transferencia
        btnMoveToSelected.addActionListener(e -> {
            List<ContactoIndividual> seleccionados = listaContactosDisponibles.getSelectedValuesList();
            for (ContactoIndividual c : seleccionados) {
                if(!contactosGrupo.contains(c)) contactosGrupo.addElement(c);
                contactosDisponibles.removeElement(c);
            }
        });

        btnMoveToAvailable.addActionListener(e -> {
            List<ContactoIndividual> seleccionados = listaContactosGrupo.getSelectedValuesList();
            for (ContactoIndividual c : seleccionados) {
                if(!contactosDisponibles.contains(c)) contactosDisponibles.addElement(c);
                contactosGrupo.removeElement(c);
            }
        });

        return new ListaContactosPanel(panel, contactosGrupo);
    }



    private void mostrarDialogoCrearGrupo() {
        JDialog dialogoCrearGrupo = new JDialog(frame, "Crear Nuevo Grupo", true);
        dialogoCrearGrupo.setSize(650, 450);
        dialogoCrearGrupo.setLocationRelativeTo(frame);
        dialogoCrearGrupo.setLayout(new BorderLayout(10, 10));

        // Panel para el nombre del grupo
        JPanel panelNombreGrupo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelNombreGrupo.setBorder(new EmptyBorder(10, 10, 0, 10));
        panelNombreGrupo.add(new JLabel("Nombre del Grupo:"));
        JTextField txtNombreGrupo = new JTextField(20);
        panelNombreGrupo.add(txtNombreGrupo);
        dialogoCrearGrupo.add(panelNombreGrupo, BorderLayout.NORTH);

        // Panel para las listas de contactos
        ListaContactosPanel groupComponents = getListasContactosGrupos(null);
        JPanel panelCentral = groupComponents.getPanel();
        DefaultListModel<ContactoIndividual> listaContactosAñadidos = groupComponents.getListaContactos();

        dialogoCrearGrupo.add(panelCentral, BorderLayout.CENTER);

        // Botones de acción del diálogo
        JPanel panelBotonesDialogo = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotonesDialogo.setBorder(new EmptyBorder(0, 10, 10, 10));
        JButton btnCrear = new JButton("Crear Grupo");
        JButton btnCancelar = new JButton("Cancelar");
        panelBotonesDialogo.add(btnCrear);
        panelBotonesDialogo.add(btnCancelar);
        dialogoCrearGrupo.add(panelBotonesDialogo, BorderLayout.SOUTH);

        // Lógica del botón Cancelar
        btnCancelar.addActionListener(e -> dialogoCrearGrupo.dispose());

        // Lógica del botón Crear Grupo
        btnCrear.addActionListener(e -> {
            String nombreGrupo = txtNombreGrupo.getText().trim();
            if (nombreGrupo.isEmpty()) {
                JOptionPane.showMessageDialog(dialogoCrearGrupo, "El nombre del grupo no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (listaContactosAñadidos.isEmpty()) {
                JOptionPane.showMessageDialog(dialogoCrearGrupo, "Debe seleccionar al menos un miembro para el grupo.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            List<ContactoIndividual> miembros = Collections.list(listaContactosAñadidos.elements());
            try {
                boolean exito = AppChat.INSTANCE.crearGrupo(nombreGrupo, miembros);
                if (exito) {
                    JOptionPane.showMessageDialog(dialogoCrearGrupo, "Grupo '" + nombreGrupo + "' creado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    panelContactos.actualizarListaContactos();
                    dialogoCrearGrupo.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialogoCrearGrupo, "No se pudo crear el grupo. Puede que ya exista un grupo o contacto con ese nombre.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialogoCrearGrupo, "Error inesperado al crear el grupo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        dialogoCrearGrupo.setVisible(true);
    }

    private void mostrarDialogoModificarGrupo(Grupo g){
        JDialog dialogoModificarGrupo = new JDialog(frame, "Modificar Grupo: " + g.getNombre(), true);
        dialogoModificarGrupo.setSize(650, 450);
        dialogoModificarGrupo.setLocationRelativeTo(frame);
        dialogoModificarGrupo.setLayout(new BorderLayout(10, 10));

        // Panel para las listas de contactos
        ListaContactosPanel groupComponents = getListasContactosGrupos(g);
        JPanel panelCentral = groupComponents.getPanel();
        DefaultListModel<ContactoIndividual> listaContactosAñadidos = groupComponents.getListaContactos();

        dialogoModificarGrupo.add(panelCentral, BorderLayout.CENTER);

        // Botones de acción del diálogo
        JPanel panelBotonesDialogo = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotonesDialogo.setBorder(new EmptyBorder(0, 10, 10, 10));
        JButton btnActualizar = new JButton("Actualizar");
        JButton btnCancelar = new JButton("Cancelar");
        panelBotonesDialogo.add(btnActualizar);
        panelBotonesDialogo.add(btnCancelar);
        dialogoModificarGrupo.add(panelBotonesDialogo, BorderLayout.SOUTH);

        // Lógica del botón Cancelar
        btnCancelar.addActionListener(e -> dialogoModificarGrupo.dispose());

        // Lógica del botón Modificar Grupo
        btnActualizar.addActionListener(e -> {
            if (listaContactosAñadidos.isEmpty()) {
                JOptionPane.showMessageDialog(dialogoModificarGrupo, "Debe seleccionar al menos un miembro para el grupo.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                // Actualizar los miembros del grupo antes de persistir
                List<ContactoIndividual> nuevosMiembros = Collections.list(listaContactosAñadidos.elements());
                g.setMiembros(nuevosMiembros);
                
                boolean exito = AppChat.INSTANCE.modificarGrupo(g);
                if (exito) {
                    JOptionPane.showMessageDialog(dialogoModificarGrupo, "Grupo '" + g.getNombre() + "' modificado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    panelContactos.actualizarListaContactos();
                    dialogoModificarGrupo.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialogoModificarGrupo, "No se pudo modificar el grupo.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialogoModificarGrupo, "Error inesperado al modificar el grupo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        dialogoModificarGrupo.setVisible(true);
    }



    private void mostrarListaContactos() {
        JDialog dialogoContactos = new JDialog(frame, "Lista de Contactos y Grupos", true);
        dialogoContactos.setSize(600, 400);
        dialogoContactos.setLocationRelativeTo(frame);
        dialogoContactos.setLayout(new BorderLayout(10, 10));

        // Obtener todos los contactos (individuales y grupos)
        List<Contacto> todosLosContactos = AppChat.INSTANCE.getContactosUsuarioActual();
        ModeloTablaContactos tableModel = new ModeloTablaContactos(todosLosContactos);
        JTable tablaContactos = new JTable(tableModel);
        tablaContactos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaContactos.setFillsViewportHeight(true);

        tablaContactos.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && tablaContactos.getSelectedRow() != -1) {
                Contacto seleccionado = tableModel.getContactoAt(tablaContactos.getSelectedRow());
                if (seleccionado != null) {

                    // Actualizar la lista principal de contactos para que el seleccionado aparezca si es necesario
                    // y para asegurar que la JList en PanelContactos está actualizada.
                    panelContactos.actualizarListaContactos();
                    
                    // Seleccionar el contacto en la JList del PanelContactos principal
                    panelContactos.getContactosList().setSelectedValue(seleccionado, true);

                    dialogoContactos.dispose();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tablaContactos);
        dialogoContactos.add(scrollPane, BorderLayout.CENTER);

        // Botón de cerrar
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.setBorder(new EmptyBorder(0, 10, 10, 10));
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dialogoContactos.dispose());
        panelBotones.add(btnCerrar);
        dialogoContactos.add(panelBotones, BorderLayout.SOUTH);

        dialogoContactos.setVisible(true);
    }

    private void mostrarDialogoBuscarMensajes() {
        JDialog dialogoBuscarMensajes = new JDialog(frame, "Buscar Mensajes", true); 
        dialogoBuscarMensajes.setSize(800, 550);
        dialogoBuscarMensajes.setLocationRelativeTo(frame);
        dialogoBuscarMensajes.setLayout(new BorderLayout(10, 10));

        // Panel superior para los filtros
        JPanel panelFiltros = new JPanel(new GridBagLayout());
        panelFiltros.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Filtros de Búsqueda"),
                new EmptyBorder(5, 10, 10, 10) 
        ));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); 
        gbc.fill = GridBagConstraints.HORIZONTAL;

        //JTextField para texto
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panelFiltros.add(new JLabel("Texto:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0; 
        gbc.gridwidth = 6; 
        JTextField txtFiltroTexto = new JTextField();
        panelFiltros.add(txtFiltroTexto, gbc);
        gbc.gridwidth = 1; 

        // Fila 1: JTextField para contacto, JTextField para telefono y botón de Buscar
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0.0; 
        panelFiltros.add(new JLabel("Contacto:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0.3; 
        JTextField txtFiltroContacto = new JTextField();
        panelFiltros.add(txtFiltroContacto, gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0.0; 
        panelFiltros.add(new JLabel("Teléfono:"), gbc);

        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.weightx = 0.3; 
        JTextField txtFiltroTelefono = new JTextField();
        panelFiltros.add(txtFiltroTelefono, gbc);

        gbc.gridx = 4;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 0.0;
        panelFiltros.add(new JLabel("Tipo Mensaje:"), gbc);

        gbc.gridx = 5;
        gbc.gridy = 1;
        gbc.weightx = 0.3; 
        String[] tiposMensajeOpciones = {"AMBOS", "ENVIADO", "RECIBIDO"};
        JComboBox<String> comboTipoMensaje = new JComboBox<>(tiposMensajeOpciones);
        comboTipoMensaje.setSelectedItem("AMBOS");
        panelFiltros.add(comboTipoMensaje, gbc);
        
        gbc.gridx = 6; 
        gbc.gridy = 1;
        gbc.weightx = 0.1; 
        gbc.fill = GridBagConstraints.NONE; 
        gbc.anchor = GridBagConstraints.EAST; 
        JButton btnEjecutarBusqueda = new JButton("Buscar");
        panelFiltros.add(btnEjecutarBusqueda, gbc);
        
        // Añadir el panel de filtros al norte del diálogo
        dialogoBuscarMensajes.add(panelFiltros, BorderLayout.NORTH);

        // Panel de resultados (ocupará el centro)
        JPanel panelResultados = new JPanel(new BorderLayout());
        panelResultados.setBorder(BorderFactory.createTitledBorder("Resultados"));
        
        DefaultListModel<ResultadoBusqueda> modeloResultados = new DefaultListModel<>();
        JList<ResultadoBusqueda> listaResultados = new JList<>(modeloResultados);
        
        // Usar el constructor especial para contexto de búsqueda
        listaResultados.setCellRenderer(new MensajeListCellRenderer());
        listaResultados.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollResultados = new JScrollPane(listaResultados);
        scrollResultados.setPreferredSize(new Dimension(750, 300));
        
        panelResultados.add(new JLabel("Los resultados aparecerán aquí cuando ejecute una búsqueda."), BorderLayout.NORTH);
        panelResultados.add(scrollResultados, BorderLayout.CENTER);
        
        dialogoBuscarMensajes.add(panelResultados, BorderLayout.CENTER);
        
        JPanel panelBotonesInferiores = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dialogoBuscarMensajes.dispose());
        panelBotonesInferiores.add(btnCancelar);
        dialogoBuscarMensajes.add(panelBotonesInferiores, BorderLayout.SOUTH);

        btnEjecutarBusqueda.addActionListener(e -> {
            String textoFiltro = txtFiltroTexto.getText().trim();
            String contactoFiltro = txtFiltroContacto.getText().trim();
            String telefonoFiltro = txtFiltroTelefono.getText().trim();
            String tipoMensajeSeleccionado = (String) comboTipoMensaje.getSelectedItem(); 
            
            // Combinar filtro de contacto y teléfono si ambos están presentes
            String filtroContactoCompleto = null;
            if (!contactoFiltro.isEmpty() && !telefonoFiltro.isEmpty()) {
                filtroContactoCompleto = contactoFiltro + " " + telefonoFiltro;
            } else if (!contactoFiltro.isEmpty()) {
                filtroContactoCompleto = contactoFiltro;
            } else if (!telefonoFiltro.isEmpty()) {
                filtroContactoCompleto = telefonoFiltro;
            }
            
            try {
                List<ResultadoBusqueda> resultados = AppChat.INSTANCE.buscarMensajes(
                    textoFiltro.isEmpty() ? null : textoFiltro,
                    filtroContactoCompleto
                );
                
                // Limpiar resultados anteriores
                modeloResultados.clear();
                
                // Agregar nuevos resultados
                for (ResultadoBusqueda resultado : resultados) {
                    modeloResultados.addElement(resultado);
                }
                
                // Actualizar el texto informativo
                if (resultados.isEmpty()) {
                    panelResultados.remove(0);
                    panelResultados.add(new JLabel("No se encontraron mensajes con los filtros aplicados."), BorderLayout.NORTH, 0);
                } else {
                    panelResultados.remove(0);
                    panelResultados.add(new JLabel("Se encontraron " + resultados.size() + " mensaje(s):"), BorderLayout.NORTH, 0);
                }
                
                panelResultados.revalidate();
                panelResultados.repaint();
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialogoBuscarMensajes, 
                    "Error al buscar mensajes: " + ex.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        dialogoBuscarMensajes.setVisible(true);
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
        if (contactoSeleccionado != null) {
            panelMensajes.setContactoDestino(contactoSeleccionado);
            panelMensajes.addMensajesContacto(contactoSeleccionado);
            if(AppChat.INSTANCE.esDesconocido(contactoSeleccionado)) mostrarDialogoCambiarNombreContacto();
        } else {
            panelMensajes.setContactoDestino(null);
        }
    }
}
