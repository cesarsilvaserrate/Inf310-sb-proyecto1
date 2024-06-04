package Excepciones;

public class ExcepcionOrdenInvalido extends Exception{

    /**
     * Creates a new instance of <code>ExcepcionOrdenInvalido</code> without detail message.
     */
    public ExcepcionOrdenInvalido() {
        super("Árbol con orden inválido");
    }

    /**
     * Constructs an instance of <code>ExcepcionOrdenInvalido</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public ExcepcionOrdenInvalido(String msg) {
        super(msg);
    }
}
