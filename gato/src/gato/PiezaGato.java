/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gato;

import adaptador.Pieza;

/**
 *
 * @author lubuntu
 */
public enum PiezaGato implements Pieza{

    X, O, E;

    @Override
    public String toString(){
        switch(this){
            case X:
                return "X";
            case O:
                return "O";
            default: 
                return " ";
             
            
        }
    }
    
    @Override
    public PiezaGato opuesto() {
        switch(this){
            case X:
                return O;
            case O:
                return X;
            default: 
                return E;
             
            
        }
        
    }
    
}
