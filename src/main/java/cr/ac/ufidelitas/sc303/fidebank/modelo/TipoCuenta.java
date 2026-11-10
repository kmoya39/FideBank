package cr.ac.ufidelitas.sc303.fidebank.modelo;

/**
 * Tipos de cuenta que se pueden abrir en la sucursal (HU-01). Reemplaza el
 * número 1 o 2 que usaba la versión de consola del Avance 1.
 */
public enum TipoCuenta {
    AHORRO("Ahorro"),
    CORRIENTE("Corriente");

    private final String descripcion;

    TipoCuenta(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * @return nombre legible del tipo de cuenta
     */
    public String getDescripcion() {
        return this.descripcion;
    }
}
