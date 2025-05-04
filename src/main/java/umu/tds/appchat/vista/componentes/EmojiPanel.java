package umu.tds.appchat.vista.componentes;

import javax.swing.*;
import java.awt.*;
import tds.BubbleText;
import umu.tds.appchat.vista.listeners.EmojiSelectionListener;

/**
 * Panel que contiene un botón para seleccionar emojis.
 * Al hacer clic en el botón, se muestra un menú emergente con varios emojis.
 * Al seleccionar un emoji, se notifica al oyente correspondiente.
 */
public class EmojiPanel extends JPanel {

    private JButton emojiButton;
    private JPopupMenu emojiPopup;
    private EmojiSelectionListener listener;

    /**
    * Constructor de EmojiPanel.
     * @param listener El oyente que se notificará cuando se seleccione un emoji.
     */
    public EmojiPanel(EmojiSelectionListener listener) {
        super(new FlowLayout(FlowLayout.LEFT, 0, 0)); 
        this.listener = listener;
        setBackground(Color.WHITE);
        initialize();
        createEmojiPopup();
    }

    private void initialize() {
        // Botón de emoji
        emojiButton = new JButton("😀"); 
        emojiButton.setToolTipText("Enviar emoji");
        emojiButton.setMargin(new Insets(2, 5, 2, 5));
        emojiButton.addActionListener(e -> {
            if (emojiPopup != null) {
                emojiPopup.show(emojiButton, 0, -emojiPopup.getPreferredSize().height);
            }
        });
        add(emojiButton); 
    }

    /**
     * Crea el JPopupMenu que contendrá los botones de emoji.
     */
    private void createEmojiPopup() {
        emojiPopup = new JPopupMenu();
        int cols = 8; // Emojis por fila
        int numEmojis = BubbleText.MAXICONO + 1;
        int rows = (int) Math.ceil((double) numEmojis / cols);
        emojiPopup.setLayout(new GridLayout(rows, cols, 2, 2)); 

        for (int i = 0; i <= BubbleText.MAXICONO; i++) {
            final int emojiCode = i;
            ImageIcon emojiIcon = BubbleText.getEmoji(i);
            JButton btn = new JButton(emojiIcon);
            btn.setMargin(new Insets(0, 0, 0, 0));
            btn.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
            btn.setContentAreaFilled(false);
            btn.setFocusPainted(false);
            btn.setToolTipText("Emoji " + i);
            btn.addActionListener(e -> {
                if (listener != null) {
                    listener.onEmojiSeleccionado(emojiCode); // Notificar al oyente
                }
                emojiPopup.setVisible(false); 
            });
            emojiPopup.add(btn);
        }
    }
}
