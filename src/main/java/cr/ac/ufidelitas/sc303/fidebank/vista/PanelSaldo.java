package cr.ac.ufidelitas.sc303.fidebank.vista;

import cr.ac.ufidelitas.sc303.fidebank.modelo.Cuenta;
import cr.ac.ufidelitas.sc303.fidebank.modelo.CuentaAhorro;
import cr.ac.ufidelitas.sc303.fidebank.modelo.CuentaCorriente;
import cr.ac.ufidelitas.sc303.fidebank.util.Formato;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * HU-03: consulta de saldo. Muestra el tipo, el número y el saldo de la
 * cuenta seleccionada; en la cuenta corriente también el sobregiro.
 */
public class PanelSaldo extends PantallaBase {

    private static final long serialVersionUID = 1L;

    private final transient VentanaCajero ventana;
    private final JLabel valorTipo;
    private final JLabel valorNumero;
    private final JLabel valorTitular;
    private final JLabel valorSaldo;
    private final JLabel etiquetaDetalle;
    private final JLabel valorDetalle;
    private final JLabel valorDisponible;
    private final JButton botonVolver;

    /**
     * @param ventana ventana del cajero
     */
    public PanelSaldo(VentanaCajero ventana) {
        super("Consulta de saldo");
        this.ventana = ventana;

        this.valorTipo = Estilos.etiqueta("");
        this.valorNumero = Estilos.etiqueta("");
        this.valorTitular = Estilos.etiqueta("");
        this.valorSaldo = Estilos.etiqueta("");
        this.valorSaldo.setFont(Estilos.FUENTE_TITULO);
        this.etiquetaDetalle = Estilos.etiqueta("");
        this.valorDetalle = Estilos.etiqueta("");
        this.valorDisponible = Estilos.etiqueta("");

        JPanel datos = new JPanel(new GridLayout(6, 2, 10, 12));
        datos.setBackground(Estilos.COLOR_COMPROBANTE);
        datos.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createDashedBorder(Estilos.COLOR_AYUDA, 2, 6, 4, false),
                BorderFactory.createEmptyBorder(16, 18, 16, 18)));
        datos.add(Estilos.etiqueta("Tipo de cuenta"));
        datos.add(this.valorTipo);
        datos.add(Estilos.etiqueta("Número de cuenta"));
        datos.add(this.valorNumero);
        datos.add(Estilos.etiqueta("Titular"));
        datos.add(this.valorTitular);
        datos.add(Estilos.etiqueta("Saldo actual"));
        datos.add(this.valorSaldo);
        datos.add(this.etiquetaDetalle);
        datos.add(this.valorDetalle);
        datos.add(Estilos.etiqueta("Disponible para retirar"));
        datos.add(this.valorDisponible);
        this.agregar(datos, 0, 0, 1, 0);

        this.botonVolver = Estilos.botonPrimario("Volver");
        this.botonVolver.addActionListener(e -> this.ventana.mostrar(VentanaCajero.MENU));
        JPanel acciones = this.filaBotones(this.botonVolver);
        this.agregar(acciones, 0, 1, 1, 20);
        this.agregarRelleno(2);
    }

    @Override
    public void alMostrar() {
        Cuenta cuenta = this.ventana.getCuentaActual();
        if (cuenta == null) {
            return;
        }
        this.valorTipo.setText(cuenta.getTipo().getDescripcion());
        this.valorNumero.setText(cuenta.getNumero());
        this.valorTitular.setText(cuenta.getTitular().getNombre());
        this.valorSaldo.setText(Formato.colones(cuenta.getSaldo()));
        // La fila cambia según el tipo concreto de cuenta
        if (cuenta instanceof CuentaCorriente corriente) {
            this.etiquetaDetalle.setText("Sobregiro autorizado");
            this.valorDetalle.setText(Formato.colones(corriente.getSobregiro()));
        } else if (cuenta instanceof CuentaAhorro ahorro) {
            this.etiquetaDetalle.setText("Tasa de interés anual");
            this.valorDetalle.setText(ahorro.getTasaInteres().toPlainString().replace('.', ',') + " %");
        }
        this.valorDisponible.setText(Formato.colones(cuenta.getDisponible()));
    }

    @Override
    public JButton getBotonPorDefecto() {
        return this.botonVolver;
    }
}
