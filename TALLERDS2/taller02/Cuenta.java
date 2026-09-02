import java.math.BigDecimal;

public abstract class Cuenta {
    protected final String id;
    protected final String idCliente;
    protected BigDecimal saldo;

    public Cuenta(String id, String idCliente, BigDecimal saldoInicial) {
        if (saldoInicial.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("El saldo inicial no puede ser negativo");
        this.id = id;
        this.idCliente = idCliente;
        this.saldo = saldoInicial;
    }

    public String getId() { return id; }
    public String getIdCliente() { return idCliente; }
    public BigDecimal getSaldo() { return saldo; }

    public void depositar(BigDecimal monto) {
        if (monto.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("El monto a depositar debe ser positivo");
        this.saldo = this.saldo.add(monto);
    }

    // Cada subtipo de cuenta implementa su propia regla de débito (POLIMORFISMO)
    public abstract void debitar(BigDecimal monto);

    public abstract String getTipo();

    @Override
    public String toString() {
        return String.format("Cuenta{id=%s, tipo=%s, idCliente=%s, saldo=%s}", id, getTipo(), idCliente, saldo);
    }
}
