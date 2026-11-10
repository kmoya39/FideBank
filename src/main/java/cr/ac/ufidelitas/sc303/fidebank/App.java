package cr.ac.ufidelitas.sc303.fidebank;

import cr.ac.ufidelitas.sc303.fidebank.vista.VentanaInicio;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Punto de entrada de FideBank. Construye la interfaz dentro del hilo de
 * despacho de eventos de Swing, como lo exige la librería.
 */
public final class App {

    private App() {
        // Clase de arranque, no se instancia
    }

    /**
     * @param args no se usan
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Mismo aspecto en Windows, macOS y Linux
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                    | UnsupportedLookAndFeelException e) {
                // Si no se puede cambiar el aspecto se usa el predeterminado
                System.err.println("No se pudo aplicar el aspecto visual: " + e.getMessage());
            }
            new VentanaInicio().setVisible(true);
        });
    }
}
