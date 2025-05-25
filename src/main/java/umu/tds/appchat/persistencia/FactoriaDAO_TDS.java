package umu.tds.appchat.persistencia;

import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import umu.tds.appchat.dao.*;

/**
 * Implementación concreta de FactoriaDAO para el servicio de persistencia TDS.
 */
public final class FactoriaDAO_TDS extends FactoriaDAO {

    private UsuarioDAO_TDS usuarioDAO;
    private ContactoIndividualDAO_TDS contactoDAO;
    private MensajeDAO_TDS mensajeDAO; 
    private GrupoDAO_TDS grupoDAO;

    /**
     * Constructor
     */
    public FactoriaDAO_TDS() {
        ServicioPersistencia serv = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
        usuarioDAO = new UsuarioDAO_TDS();
        mensajeDAO = new MensajeDAO_TDS(); 
        contactoDAO = new ContactoIndividualDAO_TDS(serv, usuarioDAO, mensajeDAO); 
        grupoDAO = new GrupoDAO_TDS(); 
        
        usuarioDAO.setContactoIndividualDAO(contactoDAO);
        usuarioDAO.setGrupoDAO(grupoDAO);
        grupoDAO.setContactoIndividualDAO(contactoDAO);
    }

    @Override
    public UsuarioDAO getUsuarioDAO() {
        return usuarioDAO;
    }

    @Override
    public ContactoIndividualDAO getContactoIndividualDAO() {
        return contactoDAO;
    }

    @Override
    public GrupoDAO getGrupoDAO() {
        return grupoDAO; 
    }

    @Override
    public MensajeDAO getMensajeDAO() {
       return mensajeDAO;
    }
}

