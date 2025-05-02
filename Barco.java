public class Barco {
    private String orientacion;
    private int tamaño;
    private int capa;
    private int fila;
    private int columna;

    public Barco(String orientacion, int tamaño, int capa, int fila, int columna) {
        String[] barco = new String[🚢][tamaño];
        this.orientacion = "";
        this.tamaño = tamaño;
        this.capa = capa;
        this.fila = fila;
        this.columna = columna;
    }

    public String getOrientacion() {return orientacion;}
    public void setOrientacion(String orientacion) {this.orientacion = orientacion;}

    public int getCapa() {return capa;}
    public void setCapa(int capa) {this.capa = capa;}

    public int getFila() {return fila;}
    public void setFila(int fila) {this.fila = fila;}

    public int getColumna() {return columna;}
    public void setColumna(int columna) {this.columna = columna;}

    public boolean sePuedeColocarBarco(int capaDestino, int filaDestino, int columnaDestino) {
        
    }

}