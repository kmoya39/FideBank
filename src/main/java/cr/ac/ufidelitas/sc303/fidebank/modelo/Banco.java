package cr.ac.ufidelitas.sc303.fidebank.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Agrupa todos los datos del banco: clientes, cuentas y bitácora de
 * transacciones. Es el objeto que se serializa completo en el archivo de
 * datos. Las operaciones del cajero viven en la capa de servicio.
 */
public class Banco implements Mostrable, Serializable {

    private static final long serialVersionUID = 1L;

    private final String nombre;
    /** Clientes por cédula: búsqueda directa al iniciar sesión. */
    private final LinkedHashMap<String, Cliente> clientes;
    /** Cuentas por número: búsqueda directa de la cuenta destino. */
    private final LinkedHashMap<String, Cuenta> cuentas;
    /** Bitácora en orden de llegada (HU-07 y HU-08). */
    private final ArrayList<Transaccion> transacciones;

    /**
     * @param nombre nombre del banco
     */
    public Banco(String nombre) {
        this.nombre = nombre;
        this.clientes = new LinkedHashMap<>();
        this.cuentas = new LinkedHashMap<>();
        this.transacciones = new ArrayList<>();
    }

    /**
     * @param cliente cliente nuevo
     */
    public void agregarCliente(Cliente cliente) {
        this.clientes.put(cliente.getCedula(), cliente);
    }

    /**
     * Registra la cuenta en el banco y en la lista del titular.
     *
     * @param cuenta cuenta nueva
     */
    public void agregarCuenta(Cuenta cuenta) {
        this.cuentas.put(cuenta.getNumero(), cuenta);
        cuenta.getTitular().agregarCuenta(cuenta);
    }

    /**
     * @param transaccion transacción a guardar en la bitácora
     */
    public void registrarTransaccion(Transaccion transaccion) {
        this.transacciones.add(transaccion);
    }

    /**
     * @param cedula cédula normalizada
     * @return el cliente o null si no existe
     */
    public Cliente buscarCliente(String cedula) {
        return this.clientes.get(cedula);
    }

    /**
     * @param numero número de cuenta en mayúscula
     * @return la cuenta o null si no existe
     */
    public Cuenta buscarCuenta(String numero) {
        return this.cuentas.get(numero);
    }

    /**
     * Reajusta los contadores estáticos de Cuenta y Transaccion después de
     * deserializar, para que los números nuevos no se repitan.
     */
    public void restaurarConsecutivos() {
        for (String numero : this.cuentas.keySet()) {
            Cuenta.sincronizarConsecutivo(Integer.parseInt(numero.substring(3)));
        }
        for (Transaccion t : this.transacciones) {
            Transaccion.sincronizarConsecutivo(t.getId());
        }
    }

    @Override
    public String mostrarInformacion() {
        StringBuilder texto = new StringBuilder("Banco " + this.nombre + " - Clientes registrados:\n");
        for (Cliente c : this.clientes.values()) {
            texto.append(c.mostrarInformacion());
        }
        return texto.toString();
    }

    public String getNombre() {
        return this.nombre;
    }

    public Collection<Cliente> getClientes() {
        return Collections.unmodifiableCollection(this.clientes.values());
    }

    public List<Transaccion> getTransacciones() {
        return Collections.unmodifiableList(this.transacciones);
    }
}
