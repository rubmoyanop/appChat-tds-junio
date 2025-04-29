package umu.tds.appchat.modelo;

public class ContactoIndividual extends Contacto{
    private Usuario usuario;

    public ContactoIndividual(Usuario usuario) {
        super(null);
        this.usuario = usuario;
    }

    public Usuario getUsuario() {
        return this.usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

}
