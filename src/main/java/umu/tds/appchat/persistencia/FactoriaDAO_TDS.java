package umu.tds.appchat.persistencia;

import java.util.List;

import umu.tds.appchat.dao.*;
import umu.tds.appchat.modelo.ContactoIndividual;
import umu.tds.appchat.modelo.Grupo;
import umu.tds.appchat.modelo.Mensaje;
import umu.tds.appchat.modelo.Usuario;

/**
 * Implementación concreta de FactoriaDAO para el servicio de persistencia TDS.
 */
public final class FactoriaDAO_TDS extends FactoriaDAO {

    /**
     * Constructor
     */
    public FactoriaDAO_TDS() {
        // Más adelante se inicizalizará la conexión al servicio de persistencia TDS
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

// Estas clases se implementarán completamente en issues posteriores. Se deja así para que compile de momento.

class UsuarioDAO_TDS implements UsuarioDAO {
    @Override public void registrarUsuario(Usuario usuario) throws DAOExcepcion { /* Implementación futura */ }
    @Override public Usuario recuperarUsuario(int id) throws DAOExcepcion { /* Implementación futura */ return null;}
    @Override public Usuario recuperarUsuarioPorMovil(String movil) throws DAOExcepcion { /* Implementación futura */ return null; }
    @Override public void modificarUsuario(Usuario usuario) throws DAOExcepcion { /* Implementación futura */ }
    @Override public void borrarUsuario(Usuario usuario) throws DAOExcepcion { /* Implementación futura */ }
    @Override public List<Usuario> recuperarTodosUsuarios() throws DAOExcepcion { /* Implementación futura */ return java.util.Collections.emptyList();}
}

class ContactoIndividualDAO_TDS implements ContactoIndividualDAO {
    @Override public void registrarContactoIndividual(ContactoIndividual contacto) throws DAOExcepcion {}
    @Override public ContactoIndividual recuperarContactoIndividual(int id) throws DAOExcepcion { return null; }
    @Override public void modificarContactoIndividual(ContactoIndividual contacto) throws DAOExcepcion {}
    @Override public void borrarContactoIndividual(ContactoIndividual contacto) throws DAOExcepcion {}
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
