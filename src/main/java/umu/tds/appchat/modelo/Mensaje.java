package umu.tds.appchat.modelo;

import java.time.LocalDateTime;

public class Mensaje {
    private int id;
    private String texto;
    private LocalDateTime fechaHora;
    private TipoMensaje tipo;
    private int codigoEmoji; // Los mensajes que no son emojis tienen -1 en este campo


    public Mensaje(String texto, LocalDateTime fechaHora, TipoMensaje tipo) {
        this.id = 0;
        this.texto = texto;
        this.fechaHora = fechaHora;
        this.tipo = tipo;
        this.codigoEmoji = -1; // Los mensajes que no son emojis tienen -1 en este campo
    }

    public Mensaje(int codigoEmoji, LocalDateTime fechaHora, TipoMensaje tipo) {
        this.id = 0;
        this.texto = ""; 
        this.fechaHora = fechaHora;
        this.tipo = tipo;
        this.codigoEmoji = codigoEmoji;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTexto() {
        return isEmoji() ? "[Emoji]" : texto;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public TipoMensaje getTipo() {
        return tipo;
    }

    public int getCodigoEmoji() {
        return codigoEmoji;
    }

    public boolean isEmoji() {
        return codigoEmoji != -1;
    }
}
