package umu.tds.appchat.modelo;

import java.time.LocalDateTime;

public class Mensaje {
    private int id; 
    private String texto;
    private LocalDateTime fechaHora;
    private TipoMensaje tipo;

    public Mensaje(String texto, LocalDateTime fechaHora, TipoMensaje tipo) {
        this.id = 0; 
        this.texto = texto;
        this.fechaHora = fechaHora;
        this.tipo = tipo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
