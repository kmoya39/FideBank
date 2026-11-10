package cr.ac.ufidelitas.sc303.fidebank.excepcion;

/**
 * Se lanza cuando falla el ingreso al cajero (HU-02). Guarda los intentos
 * restantes y si el acceso quedó bloqueado, para que la vista informe al
 * cliente sin tener que consultar de nuevo al servicio.
 */
public class AutenticacionException extends FideBankException {

    private static final long serialVersionUID = 1L;

    private final int intentosRestantes;
    private final boolean bloqueado;

    /**
     * @param mensaje           explicación para el usuario
     * @param intentosRestantes intentos que le quedan al cliente (-1 si no aplica)
     * @param bloqueado         true si el acceso está bloqueado
     */
    public AutenticacionException(String mensaje, int intentosRestantes, boolean bloqueado) {
        super(mensaje);
        this.intentosRestantes = intentosRestantes;
        this.bloqueado = bloqueado;
    }

    /**
     * @return intentos que le quedan al cliente, o -1 si no aplica
     */
    public int getIntentosRestantes() {
        return this.intentosRestantes;
    }

    /**
     * @return true si el cliente quedó bloqueado
     */
    public boolean isBloqueado() {
        return this.bloqueado;
    }
}
