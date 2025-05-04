package umu.tds.appchat.persistencia;

import umu.tds.appchat.modelo.Mensaje;
import java.util.HashMap;
import java.util.Map;

/**
 * Pool para gestionar objetos Mensaje recuperados.
 * Implementado como Singleton usando enum.
 */
public enum PoolMensajes {
    INSTANCE;

    private Map<Integer, Mensaje> pool = new HashMap<>();

    public void addMensaje(Mensaje mensaje) {
        if (mensaje != null && mensaje.getId() != 0) {
            pool.put(mensaje.getId(), mensaje);
        }
    }

    public Mensaje getMensaje(int id) {
        return pool.get(id);
    }

    public boolean contains(int id) {
        return pool.containsKey(id);
    }

    public void clear() {
        pool.clear();
    }

    public void removeMensaje(int id) {
        pool.remove(id);
    }
}
