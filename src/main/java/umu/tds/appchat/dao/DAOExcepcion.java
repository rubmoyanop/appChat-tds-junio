package umu.tds.appchat.dao;

/**
 * Excepción específica para errores ocurridos en la capa DAO.
 */
public class DAOExcepcion extends Exception {

    private static final long serialVersionUID = 1L;

    public DAOExcepcion(String message) {
        super(message);
    }

    public DAOExcepcion(String message, Throwable cause) {
        super(message, cause);
    }
}