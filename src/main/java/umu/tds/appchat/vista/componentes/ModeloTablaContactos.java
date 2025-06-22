package umu.tds.appchat.vista.componentes;

import umu.tds.appchat.modelo.Contacto;
import umu.tds.appchat.modelo.ContactoIndividual;
import umu.tds.appchat.modelo.Grupo;
import umu.tds.appchat.vista.util.ImagenPerfilUtil;

import javax.swing.table.AbstractTableModel;
import javax.swing.ImageIcon;
import java.util.ArrayList;
import java.util.List;

public class ModeloTablaContactos extends AbstractTableModel {
    private final String[] columnNames = {"Foto", "Nombre", "Teléfono", "Saludo"};
    private List<Contacto> contactos;

    public ModeloTablaContactos(List<Contacto> contactos) {
        this.contactos = contactos != null ? new ArrayList<>(contactos) : new ArrayList<>();
    }

    @Override
    public int getRowCount() {
        return contactos.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int columna) {
        return columnNames[columna];
    }

    @Override
    public Object getValueAt(int fila, int columna) {
        Contacto contacto = contactos.get(fila);
        switch (columna) {
            case 0: // Columna de imagen
                if (contacto instanceof ContactoIndividual) {
                    ContactoIndividual ci = (ContactoIndividual) contacto;
                    ImageIcon imagenPerfil = ImagenPerfilUtil.cargarImagenPerfil(
                        ci.getUsuario().getImagen(), 32, 32);
                    return imagenPerfil; // Puede ser null si no hay imagen
                } else if (contacto instanceof Grupo) {
                    return null; // Los grupos no tienen imagen de perfil individual
                }
                return null;
            case 1: 
                return contacto.getNombre();
            case 2: 
                if (contacto instanceof ContactoIndividual) {
                    return ((ContactoIndividual) contacto).getMovil();
                } else if (contacto instanceof Grupo) {
                    return ""; // Los grupos no tienen teléfono 
                }
                return "";
            case 3: 
                if (contacto instanceof ContactoIndividual) {
                    return ((ContactoIndividual) contacto).getSaludo();
                } else if (contacto instanceof Grupo) {
                    return ""; // Los grupos no tienen saludo 
                }
                return "";
            default:
                return null;
        }
    }

    @Override
    public Class<?> getColumnClass(int columna) {
        if (columna == 0) {
            return ImageIcon.class; // Primera columna es para imágenes
        }
        return String.class; // Resto de columnas son Strings
    }

    public Contacto getContactoAt(int fila) {
        if (fila >= 0 && fila < contactos.size()) {
            return contactos.get(fila);
        }
        return null;
    }
}
