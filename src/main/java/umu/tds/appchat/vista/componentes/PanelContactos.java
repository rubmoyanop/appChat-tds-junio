package umu.tds.appchat.vista.componentes;

import umu.tds.appchat.controlador.AppChat;
import umu.tds.appchat.modelo.Contacto;
import umu.tds.appchat.modelo.Usuario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

/**
 * Panel que muestra la lista de contactos.
 */
public class PanelContactos extends JPanel {

    private DefaultListModel<Contacto> contactosListModel;
    private JList<Contacto> contactosList;

    public PanelContactos() {
        super(new BorderLayout());
        initialize();
    }

    private void initialize() {
        setBackground(new Color(245, 245, 245));
        JLabel lblContactosTitle = new JLabel("Contactos");
        lblContactosTitle.setFont(new Font("Arial", Font.BOLD, 16));
        lblContactosTitle.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
                new EmptyBorder(5, 10, 5, 10)
        ));
        lblContactosTitle.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblContactosTitle, BorderLayout.NORTH);

        // Panel de lista de contactos/chats
        contactosListModel = new DefaultListModel<>();
        contactosList = new JList<>(contactosListModel);
        contactosList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        contactosList.setCellRenderer(new ContactoListCellRenderer());
        JScrollPane contactosScrollPane = new JScrollPane(contactosList);
        add(contactosScrollPane, BorderLayout.CENTER);
    }

    /**
     * Actualiza la lista de contactos en la interfaz.
     */
    public void actualizarListaContactos() {
        contactosListModel.clear();
        Usuario usuario = AppChat.INSTANCE.getUsuarioActual();
        if (usuario != null) {
            List<Contacto> contactos = usuario.getContactos();
            for (Contacto c : contactos) {
                contactosListModel.addElement(c);
            }
        }
    }

    public JList<Contacto> getContactosList() {
        return contactosList;
    }
}
