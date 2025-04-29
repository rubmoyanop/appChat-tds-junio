package umu.tds.appchat.controlador;
import umu.tds.appchat.dao.*;
import umu.tds.appchat.modelo.*;

public enum AppChat {
    INSTANCE;
   public static double COSTE_PREMIUM = 100.0;
   public static final String DAO_TDS = "um.tds.appchat.persistencia.impl.FactoriaDAO_TDS";
   private FactoriaDAO factoriaDAO;
   private UsuarioDAO usuarioDAO;
   private ContactoIndividualDAO contactoIndividualDAO;
   private GrupoDAO grupoDAO;
   private MensajeDAO mensajeDAO;
   private Usuario usuarioActual;
   private AppChat() {
    try {
        factoriaDAO = FactoriaDAO.getInstancia(DAO_TDS);
    } 
    catch (DAOExcepcion e) {
        e.printStackTrace();
    }
    
    usuarioDAO = factoriaDAO.getUsuarioDAO();
    contactoIndividualDAO = factoriaDAO.getContactoIndividualDAO();
    grupoDAO = factoriaDAO.getGrupoDAO();
    mensajeDAO = factoriaDAO.getMensajeDAO();
   }
}
