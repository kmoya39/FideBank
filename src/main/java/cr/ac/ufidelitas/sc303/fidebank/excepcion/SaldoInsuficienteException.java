package cr.ac.ufidelitas.sc303.fidebank.excepcion;

/**
 * Se lanza cuando una cuenta no tiene dinero suficiente para un retiro o
 * una transferencia (HU-04 y HU-06).
 */
public class SaldoInsuficienteException extends FideBankException {

    private static final long serialVersionUID = 1L;

    /**
     * @param mensaje explicación para el usuario
     */
    public SaldoInsuficienteException(String mensaje) {
        super(mensaje);
    }
}
