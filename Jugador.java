import java.io.Serializable;

public class Jugador implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nombre;
    private Tablero tablero;

    public Jugador(String nombre, Tablero tablero) {
        this.nombre = nombre;
        this.tablero = tablero;
    }

    public String getNombre() { return nombre; }
    public Tablero getTablero() { return tablero; }
}
