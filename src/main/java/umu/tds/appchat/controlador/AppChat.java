package umu.tds.appchat.controlador;
import java.time.LocalDate; 
import java.time.ZoneId; 
import java.util.Date;

import umu.tds.appchat.dao.*;
import umu.tds.appchat.modelo.*;

public enum AppChat {
    INSTANCE;
   public static double COSTE_PREMIUM = 100.0;
   public static final String DAO_TDS = "umu.tds.appchat.persistencia.FactoriaDAO_TDS";
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
     * @param email Email del usuario (opcional).
     * @param fechaNac Fecha de nacimiento del usuario.
     * @param movil Número de móvil.
     * @param contrasena Contraseña.
     * @param contrasenaRepetida Confirmación de contraseña.
     * @param imagen Ruta de la imagen (opcional).
     * @param saludo Saludo del usuario (opcional).
     * @param isPremium Si es premium.
     * @return true si el registro fue exitoso, false si el móvil ya existe.
     * @throws IllegalArgumentException Si los datos de entrada no son válidos.
     * @throws DAOExcepcion Si ocurre un error de persistencia.
     */
    public boolean registrarUsuario(String nombre, String email, Date fechaNac, String movil, String contrasena, String contrasenaRepetida, String imagen, String saludo, boolean isPremium) throws DAOExcepcion {
        // Validaciones de los datos de entrada (lanzan IllegalArgumentException)
        if (nombre == null || nombre.isBlank() ||
            movil == null || movil.isBlank() ||
            contrasena == null || contrasena.isBlank() ||
            contrasenaRepetida == null || contrasenaRepetida.isBlank() ||
            fechaNac == null) {
            throw new IllegalArgumentException("Todos los campos obligatorios (nombre, móvil, contraseña, fecha nacimiento) deben estar completos.");
        }
        if (!contrasena.equals(contrasenaRepetida)) {
            throw new IllegalArgumentException("Las contraseñas no coinciden.");
        }
        if (email != null && !email.isBlank() && !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
             throw new IllegalArgumentException("El formato del email no es válido.");
        }
        // Comprobamos si el móvil ya existe (devuelve false si existe)
        if (usuarioDAO.recuperarUsuarioPorMovil(movil) != null) {
            return false; // Indica que el móvil ya está registrado
        }

        // Convertir java.util.Date a java.time.LocalDate
        LocalDate fechaNacimientoLocalDate = fechaNac.toInstant()
                                                     .atZone(ZoneId.systemDefault())
                                                     .toLocalDate();

        // Creamos y registramos el usuario
        Usuario usuario = new Usuario(nombre, email, fechaNacimientoLocalDate, movil, contrasena, imagen, saludo, isPremium);
        usuarioDAO.registrarUsuario(usuario); // Lanza DAOExcepcion si falla la persistencia
        return true; // Indica que el registro fue exitoso
    }

   /**
    * Intenta iniciar sesión con el móvil y contraseña dados.
    * @param movil Número de móvil.
    * @param password Contraseña.
    * @return true si el login es correcto, false en caso contrario.
    */
   public boolean login(String movil, String password) {
       if (movil == null || movil.isBlank() || password == null || password.isBlank()) {
           return false;
       }
       try {
           Usuario usuario = usuarioDAO.recuperarUsuarioPorMovil(movil);
           if (usuario != null && usuario.getContrasena().equals(password)) {
               this.usuarioActual = usuario;
               return true;
           }
       } catch (DAOExcepcion e) {
           e.printStackTrace();
       }
       this.usuarioActual = null;
       return false;
   }

   /**
    * Obtiene el usuario que ha iniciado sesión actualmente.
    * @return El Usuario actual, o null si nadie ha iniciado sesión.
    */
   public Usuario getUsuarioActual() {
       return usuarioActual;
   }

   /**
    * Cierra la sesión del usuario actual.
    */
   public void logout() {
       this.usuarioActual = null;
   }
}
