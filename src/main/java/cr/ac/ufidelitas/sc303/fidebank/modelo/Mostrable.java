package cr.ac.ufidelitas.sc303.fidebank.modelo;

/**
 * Interfaz para las clases que pueden presentar su información en texto.
 * La implementan Cliente, Cuenta, Transaccion y Banco, y cada una decide
 * cómo mostrarse (polimorfismo por interfaz).
 */
public interface Mostrable {

    /**
     * @return información del objeto lista para mostrar o imprimir
     */
    String mostrarInformacion();
}
