package umu.tds.appchat.modelo;
import java.util.Collections;
import java.util.List;

public class Grupo extends Contacto{
    private List<ContactoIndividual> miembros;

    public Grupo(String nombre, List<ContactoIndividual> miembros) {
        super(0, nombre, null); 
        this.miembros = miembros;
    }

    public Grupo(List<ContactoIndividual> miembros) {
        super(0, null, null); 
        this.miembros = miembros;
    }

    public List<ContactoIndividual> getMiembros() { return Collections.unmodifiableList(miembros); }
    public void setMiembros(List<ContactoIndividual> miembros) { this.miembros = miembros; }
}
