package umu.tds.appchat.vista.componentes;

import javax.swing.DefaultListModel;
import javax.swing.JPanel;

import umu.tds.appchat.modelo.ContactoIndividual;

public class ListaContactosPanel {
    JPanel panel;
        DefaultListModel<ContactoIndividual> selectedContactsModel;

        ListaContactosPanel(JPanel panel, DefaultListModel<ContactoIndividual> selectedContactsModel) {
            this.panel = panel;
            this.selectedContactsModel = selectedContactsModel;
        }
}
