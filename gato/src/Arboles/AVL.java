package Arboles;

import java.util.List;

public class AVL<K extends Comparable<K>, V> extends ArbolBinarioBusqueda<K, V> {
    
    private static final int LIMITE_MAX = 1;
    
    @Override
    public void insertar(K claveAInsertar, V valorAsociado) {
        if (claveAInsertar == null) {
            throw new IllegalArgumentException("La clave a insertar no puede ser nula");
        }
        if (valorAsociado == null) {
            throw new IllegalArgumentException("El valor asociado no puede ser nulo");
        }
        if (super.contiene(claveAInsertar)) {
            throw new RuntimeException("La clave ya existe");
        }
        this.raiz = insertar(this.raiz, claveAInsertar, valorAsociado);
    }
    
    private NodoBinario<K, V> insertar(NodoBinario<K, V> nodoActual, K claveAInsertar, V valorAsociado) {
        if (NodoBinario.esNodoVacio(nodoActual)) {
            NodoBinario<K, V> nuevoNodo = new NodoBinario<>(claveAInsertar, valorAsociado);
            return nuevoNodo;
        }
        
        K claveDelNodoActual = nodoActual.getClave();
        
        if (claveAInsertar.compareTo(claveDelNodoActual) < 0) {
            NodoBinario<K, V> supuestoNuevoHijoIzquierdo = insertar(nodoActual.getHijoIzquierdo(), 
                    claveAInsertar, valorAsociado);
            nodoActual.setHijoIzquierdo(supuestoNuevoHijoIzquierdo);
            return balancear(nodoActual);
        }
        
        if (claveAInsertar.compareTo(claveDelNodoActual) > 0) {
            NodoBinario<K, V> supuestoNuevoHijoDerecho = insertar(nodoActual.getHijoDerecho(), 
                    claveAInsertar, valorAsociado);
            nodoActual.setHijoDerecho(supuestoNuevoHijoDerecho);
            return balancear(nodoActual);
        }
        
        // Si llego acá quiere decir que en el nodoActual esta la clave a insertar
        nodoActual.setValor(valorAsociado);
        return nodoActual;
    }

    private NodoBinario<K, V> balancear(NodoBinario<K, V> nodoActual) {
        int alturaPorIzquierda = Altura(nodoActual.getHijoIzquierdo());
        int alturaPorDerecha = Altura(nodoActual.getHijoDerecho());
        int diferenciaDeAlturas = alturaPorIzquierda - alturaPorDerecha;
        if (diferenciaDeAlturas > LIMITE_MAX) {
            /* Rama izquierda más larga, entonces hay que rotar a la derecha.
               Averigüemos si es simple o doble */
            NodoBinario<K, V> hijoIzqDelNodoActual = nodoActual.getHijoIzquierdo();
            alturaPorIzquierda = Altura(hijoIzqDelNodoActual.getHijoIzquierdo());
            alturaPorDerecha = Altura(hijoIzqDelNodoActual.getHijoDerecho());
            if (alturaPorDerecha > alturaPorIzquierda) {
                return rotacionDobleADerecha(nodoActual);
            }
            return rotacionSimpleADerecha(nodoActual);
        } else if (diferenciaDeAlturas < -LIMITE_MAX) {
            NodoBinario<K, V> hijoDerDelNodoActual = nodoActual.getHijoDerecho();
            alturaPorIzquierda = Altura(hijoDerDelNodoActual.getHijoIzquierdo());
            alturaPorDerecha = Altura(hijoDerDelNodoActual.getHijoDerecho());
            if (alturaPorDerecha < alturaPorIzquierda) {
                return rotacionDobleAIzquierda(nodoActual);
            }
            return rotacionSimpleAIzquierda(nodoActual);
        }
        return nodoActual;
    }
    
    private NodoBinario<K, V> rotacionSimpleADerecha(NodoBinario<K, V> nodoActual) {
        NodoBinario<K, V> nodoQueRota = nodoActual.getHijoIzquierdo();
        nodoActual.setHijoIzquierdo(nodoQueRota.getHijoDerecho());
        nodoQueRota.setHijoDerecho(nodoActual);
        return nodoQueRota;
    }
    
    private NodoBinario<K, V> rotacionDobleADerecha(NodoBinario<K, V> nodoActual) {
        NodoBinario<K, V> primerNodoQueRota = rotacionSimpleAIzquierda(nodoActual.getHijoIzquierdo());
        nodoActual.setHijoIzquierdo(primerNodoQueRota);
        return rotacionSimpleADerecha(nodoActual);
    }
    
    private NodoBinario<K, V> rotacionSimpleAIzquierda(NodoBinario<K, V> nodoActual) {
        NodoBinario<K, V> nodoQueRota = nodoActual.getHijoDerecho();
        nodoActual.setHijoDerecho(nodoQueRota.getHijoIzquierdo());
        nodoQueRota.setHijoIzquierdo(nodoActual);
        return nodoQueRota;
    }
    
    private NodoBinario<K, V> rotacionDobleAIzquierda(NodoBinario<K, V> nodoActual) {
        NodoBinario<K, V> primerNodoQueRota = rotacionSimpleADerecha(nodoActual.getHijoDerecho());
        nodoActual.setHijoDerecho(primerNodoQueRota);
        return rotacionSimpleAIzquierda(nodoActual);
    }
    public K obtenerClaveMaxima() throws Exception {
        if (NodoBinario.esNodoVacio(raiz)) {
            throw new Exception("El árbol está vacío");
        }
        NodoBinario<K, V> nodoActual = this.raiz;
        while (!NodoBinario.esNodoVacio(nodoActual)) {
            if (NodoBinario.esNodoVacio(nodoActual.getHijoDerecho())) {
                return nodoActual.getClave();
            }
            nodoActual = nodoActual.getHijoDerecho();
        }
        // Esto nunca debería ocurrir si el árbol AVL está correctamente construido
        throw new IllegalStateException("Error al buscar la clave máxima");
        
    }
    
    public static void main(String[] args) throws Exception {
        
        AVL<Integer, String> arbol1 = new AVL<>();

        arbol1.insertar(0, "aa");
        arbol1.insertar(1, "bb");
        arbol1.insertar(2, "cc");
        arbol1.insertar(3, "dd");
        arbol1.insertar(4, "ee");

        List<Integer> lista = arbol1.recorridoEnPostOrden();
        List<Integer> lista2 = arbol1.recorridoEnInOrden();
        System.out.println(lista);
        System.out.println(lista2);
        System.out.println(arbol1.obtenerClaveMaxima().toString());
        
    }

    
    
}
