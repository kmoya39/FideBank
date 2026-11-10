# FideBank – Sistema de cajeros automáticos

Proyecto final del curso **Programación Cliente/Servidor Concurrente (SC-303)**, Universidad Fidélitas.
Estudiante: Kevin Alexander Moya Jiménez.

**Avance 2:** interfaz gráfica en Swing con las 8 historias de usuario implementadas (100%).

## Requisitos
- JDK 21 (probado con Eclipse Temurin 21).
- Maven 3.9 o un IDE con soporte Maven (NetBeans, o VS Code con Extension Pack for Java).
- No usa frameworks ni librerías externas.

## Ejecutar
**NetBeans:** *File > Open Project*, seleccionar la carpeta `FideBank` y presionar *Run*.

**VS Code:** abrir la carpeta, abrir `src/main/java/cr/ac/ufidelitas/sc303/fidebank/App.java` y presionar *Run*.

**Terminal:**
```
mvn package
java -jar target/fidebank-0.2.0.jar
```

Al iniciar se abre la ventana **FideBank | Sistema de cajeros**, que permite abrir el cajero automático
(se pueden abrir varios a la vez) y la sucursal.

## Datos de prueba
Se crean la primera vez que se ejecuta el programa, en `datos/fidebank.dat`.

| Cliente | Cédula | PIN | Cuentas |
|---|---|---|---|
| Ana Solís Vargas | 1-1456-0789 | 4826 | FB-1001 (ahorro), FB-1002 (corriente) |
| Carlos Jiménez Rojas | 2-0555-0111 | 7391 | FB-1003 (ahorro) |

Para volver a los datos iniciales, borre la carpeta `datos`. Los comprobantes impresos se guardan en `comprobantes/`.

## Estructura por capas
```
cr.ac.ufidelitas.sc303.fidebank
├── App.java            Punto de entrada (SwingUtilities.invokeLater)
├── modelo/             Cliente, Cuenta (abstracta), CuentaAhorro, CuentaCorriente, Transaccion, Banco, enumeraciones
├── excepcion/          FideBankException y sus subclases
├── dao/                BancoDao (interfaz) y ArchivoBancoDao (serialización)
├── servicio/           BancoServicio (reglas de negocio sincronizadas) y DatosIniciales
├── util/               Validador, Formato, Seguridad (hash PBKDF2 del PIN)
└── vista/              Ventanas y pantallas Swing
```

## Historias de usuario
| HU | Descripción | Pantalla | Estado |
|---|---|---|---|
| HU-01 | Apertura de cuenta en sucursal | `PanelApertura` | Implementada |
| HU-02 | Ingreso con cédula y PIN, bloqueo al tercer intento | `PanelIngreso` | Implementada |
| HU-03 | Consulta de saldo | `PanelMenu`, `PanelSaldo` | Implementada |
| HU-04 | Retiro de efectivo | `PanelRetiro` | Implementada |
| HU-05 | Depósito | `PanelDeposito` | Implementada |
| HU-06 | Transferencia | `PanelTransferencia` | Implementada |
| HU-07 | Comprobante e impresión | `PanelComprobante` | Implementada |
| HU-08 | Historial de movimientos | `PanelHistorial` | Implementada |
