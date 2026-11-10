package cr.ac.ufidelitas.sc303.fidebank.dao;

import cr.ac.ufidelitas.sc303.fidebank.modelo.Banco;
import java.io.IOException;

/**
 * Contrato para guardar y cargar los datos del banco. La capa de servicio
 * solo conoce esta interfaz, así que en el avance final se puede cambiar el
 * archivo serializado por una base de datos con JDBC sin tocar el servicio.
 */
public interface BancoDao {

    /**
     * @return true si ya existen datos guardados
     */
    boolean existenDatos();

    /**
     * @return el banco con todos sus datos
     * @throws IOException si no se pueden leer los datos
     */
    Banco cargar() throws IOException;

    /**
     * @param banco datos a guardar
     * @throws IOException si no se pueden escribir los datos
     */
    void guardar(Banco banco) throws IOException;
}
