package cr.ac.ufidelitas.sc303.fidebank.vista;

import cr.ac.ufidelitas.sc303.fidebank.modelo.Cuenta;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

/**
 * HU-04: retiro de efectivo con montos rápidos (Figura 4 del prototipo).
 */
public class PanelRetiro extends PantallaBase {

    private static final long serialVersionUID = 1L;
    private static final String[] MONTOS_RAPIDOS = {"10 000", "20 000", "50 000", "100 000", "200 000"};

    private final transient VentanaCajero ventana;
    private final JComboBox<Cuenta> listaCuentas;
    private final CampoConPista campoMonto;
    private final JButton botonRetirar;
    private final JButton botonVolver;

    /**
     * @param ventana ventana del cajero
     */
    public PanelRetiro(VentanaCajero ventana) {
        super("Retiro de efectivo");
        this.ventana = ventana;

        this.listaCuentas = Estilos.decorarCampo(new JComboBox<>());
        this.listaCuentas.setBorder(null);
        this.campoMonto = new CampoConPista("50 000");
        this.botonRetirar = Estilos.botonPrimario("Retirar");
        this.botonVolver = Estilos.botonSecundario("Volver");

        this.agregar(Estilos.etiqueta("Cuenta"), 0, 0, 1, 0);
        this.agregar(this.listaCuentas, 0, 1, 1, 6);
        this.agregar(Estilos.etiqueta("Montos rápidos"), 0, 2, 1, 14);
        this.agregar(this.crearMontosRapidos(), 0, 3, 1, 6);
        this.agregar(Estilos.etiqueta("Monto"), 0, 4, 1, 14);
        this.agregar(this.campoMonto, 0, 5, 1, 6);
        this.agregar(Estilos.ayuda("Si el saldo no alcanza se muestra: \"Saldo insuficiente\"."), 0, 6, 1, 8);

        JPanel acciones = this.filaBotones(this.botonVolver, this.botonRetirar);
        this.agregar(acciones, 0, 7, 1, 16);
        this.agregarRelleno(8);

        this.botonVolver.addActionListener(e -> this.ventana.mostrar(VentanaCajero.MENU));
        this.botonRetirar.addActionListener(e -> this.retirar());
    }

    private JPanel crearMontosRapidos() {
        JPanel montos = new JPanel(new GridLayout(2, 3, 14, 10));
        montos.setOpaque(false);
        for (String monto : MONTOS_RAPIDOS) {
            JButton boton = Estilos.botonSecundario("₡" + monto);
            boton.addActionListener(e -> this.campoMonto.setText(monto));
            montos.add(boton);
        }
        JButton otro = Estilos.botonSecundario("Otro monto");
        otro.addActionListener(e -> {
            this.campoMonto.setText("");
            this.campoMonto.requestFocusInWindow();
        });
        montos.add(otro);
        return montos;
    }

    private void retirar() {
        Cuenta cuenta = (Cuenta) this.listaCuentas.getSelectedItem();
        String monto = this.campoMonto.getText();
        this.ejecutarEnSegundoPlano(
                () -> this.ventana.getServicio().retirar(cuenta, monto),
                this.ventana::mostrarComprobante,
                error -> Dialogos.mostrarError(this, error),
                this.botonRetirar, this.botonVolver);
    }

    @Override
    public void alMostrar() {
        this.ventana.llenarCuentas(this.listaCuentas);
        this.campoMonto.setText("");
    }

    @Override
    public JButton getBotonPorDefecto() {
        return this.botonRetirar;
    }
}
