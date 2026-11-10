package cr.ac.ufidelitas.sc303.fidebank.vista;

import cr.ac.ufidelitas.sc303.fidebank.excepcion.FideBankException;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.text.AbstractDocument;

/**
 * HU-02: ingreso al cajero con cédula y PIN, con teclado numérico en
 * pantalla. Figura 2 del prototipo.
 */
public class PanelIngreso extends PantallaBase {

    private static final long serialVersionUID = 1L;
    private static final String NOTA_INICIAL = "Al tercer PIN incorrecto se bloquea el acceso.";

    private final transient VentanaCajero ventana;
    private final CampoConPista campoCedula;
    private final JPasswordField campoPin;
    private final JButton botonIngresar;
    private final JLabel nota;

    /**
     * @param ventana ventana del cajero
     */
    public PanelIngreso(VentanaCajero ventana) {
        super("FideBank | Cajero automático");
        this.ventana = ventana;

        this.campoCedula = new CampoConPista("1-1456-0789");
        this.campoPin = Estilos.decorarCampo(new JPasswordField());
        ((AbstractDocument) this.campoPin.getDocument()).setDocumentFilter(new FiltroDigitos(4));
        this.botonIngresar = Estilos.botonPrimario("Ingresar");
        this.nota = Estilos.nota(NOTA_INICIAL);

        this.agregar(Estilos.etiqueta("Cédula"), 0, 0, 1, 0);
        this.agregar(this.campoCedula, 0, 1, 1, 6);
        this.agregar(Estilos.etiqueta("PIN (4 dígitos)"), 0, 2, 1, 12);
        this.agregar(this.campoPin, 0, 3, 1, 6);
        this.agregar(this.crearTeclado(), 0, 4, 1, 14);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        acciones.setOpaque(false);
        this.botonIngresar.setPreferredSize(new java.awt.Dimension(200, 46));
        acciones.add(this.botonIngresar);
        this.agregar(acciones, 0, 5, 1, 14);

        JPanel filaNota = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        filaNota.setOpaque(false);
        filaNota.add(this.nota);
        this.agregar(filaNota, 0, 6, 1, 12);
        this.agregarRelleno(7);

        this.botonIngresar.addActionListener(e -> this.ingresar());
    }

    /**
     * Teclado numérico de la Figura 2: dígitos, Borrar y Limpiar.
     */
    private JPanel crearTeclado() {
        JPanel teclado = new JPanel(new GridLayout(4, 3, 14, 10));
        teclado.setOpaque(false);
        String[] teclas = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "Borrar", "0", "Limpiar"};
        for (String tecla : teclas) {
            JButton boton = Estilos.botonSecundario(tecla);
            switch (tecla) {
                case "Borrar" -> boton.addActionListener(e -> this.borrarUltimoDigito());
                case "Limpiar" -> boton.addActionListener(e -> this.campoPin.setText(""));
                default -> boton.addActionListener(e -> this.agregarDigito(tecla));
            }
            teclado.add(boton);
        }
        return teclado;
    }

    private void agregarDigito(String digito) {
        // El filtro del campo impide pasar de 4 dígitos
        this.campoPin.setText(new String(this.campoPin.getPassword()) + digito);
    }

    private void borrarUltimoDigito() {
        String pin = new String(this.campoPin.getPassword());
        if (!pin.isEmpty()) {
            this.campoPin.setText(pin.substring(0, pin.length() - 1));
        }
    }

    /**
     * Envía la cédula y el PIN al servicio en segundo plano.
     */
    private void ingresar() {
        String cedula = this.campoCedula.getText();
        String pin = new String(this.campoPin.getPassword());
        this.ejecutarEnSegundoPlano(
                () -> this.ventana.getServicio().iniciarSesion(cedula, pin),
                this.ventana::iniciarSesion,
                error -> {
                    this.campoPin.setText("");
                    if (error instanceof FideBankException) {
                        this.nota.setText(error.getMessage());
                    } else {
                        Dialogos.mostrarError(this, error);
                    }
                },
                this.botonIngresar);
    }

    @Override
    public void alMostrar() {
        this.campoCedula.setText("");
        this.campoPin.setText("");
        this.nota.setText(NOTA_INICIAL);
    }

    @Override
    public JButton getBotonPorDefecto() {
        return this.botonIngresar;
    }
}
