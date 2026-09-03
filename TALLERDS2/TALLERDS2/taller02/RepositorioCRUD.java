import java.util.List;
import java.util.Optional;

public interface RepositorioCRUD<T, ID> {
    T crear(T entidad);
    Optional<T> obtenerPorId(ID id);
    List<T> obtenerTodos();
    T actualizar(T entidad);
    boolean eliminar(ID id);
}
