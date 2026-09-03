import java.math.BigDecimal;
import java.time.LocalDate;

public class Pago {
    private final String id;
    private final String idFactura;
    private final String idCuenta;
    private final String idCliente;
    private final BigDecimal monto;
    private final LocalDate fecha;

    public Pago(String id, String idFactura, String idCuenta, String idCliente, BigDecimal monto, LocalDate fecha) {
        this.id = id;
        this.idFactura = idFactura;
        this.idCuenta = idCuenta;
        this.idCliente = idCliente;
        this.monto = monto;
        this.fecha = fecha;
    }

    public String getId() { return id; }
    public String getIdFactura() { return idFactura; }
    public String getIdCuenta() { return idCuenta; }
    public String getIdCliente() { return idCliente; }
    public BigDecimal getMonto() { return monto; }
    public LocalDate getFecha() { return fecha; }

    @Override
    public String toString() {
        return String.format("Pago{id=%s, factura=%s, cuenta=%s, cliente=%s, monto=%s, fecha=%s}",
                id, idFactura, idCuenta, idCliente, monto, fecha);
    }
}
