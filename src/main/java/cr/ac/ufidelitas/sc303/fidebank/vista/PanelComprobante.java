package cr.ac.ufidelitas.sc303.fidebank.vista;

import cr.ac.ufidelitas.sc303.fidebank.modelo.Transaccion;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * HU-07: muestra el comprobante al terminar cada operación y permite
 * imprimirlo en un archivo de texto (Figura 6 del prototipo).
 */
public class PanelComprobante extends PantallaBase {

    private static final long serialVersionUID = 1L;

    private final transient VentanaCajero ventana;
    private final JTextArea texto;
    private final JButton botonImprimir;
    private final JButton botonNoImprimir;
    private final JLabel nota;
    private transient Transaccion transaccion;

    /**
     * @param ventana ventana del cajero
     */
    public PanelComprobante(VentanaCajero ventana) {
        super("Comprobante");
        this.ventana = ventana;

        this.texto = new JTextArea(10, 34);
        this.texto.setEditable(false);
        this.texto.setFont(Estilos.FUENTE_MONO);
        this.texto.setBackground(Estilos.COLOR_COMPROBANTE);
        this.texto.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createDashedBorder(Estilos.COLOR_AYUDA, 2, 6, 4, false),
                BorderFactory.createEmptyBorder(10, 14, 10, 14)));
        this.botonImprimir = Estilos.botonPrimario("Imprimir comprobante");
        this.botonNoImprimir = Estilos.botonSecundario("No imprimir");
        this.nota = Estilos.nota("Imprimir genera el archivo comprobante_N.txt");

        this.agregar(this.texto, 0, 0, 1, 0);
        JPanel acciones = this.filaBotones(this.botonNoImprimir, this.botonImprimir);
        this.agregar(acciones, 0, 1, 1, 18);
        JPanel filaNota = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        filaNota.setOpaque(false);
        filaNota.add(this.nota);
        this.agregar(filaNota, 0, 2, 1, 14);
        this.agregarRelleno(3);

        this.botonNoImprimir.addActionListener(e -> this.ventana.mostrar(VentanaCajero.MENU));
        this.botonImprimir.addActionListener(e -> this.imprimir());
    }

    /**
     * @param transaccion transacción cuyo comprobante se mostrará
     */
    public void setTransaccion(Transaccion transaccion) {
        this.transaccion = transaccion;
    }

    private void imprimir() {
        this.ejecutarEnSegundoPlano(
                () -> this.ventana.getServicio().imprimirComprobante(this.transaccion),
                archivo -> {
                    Dialogos.informar(this, "Comprobante impreso en el archivo:\n" + archivo.toAbsolutePath());
                    this.ventana.mostrar(VentanaCajero.MENU);
                },
                error -> Dialogos.mostrarError(this, error),
                this.botonImprimir, this.botonNoImprimir);
    }

    @Override
    public void alMostrar() {
        if (this.transaccion != null) {
            this.texto.setText(this.transaccion.mostrarInformacion());
            this.nota.setText("Imprimir genera el archivo comprobante_" + this.transaccion.getId() + ".txt");
        }
    }

    @Override
    public JButton getBotonPorDefecto() {
        return this.botonImprimir;
    }
}
