package umu.tds.appchat.modelo;

public class ResultadoBusqueda {
    private Mensaje mensaje;
    private String emisor;
    private String receptor;
    
    public ResultadoBusqueda(Mensaje mensaje, String emisor, String receptor) {
        this.mensaje = mensaje;
        this.emisor = emisor;
        this.receptor = receptor;
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
}
