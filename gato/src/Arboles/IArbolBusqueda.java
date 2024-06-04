package Arboles;

import Excepciones.ClaveNoExisteException;
import java.util.List;

public interface IArbolBusqueda<K extends Comparable<K>, V> {
    
    void insertar(K clave, V valor);
    
    V eliminar(K clave) throws ClaveNoExisteException;
    
    V buscar(K clave);
    
    boolean contiene(K clave);
    
    int size();
    
    int altura();
    
    int nivel();
    
    void vaciar();
    
    boolean esArbolVacio();
    
    List<K> recorridoEnInOrden();
    
    List<K> recorridoEnPreOrden();
    
    List<K> recorridoEnPostOrden();
    
    List<K> recorridoPorNiveles();
    
    K minimo();
    
}
