CU-01 – Procesar Pago

Actor primario: Cliente (titular de la factura y de la cuenta con la que paga)
Actor secundario: Operador/Cajero (quien opera el sistema en el punto de pago en nombre del cliente)

Precondiciones:

El cliente está registrado en el sistema.
La factura a pagar existe y su estado es PENDIENTE.
La cuenta con la que se paga existe y pertenece al mismo cliente dueño de la factura.

Postcondiciones:

El estado de la factura cambia a PAGADA.
El saldo de la cuenta se reduce en el monto de la factura.
Queda registrado un nuevo Pago asociado a la factura, la cuenta y el cliente, con fecha del día.

Flujo principal:

El operador selecciona la opción "Procesar pago".
El sistema solicita el id de la factura a pagar.
El operador ingresa el id de la factura.
El sistema busca la factura y verifica que exista y esté PENDIENTE.
El sistema solicita el id de la cuenta con la que se pagará.
El operador ingresa el id de la cuenta.
El sistema busca la cuenta y verifica que exista.
El sistema verifica que la cuenta pertenezca al mismo cliente dueño de la factura.
El sistema debita el monto de la factura de la cuenta, aplicando la regla propia del tipo de cuenta (ahorros o corriente).
El sistema marca la factura como PAGADA.
El sistema genera y registra un nuevo Pago.
El sistema muestra al operador la confirmación del pago realizado.

Flujos alternativos:

FA-01 – Pago con cuenta corriente en descubierto: en el paso 9, si la cuenta es CuentaCorriente y el saldo actual no alcanza pero sí el saldo + el límite de descubierto, el sistema autoriza el débito dejando el saldo en negativo hasta ese límite, y continúa normalmente desde el paso 10.
FA-02 – Operación cancelada por el operador: en cualquier punto entre los pasos 2 y 8, el operador puede decidir no continuar (por ejemplo, no tiene a la mano el id de la cuenta); el sistema no realiza ningún cambio y vuelve al menú principal.

Excepciones:

EX-01 – Factura inexistente: en el paso 4, si no existe una factura con ese id, el sistema muestra "Factura no encontrada" y cancela la operación sin afectar cuenta alguna.
EX-02 – Factura ya pagada: en el paso 4, si la factura existe pero su estado ya es PAGADA, el sistema muestra "La factura ya fue pagada" y cancela la operación.
EX-03 – Cuenta inexistente: en el paso 7, si no existe una cuenta con ese id, el sistema muestra "Cuenta no encontrada" y cancela la operación.
EX-04 – Cuenta de otro cliente: en el paso 8, si la cuenta no pertenece al cliente dueño de la factura, el sistema muestra "La cuenta no pertenece al cliente dueño de la factura" y cancela la operación.
EX-05 – Saldo insuficiente: en el paso 9, si el monto de la factura supera el saldo disponible (saldo, o saldo + límite de descubierto si es cuenta corriente), el sistema muestra el mensaje de saldo insuficiente, no modifica saldo ni factura, y cancela la operación.
CU-02 – Consultar Saldo de Cuenta

Actor primario: Cliente
Actor secundario: Operador/Cajero

Precondiciones: la cuenta a consultar existe en el sistema.

Postcondiciones: ninguna — es una operación de solo lectura, no altera ningún dato.

Flujo principal:

El operador selecciona "Consultar saldo de una cuenta".
El sistema solicita el id de la cuenta.
El operador ingresa el id.
El sistema busca la cuenta.
El sistema muestra el saldo actual.

Flujos alternativos:

FA-01 – Saldo negativo: si la cuenta consultada es corriente y está en descubierto, el sistema muestra el valor negativo con normalidad, sin tratarlo como un error.
FA-02 – Consulta inmediatamente después de un pago: si se consulta la cuenta justo tras procesar un pago con ella, el saldo reflejado ya incluye ese débito, sin necesidad de ninguna acción adicional del operador.

Excepciones:

EX-01 – Cuenta inexistente: en el paso 4, si no existe una cuenta con ese id, el sistema muestra "Cuenta no encontrada" y no muestra ningún saldo.
EX-02 – Falla de acceso al repositorio de cuentas: si el sistema no logra acceder al almacenamiento de cuentas, informa al operador que no fue posible completar la consulta y sugiere reintentar, sin mostrar un saldo parcial o incorrecto.
CU-03 – Consultar Pagos por Cliente

Actor primario: Cliente
Actor secundario: Operador/Cajero

Precondiciones: el cliente consultado está registrado en el sistema.

Postcondiciones: ninguna — operación de solo lectura.

Flujo principal:

El operador selecciona "Consultar pagos por cliente".
El sistema solicita el id del cliente.
El operador lo ingresa.
El sistema verifica que el cliente exista.
El sistema recorre todos los pagos registrados y filtra los del cliente.
El sistema muestra la lista de pagos (id, factura, cuenta, monto, fecha).

Flujos alternativos:

FA-01 – Cliente sin pagos: si el cliente existe pero no tiene pagos, el sistema muestra "Este cliente aún no tiene pagos registrados" en lugar de una lista vacía.
FA-02 – Cliente con varios pagos: si tiene múltiples pagos, el sistema los muestra todos en el orden en que fueron registrados.

Excepciones:

EX-01 – Cliente inexistente: en el paso 4, si no existe un cliente con ese id, el sistema muestra "Cliente no encontrado" y no busca pagos.
EX-02 – Id vacío o mal ingresado: si el operador no escribe ningún valor, el sistema lo procesa igual que un id no encontrado (EX-01), sin interrumpir la ejecución del sistema.
CU-04 – Consultar Facturas por Cliente

Actor primario: Cliente
Actor secundario: Operador/Cajero

Precondiciones: el cliente consultado está registrado en el sistema.

Postcondiciones: ninguna — operación de solo lectura.

Flujo principal:

El operador selecciona "Consultar facturas por cliente".
El sistema solicita el id del cliente.
El operador lo ingresa.
El sistema verifica que el cliente exista.
El sistema recorre todas las facturas y filtra las del cliente.
El sistema muestra la lista de facturas (id, servicio, monto, estado).

Flujos alternativos:

FA-01 – Cliente sin facturas: el sistema muestra "Este cliente no tiene facturas registradas" en lugar de una lista vacía.
FA-02 – Facturas en distintos estados: la lista muestra tanto facturas PENDIENTES como PAGADAS, permitiendo distinguir cuáles ya fueron canceladas.

Excepciones:

EX-01 – Cliente inexistente: el sistema muestra "Cliente no encontrado" y no busca facturas.
EX-02 – Id vacío o mal ingresado: se procesa igual que EX-01, sin interrumpir la ejecución.