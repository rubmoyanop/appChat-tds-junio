package umu.tds.appchat.modelo;

import java.time.LocalDateTime;

public class Mensaje {
    private String texto;
    private LocalDateTime fechaHora;
    private TipoMensaje tipo;

    public Mensaje(String texto, LocalDateTime fechaHora, TipoMensaje tipo) {
        this.texto = texto;
        this.fechaHora = fechaHora;
        this.tipo = tipo;
    }

    public String getTexto() {
        return texto;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public TipoMensaje getTipo() {
        return tipo;
    }
}
