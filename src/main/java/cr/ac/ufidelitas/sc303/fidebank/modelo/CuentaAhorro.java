package cr.ac.ufidelitas.sc303.fidebank.modelo;

/**
 * Cuenta de ahorro: solo se puede retirar el saldo que existe.
 */
public class CuentaAhorro extends Cuenta {

    private double tasaInteres;

    /**
     * @param titular      dueño de la cuenta
     * @param saldoInicial depósito de apertura
     * @param tasaInteres  tasa anual en porcentaje
     */
    public CuentaAhorro(Cliente titular, double saldoInicial, double tasaInteres) {
        super(titular, saldoInicial);
        this.tasaInteres = tasaInteres;
    }

    @Override
    public void retirar(double monto) throws SaldoInsuficienteException {
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor que cero.");
        }
        if (monto > saldo) {
            throw new SaldoInsuficienteException("Saldo insuficiente en la cuenta de ahorro.");
        }
        saldo -= monto;
    }

    @Override
    public String getTipo() {
        return "Ahorro";
    }

    public double getTasaInteres() {
        return tasaInteres;
    }

    public void setTasaInteres(double tasaInteres) {
        this.tasaInteres = tasaInteres;
    }
}
