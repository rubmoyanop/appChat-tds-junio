package umu.tds.appchat.vista.componentes;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import umu.tds.appchat.modelo.Contacto;
import umu.tds.appchat.modelo.Mensaje;
import umu.tds.appchat.modelo.Grupo;
import java.time.format.DateTimeFormatter;

public class ContactoListCellRenderer extends JPanel implements ListCellRenderer<Contacto> {
    private JLabel lblFoto;
    private JLabel lblNombre;
    private JLabel lblUltimoMensaje;
    private JLabel lblHora;

    // formateador de hora
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

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

        lblUltimoMensaje = new JLabel();
        lblUltimoMensaje.setFont(new Font("Arial", Font.PLAIN, 12));
        lblUltimoMensaje.setForeground(Color.GRAY);

        centerPanel.add(lblNombre, BorderLayout.NORTH);
        centerPanel.add(lblUltimoMensaje, BorderLayout.CENTER);

        lblHora = new JLabel();
        lblHora.setFont(new Font("Arial", Font.PLAIN, 11));
        lblHora.setForeground(Color.GRAY);

        add(lblFoto, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);
        add(lblHora, BorderLayout.EAST);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Contacto> list, Contacto contacto, int index, boolean isSelected, boolean cellHasFocus) {
        // Imagen de usuario (placeholder)
        if (contacto instanceof Grupo) {
            lblFoto.setText("G");
            lblFoto.setIcon(null);
            lblFoto.setBackground(new Color(200, 230, 200));
            lblNombre.setText(contacto.getNombre() + " (Grupo)");
            lblNombre.setForeground(new Color(0, 102, 0));
        } else {
            lblFoto.setText("F");
            lblFoto.setIcon(null);
            lblFoto.setBackground(Color.WHITE);
            lblNombre.setText(contacto.getNombre());
            lblNombre.setForeground(Color.BLACK);
        }

        // Obtenemos el último mensaje para sacar los atributos de texto y hora 
        Mensaje ultimo = contacto.getUltimoMensaje();
        if (ultimo != null) {
            lblUltimoMensaje.setText(ultimo.getTexto());
            lblHora.setText(ultimo.getFechaHora().format(TIME_FORMATTER));
        } else {
            lblUltimoMensaje.setText("Sin mensajes");
            lblHora.setText("");
        }

        if (isSelected) {
            setBackground(new Color(220, 235, 252));
        } else if (contacto instanceof Grupo) {
            setBackground(new Color(235, 255, 235));
        } else {
            setBackground(Color.WHITE);
        }
        return this;
    }
}
