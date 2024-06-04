package Arboles;

import Excepciones.ClaveNoExisteException;
import Excepciones.ExcepcionOrdenInvalido;
import Presentacion.Grafica;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import javax.swing.JPanel;

public class ArbolMVias<K extends Comparable<K>, V> implements IArbolBusqueda<K, V> {

    protected NodoMVias<K, V> raiz;
    protected int orden;
    protected int POSICION_INVALIDA = -1;
    private static final int ORDEN_MINIMO = 3;

    public NodoMVias<K, V> getRaiz() {
        return raiz;
    }

    public ArbolMVias() {
        this.orden = ORDEN_MINIMO;
    }

    public ArbolMVias(int orden) throws ExcepcionOrdenInvalido {
        if (orden < ORDEN_MINIMO) {
            throw new ExcepcionOrdenInvalido();
        }
        this.orden = orden;
    }

    @Override
    public void vaciar() {
        this.raiz = NodoMVias.nodoVacio();
    }

    @Override
    public boolean esArbolVacio() {
        return NodoMVias.esNodoVacio(this.raiz);
    }

    @Override
    public V buscar(K claveABuscar) {
        if (claveABuscar == NodoMVias.datoVacio()) {
            throw new IllegalArgumentException("La clave a buscar no puede ser nula.");
        }
        NodoMVias<K, V> nodoActual = this.raiz;
        while (!NodoMVias.esNodoVacio(nodoActual)) {
            boolean huboCambioDeNodoActual = false;
            for (int i = 0; i < nodoActual.nroDeClavesNoVacias() && !huboCambioDeNodoActual; i++) {
                K claveActual = nodoActual.getClave(i);
                if (claveABuscar.compareTo(claveActual) == 0) {
                    return nodoActual.getValor(i);
                } else if (claveABuscar.compareTo(claveActual) < 0) {
                    if (!nodoActual.esHijoVacio(i)) {
                        nodoActual = nodoActual.getHijo(i);
                        huboCambioDeNodoActual = true;
                    }
                }
            }
            if (!huboCambioDeNodoActual) {
                nodoActual = nodoActual.getHijo(nodoActual.nroDeClavesNoVacias());
            }
        }
        return (V) NodoMVias.datoVacio();
    }

    @Override
    public boolean contiene(K clave) {
        return this.buscar(clave) != (V)NodoMVias.datoVacio();
    }

    @Override
    public void insertar(K claveAInsertar, V valorAInsertar) {
        if (claveAInsertar == null) {
            throw new IllegalArgumentException("La clave a insertar no puede ser nula");
        }
        if (valorAInsertar == null) {
            throw new IllegalArgumentException("El valor asociado a la clave no puede ser nula");
        }
        
        if (this.esArbolVacio()) {
            this.raiz = new NodoMVias<>(this.orden, claveAInsertar, valorAInsertar);
            return;
        }
        NodoMVias<K, V> nodoActual = this.raiz;
        while (!NodoMVias.esNodoVacio(nodoActual)) {
            int posicionDeClaveAInsertar = this.obtenerPosicionDeClave(nodoActual, claveAInsertar);
            if (posicionDeClaveAInsertar != POSICION_INVALIDA) {
                nodoActual.setValor(posicionDeClaveAInsertar, valorAInsertar);
                nodoActual = NodoMVias.nodoVacio();
            } else if (nodoActual.esHoja()) {
                if (nodoActual.estanClavesLlenas()) {
                    int posicionPorDondeBajar = this.obtenerPosicionPorDondeBajar(nodoActual, claveAInsertar);
                    NodoMVias<K, V> nuevoHijo = new NodoMVias<>(this.orden, claveAInsertar, valorAInsertar);
                    nodoActual.setHijo(posicionPorDondeBajar, nuevoHijo);
                } else {
                    this.insertarDatosOrdenadoEnNodo(nodoActual, claveAInsertar, valorAInsertar);
                }
                nodoActual = NodoMVias.nodoVacio();
            } else {
                // En caso de que el nodo actual no sea hoja
                int posicionPorDondeBajar = this.obtenerPosicionPorDondeBajar(nodoActual, claveAInsertar);
                if (nodoActual.esHijoVacio(posicionPorDondeBajar)) {
                    NodoMVias<K, V> nuevoHijo = new NodoMVias<>(this.orden, claveAInsertar, valorAInsertar);
                    nodoActual.setHijo(posicionPorDondeBajar, nuevoHijo);
                    nodoActual = NodoMVias.nodoVacio();
                } else {
                    nodoActual = nodoActual.getHijo(posicionPorDondeBajar);
                }
            }
        }
    }

    protected int obtenerPosicionDeClave(NodoMVias<K, V> nodoActual, K claveABuscar) {
        for (int i = 0; i < nodoActual.nroDeClavesNoVacias(); i++) {
            K claveActual = nodoActual.getClave(i);
            if (claveABuscar.compareTo(claveActual) == 0) {
                return i;
            }
        }
        return POSICION_INVALIDA;
    }

    protected int obtenerPosicionPorDondeBajar(NodoMVias<K, V> nodoActual, K claveABuscar) {
        for (int i = 0; i < nodoActual.nroDeClavesNoVacias(); i++) {
            K claveActual = nodoActual.getClave(i);
            if (claveABuscar.compareTo(claveActual) < 0) {
                return i;
            }
        }
        return nodoActual.nroDeClavesNoVacias();
    }

    protected void insertarDatosOrdenadoEnNodo(NodoMVias<K, V> nodoActual, K claveAInsertar, V valorAInsertar) {
        int posicion = obtenerPosicionPorDondeBajar(nodoActual, claveAInsertar);
        K clave = claveAInsertar;
        V valor = valorAInsertar;
        for (int i = posicion; i < orden - 1; i++) {
            K auxClave = nodoActual.getClave(i);
            V auxValor = nodoActual.getValor(i);
            nodoActual.setClave(i, clave);
            nodoActual.setValor(i, valor);
            clave = auxClave;
            valor = auxValor;
        }
    }

    @Override
    public V eliminar(K claveAEliminar) throws ClaveNoExisteException {
        if (claveAEliminar == null) {
            throw new IllegalArgumentException("Clave a eliminar no puede ser nula");
        }
        V valorAEliminar = buscar(claveAEliminar);
        if (valorAEliminar == null) {
            throw new ClaveNoExisteException();
        }
        this.raiz = eliminar(this.raiz, claveAEliminar);
        return valorAEliminar;
    }

    private NodoMVias<K, V> eliminar(NodoMVias<K, V> nodoActual, K claveAEliminar) {
        for (int i = 0; i < nodoActual.nroDeClavesNoVacias(); i++) {
            K claveActual = nodoActual.getClave(i);
            if (claveAEliminar.compareTo(claveActual) == 0) {
                if (nodoActual.esHoja()) { // Caso 1
                    this.eliminarDatosDeNodo(nodoActual, i);
                    if (nodoActual.nroDeClavesNoVacias() == 0) {
                        return NodoMVias.nodoVacio();
                    }
                    return nodoActual;
                }
                // Si llego acá la clave esta en un nodo no hoja
                // Caso 2 o 3
                K claveDeReemplazo;
                if (this.hayHijosMasAdelante(nodoActual, i)) {
                    // Caso 2
                    claveDeReemplazo = this.buscarClaveSucesoraInOrden(nodoActual, claveAEliminar);
                } else {
                    // Caso 3
                    claveDeReemplazo = this.buscarClavePredecesoraInOrden(nodoActual, claveAEliminar);
                }
                V valorDeReemplazo = this.buscar(claveDeReemplazo);
                nodoActual = eliminar(nodoActual, claveDeReemplazo);
                nodoActual.setClave(i, claveDeReemplazo);
                nodoActual.setValor(i, valorDeReemplazo);
                return nodoActual;
            }
            if (claveAEliminar.compareTo(claveActual) < 0) {
                NodoMVias<K, V> supuestoNuevoHijo = eliminar(nodoActual.getHijo(i), claveAEliminar);
                nodoActual.setHijo(i, supuestoNuevoHijo);
                return nodoActual;
            }
        }
        NodoMVias<K, V> supuestoNuevoHijo = eliminar(nodoActual.getHijo(orden - 1), claveAEliminar);
        nodoActual.setHijo(orden - 1, supuestoNuevoHijo);
        return nodoActual;
    }

    public void eliminarDatosDeNodo(NodoMVias<K, V> nodoActual, int posicion) {
        for (int i = posicion; i < orden - 2; i++) {
            K clave = nodoActual.getClave(i + 1);
            V valor = nodoActual.getValor(i + 1);
            nodoActual.setClave(i, clave);
            nodoActual.setValor(i, valor);
        }
        nodoActual.setClave(orden - 2, (K) NodoMVias.datoVacio());
        nodoActual.setValor(orden - 2, (V) NodoMVias.datoVacio());
    }

    protected boolean hayHijosMasAdelante(NodoMVias<K, V> nodoActual, int i) {
        for (int j = i; j < orden - 1; j++) {
            if (nodoActual.getHijo(j + 1) != NodoMVias.nodoVacio()) {
                return true;
            }
        }
        return false;
    }

    public K buscarClaveSucesoraInOrden(NodoMVias<K, V> nodoActual, K claveAEliminar) {
        int posicion = obtenerPosicionDeClave(nodoActual, claveAEliminar);
        if (!nodoActual.esHijoVacio(posicion + 1)) {
            NodoMVias<K, V> nodoHijo = nodoActual.getHijo(posicion + 1);
            return nodoHijo.getClave(0);
        }
        return nodoActual.getClave(posicion + 1);
    }

    public K buscarClavePredecesoraInOrden(NodoMVias<K, V> nodoActual, K claveAEliminar) {
        int posicion = obtenerPosicionDeClave(nodoActual, claveAEliminar);
        if (!nodoActual.esHijoVacio(posicion)) {
            NodoMVias<K, V> nodoHijo = nodoActual.getHijo(posicion);
            for (int i = 0; i < nodoHijo.nroDeClavesNoVacias(); i++) {
                if (nodoHijo.getClave(i) != NodoMVias.datoVacio()) {
                    posicion = i;
                }
            }
            return nodoHijo.getClave(posicion);
        }
        return nodoActual.getClave(posicion - 1);
    }

    @Override
    public int size() {
        int cantidad = 0;
        if (!esArbolVacio()) {
            Queue<NodoMVias<K, V>> colaDeNodos = new LinkedList<>();
            colaDeNodos.offer(this.raiz);
            while (!colaDeNodos.isEmpty()) {
                NodoMVias<K, V> nodoActual = colaDeNodos.poll();
                cantidad++;

                for (int i = 0; i < nodoActual.nroDeClavesNoVacias(); i++) {
                    if (!nodoActual.esHijoVacio(i)) {
                        colaDeNodos.offer(nodoActual.getHijo(i));
                    }
                }

                if (!nodoActual.esHijoVacio(nodoActual.nroDeClavesNoVacias())) {
                    colaDeNodos.offer(nodoActual.getHijo(nodoActual.nroDeClavesNoVacias()));
                }
            }
        }
        return cantidad;
    }

    @Override
    public int altura() {
        return altura(this.raiz);
    }

    protected int altura(NodoMVias<K, V> nodoActual) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return 0;
        }
        int alturaMayor = 0;
        for (int i = 0; i < orden; i++) {
            int alturaDeHijo = altura(nodoActual.getHijo(i));
            if (alturaDeHijo > alturaMayor) {
                alturaMayor = alturaDeHijo;
            }
        }
        return alturaMayor + 1;
    }

    @Override
    public int nivel() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<K> recorridoEnInOrden() {
        List<K> recorrido = new ArrayList();
        recorridoEnInOrden(recorrido, this.raiz);
        return recorrido;
    }

    private void recorridoEnInOrden(List<K> recorrido, NodoMVias<K, V> nodoActual) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return;
        }
        for (int i = 0; i < nodoActual.nroDeClavesNoVacias(); i++) {
            recorridoEnInOrden(recorrido, nodoActual.getHijo(i));
            recorrido.add(nodoActual.getClave(i));
        }
        recorridoEnInOrden(recorrido, nodoActual.getHijo(nodoActual.nroDeClavesNoVacias()));
    }

    @Override
    public List<K> recorridoEnPreOrden() {
        List<K> recorrido = new ArrayList();
        recorridoEnPreOrden(recorrido, this.raiz);
        return recorrido;
    }

    private void recorridoEnPreOrden(List<K> recorrido, NodoMVias<K, V> nodoActual) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return;
        }
        for (int i = 0; i < nodoActual.nroDeClavesNoVacias(); i++) {
            recorrido.add(nodoActual.getClave(i));
            recorridoEnPreOrden(recorrido, nodoActual.getHijo(i));
        }
        recorridoEnPreOrden(recorrido, nodoActual.getHijo(nodoActual.nroDeClavesNoVacias()));
    }

    @Override
    public List<K> recorridoEnPostOrden() {
        List<K> recorrido = new ArrayList();
        recorridoPostOrden(recorrido, this.raiz);
        return recorrido;
    }

    private void recorridoPostOrden(List<K> recorrido, NodoMVias<K, V> nodoActual) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return;
        }
        recorridoPostOrden(recorrido, nodoActual.getHijo(0));
        for (int i = 0; i < nodoActual.nroDeClavesNoVacias(); i++) {
            recorridoPostOrden(recorrido, nodoActual.getHijo(i + 1));
            recorrido.add(nodoActual.getClave(i));
        }
    }

    @Override
    public List<K> recorridoPorNiveles() {
        List<K> recorrido = new ArrayList<>();
        if (!esArbolVacio()) {
            Queue<NodoMVias<K, V>> colaDeNodos = new LinkedList<>();
            colaDeNodos.offer(this.raiz);
            while (!colaDeNodos.isEmpty()) {
                NodoMVias<K, V> nodoActual = colaDeNodos.poll();

                for (int i = 0; i < nodoActual.nroDeClavesNoVacias(); i++) {
                    recorrido.add(nodoActual.getClave(i));
                    if (!nodoActual.esHijoVacio(i)) {
                        colaDeNodos.offer(nodoActual.getHijo(i));
                    }
                }

                if (!nodoActual.esHijoVacio(nodoActual.nroDeClavesNoVacias())) {
                    colaDeNodos.offer(nodoActual.getHijo(nodoActual.nroDeClavesNoVacias()));
                }
            }
        }
        return recorrido;
    }

    @Override
    public K minimo() {
        if (this.esArbolVacio()) {
            return null;
        }
        NodoMVias<K, V> nodoActual = this.raiz;
        NodoMVias<K, V> nodoAnterior = NodoMVias.nodoVacio();
        while (!NodoMVias.esNodoVacio(nodoActual)) {
            nodoAnterior = nodoActual;
            nodoActual = nodoActual.getHijo(0);
        }
        return nodoAnterior.getClave(0);
    }

    public JPanel getdibujo() {
        return new Grafica((ArbolMVias<Integer, String>) this);
    }

    // Método que roterne la cantidad de datos vacios que hay en un árbol m vias
    public int cantidadDatosVacios() {
        return cantidadDatosVacios(this.raiz);
    }
    
    private int cantidadDatosVacios(NodoMVias<K, V> nodoActual) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return 0;
        }
        
        int cantidad = 0;
        
        for (int i = 0; i < orden - 1; i++) {
            cantidad = cantidad + cantidadDatosVacios(nodoActual.getHijo(i));
            if (nodoActual.esClaveVacia(i)) {
                cantidad++;
            }
        }
        cantidad = cantidad + cantidadDatosVacios(nodoActual.getHijo(orden - 1));
        return cantidad;
    }
    
    // Cantidad de hijos vacios
    public int cantidadHijosVacios() {
        return cantidadHijosVacios(this.raiz);
    }
    
    private int cantidadHijosVacios(NodoMVias<K, V> nodoActual) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return 0;
        }
        
        int cantidad = 0;
        
        for (int i = 0; i < orden - 1; i++) {
            cantidad = cantidad + cantidadHijosVacios(nodoActual.getHijo(i));
            if (nodoActual.esHijoVacio(i)) {
                cantidad++;
            }
        }
        cantidad = cantidad + cantidadHijosVacios(nodoActual.getHijo(orden - 1));
        return cantidad;
    }
    
    // Cantidad de nodos hojas en un árbol
    public int cantidadHojas() {
        return cantidadHojas(this.raiz);
    }
    
    private int cantidadHojas(NodoMVias<K, V> nodoActual) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return 0;
        }
        if (nodoActual.esHoja()) {
            return 1;
        }
        int cantidad = 0;
        for (int i = 0; i < orden; i++) {
            cantidad = cantidad + cantidadHojas(nodoActual.getHijo(i));
        }
        return cantidad;
    }
    
    // Cantidad de nodos hojas en un árbol a partir de un nivel
    public int cantidadHojasDesdeNivel(int nivelBase) {
        return cantidadHojasDesdeNivel(this.raiz, nivelBase, 0);
    }
    
    private int cantidadHojasDesdeNivel(NodoMVias<K, V> nodoActual, int nivelBase, int nivelActual) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return 0;
        }
        if (nivelActual >= nivelBase) {
            if (nodoActual.esHoja()) {
                return 1;
            }
        } else {
            if (nodoActual.esHoja()) {
                return 0;
            }
        }
        int cantidad = 0;
        for (int i = 0; i < orden; i++) {
            cantidad = cantidad + cantidadHojasDesdeNivel(nodoActual.getHijo(i), nivelBase, nivelActual+1);
        }
        return cantidad;
    }
    
    // Cantidad de nodos no hojas en un árbol
    public int cantidadNoHojas() {
        return cantidadNoHojas(this.raiz);
    }
    
    private int cantidadNoHojas(NodoMVias<K, V> nodoActual) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return 0;
        }
        int cantidad = 0;
        if (!nodoActual.esHoja()) {
            cantidad++;
        }
        for (int i = 0; i < orden; i++) {
            cantidad = cantidad + cantidadNoHojas(nodoActual.getHijo(i));
        }
        return cantidad;
    }
    
    /* Metodo que devuelva verdadero si los nodos del nivel n tienen todos 
       sus hijos diferente de vacio */
//    public boolean hijosDiferenteDeVacioDesdeNivel(int nivelBase) {
//        return hijosDiferenteDeVacioDesdeNivel(this.raiz, nivelBase, 0);
//    }
    
//    private boolean hijosDiferenteDeVacioDesdeNivel(NodoMVias<K, V> nodoActual, int nivelBase, int nivelActual) {
//        if (NodoMVias.esNodoVacio(nodoActual)) {
//            return false;
//        }
//        if (nivelActual >= nivelBase) {
//            if (nodoActual.esHoja()) {
//                return 1;
//            }
//        } else {
//            if (nodoActual.esHoja()) {
//                return 0;
//            }
//        }
//        int cantidad = 0;
//        for (int i = 0; i < orden; i++) {
//            hijosDiferenteDeVacioDesdeNivel(nodoActual.getHijo(i), nivelBase, nivelActual+1);
//            if (nodoActual.esHijoVacio(orden)) {
//                return false;
//            }
//        }
//        return true;
//    }
    
    
}
