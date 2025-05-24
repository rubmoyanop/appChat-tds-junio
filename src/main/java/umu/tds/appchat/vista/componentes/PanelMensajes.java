package umu.tds.appchat.vista.componentes;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import tds.BubbleText;
import umu.tds.appchat.vista.listeners.EmojiSelectionListener;
import umu.tds.appchat.modelo.Contacto;
import umu.tds.appchat.modelo.Mensaje;
import umu.tds.appchat.modelo.TipoMensaje;
import umu.tds.appchat.controlador.AppChat;
import umu.tds.appchat.dao.DAOExcepcion;

/**
 * Panel que muestra el área de mensajes y el área de envío.
 */
public class PanelMensajes extends JPanel implements EmojiSelectionListener {

    private JPanel chatPanel; // Panel que contiene las burbujas de mensajes
    private JTextArea messageInput; // Área de texto para escribir mensajes
    private JButton sendButton; // Botón para enviar mensajes
    private Contacto contactoDestino; // Contacto destino para enviar mensajes

    public PanelMensajes() {
        super(new BorderLayout());
        System.out.println(BubbleText.getVersion());
        initialize();
    }

    /**
     * Setter para que VentanaPrincipal informe el contacto seleccionado.
     * @param contacto El contacto destino.
     */
    public void setContactoDestino(Contacto contacto) {
        this.contactoDestino = contacto;
    }

    private void initialize() {
        setBackground(Color.WHITE);

        // Etiqueta de título
        JLabel lblMensajesTitle = new JLabel("Mensajes");
        lblMensajesTitle.setFont(new Font("Arial", Font.BOLD, 16));
        lblMensajesTitle.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
                new EmptyBorder(5, 10, 5, 10)
        ));
        lblMensajesTitle.setHorizontalAlignment(SwingConstants.CENTER);
        add(lblMensajesTitle, BorderLayout.NORTH);

        // Panel de chat
        chatPanel = new JPanel() {
            @Override
            public Dimension getPreferredSize() {
                return super.getPreferredSize();
            }
        };
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
        chatPanel.setBackground(Color.WHITE);

        // ScrollPane para el panel de chat
        JScrollPane scrollPane = new JScrollPane(chatPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);

        // Panel de entrada de mensajes
        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        inputPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Relleno
        inputPanel.setBackground(Color.WHITE);

        // Área de texto para escribir mensajes
        messageInput = new JTextArea();
        messageInput.setLineWrap(true); // Ajuste de línea automático
        messageInput.setWrapStyleWord(true); // No corta palabras al final de línea
        messageInput.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        // Listener para Ctrl + Enter -> Envío Mensaje
        messageInput.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_ENTER) {
                    e.consume(); // Evita que el Enter normal inserte una nueva línea
                    sendButton.doClick(); // Simula el clic en el botón de enviar
                }
            }
        });

        // Autoajuste de tamaño del área de texto
        messageInput.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { ajustarTamañoAreaTexto(); }
            @Override public void removeUpdate(DocumentEvent e) { ajustarTamañoAreaTexto(); }
            @Override public void changedUpdate(DocumentEvent e) { ajustarTamañoAreaTexto(); }
        });

        // ScrollPane para el área de texto de entrada
        JScrollPane inputScrollPane = new JScrollPane(messageInput);
        inputScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        inputScrollPane.setPreferredSize(new Dimension(0, 30));
        inputPanel.add(inputScrollPane, BorderLayout.CENTER);

        // Panel para los botones (Enviar y Emoji)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0)); // Alineación derecha, sin espacio extra
        buttonPanel.setBackground(Color.WHITE);

        // Crear e instanciar el EmojiPanel, pasando este panel como listener
        EmojiPanel emojiPanel = new EmojiPanel(this);
        buttonPanel.add(emojiPanel);

        // Botón de enviar
        sendButton = new JButton("Enviar");
        sendButton.addActionListener(e -> {
            String text = getInputText().trim();
            if (text.isEmpty()) return;

            if (contactoDestino == null) {
                JOptionPane.showMessageDialog(this,
                    "Seleccione un contacto antes de enviar.",
                    "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                AppChat.INSTANCE.enviarMensaje(contactoDestino, text);
                addMessageBubble(text, Color.GREEN, "Yo", BubbleText.SENT);
                clearInputText();
                ajustarTamañoAreaTexto();
            } catch (DAOExcepcion ex) {
                JOptionPane.showMessageDialog(this,
                    "Error enviando mensaje: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });
        buttonPanel.add(Box.createHorizontalStrut(5)); // Espacio entre botones
        buttonPanel.add(sendButton);

        inputPanel.add(buttonPanel, BorderLayout.EAST);

        add(inputPanel, BorderLayout.SOUTH);
    }

    /**
     * Añade una burbuja de mensaje de texto al panel de chat.
     * @param text El texto del mensaje.
     * @param color El color de fondo de la burbuja.
     * @param name El nombre del remitente.
     * @param type BubbleText.SENT o BubbleText.RECEIVED.
     */
    public void addMessageBubble(String text, Color color, String name, int type) {
        SwingUtilities.invokeLater(() -> {
            BubbleText bubble = new BubbleText(chatPanel, text, color, name, type);
            chatPanel.add(bubble);
            chatPanel.add(Box.createVerticalStrut(5)); // Para añadir espaciado vertical entre burbujas
            chatPanel.revalidate();
            chatPanel.repaint();
            // Desplaza el scroll al final tras añadir el mensaje
            scrollToBottom();
        });
    }

    /**
     * Añade una burbuja de emoji al panel de chat.
     * @param codigoEmoji El código entero del emoji.
     * @param color El color de fondo de la burbuja.
     * @param name El nombre del remitente.
     * @param type BubbleText.SENT o BubbleText.RECEIVED.
     * @param fontSize El tamaño de fuente (afecta al tamaño del emoji).
     */
    public void addEmojiBubble(int codigoEmoji, Color color, String name, int type, int fontSize) {
        SwingUtilities.invokeLater(() -> {
            BubbleText bubble = new BubbleText(chatPanel, codigoEmoji, color, name, type, fontSize);
            chatPanel.add(bubble);
            chatPanel.add(Box.createVerticalStrut(5)); // Espaciado vertical entre burbujas
            chatPanel.revalidate();
            chatPanel.repaint();
            // Desplaza el scroll al final tras añadir el mensaje
            scrollToBottom();
        });
    }

    /**
     * Limpia todas las burbujas del chat.
     */
    public void clearMensajes() {
        SwingUtilities.invokeLater(() -> {
            chatPanel.removeAll();
            chatPanel.revalidate();
            chatPanel.repaint();
        });
    }

    /**
     * Carga y muestra en el chat todos los mensajes de un contacto.
     * @param contacto El contacto cuyos mensajes se van a cargar.
     */
    public void addMensajesContacto(Contacto contacto) {
        SwingUtilities.invokeLater(() -> {
            for (Mensaje m : contacto.getMensajes()) {
                if (m.getTipo() == TipoMensaje.ENVIADO) {
                    if (m.isEmoji()) {
                        addEmojiBubble(m.getCodigoEmoji(), Color.GREEN, "Yo", BubbleText.SENT, 16);
                    } else {
                        addMessageBubble(m.getTexto(), Color.GREEN, "Yo", BubbleText.SENT);
                    }
                } else { // RECIBIDO
                    if (m.isEmoji()) {
                        addEmojiBubble(m.getCodigoEmoji(), Color.LIGHT_GRAY, contacto.getNombre(), BubbleText.RECEIVED, 16);
                    } else {
                        addMessageBubble(m.getTexto(), Color.LIGHT_GRAY, contacto.getNombre(), BubbleText.RECEIVED);
                    }
                }
            }
            // Desplaza el scroll al final tras cargar todos los mensajes
            scrollToBottom();
        });
    }

    /**
     * Desplaza el panel de chat hasta el final para mostrar el último mensaje.
     */
    private void scrollToBottom() {
        JScrollPane scrollPane = (JScrollPane) SwingUtilities.getAncestorOfClass(JScrollPane.class, chatPanel);
        if (scrollPane != null) {
            JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
            SwingUtilities.invokeLater(() -> verticalScrollBar.setValue(verticalScrollBar.getMaximum()));
        }
    }

    /**
     * Devuelve el texto actual del área de entrada.
     * @return El texto escrito por el usuario.
     */
    public String getInputText() {
        return messageInput.getText();
    }

    /**
     * Limpia el área de entrada de texto.
     */
    public void clearInputText() {
        messageInput.setText("");
    }

    /**
     * Ajusta dinámicamente la altura del área de entrada de texto según el número de líneas.
     * Limita la altura máxima.
     */
    private void ajustarTamañoAreaTexto() {
        SwingUtilities.invokeLater(() -> {
            JScrollPane scrollPane = (JScrollPane) SwingUtilities.getAncestorOfClass(JScrollPane.class, messageInput);
            if (scrollPane == null) return;

            int maxLines = 5; // Limitar a 5 líneas visibles antes de scroll
            int lineHeight = messageInput.getFontMetrics(messageInput.getFont()).getHeight();
            int currentLines = messageInput.getLineCount();

            int desiredHeight = Math.max(1, Math.min(currentLines, maxLines)) * lineHeight;

            Insets borderInsets = messageInput.getBorder().getBorderInsets(messageInput);
            desiredHeight += borderInsets.top + borderInsets.bottom + 4; // Añadir espacio para el borde y un pequeño margen

            Dimension prefSize = scrollPane.getPreferredSize();
            if (prefSize.height != desiredHeight) {
                scrollPane.setPreferredSize(new Dimension(prefSize.width, desiredHeight));
                scrollPane.getParent().revalidate(); // Revalidar el contenedor padre para aplicar el cambio de tamaño
                scrollPane.getParent().repaint(); // Repintar el contenedor padre
            }
        });
    }

    /**
     * Implementación de EmojiSelectionListener.
     * Se llama cuando se selecciona un emoji desde el EmojiPanel.
     * @param codigoEmoji El código del emoji seleccionado.
     */
    @Override
    public void onEmojiSeleccionado(int codigoEmoji) {
        if (contactoDestino == null) {
            JOptionPane.showMessageDialog(this,
                "Seleccione un contacto antes de enviar.",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            AppChat.INSTANCE.enviarEmoji(contactoDestino, codigoEmoji);
            addEmojiBubble(codigoEmoji, Color.GREEN, "Yo", BubbleText.SENT, 10);
        } catch (DAOExcepcion ex) {
            JOptionPane.showMessageDialog(this,
                "Error enviando emoji: " + ex.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } catch (IllegalArgumentException ex) {
             JOptionPane.showMessageDialog(this,
                ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
