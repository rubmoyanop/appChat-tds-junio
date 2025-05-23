package umu.tds.appchat.persistencia;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import beans.Entidad;
import beans.Propiedad;
import tds.driver.FactoriaServicioPersistencia;
import tds.driver.ServicioPersistencia;
import umu.tds.appchat.dao.ContactoIndividualDAO;
import umu.tds.appchat.dao.DAOExcepcion;
import umu.tds.appchat.dao.FactoriaDAO;
import umu.tds.appchat.dao.GrupoDAO;
import umu.tds.appchat.modelo.ContactoIndividual;
import umu.tds.appchat.modelo.Grupo;

public class GrupoDAO_TDS implements GrupoDAO {

    private ServicioPersistencia servPersistencia;
    private ContactoIndividualDAO contactoIndividualDAO;

    
    public GrupoDAO_TDS(){
        this(FactoriaServicioPersistencia.getInstance().getServicioPersistencia());
    }

    private GrupoDAO_TDS(ServicioPersistencia servPersistencia) {
        this.servPersistencia = servPersistencia;
        try {
            FactoriaDAO factoria = FactoriaDAO_TDS.getInstancia();
            this.contactoIndividualDAO = factoria.getContactoIndividualDAO();
        } catch (DAOExcepcion e) {
            throw new RuntimeException("No se pudo obtener DAOs en ContactoIndividualDAO_TDS", e);
        }
    }

    private Entidad grupoToEntidad(Grupo grupo) {
        Entidad entidad = new Entidad();
        entidad.setNombre("Grupo");
        List<Propiedad> propiedades = new LinkedList<>();
        propiedades.add(new Propiedad("nombre", grupo.getNombre()));

        String idsMiembros = grupo.getMiembros().stream()
                            .map(m -> String.valueOf(m.getId()))
                            .filter(id -> !id.equals("0"))
                            .collect(Collectors.joining(","));
        propiedades.add(new Propiedad("miembrosIds", idsMiembros));

        entidad.setPropiedades(propiedades);
        return entidad;
    }

    private Grupo entidadToGrupo(Entidad entidad) throws DAOExcepcion {
        if (entidad == null || !"Grupo".equals(entidad.getNombre())) {
            return null;
        }
        String nombre = servPersistencia.recuperarPropiedadEntidad(entidad, "nombre");
        String miembrosIdStr = servPersistencia.recuperarPropiedadEntidad(entidad, "miembrosIds");

        if (nombre == null || miembrosIdStr == null) {
            throw new DAOExcepcion("Propiedades 'nombre' o 'miembrosIdStr' faltantes en Entidad ContactoIndividual con ID: " + entidad.getId());
        }

        List<ContactoIndividual> miembros = new LinkedList<>();

        if (miembrosIdStr != null && !miembrosIdStr.isEmpty()) {
            String[] ids = miembrosIdStr.split(",");
            for (String id : ids) {
                try {
                    ContactoIndividual contacto = contactoIndividualDAO.recuperarContactoIndividual(Integer.parseInt(id));
                    miembros.add(contacto);
                } catch (DAOExcepcion e) {
                    throw new DAOExcepcion("Error al recuperar contacto individual con ID: " + id, e);
                } catch (NumberFormatException e) {
                    throw new DAOExcepcion("Error al convertir ID de contacto individual: " + id, e);
                }
            }
        }

        Grupo grupo = new Grupo(nombre, miembros);
        grupo.setId(entidad.getId());
        return grupo;
    }

    @Override
    public void registrarGrupo(Grupo grupo) throws DAOExcepcion {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'registrarGrupo'");
    }

    @Override
    public Grupo recuperarGrupo(int id) throws DAOExcepcion {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'recuperarGrupo'");
    }

    @Override
    public void modificarGrupo(Grupo grupo) throws DAOExcepcion {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'modificarGrupo'");
    }

    @Override
    public void borrarGrupo(Grupo grupo) throws DAOExcepcion {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'borrarGrupo'");
    }

    
    
}
