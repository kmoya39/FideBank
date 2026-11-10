package cr.ac.ufidelitas.sc303.fidebank.vista;

import cr.ac.ufidelitas.sc303.fidebank.modelo.Cuenta;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

/**
 * HU-06: transferencia a otra cuenta de FideBank (Figura 5 del prototipo).
 */
public class PanelTransferencia extends PantallaBase {

    private static final long serialVersionUID = 1L;

    private final transient VentanaCajero ventana;
    private final JComboBox<Cuenta> listaOrigen;
    private final CampoConPista campoDestino;
    private final CampoConPista campoMonto;
    private final JButton botonTransferir;
    private final JButton botonVolver;

    /**
     * @param ventana ventana del cajero
     */
    public PanelTransferencia(VentanaCajero ventana) {
        super("Transferencia");
        this.ventana = ventana;

        this.listaOrigen = Estilos.decorarCampo(new JComboBox<>());
        this.listaOrigen.setBorder(null);
        this.campoDestino = new CampoConPista("FB-1003");
        this.campoMonto = new CampoConPista("100 000");
        this.botonTransferir = Estilos.botonPrimario("Transferir");
        this.botonVolver = Estilos.botonSecundario("Volver");

        this.agregar(Estilos.etiqueta("Cuenta origen"), 0, 0, 1, 0);
        this.agregar(this.listaOrigen, 0, 1, 1, 6);
        this.agregar(Estilos.etiqueta("Cuenta destino"), 0, 2, 1, 14);
        this.agregar(this.campoDestino, 0, 3, 1, 6);
        this.agregar(Estilos.etiqueta("Monto"), 0, 4, 1, 14);
        this.agregar(this.campoMonto, 0, 5, 1, 6);

        JPanel acciones = this.filaBotones(this.botonVolver, this.botonTransferir);
        this.agregar(acciones, 0, 6, 1, 18);
        this.agregarRelleno(7);

        this.botonVolver.addActionListener(e -> this.ventana.mostrar(VentanaCajero.MENU));
        this.botonTransferir.addActionListener(e -> this.transferir());
    }

    private void transferir() {
        Cuenta origen = (Cuenta) this.listaOrigen.getSelectedItem();
        String destino = this.campoDestino.getText();
        String monto = this.campoMonto.getText();
        this.ejecutarEnSegundoPlano(
                () -> this.ventana.getServicio().transferir(origen, destino, monto),
                this.ventana::mostrarComprobante,
                error -> Dialogos.mostrarError(this, error),
                this.botonTransferir, this.botonVolver);
    }

    @Override
    public void alMostrar() {
        this.ventana.llenarCuentas(this.listaOrigen);
        this.campoDestino.setText("");
        this.campoMonto.setText("");
    }

    @Override
    public JButton getBotonPorDefecto() {
        return this.botonTransferir;
    }
}
