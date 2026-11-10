package cr.ac.ufidelitas.sc303.fidebank.vista;

import cr.ac.ufidelitas.sc303.fidebank.modelo.Cuenta;
import cr.ac.ufidelitas.sc303.fidebank.modelo.TipoCuenta;
import cr.ac.ufidelitas.sc303.fidebank.util.Formato;
import java.awt.FlowLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.text.AbstractDocument;

/**
 * HU-01: registro del cliente y apertura de su cuenta en la sucursal
 * (Figura 7 del prototipo).
 */
public class PanelApertura extends PantallaBase {

    private static final long serialVersionUID = 1L;
    private static final int SEPARACION_COLUMNAS = 18;

    private final transient VentanaSucursal ventana;
    private final CampoConPista campoCedula;
    private final CampoConPista campoNombre;
    private final JPasswordField campoPin;
    private final JRadioButton opcionAhorro;
    private final JRadioButton opcionCorriente;
    private final ButtonGroup grupoTipo;
    private final CampoConPista campoDeposito;
    private final JButton botonAbrir;
    private final JButton botonLimpiar;

    /**
     * @param ventana ventana de la sucursal
     */
    public PanelApertura(VentanaSucursal ventana) {
        super("Sucursal | Apertura de cuenta");
        this.ventana = ventana;

        this.campoCedula = new CampoConPista("3-0456-0789");
        this.campoNombre = new CampoConPista("María Arias Mora");
        this.campoPin = Estilos.decorarCampo(new JPasswordField());
        ((AbstractDocument) this.campoPin.getDocument()).setDocumentFilter(new FiltroDigitos(4));
        this.opcionAhorro = this.crearOpcion("Ahorro");
        this.opcionCorriente = this.crearOpcion("Corriente");
        this.grupoTipo = new ButtonGroup();
        this.grupoTipo.add(this.opcionAhorro);
        this.grupoTipo.add(this.opcionCorriente);
        JPanel panelTipo = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        panelTipo.setBackground(Estilos.COLOR_FONDO);
        panelTipo.setBorder(Estilos.bordeCampo());
        panelTipo.add(this.opcionAhorro);
        panelTipo.add(this.opcionCorriente);
        this.campoDeposito = new CampoConPista("60 000");
        this.botonAbrir = Estilos.botonPrimario("Abrir cuenta");
        this.botonLimpiar = Estilos.botonSecundario("Limpiar");

        int s = SEPARACION_COLUMNAS;
        this.agregar(Estilos.etiqueta("Cédula"), 0, 0, 1, 0, s);
        this.agregar(Estilos.etiqueta("Nombre completo"), 1, 0, 1, 0);
        this.agregar(this.campoCedula, 0, 1, 1, 6, s);
        this.agregar(this.campoNombre, 1, 1, 1, 6);
        this.agregar(Estilos.etiqueta("PIN inicial"), 0, 2, 1, 16, s);
        this.agregar(Estilos.etiqueta("Tipo de cuenta"), 1, 2, 1, 16);
        this.agregar(this.campoPin, 0, 3, 1, 6, s);
        this.agregar(panelTipo, 1, 3, 1, 6);
        this.agregar(Estilos.etiqueta("Depósito inicial"), 0, 4, 1, 16, s);
        this.agregar(this.campoDeposito, 0, 5, 1, 6, s);
        this.agregar(Estilos.ayuda("Depósito mínimo de apertura: ₡10 000."), 0, 6, 2, 8);

        JPanel acciones = this.filaBotones(this.botonLimpiar, this.botonAbrir);
        this.agregar(acciones, 0, 7, 2, 18);
        this.agregarRelleno(8);

        this.botonLimpiar.addActionListener(e -> this.alMostrar());
        this.botonAbrir.addActionListener(e -> this.abrirCuenta());
    }

    private JRadioButton crearOpcion(String texto) {
        JRadioButton opcion = new JRadioButton(texto);
        opcion.setFont(Estilos.FUENTE_ETIQUETA);
        opcion.setBackground(Estilos.COLOR_FONDO);
        opcion.setFocusPainted(false);
        return opcion;
    }

    /**
     * @return tipo marcado o null si no se marcó ninguno
     */
    private TipoCuenta tipoSeleccionado() {
        if (this.opcionAhorro.isSelected()) {
            return TipoCuenta.AHORRO;
        }
        if (this.opcionCorriente.isSelected()) {
            return TipoCuenta.CORRIENTE;
        }
        return null;
    }

    private void abrirCuenta() {
        String cedula = this.campoCedula.getText();
        String nombre = this.campoNombre.getText();
        String pin = new String(this.campoPin.getPassword());
        TipoCuenta tipo = this.tipoSeleccionado();
        String deposito = this.campoDeposito.getText();
        this.ejecutarEnSegundoPlano(
                () -> this.ventana.getServicio().abrirCuentaClienteNuevo(cedula, nombre, pin, tipo, deposito),
                this::confirmarApertura,
                error -> Dialogos.mostrarError(this, error),
                this.botonAbrir, this.botonLimpiar);
    }

    private void confirmarApertura(Cuenta cuenta) {
        Dialogos.informar(this, "Cuenta abierta con éxito.\n\n"
                + "Cliente: " + cuenta.getTitular().getNombre() + "\n"
                + "Cuenta: " + cuenta + "\n"
                + "Saldo inicial: " + Formato.colones(cuenta.getSaldo()) + "\n\n"
                + "El cliente ya puede ingresar al cajero con su cédula y su PIN.");
        this.alMostrar();
    }

    @Override
    public void alMostrar() {
        this.campoCedula.setText("");
        this.campoNombre.setText("");
        this.campoPin.setText("");
        this.grupoTipo.clearSelection();
        this.campoDeposito.setText("");
        this.campoCedula.requestFocusInWindow();
    }

    @Override
    public JButton getBotonPorDefecto() {
        return this.botonAbrir;
    }
}
