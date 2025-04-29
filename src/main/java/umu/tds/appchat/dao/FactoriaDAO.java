package umu.tds.appchat.dao;

/**
 * Factoría abstracta DAO. Proporciona acceso a los distintos DAO de la aplicación.
 * Sigue el patrón Singleton.
 */
public abstract class FactoriaDAO {

    private static FactoriaDAO unicaInstancia = null;

    // Identificador para la implementación concreta de TDS_DAO
    public static final String TDS_DAO = "umu.tds.appchat.persistencia.FactoriaDAO_TDS";

    /**
     * Constructor protegido para el patrón Singleton.
     */
    protected FactoriaDAO() { }

    /**
     * Obtiene la instancia única de la factoría DAO.
     * Si no existe, intenta crear la implementación por defecto (TDS_DAO).
     * @return La instancia única de la factoría.
     * @throws DAOExcepcion Si no se puede instanciar la factoría.
     */
    public static FactoriaDAO getInstancia() throws DAOExcepcion {
        if (unicaInstancia == null) {
            // Intenta crear la instancia por defecto
             try {
                unicaInstancia = (FactoriaDAO) Class.forName(TDS_DAO).getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new DAOExcepcion("Error al instanciar la factoría DAO por defecto: " + e.getMessage(), e);
            }
        }
        return unicaInstancia;
    }

   /**
     * Obtiene una instancia de una implementación concreta de la factoría DAO.
     * @param nombreFactoria Nombre completo de la clase de la factoría concreta.
     * @return La instancia de la factoría solicitada.
     * @throws DAOExcepcion Si no se puede instanciar la factoría.
     */
    public static FactoriaDAO getInstancia(String nombreFactoria) throws DAOExcepcion {
         if (unicaInstancia == null) {
             try {
                unicaInstancia = (FactoriaDAO) Class.forName(nombreFactoria).getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new DAOExcepcion("Error al instanciar la factoría DAO: " + nombreFactoria + " - " + e.getMessage(), e);
            }
         }
         return unicaInstancia;
    }

    // Métodos abstractos para obtener los DAOs específicos
    public abstract UsuarioDAO getUsuarioDAO();
    public abstract ContactoIndividualDAO getContactoIndividualDAO();
    public abstract GrupoDAO getGrupoDAO();
    public abstract MensajeDAO getMensajeDAO();
}