public class CuentaRepositorio extends RepositorioEnMemoria<Cuenta, String> {
    @Override
    protected String obtenerId(Cuenta entidad) { return entidad.getId(); }
}
