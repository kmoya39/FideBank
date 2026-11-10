package cr.ac.ufidelitas.sc303.fidebank.excepcion;

/**
 * Se lanza cuando un dato digitado por el usuario no cumple el formato o
 * las reglas del negocio (cédula, PIN, montos, número de cuenta).
 */
public class DatoInvalidoException extends FideBankException {

    private static final long serialVersionUID = 1L;

    /**
     * @param mensaje explicación para el usuario
     */
    public DatoInvalidoException(String mensaje) {
        super(mensaje);
    }
}
