package umu.tds.appchat.modelo;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

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
    
    /**
     * Verifica si un contacto individual es miembro del grupo.
     * @param contacto El contacto a verificar.
     * @return true si es miembro, false en caso contrario.
     */
    public boolean esMiembro(ContactoIndividual contacto) {
        if (contacto == null || miembros == null) return false;
        return miembros.stream().anyMatch(miembro -> 
            miembro.getId() == contacto.getId() || 
            (miembro.getUsuario() != null && contacto.getUsuario() != null && 
             miembro.getUsuario().getMovil().equals(contacto.getUsuario().getMovil()))
        );
    }
    
    /**
     * Obtiene un set de teléfonos de los miembros del grupo.
     * @return Set con los moviles de los miembros.
     */
    public Set<String> getMiembrosMoviles() {
        Set<String> moviles = new HashSet<>();
        if (miembros != null) {
            for (ContactoIndividual miembro : miembros) {
                moviles.add(miembro.getMovil());
            }
        }
        return moviles;
    }
}
