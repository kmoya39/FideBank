package cr.ac.ufidelitas.sc303.fidebank.dao;

import cr.ac.ufidelitas.sc303.fidebank.modelo.Banco;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputFilter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * Guarda el banco completo en un archivo binario por medio de serialización
 * de objetos.
 */
public class ArchivoBancoDao implements BancoDao {

    /**
     * Filtro de deserialización: solo se aceptan clases del proyecto y de
     * java.base. Así se evita que un archivo alterado cree objetos de otras
     * clases al leerse, un ataque conocido contra la serialización de Java.
     */
    private static final String FILTRO_CLASES = "cr.ac.ufidelitas.sc303.fidebank.**;java.base/*;!*";

    private final Path archivo;

    /**
     * @param archivo ruta del archivo de datos, por ejemplo datos/fidebank.dat
     */
    public ArchivoBancoDao(Path archivo) {
        this.archivo = archivo;
    }

    @Override
    public boolean existenDatos() {
        return Files.exists(this.archivo);
    }

    @Override
    public Banco cargar() throws IOException {
        try (ObjectInputStream entrada = new ObjectInputStream(
                new BufferedInputStream(Files.newInputStream(this.archivo)))) {
            entrada.setObjectInputFilter(ObjectInputFilter.Config.createFilter(FILTRO_CLASES));
            Banco banco = (Banco) entrada.readObject();
            banco.restaurarConsecutivos();
            return banco;
        } catch (ClassNotFoundException | ClassCastException | InvalidClassException e) {
            throw new IOException("El archivo de datos no es válido o fue modificado.", e);
        }
    }

    /**
     * Escribe primero en un archivo temporal y luego lo reemplaza, para que
     * un fallo a medio guardar no deje el archivo de datos dañado.
     */
    @Override
    public void guardar(Banco banco) throws IOException {
        Path carpeta = this.archivo.toAbsolutePath().getParent();
        if (carpeta != null) {
            Files.createDirectories(carpeta);
        }
        Path temporal = this.archivo.resolveSibling(this.archivo.getFileName() + ".tmp");
        try (ObjectOutputStream salida = new ObjectOutputStream(
                new BufferedOutputStream(Files.newOutputStream(temporal)))) {
            salida.writeObject(banco);
        }
        Files.move(temporal, this.archivo, StandardCopyOption.REPLACE_EXISTING);
    }
}
