import java.math.BigDecimal;

public class CuentaCorriente extends Cuenta {
    private final BigDecimal limiteDescubierto;

    public CuentaCorriente(String id, String idCliente, BigDecimal saldoInicial, BigDecimal limiteDescubierto) {
        super(id, idCliente, saldoInicial);
        this.limiteDescubierto = limiteDescubierto;
    }

    public BigDecimal getLimiteDescubierto() { return limiteDescubierto; }

    // Sobrescribe debitar(): permite saldo negativo hasta el límite de descubierto (POLIMORFISMO)
    @Override
    public void debitar(BigDecimal monto) {
        if (monto.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("El monto a debitar debe ser positivo");
        BigDecimal disponible = this.saldo.add(limiteDescubierto);
        if (monto.compareTo(disponible) > 0)
            throw new SaldoInsuficienteException(
                    "Cuenta corriente " + id + " - supera el límite de descubierto (disponible: " + disponible + ")");
        this.saldo = this.saldo.subtract(monto);
    }

    @Override
    public String getTipo() { return "CORRIENTE"; }
}
