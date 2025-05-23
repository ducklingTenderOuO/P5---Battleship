/**
 * Representa un barco en el juego, con su orientación, tamaño
 * y posición en el tablero.
 *
 * @author (AYJB)
 * @version (ABR 2025)
 */

import java.io.Serializable;

public class Barco implements Serializable {
    private static final long serialVersionUID = 1L;
    private String orientacion;
    private int tamaño;
    private int capa;
    private int fila;
    private int columna;
    private String[] dibujo;

    public Barco(String orientacion, int tamaño, int capa, int fila, int columna) {
        this.orientacion = orientacion;
        this.tamaño = tamaño;
        this.capa = capa;
        this.fila = fila;
        this.columna = columna;
        this.dibujo = new String[tamaño];
        for (int i = 0; i < tamaño; i++) {
            dibujo[i] = "🚢";
        }
    }

    public void mostrarBarco() {
        System.out.print("Barco (" + tamaño + "): ");
        for (String parte : dibujo) {
            System.out.print(parte + " ");
        }
        System.out.println();
    }

    // Getteeeeeeeeeeeeeeeers
    public String getOrientacion() { return orientacion; }
    public int getTamaño() { return tamaño; }
    public int getCapa() { return capa; }
    public int getFila() { return fila; }
    public int getColumna() { return columna; }
}
