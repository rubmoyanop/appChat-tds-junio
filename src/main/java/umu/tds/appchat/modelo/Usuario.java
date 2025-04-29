package umu.tds.appchat.modelo;

public class Usuario {
    private String nombre;
    private String movil;
    private String contrasena;
    private String imagen;
    private boolean isPremium;

    Usuario(String nombre, String movil, String contrasena, String imagen, boolean isPremium) {
        this.nombre = nombre;
        this.movil = movil;
        this.contrasena = contrasena;
        this.imagen = imagen;
        this.isPremium = isPremium;
    }

    // Getters y Setters
    public String getNombre() {
         return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getMovil() {
        return movil;
    }

    public void setMovil(String movil) {
        this.movil = movil;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public boolean isPremium() {
        return isPremium;
    }
    
    public void setPremium(boolean isPremium) {
        this.isPremium = isPremium;
    }
}
