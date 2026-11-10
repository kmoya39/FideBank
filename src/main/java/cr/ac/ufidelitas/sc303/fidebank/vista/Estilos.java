package cr.ac.ufidelitas.sc303.fidebank.vista;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.border.Border;

/**
 * Colores, fuentes y componentes con el estilo de los wireframes del
 * Avance 1: encabezado gris, fondo blanco, botones grises con borde oscuro
 * y botón principal gris oscuro con texto blanco.
 */
public final class Estilos {

    public static final Color COLOR_FONDO = Color.WHITE;
    public static final Color COLOR_ENCABEZADO = new Color(0xCC, 0xCC, 0xCC);
    public static final Color COLOR_BORDE = new Color(0x44, 0x44, 0x44);
    public static final Color COLOR_BOTON = new Color(0xE6, 0xE6, 0xE6);
    public static final Color COLOR_PRIMARIO = new Color(0x55, 0x55, 0x55);
    public static final Color COLOR_AYUDA = new Color(0x66, 0x66, 0x66);
    public static final Color COLOR_NOTA = new Color(0xFF, 0xF6, 0xB3);
    public static final Color COLOR_BORDE_NOTA = new Color(0xD4, 0xB1, 0x06);
    public static final Color COLOR_COMPROBANTE = new Color(0xF4, 0xF4, 0xF4);

    public static final Font FUENTE_TITULO = new Font(Font.SANS_SERIF, Font.BOLD, 22);
    public static final Font FUENTE_ETIQUETA = new Font(Font.SANS_SERIF, Font.PLAIN, 15);
    public static final Font FUENTE_BOTON = new Font(Font.SANS_SERIF, Font.BOLD, 15);
    public static final Font FUENTE_AYUDA = new Font(Font.SANS_SERIF, Font.PLAIN, 13);
    public static final Font FUENTE_MONO = new Font(Font.MONOSPACED, Font.PLAIN, 15);

    private Estilos() {
        // Clase de utilidades, no se instancia
    }

    /**
     * @return borde redondeado de los campos y botones
     */
    public static Border bordeCampo() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE, 2, true),
                BorderFactory.createEmptyBorder(8, 10, 8, 10));
    }

    /**
     * @param texto texto del botón
     * @return botón gris claro (acciones secundarias)
     */
    public static JButton botonSecundario(String texto) {
        return crearBoton(texto, COLOR_BOTON, Color.BLACK);
    }

    /**
     * @param texto texto del botón
     * @return botón gris oscuro (acción principal de la pantalla)
     */
    public static JButton botonPrimario(String texto) {
        return crearBoton(texto, COLOR_PRIMARIO, Color.WHITE);
    }

    private static JButton crearBoton(String texto, Color fondo, Color letra) {
        JButton boton = new JButton(texto);
        boton.setFont(FUENTE_BOTON);
        boton.setBackground(fondo);
        boton.setForeground(letra);
        boton.setOpaque(true);
        boton.setContentAreaFilled(true);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE, 2, true),
                BorderFactory.createEmptyBorder(10, 18, 10, 18)));
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return boton;
    }

    /**
     * @param texto texto de la etiqueta
     * @return etiqueta normal sobre un campo
     */
    public static JLabel etiqueta(String texto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(FUENTE_ETIQUETA);
        return etiqueta;
    }

    /**
     * @param texto texto de ayuda
     * @return etiqueta gris pequeña con indicaciones
     */
    public static JLabel ayuda(String texto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(FUENTE_AYUDA);
        etiqueta.setForeground(COLOR_AYUDA);
        return etiqueta;
    }

    /**
     * @param texto texto de la nota
     * @return etiqueta amarilla para avisos al usuario
     */
    public static JLabel nota(String texto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(FUENTE_AYUDA);
        etiqueta.setOpaque(true);
        etiqueta.setBackground(COLOR_NOTA);
        etiqueta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE_NOTA, 1),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        return etiqueta;
    }

    /**
     * Aplica la fuente y el borde estándar a un campo.
     *
     * @param campo componente a decorar
     * @param <T>   tipo del componente
     * @return el mismo componente
     */
    public static <T extends JComponent> T decorarCampo(T campo) {
        campo.setFont(FUENTE_ETIQUETA);
        campo.setBorder(bordeCampo());
        return campo;
    }
}
