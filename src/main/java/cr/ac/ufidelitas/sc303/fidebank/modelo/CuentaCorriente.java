package cr.ac.ufidelitas.sc303.fidebank.modelo;

import cr.ac.ufidelitas.sc303.fidebank.excepcion.DatoInvalidoException;
import cr.ac.ufidelitas.sc303.fidebank.excepcion.SaldoInsuficienteException;
import java.math.BigDecimal;

/**
 * Cuenta corriente: permite retirar el saldo más un sobregiro autorizado.
 */
public class CuentaCorriente extends Cuenta {

    private static final long serialVersionUID = 1L;

    private BigDecimal sobregiro;

    /**
     * @param titular      dueño de la cuenta
     * @param saldoInicial depósito de apertura
     * @param sobregiro    monto extra que el banco presta
     */
    public CuentaCorriente(Cliente titular, BigDecimal saldoInicial, BigDecimal sobregiro) {
        super(titular, saldoInicial);
        this.sobregiro = sobregiro;
    }

    @Override
    public void retirar(BigDecimal monto) throws SaldoInsuficienteException, DatoInvalidoException {
        validarMontoPositivo(monto);
        // Se permite quedar en negativo hasta el límite del sobregiro
        if (monto.compareTo(this.getDisponible()) > 0) {
            throw new SaldoInsuficienteException("Saldo insuficiente. El monto supera el saldo y el sobregiro disponible.");
        }
        this.saldo = this.saldo.subtract(monto);
    }

    @Override
    public BigDecimal getDisponible() {
        return this.saldo.add(this.sobregiro);
    }

    @Override
    public TipoCuenta getTipo() {
        return TipoCuenta.CORRIENTE;
    }

    public BigDecimal getSobregiro() {
        return this.sobregiro;
    }

    public void setSobregiro(BigDecimal sobregiro) {
        this.sobregiro = sobregiro;
    }
}
