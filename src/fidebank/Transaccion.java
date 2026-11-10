package fidebank;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Registro de una operación hecha en el cajero. Sirve para el historial
 * y para imprimir el comprobante.
 */
public class Transaccion implements Mostrable {

    private static int consecutivo = 0;

    private int id;
    private TipoTransaccion tipo;
    private double monto;
    private LocalDateTime fecha;
    private String cuentaOrigen;
    private String cuentaDestino; // Solo se usa en transferencias
    private double saldoFinal;

    /**
     * @param tipo          tipo de operación
     * @param monto         monto de la operación
     * @param cuentaOrigen  cuenta afectada
     * @param cuentaDestino cuenta que recibe (null si no es transferencia)
     * @param saldoFinal    saldo de la cuenta origen después de la operación
     */
    public Transaccion(TipoTransaccion tipo, double monto, String cuentaOrigen, String cuentaDestino,
            double saldoFinal) {
        consecutivo++;
        this.id = consecutivo;
        this.tipo = tipo;
        this.monto = monto;
        this.fecha = LocalDateTime.now();
        this.cuentaOrigen = cuentaOrigen;
        this.cuentaDestino = cuentaDestino;
        this.saldoFinal = saldoFinal;
    }

    @Override
    public String mostrarInformacion() {
        String fechaTexto = fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        String texto = "===== FIDEBANK - COMPROBANTE =====\n"
                + "Transacción #" + id + "\n"
                + "Fecha: " + fechaTexto + "\n"
                + "Tipo: " + tipo + "\n"
                + "Cuenta: " + cuentaOrigen + "\n";
        if (cuentaDestino != null) {
            texto += "Cuenta destino: " + cuentaDestino + "\n";
        }
        texto += "Monto: ₡" + String.format("%,.2f", monto) + "\n"
                + "Saldo final: ₡" + String.format("%,.2f", saldoFinal) + "\n"
                + "==================================";
        return texto;
    }

    public int getId() {
        return id;
    }

    public TipoTransaccion getTipo() {
        return tipo;
    }

    public double getMonto() {
        return monto;
    }

    public String getCuentaOrigen() {
        return cuentaOrigen;
    }

    public String getCuentaDestino() {
        return cuentaDestino;
    }
}
