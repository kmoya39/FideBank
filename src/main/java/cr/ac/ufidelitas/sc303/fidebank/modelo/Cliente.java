package cr.ac.ufidelitas.sc303.fidebank.modelo;

import cr.ac.ufidelitas.sc303.fidebank.util.Seguridad;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Cliente de FideBank. Ingresa al cajero con su cédula y su PIN. El PIN no
 * se guarda en texto plano, solo su hash con una sal propia.
 */
public class Cliente implements Mostrable, Serializable {

    private static final long serialVersionUID = 1L;

    /** Intentos permitidos antes de bloquear el acceso (HU-02). */
    public static final int MAX_INTENTOS = 3;

    private String cedula;
    private String nombre;
    private String salPin;
    private String hashPin;
    private int intentosFallidos;
    private boolean bloqueado;
    private final ArrayList<Cuenta> cuentas; // Un cliente puede tener varias cuentas

    /**
     * Crea un cliente sin cuentas. El PIN recibido se convierte en hash.
     *
     * @param cedula cédula del cliente (9 dígitos)
     * @param nombre nombre completo
     * @param pin    PIN de 4 dígitos en texto plano
     */
    public Cliente(String cedula, String nombre, String pin) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.intentosFallidos = 0;
        this.bloqueado = false;
        this.cuentas = new ArrayList<>();
        this.setPin(pin);
    }

    /**
     * Compara el PIN digitado contra el hash guardado. Al acertar se
     * reinicia el contador; al tercer error el cliente queda bloqueado.
     *
     * @param pinDigitado PIN ingresado en el cajero
     * @return true si el PIN es correcto
     */
    public boolean validarPin(String pinDigitado) {
        if (this.bloqueado) {
            return false;
        }
        if (Seguridad.verificarPin(pinDigitado, this.salPin, this.hashPin)) {
            this.intentosFallidos = 0;
            return true;
        }
        this.intentosFallidos++;
        if (this.intentosFallidos >= MAX_INTENTOS) {
            this.bloqueado = true;
        }
        return false;
    }

    /**
     * @return intentos que le quedan antes del bloqueo
     */
    public int getIntentosRestantes() {
        return Math.max(0, MAX_INTENTOS - this.intentosFallidos);
    }

    /**
     * @param cuenta cuenta nueva del cliente
     */
    public void agregarCuenta(Cuenta cuenta) {
        this.cuentas.add(cuenta);
    }

    /**
     * @return primer nombre, usado en el saludo del menú principal
     */
    public String getPrimerNombre() {
        return this.nombre.split(" ")[0];
    }

    @Override
    public String mostrarInformacion() {
        StringBuilder texto = new StringBuilder("Cliente: " + this.nombre + " (" + this.cedula + ")\n");
        for (Cuenta c : this.cuentas) {
            texto.append("  ").append(c.mostrarInformacion()).append("\n");
        }
        return texto.toString();
    }

    /**
     * Cambia el PIN generando una sal nueva y su hash.
     *
     * @param pin PIN nuevo en texto plano
     */
    public final void setPin(String pin) {
        this.salPin = Seguridad.generarSal();
        this.hashPin = Seguridad.hashPin(pin, this.salPin);
    }

    public String getCedula() {
        return this.cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getIntentosFallidos() {
        return this.intentosFallidos;
    }

    public boolean isBloqueado() {
        return this.bloqueado;
    }

    /**
     * @return lista de cuentas de solo lectura, para que nadie la modifique
     *         sin pasar por {@link #agregarCuenta(Cuenta)}
     */
    public List<Cuenta> getCuentas() {
        return Collections.unmodifiableList(this.cuentas);
    }
}
