/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gato;

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

    //Cantidad de casillas
    private static final int NUM_CASILLAS = 9;
    //almacen de cassillas
    private PiezaGato[] posicion;
    //Turno actual en ejecucion
    private PiezaGato turnoActual;

    public TableroGato() {
        //inicializamon el arreglo con 9 posiciones
        this.posicion = new PiezaGato[NUM_CASILLAS];
        //iniciamos el primer turno
        turnoActual = PiezaGato.X;
        //Rellenamos el array con espacios vacios:
        Arrays.fill(posicion, PiezaGato.E);
    }

    public TableroGato(PiezaGato[] posicion, PiezaGato turnoActual) {
        this.posicion = posicion;
        this.turnoActual = turnoActual;
    }

    @Override
    public PiezaGato getTurno() {
        return this.turnoActual;
    }

    @Override
    public Tablero<Integer> mover(Integer ubicacion) {
        //copiamos las posiciones actuales
        PiezaGato[] posicionTemporal = Arrays.copyOf(posicion, posicion.length);
        //dibujamos el turno actual
        posicionTemporal[ubicacion] = turnoActual;
        //devolvermos un tablero nuevo, con el turno hecho
        //y lo entregamos con el turno opuesto
        return new TableroGato(posicionTemporal, turnoActual.opuesto());
    }

    @Override
    public List<Integer> getMovimientosLegales() {
        var movimientosLegales = new ArrayList<Integer>();
        for (int i = 0; i < posicion.length; i++) {
            //si es un espacio vacio lo agregamos
            //a la lista de movimientos permitidos
            if (posicion[i] == PiezaGato.E) {
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
        return posicion[pos0] == posicion[pos1]
                && posicion[pos1] == posicion[pos2]
                && posicion[pos0] != PiezaGato.E; //para que no me tome el caso vacio
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
                sb.append(posicion[fila * 3 + columna].toString());
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
