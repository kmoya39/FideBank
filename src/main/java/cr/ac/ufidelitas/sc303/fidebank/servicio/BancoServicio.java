package cr.ac.ufidelitas.sc303.fidebank.servicio;

import cr.ac.ufidelitas.sc303.fidebank.dao.BancoDao;
import cr.ac.ufidelitas.sc303.fidebank.excepcion.AutenticacionException;
import cr.ac.ufidelitas.sc303.fidebank.excepcion.DatoInvalidoException;
import cr.ac.ufidelitas.sc303.fidebank.excepcion.FideBankException;
import cr.ac.ufidelitas.sc303.fidebank.modelo.Banco;
import cr.ac.ufidelitas.sc303.fidebank.modelo.Cliente;
import cr.ac.ufidelitas.sc303.fidebank.modelo.Cuenta;
import cr.ac.ufidelitas.sc303.fidebank.modelo.CuentaAhorro;
import cr.ac.ufidelitas.sc303.fidebank.modelo.CuentaCorriente;
import cr.ac.ufidelitas.sc303.fidebank.modelo.TipoCuenta;
import cr.ac.ufidelitas.sc303.fidebank.modelo.TipoTransaccion;
import cr.ac.ufidelitas.sc303.fidebank.modelo.Transaccion;
import cr.ac.ufidelitas.sc303.fidebank.util.Validador;
import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Reglas de negocio del cajero y de la sucursal. La interfaz gráfica llama
 * a estos métodos desde hilos de fondo (SwingWorker), y como pueden estar
 * abiertos varios cajeros a la vez, los métodos que cambian datos son
 * synchronized.
 *
 * <p>Condición de carrera que se evita: dos cajeros retiran de la misma
 * cuenta al mismo tiempo. Sin sincronización ambos hilos podrían validar
 * el saldo antes de que el otro lo rebaje, y la cuenta quedaría por debajo
 * de lo permitido. Con el candado del servicio, la validación y el rebajo
 * del saldo ocurren como una sola operación.</p>
 */
public class BancoServicio {

    /** Depósito mínimo para abrir una cuenta (HU-01). */
    public static final BigDecimal DEPOSITO_MINIMO = new BigDecimal("10000.00");
    /** Sobregiro autorizado de las cuentas corrientes. */
    public static final BigDecimal SOBREGIRO_CORRIENTE = new BigDecimal("100000.00");
    /** Tasa anual de las cuentas de ahorro, en porcentaje. */
    public static final BigDecimal TASA_AHORRO = new BigDecimal("3.50");
    /** Carpeta donde se guardan los comprobantes impresos (HU-07). */
    public static final Path CARPETA_COMPROBANTES = Path.of("comprobantes");

    private final BancoDao dao;
    private final Banco banco;

    /**
     * Carga los datos guardados o, si es la primera ejecución, crea los
     * datos de prueba y los guarda.
     *
     * @param dao objeto de acceso a datos
     * @throws IOException si no se pueden leer o crear los datos
     */
    public BancoServicio(BancoDao dao) throws IOException {
        this.dao = dao;
        if (dao.existenDatos()) {
            this.banco = dao.cargar();
        } else {
            this.banco = DatosIniciales.crear();
            dao.guardar(this.banco);
        }
    }

    /**
     * HU-01: registra a un cliente nuevo y le abre su primera cuenta.
     *
     * @param cedulaTexto  cédula digitada
     * @param nombreTexto  nombre completo digitado
     * @param pin          PIN inicial digitado
     * @param tipo         tipo de cuenta elegido (puede venir null)
     * @param montoTexto   depósito inicial digitado
     * @return la cuenta creada
     * @throws FideBankException si algún dato no es válido
     * @throws IOException       si no se pueden guardar los datos
     */
    public synchronized Cuenta abrirCuentaClienteNuevo(String cedulaTexto, String nombreTexto, String pin,
            TipoCuenta tipo, String montoTexto) throws FideBankException, IOException {
        String cedula = Validador.cedula(cedulaTexto);
        if (this.banco.buscarCliente(cedula) != null) {
            throw new DatoInvalidoException("Ya existe un cliente con la cédula " + cedulaTexto.trim() + ".");
        }
        String nombre = Validador.nombre(nombreTexto);
        Validador.pin(pin);
        if (tipo == null) {
            throw new DatoInvalidoException("Seleccione el tipo de cuenta.");
        }
        BigDecimal monto = Validador.monto(montoTexto);
        if (monto.compareTo(DEPOSITO_MINIMO) < 0) {
            throw new DatoInvalidoException("El depósito mínimo de apertura es ₡10 000.");
        }

        Cliente cliente = new Cliente(cedula, nombre, pin);
        // Se crea el tipo concreto de cuenta; el resto del sistema la trata como Cuenta
        Cuenta cuenta = switch (tipo) {
            case AHORRO -> new CuentaAhorro(cliente, monto, TASA_AHORRO);
            case CORRIENTE -> new CuentaCorriente(cliente, monto, SOBREGIRO_CORRIENTE);
        };
        this.banco.agregarCliente(cliente);
        this.banco.agregarCuenta(cuenta);
        this.dao.guardar(this.banco);
        return cuenta;
    }

    /**
     * HU-02: valida la cédula y el PIN. Al tercer intento fallido el cliente
     * queda bloqueado y el bloqueo se guarda.
     *
     * @param cedulaTexto cédula digitada
     * @param pin         PIN digitado
     * @return el cliente autenticado
     * @throws FideBankException si los datos no son válidos o el acceso falla
     * @throws IOException       si no se puede guardar el contador de intentos
     */
    public synchronized Cliente iniciarSesion(String cedulaTexto, String pin) throws FideBankException, IOException {
        String cedula = Validador.cedula(cedulaTexto);
        Validador.pin(pin);
        Cliente cliente = this.banco.buscarCliente(cedula);
        if (cliente == null) {
            throw new AutenticacionException("Cédula o PIN incorrectos.", -1, false);
        }
        if (cliente.isBloqueado()) {
            throw new AutenticacionException("Acceso bloqueado por intentos fallidos. Diríjase a una sucursal.", 0, true);
        }
        boolean correcto = cliente.validarPin(pin);
        this.dao.guardar(this.banco); // Se guarda el contador de intentos o su reinicio
        if (!correcto) {
            if (cliente.isBloqueado()) {
                throw new AutenticacionException("Tercer intento fallido. El acceso fue bloqueado.", 0, true);
            }
            throw new AutenticacionException("Cédula o PIN incorrectos. Intentos restantes: "
                    + cliente.getIntentosRestantes(), cliente.getIntentosRestantes(), false);
        }
        return cliente;
    }

    /**
     * HU-04: retira dinero. Cada tipo de cuenta aplica su propia regla.
     *
     * @param cuenta     cuenta seleccionada
     * @param montoTexto monto digitado
     * @return la transacción registrada
     * @throws FideBankException si el monto no es válido o no alcanza el saldo
     * @throws IOException       si no se pueden guardar los datos
     */
    public synchronized Transaccion retirar(Cuenta cuenta, String montoTexto) throws FideBankException, IOException {
        BigDecimal monto = Validador.monto(montoTexto);
        cuenta.retirar(monto); // Polimorfismo: CuentaAhorro o CuentaCorriente
        return this.registrar(new Transaccion(TipoTransaccion.RETIRO, monto, cuenta.getNumero(), null, cuenta.getSaldo()));
    }

    /**
     * HU-05: deposita dinero en la cuenta.
     *
     * @param cuenta     cuenta seleccionada
     * @param montoTexto monto digitado
     * @return la transacción registrada
     * @throws FideBankException si el monto no es válido
     * @throws IOException       si no se pueden guardar los datos
     */
    public synchronized Transaccion depositar(Cuenta cuenta, String montoTexto) throws FideBankException, IOException {
        BigDecimal monto = Validador.monto(montoTexto);
        cuenta.depositar(monto);
        return this.registrar(new Transaccion(TipoTransaccion.DEPOSITO, monto, cuenta.getNumero(), null, cuenta.getSaldo()));
    }

    /**
     * HU-06: pasa dinero de una cuenta a otra cuenta de FideBank. Si el
     * retiro falla no se toca la cuenta destino.
     *
     * @param origen       cuenta de origen
     * @param destinoTexto número de la cuenta destino digitado
     * @param montoTexto   monto digitado
     * @return la transacción registrada
     * @throws FideBankException si algún dato no es válido o no alcanza el saldo
     * @throws IOException       si no se pueden guardar los datos
     */
    public synchronized Transaccion transferir(Cuenta origen, String destinoTexto, String montoTexto)
            throws FideBankException, IOException {
        String numeroDestino = Validador.numeroCuenta(destinoTexto);
        Cuenta destino = this.banco.buscarCuenta(numeroDestino);
        if (destino == null) {
            throw new DatoInvalidoException("La cuenta destino " + numeroDestino + " no existe.");
        }
        if (destino == origen) {
            throw new DatoInvalidoException("La cuenta destino debe ser distinta a la de origen.");
        }
        BigDecimal monto = Validador.monto(montoTexto);
        origen.retirar(monto);   // Si no alcanza, se lanza la excepción y no se deposita
        destino.depositar(monto);
        return this.registrar(new Transaccion(TipoTransaccion.TRANSFERENCIA, monto, origen.getNumero(),
                destino.getNumero(), origen.getSaldo()));
    }

    /**
     * HU-07: "imprime" el comprobante guardándolo en un archivo de texto.
     *
     * @param transaccion transacción a imprimir
     * @return ruta del archivo creado
     * @throws IOException si no se puede escribir el archivo
     */
    public Path imprimirComprobante(Transaccion transaccion) throws IOException {
        Files.createDirectories(CARPETA_COMPROBANTES);
        Path archivo = CARPETA_COMPROBANTES.resolve("comprobante_" + transaccion.getId() + ".txt");
        try (BufferedWriter escritor = Files.newBufferedWriter(archivo, StandardCharsets.UTF_8)) {
            escritor.write(transaccion.mostrarInformacion());
            escritor.newLine();
        }
        return archivo;
    }

    /**
     * HU-08: movimientos de una cuenta, incluidas las transferencias
     * recibidas, del más reciente al más antiguo.
     *
     * @param cuenta cuenta consultada
     * @return lista nueva con las transacciones de la cuenta
     */
    public synchronized List<Transaccion> obtenerHistorial(Cuenta cuenta) {
        List<Transaccion> historial = new ArrayList<>();
        for (Transaccion t : this.banco.getTransacciones()) {
            if (t.involucraCuenta(cuenta.getNumero())) {
                historial.add(t);
            }
        }
        historial.sort(Comparator.reverseOrder()); // Usa el compareTo de Transaccion
        return historial;
    }

    /**
     * Agrega la transacción a la bitácora y guarda los datos.
     */
    private Transaccion registrar(Transaccion transaccion) throws IOException {
        this.banco.registrarTransaccion(transaccion);
        this.dao.guardar(this.banco);
        return transaccion;
    }

    /**
     * @return nombre del banco
     */
    public String getNombreBanco() {
        return this.banco.getNombre();
    }
}
