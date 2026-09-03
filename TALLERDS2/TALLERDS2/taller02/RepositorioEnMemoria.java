import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// Implementación base en memoria (HERENCIA: cada repositorio concreto extiende esto)
public abstract class RepositorioEnMemoria<T, ID> implements RepositorioCRUD<T, ID> {
    protected final Map<ID, T> almacen = new LinkedHashMap<>();

    // POLIMORFISMO: cada subclase define cómo extraer el id de SU propia entidad
    protected abstract ID obtenerId(T entidad);

    @Override
    public T crear(T entidad) {
        almacen.put(obtenerId(entidad), entidad);
        return entidad;
    }

    @Override
    public Optional<T> obtenerPorId(ID id) {
        return Optional.ofNullable(almacen.get(id));
    }

    @Override
    public List<T> obtenerTodos() {
        return new ArrayList<>(almacen.values());
    }

    @Override
    public T actualizar(T entidad) {
        ID id = obtenerId(entidad);
        if (!almacen.containsKey(id))
            throw new RecursoNoEncontradoException("No existe el registro con id " + id);
        almacen.put(id, entidad);
        return entidad;
    }

    @Override
    public boolean eliminar(ID id) {
        return almacen.remove(id) != null;
    }
}
