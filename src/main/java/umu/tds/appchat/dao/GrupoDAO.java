package umu.tds.appchat.dao;

import umu.tds.appchat.modelo.Grupo;

public interface GrupoDAO {

    void registrarGrupo(Grupo grupo) throws DAOExcepcion;

    Grupo recuperarGrupo(int id) throws DAOExcepcion;

    void modificarGrupo(Grupo grupo) throws DAOExcepcion; 

    void borrarGrupo(Grupo grupo) throws DAOExcepcion;
}