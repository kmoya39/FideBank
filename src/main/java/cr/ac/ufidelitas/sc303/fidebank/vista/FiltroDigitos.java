package cr.ac.ufidelitas.sc303.fidebank.vista;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/**
 * Filtro que solo deja escribir dígitos y hasta un largo máximo. Se usa en
 * los campos del PIN para que no se puedan digitar letras ni más de 4
 * números.
 */
public class FiltroDigitos extends DocumentFilter {

    private final int largoMaximo;

    /**
     * @param largoMaximo cantidad máxima de dígitos
     */
    public FiltroDigitos(int largoMaximo) {
        this.largoMaximo = largoMaximo;
    }

    @Override
    public void insertString(FilterBypass fb, int offset, String texto, AttributeSet attr)
            throws BadLocationException {
        this.replace(fb, offset, 0, texto, attr);
    }

    @Override
    public void replace(FilterBypass fb, int offset, int largo, String texto, AttributeSet attrs)
            throws BadLocationException {
        String nuevo = texto == null ? "" : texto;
        int largoFinal = fb.getDocument().getLength() - largo + nuevo.length();
        if (nuevo.matches("\\d*") && largoFinal <= this.largoMaximo) {
            super.replace(fb, offset, largo, nuevo, attrs);
        }
    }
}
