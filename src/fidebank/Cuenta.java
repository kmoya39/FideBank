package fidebank;

/**
 * Clase abstracta con lo común a toda cuenta bancaria.
 * Cada tipo de cuenta define su propia forma de retirar (polimorfismo).
 */
public abstract class Cuenta implements Mostrable {

    // Contador compartido por todas las cuentas para generar el número
    private static int consecutivo = 1000;

    private String numero;
    protected double saldo;
    private Cliente titular;

    /**
     * @param titular      dueño de la cuenta
     * @param saldoInicial depósito de apertura
     */
    public Cuenta(Cliente titular, double saldoInicial) {
        consecutivo++;
        this.numero = "FB-" + consecutivo;
        this.titular = titular;
        this.saldo = saldoInicial;
    }

    /**
     * Suma dinero a la cuenta.
     *
     * @param monto monto a depositar
     */
    public void depositar(double monto) {
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor que cero.");
        }
        this.saldo += monto;
    }

    /**
     * Cada subclase decide cuánto se puede retirar.
     *
     * @param monto monto a retirar
     * @throws SaldoInsuficienteException si no alcanza el dinero
     */
    public abstract void retirar(double monto) throws SaldoInsuficienteException;

    /**
     * @return nombre del tipo de cuenta
     */
    public abstract String getTipo();

    @Override
    public String mostrarInformacion() {
        return getTipo() + " " + numero + " | Saldo: ₡" + String.format("%,.2f", saldo);
    }

    public String getNumero() {
        return numero;
    }

    public double getSaldo() {
        return saldo;
    }

    public Cliente getTitular() {
        return titular;
    }
}
