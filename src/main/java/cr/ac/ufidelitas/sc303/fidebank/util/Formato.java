package cr.ac.ufidelitas.sc303.fidebank.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Métodos de apoyo para mostrar montos y fechas con el formato usado en
 * Costa Rica (₡450 000,00 y 13/10/2026 09:15).
 */
public final class Formato {

    /** Formato de fecha y hora de los comprobantes. */
    public static final DateTimeFormatter FECHA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private Formato() {
        // Clase de utilidades, no se instancia
    }

    /**
     * Convierte un monto a texto con símbolo de colón, espacio como separador
     * de miles y coma decimal.
     *
     * @param monto monto a formatear
     * @return texto como ₡100 000,00
     */
    public static String colones(BigDecimal monto) {
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setGroupingSeparator(' ');
        simbolos.setDecimalSeparator(',');
        simbolos.setMinusSign('-');
        DecimalFormat formato = new DecimalFormat("#,##0.00", simbolos);
        return "₡" + formato.format(monto);
    }

    /**
     * @param fecha fecha a formatear
     * @return texto como 13/10/2026 09:15
     */
    public static String fecha(LocalDateTime fecha) {
        return fecha.format(FECHA_HORA);
    }
}
