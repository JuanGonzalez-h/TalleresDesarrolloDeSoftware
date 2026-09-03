public class FacturaRepositorio extends RepositorioEnMemoria<Factura, String> {
    @Override
    protected String obtenerId(Factura entidad) { return entidad.getId(); }
}
