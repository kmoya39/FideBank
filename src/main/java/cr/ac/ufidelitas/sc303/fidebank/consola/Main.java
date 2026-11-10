package cr.ac.ufidelitas.sc303.fidebank.consola;

import cr.ac.ufidelitas.sc303.fidebank.excepcion.SaldoInsuficienteException;
import cr.ac.ufidelitas.sc303.fidebank.modelo.*;

import java.io.IOException;
import java.util.Scanner;

/**
 * Programa principal: menú de sucursal (abrir cuentas) y menú de cajero.
 */
public class Main {

    private static Scanner sc = new Scanner(System.in);
    private static Banco banco = new Banco("FideBank");

    public static void main(String[] args) {
        cargarDatosDePrueba();
        int opcion;
        do {
            System.out.println("\n===== FIDEBANK =====");
            System.out.println("1. Sucursal (registrar cliente y abrir cuenta)");
            System.out.println("2. Cajero automático");
            System.out.println("3. Ver clientes y cuentas");
            System.out.println("0. Salir");
            opcion = leerEntero("Opción: ");
            switch (opcion) {
                case 1 -> menuSucursal();
                case 2 -> menuCajero();
                case 3 -> System.out.println(banco.mostrarInformacion());
                case 0 -> System.out.println("Gracias por usar FideBank.");
                default -> System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

    /**
     * Registra un cliente y le abre su primera cuenta.
     */
    private static void menuSucursal() {
        try {
            String cedula = leerTexto("Cédula: ");
            if (banco.buscarCliente(cedula) == null) {
                String nombre = leerTexto("Nombre completo: ");
                String pin = leerTexto("PIN de 4 dígitos: ");
                banco.registrarCliente(cedula, nombre, pin);
            }
            int tipo = leerEntero("Tipo de cuenta (1 = Ahorro, 2 = Corriente): ");
            double monto = leerDouble("Depósito inicial: ");
            Cuenta cuenta = banco.abrirCuenta(cedula, tipo, monto);
            System.out.println("Cuenta abierta: " + cuenta.mostrarInformacion());
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Inicia sesión y muestra las operaciones del cajero.
     */
    private static void menuCajero() {
        Cliente cliente;
        try {
            String cedula = leerTexto("Cédula: ");
            String pin = leerTexto("PIN: ");
            cliente = banco.iniciarSesion(cedula, pin);
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println(e.getMessage());
            return;
        }
        System.out.println("Bienvenido(a), " + cliente.getNombre());
        Cuenta cuenta = elegirCuenta(cliente);
        if (cuenta == null) {
            return;
        }
        int opcion;
        do {
            System.out.println("\n--- Cajero | " + cuenta.mostrarInformacion() + " ---");
            System.out.println("1. Consultar saldo  2. Retirar  3. Depositar");
            System.out.println("4. Transferir       5. Historial  0. Salir");
            opcion = leerEntero("Opción: ");
            try {
                Transaccion t = null;
                switch (opcion) {
                    case 1 -> System.out.println(cuenta.mostrarInformacion());
                    case 2 -> t = banco.retirar(cuenta, leerDouble("Monto a retirar: "));
                    case 3 -> t = banco.depositar(cuenta, leerDouble("Monto a depositar: "));
                    case 4 -> t = banco.transferir(cuenta, leerTexto("Cuenta destino (ej. FB-1002): "),
                            leerDouble("Monto a transferir: "));
                    case 5 -> System.out.println(banco.mostrarHistorial(cuenta));
                    case 0 -> System.out.println("Sesión finalizada.");
                    default -> System.out.println("Opción no válida.");
                }
                // Si hubo transacción se ofrece imprimir el comprobante
                if (t != null) {
                    System.out.println(t.mostrarInformacion());
                    if (leerTexto("¿Imprimir comprobante? (s/n): ").equalsIgnoreCase("s")) {
                        System.out.println("Comprobante guardado en " + banco.imprimirComprobante(t));
                    }
                }
            } catch (SaldoInsuficienteException | IllegalArgumentException e) {
                System.out.println("Operación rechazada: " + e.getMessage());
            } catch (IOException e) {
                System.out.println("No se pudo imprimir el comprobante.");
            }
        } while (opcion != 0);
    }

    /**
     * Muestra las cuentas del cliente y devuelve la que elija.
     */
    private static Cuenta elegirCuenta(Cliente cliente) {
        if (cliente.getCuentas().isEmpty()) {
            System.out.println("El cliente no tiene cuentas.");
            return null;
        }
        for (int i = 0; i < cliente.getCuentas().size(); i++) {
            System.out.println((i + 1) + ". " + cliente.getCuentas().get(i).mostrarInformacion());
        }
        int indice = leerEntero("Seleccione la cuenta: ") - 1;
        if (indice < 0 || indice >= cliente.getCuentas().size()) {
            System.out.println("Cuenta no válida.");
            return null;
        }
        return cliente.getCuentas().get(indice);
    }

    /**
     * Crea dos clientes de ejemplo para probar el programa.
     */
    private static void cargarDatosDePrueba() {
        banco.registrarCliente("114560789", "Ana Solís Vargas", "4826");
        banco.abrirCuenta("114560789", 1, 450000);   // FB-1001
        banco.abrirCuenta("114560789", 2, 1200000);  // FB-1002
        banco.registrarCliente("205550111", "Carlos Jiménez Rojas", "7391");
        banco.abrirCuenta("205550111", 1, 85000);    // FB-1003
    }

    // ----- Métodos de lectura con validación del tipo de dato -----

    private static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        String texto = sc.nextLine().trim();
        while (texto.isEmpty()) {
            System.out.print("El dato es obligatorio. " + mensaje);
            texto = sc.nextLine().trim();
        }
        return texto;
    }

    private static int leerEntero(String mensaje) {
        while (true) {
            try {
                return Integer.parseInt(leerTexto(mensaje));
            } catch (NumberFormatException e) {
                System.out.println("Debe digitar un número entero.");
            }
        }
    }

    private static double leerDouble(String mensaje) {
        while (true) {
            try {
                return Double.parseDouble(leerTexto(mensaje));
            } catch (NumberFormatException e) {
                System.out.println("Debe digitar un monto numérico, por ejemplo 25000.");
            }
        }
    }
}
