package cr.ac.ufidelitas.sc303.fidebank.vista;

import cr.ac.ufidelitas.sc303.fidebank.servicio.BancoServicio;
import java.awt.Dimension;
import javax.swing.JFrame;

/**
 * Ventana de la sucursal, donde el funcionario abre las cuentas (HU-01).
 */
public class VentanaSucursal extends JFrame {

    private static final long serialVersionUID = 1L;

    private final transient BancoServicio servicio;

    /**
     * @param servicio servicio compartido del banco
     */
    public VentanaSucursal(BancoServicio servicio) {
        super("FideBank - Sucursal");
        this.servicio = servicio;
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        PanelApertura panel = new PanelApertura(this);
        this.setContentPane(panel);
        this.getRootPane().setDefaultButton(panel.getBotonPorDefecto());
        this.pack();
        this.setMinimumSize(new Dimension(700, this.getHeight()));
        this.setSize(720, this.getHeight());
        this.setLocationByPlatform(true);
    }

    public BancoServicio getServicio() {
        return this.servicio;
    }
}
