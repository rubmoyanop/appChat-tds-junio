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
        this.usuarioDAO = factoriaDAO.getUsuarioDAO();
    } 
    catch (DAOExcepcion e) {
        e.printStackTrace();
        System.err.println("Error inicializando la capa de persistencia. La aplicación no puede continuar.");
        System.exit(1); 
    }
    
    contactoIndividualDAO = factoriaDAO.getContactoIndividualDAO();
    grupoDAO = factoriaDAO.getGrupoDAO();
    mensajeDAO = factoriaDAO.getMensajeDAO();
   }

   /**
     * Registra un nuevo usuario en el sistema.
     * @param nombre Nombre del usuario.
     * @param movil Número de móvil.
     * @param contrasena Contraseña.
     * @param contrasenaRepetida Confirmación de contraseña.
     * @param imagen Ruta de la imagen.
     * @param isPremium Si es premium.
     * @throws IllegalArgumentException Si los datos no son válidos.
     * @throws DAOExcepcion Si ocurre un error de persistencia.
     */
    public void registrarUsuario(String nombre, String movil, String contrasena, String contrasenaRepetida, String imagen, boolean isPremium) throws DAOExcepcion {
        // Validaciones de los datos de entrada
        if (nombre == null || nombre.isBlank() ||
            movil == null || movil.isBlank() ||
            contrasena == null || contrasena.isBlank() ||
            contrasenaRepetida == null || contrasenaRepetida.isBlank()) {
            throw new IllegalArgumentException("Todos los campos obligatorios deben estar completos.");
        }
        if (!contrasena.equals(contrasenaRepetida)) {
            throw new IllegalArgumentException("Las contraseñas no coinciden.");
        }
        // Comprobamos si el móvil ya existe
        if (usuarioDAO.recuperarUsuarioPorMovil(movil) != null) {
            throw new IllegalArgumentException("Ya existe un usuario con ese número de móvil.");
        }
        // Creamos y registramos el usuario
        Usuario usuario = new Usuario(nombre, movil, contrasena, imagen, isPremium);
        usuarioDAO.registrarUsuario(usuario);
    }
}
