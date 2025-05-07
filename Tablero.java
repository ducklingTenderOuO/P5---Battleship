import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Tablero implements Serializable {
    private static final long serialVersionUID = 1L;
    private int[][][] tablero;       // [0]=barcos, [1]=ataques
    private ArrayList<Barco> barcos; // gestion de barcos

    public Tablero() {
        this.tablero = new int[2][14][14];
        // Inicializar todo en 0 usando Arrays.fill
        for (int capa = 0; capa < 2; capa++) {
            for (int f = 0; f < 14; f++) {
                Arrays.fill(tablero[capa][f], 0);
            }
        }
        this.barcos = new ArrayList<>();
    }

    public void colocarBarcos() {
        Scanner sc = new Scanner(System.in);
        int[] tamaños = {3, 4, 5}; // solo 3 barcos
        for (int i = 0; i < tamaños.length; i++) {
            boolean colocado = false;
            while (!colocado) {
                System.out.println("\nBarco " + (i+1) + " (tamaño " + tamaños[i] + ")");
                Barco preview = new Barco("", tamaños[i], 0, 0, 0);
                preview.mostrarBarco();
                System.out.print("Fila (0-13): ");
                int fila = sc.nextInt();
                System.out.print("Columna (0-13): ");
                int columna = sc.nextInt();
                sc.nextLine(); // limpia
                System.out.print("Orientación (horizontal/vertical): ");
                String orientacion = sc.nextLine();

                Barco barco = new Barco(orientacion, tamaños[i], 0, fila, columna);
                if (colocarBarco(orientacion, tamaños[i], 0, fila, columna)) {
                    barcos.add(barco);
                    colocado = true;
                }
            }
        }
    }

    public boolean colocarBarco(String orientacion, int tamaño, int capa, int fila, int columna) {
        boolean libre = true;
        if (orientacion.equalsIgnoreCase("horizontal")) {
            if (columna + tamaño > 14) {
                System.out.println("❌ No cabe horizontalmente.");
                return false;
            }
            for (int i=0; i<tamaño; i++) {
                if (tablero[capa][fila][columna+i] != 0) { libre = false; break; }
            }
            if (!libre) { System.out.println("❌ Espacio ocupado."); return false; }
            for (int i=0; i<tamaño; i++) {
                tablero[capa][fila][columna+i] = 5;
            }
            System.out.println("✅ Barco colocado horizontalmente.");
        } else if (orientacion.equalsIgnoreCase("vertical")) {
            if (fila + tamaño > 14) {
                System.out.println("❌ No cabe verticalmente.");
                return false;
            }
            for (int i=0; i<tamaño; i++) {
                if (tablero[capa][fila+i][columna] != 0) { libre = false; break; }
            }
            if (!libre) { System.out.println("❌ Espacio ocupado."); return false; }
            for (int i=0; i<tamaño; i++) {
                tablero[capa][fila+i][columna] = 5;
            }
            System.out.println("✅ Barco colocado verticalmente.");
        } else {
            System.out.println("❌ Orientación no válida.");
            return false;
        }
        imprimirTablero(capa);
        return true;
    }

    public void imprimirTablero(int capa) {
        System.out.println("\n🧭 TABLERO - " + (capa==0?"Barcos":"Ataques"));
        System.out.print("   "); for (int j=0; j<14; j++) System.out.printf("%2d ", j);
        System.out.println();
        for (int i=0; i<14; i++) {
            System.out.printf(i<10?" %d ":"%d ", i);
            for (int j=0; j<14; j++) {
                int v = tablero[capa][i][j];
                char c = ' ';
                switch(v) {
                    case 5: c='🚢'; break;
                    case 8: c='🔥'; break;
                    case 9: c='O'; break;
                    default: c='🌊';
                }
                System.out.print(c+" ");
            }
            System.out.println();
        }
    }

    public boolean recibirAtaque(int fila, int columna) {
        if (tablero[0][fila][columna] == 5) {
            tablero[0][fila][columna] = 8;
            return true;
        }
        return false;
    }

    public void registrarAtaque(int fila, int columna, boolean acierto) {
        tablero[1][fila][columna] = acierto ? 8 : 9;
    }

    public boolean todosLosBarcosDestruidos() {
        for (int i=0; i<14; i++) {
            for (int j=0; j<14; j++) {
                if (tablero[0][i][j] == 5) return false;
            }
        }
        return true;
    }
}
