package umu.tds.appchat.vista.componentes;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Panel que muestra el área de mensajes y envío.
 */
public class PanelMensajes extends JPanel {

    public PanelMensajes() {
        super(new BorderLayout());
        initialize();
    }

    private void initialize() {
        setBackground(Color.WHITE);
        JLabel lblMensajesTitle = new JLabel("Mensajes");
        lblMensajesTitle.setFont(new Font("Arial", Font.BOLD, 16));
        lblMensajesTitle.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
                new EmptyBorder(5, 10, 5, 10)
        ));
        lblMensajesTitle.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblMensajesTitle, BorderLayout.NORTH);
        // TODO: Aquí iría la visualización y el envío de mensajes
    }
}
