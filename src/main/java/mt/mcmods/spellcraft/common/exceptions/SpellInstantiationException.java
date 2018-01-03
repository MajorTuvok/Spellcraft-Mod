package mt.mcmods.spellcraft.common.exceptions;

public class SpellInstantiationException extends InstantiationException {
    /**
     * Constructs an {@code InstantiationException} with no detail message.
     */
    public SpellInstantiationException() {
    }

    /**
     * Constructs an {@code InstantiationException} with the
     * specified detail message.
     *
     * @param s the detail message.
     */
    public SpellInstantiationException(String s) {
        super(s);
    }

    /**
     * Constructs an {@code InstantiationException} with the
     * specified detail message.
     *
     * @param s the detail message.
     */
    public SpellInstantiationException(String s, Throwable cause) {
        super(s);
        this.initCause(cause);
    }
}
