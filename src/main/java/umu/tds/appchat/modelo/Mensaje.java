package umu.tds.appchat.modelo;

public class Mensaje {
    private String texto;
    private String hora;
    private String emoticon;
    private String tipo;

    public Mensaje(String texto, String hora, String emoticon, String tipo) {
        this.texto = texto;
        this.hora = hora;
        this.emoticon = emoticon;
        this.tipo = tipo;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getEmoticon() {
        return emoticon;
    }

    public void setEmoticon(String emoticon) {
        this.emoticon = emoticon;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
