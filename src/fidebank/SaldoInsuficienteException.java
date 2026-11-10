package fidebank;

/**
 * Excepción propia que se lanza cuando la cuenta no tiene dinero suficiente.
 */
public class SaldoInsuficienteException extends Exception {

    /**
     * @param mensaje explicación para el usuario
     */
    public SaldoInsuficienteException(String mensaje) {
        super(mensaje);
    }
}
