package umu.tds.appchat.modelo;
import java.util.List;

public class Grupo extends Contacto{
    private List<ContactoIndividual> miembros;

    public Grupo(List<ContactoIndividual> miembros) {
        super(null); // Llamada al constructor de la clase padre Contacto
        this.miembros = miembros;
    }

    public List<ContactoIndividual> getMiembros() { return miembros; }
    public void setMiembros(List<ContactoIndividual> miembros) { this.miembros = miembros; }
}
