package Presentacion;

import Arboles.ArbolMVias;
import Arboles.NodoMVias;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.HashMap;

import java.awt.*;
import java.util.*;
import javax.swing.*;

public class Grafica extends JPanel {

    private ArbolMVias<Integer, String> miArbol;
    private HashMap posicionNodos = null;
    private HashMap subtreeSizes = null;
    private boolean dirty = true;
    private int parent2child = 20, child2child = 30;
    private Dimension empty = new Dimension(0, 0);
    private FontMetrics fm = null;

    /**
     * Constructor de la clase ArbolExpresionGrafico.El constructor permite inicializar los atributos de la clase ArbolExpresionGrafico y llama al método repaint(), que es el encargado de pintar el Arbol.
     *
     * @param miArbol
     */
    public Grafica(ArbolMVias<Integer, String> miArbol) {
        this.miArbol = miArbol;
        this.setBackground(Color.WHITE);
        posicionNodos = new HashMap();
        subtreeSizes = new HashMap();
        dirty = true;
        repaint();
    }

    /**
     * Calcula las posiciones de los respectivos subárboles y de cada nodo que forma parte de ese subárbol, para conocer en que posición van a ir dibujados los rectángulos representativos del árbol de la expresión.
     */
    private void calcularPosiciones() {
        posicionNodos.clear();
        subtreeSizes.clear();
        NodoMVias root = this.miArbol.getRaiz();
        if (root != null) {
            calcularTamañoSubarbol(root);
            calcularPosicion(root, Integer.MAX_VALUE, Integer.MAX_VALUE, 0);
        }
    }

    /**
     * Calcula el tamaño de cada subárbol y lo agrega al objeto subtreeSizes de la clase de tipo HashMap que va a contener la coleccion de todos los subárboles que contiene un arbol.
     *
     * @param n:Objeto de la clase NodoB <T> que se utiliza como referencia calcular el tamaño de cada subárbol.
     * @return Dimension con el tamaño de cada subárbol.
     */
    private Dimension calcularTamañoSubarbol(NodoMVias n) {
        if (n == null) {
            return new Dimension(0, 0);
        }

        Dimension ld = calcularTamañoSubarbol(n.getHijo(0));
        Dimension rd = calcularTamañoSubarbol(n.getHijo(2));

        int h = fm.getHeight() + parent2child + Math.max(ld.height, rd.height);
        int w = ld.width + child2child + rd.width;

        Dimension d = new Dimension(w, h);
        subtreeSizes.put(n, d);

        return d;
    }

    /**
     * Calcula la ubicación de cada nodo de cada subárbol y agrega cada nodo con un objeto de tipo Rectangule que tiene la ubicación y la información específica de dónde va a ser dibujado.
     *
     * @param n: Objeto de tipo NodoB <T> que se utiliza como referencia para calcular la ubicación de cada nodo.
     * @param left: int con alineación y orientación a la izquierda.
     * @param right: int con alineación y orientación a la derecha.
     * @param top: int con el tope.
     *
     *
     *////PASOS PARA DIBUJAR LOS  CUADRITOS
    private void calcularPosicion(NodoMVias n, int left, int right, int top) {
        if (n == null) {
            return;
        }
        Dimension ld = (Dimension) subtreeSizes.get(n.getHijo(0));
        if (ld == null) {
            ld = empty;
        }
        Dimension rd = (Dimension) subtreeSizes.get(n.getHijo(2));
        if (rd == null) {
            rd = empty;
        }
        int center = 0;
        if (right != Integer.MAX_VALUE) {
            center = right - rd.width - child2child / 2;
        } else if (left != Integer.MAX_VALUE) {
            center = left + ld.width + child2child / 2;
        }
        int clave = (int) n.getClave(1);
        if (clave != -9999)//cuande este vacio el hijo derecho
        {
            int width = fm.stringWidth(n.getClave(0) + "  " + n.getClave(1) + " "); //carga el tamaño del cuadrito
            posicionNodos.put(n, new Rectangle(center - width / 2 - 3, top, width + 6, fm.getHeight()));
        } else {
            int width = fm.stringWidth(n.getHijo(0) + "" + ""); //carga el tamaño del cuadrito
            posicionNodos.put(n, new Rectangle(center - width / 2 - 3, top, width + 6, fm.getHeight()));
        }
        calcularPosicion(n.getHijo(0), Integer.MAX_VALUE, center - child2child - 25, top + fm.getHeight() + parent2child);// Coloca las posiciones de los cuadritos
        calcularPosicion(n.getHijo(2), center + child2child + 25, Integer.MAX_VALUE, top + fm.getHeight() + parent2child);
        calcularPosicion(n.getHijo(1), center - 15, Integer.MAX_VALUE, top + fm.getHeight() + parent2child);// Hijo Medio
    }

    /**
     * Dibuja el árbol teniendo en cuenta las ubicaciones de los nodos y los subárboles calculadas anteriormente.
     *
     * @param g: Objeto de la clase Graphics2D que permite realizar el dibujo de las líneas, rectangulos y del String de la información que contiene el Nodo.
     * @param n: Objeto de la clase NodoB <T> que se utiliza como referencia para dibujar el árbol.
     * @param puntox: int con la posición en x desde donde se va a dibujar la línea hasta el siguiente hijo.
     * @param puntoy: int con la posición en y desde donde se va a dibujar la línea hasta el siguiente hijo.
     * @param yoffs: int con la altura del FontMetrics.
     */
    //   DIBUJA EL ARBOL. PERO CON SUS VALORES. SIN EL CUADRITO
    //  DatoDerecho = 2: DatoIzquierdo = 1 : NodoIzq=1 : NodoMedio=2 : NodoDer=3 
    private void dibujarArbol(Graphics2D g, NodoMVias n, int puntox, int puntoy, int yoffs) {
        if (n == null) {
            return;
        }

        Rectangle r = (Rectangle) posicionNodos.get(n);
        g.draw(r);
        int clave = (int) n.getClave(1);
        if (clave != -9999)//cuando el hijo derecho este vacio
        {
            g.drawString("" + n.getClave(0) + " | " + n.getClave(1) + "", r.x + 3, r.y + yoffs);// carga los datos en un cuadro
        } else {
            g.drawString("" + n.getClave(0) + " | " + "", r.x + 3, r.y + yoffs);// carga los datos en un cuadro
        }
        if (puntox != Integer.MAX_VALUE) {
            g.drawLine(puntox, puntoy, (int) (r.x + r.width / 2), r.y);
        }

        dibujarArbol(g, n.getHijo(0), (int) (r.x + r.width - r.width), r.y + r.height, yoffs); //calcula de qe posicion se va a partir la Linea
        dibujarArbol(g, n.getHijo(2), (int) (r.x + r.width), r.y + r.height, yoffs);
        dibujarArbol(g, n.getHijo(1), (int) (r.x + r.width / 2), r.y + r.height, yoffs);  //Hijo Medio 
    }

    /**
     * Sobreescribe el metodo paint y se encarga de pintar todo el árbol.
     *
     * @param g: Objeto de la clase Graphics.
     */
    public void paint(Graphics g) {
        super.paint(g);
        fm = g.getFontMetrics();

        if (dirty) {
            calcularPosiciones();
            dirty = false;
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(getWidth() / 2, parent2child);
        dibujarArbol(g2d, this.miArbol.getRaiz(), Integer.MAX_VALUE, Integer.MAX_VALUE,
                fm.getLeading() + fm.getAscent());
        fm = null;
    }
}
