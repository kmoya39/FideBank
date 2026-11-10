package cr.ac.ufidelitas.sc303.fidebank.vista;

import cr.ac.ufidelitas.sc303.fidebank.modelo.Cliente;
import cr.ac.ufidelitas.sc303.fidebank.modelo.Cuenta;
import cr.ac.ufidelitas.sc303.fidebank.modelo.Transaccion;
import cr.ac.ufidelitas.sc303.fidebank.servicio.BancoServicio;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.util.LinkedHashMap;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Ventana del cajero automático. Usa un CardLayout para pasar de una
 * pantalla a otra sin abrir ventanas nuevas, como en un cajero real, y
 * guarda el cliente y la cuenta con los que se está trabajando.
 */
public class VentanaCajero extends JFrame {

    private static final long serialVersionUID = 1L;

    public static final String INGRESO = "ingreso";
    public static final String MENU = "menu";
    public static final String SALDO = "saldo";
    public static final String RETIRO = "retiro";
    public static final String DEPOSITO = "deposito";
    public static final String TRANSFERENCIA = "transferencia";
    public static final String COMPROBANTE = "comprobante";
    public static final String HISTORIAL = "historial";

    private final transient BancoServicio servicio;
    private final CardLayout tarjetas;
    private final JPanel contenedor;
    /** Pantallas por nombre, para llamar a alMostrar() al cambiar. */
    private final LinkedHashMap<String, PantallaBase> pantallas;
    private transient Cliente clienteActual;
    private transient Cuenta cuentaActual;

    /**
     * @param servicio servicio compartido del banco
     */
    public VentanaCajero(BancoServicio servicio) {
        super("FideBank - Cajero");
        this.servicio = servicio;
        this.tarjetas = new CardLayout();
        this.contenedor = new JPanel(this.tarjetas);
        this.pantallas = new LinkedHashMap<>();
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.agregarPantalla(INGRESO, new PanelIngreso(this));              // HU-02
        this.agregarPantalla(MENU, new PanelMenu(this));                    // HU-03
        this.agregarPantalla(SALDO, new PanelSaldo(this));                  // HU-03
        this.agregarPantalla(RETIRO, new PanelRetiro(this));                // HU-04
        this.agregarPantalla(DEPOSITO, new PanelDeposito(this));            // HU-05
        this.agregarPantalla(TRANSFERENCIA, new PanelTransferencia(this));  // HU-06

        this.setContentPane(this.contenedor);
        this.pack();
        this.setMinimumSize(new Dimension(660, this.getHeight()));
        this.setLocationByPlatform(true);
        this.mostrar(INGRESO);
    }

    private void agregarPantalla(String nombre, PantallaBase pantalla) {
        this.pantallas.put(nombre, pantalla);
        this.contenedor.add(pantalla, nombre);
    }

    /**
     * Cambia a la pantalla indicada y le pide que refresque sus datos.
     *
     * @param nombre nombre de la pantalla (constantes de esta clase)
     */
    public void mostrar(String nombre) {
        PantallaBase pantalla = this.pantallas.get(nombre);
        if (pantalla == null) {
            return;
        }
        pantalla.alMostrar(); // Polimorfismo: cada pantalla sabe cómo refrescarse
        this.getRootPane().setDefaultButton(pantalla.getBotonPorDefecto());
        this.tarjetas.show(this.contenedor, nombre);
    }

    /**
     * Se llama cuando el cliente ingresa con éxito (HU-02).
     *
     * @param cliente cliente autenticado
     */
    public void iniciarSesion(Cliente cliente) {
        this.clienteActual = cliente;
        this.cuentaActual = cliente.getCuentas().get(0);
        this.mostrar(MENU);
    }

    /**
     * Olvida al cliente y vuelve a la pantalla de ingreso.
     */
    public void cerrarSesion() {
        this.clienteActual = null;
        this.cuentaActual = null;
        this.mostrar(INGRESO);
    }

    /**
     * Muestra el comprobante de la operación recién hecha (HU-07).
     *
     * @param transaccion transacción realizada
     */
    public void mostrarComprobante(Transaccion transaccion) {
        // La pantalla del comprobante se agrega con la HU-07
        this.mostrar(MENU);
    }

    /**
     * Llena una lista desplegable con las cuentas del cliente y selecciona
     * la cuenta actual. Al cambiar la selección se actualiza la cuenta actual.
     *
     * @param lista lista desplegable de cuentas
     */
    public void llenarCuentas(JComboBox<Cuenta> lista) {
        for (var oyente : lista.getActionListeners()) {
            lista.removeActionListener(oyente);
        }
        lista.removeAllItems();
        if (this.clienteActual == null) {
            return;
        }
        for (Cuenta cuenta : this.clienteActual.getCuentas()) {
            lista.addItem(cuenta);
        }
        lista.setSelectedItem(this.cuentaActual);
        lista.addActionListener(e -> {
            if (lista.getSelectedItem() instanceof Cuenta seleccionada) {
                this.cuentaActual = seleccionada;
            }
        });
    }

    public BancoServicio getServicio() {
        return this.servicio;
    }

    public Cliente getClienteActual() {
        return this.clienteActual;
    }

    public Cuenta getCuentaActual() {
        return this.cuentaActual;
    }
}
