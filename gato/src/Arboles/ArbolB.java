package Arboles;

import Excepciones.ClaveNoExisteException;
import Excepciones.ExcepcionOrdenInvalido;
import java.util.Stack;

public class ArbolB<K extends Comparable<K>, V> extends ArbolMVias<K, V> {
    
    private int nroMaximoDeDatos;
    private int nroMinimoDeDatos;
    private int nroMinimoDeHijos;
    
    public ArbolB() {
        super(); // super.orden = 3;
        this.nroMaximoDeDatos = 2;
        this.nroMinimoDeDatos = 1;
        this.nroMinimoDeHijos = 2;
    }
    
    public ArbolB(int orden) throws ExcepcionOrdenInvalido {
        super(orden);
        this.nroMaximoDeDatos = super.orden - 1;
        this.nroMinimoDeDatos = this.nroMaximoDeDatos / 2;
        this.nroMinimoDeHijos = this.nroMinimoDeDatos + 1;
    }
    
    @Override
    public void insertar(K claveAInsertar, V valorAInsertar) {
        if (claveAInsertar == null) {
            throw new IllegalArgumentException("La clave a insertar no puede ser nula");
        }
        if (valorAInsertar == null) {
            throw new IllegalArgumentException("El valor asociado a la clave no puede ser nula");
        }
        if (super.esArbolVacio()) {
            super.raiz = new NodoMVias<>(super.orden + 1, claveAInsertar, valorAInsertar);
            return;
        }
        
        Stack<NodoMVias<K, V>> pilaDeAncestros = new Stack<>();
        NodoMVias<K, V> nodoActual = this.raiz;
        
        while (!NodoMVias.esNodoVacio(nodoActual)) {
            int posicionDeClaveAInsertar = super.obtenerPosicionDeClave(nodoActual, claveAInsertar);
            if (posicionDeClaveAInsertar != POSICION_INVALIDA) {
                nodoActual.setValor(posicionDeClaveAInsertar, valorAInsertar);
                nodoActual = NodoMVias.nodoVacio();
            } else if (nodoActual.esHoja()) {
                super.insertarDatosOrdenadoEnNodo(nodoActual, claveAInsertar, valorAInsertar);
                if (nodoActual.nroDeClavesNoVacias() > this.nroMaximoDeDatos) {
                    this.dividir(nodoActual, pilaDeAncestros);
                } 
                nodoActual = NodoMVias.nodoVacio();
            } else {
                // En caso de que el nodo actual no sea hoja
                int posicionPorDondeBajar = super.obtenerPosicionPorDondeBajar(nodoActual, claveAInsertar);
                pilaDeAncestros.push(nodoActual);
                nodoActual = nodoActual.getHijo(posicionPorDondeBajar);
            }
        }
    }

    private void dividir(NodoMVias<K, V> nodoActual, Stack<NodoMVias<K, V>> pilaDeAncestros) {
        
    }
    
    @Override
    public V eliminar(K claveAEliminar) throws ClaveNoExisteException {
        if (claveAEliminar == NodoMVias.datoVacio()) {
            throw new IllegalArgumentException("La clave a eliminar no puede ser nula");
        }
        
        Stack<NodoMVias<K, V>> pilaDeAncestros = new Stack<>();
        NodoMVias<K, V> nodoActual = this.buscarNodoDeLaClave(claveAEliminar, pilaDeAncestros);
        
        if (!NodoMVias.esNodoVacio(nodoActual)) {
            throw new ClaveNoExisteException();
        }
        int posicionDeClaveAEliminar = super.obtenerPosicionPorDondeBajar(nodoActual, claveAEliminar) - 1;
        V valorAEliminar = nodoActual.getValor(posicionDeClaveAEliminar);
        
        if (nodoActual.esHoja()) {
            super.eliminarDatosDeNodo(nodoActual, posicionDeClaveAEliminar);
            if (nodoActual.nroDeClavesNoVacias() < this.nroMinimoDeDatos) {
                if (pilaDeAncestros.isEmpty()) { // El nodo actual es igual a la raiz
                    if (nodoActual.nroDeClavesNoVacias() == 0) {
                        super.vaciar();
                    }
                } else {
                    this.prestarseOFusionarse(nodoActual, pilaDeAncestros);
                }
            }
        } else { // El nodoActual no es hoja
            pilaDeAncestros.push(nodoActual);
            NodoMVias<K, V> nodoDelPredecesor = this.obtenerNodoDelPredecesor(pilaDeAncestros, 
                    nodoActual.getHijo(posicionDeClaveAEliminar));
            int posicionDelPredecesor = nodoDelPredecesor.nroDeClavesNoVacias() - 1;
            K clavePredecesora = nodoDelPredecesor.getClave(posicionDelPredecesor);
            V valorPredecesor = nodoDelPredecesor.getValor(posicionDelPredecesor);
            
            super.eliminarDatosDeNodo(nodoDelPredecesor, posicionDelPredecesor);
            nodoActual.setClave(posicionDeClaveAEliminar, clavePredecesora);
            nodoActual.setValor(posicionDeClaveAEliminar, valorPredecesor);
            
            if (nodoDelPredecesor.nroDeClavesNoVacias() < this.nroMinimoDeDatos) {
                this.prestarseOFusionarse(nodoDelPredecesor, pilaDeAncestros);
            }
        }
        
        return valorAEliminar;
    }

    private NodoMVias<K, V> buscarNodoDeLaClave(K claveAEliminar, Stack<NodoMVias<K, V>> pilaDeAncestros) {
        return null;
    }

    private void prestarseOFusionarse(NodoMVias<K, V> nodoActual, Stack<NodoMVias<K, V>> pilaDeAncestros) {
        
    }

    private NodoMVias<K, V> obtenerNodoDelPredecesor(Stack<NodoMVias<K, V>> pilaDeAncestros, NodoMVias<K, V> nodoActual) {
        while (!nodoActual.esHoja()) {
            pilaDeAncestros.push(nodoActual);
            nodoActual = nodoActual.getHijo(nodoActual.nroDeClavesNoVacias());
        }
        return nodoActual; 
    }
    
}
