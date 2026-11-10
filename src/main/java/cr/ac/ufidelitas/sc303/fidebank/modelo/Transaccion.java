package cr.ac.ufidelitas.sc303.fidebank.modelo;

import cr.ac.ufidelitas.sc303.fidebank.util.Formato;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Registro de una operación hecha en el cajero. Sirve para el historial
 * (HU-08) y para el comprobante (HU-07). Implementa Comparable para que el
 * historial se pueda ordenar por número de transacción.
 */
public class Transaccion implements Mostrable, Serializable, Comparable<Transaccion> {

    private static final long serialVersionUID = 1L;

    private static int consecutivo = 0;

    private final int id;
    private final TipoTransaccion tipo;
    private final BigDecimal monto;
    private final LocalDateTime fecha;
    private final String cuentaOrigen;
    private final String cuentaDestino; // Solo se usa en transferencias
    private final BigDecimal saldoFinal;

    /**
     * @param tipo          tipo de operación
     * @param monto         monto de la operación
     * @param cuentaOrigen  cuenta afectada
     * @param cuentaDestino cuenta que recibe (null si no es transferencia)
     * @param saldoFinal    saldo de la cuenta origen después de la operación
     */
    public Transaccion(TipoTransaccion tipo, BigDecimal monto, String cuentaOrigen, String cuentaDestino,
            BigDecimal saldoFinal) {
        consecutivo++;
        this.id = consecutivo;
        this.tipo = tipo;
        this.monto = monto;
        this.fecha = LocalDateTime.now();
        this.cuentaOrigen = cuentaOrigen;
        this.cuentaDestino = cuentaDestino;
        this.saldoFinal = saldoFinal;
    }

    /**
     * Ajusta el contador estático después de cargar los datos guardados.
     *
     * @param ultimoId mayor número de transacción existente
     */
    public static void sincronizarConsecutivo(int ultimoId) {
        consecutivo = Math.max(consecutivo, ultimoId);
    }

    /**
     * @param numeroCuenta cuenta consultada
     * @return true si la transacción afecta esa cuenta como origen o destino
     */
    public boolean involucraCuenta(String numeroCuenta) {
        return numeroCuenta.equals(this.cuentaOrigen) || numeroCuenta.equals(this.cuentaDestino);
    }

    /**
     * Genera el texto del comprobante con el formato del prototipo (HU-07).
     */
    @Override
    public String mostrarInformacion() {
        StringBuilder texto = new StringBuilder();
        texto.append("===== FIDEBANK - COMPROBANTE =====\n");
        texto.append("Transacción #").append(this.id).append("\n");
        texto.append("Fecha: ").append(Formato.fecha(this.fecha)).append("\n");
        texto.append("Tipo: ").append(this.tipo).append("\n");
        texto.append("Cuenta: ").append(this.cuentaOrigen).append("\n");
        if (this.cuentaDestino != null) {
            texto.append("Cuenta destino: ").append(this.cuentaDestino).append("\n");
        }
        texto.append("Monto: ").append(Formato.colones(this.monto)).append("\n");
        texto.append("Saldo final: ").append(Formato.colones(this.saldoFinal)).append("\n");
        texto.append("==================================");
        return texto.toString();
    }

    @Override
    public int compareTo(Transaccion otra) {
        return Integer.compare(this.id, otra.id);
    }

    public int getId() {
        return this.id;
    }

    public TipoTransaccion getTipo() {
        return this.tipo;
    }

    public BigDecimal getMonto() {
        return this.monto;
    }

    public LocalDateTime getFecha() {
        return this.fecha;
    }

    public String getCuentaOrigen() {
        return this.cuentaOrigen;
    }

    public String getCuentaDestino() {
        return this.cuentaDestino;
    }

    public BigDecimal getSaldoFinal() {
        return this.saldoFinal;
    }
}
