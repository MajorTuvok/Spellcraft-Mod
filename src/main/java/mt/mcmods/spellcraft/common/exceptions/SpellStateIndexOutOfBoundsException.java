package mt.mcmods.spellcraft.common.exceptions;

public class SpellStateIndexOutOfBoundsException extends IndexOutOfBoundsException {
    private int mMaxIndex;
    private String mStateName;
    private int mUsedIndex;

    /**
     * Constructs an <code>IndexOutOfBoundsException</code> with no
     * detail message.
     */
    public SpellStateIndexOutOfBoundsException(String stateName, int usedIndex, int maxIndex) {
        this("Attempted to access impossible Index " + usedIndex + " (range is 0-" + maxIndex + ") in SpellState " + stateName + "!", stateName, usedIndex, maxIndex);
    }

    /**
     * Constructs an <code>IndexOutOfBoundsException</code> with the
     * specified detail message.
     *
     * @param s the detail message.
     */
    public SpellStateIndexOutOfBoundsException(String s, String stateName, int usedIndex, int maxIndex) {
        super(s);
        initInfo(stateName, usedIndex, maxIndex);
    }

    /**
     * Constructs an <code>IndexOutOfBoundsException</code> with the
     * specified detail message.
     *
     * @param s the detail message.
     */
    public SpellStateIndexOutOfBoundsException(String s, Throwable throwable, String stateName, int usedIndex, int maxIndex) {
        super(s);
        initCause(throwable);
        initInfo(stateName, usedIndex, maxIndex);
    }

    public int getUsedIndex() {
        return mUsedIndex;
    }

    public int getMaxIndex() {
        return mMaxIndex;
    }

    public String getStateName() {
        return mStateName;
    }

    private void initInfo(String stateName, int usedIndex, int maxIndex) {
        mStateName = stateName;
        mUsedIndex = usedIndex;
        mMaxIndex = maxIndex;
    }
}
