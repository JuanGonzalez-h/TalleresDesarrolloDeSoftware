import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class main {
    static class SaldoInsuficienteException extends RuntimeException {
        public SaldoInsuficienteException(String mensaje) {
            super(mensaje);
        }
    }

    static class Cuenta {
        protected final String numero;
        protected final String titular;
        protected BigDecimal saldo;

        public Cuenta(String numero, String titular, BigDecimal saldoInicial) {
            if (saldoInicial.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("El saldo inicial no puede ser negativo");
            }
            this.numero = numero;
            this.titular = titular;
            this.saldo = saldoInicial;
        }

        public void depositar(BigDecimal monto) {
            if (monto.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("El monto debe ser positivo");
            }
            this.saldo = this.saldo.add(monto);
        }

        public void debitar(BigDecimal monto) {
            if (monto.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("El monto debe ser positivo");
            }
            if (monto.compareTo(this.saldo) > 0) {
                throw new SaldoInsuficienteException(
                        "Saldo: " + saldo + ", solicitado: " + monto);
            }
            this.saldo = this.saldo.subtract(monto);
        }

        public BigDecimal getSaldo() {
            return this.saldo;
        }

        public String getNumero() {
            return numero;
        }

        @Override
        public String toString() {
            return String.format("[%s] %s - %s - Saldo: $%s",
                    getClass().getSimpleName(), numero, titular, saldo);
        }
    }

    static class CuentaCorriente extends Cuenta {
        private final BigDecimal limiteDescubierto;

        public CuentaCorriente(String numero, String titular, BigDecimal saldoInicial,
                                BigDecimal limiteDescubierto) {
            super(numero, titular, saldoInicial); 
            this.limiteDescubierto = limiteDescubierto;
        }

        @Override
        public void debitar(BigDecimal monto) {
            if (monto.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("El monto debe ser positivo");
            }
            BigDecimal saldoDisponible = this.saldo.add(limiteDescubierto);
            if (monto.compareTo(saldoDisponible) > 0) {
                throw new SaldoInsuficienteException("Supera el límite de descubierto");
            }
            this.saldo = this.saldo.subtract(monto);
        }
    }

    static class Pedido {
        private final String descripcion;
        private final BigDecimal total;

        public Pedido(String descripcion, BigDecimal total) {
            this.descripcion = descripcion;
            this.total = total;
        }

        public BigDecimal getTotal() {
            return total;
        }

        public String getDescripcion() {
            return descripcion;
        }
    }

    interface Pago {
        void procesar(BigDecimal monto);
        String getDescripcion();
    }

    static class PagoTarjeta implements Pago {
        private final String numeroTarjeta;

        public PagoTarjeta(String numeroTarjeta) {
            this.numeroTarjeta = numeroTarjeta;
        }

        @Override
        public void procesar(BigDecimal monto) {
            System.out.println("Cargando $" + monto + " a la tarjeta " + numeroTarjeta);
        }

        @Override
        public String getDescripcion() {
            return "Tarjeta " + numeroTarjeta;
        }
    }

    static class PagoTransferencia implements Pago {
        private final String cbu;

        public PagoTransferencia(String cbu) {
            this.cbu = cbu;
        }

        @Override
        public void procesar(BigDecimal monto) {
            System.out.println("Transfiriendo $" + monto + " al CBU " + cbu);
        }

        @Override
        public String getDescripcion() {
            return "Transferencia a CBU " + cbu;
        }
    }

    static class PagoEfectivo implements Pago {
        @Override
        public void procesar(BigDecimal monto) {
            System.out.println("Registrando pago en efectivo de $" + monto);
        }

        @Override
        public String getDescripcion() {
            return "Efectivo";
        }
    }

    static class CheckoutService {
        public void finalizarCompra(Pedido pedido, Pago metodoDePago) {
            metodoDePago.procesar(pedido.getTotal());
            System.out.println("Compra finalizada (" + pedido.getDescripcion()
                    + "). Método: " + metodoDePago.getDescripcion());
        }
    }

    private static final List<Cuenta> cuentas = new ArrayList<>();
    private static final CheckoutService checkout = new CheckoutService();
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero("Elige una opcion: ");
            try {
                switch (opcion) {
                    case 1 -> crearCuenta();
                    case 2 -> depositar();
                    case 3 -> debitar();
                    case 4 -> consultarSaldo();
                    case 5 -> procesarPago(new PagoTarjeta(pedirTexto("Número de tarjeta: ")));
                    case 6 -> procesarPago(new PagoTransferencia(pedirTexto("CBU: ")));
                    case 7 -> procesarPago(new PagoEfectivo());
                    case 0 -> System.out.println("¡Hasta luego!");
                    default -> System.out.println("Opción inválida.");
                }
            } catch (IllegalArgumentException | SaldoInsuficienteException e) {
                System.out.println("Error: " + e.getMessage());
            }
            System.out.println();
        } while (opcion != 0);

        sc.close();
    }

    private static void mostrarMenu() {
        System.out.println("========= MENU =========");
        System.out.println("-- Cuentas --");
        System.out.println("1. Crear cuenta");
        System.out.println("2. Depositar");
        System.out.println("3. Debitar");
        System.out.println("4. Consultar saldo");
        System.out.println("-- Pagos --");
        System.out.println("5. Pagar con tarjeta");
        System.out.println("6. Pagar con transferencia");
        System.out.println("7. Pagar en efectivo");
        System.out.println("0. Salir");
        System.out.println("=========================");
    }

    private static void crearCuenta() {
        String numero = pedirTexto("Numero de cuenta: ");
        String titular = pedirTexto("Titular: ");
        BigDecimal saldoInicial = new BigDecimal(pedirTexto("Saldo inicial: "));

        String tipo = pedirTexto("¿Tipo? (1=Normal, 2=Corriente): ");
        if (tipo.equals("2")) {
            BigDecimal limite = new BigDecimal(pedirTexto("Limite de descubierto: "));
            cuentas.add(new CuentaCorriente(numero, titular, saldoInicial, limite));
        } else {
            cuentas.add(new Cuenta(numero, titular, saldoInicial));
        }
        System.out.println("Cuenta creada correctamente.");
    }

    private static void depositar() {
        Cuenta c = buscarCuenta();
        if (c == null) return;
        BigDecimal monto = new BigDecimal(pedirTexto("Monto a depositar: "));
        c.depositar(monto);
        System.out.println("Deposito realizado. Nuevo saldo: " + c.getSaldo());
    }

    private static void debitar() {
        Cuenta c = buscarCuenta();
        if (c == null) return;
        BigDecimal monto = new BigDecimal(pedirTexto("Monto a debitar: "));
        c.debitar(monto);
        System.out.println("DDebito realizado. Nuevo saldo: " + c.getSaldo());
    }

    private static void consultarSaldo() {
        Cuenta c = buscarCuenta();
        if (c == null) return;
        System.out.println(c);
    }

    private static Cuenta buscarCuenta() {
        if (cuentas.isEmpty()) {
            System.out.println("No hay cuentas creadas todavia.");
            return null;
        }
        String numero = pedirTexto("Numero de cuenta: ");
        for (Cuenta c : cuentas) {
            if (c.getNumero().equals(numero)) return c;
        }
        System.out.println("Cuenta no encontrada.");
        return null;
    }

    private static void procesarPago(Pago metodo) {
        String descripcion = pedirTexto("Descripción del pedido: ");
        BigDecimal total = new BigDecimal(pedirTexto("Total del pedido: "));
        Pedido pedido = new Pedido(descripcion, total);
        checkout.finalizarCompra(pedido, metodo);
    }

    private static String pedirTexto(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    private static int leerEntero(String prompt) {
        System.out.print(prompt);
        while (!sc.hasNextInt()) {
            sc.next();
            System.out.print("Ingresa un numero válido: ");
        }
        int valor = sc.nextInt();
        sc.nextLine(); 
        return valor;
    }
}
