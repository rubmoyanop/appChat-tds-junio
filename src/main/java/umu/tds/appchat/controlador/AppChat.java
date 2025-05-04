package umu.tds.appchat.controlador;
import java.time.LocalDate; 
import java.time.ZoneId; 
import java.time.LocalDateTime;
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
        this.contactoIndividualDAO = factoriaDAO.getContactoIndividualDAO();
    } 
    catch (DAOExcepcion e) {
        e.printStackTrace();
        System.err.println("Error inicializando la capa de persistencia. La aplicación no puede continuar.");
        System.exit(1); 
    }
    
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

   public boolean agregarContactoIndividual(String nombre, String movil) throws DAOExcepcion {
        if (usuarioActual == null) {
            throw new IllegalStateException("Debe iniciar sesión para agregar contactos.");
        }
        if(nombre == null || nombre.isBlank() || movil == null || movil.isBlank()) {
            throw new IllegalArgumentException("Los campos de nombre y móvil son obligatorios.");
        }

        //Comprobamos que el usuario esté registrado
        Usuario nuevoUsuarioContacto = usuarioDAO.recuperarUsuarioPorMovil(movil);
        if (nuevoUsuarioContacto == null) {
            return false; 
        }

        //Comprobamos que el contacto no esté ya agregado en contactos del usuario actual
        if(usuarioActual.buscarContactoIndividualPorMovil(movil) != null) {
            throw new IllegalArgumentException("Ya tienes este contacto agregado.");
        }

        ContactoIndividual contacto = new ContactoIndividual(nombre, nuevoUsuarioContacto);

        // Registrar el nuevo contacto individual en la persistencia
        contactoIndividualDAO.registrarContactoIndividual(contacto);

        // Agregamos el contacto a la lista de contactos del usuario actual en memoria
        usuarioActual.agregarContacto(contacto);

        // Actualizar el usuario actual en la persistencia para guardar la nueva relación de contacto
        usuarioDAO.modificarUsuario(usuarioActual);

        return true;
    }

    public void enviarMensaje(ContactoIndividual contactoDestino, String texto) throws DAOExcepcion {
        if (usuarioActual == null) {
            throw new IllegalStateException("Debe iniciar sesión para enviar mensajes.");
        }
        if (contactoDestino == null || texto == null || texto.isBlank()) {
            throw new IllegalArgumentException("Contacto y texto son obligatorios.");
        }

        // 1. Crear y enviar mensaje ENVIADO
        Mensaje msgEnviado = new Mensaje(texto, LocalDateTime.now(), TipoMensaje.ENVIADO);
        contactoDestino.agregarMensaje(msgEnviado);
        mensajeDAO.registrarMensaje(msgEnviado);
        contactoIndividualDAO.modificarContactoIndividual(contactoDestino);

        // 2. Simular recepción en el otro usuario
        Usuario receptor = usuarioDAO.recuperarUsuarioPorMovil(
            contactoDestino.getMovil()
        );
        if (receptor != null) {
            ContactoIndividual contactoReceptor = receptor.buscarContactoIndividualPorMovil(usuarioActual.getMovil());
            if (contactoReceptor == null) {
                contactoReceptor = new ContactoIndividual("", usuarioActual);
                contactoIndividualDAO.registrarContactoIndividual(contactoReceptor);
                receptor.agregarContacto(contactoReceptor);
                usuarioDAO.modificarUsuario(receptor);
            }
            Mensaje msgRecibido = new Mensaje(texto, LocalDateTime.now(), TipoMensaje.RECIBIDO);
            contactoReceptor.agregarMensaje(msgRecibido);
            mensajeDAO.registrarMensaje(msgRecibido);
            contactoIndividualDAO.modificarContactoIndividual(contactoReceptor);
        }
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
