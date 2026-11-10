package cr.ac.ufidelitas.sc303.fidebank.excepcion;

/**
 * Excepción base de FideBank. Todas las excepciones propias del sistema
 * heredan de esta clase, de modo que la interfaz gráfica puede capturarlas
 * con un solo bloque catch y mostrar el mensaje al usuario.
 */
public class FideBankException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Crea la excepción con un mensaje entendible para el usuario.
     *
     * @param mensaje explicación del problema
     */
    public FideBankException(String mensaje) {
        super(mensaje);
    }
}
