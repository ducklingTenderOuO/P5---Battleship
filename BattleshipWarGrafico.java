/**
 * Es la clase que gestiona el juego :D
 * Controla el flujo del juego, la interfaz gráfica y la comunicación
 * entre jugadores.
 *
 * @author (AYJB)
 * @version (ABR 2025)
 */

import javax.swing.*;
import java.awt.*;

public class BattleshipWarGrafico extends JFrame {
    private static final String ARCHIVO_GUARDADO = "partidaBatallaNaval.dat";
    private Jugador[] jugadores;
    private int turno = 0;
    private boolean partidaEnCurso = false;
    private boolean colocandoBarcos = false;
    private int jugadorActualConfig = 0;
    private int[] tamañosBarcos = {3, 4, 4, 5, 5, 6};
    private int barcoActual = 0;
    private String orientacionActual = "horizontal";

    private JFrame ventana;
    private JPanel panelPrincipal;
    private JPanel panelInicio;
    private JPanel panelJuego;
    private JPanel panelColocacion;
    private JTextArea areaRegistro;
    private JLabel etiquetaEstado;
    private JButton[][] botonesAtaque;
    private JButton[][] botonesColocacion;
    private JButton botonRotar;
    private JLabel etiquetaInfoColocacion;

    public BattleshipWarGrafico() {
        inicializarInterfaz();
    }

    private void inicializarInterfaz() {
        ventana = new JFrame("Batalla Naval");
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setSize(1000, 800);
        ventana.setMinimumSize(new Dimension(900, 700));

        panelPrincipal = new JPanel(new CardLayout());

        // Panel de inicio
        panelInicio = crearPanelInicio();
        panelPrincipal.add(panelInicio, "inicio");

        // Panel de colocación de barcos
        panelColocacion = crearPanelColocacion();
        panelPrincipal.add(panelColocacion, "colocacion");

        // Panel de juego
        panelJuego = crearPanelJuego();
        panelPrincipal.add(panelJuego, "juego");

        ventana.add(panelPrincipal);
        ventana.setVisible(true);
    }

    private JPanel crearPanelInicio() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titulo = new JLabel(" ¡ BATALLA NAVAL !");
        titulo.setFont(new Font("Arial", Font.BOLD, 32));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titulo, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        JButton botonCargar = new JButton("Cargar Partida");
        botonCargar.setFont(new Font("Arial", Font.PLAIN, 18));
        botonCargar.addActionListener(e -> cargarPartida());
        panel.add(botonCargar, gbc);

        gbc.gridx = 1;
        JButton botonNueva = new JButton("Nueva Partida");
        botonNueva.setFont(new Font("Arial", Font.PLAIN, 18));
        botonNueva.addActionListener(e -> comenzarNuevaPartida());
        panel.add(botonNueva, gbc);

        return panel;
    }

    private JPanel crearPanelColocacion() {
        JPanel panel = new JPanel(new BorderLayout());

        // Panel de información
        JPanel panelInfo = new JPanel();
        etiquetaInfoColocacion = new JLabel("", JLabel.CENTER);
        etiquetaInfoColocacion.setFont(new Font("Arial", Font.BOLD, 18));
        panelInfo.add(etiquetaInfoColocacion);

        // Botón de rotación
        botonRotar = new JButton("Rotar a Vertical");
        botonRotar.addActionListener(e -> {
            orientacionActual = orientacionActual.equals("horizontal") ? "vertical" : "horizontal";
            botonRotar.setText(orientacionActual.equals("horizontal") ? "Rotar a Vertical" : "Rotar a Horizontal");
        });
        panelInfo.add(botonRotar);

        panel.add(panelInfo, BorderLayout.NORTH);

        // Panel del tablero para colocar barcos
        JPanel panelTablero = new JPanel(new GridLayout(14, 14));
        botonesColocacion = new JButton[14][14];

        for (int fila = 0; fila < 14; fila++) {
            for (int columna = 0; columna < 14; columna++) {
                botonesColocacion[fila][columna] = new JButton();
                botonesColocacion[fila][columna].setPreferredSize(new Dimension(40, 40));
                final int f = fila;
                final int c = columna;
                botonesColocacion[fila][columna].addActionListener(e -> colocarBarco(f, c));
                panelTablero.add(botonesColocacion[fila][columna]);
            }
        }

        panel.add(panelTablero, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelJuego() {
        JPanel panel = new JPanel(new BorderLayout());

        // Panel de registro
        areaRegistro = new JTextArea(10, 30);
        areaRegistro.setEditable(false);
        JScrollPane scroll = new JScrollPane(areaRegistro);
        panel.add(scroll, BorderLayout.SOUTH);

        // Panel de estado
        etiquetaEstado = new JLabel(" ", JLabel.CENTER);
        etiquetaEstado.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(etiquetaEstado, BorderLayout.NORTH);

        // Panel de ataque
        JPanel panelAtaque = new JPanel(new GridLayout(14, 14));
        botonesAtaque = new JButton[14][14];

        for (int fila = 0; fila < 14; fila++) {
            for (int columna = 0; columna < 14; columna++) {
                botonesAtaque[fila][columna] = new JButton();
                botonesAtaque[fila][columna].setPreferredSize(new Dimension(40, 40));
                final int f = fila;
                final int c = columna;
                botonesAtaque[fila][columna].addActionListener(e -> atacar(f, c));
                panelAtaque.add(botonesAtaque[fila][columna]);
            }
        }

        panel.add(panelAtaque, BorderLayout.CENTER);

        return panel;
    }

    private void cargarPartida() {
        jugadores = Tablero.cargarPartida(ARCHIVO_GUARDADO);
        if (jugadores != null) {
            boolean jugador1Derrotado = jugadores[0].getTablero().todosLosBarcosDestruidos();
            boolean jugador2Derrotado = jugadores[1].getTablero().todosLosBarcosDestruidos();

            if (jugador1Derrotado || jugador2Derrotado) {
                areaRegistro.append("⚠️ La partida guardada ya terminó. Comenzando nueva partida.\n");
                comenzarNuevaPartida();
            } else {
                areaRegistro.append("✔ Partida cargada correctamente.\n");
                iniciarJuego();
            }
        } else {
            areaRegistro.append("No se pudo cargar la partida. Comenzando nueva partida.\n");
            comenzarNuevaPartida();
        }
    }

    private void comenzarNuevaPartida() {
        // Pedir nombres de jugadores
        String nombreJugador1 = JOptionPane.showInputDialog(ventana, "Nombre del Jugador 1:", "Nueva Partida", JOptionPane.PLAIN_MESSAGE);
        if (nombreJugador1 == null || nombreJugador1.trim().isEmpty()) nombreJugador1 = "Jugador 1";

        String nombreJugador2 = JOptionPane.showInputDialog(ventana, "Nombre del Jugador 2:", "Nueva Partida", JOptionPane.PLAIN_MESSAGE);
        if (nombreJugador2 == null || nombreJugador2.trim().isEmpty()) nombreJugador2 = "Jugador 2";

        // Crear jugadores
        jugadores = new Jugador[2];
        jugadores[0] = new Jugador(nombreJugador1, new Tablero());
        jugadores[1] = new Jugador(nombreJugador2, new Tablero());

        // Iniciar colocación de barcos
        colocandoBarcos = true;
        jugadorActualConfig = 0;
        barcoActual = 0;
        actualizarInterfazColocacion();
        ((CardLayout) panelPrincipal.getLayout()).show(panelPrincipal, "colocacion");
    }

    private void actualizarInterfazColocacion() {
        etiquetaInfoColocacion.setText(jugadores[jugadorActualConfig].getNombre() +
                " - Colocando barco " + (barcoActual+1) + "/" + tamañosBarcos.length +
                " (Tamaño: " + tamañosBarcos[barcoActual] + ")");

        // Limpiar tablero de colocación
        for (int fila = 0; fila < 14; fila++) {
            for (int columna = 0; columna < 14; columna++) {
                botonesColocacion[fila][columna].setBackground(null);
                botonesColocacion[fila][columna].setText("");
            }
        }

        // Mostrar barcos ya colocados
        int[][] tablero = jugadores[jugadorActualConfig].getTablero().getTablero()[0];
        for (int fila = 0; fila < 14; fila++) {
            for (int columna = 0; columna < 14; columna++) {
                if (tablero[fila][columna] == 5) {
                    botonesColocacion[fila][columna].setBackground(Color.GRAY);
                }
            }
        }
    }

    private void colocarBarco(int fila, int columna) {
        if (!colocandoBarcos) return;

        Tablero tablero = jugadores[jugadorActualConfig].getTablero();
        int tamaño = tamañosBarcos[barcoActual];

        boolean colocado = tablero.colocarBarco(orientacionActual, tamaño, 0, fila, columna);

        if (colocado) {
            barcoActual++;

            if (barcoActual >= tamañosBarcos.length) {
                // Cambiar al siguiente jugador o comenzar el juego
                jugadorActualConfig++;
                if (jugadorActualConfig >= 2) {
                    colocandoBarcos = false;
                    iniciarJuego();
                    return;
                } else {
                    barcoActual = 0;
                    JOptionPane.showMessageDialog(ventana,
                            "Ahora le toca a " + jugadores[jugadorActualConfig].getNombre() + " colocar sus barcos",
                            "Cambio de jugador", JOptionPane.INFORMATION_MESSAGE);
                }
            }

            actualizarInterfazColocacion();
        }
    }

    private void iniciarJuego() {
        ((CardLayout) panelPrincipal.getLayout()).show(panelPrincipal, "juego");
        partidaEnCurso = true;
        turno = 0;
        actualizarInterfazJuego();
    }

    private void atacar(int fila, int columna) {
        if (!partidaEnCurso) return;

        Jugador atacante = jugadores[turno % 2];
        Jugador defensor = jugadores[(turno + 1) % 2]; // El oponente es quien recibe el ataque

        // Verificar si la celda ya fue atacada en el TABLERO DE ATAQUE del atacante
        if (atacante.getTablero().getTablero()[1][fila][columna] != 0) {
            JOptionPane.showMessageDialog(ventana, "¡Ya atacaste esta posición!", "Movimiento inválido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean impacto = defensor.getTablero().recibirAtaque(fila, columna);

        atacante.getTablero().registrarAtaque(fila, columna, impacto);

        // Actualizar interfaz
        if (impacto) {
            botonesAtaque[fila][columna].setBackground(Color.RED);
            botonesAtaque[fila][columna].setText("X");
            areaRegistro.append(atacante.getNombre() + ": Ataque en (" + fila + "," + columna + ") - 🔥 Impacto!\n");
        } else {
            botonesAtaque[fila][columna].setBackground(Color.BLUE);
            botonesAtaque[fila][columna].setText("O");
            areaRegistro.append(atacante.getNombre() + ": Ataque en (" + fila + "," + columna + ") - 💧 Falló...\n");
        }
        botonesAtaque[fila][columna].setEnabled(false);

        // Verificar fin del juego
        if (defensor.getTablero().todosLosBarcosDestruidos()) {
            partidaEnCurso = false;
            etiquetaEstado.setText("🏆 " + atacante.getNombre() + " ha ganado!");
            areaRegistro.append("\n🏆 " + atacante.getNombre() + " ha ganado el juego!\n");
            JOptionPane.showMessageDialog(ventana, atacante.getNombre() + " ha ganado el juego!", "Fin del Juego", JOptionPane.INFORMATION_MESSAGE);
        } else {
            turno++;
            actualizarInterfazJuego();
        }

        Tablero.guardarPartida(jugadores, ARCHIVO_GUARDADO);
    }

    private void actualizarInterfazJuego() {
        if (jugadores == null) return;

        Jugador jugadorActual = jugadores[turno % 2];
        Jugador oponente = jugadores[(turno + 1) % 2];
        etiquetaEstado.setText("Turno de: " + jugadorActual.getNombre() + " - Atacando a " + oponente.getNombre());

        // Mostrar el tablero de ataque del oponente
        int[][] tableroAtaque = jugadorActual.getTablero().getTablero()[1];

        for (int fila = 0; fila < 14; fila++) {
            for (int columna = 0; columna < 14; columna++) {
                botonesAtaque[fila][columna].setEnabled(true);
                botonesAtaque[fila][columna].setText("");
                botonesAtaque[fila][columna].setBackground(null);

                // Mostrar los ataques previos
                if (tableroAtaque[fila][columna] == 8) { // Impacto
                    botonesAtaque[fila][columna].setBackground(Color.RED);
                    botonesAtaque[fila][columna].setText("X");
                    botonesAtaque[fila][columna].setEnabled(false);
                } else if (tableroAtaque[fila][columna] == 9) { // Falló
                    botonesAtaque[fila][columna].setBackground(Color.BLUE);
                    botonesAtaque[fila][columna].setText("O");
                    botonesAtaque[fila][columna].setEnabled(false);
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BattleshipWarGrafico());
    }
}
