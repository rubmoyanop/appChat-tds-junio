package umu.tds.appchat.dao;

import umu.tds.appchat.modelo.Usuario;
import java.util.List;

public interface UsuarioDAO {

    /**
     * Registra un nuevo usuario en el sistema de persistencia.
     * @param usuario El usuario a registrar.
     * @throws DAOExcepcion Si ocurre un error durante el registro.
     */
    void registrarUsuario(Usuario usuario) throws DAOExcepcion;

    /**
     * Recupera un usuario por su ID único.
     * @param id El ID del usuario.
     * @return El Usuario encontrado, o null si no existe.
     * @throws DAOExcepcion Si ocurre un error durante la recuperación.
     */
    Usuario recuperarUsuario(int id) throws DAOExcepcion;

    /**
     * Recupera un usuario por su número de móvil.
     * @param movil El número de móvil del usuario.
     * @return El Usuario encontrado, o null si no existe.
     * @throws DAOExcepcion Si ocurre un error durante la recuperación.
     */
    Usuario recuperarUsuarioPorMovil(String movil) throws DAOExcepcion;

    /**
     * Modifica los datos de un usuario existente.
     * @param usuario El usuario con los datos actualizados.
     * @throws DAOExcepcion Si ocurre un error durante la modificación.
     */
    void modificarUsuario(Usuario usuario) throws DAOExcepcion;

    /**
     * Borra un usuario del sistema.
     * @param usuario El usuario a borrar.
     * @throws DAOExcepcion Si ocurre un error durante el borrado.
     */
    void borrarUsuario(Usuario usuario) throws DAOExcepcion;

    /**
     * Recupera todos los usuarios registrados.
     * @return Una lista con todos los usuarios.
     * @throws DAOExcepcion Si ocurre un error durante la recuperación.
     */
    List<Usuario> recuperarTodosUsuarios() throws DAOExcepcion;
}