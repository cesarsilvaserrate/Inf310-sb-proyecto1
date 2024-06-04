package Excepciones;

public class ClaveNoExisteException extends Exception {

    /**
     * Creates a new instance of <code>ClaveNoExisteException</code> without detail message.
     */
    public ClaveNoExisteException() {
        super("Clave no existe");
    }

    /**
     * Constructs an instance of <code>ClaveNoExisteException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public ClaveNoExisteException(String msg) {
        super(msg);
    }
}
