package cr.ac.ufidelitas.sc303.fidebank.vista;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import javax.swing.JTextField;

/**
 * Campo de texto que muestra un ejemplo en gris mientras está vacío, igual
 * que los textos de ejemplo de los wireframes (por ejemplo 1-1456-0789).
 * Hereda de JTextField y solo redefine cómo se pinta.
 */
public class CampoConPista extends JTextField {

    private static final long serialVersionUID = 1L;

    private final String pista;

    /**
     * @param pista texto de ejemplo que se muestra con el campo vacío
     */
    public CampoConPista(String pista) {
        this.pista = pista;
        Estilos.decorarCampo(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (this.getText().isEmpty()) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2.setColor(Estilos.COLOR_AYUDA);
            g2.setFont(this.getFont());
            Insets margen = this.getInsets();
            int y = margen.top + g2.getFontMetrics().getAscent();
            g2.drawString(this.pista, margen.left, y);
            g2.dispose();
        }
    }
}
