package umu.tds.appchat.vista.componentes;

import umu.tds.appchat.modelo.Contacto;
import umu.tds.appchat.modelo.ContactoIndividual;
import umu.tds.appchat.modelo.Grupo;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class ModeloTablaContactos extends AbstractTableModel {
    private final String[] columnNames = {"Nombre", "Teléfono", "Saludo"};
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
            case 0: 
                return contacto.getNombre();
            case 1: 
                if (contacto instanceof ContactoIndividual) {
                    return ((ContactoIndividual) contacto).getMovil();
                } else if (contacto instanceof Grupo) {
                    return ""; // Los grupos no tienen teléfono 
                }
                return "";
            case 2: 
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
        return String.class; // Todas las columnas mostrarán Strings
    }

    public Contacto getContactoAt(int fila) {
        if (fila >= 0 && fila < contactos.size()) {
            return contactos.get(fila);
        }
        return null;
    }
}
