/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gato;

import java.util.Scanner;

/**
 *
 * @author lubuntu
 */
public class Gato {
    
    private TableroGato tablero= new TableroGato();
    private Scanner scanner = new Scanner(System.in);
    
    public Integer getMovimientoDelJugador(){
        Integer mover = -1;
        while(!tablero.getMovimientosLegales().contains(mover)){
            System.out.println("Ingrese la casilla del 0 al 8: ");
            Integer entradaDelMovimiento = scanner.nextInt();
            mover = entradaDelMovimiento;
        }
        return mover;
    }
    
    private void jugarAlGato(){
        while(true){
            Integer movimientoHumano = getMovimientoDelJugador();
            tablero = (TableroGato) tablero.mover(movimientoHumano);
            
            if(tablero.haGanado()){
                System.out.println(tablero);
                System.out.println("Los humanos aun dominan las máquinas");
                
                break;
            }else if(tablero.haEmpatado()){
                System.out.println(tablero);
                System.out.println("Maquina y hombres tienen la misma capacidad");
                
                break;
            }
            
            Integer movimientoPc = Minimax.EncontrarMejorMovimiento(tablero, 9);
            System.out.println("La computadora ha Decidido: " + movimientoPc);
            tablero = (TableroGato) tablero.mover(movimientoPc);
            System.out.println(tablero);
            
            if(tablero.haGanado()){
                System.out.println(tablero);
                System.out.println("La rebelion de las maquinas ha comenzado");
                
                break;
            }else if(tablero.haEmpatado()){
                System.out.println(tablero);
                System.out.println("Maquina y hombres tienen la misma capacidad");
                
                break;
            }
            
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Gato gato = new Gato();
        gato.jugarAlGato();
        
        
    }
    
}
