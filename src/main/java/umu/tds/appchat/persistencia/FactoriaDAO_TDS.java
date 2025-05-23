package umu.tds.appchat.persistencia;

import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import umu.tds.appchat.dao.*;
import umu.tds.appchat.modelo.Grupo;

/**
 * Implementación concreta de FactoriaDAO para el servicio de persistencia TDS.
 */
public final class FactoriaDAO_TDS extends FactoriaDAO {

    private UsuarioDAO_TDS usuarioDAO;
    private ContactoIndividualDAO_TDS contactoDAO;
    private MensajeDAO_TDS mensajeDAO; 

    /**
     * Constructor
     */
    public FactoriaDAO_TDS() {
        ServicioPersistencia serv = FactoriaServicioPersistencia.getInstance().getServicioPersistencia();
        usuarioDAO = new UsuarioDAO_TDS();
        mensajeDAO = new MensajeDAO_TDS(); 
        contactoDAO = new ContactoIndividualDAO_TDS(serv, usuarioDAO, mensajeDAO); 
        usuarioDAO.setContactoIndividualDAO(contactoDAO);
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
        return new GrupoDAO_TDS(); 
    }

    @Override
    public MensajeDAO getMensajeDAO() {
       return mensajeDAO;
    }
}

class GrupoDAO_TDS implements GrupoDAO {
    @Override public void registrarGrupo(Grupo grupo) throws DAOExcepcion {}
    @Override public Grupo recuperarGrupo(int id) throws DAOExcepcion { return null; }
    @Override public void modificarGrupo(Grupo grupo) throws DAOExcepcion {}
    @Override public void borrarGrupo(Grupo grupo) throws DAOExcepcion {}
}
