package umu.tds.appchat.persistencia;

import java.util.List;

import umu.tds.appchat.dao.*;
import umu.tds.appchat.modelo.Grupo;
import umu.tds.appchat.modelo.Mensaje;

/**
 * Implementación concreta de FactoriaDAO para el servicio de persistencia TDS.
 */
public final class FactoriaDAO_TDS extends FactoriaDAO {

    /**
     * Constructor
     */
    public FactoriaDAO_TDS() {
        
    }

    @Override
    public UsuarioDAO getUsuarioDAO() {
        return new UsuarioDAO_TDS(); 
    }

    @Override
    public ContactoIndividualDAO getContactoIndividualDAO() {
       return new ContactoIndividualDAO_TDS(); 
    }

    @Override
    public GrupoDAO getGrupoDAO() {
        return new GrupoDAO_TDS(); 
    }

    @Override
    public MensajeDAO getMensajeDAO() {
       return new MensajeDAO_TDS(); 
    }
}

class GrupoDAO_TDS implements GrupoDAO {
    @Override public void registrarGrupo(Grupo grupo) throws DAOExcepcion {}
    @Override public Grupo recuperarGrupo(int id) throws DAOExcepcion { return null; }
    @Override public void modificarGrupo(Grupo grupo) throws DAOExcepcion {}
    @Override public void borrarGrupo(Grupo grupo) throws DAOExcepcion {}
}

class MensajeDAO_TDS implements MensajeDAO {
     @Override public void registrarMensaje(Mensaje mensaje) throws DAOExcepcion {}
     @Override public Mensaje recuperarMensaje(int id) throws DAOExcepcion { return null; }
     @Override public void modificarMensaje(Mensaje mensaje) throws DAOExcepcion {}
     @Override public void borrarMensaje(Mensaje mensaje) throws DAOExcepcion {}
     @Override public List<Mensaje> recuperarTodosMensajes() throws DAOExcepcion { return java.util.Collections.emptyList();}
}
