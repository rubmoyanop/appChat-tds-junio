package umu.tds.appchat.modelo;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class Usuario {
    private int id; 
    private String nombre;
    private String movil;
    private String contrasena;
    private String imagen;
    private LocalDate fechaNacimiento; 
    private String saludo;
    private boolean isPremium;
    private List<Contacto> contactos;

    public Usuario(int id, String nombre, String movil, String contrasena, String imagen, LocalDate fechaNacimiento, String saludo, List<Contacto> contactos,  boolean isPremium) {
        this.id = id;
        this.nombre = nombre;
        this.movil = movil;
        this.contrasena = contrasena;
        this.imagen = imagen;
        this.fechaNacimiento = fechaNacimiento;
        this.saludo = saludo;
        this.contactos = contactos;
        this.isPremium = isPremium;
    }

    public Usuario(String nombre, String movil, String contrasena, String imagen, boolean isPremium) {
        this.nombre = nombre;
        this.movil = movil;
        this.contrasena = contrasena;
        this.imagen = imagen;
        this.isPremium = isPremium;
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public LocalDate getFechaNacimiento() {
        return this.fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getSaludo(){
        return this.saludo;
    }

    public void setSaludo(String saludo) {
        this.saludo = saludo;
    }

    public List<Contacto> getContactos() {
        return Collections.unmodifiableList(contactos);
    }

    public void setContactos(List<Contacto> contactos) {
        this.contactos = contactos;
    }
}
