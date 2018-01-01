package com.mt.mcmods.spellcraft.common.exceptions;

public class SpellStateIndexOutOfBoundsException extends IndexOutOfBoundsException {
    /**
     * Constructs an <code>IndexOutOfBoundsException</code> with no
     * detail message.
     */
    public SpellStateIndexOutOfBoundsException() {
        super();
    }

    /**
     * Constructs an <code>IndexOutOfBoundsException</code> with the
     * specified detail message.
     *
     * @param s the detail message.
     */
    public SpellStateIndexOutOfBoundsException(String s) {
        super(s);
    }
}
