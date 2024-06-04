/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gato;

import adaptador.Pieza;
import adaptador.Tablero;

/**
 *
 * @author lubuntu
 */
public class Minimax {
    public static<Movimiento> double minimax(Tablero<Movimiento> tablero, boolean isMaximizando, Pieza jugadorOriginal, int profundidadMaxima){
        if(tablero.haGanado() || tablero.haEmpatado() || profundidadMaxima == 0){
            return tablero.evaluar(jugadorOriginal);
        }
        
        //Minimax
        if(isMaximizando){
            double v = Double.NEGATIVE_INFINITY;
            for(Movimiento mover: tablero.getMovimientosLegales()){
                double resultado = minimax(tablero.mover(mover), false,
                        jugadorOriginal, profundidadMaxima -1);
                v=Math.max(v, resultado);
            }
            return v;
            
        }else{
            double v = Double.POSITIVE_INFINITY;
            for(Movimiento mover: tablero.getMovimientosLegales()){
                double resultado = minimax(tablero.mover(mover), true,
                        jugadorOriginal, profundidadMaxima -1);
                v=Math.max(v, resultado);
            }
            return v;
            
            
        }
        
        
    }
    
    public static <Mover> Mover EncontrarMejorMovimiento(Tablero<Mover> tablero, int profundidadMax){
        double mejorValor = Double.NEGATIVE_INFINITY;
        Mover mejorMovimiento = null;
        for(Mover mover : tablero.getMovimientosLegales()){
            double resultado = minimax(tablero, false, tablero.getTurno(), profundidadMax);
            if(resultado > mejorValor){
                mejorValor = resultado;
                mejorMovimiento = mover;
            }
        }
        
        return mejorMovimiento;
    }
    
    
    
}














