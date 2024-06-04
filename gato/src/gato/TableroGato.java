/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gato;

import Arboles.*;
import adaptador.Pieza;
import adaptador.Tablero;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author lubuntu
 */
public class TableroGato implements Tablero<Integer> {

    private AVL<Integer, PiezaGato[]> historialMovimientos;
    private int contador;

    //Cantidad de casillas
    private static final int NUM_CASILLAS = 9;
    //almacen de cassillas
    private PiezaGato[] almacenCasillas;
    //Turno actual en ejecucion
    private PiezaGato turnoActual;

    public TableroGato() {
        //inicializamon el arreglo con 9 posiciones
        this.almacenCasillas = new PiezaGato[NUM_CASILLAS];
        //iniciamos el primer turno
        turnoActual = PiezaGato.X;
        //Rellenamos el almacenCasillas con espacios vacios:
        Arrays.fill(almacenCasillas, PiezaGato.E);
        this.historialMovimientos = new AVL<>();

        this.contador = 0;
    }

    public TableroGato(PiezaGato[] almacen, PiezaGato turnoActual) {
        this.almacenCasillas = almacen;
        this.turnoActual = turnoActual;
    }

    public TableroGato(PiezaGato[] almacen, PiezaGato turnoActual, AVL<Integer, PiezaGato[]> historialMovimientos) {
        this.almacenCasillas = almacen;
        this.turnoActual = turnoActual;
        this.historialMovimientos = historialMovimientos;
    }

    public TableroGato(PiezaGato[] almacen, PiezaGato turnoActual, AVL<Integer, PiezaGato[]> historialMovimientos, int contador) {
        this.almacenCasillas = almacen;
        this.turnoActual = turnoActual;
        this.historialMovimientos = historialMovimientos;
        this.contador = contador;
    }

    @Override
    public PiezaGato getTurno() {
        return this.turnoActual;
    }

    @Override
    public Tablero<Integer> mover(Integer posicion) {
        // Copiamos el almacen actual a un almacen temporal
        PiezaGato[] almacenTempCasillas = Arrays.copyOf(almacenCasillas, almacenCasillas.length);

        // Agregamos el turno actual
        almacenTempCasillas[posicion] = turnoActual;

        // Incrementamos el contador
        int nuevoContador = this.contador + 1;

        // Creamos un nuevo AVL para el historial de movimientos
        AVL<Integer, PiezaGato[]> nuevoHistorialMovimientos = new AVL<>();

        // Copiamos los elementos del historial actual al nuevo historial
        for (int i = 1; i <= this.contador; i++) {
            PiezaGato[] estado = historialMovimientos.buscar(i);
            if (estado != null) {
                nuevoHistorialMovimientos.insertar(i, estado);
            }
        }

        // Insertamos el nuevo estado en el historial
        nuevoHistorialMovimientos.insertar(nuevoContador, almacenTempCasillas);

        // Devolvemos un nuevo tablero con el turno hecho y el turno opuesto
        return new TableroGato(almacenTempCasillas, turnoActual.opuesto(), nuevoHistorialMovimientos, nuevoContador);
    }

    public TableroGato deshacerUltimoMovimiento() throws Exception {
        if (NodoBinario.esNodoVacio(historialMovimientos.getRaiz())) {
            throw new Exception("No hay movimientos para deshacer");
        }

        Integer ubicacionUltimoMovimiento = historialMovimientos.obtenerClaveMaxima();
        AVL<Integer, PiezaGato[]> nuevoHistorialMovimientos = new AVL<>();

        for (int i = 1; i < ubicacionUltimoMovimiento; i++) {
            PiezaGato[] estado = historialMovimientos.buscar(i);
            if (estado != null) {
                nuevoHistorialMovimientos.insertar(i, estado);
            }
        }

        // Obtener el estado anterior al último movimiento
        PiezaGato[] estadoAnterior = historialMovimientos.buscar(ubicacionUltimoMovimiento - 1);

        // Decrementar el contador
        int nuevoContador = this.contador - 1;

        // Si no hay movimientos previos, inicializar un tablero vacío
        if (estadoAnterior == null) {
            estadoAnterior = new PiezaGato[NUM_CASILLAS];
            Arrays.fill(estadoAnterior, PiezaGato.E);
        }

        // Devolver un nuevo tablero con el estado anterior y el turno opuesto
        return new TableroGato(estadoAnterior, turnoActual.opuesto(), nuevoHistorialMovimientos, nuevoContador);
    }

    @Override
    public List<Integer> getMovimientosLegales() {
        var movimientosLegales = new ArrayList<Integer>();
        for (int i = 0; i < almacenCasillas.length; i++) {
            //si es un espacio vacio lo agregamos
            //a la lista de movimientos permitidos
            if (almacenCasillas[i] == PiezaGato.E) {
                movimientosLegales.add(i);
            }
        }
        return movimientosLegales;
    }

    @Override
    public boolean haGanado() {
        return chequearPosicion(0, 1, 2) || chequearPosicion(3, 4, 5)
                || chequearPosicion(6, 7, 8) || chequearPosicion(0, 3, 6)
                || chequearPosicion(1, 4, 7) || chequearPosicion(2, 5, 8)
                || chequearPosicion(0, 4, 8) || chequearPosicion(2, 4, 6);
    }

    private boolean chequearPosicion(int pos0, int pos1, int pos2) {
        return almacenCasillas[pos0] == almacenCasillas[pos1]
                && almacenCasillas[pos1] == almacenCasillas[pos2]
                && almacenCasillas[pos0] != PiezaGato.E; //para que no me tome el caso vacio
    }

    @Override
    public boolean haEmpatado() {
        return Tablero.super.haEmpatado(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double evaluar(Pieza jugador) {
        if (haGanado() && turnoActual == jugador) {
            return -1;
        } else if (haGanado() && turnoActual != jugador) {
            return 1;
        } else {
            return 0.0;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int fila = 0; fila < 3; fila++) {
            for (int columna = 0; columna < 3; columna++) {
                //primero dibuja la pieza
                sb.append(almacenCasillas[fila * 3 + columna].toString());
                //si no es la columna final rayita
                if (columna != 2) {
                    sb.append("|");
                }

            }
            //salto de linea
            sb.append(System.lineSeparator());
            if (fila != 2) {
                sb.append("------");
                sb.append(System.lineSeparator());
            }
        }

        return sb.toString();

    }

}
