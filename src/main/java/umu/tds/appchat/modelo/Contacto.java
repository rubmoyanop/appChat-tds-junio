package umu.tds.appchat.modelo;
import java.util.List;

public abstract class Contacto {
    private List<Mensaje> mensajes;

    public Contacto(List<Mensaje> mensajes) {
        this.mensajes = mensajes;
    }

    public List<Mensaje> getMensajes() { return mensajes; }
    public void setMensajes(List<Mensaje> mensajes) { this.mensajes = mensajes; }
}