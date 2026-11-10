package cr.ac.ufidelitas.sc303.fidebank.modelo;

import cr.ac.ufidelitas.sc303.fidebank.excepcion.DatoInvalidoException;
import cr.ac.ufidelitas.sc303.fidebank.excepcion.SaldoInsuficienteException;
import cr.ac.ufidelitas.sc303.fidebank.util.Formato;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Clase abstracta con lo común a toda cuenta bancaria. Cada tipo de cuenta
 * define su propia forma de retirar y su monto disponible (polimorfismo).
 */
public abstract class Cuenta implements Mostrable, Serializable {

    private static final long serialVersionUID = 1L;

    /** Contador compartido por todas las cuentas para generar el número. */
    private static int consecutivo = 1000;

    private final String numero;
    protected BigDecimal saldo;
    private final Cliente titular;

    /**
     * @param titular      dueño de la cuenta
     * @param saldoInicial depósito de apertura
     */
    protected Cuenta(Cliente titular, BigDecimal saldoInicial) {
        consecutivo++;
        this.numero = "FB-" + consecutivo;
        this.titular = titular;
        this.saldo = saldoInicial;
    }

    /**
     * Suma dinero a la cuenta (HU-05).
     *
     * @param monto monto a depositar
     * @throws DatoInvalidoException si el monto no es mayor que cero
     */
    public void depositar(BigDecimal monto) throws DatoInvalidoException {
        validarMontoPositivo(monto);
        this.saldo = this.saldo.add(monto);
    }

    /**
     * Cada subclase decide cuánto se puede retirar (HU-04).
     *
     * @param monto monto a retirar
     * @throws SaldoInsuficienteException si no alcanza el dinero
     * @throws DatoInvalidoException      si el monto no es mayor que cero
     */
    public abstract void retirar(BigDecimal monto) throws SaldoInsuficienteException, DatoInvalidoException;

    /**
     * @return dinero que el cliente puede usar en este momento
     */
    public abstract BigDecimal getDisponible();

    /**
     * @return tipo de la cuenta
     */
    public abstract TipoCuenta getTipo();

    /**
     * Verifica que el monto sea mayor que cero. Lo usan las subclases.
     *
     * @param monto monto a validar
     * @throws DatoInvalidoException si es nulo, cero o negativo
     */
    protected void validarMontoPositivo(BigDecimal monto) throws DatoInvalidoException {
        if (monto == null || monto.signum() <= 0) {
            throw new DatoInvalidoException("El monto debe ser mayor que cero.");
        }
    }

    /**
     * Ajusta el contador estático después de cargar los datos guardados,
     * porque los atributos static no se serializan.
     *
     * @param ultimoNumero mayor número de cuenta existente (por ejemplo 1003)
     */
    public static void sincronizarConsecutivo(int ultimoNumero) {
        consecutivo = Math.max(consecutivo, ultimoNumero);
    }

    @Override
    public String mostrarInformacion() {
        return this.getTipo().getDescripcion() + " " + this.numero + " | Saldo: " + Formato.colones(this.saldo);
    }

    /**
     * Texto que se muestra en las listas desplegables, por ejemplo
     * "Ahorro FB-1001".
     */
    @Override
    public String toString() {
        return this.getTipo().getDescripcion() + " " + this.numero;
    }

    public String getNumero() {
        return this.numero;
    }

    public BigDecimal getSaldo() {
        return this.saldo;
    }

    public Cliente getTitular() {
        return this.titular;
    }
}
