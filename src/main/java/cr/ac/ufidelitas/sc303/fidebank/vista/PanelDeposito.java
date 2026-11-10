package cr.ac.ufidelitas.sc303.fidebank.vista;

import cr.ac.ufidelitas.sc303.fidebank.modelo.Cuenta;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

/**
 * HU-05: depósito de dinero. Sigue la misma distribución de la pantalla de
 * retiro del prototipo, sin los montos rápidos.
 */
public class PanelDeposito extends PantallaBase {

    private static final long serialVersionUID = 1L;

    private final transient VentanaCajero ventana;
    private final JComboBox<Cuenta> listaCuentas;
    private final CampoConPista campoMonto;
    private final JButton botonDepositar;
    private final JButton botonVolver;

    /**
     * @param ventana ventana del cajero
     */
    public PanelDeposito(VentanaCajero ventana) {
        super("Depósito");
        this.ventana = ventana;

        this.listaCuentas = Estilos.decorarCampo(new JComboBox<>());
        this.listaCuentas.setBorder(null);
        this.campoMonto = new CampoConPista("25 000");
        this.botonDepositar = Estilos.botonPrimario("Depositar");
        this.botonVolver = Estilos.botonSecundario("Volver");

        this.agregar(Estilos.etiqueta("Cuenta"), 0, 0, 1, 0);
        this.agregar(this.listaCuentas, 0, 1, 1, 6);
        this.agregar(Estilos.etiqueta("Monto a depositar"), 0, 2, 1, 14);
        this.agregar(this.campoMonto, 0, 3, 1, 6);
        this.agregar(Estilos.ayuda("El monto debe ser numérico y mayor que cero. El saldo se actualiza de inmediato."),
                0, 4, 1, 8);

        JPanel acciones = this.filaBotones(this.botonVolver, this.botonDepositar);
        this.agregar(acciones, 0, 5, 1, 16);
        this.agregarRelleno(6);

        this.botonVolver.addActionListener(e -> this.ventana.mostrar(VentanaCajero.MENU));
        this.botonDepositar.addActionListener(e -> this.depositar());
    }

    private void depositar() {
        Cuenta cuenta = (Cuenta) this.listaCuentas.getSelectedItem();
        String monto = this.campoMonto.getText();
        this.ejecutarEnSegundoPlano(
                () -> this.ventana.getServicio().depositar(cuenta, monto),
                this.ventana::mostrarComprobante,
                error -> Dialogos.mostrarError(this, error),
                this.botonDepositar, this.botonVolver);
    }

    @Override
    public void alMostrar() {
        this.ventana.llenarCuentas(this.listaCuentas);
        this.campoMonto.setText("");
    }

    @Override
    public JButton getBotonPorDefecto() {
        return this.botonDepositar;
    }
}
