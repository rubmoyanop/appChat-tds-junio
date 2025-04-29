package umu.tds.appchat.dao;

import umu.tds.appchat.modelo.ContactoIndividual;

public interface ContactoIndividualDAO {

    void registrarContactoIndividual(ContactoIndividual contacto) throws DAOExcepcion;

    ContactoIndividual recuperarContactoIndividual(int id) throws DAOExcepcion;

    void modificarContactoIndividual(ContactoIndividual contacto) throws DAOExcepcion; // Para actualizar mensajes

    void borrarContactoIndividual(ContactoIndividual contacto) throws DAOExcepcion;
}