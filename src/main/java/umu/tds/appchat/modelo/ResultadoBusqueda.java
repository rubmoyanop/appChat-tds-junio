package umu.tds.appchat.modelo;

public class ResultadoBusqueda {
    private Mensaje mensaje;
    private String emisor;
    private String receptor;
    private Contacto contacto; 
    
    public ResultadoBusqueda(Mensaje mensaje, String emisor, String receptor, Contacto contacto) { // MODIFIED
        this.mensaje = mensaje;
        this.emisor = emisor;
        this.receptor = receptor;
        this.contacto = contacto; 
    }
    
    public Mensaje getMensaje() {
        return mensaje;
    }
    
    public String getEmisor() {
        return emisor;
    }
    
    public String getReceptor() {
        return receptor;
    }

    public Contacto getContacto() {
        return contacto;
    }
}
