package umu.tds.appchat.vista.componentes;

import javax.swing.*;
import java.awt.*;
import umu.tds.appchat.modelo.Mensaje;
import umu.tds.appchat.modelo.Contacto;
import umu.tds.appchat.modelo.TipoMensaje; 

public class MensajeListCellRenderer extends JPanel implements ListCellRenderer<Mensaje> {
    private JLabel lblEmisor;
    private JLabel lblReceptor;
    private JLabel lblTexto;
    private JPanel pnlHeader;

    private Contacto chatContact;

    public MensajeListCellRenderer(Contacto chatContact) {
        if (chatContact == null) {
            throw new IllegalArgumentException("Chat contact cannot be null.");
        }
        this.chatContact = chatContact;

        setLayout(new BorderLayout(5, 0)); // Espacio entre cabecera y texto
        setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8)); // Relleno para toda la celda
        setOpaque(true); // Necesario para que los colores de fondo se pinten

        // Panel de cabecera para Emisor y Receptor
        pnlHeader = new JPanel(new BorderLayout(5, 0)); // Espacio entre emisor y receptor
        pnlHeader.setOpaque(false); // Hereda el fondo del padre

        lblEmisor = new JLabel();
        lblEmisor.setFont(lblEmisor.getFont().deriveFont(Font.BOLD));
        lblEmisor.setForeground(Color.DARK_GRAY);

        lblReceptor = new JLabel();
        lblReceptor.setForeground(Color.GRAY); // Color ligeramente diferente para el receptor

        pnlHeader.add(lblEmisor, BorderLayout.WEST);
        pnlHeader.add(lblReceptor, BorderLayout.EAST);

        // Etiqueta de texto
        lblTexto = new JLabel();

        add(pnlHeader, BorderLayout.NORTH);
        add(lblTexto, BorderLayout.CENTER);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Mensaje> list,
                                                  Mensaje mensaje,
                                                  int index,
                                                  boolean isSelected,
                                                  boolean cellHasFocus) {
        String emisorDisplay;
        String receptorDisplay;

        // Determinar emisor y receptor según el tipo de mensaje y el contexto del usuario actual
        if (mensaje.getTipo() == TipoMensaje.ENVIADO) { // Mensaje enviado por currentUser
            emisorDisplay = "Yo"; // O currentUser.getNombre()
            receptorDisplay = chatContact.getNombre();
        } else { // Mensaje recibido por currentUser (presumiblemente de chatContact)
            emisorDisplay = chatContact.getNombre();
            // Si chatContact es un Grupo, emisorDisplay será el nombre del grupo.
            // Para el nombre del remitente individual en el chat grupal, el modelo Mensaje necesitaría almacenar el remitente original.
            receptorDisplay = "Yo"; // O currentUser.getNombre()
        }

        lblEmisor.setText(emisorDisplay);
        lblReceptor.setText(receptorDisplay);

        return this;
    }
}
