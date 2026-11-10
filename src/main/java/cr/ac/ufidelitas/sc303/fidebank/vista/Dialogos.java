package cr.ac.ufidelitas.sc303.fidebank.vista;

import cr.ac.ufidelitas.sc303.fidebank.excepcion.FideBankException;
import java.awt.Component;
import java.io.IOException;
import javax.swing.JOptionPane;

/**
 * Mensajes al usuario. Traduce cada tipo de excepción en un mensaje claro,
 * sin mostrar detalles técnicos.
 */
public final class Dialogos {

    private static final String TITULO = "FideBank";

    private Dialogos() {
        // Clase de utilidades, no se instancia
    }

    /**
     * Muestra el error según su tipo: las excepciones del negocio como
     * advertencia con su mensaje, y los errores de archivo o inesperados
     * como error general.
     *
     * @param padre ventana sobre la que se muestra el mensaje
     * @param error excepción ocurrida
     */
    public static void mostrarError(Component padre, Exception error) {
        if (error instanceof FideBankException) {
            JOptionPane.showMessageDialog(padre, error.getMessage(), TITULO, JOptionPane.WARNING_MESSAGE);
        } else if (error instanceof IOException) {
            JOptionPane.showMessageDialog(padre,
                    "No se pudieron leer o guardar los datos del banco.\nIntente de nuevo o avise al personal de soporte.",
                    TITULO, JOptionPane.ERROR_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(padre,
                    "Ocurrió un error inesperado. La operación no se completó.",
                    TITULO, JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * @param padre   ventana sobre la que se muestra el mensaje
     * @param mensaje texto informativo
     */
    public static void informar(Component padre, String mensaje) {
        JOptionPane.showMessageDialog(padre, mensaje, TITULO, JOptionPane.INFORMATION_MESSAGE);
    }
}
