package umu.tds.appchat.vista.componentes;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import umu.tds.appchat.modelo.Contacto;

public class ContactoListCellRenderer extends JPanel implements ListCellRenderer<Contacto> {
    private JLabel lblFoto;
    private JLabel lblNombre;
    private JLabel lblUltimoMensaje;
    private JLabel lblHora;

    public ContactoListCellRenderer() {
        setLayout(new BorderLayout(8, 0));
        setBorder(new EmptyBorder(6, 8, 6, 8));
        setOpaque(true);

        lblFoto = new JLabel();
        lblFoto.setPreferredSize(new Dimension(40, 40));
        lblFoto.setHorizontalAlignment(SwingConstants.CENTER);
        lblFoto.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);

        lblNombre = new JLabel();
        lblNombre.setFont(new Font("Arial", Font.BOLD, 14));

        lblUltimoMensaje = new JLabel("Último mensaje..."); // Placeholder
        lblUltimoMensaje.setFont(new Font("Arial", Font.PLAIN, 12));
        lblUltimoMensaje.setForeground(Color.GRAY);

        centerPanel.add(lblNombre, BorderLayout.NORTH);
        centerPanel.add(lblUltimoMensaje, BorderLayout.CENTER);

        lblHora = new JLabel("00:00"); // Placeholder
        lblHora.setFont(new Font("Arial", Font.PLAIN, 11));
        lblHora.setForeground(Color.GRAY);

        add(lblFoto, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);
        add(lblHora, BorderLayout.EAST);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Contacto> list, Contacto value, int index, boolean isSelected, boolean cellHasFocus) {
        // Imagen de usuario (placeholder)
        lblFoto.setText("F");
        lblFoto.setIcon(null);

        // Nombre
        lblNombre.setText(value.getNombre());

        // Placeholder para último mensaje y hora
        lblUltimoMensaje.setText("Último mensaje...");
        lblHora.setText("00:00");

        if (isSelected) {
            setBackground(new Color(220, 235, 252));
        } else {
            setBackground(Color.WHITE);
        }
        return this;
    }
}
