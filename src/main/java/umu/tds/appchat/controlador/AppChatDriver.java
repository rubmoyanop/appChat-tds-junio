package umu.tds.appchat.controlador;

import umu.tds.appchat.dao.DAOExcepcion;
import java.util.Date; 

public class AppChatDriver {
    public static void main(String[] args) {
        AppChat appChat = AppChat.INSTANCE;

        // Prueba del método registrarUsuario
        try {
            System.out.println("Registrando usuario...");
            // Actualizar la llamada para incluir todos los parámetros
            appChat.registrarUsuario(
                "Juan Pérez",                 
                "juan.perez@example.com",     
                new Date(),                   
                "123456789",
                "password123",
                "password123",
                "ruta/imagen.jpg",        
                "Hola!",                  
                false                  
            );
            System.out.println("Usuario registrado exitosamente.");
        } catch (IllegalArgumentException e) {
            System.err.println("Error en el registro: " + e.getMessage());
        } catch (DAOExcepcion e) {
            System.err.println("Error de persistencia: " + e.getMessage());
            e.printStackTrace(); // Imprimir stack trace para más detalles
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
