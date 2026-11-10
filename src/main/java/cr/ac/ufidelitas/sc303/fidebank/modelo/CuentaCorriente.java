package cr.ac.ufidelitas.sc303.fidebank.modelo;

import cr.ac.ufidelitas.sc303.fidebank.excepcion.SaldoInsuficienteException;
/**
 * Cuenta corriente: permite retirar el saldo más un sobregiro autorizado.
 */
public class CuentaCorriente extends Cuenta {

    private double sobregiro;

    /**
     * @param titular      dueño de la cuenta
     * @param saldoInicial depósito de apertura
     * @param sobregiro    monto extra que el banco presta
     */
    public CuentaCorriente(Cliente titular, double saldoInicial, double sobregiro) {
        super(titular, saldoInicial);
        this.sobregiro = sobregiro;
    }

    @Override
    public void retirar(double monto) throws SaldoInsuficienteException {
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor que cero.");
        }
        // Se permite quedar en negativo hasta el límite del sobregiro
        if (monto > saldo + sobregiro) {
            throw new SaldoInsuficienteException("El monto supera el saldo y el sobregiro disponible.");
        }
        saldo -= monto;
    }

    @Override
    public String getTipo() {
        return "Corriente";
    }

    public double getSobregiro() {
        return sobregiro;
    }

    public void setSobregiro(double sobregiro) {
        this.sobregiro = sobregiro;
    }
}
