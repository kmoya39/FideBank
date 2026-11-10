package cr.ac.ufidelitas.sc303.fidebank.vista;

import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import javax.swing.SwingWorker;

/**
 * Ejecuta una operación del servicio en un hilo de fondo para no congelar
 * la interfaz (el cálculo del hash del PIN y el guardado del archivo tardan
 * un poco). Al terminar, el resultado o el error se entregan en el hilo de
 * despacho de eventos de Swing, donde sí se puede tocar la interfaz.
 *
 * @param <T> tipo del resultado de la operación
 */
public class TareaSegundoPlano<T> extends SwingWorker<T, Void> {

    /**
     * Operación que puede lanzar excepciones verificadas.
     *
     * @param <R> tipo del resultado
     */
    @FunctionalInterface
    public interface Operacion<R> {
        /**
         * @return resultado de la operación
         * @throws Exception cualquier error de negocio o de archivo
         */
        R ejecutar() throws Exception;
    }

    private final Operacion<T> operacion;
    private final Consumer<T> alTerminar;
    private final Consumer<Exception> alFallar;

    /**
     * @param operacion  trabajo que se hace en el hilo de fondo
     * @param alTerminar qué hacer con el resultado (en el hilo de Swing)
     * @param alFallar   qué hacer con el error (en el hilo de Swing)
     */
    public TareaSegundoPlano(Operacion<T> operacion, Consumer<T> alTerminar, Consumer<Exception> alFallar) {
        this.operacion = operacion;
        this.alTerminar = alTerminar;
        this.alFallar = alFallar;
    }

    @Override
    protected T doInBackground() throws Exception {
        return this.operacion.ejecutar();
    }

    @Override
    protected void done() {
        try {
            this.alTerminar.accept(this.get());
        } catch (ExecutionException e) {
            Throwable causa = e.getCause();
            this.alFallar.accept(causa instanceof Exception ex ? ex : new Exception(causa));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            this.alFallar.accept(e);
        }
    }
}
