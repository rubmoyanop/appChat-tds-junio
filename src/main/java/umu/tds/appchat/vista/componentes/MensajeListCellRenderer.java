package umu.tds.appchat.vista.componentes;

import javax.swing.*;
import java.awt.*;
import umu.tds.appchat.modelo.Mensaje;
import umu.tds.appchat.modelo.ResultadoBusqueda;
import umu.tds.appchat.modelo.Contacto; 

public class MensajeListCellRenderer extends JPanel implements ListCellRenderer<Object> {
    private JLabel lblEmisor;
    private JLabel lblReceptor;
    private JLabel lblTexto;
    private JPanel pnlHeader;
    
    // Constructor para chat específico
    public MensajeListCellRenderer(Contacto chatContact) {
        initComponents();
    }

    // Constructor para contexto de búsqueda
    public MensajeListCellRenderer() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(5, 0));
        setBorder(BorderFactory.createEmptyBorder(5, 8, 5, 8));
        setOpaque(true);

        pnlHeader = new JPanel(new BorderLayout(5, 0));
        pnlHeader.setOpaque(false);

        lblEmisor = new JLabel();
        lblEmisor.setFont(lblEmisor.getFont().deriveFont(Font.BOLD));
        lblEmisor.setForeground(Color.DARK_GRAY);

        lblReceptor = new JLabel();
        lblReceptor.setForeground(Color.GRAY);

        pnlHeader.add(lblEmisor, BorderLayout.WEST);
        pnlHeader.add(lblReceptor, BorderLayout.EAST);

        lblTexto = new JLabel();

        add(pnlHeader, BorderLayout.NORTH);
        add(lblTexto, BorderLayout.CENTER);
    }

    @Override
    public Component getListCellRendererComponent(JList<?> list,
                                                  Object value,
                                                  int index,
                                                  boolean isSelected,
                                                  boolean cellHasFocus) {
        String emisorDisplay;
        String receptorDisplay;
        Mensaje mensaje;

        if (value instanceof ResultadoBusqueda) {
            ResultadoBusqueda resultado = (ResultadoBusqueda) value;
            mensaje = resultado.getMensaje();
            emisorDisplay = resultado.getEmisor();
            receptorDisplay = resultado.getReceptor();
        } else {
            // Fallback
            lblEmisor.setText("Error");
            lblReceptor.setText("Error");
            lblTexto.setText("Tipo de dato no soportado");
            return this;
        }

        lblEmisor.setText(emisorDisplay);
        lblReceptor.setText(receptorDisplay);
        lblTexto.setText(mensaje.isEmoji() ? "Emoji: " + mensaje.getCodigoEmoji() : mensaje.getTexto());
        
        setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
        setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());

        return this;
    }
}
