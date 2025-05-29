package umu.tds.appchat.modelo;

import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Usuario {
    private int id;
    private String nombre;
    private String email;
    private String movil;
    private String contrasena;
    private String imagen;
    private LocalDate fechaNacimiento;
    private LocalDate fechaRegistro;
    private String saludo;
    private boolean isPremium;
    private List<Contacto> contactos;

    public Usuario(String nombre, String email, LocalDate fechaNacimiento, LocalDate fechaRegistro, String movil, String contrasena, String imagen, String saludo, boolean isPremium) {
        this.id = 0;
        this.nombre = nombre;
        this.email = email;
        this.movil = movil;
        this.contrasena = contrasena;
        this.imagen = imagen;
        this.fechaNacimiento = fechaNacimiento;
        this.fechaRegistro = fechaRegistro;
        this.saludo = saludo;
        this.contactos = new LinkedList<>();
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public LocalDate getFechaRegistro() {
        return this.fechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getSaludo() {
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

    /**
     * Busca un ContactoIndividual en la lista de contactos por su número de móvil.
     * @param movil El número de móvil del contacto a buscar.
     * @return El ContactoIndividual si se encuentra, null en caso contrario.
     */
    public ContactoIndividual buscarContactoIndividualPorMovil(String movil) {
        return contactos.stream()
            .filter(c -> c instanceof ContactoIndividual)
            .map(c -> (ContactoIndividual) c)
            .filter(ci -> ci.getUsuario().getMovil().equals(movil))
            .findFirst()
            .orElse(null);
    }

    public void agregarContacto(Contacto contacto) {
        if (contacto != null) {
            contactos.add(contacto);
        }
    }
}
