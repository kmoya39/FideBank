package cr.ac.ufidelitas.sc303.fidebank.modelo;

/**
 * Tipos de transacción que se pueden hacer en el cajero.
 */
public enum TipoTransaccion {
    RETIRO("Retiro"),
    DEPOSITO("Depósito"),
    TRANSFERENCIA("Transferencia");

    private final String descripcion;

    TipoTransaccion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * @return nombre legible del tipo, por ejemplo "Depósito"
     */
    public String getDescripcion() {
        return this.descripcion;
    }
}
