package cr.ac.ufidelitas.sc303.fidebank.vista;

import cr.ac.ufidelitas.sc303.fidebank.modelo.TipoTransaccion;
import cr.ac.ufidelitas.sc303.fidebank.modelo.Transaccion;
import cr.ac.ufidelitas.sc303.fidebank.util.Formato;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 * Modelo de la tabla del historial (HU-08). Hereda de AbstractTableModel y
 * redefine los métodos que la JTable usa para pedir los datos.
 */
public class ModeloTablaHistorial extends AbstractTableModel {

    private static final long serialVersionUID = 1L;
    private static final String[] COLUMNAS = {"#", "Fecha", "Tipo", "Detalle", "Monto"};

    private final ArrayList<Transaccion> filas = new ArrayList<>();
    private String cuentaConsultada = "";

    /**
     * @param transacciones movimientos a mostrar
     * @param numeroCuenta  cuenta consultada, para saber si una transferencia
     *                      fue enviada o recibida
     */
    public void setDatos(List<Transaccion> transacciones, String numeroCuenta) {
        this.filas.clear();
        this.filas.addAll(transacciones);
        this.cuentaConsultada = numeroCuenta;
        this.fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return this.filas.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMNAS.length;
    }

    @Override
    public String getColumnName(int columna) {
        return COLUMNAS[columna];
    }

    @Override
    public Object getValueAt(int fila, int columna) {
        Transaccion t = this.filas.get(fila);
        return switch (columna) {
            case 0 -> t.getId();
            case 1 -> Formato.fecha(t.getFecha());
            case 2 -> t.getTipo().getDescripcion();
            case 3 -> this.detalle(t);
            case 4 -> (this.esSalida(t) ? "- " : "+ ") + Formato.colones(t.getMonto());
            default -> "";
        };
    }

    /**
     * Retiros y transferencias enviadas restan; depósitos y transferencias
     * recibidas suman.
     */
    private boolean esSalida(Transaccion t) {
        return t.getTipo() == TipoTransaccion.RETIRO
                || (t.getTipo() == TipoTransaccion.TRANSFERENCIA && this.cuentaConsultada.equals(t.getCuentaOrigen()));
    }

    private String detalle(Transaccion t) {
        return switch (t.getTipo()) {
            case RETIRO -> "Retiro en cajero";
            case DEPOSITO -> "Depósito en cajero";
            case TRANSFERENCIA -> this.cuentaConsultada.equals(t.getCuentaOrigen())
                    ? "Enviada a " + t.getCuentaDestino()
                    : "Recibida de " + t.getCuentaOrigen();
        };
    }
}
