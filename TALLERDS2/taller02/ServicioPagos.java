import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ServicioPagos {
    private final ClienteRepositorio clienteRepo;
    private final CuentaRepositorio cuentaRepo;
    private final FacturaRepositorio facturaRepo;
    private final PagoRepositorio pagoRepo;
    private int contadorPagos = 1;

    public ServicioPagos(ClienteRepositorio clienteRepo, CuentaRepositorio cuentaRepo,
                          FacturaRepositorio facturaRepo, PagoRepositorio pagoRepo) {
        this.clienteRepo = clienteRepo;
        this.cuentaRepo = cuentaRepo;
        this.facturaRepo = facturaRepo;
        this.pagoRepo = pagoRepo;
    }

    public Pago procesarPago(String idFactura, String idCuenta) {
        Factura factura = facturaRepo.obtenerPorId(idFactura)
                .orElseThrow(() -> new RecursoNoEncontradoException("Factura no encontrada: " + idFactura));

        if (factura.getEstado() == EstadoFactura.PAGADA)
            throw new FacturaYaPagadaException("La factura " + idFactura + " ya fue pagada");

        Cuenta cuenta = cuentaRepo.obtenerPorId(idCuenta)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cuenta no encontrada: " + idCuenta));

        if (!cuenta.getIdCliente().equals(factura.getIdCliente()))
            throw new IllegalArgumentException("La cuenta no pertenece al cliente dueño de la factura");

        // POLIMORFISMO en acción: debitar() se comporta distinto según sea AHORROS o CORRIENTE
        cuenta.debitar(factura.getMonto());
        factura.marcarComoPagada();

        Pago pago = new Pago("P" + (contadorPagos++), idFactura, idCuenta, factura.getIdCliente(),
                factura.getMonto(), LocalDate.now());
        return pagoRepo.crear(pago);
    }

    public BigDecimal obtenerSaldoCuenta(String idCuenta) {
        Cuenta cuenta = cuentaRepo.obtenerPorId(idCuenta)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cuenta no encontrada: " + idCuenta));
        return cuenta.getSaldo();
    }

    public List<Pago> obtenerPagosPorCliente(String idCliente) {
        if (clienteRepo.obtenerPorId(idCliente).isEmpty())
            throw new RecursoNoEncontradoException("Cliente no encontrado: " + idCliente);
        List<Pago> resultado = new ArrayList<>();
        for (Pago p : pagoRepo.obtenerTodos())
            if (p.getIdCliente().equals(idCliente))
                resultado.add(p);
        return resultado;
    }

    public List<Factura> obtenerFacturasPorCliente(String idCliente) {
        if (clienteRepo.obtenerPorId(idCliente).isEmpty())
            throw new RecursoNoEncontradoException("Cliente no encontrado: " + idCliente);
        List<Factura> resultado = new ArrayList<>();
        for (Factura f : facturaRepo.obtenerTodos())
            if (f.getIdCliente().equals(idCliente))
                resultado.add(f);
        return resultado;
    }
}
