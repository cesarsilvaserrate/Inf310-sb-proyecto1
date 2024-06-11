package gato;

import java.util.Scanner;
import Arboles.*;
import Excepciones.ExcepcionOrdenInvalido;
/**
 *
 * @author lubuntu
 */
public class Gato {

    private final ArbolMVias<Integer, TableroGato> arbol1;  // Usamos un árbol de orden 3
    private Scanner scanner = new Scanner(System.in);
    private int contadorDeMovimientos; // Variable para llevar la cuenta de movimientos
    private TableroGato primerMovimiento; // Variable para almacenar el primer movimiento

    public Gato() throws ExcepcionOrdenInvalido {
        this.arbol1 = new ArbolMVias<>(3);
        this.contadorDeMovimientos = 0;
        this.primerMovimiento = null;
        arbol1.insertar(contadorDeMovimientos, new TableroGato());  // Inicializamos el árbol con un tablero vacío
    }

    public TableroGato obtenerTablero(int movimiento) {
        return arbol1.buscar(movimiento);
    }

    public void actualizarTablero(TableroGato tablero) {
        contadorDeMovimientos++;
        arbol1.insertar(contadorDeMovimientos, tablero);
        if (contadorDeMovimientos == 1) {
            primerMovimiento = tablero;
        }
    }

    public Integer getMovimientoDelJugador() {
        TableroGato tablero = obtenerTablero(contadorDeMovimientos);
        Integer mover = -1;
        while (!tablero.getMovimientosLegales().contains(mover)) {
            System.out.println("Ingrese la casilla del 0 al 8 o -1 para deshacer el último movimiento, o -2 para ver el primer movimiento: ");
            Integer entradaDelMovimiento = scanner.nextInt();
            if (entradaDelMovimiento == -1) {
                if (contadorDeMovimientos > 0) {
                    try {
                        tablero = tablero.deshacerUltimoMovimiento();
                        arbol1.insertar(contadorDeMovimientos, tablero);  // Actualizamos el tablero en el árbol
                        contadorDeMovimientos--;  // Reducimos el contador ya que se deshizo un movimiento
                        System.out.println("Movimiento deshecho.");
                        System.out.println(tablero);
                    } catch (Exception e) {
                        System.out.println("No se puede deshacer el movimiento: " + e.getMessage());
                    }
                } else {
                    System.out.println("No hay movimientos para deshacer.");
                }
            } else if (entradaDelMovimiento == -2) {
                if (primerMovimiento != null) {
                    System.out.println("Primer movimiento de la partida:");
                    System.out.println(primerMovimiento);
                } else {
                    System.out.println("No hay movimientos realizados aún.");
                }
            } else {
                mover = entradaDelMovimiento;
            }
        }
        return mover;
    }

    private void jugarAlGato() {
        while (true) {
            Integer movimientoHumano = getMovimientoDelJugador();
            if (movimientoHumano != -1) {
                TableroGato tablero = obtenerTablero(contadorDeMovimientos);
                tablero = (TableroGato) tablero.mover(movimientoHumano);
                actualizarTablero(tablero);  // Actualizamos el tablero en el árbol

                if (tablero.haGanado()) {
                    System.out.println(tablero);
                    System.out.println("Los humanos aún dominan las máquinas.");
                    break;
                } else if (tablero.haEmpatado()) {
                    System.out.println(tablero);
                    System.out.println("Máquina y hombres tienen la misma capacidad.");
                    break;
                }

                Integer movimientoPc = Minimax.EncontrarMejorMovimiento(tablero, 9);
                System.out.println("La computadora ha decidido: " + movimientoPc);
                tablero = (TableroGato) tablero.mover(movimientoPc);
                actualizarTablero(tablero);  // Actualizamos el tablero en el árbol
                System.out.println(tablero);

                if (tablero.haGanado()) {
                    System.out.println(tablero);
                    System.out.println("La rebelión de las máquinas ha comenzado.");
                    break;
                } else if (tablero.haEmpatado()) {
                    System.out.println(tablero);
                    System.out.println("Máquina y hombres tienen la misma capacidad.");
                    break;
                }
            }
        }
    }

    public static void main(String[] args) throws ExcepcionOrdenInvalido {
        Gato gato = new Gato();
        gato.jugarAlGato();
    }
}
