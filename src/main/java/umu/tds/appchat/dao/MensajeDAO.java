package umu.tds.appchat.dao;

import umu.tds.appchat.modelo.Mensaje;
import java.util.List;

public interface MensajeDAO {

    void registrarMensaje(Mensaje mensaje) throws DAOExcepcion;

    Mensaje recuperarMensaje(int id) throws DAOExcepcion;

    // Modificar y borrar mensajes es menos común, pero posible
    void modificarMensaje(Mensaje mensaje) throws DAOExcepcion;
    void borrarMensaje(Mensaje mensaje) throws DAOExcepcion;

    List<Mensaje> recuperarTodosMensajes() throws DAOExcepcion; // Probablemente no necesario
}