package cr.ac.ufidelitas.sc303.fidebank.vista;

import cr.ac.ufidelitas.sc303.fidebank.dao.ArchivoBancoDao;
import cr.ac.ufidelitas.sc303.fidebank.servicio.BancoServicio;
import java.awt.GridLayout;
import java.nio.file.Path;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Ventana de arranque. Carga los datos del banco en segundo plano y permite
 * abrir el cajero automático o la sucursal. Se pueden abrir varios cajeros
 * a la vez; todos comparten el mismo servicio.
 */
public class VentanaInicio extends JFrame {

    private static final long serialVersionUID = 1L;

    /** Archivo donde se serializan los datos del banco. */
    private static final Path ARCHIVO_DATOS = Path.of("datos", "fidebank.dat");

    private transient BancoServicio servicio;
    private final JButton botonCajero;
    private final JButton botonSucursal;
    private final JLabel estado;

    /**
     * Construye la ventana y empieza a cargar los datos.
     */
    public VentanaInicio() {
        super("FideBank");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        PantallaInicio pantalla = new PantallaInicio();
        this.botonCajero = Estilos.botonPrimario("Abrir cajero automático");
        this.botonSucursal = Estilos.botonSecundario("Abrir sucursal");
        this.estado = Estilos.ayuda("Cargando datos del banco...");
        pantalla.armar(this.botonCajero, this.botonSucursal, this.estado);
        this.setContentPane(pantalla);

        this.botonCajero.addActionListener(e -> new VentanaCajero(this.servicio).setVisible(true));
        this.botonSucursal.addActionListener(e -> new VentanaSucursal(this.servicio).setVisible(true));

        this.setSize(560, 380);
        this.setLocationRelativeTo(null);
        this.cargarDatos();
    }

    /**
     * Lee el archivo de datos sin bloquear la interfaz.
     */
    private void cargarDatos() {
        this.botonCajero.setEnabled(false);
        this.botonSucursal.setEnabled(false);
        new TareaSegundoPlano<>(
                () -> new BancoServicio(new ArchivoBancoDao(ARCHIVO_DATOS)),
                servicioCargado -> {
                    this.servicio = servicioCargado;
                    this.botonCajero.setEnabled(true);
                    this.botonSucursal.setEnabled(true);
                    this.estado.setText("Datos cargados. Puede abrir varios cajeros a la vez.");
                },
                error -> {
                    this.estado.setText("No se pudieron cargar los datos del banco.");
                    Dialogos.mostrarError(this, error);
                }).execute();
    }

    /**
     * Pantalla de bienvenida con el mismo estilo de las demás.
     */
    private static class PantallaInicio extends PantallaBase {

        private static final long serialVersionUID = 1L;

        PantallaInicio() {
            super("FideBank | Sistema de cajeros");
        }

        void armar(JButton cajero, JButton sucursal, JLabel estado) {
            this.agregar(Estilos.etiqueta("Seleccione el módulo que desea abrir:"), 0, 0, 1, 0);
            JPanel botones = new JPanel(new GridLayout(1, 2, 16, 0));
            botones.setOpaque(false);
            botones.add(cajero);
            botones.add(sucursal);
            this.agregar(botones, 0, 1, 1, 16);
            this.agregar(estado, 0, 2, 1, 18);
            this.agregarRelleno(3);
        }
    }
}
