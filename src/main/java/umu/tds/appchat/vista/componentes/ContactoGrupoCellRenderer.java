package umu.tds.appchat.vista.componentes;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JList;

import umu.tds.appchat.modelo.ContactoIndividual;
import umu.tds.appchat.vista.util.ImagenPerfilUtil;

/**
 * Clase que renderiza un contacto individual en una lista.
 * Muestra el nombre, número de móvil y la imagen de perfil del contacto.
 */
public class ContactoGrupoCellRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof ContactoIndividual) {
            ContactoIndividual contacto = (ContactoIndividual) value;
            setText(contacto.getNombre() + " (" + contacto.getMovil() + ")");
            
            // Intentar cargar imagen de perfil del usuario
            ImageIcon imagenPerfil = ImagenPerfilUtil.cargarImagenPerfil(
                contacto.getUsuario().getImagen(), 16, 16);
            
            if (imagenPerfil != null) {
                setIcon(imagenPerfil);
            } else {
                setIcon(null);
            }
        }
        return this;
    }
}
