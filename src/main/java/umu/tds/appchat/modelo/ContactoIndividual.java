package umu.tds.appchat.modelo;

public class ContactoIndividual extends Contacto{
    private Usuario usuario;

    public ContactoIndividual(int id, String nombre, Usuario usuario) {
        super(id, nombre, null); 
        this.usuario = usuario;
    }

    public ContactoIndividual(String nombre, Usuario usuario) {
        super(0, nombre, null); 
        this.usuario = usuario;
    }

    public String getMovil() {
        return this.usuario.getMovil();
    }

    public Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getSaludo() {
        return this.usuario.getSaludo();
    }

}
