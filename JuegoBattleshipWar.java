import java.io.*;
import java.util.Scanner;

public class JuegoBattleshipWar {
    private static final String SAVE_FILE = "savegame.dat";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Jugador[] jugadores;

        System.out.print("¿Cargar partida previa? (s/n): ");
        if (sc.nextLine().equalsIgnoreCase("s")) {
            jugadores = cargarPartida(SAVE_FILE);
            if (jugadores != null) {
                System.out.println("✔ Partida cargada.");
            } else {
                jugadores = crearJugadores(sc);
            }
        } else {
            jugadores = crearJugadores(sc);
        }

        // Ciclo de juego
        boolean fin = false;
        int turno = 0;
        while (!fin) {
            Jugador atk = jugadores[turno % 2];
            Jugador def = jugadores[(turno + 1) % 2];
            System.out.println("\n🎯 Turno de " + atk.getNombre());
            atk.getTablero().imprimirTablero(1);

            int fila, col;
            do {
                System.out.print("Fila (0-13): ");
                fila = sc.nextInt();
                System.out.print("Columna (0-13): ");
                col = sc.nextInt();
            } while (fila < 0 || fila > 13 || col < 0 || col > 13);

            boolean ac = def.getTablero().recibirAtaque(fila, col);
            atk.getTablero().registrarAtaque(fila, col, ac);
            System.out.println(ac ? "🔥 Impacto!" : "💧 Agua...");

            if (def.getTablero().todosLosBarcosDestruidos()) {
                System.out.println("\n🏆 " + atk.getNombre() + " ha ganado!");
                fin = true;
            }
            turno++;
        }

        guardarPartida(jugadores, SAVE_FILE);
        sc.close();
    }

    private static Jugador[] crearJugadores(Scanner sc) {
        Jugador[] jugs = new Jugador[2];
        for (int i = 0; i < 2; i++) {
            System.out.print("Jugador " + (i + 1) + ", ingresa tu nombre: ");
            String nom = sc.nextLine();
            Tablero tab = new Tablero();
            System.out.println(nom + ", coloca tus barcos:");
            tab.colocarBarcos();
            jugs[i] = new Jugador(nom, tab);
        }
        return jugs;
    }

    private static void guardarPartida(Jugador[] jugs, String archivo) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(archivo))) {
            out.writeObject(jugs);
            System.out.println("✔ Partida guardada.");
        } catch (IOException e) {
            System.err.println("Error guardando: " + e.getMessage());
        }
    }

    private static Jugador[] cargarPartida(String archivo) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(archivo))) {
            return (Jugador[]) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("No fue posible cargar partida: " + e.getMessage());
            return null;
        }
    }
}