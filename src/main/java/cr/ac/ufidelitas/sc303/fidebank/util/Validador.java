package cr.ac.ufidelitas.sc303.fidebank.util;

import cr.ac.ufidelitas.sc303.fidebank.excepcion.DatoInvalidoException;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Valida y convierte los datos que el usuario digita en la interfaz antes
 * de que lleguen al modelo. Si un dato no es válido se lanza
 * {@link DatoInvalidoException} con un mensaje claro.
 */
public final class Validador {

    /** Cédula física costarricense: 9 dígitos (los guiones son opcionales). */
    private static final String PATRON_CEDULA = "\\d{9}";
    /** PIN del cajero: exactamente 4 dígitos. */
    private static final String PATRON_PIN = "\\d{4}";
    /** Número de cuenta FideBank, por ejemplo FB-1003. */
    private static final String PATRON_CUENTA = "FB-\\d{4,}";
    /** Monto con dígitos y hasta dos decimales separados por coma o punto. */
    private static final String PATRON_MONTO = "\\d{1,12}([.,]\\d{1,2})?";
    /** Largo mínimo del nombre completo. */
    private static final int LARGO_MINIMO_NOMBRE = 5;

    private Validador() {
        // Clase de utilidades, no se instancia
    }

    /**
     * Quita espacios y guiones y verifica que queden 9 dígitos.
     *
     * @param texto cédula digitada, con o sin guiones (1-1456-0789)
     * @return cédula normalizada (114560789)
     * @throws DatoInvalidoException si el formato no es válido
     */
    public static String cedula(String texto) throws DatoInvalidoException {
        String limpia = texto == null ? "" : texto.replace("-", "").replace(" ", "").trim();
        if (limpia.isEmpty()) {
            throw new DatoInvalidoException("Digite la cédula.");
        }
        if (!limpia.matches(PATRON_CEDULA)) {
            throw new DatoInvalidoException("La cédula debe tener 9 dígitos, por ejemplo 1-1456-0789.");
        }
        return limpia;
    }

    /**
     * @param pin PIN digitado
     * @return el mismo PIN si tiene 4 dígitos
     * @throws DatoInvalidoException si no tiene 4 dígitos
     */
    public static String pin(String pin) throws DatoInvalidoException {
        if (pin == null || !pin.matches(PATRON_PIN)) {
            throw new DatoInvalidoException("El PIN debe tener exactamente 4 dígitos.");
        }
        return pin;
    }

    /**
     * @param nombre nombre completo digitado
     * @return nombre sin espacios sobrantes
     * @throws DatoInvalidoException si está vacío, es muy corto o tiene números
     */
    public static String nombre(String nombre) throws DatoInvalidoException {
        String limpio = nombre == null ? "" : nombre.trim().replaceAll("\\s+", " ");
        if (limpio.length() < LARGO_MINIMO_NOMBRE) {
            throw new DatoInvalidoException("Digite el nombre completo del cliente.");
        }
        if (!limpio.matches("[\\p{L} .'-]+")) {
            throw new DatoInvalidoException("El nombre solo puede contener letras y espacios.");
        }
        return limpio;
    }

    /**
     * @param texto número de cuenta digitado
     * @return número en mayúscula (FB-1003)
     * @throws DatoInvalidoException si no tiene el formato FB-0000
     */
    public static String numeroCuenta(String texto) throws DatoInvalidoException {
        String limpio = texto == null ? "" : texto.trim().toUpperCase();
        if (limpio.isEmpty()) {
            throw new DatoInvalidoException("Digite la cuenta destino.");
        }
        if (!limpio.matches(PATRON_CUENTA)) {
            throw new DatoInvalidoException("La cuenta destino debe tener el formato FB-1003.");
        }
        return limpio;
    }

    /**
     * Convierte el texto digitado en un monto positivo con dos decimales.
     * Acepta separadores de miles con espacio (50 000) y decimales con coma
     * o punto (50 000,50).
     *
     * @param texto monto digitado
     * @return monto como BigDecimal con escala 2
     * @throws DatoInvalidoException si no es numérico o no es mayor que cero
     */
    public static BigDecimal monto(String texto) throws DatoInvalidoException {
        String limpio = texto == null ? "" : texto.replace(" ", "").replace(" ", "").replace("₡", "").trim();
        if (limpio.isEmpty()) {
            throw new DatoInvalidoException("Digite el monto.");
        }
        if (!limpio.matches(PATRON_MONTO)) {
            throw new DatoInvalidoException("El monto debe ser numérico, por ejemplo 50 000 o 50 000,50.");
        }
        BigDecimal monto = new BigDecimal(limpio.replace(',', '.')).setScale(2, RoundingMode.HALF_UP);
        if (monto.signum() <= 0) {
            throw new DatoInvalidoException("El monto debe ser mayor que cero.");
        }
        return monto;
    }
}
