package fidebank;

import java.util.ArrayList;

/**
 * Cliente de FideBank. Ingresa al cajero con su cédula y su PIN.
 */
public class Cliente implements Mostrable {

    // Intentos permitidos antes de bloquear el acceso
    public static final int MAX_INTENTOS = 3;

    private String cedula;
    private String nombre;
    private String pin;
    private int intentosFallidos;
    private boolean bloqueado;
    private ArrayList<Cuenta> cuentas; // Un cliente puede tener varias cuentas

    /**
     * Crea un cliente sin cuentas.
     *
     * @param cedula cédula del cliente
     * @param nombre nombre completo
     * @param pin    PIN de 4 dígitos
     */
    public Cliente(String cedula, String nombre, String pin) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.pin = pin;
        this.intentosFallidos = 0;
        this.bloqueado = false;
        this.cuentas = new ArrayList<>();
    }

    /**
     * Compara el PIN digitado. Al tercer error el cliente queda bloqueado.
     *
     * @param pinDigitado PIN ingresado en el cajero
     * @return true si el PIN es correcto
     */
    public boolean validarPin(String pinDigitado) {
        if (this.bloqueado) {
            return false;
        }
        if (this.pin.equals(pinDigitado)) {
            this.intentosFallidos = 0; // Se reinicia el contador al acertar
            return true;
        }
        this.intentosFallidos++;
        if (this.intentosFallidos >= MAX_INTENTOS) {
            this.bloqueado = true;
        }
        return false;
    }

    /**
     * @param cuenta cuenta nueva del cliente
     */
    public void agregarCuenta(Cuenta cuenta) {
        this.cuentas.add(cuenta);
    }

    @Override
    public String mostrarInformacion() {
        String texto = "Cliente: " + nombre + " (" + cedula + ")\n";
        for (Cuenta c : cuentas) {
            texto += "  " + c.mostrarInformacion() + "\n";
        }
        return texto;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public int getIntentosFallidos() {
        return intentosFallidos;
    }

    public boolean isBloqueado() {
        return bloqueado;
    }

    public ArrayList<Cuenta> getCuentas() {
        return cuentas;
    }
}
