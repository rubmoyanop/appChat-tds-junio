package umu.tds.appchat.vista.listeners;

/**
 * Interfaz para escuchar eventos de selección de emojis.
 */
public interface EmojiSelectionListener {
    /**
     * Llamado cuando se selecciona un emoji.
     * @param emojiCode El código entero del emoji seleccionado.
     */
    void onEmojiSeleccionado(int emojiCode);
}
