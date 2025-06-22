package umu.tds.appchat.vista.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.net.URI;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * Clase utilitaria para cargar imágenes de perfil de usuarios.
 */
public class ImagenPerfilUtil {
    
    /**
     * Carga una imagen desde una URL.
     * 
     * @param imageUrl La URL de la imagen
     * @return ImageIcon o null si hay error
     */
    public static ImageIcon cargarImagenPerfil(String imageUrl) {
        if (imageUrl == null || imageUrl.trim().isEmpty()) {
            return null;
        }
        
        try {
            URL url = URI.create(imageUrl.trim()).toURL();
            BufferedImage originalImage = ImageIO.read(url);
            
            if (originalImage != null) {
                return new ImageIcon(originalImage);
            }
        } catch (Exception e) {
            System.err.println("Error cargando imagen desde URL: " + imageUrl + " - " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Carga una imagen desde una URL y la redimensiona al tamaño especificado.
     * 
     * @param imageUrl La URL de la imagen
     * @param width Ancho deseado
     * @param height Alto deseado
     * @return ImageIcon redimensionado o null si hay error
     */
    public static ImageIcon cargarImagenPerfil(String imageUrl, int width, int height) {
        ImageIcon imagen = cargarImagenPerfil(imageUrl);
        if (imagen != null) {
            Image scaledImage = imagen.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        }
        return null;
    }
    
    /**
     * Verifica si una URL de imagen es válida (no nula ni vacía).
     * 
     * @param imageUrl La URL a verificar
     * @return true si la URL es válida, false en caso contrario
     */
    public static boolean esUrlValida(String imageUrl) {
        return imageUrl != null && !imageUrl.trim().isEmpty();
    }
}