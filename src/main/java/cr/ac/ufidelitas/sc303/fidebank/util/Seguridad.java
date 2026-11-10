package cr.ac.ufidelitas.sc303.fidebank.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Protección del PIN. En lugar de guardar el PIN en texto plano (como en el
 * Avance 1) se guarda un hash PBKDF2 con una sal aleatoria por cliente. Así,
 * si alguien obtiene el archivo de datos, no puede leer los PIN.
 */
public final class Seguridad {

    private static final String ALGORITMO = "PBKDF2WithHmacSHA256";
    private static final int ITERACIONES = 100_000;
    private static final int LARGO_LLAVE_BITS = 256;
    private static final int LARGO_SAL_BYTES = 16;
    private static final SecureRandom ALEATORIO = new SecureRandom();

    private Seguridad() {
        // Clase de utilidades, no se instancia
    }

    /**
     * @return sal aleatoria codificada en Base64
     */
    public static String generarSal() {
        byte[] sal = new byte[LARGO_SAL_BYTES];
        ALEATORIO.nextBytes(sal);
        return Base64.getEncoder().encodeToString(sal);
    }

    /**
     * Calcula el hash del PIN con la sal indicada.
     *
     * @param pin PIN en texto plano
     * @param sal sal en Base64
     * @return hash en Base64
     */
    public static String hashPin(String pin, String sal) {
        PBEKeySpec especificacion = new PBEKeySpec(pin.toCharArray(), Base64.getDecoder().decode(sal),
                ITERACIONES, LARGO_LLAVE_BITS);
        try {
            SecretKeyFactory fabrica = SecretKeyFactory.getInstance(ALGORITMO);
            return Base64.getEncoder().encodeToString(fabrica.generateSecret(especificacion).getEncoded());
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            // PBKDF2WithHmacSHA256 viene incluido en todo JDK desde la versión 8
            throw new IllegalStateException("El algoritmo de seguridad no está disponible.", e);
        } finally {
            especificacion.clearPassword();
        }
    }

    /**
     * Compara en tiempo constante el PIN digitado contra el hash guardado,
     * para no dar pistas por la duración de la comparación.
     *
     * @param pinDigitado PIN ingresado en el cajero
     * @param sal         sal del cliente
     * @param hashGuardado hash almacenado del cliente
     * @return true si coinciden
     */
    public static boolean verificarPin(String pinDigitado, String sal, String hashGuardado) {
        byte[] calculado = Base64.getDecoder().decode(hashPin(pinDigitado, sal));
        byte[] guardado = Base64.getDecoder().decode(hashGuardado);
        return MessageDigest.isEqual(calculado, guardado);
    }
}
