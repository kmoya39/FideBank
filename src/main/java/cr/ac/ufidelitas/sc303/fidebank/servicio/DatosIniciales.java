package cr.ac.ufidelitas.sc303.fidebank.servicio;

import cr.ac.ufidelitas.sc303.fidebank.modelo.Banco;
import cr.ac.ufidelitas.sc303.fidebank.modelo.Cliente;
import cr.ac.ufidelitas.sc303.fidebank.modelo.CuentaAhorro;
import cr.ac.ufidelitas.sc303.fidebank.modelo.CuentaCorriente;
import java.math.BigDecimal;

/**
 * Datos de prueba que se cargan la primera vez que se ejecuta el sistema,
 * cuando todavía no existe el archivo de datos. Son los mismos del Avance 1.
 */
public final class DatosIniciales {

    private DatosIniciales() {
        // Clase de utilidades, no se instancia
    }

    /**
     * @return banco con dos clientes y tres cuentas de prueba
     */
    public static Banco crear() {
        Banco banco = new Banco("FideBank");

        Cliente ana = new Cliente("114560789", "Ana Solís Vargas", "4826");
        banco.agregarCliente(ana);
        banco.agregarCuenta(new CuentaAhorro(ana, new BigDecimal("450000.00"), BancoServicio.TASA_AHORRO));         // FB-1001
        banco.agregarCuenta(new CuentaCorriente(ana, new BigDecimal("1200000.00"), BancoServicio.SOBREGIRO_CORRIENTE)); // FB-1002

        Cliente carlos = new Cliente("205550111", "Carlos Jiménez Rojas", "7391");
        banco.agregarCliente(carlos);
        banco.agregarCuenta(new CuentaAhorro(carlos, new BigDecimal("85000.00"), BancoServicio.TASA_AHORRO));      // FB-1003

        return banco;
    }
}
