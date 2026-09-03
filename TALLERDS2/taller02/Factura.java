import java.math.BigDecimal;

public class Factura {
    private final String id;
    private final String idCliente;
    private final String servicio;
    private BigDecimal monto;
    private EstadoFactura estado;

    public Factura(String id, String idCliente, String servicio, BigDecimal monto) {
        this.id = id;
        this.idCliente = idCliente;
        this.servicio = servicio;
        this.monto = monto;
        this.estado = EstadoFactura.PENDIENTE;
    }

    public String getId() { return id; }
    public String getIdCliente() { return idCliente; }
    public String getServicio() { return servicio; }
    public BigDecimal getMonto() { return monto; }
    public EstadoFactura getEstado() { return estado; }

    public void setMonto(BigDecimal monto) {
        if (estado == EstadoFactura.PAGADA)
            throw new IllegalStateException("No se puede modificar una factura ya pagada");
        this.monto = monto;
    }

    public void marcarComoPagada() { this.estado = EstadoFactura.PAGADA; }

    @Override
    public String toString() {
        return String.format("Factura{id=%s, cliente=%s, servicio=%s, monto=%s, estado=%s}",
                id, idCliente, servicio, monto, estado);
    }
}
