package cr.ac.ufidelitas.sc303.fidebank.vista;

import cr.ac.ufidelitas.sc303.fidebank.modelo.Cuenta;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Menú principal del cajero (Figura 3 del prototipo). Muestra las cuentas
 * del cliente (criterio 3 de la HU-02) y da acceso a las operaciones de
 * las HU-03 a la HU-08.
 */
public class PanelMenu extends PantallaBase {

    private static final long serialVersionUID = 1L;

    private final transient VentanaCajero ventana;
    private final JLabel saludo;
    private final JComboBox<Cuenta> listaCuentas;

    /**
     * @param ventana ventana del cajero
     */
    public PanelMenu(VentanaCajero ventana) {
        super("Menú principal");
        this.ventana = ventana;

        this.saludo = Estilos.etiqueta("Hola. Cuenta:");
        this.listaCuentas = Estilos.decorarCampo(new JComboBox<>());
        this.listaCuentas.setBorder(null);
        JPanel filaSaludo = new JPanel(new BorderLayout(10, 0));
        filaSaludo.setOpaque(false);
        filaSaludo.add(this.saludo, BorderLayout.WEST);
        filaSaludo.add(this.listaCuentas, BorderLayout.CENTER);
        this.agregar(filaSaludo, 0, 0, 1, 0);

        JPanel opciones = new JPanel(new GridLayout(3, 2, 16, 16));
        opciones.setOpaque(false);
        opciones.setPreferredSize(new Dimension(560, 300));
        opciones.add(this.opcion("Consultar saldo", VentanaCajero.SALDO));
        opciones.add(this.opcion("Retirar", VentanaCajero.RETIRO));
        opciones.add(this.opcion("Depositar", VentanaCajero.DEPOSITO));
        opciones.add(this.opcion("Transferir", VentanaCajero.TRANSFERENCIA));
        opciones.add(this.opcion("Historial", VentanaCajero.HISTORIAL));
        JButton salir = Estilos.botonPrimario("Salir");
        salir.addActionListener(e -> this.ventana.cerrarSesion());
        opciones.add(salir);
        this.agregar(opciones, 0, 1, 1, 18);
        this.agregarRelleno(2);
    }

    private JButton opcion(String texto, String pantalla) {
        JButton boton = Estilos.botonSecundario(texto);
        boton.addActionListener(e -> this.ventana.mostrar(pantalla));
        return boton;
    }

    @Override
    public void alMostrar() {
        if (this.ventana.getClienteActual() != null) {
            this.saludo.setText("Hola, " + this.ventana.getClienteActual().getPrimerNombre() + ". Cuenta:");
        }
        this.ventana.llenarCuentas(this.listaCuentas);
    }
}
