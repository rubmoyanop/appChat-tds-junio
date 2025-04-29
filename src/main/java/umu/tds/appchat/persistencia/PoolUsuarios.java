package umu.tds.appchat.persistencia;

import umu.tds.appchat.modelo.Usuario;
import java.util.HashMap;
import java.util.Map;

/**
 * Pool para gestionar objetos Usuario recuperados y evitar ciclos.
 * Implementado como Singleton usando enum.
 */
public enum PoolUsuarios {
    INSTANCE;

    private Map<Integer, Usuario> pool = new HashMap<>();

    public void addUsuario(Usuario usuario) {
        if (usuario != null && usuario.getId() != 0) {
            pool.put(usuario.getId(), usuario);
        }
    }

    public Usuario getUsuario(int id) {
        return pool.get(id);
    }

    public boolean contains(int id) {
        return pool.containsKey(id);
    }

    public void clear() {
        pool.clear();
    }
}
