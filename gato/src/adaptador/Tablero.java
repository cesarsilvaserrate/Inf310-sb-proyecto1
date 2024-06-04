/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adaptador;

import java.util.List;

/**
 *
 * @author lubuntu
 */
public interface Tablero<Movimiento>{
    Pieza getTurno();
    Tablero<Movimiento> mover(Movimiento ubicacion);
    List<Movimiento> getMovimientosLegales();
    boolean haGanado();
    default boolean haEmpatado(){
        return !haGanado() && getMovimientosLegales().isEmpty();
    }
    double evaluar(Pieza Jugador);
    
}
