package umu.tds.appchat.controlador;

import umu.tds.appchat.dao.DAOExcepcion;

public class AppChatDriver {
    public static void main(String[] args) {
        AppChat appChat = AppChat.INSTANCE;

        // Prueba del método registrarUsuario
        try {
            System.out.println("Registrando usuario...");
            appChat.registrarUsuario("Juan Pérez", "123456789", "password123", "password123", "ruta/imagen.jpg", false);
            System.out.println("Usuario registrado exitosamente.");
        } catch (IllegalArgumentException e) {
            System.err.println("Error en el registro: " + e.getMessage());
        } catch (DAOExcepcion e) {
            System.err.println("Error de persistencia: " + e.getMessage());
        }

        // Prueba del método login
        System.out.println("Intentando iniciar sesión...");
        boolean loginExitoso = appChat.login("123456789", "password123");
        if (loginExitoso) {
            System.out.println("Inicio de sesión exitoso.");
        } else {
            System.out.println("Inicio de sesión fallido.");
        }
    }
}
