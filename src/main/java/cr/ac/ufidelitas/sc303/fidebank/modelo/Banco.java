package cr.ac.ufidelitas.sc303.fidebank.modelo;

import cr.ac.ufidelitas.sc303.fidebank.excepcion.SaldoInsuficienteException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Clase central que guarda los clientes y las transacciones y ofrece
 * las operaciones del cajero.
 */
public class Banco implements Mostrable {

    private String nombre;
    private ArrayList<Cliente> clientes;
    private ArrayList<Transaccion> transacciones;

    /**
     * @param nombre nombre del banco
     */
    public Banco(String nombre) {
        this.nombre = nombre;
        this.clientes = new ArrayList<>();
        this.transacciones = new ArrayList<>();
    }

    /**
     * Registra un cliente nuevo si la cédula no existe.
     *
     * @return el cliente creado
     */
    public Cliente registrarCliente(String cedula, String nombre, String pin) {
        if (buscarCliente(cedula) != null) {
            throw new IllegalArgumentException("Ya existe un cliente con esa cédula.");
        }
        if (!pin.matches("\\d{4}")) {
            throw new IllegalArgumentException("El PIN debe tener 4 dígitos.");
        }
        Cliente cliente = new Cliente(cedula, nombre, pin);
        clientes.add(cliente);
        return cliente;
    }

    /**
     * Abre una cuenta de ahorro o corriente a un cliente existente.
     *
     * @param tipo 1 = ahorro, 2 = corriente
     * @return la cuenta creada
     */
    public Cuenta abrirCuenta(String cedula, int tipo, double montoInicial) {
        Cliente cliente = buscarCliente(cedula);
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente no existe.");
        }
        if (montoInicial < 10000) {
            throw new IllegalArgumentException("El depósito mínimo de apertura es ₡10 000.");
        }
        // Se crea el tipo de cuenta elegido
        Cuenta cuenta;
        if (tipo == 1) {
            cuenta = new CuentaAhorro(cliente, montoInicial, 3.5);
        } else if (tipo == 2) {
            cuenta = new CuentaCorriente(cliente, montoInicial, 100000);
        } else {
            throw new IllegalArgumentException("Tipo de cuenta no válido.");
        }
        cliente.agregarCuenta(cuenta);
        return cuenta;
    }

    /**
     * Valida la cédula y el PIN. Informa si el cliente quedó bloqueado.
     *
     * @return el cliente si los datos son correctos
     */
    public Cliente iniciarSesion(String cedula, String pin) {
        Cliente cliente = buscarCliente(cedula);
        if (cliente == null) {
            throw new IllegalArgumentException("Cédula o PIN incorrectos.");
        }
        if (cliente.isBloqueado()) {
            throw new IllegalStateException("Cliente bloqueado. Debe ir a una sucursal.");
        }
        if (!cliente.validarPin(pin)) {
            if (cliente.isBloqueado()) {
                throw new IllegalStateException("Tercer intento fallido. El acceso fue bloqueado.");
            }
            int restantes = Cliente.MAX_INTENTOS - cliente.getIntentosFallidos();
            throw new IllegalArgumentException("Cédula o PIN incorrectos. Intentos restantes: " + restantes);
        }
        return cliente;
    }

    /**
     * Retira dinero y registra la transacción.
     */
    public Transaccion retirar(Cuenta cuenta, double monto) throws SaldoInsuficienteException {
        cuenta.retirar(monto); // Polimorfismo: cada cuenta aplica su regla
        return registrar(new Transaccion(TipoTransaccion.RETIRO, monto, cuenta.getNumero(), null, cuenta.getSaldo()));
    }

    /**
     * Deposita dinero y registra la transacción.
     */
    public Transaccion depositar(Cuenta cuenta, double monto) {
        cuenta.depositar(monto);
        return registrar(new Transaccion(TipoTransaccion.DEPOSITO, monto, cuenta.getNumero(), null, cuenta.getSaldo()));
    }

    /**
     * Pasa dinero de una cuenta a otra cuenta de FideBank.
     */
    public Transaccion transferir(Cuenta origen, String numeroDestino, double monto)
            throws SaldoInsuficienteException {
        Cuenta destino = buscarCuenta(numeroDestino);
        if (destino == null) {
            throw new IllegalArgumentException("La cuenta destino no existe.");
        }
        if (destino == origen) {
            throw new IllegalArgumentException("La cuenta destino debe ser distinta a la de origen.");
        }
        origen.retirar(monto);   // Si no hay saldo, aquí se detiene
        destino.depositar(monto);
        return registrar(new Transaccion(TipoTransaccion.TRANSFERENCIA, monto, origen.getNumero(),
                destino.getNumero(), origen.getSaldo()));
    }

    /**
     * Guarda el comprobante en un archivo de texto (impresión del comprobante).
     *
     * @return nombre del archivo creado
     */
    public String imprimirComprobante(Transaccion transaccion) throws IOException {
        String archivo = "comprobante_" + transaccion.getId() + ".txt";
        // try-with-resources cierra el archivo automáticamente
        try (FileWriter escritor = new FileWriter(archivo)) {
            escritor.write(transaccion.mostrarInformacion());
        }
        return archivo;
    }

    /**
     * @return historial de una cuenta (incluye transferencias recibidas)
     */
    public String mostrarHistorial(Cuenta cuenta) {
        String texto = "";
        for (Transaccion t : transacciones) {
            if (cuenta.getNumero().equals(t.getCuentaOrigen()) || cuenta.getNumero().equals(t.getCuentaDestino())) {
                texto += "#" + t.getId() + " " + t.getTipo() + " ₡" + String.format("%,.2f", t.getMonto()) + "\n";
            }
        }
        return texto.isEmpty() ? "No hay movimientos." : texto;
    }

    /**
     * @return cliente con esa cédula o null
     */
    public Cliente buscarCliente(String cedula) {
        for (Cliente c : clientes) {
            if (c.getCedula().equals(cedula)) {
                return c;
            }
        }
        return null;
    }

    /**
     * @return cuenta con ese número o null
     */
    public Cuenta buscarCuenta(String numero) {
        for (Cliente c : clientes) {
            for (Cuenta cuenta : c.getCuentas()) {
                if (cuenta.getNumero().equalsIgnoreCase(numero)) {
                    return cuenta;
                }
            }
        }
        return null;
    }

    /**
     * Agrega la transacción a la bitácora y la devuelve.
     */
    private Transaccion registrar(Transaccion transaccion) {
        transacciones.add(transaccion);
        return transaccion;
    }

    @Override
    public String mostrarInformacion() {
        String texto = "Banco " + nombre + " - Clientes registrados:\n";
        for (Cliente c : clientes) {
            texto += c.mostrarInformacion();
        }
        return texto;
    }

    public String getNombre() {
        return nombre;
    }

    public ArrayList<Cliente> getClientes() {
        return clientes;
    }
}
