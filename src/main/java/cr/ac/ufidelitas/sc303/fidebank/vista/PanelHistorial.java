package cr.ac.ufidelitas.sc303.fidebank.vista;

import cr.ac.ufidelitas.sc303.fidebank.modelo.Cuenta;
import cr.ac.ufidelitas.sc303.fidebank.util.Formato;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * HU-08: historial de movimientos de la cuenta seleccionada, incluidas las
 * transferencias recibidas, del más reciente al más antiguo.
 */
public class PanelHistorial extends PantallaBase {

    private static final long serialVersionUID = 1L;

    private final transient VentanaCajero ventana;
    private final JLabel encabezadoCuenta;
    private final JLabel mensajeVacio;
    private final ModeloTablaHistorial modelo;
    private final JButton botonVolver;

    /**
     * @param ventana ventana del cajero
     */
    public PanelHistorial(VentanaCajero ventana) {
        super("Historial de movimientos");
        this.ventana = ventana;

        this.encabezadoCuenta = Estilos.etiqueta("");
        this.mensajeVacio = Estilos.ayuda("No hay movimientos registrados en esta cuenta.");
        this.modelo = new ModeloTablaHistorial();

        JTable tabla = new JTable(this.modelo);
        tabla.setFont(Estilos.FUENTE_AYUDA);
        tabla.setRowHeight(26);
        tabla.getTableHeader().setFont(Estilos.FUENTE_BOTON);
        tabla.getTableHeader().setBackground(Estilos.COLOR_ENCABEZADO);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.setFillsViewportHeight(true);
        DefaultTableCellRenderer derecha = new DefaultTableCellRenderer();
        derecha.setHorizontalAlignment(SwingConstants.RIGHT);
        tabla.getColumnModel().getColumn(4).setCellRenderer(derecha);
        int[] anchos = {40, 130, 110, 170, 140};
        for (int i = 0; i < anchos.length; i++) {
            tabla.getColumnModel().getColumn(i).setPreferredWidth(anchos[i]);
        }
        JScrollPane desplazamiento = new JScrollPane(tabla);
        desplazamiento.setBorder(BorderFactory.createLineBorder(Estilos.COLOR_BORDE, 2, true));
        desplazamiento.setPreferredSize(new Dimension(580, 300));

        this.botonVolver = Estilos.botonPrimario("Volver");
        this.botonVolver.addActionListener(e -> this.ventana.mostrar(VentanaCajero.MENU));

        this.agregar(this.encabezadoCuenta, 0, 0, 1, 0);
        this.agregar(desplazamiento, 0, 1, 1, 10);
        this.agregar(this.mensajeVacio, 0, 2, 1, 8);
        JPanel acciones = this.filaBotones(this.botonVolver);
        this.agregar(acciones, 0, 3, 1, 14);
        this.agregarRelleno(4);
    }

    @Override
    public void alMostrar() {
        Cuenta cuenta = this.ventana.getCuentaActual();
        if (cuenta == null) {
            return;
        }
        this.encabezadoCuenta.setText("Cuenta: " + cuenta + "   |   Saldo: " + Formato.colones(cuenta.getSaldo()));
        this.mensajeVacio.setVisible(false);
        this.ejecutarEnSegundoPlano(
                () -> this.ventana.getServicio().obtenerHistorial(cuenta),
                historial -> {
                    this.modelo.setDatos(historial, cuenta.getNumero());
                    this.mensajeVacio.setVisible(historial.isEmpty());
                },
                error -> Dialogos.mostrarError(this, error),
                this.botonVolver);
    }

    @Override
    public JButton getBotonPorDefecto() {
        return this.botonVolver;
    }
}
