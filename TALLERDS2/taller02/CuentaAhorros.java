import java.math.BigDecimal;

public class CuentaAhorros extends Cuenta {
    private final BigDecimal tasaInteres;

    public CuentaAhorros(String id, String idCliente, BigDecimal saldoInicial, BigDecimal tasaInteres) {
        super(id, idCliente, saldoInicial);
        this.tasaInteres = tasaInteres;
    }

    public BigDecimal getTasaInteres() { return tasaInteres; }

    @Override
    public void debitar(BigDecimal monto) {
        if (monto.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("El monto a debitar debe ser positivo");
        if (monto.compareTo(this.saldo) > 0)
            throw new SaldoInsuficienteException(
                    "Cuenta de ahorros " + id + " - saldo: " + saldo + ", solicitado: " + monto);
        this.saldo = this.saldo.subtract(monto);
    }

    @Override
    public String getTipo() { return "AHORROS"; }
}
