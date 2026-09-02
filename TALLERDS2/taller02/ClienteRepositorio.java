public class ClienteRepositorio extends RepositorioEnMemoria<Cliente, String> {
    @Override
    protected String obtenerId(Cliente entidad) { return entidad.getId(); }
}
