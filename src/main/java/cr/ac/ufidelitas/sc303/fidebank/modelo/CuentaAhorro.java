package cr.ac.ufidelitas.sc303.fidebank.modelo;

import cr.ac.ufidelitas.sc303.fidebank.excepcion.DatoInvalidoException;
import cr.ac.ufidelitas.sc303.fidebank.excepcion.SaldoInsuficienteException;
import java.math.BigDecimal;

/**
 * Cuenta de ahorro: solo se puede retirar el saldo que existe.
 */
public class CuentaAhorro extends Cuenta {

    private static final long serialVersionUID = 1L;

    private BigDecimal tasaInteres;

    /**
     * @param titular      dueño de la cuenta
     * @param saldoInicial depósito de apertura
     * @param tasaInteres  tasa anual en porcentaje
     */
    public CuentaAhorro(Cliente titular, BigDecimal saldoInicial, BigDecimal tasaInteres) {
        super(titular, saldoInicial);
        this.tasaInteres = tasaInteres;
    }

    @Override
    public void retirar(BigDecimal monto) throws SaldoInsuficienteException, DatoInvalidoException {
        validarMontoPositivo(monto);
        if (monto.compareTo(this.saldo) > 0) {
            throw new SaldoInsuficienteException("Saldo insuficiente.");
        }
        this.saldo = this.saldo.subtract(monto);
    }

    @Override
    public BigDecimal getDisponible() {
        return this.saldo;
    }

    @Override
    public TipoCuenta getTipo() {
        return TipoCuenta.AHORRO;
    }

    public BigDecimal getTasaInteres() {
        return this.tasaInteres;
    }

    public void setTasaInteres(BigDecimal tasaInteres) {
        this.tasaInteres = tasaInteres;
    }
}
