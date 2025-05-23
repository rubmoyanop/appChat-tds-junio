package umu.tds.appchat.vista.componentes;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import umu.tds.appchat.modelo.ContactoIndividual;

public class ContactoIndividualListCellRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof ContactoIndividual) {
            ContactoIndividual contacto = (ContactoIndividual) value;
            setText(contacto.getNombre() + " (" + contacto.getMovil() + ")");
        }
        return this;
    }
}
