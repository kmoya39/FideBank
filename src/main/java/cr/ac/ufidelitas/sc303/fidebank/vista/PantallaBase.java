package cr.ac.ufidelitas.sc303.fidebank.vista;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Pantalla genérica con el encabezado gris y el cuerpo blanco de los
 * wireframes. Todas las pantallas del cajero y de la sucursal heredan de
 * esta clase y redefinen {@link #alMostrar()} (polimorfismo: la ventana
 * llama al mismo método sin saber qué pantalla concreta está mostrando).
 */
public abstract class PantallaBase extends JPanel {

    private static final long serialVersionUID = 1L;

    private final JLabel titulo;
    private final JPanel cuerpo;

    /**
     * @param textoTitulo texto del encabezado
     */
    protected PantallaBase(String textoTitulo) {
        super(new BorderLayout());
        this.setBackground(Estilos.COLOR_FONDO);

        this.titulo = new JLabel(textoTitulo);
        this.titulo.setFont(Estilos.FUENTE_TITULO);
        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setBackground(Estilos.COLOR_ENCABEZADO);
        encabezado.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, Estilos.COLOR_BORDE),
                BorderFactory.createEmptyBorder(16, 22, 16, 22)));
        encabezado.add(this.titulo, BorderLayout.CENTER);

        this.cuerpo = new JPanel(new GridBagLayout());
        this.cuerpo.setBackground(Estilos.COLOR_FONDO);
        this.cuerpo.setBorder(BorderFactory.createEmptyBorder(18, 32, 18, 32));

        this.add(encabezado, BorderLayout.NORTH);
        this.add(this.cuerpo, BorderLayout.CENTER);
    }

    /**
     * Se llama cada vez que la pantalla se vuelve visible, para refrescar
     * datos y limpiar campos.
     */
    public void alMostrar() {
        // Por defecto no hace nada; las subclases lo redefinen
    }

    /**
     * @return botón que se activa con la tecla Enter, o null
     */
    public JButton getBotonPorDefecto() {
        return null;
    }

    /**
     * @return panel blanco donde cada pantalla coloca sus componentes
     */
    protected JPanel getCuerpo() {
        return this.cuerpo;
    }

    /**
     * Agrega un componente al cuerpo en la fila y columna indicadas.
     *
     * @param componente componente a agregar
     * @param columna    columna en la cuadrícula
     * @param fila       fila en la cuadrícula
     * @param ancho      cantidad de columnas que ocupa
     * @param margenArriba espacio en píxeles sobre el componente
     */
    protected void agregar(JComponent componente, int columna, int fila, int ancho, int margenArriba) {
        this.agregar(componente, columna, fila, ancho, margenArriba, 0);
    }

    /**
     * Igual que el método anterior, con espacio a la derecha para separar
     * dos columnas de campos.
     *
     * @param componente     componente a agregar
     * @param columna        columna en la cuadrícula
     * @param fila           fila en la cuadrícula
     * @param ancho          cantidad de columnas que ocupa
     * @param margenArriba   espacio en píxeles sobre el componente
     * @param margenDerecho  espacio en píxeles a la derecha del componente
     */
    protected void agregar(JComponent componente, int columna, int fila, int ancho, int margenArriba,
            int margenDerecho) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = columna;
        c.gridy = fila;
        c.gridwidth = ancho;
        c.weightx = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTHWEST;
        c.insets = new Insets(margenArriba, 0, 0, margenDerecho);
        this.cuerpo.add(componente, c);
    }

    /**
     * Crea la fila de botones alineada a la derecha, con el mismo borde que
     * los campos y 12 píxeles de separación entre botones.
     *
     * @param botones botones de izquierda a derecha
     * @return panel con los botones
     */
    protected JPanel filaBotones(JButton... botones) {
        JPanel fila = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        fila.setOpaque(false);
        for (int i = 0; i < botones.length; i++) {
            if (i > 0) {
                fila.add(Box.createHorizontalStrut(12));
            }
            fila.add(botones[i]);
        }
        return fila;
    }

    /**
     * Agrega un relleno que empuja el contenido hacia arriba.
     *
     * @param fila fila donde se coloca el relleno
     */
    protected void agregarRelleno(int fila) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = fila;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.VERTICAL;
        JPanel relleno = new JPanel();
        relleno.setOpaque(false);
        this.cuerpo.add(relleno, c);
    }

    /**
     * Ejecuta una operación del servicio en segundo plano. Mientras corre se
     * desactivan los botones indicados para evitar dobles clics, que en un
     * cajero podrían repetir un retiro.
     *
     * @param operacion  trabajo a ejecutar fuera del hilo de Swing
     * @param alTerminar qué hacer con el resultado
     * @param alFallar   qué hacer si ocurre un error
     * @param botones    botones a desactivar mientras se ejecuta
     * @param <T>        tipo del resultado
     */
    protected <T> void ejecutarEnSegundoPlano(TareaSegundoPlano.Operacion<T> operacion, Consumer<T> alTerminar,
            Consumer<Exception> alFallar, JButton... botones) {
        this.activar(false, botones);
        TareaSegundoPlano<T> tarea = new TareaSegundoPlano<>(operacion,
                resultado -> {
                    this.activar(true, botones);
                    alTerminar.accept(resultado);
                },
                error -> {
                    this.activar(true, botones);
                    alFallar.accept(error);
                });
        tarea.execute();
    }

    private void activar(boolean activo, JButton... botones) {
        for (JButton boton : botones) {
            boton.setEnabled(activo);
        }
        this.setCursor(activo ? Cursor.getDefaultCursor() : Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    }
}
