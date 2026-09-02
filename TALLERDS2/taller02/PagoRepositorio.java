public class PagoRepositorio extends RepositorioEnMemoria<Pago, String> {
    @Override
    protected String obtenerId(Pago entidad) { return entidad.getId(); }
}
