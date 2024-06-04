package Arboles;

import Excepciones.ClaveNoExisteException;
import Presentacion.ArbolExpresionGrafico;
import java.util.ArrayList;
import java.util.*;
import javax.swing.JPanel;

public class ArbolBinarioBusqueda<K extends Comparable<K>, V> implements IArbolBusqueda<K, V> {

    protected NodoBinario<K, V> raiz;

    public ArbolBinarioBusqueda() {
    }

    public NodoBinario<K, V> getRaiz() {
        return raiz;
    }

    @Override
    public void vaciar() {
        this.raiz = NodoBinario.nodoVacio();
    }

    @Override
    public boolean esArbolVacio() {
        return NodoBinario.esNodoVacio(this.raiz);
    }

    @Override
    public V buscar(K claveABuscar) {
        if (claveABuscar == null) {
            throw new IllegalArgumentException("La clave a buscar no puede ser nula");
        }
        NodoBinario<K, V> nodoActual = this.raiz;
        while (!NodoBinario.esNodoVacio(nodoActual)) {
            K claveActual = nodoActual.getClave();
            if (claveABuscar.compareTo(claveActual) < 0) {
                nodoActual = nodoActual.getHijoIzquierdo();
            } else if (claveABuscar.compareTo(claveActual) > 0) {
                nodoActual = nodoActual.getHijoDerecho();
            } else {
                return nodoActual.getValor();
            }
        }
        return null;
    }

    @Override
    public boolean contiene(K claveABuscar) {
        return buscar(claveABuscar) != null;
    }

    @Override
    public void insertar(K claveAInsertar, V valorAsociado) {
        if (claveAInsertar == null) {
            throw new IllegalArgumentException("La clave a insertar no puede ser nula");
        }
        if (valorAsociado == null) {
            throw new IllegalArgumentException("El valor asociado a la clave no puede ser nula");
        }
        if (this.esArbolVacio()) {
            this.raiz = new NodoBinario<>(claveAInsertar, valorAsociado);
            return;
        }
        NodoBinario<K, V> nodoAnterior = NodoBinario.nodoVacio();
        NodoBinario<K, V> nodoActual = this.raiz;
        while (!NodoBinario.esNodoVacio(nodoActual)) {
            K claveActual = nodoActual.getClave();
            if (claveAInsertar.compareTo(claveActual) < 0) {
                nodoAnterior = nodoActual;
                nodoActual = nodoActual.getHijoIzquierdo();
            } else if (claveAInsertar.compareTo(claveActual) > 0) {
                nodoAnterior = nodoActual;
                nodoActual = nodoActual.getHijoDerecho();
            } else {
                nodoActual.setValor(valorAsociado);
                return;
            }
        }
        // Si llego acá es porque ya se que la clave no hay en el árbol,
        // entonces debo insertarlo en el árbol en un nuevo nodoActual, enlazado
        // al nodoAnterior como su padre de ese nuevo nodoActual
        NodoBinario<K, V> nuevoNodo = new NodoBinario<>(claveAInsertar, valorAsociado);
        K claveDelNodoAnterior = nodoAnterior.getClave();
        if (claveAInsertar.compareTo(claveDelNodoAnterior) < 0) {
            nodoAnterior.setHijoIzquierdo(nuevoNodo);
        } else if (claveAInsertar.compareTo(claveDelNodoAnterior) > 0) {
            nodoAnterior.setHijoDerecho(nuevoNodo);
        }
    }

    @Override
    public List<K> recorridoPorNiveles() {
        List<K> recorrido = new ArrayList<>();
        if (!this.esArbolVacio()) {
            Queue<NodoBinario<K, V>> colaDeNodos = new LinkedList<>();
            colaDeNodos.offer(this.raiz);
            while (!colaDeNodos.isEmpty()) {
                NodoBinario<K, V> nodoActual = colaDeNodos.poll();
                recorrido.add(nodoActual.getClave());
                if (!nodoActual.esHijoIzquierdoVacio()) {
                    colaDeNodos.offer(nodoActual.getHijoIzquierdo());
                }
                if (!nodoActual.esHijoDerechoVacio()) {
                    colaDeNodos.offer(nodoActual.getHijoDerecho());
                }
            }
        }
        return recorrido;
    }

    @Override
    public List<K> recorridoEnPreOrden() {
        List<K> recorrido = new ArrayList<>();
        if (!this.esArbolVacio()) {
            Stack<NodoBinario<K, V>> pilaDeNodos = new Stack<>();
            pilaDeNodos.push(this.raiz);
            while (!pilaDeNodos.isEmpty()) {
                NodoBinario<K, V> nodoActual = pilaDeNodos.pop();
                recorrido.add(nodoActual.getClave());
                if (!nodoActual.esHijoDerechoVacio()) {
                    pilaDeNodos.push(nodoActual.getHijoDerecho());
                }
                if (!nodoActual.esHijoIzquierdoVacio()) {
                    pilaDeNodos.push(nodoActual.getHijoIzquierdo());
                }
            }
        }
        return recorrido;
    }

    @Override
    public List<K> recorridoEnInOrden() {
        List<K> recorrido = new ArrayList<>();
        Stack<NodoBinario<K, V>> pilaDeNodos = new Stack<>();
        this.apilarParaInOrden(pilaDeNodos, this.raiz);
        while (!pilaDeNodos.isEmpty()) {
            NodoBinario<K, V> nodoActual = pilaDeNodos.pop();
            recorrido.add(nodoActual.getClave());
            this.apilarParaInOrden(pilaDeNodos, nodoActual.getHijoDerecho());
        }
        return recorrido;
    }

    private void apilarParaInOrden(Stack<NodoBinario<K, V>> pilaDeNodos, NodoBinario<K, V> nodoActual) {
        while (!NodoBinario.esNodoVacio(nodoActual)) {
            pilaDeNodos.push(nodoActual);
            nodoActual = nodoActual.getHijoIzquierdo();
        }
    }

//    public List<K> recorridoEnInOrden() {
//        List<K> recorrido = new ArrayList<>();
//        Stack<NodoBinario<K, V>> pilaDeNodos = new Stack<>();
//        NodoBinario<K, V> nodoActual = this.raiz;
//        while (!NodoBinario.esNodoVacio(nodoActual)) {
//            pilaDeNodos.push(nodoActual);
//            nodoActual = nodoActual.getHijoIzquierdo();
//        }
//        while (!pilaDeNodos.isEmpty()) {
//            nodoActual = pilaDeNodos.pop();
//            recorrido.add(nodoActual.getClave());
//            nodoActual = nodoActual.getHijoDerecho();
//            while (!NodoBinario.esNodoVacio(nodoActual)) {
//                pilaDeNodos.push(nodoActual);
//                nodoActual = nodoActual.getHijoIzquierdo();
//            }
//        }
//        return recorrido;
//    }
    @Override
    public List<K> recorridoEnPostOrden() {
        List<K> recorrido = new ArrayList<>();
        if (!this.esArbolVacio()) {
            Stack<NodoBinario<K, V>> pilaDeNodos = new Stack<>();
            // El proceso inicial antes de iterar en la pila
            apilarParaPostOrden(pilaDeNodos, this.raiz);
            // Empezamos a iterar sobre la pila
            while (!pilaDeNodos.isEmpty()) {
                NodoBinario<K, V> nodoActual = pilaDeNodos.pop();
                recorrido.add(nodoActual.getClave());
                if (!pilaDeNodos.isEmpty()) {
                    NodoBinario<K, V> nodoDelTope = pilaDeNodos.peek();
                    if (!nodoDelTope.esHijoDerechoVacio() && nodoDelTope.getHijoDerecho() != nodoActual) {
                        // Volver a hacer el mismo bucle inicial
                        apilarParaPostOrden(pilaDeNodos, nodoDelTope.getHijoDerecho());
                    }
                }
            }
        }
        return recorrido;
    }

    private void apilarParaPostOrden(Stack<NodoBinario<K, V>> pilaDeNodos, NodoBinario<K, V> nodoActual) {
        while (!NodoBinario.esNodoVacio(nodoActual)) {
            pilaDeNodos.push(nodoActual);
            if (!nodoActual.esHijoIzquierdoVacio()) {
                nodoActual = nodoActual.getHijoIzquierdo();
            } else {
                nodoActual = nodoActual.getHijoDerecho();
            }
        }
    }

    @Override
    public V eliminar(K clave) throws ClaveNoExisteException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int size() {
        int nroDeNodos = 0;
        if (!this.esArbolVacio()) {
            Queue<NodoBinario<K, V>> colaDeNodos = new LinkedList<>();
            colaDeNodos.offer(this.raiz);
            while (!colaDeNodos.isEmpty()) {
                NodoBinario<K, V> nodoActual = colaDeNodos.poll();
                nroDeNodos++;
                if (!nodoActual.esHijoIzquierdoVacio()) {
                    colaDeNodos.offer(nodoActual.getHijoIzquierdo());
                }
                if (!nodoActual.esHijoDerechoVacio()) {
                    colaDeNodos.offer(nodoActual.getHijoDerecho());
                }
            }
        }
        return nroDeNodos;
    }

    @Override
    public int altura() {
        int altura = 0;
        if (!esArbolVacio()) {
            Queue<NodoBinario<K, V>> colaDeNodos = new LinkedList<>();
            colaDeNodos.offer(this.raiz);
            while (!colaDeNodos.isEmpty()) {
                int cantidadNodosEnLaCola = colaDeNodos.size();
                int nivel = 0;
                while (nivel < cantidadNodosEnLaCola) {
                    NodoBinario<K, V> nodoActual = colaDeNodos.poll();
                    if (!nodoActual.esHijoIzquierdoVacio()) {
                        colaDeNodos.offer(nodoActual.getHijoIzquierdo());
                    }
                    if (!nodoActual.esHijoDerechoVacio()) {
                        colaDeNodos.offer(nodoActual.getHijoDerecho());
                    }
                    nivel++;
                }
                altura++;
            }
        }
        return altura;
    }

    @Override
    public int nivel() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public int contarHojasPost() {
        int hojas = 0;
        if (!esArbolVacio()) {
            Stack<NodoBinario<K, V>> pilaDeNodos = new Stack<>();
            apilarParaPostOrden(pilaDeNodos, this.raiz);
            while (!pilaDeNodos.isEmpty()) {
                NodoBinario<K, V> nodoActual = pilaDeNodos.pop();
                if (nodoActual.esHoja()) {
                    hojas++;
                }
                if (!pilaDeNodos.isEmpty()) {
                    NodoBinario<K, V> nodoDelTope = pilaDeNodos.peek();
                    if (!nodoDelTope.esHijoDerechoVacio() && nodoDelTope.getHijoDerecho() != nodoActual) {
                        apilarParaPostOrden(pilaDeNodos, nodoDelTope.getHijoDerecho());
                    }
                }
            }
        }
        return hojas;
    }
    
    public int contarHojas() {
        return contarHojas(this.raiz);
    }
    
    private int contarHojas(NodoBinario<K, V> nodoActual) {
        if (NodoBinario.esNodoVacio(nodoActual)) {
            return 0;
        }
        int izq = contarHojas(nodoActual.getHijoIzquierdo());
        int der = contarHojas(nodoActual.getHijoDerecho());
        if (nodoActual.esHoja()) {
            return izq + der + 1;
        }
        return izq + der;
    }
    
    // Contar la cantidad de nodos distintos de vacio
    public int contarConInOrden() {
        if (this.esArbolVacio()) {
            return 0;
        }
        int contador = 0;
        Stack<NodoBinario<K, V>> pilaDeNodos = new Stack<>();
        this.apilarParaInOrden(pilaDeNodos, this.raiz);
        while (!pilaDeNodos.isEmpty()) {
            NodoBinario<K, V> nodoActual = pilaDeNodos.pop();
            if (!nodoActual.esHijoIzquierdoVacio() && !nodoActual.esHijoDerechoVacio()) {
                contador++;
            }
            if (!nodoActual.esHijoDerechoVacio()) {
                this.apilarParaInOrden(pilaDeNodos, nodoActual.getHijoDerecho());
            }
        }
        return contador;
    }
    
    @Override
    public K minimo() {
        if (this.esArbolVacio()) {
            return null;
        }
        NodoBinario<K, V> nodoActual = this.raiz;
        NodoBinario<K, V> nodoAnterior = NodoBinario.nodoVacio();
        while (!NodoBinario.esNodoVacio(nodoActual)) {
            nodoAnterior = nodoActual;
            nodoActual = nodoActual.getHijoIzquierdo();
        }
        return nodoAnterior.getClave();
    }
    
    //******************************************************************
    //******************** RECURSIVOS **********************************
    //******************************************************************
    
    // Insertar nodo en un arbol de manera recursiva
    public void Insertar(K claveAInsertar, V valorAInsertar) {
        this.raiz = Insertar(this.raiz, claveAInsertar, valorAInsertar);
    }

    private NodoBinario<K, V> Insertar(NodoBinario<K, V> nodoActual, K claveAInsertar, V valorAInsertar) {
        if (NodoBinario.esNodoVacio(nodoActual)) {
            NodoBinario<K, V> nuevoNodo = new NodoBinario(claveAInsertar, valorAInsertar);
            nodoActual = nuevoNodo;
            return nodoActual;
        }
        K claveActual = nodoActual.getClave();
        if (claveAInsertar.compareTo(claveActual) < 0) {
            NodoBinario<K, V> nodoActualIzquierdo = Insertar(nodoActual.getHijoIzquierdo(), claveAInsertar, valorAInsertar);
            nodoActual.setHijoIzquierdo(nodoActualIzquierdo);
            return nodoActual;
        } else if (claveAInsertar.compareTo(claveActual) > 0) {
            NodoBinario<K, V> nodoActualDerecho = Insertar(nodoActual.getHijoDerecho(), claveAInsertar, valorAInsertar);
            nodoActual.setHijoDerecho(nodoActualDerecho);
            return nodoActual;
        }
        nodoActual.setValor(valorAInsertar);
        return nodoActual;
    }

    // Eliminar un nodo en recursivo
    public V eliminarR(K claveAEliminar) throws ClaveNoExisteException {
        if (claveAEliminar == null) {
            throw new IllegalArgumentException("Clave a eliminar no puede ser nula");
        }
        V valorAEliminar = buscar(claveAEliminar);
        if (valorAEliminar == null) {
            throw new ClaveNoExisteException();
        }
        this.raiz = eliminarR(this.raiz, claveAEliminar);
        return valorAEliminar;
    }

    private NodoBinario<K, V> eliminarR(NodoBinario<K, V> nodoActual, K claveAEliminar) {
        K claveDelNodoActual = nodoActual.getClave();
        if (claveAEliminar.compareTo(claveDelNodoActual) < 0) {
            NodoBinario<K, V> supuestoNuevoHijoIzquierdo = eliminarR(nodoActual.getHijoIzquierdo(), claveAEliminar);
            nodoActual.setHijoIzquierdo(supuestoNuevoHijoIzquierdo);
            return nodoActual;
        }
        if (claveAEliminar.compareTo(claveDelNodoActual) > 0) {
            NodoBinario<K, V> supuestoNuevoHijoDerecho = eliminarR(nodoActual.getHijoDerecho(), claveAEliminar);
            nodoActual.setHijoDerecho(supuestoNuevoHijoDerecho);
            return nodoActual;
        }

        // caso 1
        if (nodoActual.esHoja()) {
            return NodoBinario.nodoVacio();
        }

        // caso 2, opción a
        if (!nodoActual.esHijoIzquierdoVacio() && nodoActual.esHijoDerechoVacio()) {
            return nodoActual.getHijoIzquierdo();
        }

        // caso 2, opción b
        if (nodoActual.esHijoIzquierdoVacio() && !nodoActual.esHijoDerechoVacio()) {
            return nodoActual.getHijoDerecho();
        }

        // caso 3
        NodoBinario<K, V> nodoDelSucesor = obtenerSucesor(nodoActual.getHijoDerecho());
        NodoBinario<K, V> supuestoNuevoHijoDerecho = this.eliminarR(nodoActual.getHijoDerecho(), nodoDelSucesor.getClave());
        nodoActual.setHijoDerecho(supuestoNuevoHijoDerecho);
        nodoActual.setClave(nodoDelSucesor.getClave());
        nodoActual.setValor(nodoDelSucesor.getValor());
        return nodoActual;
    }

    protected NodoBinario<K, V> obtenerSucesor(NodoBinario<K, V> nodoEnTurno) {
        NodoBinario<K, V> nodoAnterior = NodoBinario.nodoVacio();
        while (!NodoBinario.esNodoVacio(nodoEnTurno)) {
            nodoAnterior = nodoEnTurno;
            nodoEnTurno = nodoEnTurno.getHijoIzquierdo();
        }
        return nodoAnterior;
    }

    // Recorrido en PreOrden en recusivo
    public List<K> recorridoPreOrden() {
        List<K> recorrido = new ArrayList();
        recorridoPreOrden(recorrido, this.raiz);
        return recorrido;
    }

    private List<K> recorridoPreOrden(List<K> recorrido, NodoBinario<K, V> nodoActual) {
        if (NodoBinario.esNodoVacio(nodoActual)) {
            return recorrido;
        } else {
            recorrido.add(nodoActual.getClave());
            recorridoPreOrden(recorrido, nodoActual.getHijoIzquierdo());
            recorridoPreOrden(recorrido, nodoActual.getHijoDerecho());
        }
        return recorrido;
    }

    // Recorrido en InOrden en recusivo
    public List<K> recorridoInOrden() {
        List<K> recorrido = new ArrayList();
        recorridoInOrden(recorrido, this.raiz);
        return recorrido;
    }

    private List<K> recorridoInOrden(List<K> recorrido, NodoBinario<K, V> nodoActual) {
        if (NodoBinario.esNodoVacio(nodoActual)) {
            return recorrido;
        } else {
            recorridoInOrden(recorrido, nodoActual.getHijoIzquierdo());
            recorrido.add(nodoActual.getClave());
            recorridoInOrden(recorrido, nodoActual.getHijoDerecho());
        }
        return recorrido;
    }

    private void recorridoInOrden2(List<K> recorrido, NodoBinario<K, V> nodoActual) {
        if (NodoBinario.esNodoVacio(nodoActual)) {
            return;
        }
        recorridoInOrden(recorrido, nodoActual.getHijoIzquierdo());
        recorrido.add(nodoActual.getClave());
        recorridoInOrden(recorrido, nodoActual.getHijoDerecho());
    }

    // Recorrido en PostOrden en recusivo
    public List<K> recorridoPostOrden() {
        List<K> recorrido = new ArrayList();
        recorridoPostOrden(recorrido, this.raiz);
        return recorrido;
    }

    private List<K> recorridoPostOrden(List<K> recorrido, NodoBinario<K, V> nodoActual) {
        if (NodoBinario.esNodoVacio(nodoActual)) {
            return recorrido;
        } else {
            recorridoPostOrden(recorrido, nodoActual.getHijoIzquierdo());
            recorridoPostOrden(recorrido, nodoActual.getHijoDerecho());
            recorrido.add(nodoActual.getClave());
        }
        return recorrido;
    }

    private void recorridoPostOrden2(List<K> recorrido, NodoBinario<K, V> nodoActual) {
        if (NodoBinario.esNodoVacio(nodoActual)) {
            return;
        }
        recorridoPostOrden(recorrido, nodoActual.getHijoIzquierdo());
        recorridoPostOrden(recorrido, nodoActual.getHijoDerecho());
        recorrido.add(nodoActual.getClave());
    }

    // Size en recursivo
    public int sizeR() {
        return sizeR(this.raiz);
    }

    private int sizeR(NodoBinario<K, V> nodoActual) {
        if (NodoBinario.esNodoVacio(nodoActual)) {
            return 0;
        }
        int nroDeNodosPorIzq = sizeR(nodoActual.getHijoIzquierdo());
        int nroDeNodosPorDer = sizeR(nodoActual.getHijoDerecho());
        return nroDeNodosPorIzq + nroDeNodosPorDer + 1;
    }

    // Altura en recursivo
    public int Altura() {
        return Altura(this.raiz);
    }

    protected int Altura(NodoBinario<K, V> nodoActual) {
        if (NodoBinario.esNodoVacio(nodoActual)) {
            return 0;
        }
        int alturaPorIzq = Altura(nodoActual.getHijoIzquierdo());
        int alturaPorDer = Altura(nodoActual.getHijoDerecho());
        return (alturaPorIzq > alturaPorDer) ? alturaPorIzq + 1 : alturaPorDer + 1;
    }

    // Muestra los nodos que estan en un nivel determinado -- Recursivo
    public List<K> mostrarNivel(int nivel) {
        List<K> lista = new ArrayList<>();
        mostrarNivel(lista, this.raiz, nivel, 0);
        return lista;
    }

    private List<K> mostrarNivel(List<K> lista, NodoBinario<K, V> nodoActual, int nivelABuscar, int nivelActual) {
        if (NodoBinario.esNodoVacio(nodoActual)) {
            return lista;
        }
        mostrarNivel(lista, nodoActual.getHijoIzquierdo(), nivelABuscar, nivelActual + 1);
        mostrarNivel(lista, nodoActual.getHijoDerecho(), nivelABuscar, nivelActual + 1);
        if (nivelActual == nivelABuscar) {
            lista.add(nodoActual.getClave());
        }
        return lista;
    }

    
    // Devuelve la cantidad de hijos derechos que tiene un arbol
    public int cantidadHijosDerechos() {
        return cantidadHijosDerechos(this.raiz);
    }
    
    private int cantidadHijosDerechos(NodoBinario<K, V> nodoActual) {
        if (NodoBinario.esNodoVacio(nodoActual)) {
            return 0;
        }
        int HDPorRamaIzquierda = cantidadHijosDerechos(nodoActual.getHijoIzquierdo());
        int HDPorRamaDerecha = cantidadHijosDerechos(nodoActual.getHijoDerecho());
        if (!nodoActual.esHijoDerechoVacio()) {
            return HDPorRamaIzquierda + HDPorRamaDerecha + 1;
        }
        return HDPorRamaIzquierda + HDPorRamaDerecha;
    }
    
    
    /**
     * Instancia un arbol reconstruyendo en base a su recorrido inOrden y (PreOrden o PostOrden).Sí el parametro usandoPreOrden es verdadero, los parametros clavesNoInOrden y valoresNoInOrden 
 tendran el recorrido en preOrden del arbol, caso contrario serán el postOrden
     * @param clavesInOrden
     * @param valoresInOrden
     * @param clavesNoInOrden
     * @param valoresNoInOrden
     * @param esConPreOrden
     */
    public ArbolBinarioBusqueda(List<K> clavesInOrden, List<V> valoresInOrden,
                                List<K> clavesNoInOrden, List<V> valoresNoInOrden,
                                boolean esConPreOrden) {
        
        if (clavesInOrden == null || clavesNoInOrden == null ||
            valoresInOrden == null || valoresNoInOrden == null) {
            throw new IllegalArgumentException("Los parámetros no pueden ser nulos");
        }
        
        if (clavesInOrden.isEmpty() || clavesNoInOrden.isEmpty() ||
            valoresInOrden.isEmpty() || valoresNoInOrden.isEmpty()) {
            throw new IllegalArgumentException("Los parámetros no pueden ser vacios");
        }
        
        if (clavesInOrden.size() != clavesNoInOrden.size() ||
            clavesInOrden.size() != valoresInOrden.size() ||
            clavesNoInOrden.size() != valoresNoInOrden.size()) {
            throw new IllegalArgumentException("Los parámetros no pueden ser listas con diferentes tamaños");
        }
        
        if (esConPreOrden) {
            this.raiz = reconstruirConPreOrden(clavesInOrden, valoresInOrden, clavesNoInOrden, valoresNoInOrden);
        } else {
            this.raiz = reconstruirConPostOrden(clavesInOrden, valoresInOrden, clavesNoInOrden, valoresNoInOrden);
        }
    }

    private NodoBinario<K, V> reconstruirConPreOrden(List<K> clavesInOrden, List<V> valoresInOrden, 
                                    List<K> clavesEnPreOrden, List<V> valoresEnPreOrden) {
        if (clavesInOrden.isEmpty()) {
            return NodoBinario.nodoVacio();
        }
        
        int posicionEnTurnoEnPreOrden = 0;
        K claveDelNodoActual= clavesEnPreOrden.get(posicionEnTurnoEnPreOrden);
        V valorDelNodoActual = valoresEnPreOrden.get(posicionEnTurnoEnPreOrden);
        
        int posicionEnTurnoEnInOrden = this.obtenerPosicionDeClave(claveDelNodoActual, clavesInOrden);
        
        // Para armar la rama izquierda
        List<K> clavesInOrdenPorIzq = clavesInOrden.subList(0, posicionEnTurnoEnInOrden);
        List<V> valoresInOrdenPorIzq = valoresInOrden.subList(0, posicionEnTurnoEnInOrden);
        List<K> clavesPreOrdenPorIzq = clavesEnPreOrden.subList(1, posicionEnTurnoEnInOrden + 1);
        List<V> valoresPreOrdenPorIzq = valoresEnPreOrden.subList(1, posicionEnTurnoEnInOrden + 1);
        
        NodoBinario<K, V> hijoIzqDelNodoActual = reconstruirConPreOrden(clavesInOrdenPorIzq, 
                    valoresInOrdenPorIzq, clavesPreOrdenPorIzq, valoresPreOrdenPorIzq);
        
        // Para armar la rama derecha
        List<K> clavesInOrdenPorDer = clavesInOrden.subList(posicionEnTurnoEnInOrden + 1, clavesInOrden.size());
        List<V> valoresInOrdenPorDer = valoresInOrden.subList(posicionEnTurnoEnInOrden + 1, clavesInOrden.size());
        List<K> clavesPreOrdenPorDer = clavesEnPreOrden.subList(posicionEnTurnoEnInOrden + 1, clavesInOrden.size());
        List<V> valoresPreOrdenPorDer = valoresEnPreOrden.subList(posicionEnTurnoEnInOrden + 1, clavesInOrden.size());
        
        NodoBinario<K, V> hijoDerDelNodoActual = reconstruirConPreOrden(clavesInOrdenPorDer, 
                    valoresInOrdenPorDer, clavesPreOrdenPorDer, valoresPreOrdenPorDer);
        
        // Armando el nodoActual
        NodoBinario<K, V> nodoActual = new NodoBinario<>(claveDelNodoActual, valorDelNodoActual);
        nodoActual.setHijoIzquierdo(hijoIzqDelNodoActual);
        nodoActual.setHijoDerecho(hijoDerDelNodoActual);
        
        return nodoActual;
    }

    private NodoBinario<K, V> reconstruirConPostOrden(List<K> clavesInOrden, List<V> valoresInOrden, 
                                     List<K> clavesEnPostOrden, List<V> valoresEnPostOrden) {
        if (clavesInOrden.isEmpty()) {
            return NodoBinario.nodoVacio();
        }
        
        int posicionEnTurnoEnPostOrden = clavesEnPostOrden.size() - 1;
        K claveDelNodoActual = clavesEnPostOrden.get(posicionEnTurnoEnPostOrden);
        V valorDelNodoActual = valoresEnPostOrden.get(posicionEnTurnoEnPostOrden);
        
        int posicionEnTurnoEnInOrden = obtenerPosicionDeClave(claveDelNodoActual, clavesInOrden);
        
        // Para armar la rama izquierda
        List<K> clavesInOrdenPorIzq = clavesInOrden.subList(0, posicionEnTurnoEnInOrden);
        List<V> valoresInOrdenPorIzq = valoresInOrden.subList(0, posicionEnTurnoEnInOrden);
        List<K> clavesPostOrdenPorIzq = clavesEnPostOrden.subList(0, posicionEnTurnoEnInOrden);
        List<V> valoresPostOrdenPorIzq = valoresEnPostOrden.subList(0, posicionEnTurnoEnInOrden);
        
        NodoBinario<K, V> hijoIzqDelNodoActual = reconstruirConPostOrden(clavesInOrdenPorIzq, 
                        valoresInOrdenPorIzq, clavesPostOrdenPorIzq, valoresPostOrdenPorIzq);
        
        // Para armar la rama derecha
        List<K> clavesInOrdenPorDer = clavesInOrden.subList(posicionEnTurnoEnInOrden+1, clavesInOrden.size());
        List<V> valoresInOrdenPorDer = valoresInOrden.subList(posicionEnTurnoEnInOrden+1, clavesInOrden.size());
        List<K> clavesPostOrdenPorDer = clavesInOrden.subList(posicionEnTurnoEnInOrden, clavesInOrden.size()-1);
        List<V> valoresPostOrdenPorDer = valoresInOrden.subList(posicionEnTurnoEnInOrden, clavesInOrden.size()-1);
        
        NodoBinario<K, V> hijoDerDelNodoActual = reconstruirConPostOrden(clavesInOrdenPorDer, 
                        valoresInOrdenPorDer, clavesPostOrdenPorDer, valoresPostOrdenPorDer);
        
        // Armando el nodoActual
        NodoBinario<K, V> nodoActual = new NodoBinario<>(claveDelNodoActual, valorDelNodoActual);
        nodoActual.setHijoIzquierdo(hijoIzqDelNodoActual);
        nodoActual.setHijoDerecho(hijoDerDelNodoActual);
        
        return nodoActual;
    }
    
    private int obtenerPosicionDeClave(K claveABuscar, List<K> listaDeClaves) {
        for (int i = 0; i < listaDeClaves.size(); i++) {
            K claveActual = listaDeClaves.get(i);
            if (claveActual.compareTo(claveABuscar) == 0) {
                return i;
            }
        }
        return -1;
    }
    
    public JPanel getdibujo() {
        return new ArbolExpresionGrafico(this);
    }
    
}
