import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class Main {

    static ClienteRepositorio clienteRepo = new ClienteRepositorio();
    static CuentaRepositorio cuentaRepo = new CuentaRepositorio();
    static FacturaRepositorio facturaRepo = new FacturaRepositorio();
    static PagoRepositorio pagoRepo = new PagoRepositorio();
    static ServicioPagos servicioPagos = new ServicioPagos(clienteRepo, cuentaRepo, facturaRepo, pagoRepo);
    static Scanner sc = new Scanner(System.in);

    static int contadorClientes = 1;
    static int contadorCuentas = 1;
    static int contadorFacturas = 1;
    public static void main(String[] args) {
        cargarDatosDeEjemplo();

        int opcion;
        do {
            mostrarMenuPrincipal();
            opcion = leerOpcion();
            switch (opcion) {
                case 1 -> menuClientes();
                case 2 -> menuCuentas();
                case 3 -> menuFacturas();
                case 4 -> procesarPagoUI();
                case 5 -> consultarSaldoUI();
                case 6 -> pagosPorClienteUI();
                case 7 -> facturasPorClienteUI();
                case 8 -> menuPagos();
                case 0 -> System.out.println("Saliendo...");
                default -> System.out.println("Opción no válida.\n");
            }
        } while (opcion != 0);

        sc.close();
    }

    private static void cargarDatosDeEjemplo() {
        Cliente c1 = new Cliente("CL" + (contadorClientes++), "Ana Gómez", "1001234567");
        Cliente c2 = new Cliente("CL" + (contadorClientes++), "Luis Ramírez", "1007654321");
        clienteRepo.crear(c1);
        clienteRepo.crear(c2);

        Cuenta cuentaAhorros = new CuentaAhorros("CU" + (contadorCuentas++), c1.getId(),
                new BigDecimal("500000"), new BigDecimal("0.02"));
        Cuenta cuentaCorriente = new CuentaCorriente("CU" + (contadorCuentas++), c2.getId(),
                new BigDecimal("200000"), new BigDecimal("100000"));
        cuentaRepo.crear(cuentaAhorros);
        cuentaRepo.crear(cuentaCorriente);

        facturaRepo.crear(new Factura("F" + (contadorFacturas++), c1.getId(), "Agua", new BigDecimal("45000")));
        facturaRepo.crear(new Factura("F" + (contadorFacturas++), c1.getId(), "Energía", new BigDecimal("80000")));
        facturaRepo.crear(new Factura("F" + (contadorFacturas++), c2.getId(), "Gas", new BigDecimal("30000")));

        System.out.println("Datos de ejemplo cargados: 2 clientes, 2 cuentas, 3 facturas.");
        System.out.println("(Clientes: CL1, CL2 | Cuentas: CU1 = Ahorros de CL1, CU2 = Corriente de CL2 | Facturas: F1, F2 de CL1, F3 de CL2)\n");
    }

    private static void mostrarMenuPrincipal() {
        System.out.println("=========================================");
        System.out.println(" SISTEMA DE PAGOS DE SERVICIOS PÚBLICOS");
        System.out.println("=========================================");
        System.out.println("1. Gestionar Clientes (CRUD)");
        System.out.println("2. Gestionar Cuentas (CRUD)");
        System.out.println("3. Gestionar Facturas (CRUD)");
        System.out.println("4. Procesar pago");
        System.out.println("5. Consultar saldo de una cuenta");
        System.out.println("6. Consultar pagos por cliente");
        System.out.println("7. Consultar facturas por cliente");
        System.out.println("8. Consultar pagos registrados (listar / eliminar)");
        System.out.println("0. Salir");
    }

    private static int leerOpcion() {
        System.out.print("Elige una opción: ");
        try {
            return Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static BigDecimal leerBigDecimal(String etiqueta) {
        System.out.print(etiqueta);
        try {
            return new BigDecimal(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Valor inválido, se usará 0.");
            return BigDecimal.ZERO;
        }
    }

    private static void menuClientes() {
        int opcion;
        do {
            System.out.println("\n--- Gestión de Clientes ---");
            System.out.println("1. Crear");
            System.out.println("2. Listar todos");
            System.out.println("3. Buscar por id");
            System.out.println("4. Actualizar");
            System.out.println("5. Eliminar");
            System.out.println("0. Volver");
            opcion = leerOpcion();
            try {
                switch (opcion) {
                    case 1 -> {
                        System.out.print("Nombre: ");
                        String nombre = sc.nextLine();
                        System.out.print("Documento: ");
                        String documento = sc.nextLine();
                        Cliente cliente = new Cliente("CL" + (contadorClientes++), nombre, documento);
                        clienteRepo.crear(cliente);
                        System.out.println("Creado: " + cliente);
                    }
                    case 2 -> {
                        List<Cliente> todos = clienteRepo.obtenerTodos();
                        if (todos.isEmpty()) System.out.println("No hay clientes registrados.");
                        else todos.forEach(System.out::println);
                    }
                    case 3 -> {
                        System.out.print("Id del cliente: ");
                        String id = sc.nextLine().trim();
                        System.out.println(clienteRepo.obtenerPorId(id)
                                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado: " + id)));
                    }
                    case 4 -> {
                        System.out.print("Id del cliente a actualizar: ");
                        String id = sc.nextLine().trim();
                        Cliente actual = clienteRepo.obtenerPorId(id)
                                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado: " + id));
                        System.out.print("Nuevo nombre (enter para mantener \"" + actual.getNombre() + "\"): ");
                        String nombre = sc.nextLine();
                        if (!nombre.isBlank()) actual.setNombre(nombre);
                        System.out.print("Nuevo documento (enter para mantener \"" + actual.getDocumento() + "\"): ");
                        String documento = sc.nextLine();
                        if (!documento.isBlank()) actual.setDocumento(documento);
                        clienteRepo.actualizar(actual);
                        System.out.println("Actualizado: " + actual);
                    }
                    case 5 -> {
                        System.out.print("Id del cliente a eliminar: ");
                        String id = sc.nextLine().trim();
                        System.out.println(clienteRepo.eliminar(id) ? "Cliente eliminado." : "No existía ese cliente.");
                    }
                    case 0 -> { }
                    default -> System.out.println("Opción no válida.");
                }
            } catch (RuntimeException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } while (opcion != 0);
    }

    private static void menuCuentas() {
        int opcion;
        do {
            System.out.println("\n--- Gestión de Cuentas ---");
            System.out.println("1. Crear");
            System.out.println("2. Listar todas");
            System.out.println("3. Buscar por id");
            System.out.println("4. Actualizar (depositar dinero)");
            System.out.println("5. Eliminar");
            System.out.println("0. Volver");
            opcion = leerOpcion();
            try {
                switch (opcion) {
                    case 1 -> crearCuentaUI();
                    case 2 -> {
                        List<Cuenta> todas = cuentaRepo.obtenerTodos();
                        if (todas.isEmpty()) System.out.println("No hay cuentas registradas.");
                        else todas.forEach(System.out::println);
                    }
                    case 3 -> {
                        System.out.print("Id de la cuenta: ");
                        String id = sc.nextLine().trim();
                        System.out.println(cuentaRepo.obtenerPorId(id)
                                .orElseThrow(() -> new RecursoNoEncontradoException("Cuenta no encontrada: " + id)));
                    }
                    case 4 -> {
                        System.out.print("Id de la cuenta: ");
                        String id = sc.nextLine().trim();
                        Cuenta cuenta = cuentaRepo.obtenerPorId(id)
                                .orElseThrow(() -> new RecursoNoEncontradoException("Cuenta no encontrada: " + id));
                        BigDecimal monto = leerBigDecimal("Monto a depositar: ");
                        cuenta.depositar(monto);
                        cuentaRepo.actualizar(cuenta);
                        System.out.println("Nuevo saldo: " + cuenta.getSaldo());
                    }
                    case 5 -> {
                        System.out.print("Id de la cuenta a eliminar: ");
                        String id = sc.nextLine().trim();
                        System.out.println(cuentaRepo.eliminar(id) ? "Cuenta eliminada." : "No existía esa cuenta.");
                    }
                    case 0 -> { }
                    default -> System.out.println("Opción no válida.");
                }
            } catch (RuntimeException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } while (opcion != 0);
    }

    private static void crearCuentaUI() {
        System.out.print("Id del cliente dueño de la cuenta: ");
        String idCliente = sc.nextLine().trim();
        if (clienteRepo.obtenerPorId(idCliente).isEmpty()) {
            System.out.println("No existe un cliente con ese id.");
            return;
        }
        System.out.println("Tipo de cuenta: 1) Ahorros  2) Corriente");
        int tipo = leerOpcion();
        BigDecimal saldoInicial = leerBigDecimal("Saldo inicial: ");
        String id = "CU" + (contadorCuentas++);

        Cuenta cuenta;
        if (tipo == 1) {
            BigDecimal tasa = leerBigDecimal("Tasa de interés (ej. 0.02): ");
            cuenta = new CuentaAhorros(id, idCliente, saldoInicial, tasa);
        } else if (tipo == 2) {
            BigDecimal limite = leerBigDecimal("Límite de descubierto: ");
            cuenta = new CuentaCorriente(id, idCliente, saldoInicial, limite);
        } else {
            System.out.println("Tipo de cuenta inválido.");
            return;
        }
        cuentaRepo.crear(cuenta);
        System.out.println("Cuenta creada: " + cuenta);
    }

    private static void menuFacturas() {
        int opcion;
        do {
            System.out.println("\n--- Gestión de Facturas ---");
            System.out.println("1. Crear");
            System.out.println("2. Listar todas");
            System.out.println("3. Buscar por id");
            System.out.println("4. Actualizar monto (si está pendiente)");
            System.out.println("5. Eliminar");
            System.out.println("0. Volver");
            opcion = leerOpcion();
            try {
                switch (opcion) {
                    case 1 -> {
                        System.out.print("Id del cliente: ");
                        String idCliente = sc.nextLine().trim();
                        if (clienteRepo.obtenerPorId(idCliente).isEmpty()) {
                            System.out.println("No existe un cliente con ese id.");
                            break;
                        }
                        System.out.print("Servicio (ej. Agua, Energía, Gas): ");
                        String servicio = sc.nextLine();
                        BigDecimal monto = leerBigDecimal("Monto: ");
                        Factura factura = new Factura("F" + (contadorFacturas++), idCliente, servicio, monto);
                        facturaRepo.crear(factura);
                        System.out.println("Creada: " + factura);
                    }
                    case 2 -> {
                        List<Factura> todas = facturaRepo.obtenerTodos();
                        if (todas.isEmpty()) System.out.println("No hay facturas registradas.");
                        else todas.forEach(System.out::println);
                    }
                    case 3 -> {
                        System.out.print("Id de la factura: ");
                        String id = sc.nextLine().trim();
                        System.out.println(facturaRepo.obtenerPorId(id)
                                .orElseThrow(() -> new RecursoNoEncontradoException("Factura no encontrada: " + id)));
                    }
                    case 4 -> {
                        System.out.print("Id de la factura: ");
                        String id = sc.nextLine().trim();
                        Factura factura = facturaRepo.obtenerPorId(id)
                                .orElseThrow(() -> new RecursoNoEncontradoException("Factura no encontrada: " + id));
                        BigDecimal monto = leerBigDecimal("Nuevo monto: ");
                        factura.setMonto(monto);
                        facturaRepo.actualizar(factura);
                        System.out.println("Actualizada: " + factura);
                    }
                    case 5 -> {
                        System.out.print("Id de la factura a eliminar: ");
                        String id = sc.nextLine().trim();
                        System.out.println(facturaRepo.eliminar(id) ? "Factura eliminada." : "No existía esa factura.");
                    }
                    case 0 -> { }
                    default -> System.out.println("Opción no válida.");
                }
            } catch (RuntimeException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } while (opcion != 0);
    }

    private static void menuPagos() {
        int opcion;
        do {
            System.out.println("\n--- Pagos registrados ---");
            System.out.println("1. Listar todos");
            System.out.println("2. Buscar por id");
            System.out.println("3. Eliminar (corrección de registro)");
            System.out.println("0. Volver");
            opcion = leerOpcion();
            try {
                switch (opcion) {
                    case 1 -> {
                        List<Pago> todos = pagoRepo.obtenerTodos();
                        if (todos.isEmpty()) System.out.println("Aún no se ha registrado ningún pago.");
                        else todos.forEach(System.out::println);
                    }
                    case 2 -> {
                        System.out.print("Id del pago: ");
                        String id = sc.nextLine().trim();
                        System.out.println(pagoRepo.obtenerPorId(id)
                                .orElseThrow(() -> new RecursoNoEncontradoException("Pago no encontrado: " + id)));
                    }
                    case 3 -> {
                        System.out.print("Id del pago a eliminar: ");
                        String id = sc.nextLine().trim();
                        System.out.println(pagoRepo.eliminar(id) ? "Pago eliminado." : "No existía ese pago.");
                    }
                    case 0 -> { }
                    default -> System.out.println("Opción no válida.");
                }
            } catch (RuntimeException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } while (opcion != 0);
    }

    private static void procesarPagoUI() {
        try {
            System.out.print("Id de la factura a pagar: ");
            String idFactura = sc.nextLine().trim();
            System.out.print("Id de la cuenta con la que se paga: ");
            String idCuenta = sc.nextLine().trim();
            Pago pago = servicioPagos.procesarPago(idFactura, idCuenta);
            System.out.println("Pago procesado con éxito: " + pago);
        } catch (RuntimeException e) {
            System.out.println("Error al procesar el pago: " + e.getMessage());
        }
    }

    private static void consultarSaldoUI() {
        try {
            System.out.print("Id de la cuenta: ");
            String idCuenta = sc.nextLine().trim();
            BigDecimal saldo = servicioPagos.obtenerSaldoCuenta(idCuenta);
            System.out.println("Saldo actual: " + saldo);
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void pagosPorClienteUI() {
        try {
            System.out.print("Id del cliente: ");
            String idCliente = sc.nextLine().trim();
            List<Pago> pagos = servicioPagos.obtenerPagosPorCliente(idCliente);
            if (pagos.isEmpty()) System.out.println("Este cliente aún no tiene pagos registrados.");
            else pagos.forEach(System.out::println);
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void facturasPorClienteUI() {
        try {
            System.out.print("Id del cliente: ");
            String idCliente = sc.nextLine().trim();
            List<Factura> facturas = servicioPagos.obtenerFacturasPorCliente(idCliente);
            if (facturas.isEmpty()) System.out.println("Este cliente no tiene facturas registradas.");
            else facturas.forEach(System.out::println);
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
