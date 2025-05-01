package umu.tds.appchat.modelo;
import java.util.List;

public abstract class Contacto {
    private int id;
    private String nombre;
    private List<Mensaje> mensajes;

    public Contacto(int id) {
        this.id = id;
    }

    public Contacto(int id, String nombre, List<Mensaje> mensajes) {
        this.id = id;
        this.nombre = nombre;
        this.mensajes = mensajes;
    }

    public List<Mensaje> getMensajes() { return mensajes; }
    public void setMensajes(List<Mensaje> mensajes) { this.mensajes = mensajes; }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}