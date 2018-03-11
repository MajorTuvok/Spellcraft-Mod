package mt.mcmods.spellcraft.common.exceptions;


import java.util.HashSet;
import java.util.Set;

public class UnknownSpellStateException extends RuntimeException {
    private Set<String> mAvailableStates;
    private String mStateName;

    /**
     * Constructs a new exception with {@code null} as its detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     */
    public UnknownSpellStateException(String stateName, Set<String> availableStates) {
        this("Attempted to access unknown SpellState with name " + stateName + "!", stateName, availableStates);
    }

    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public UnknownSpellStateException(String message, String stateName, Set<String> availableStates) {
        super(message);
        initInfo(stateName, availableStates);
    }

    /**
     * Constructs a new exception with the specified detail message and
     * cause.  <p>Note that the detail message associated with
     * {@code cause} is <i>not</i> automatically incorporated in
     * this exception's detail message.
     *
     * @param message the detail message (which is saved for later retrieval
     *                by the {@link #getMessage()} method).
     * @param cause   the cause (which is saved for later retrieval by the
     *                {@link #getCause()} method).  (A <tt>null</tt> value is
     *                permitted, and indicates that the cause is nonexistent or
     *                unknown.)
     * @since 1.4
     */
    public UnknownSpellStateException(String message, Throwable cause, String stateName, Set<String> availableStates) {
        super(message, cause);
        initInfo(stateName, availableStates);
    }

    /**
     * Constructs a new exception with the specified cause and a detail
     * message of <tt>(cause==null ? null : cause.toString())</tt> (which
     * typically contains the class and detail message of <tt>cause</tt>).
     * This constructor is useful for exceptions that are little more than
     * wrappers for other throwables (for example, PriviligedActionException).
     *
     * @param cause the cause (which is saved for later retrieval by the
     *              {@link #getCause()} method).  (A <tt>null</tt> value is
     *              permitted, and indicates that the cause is nonexistent or
     *              unknown.)
     * @since 1.4
     */
    public UnknownSpellStateException(Throwable cause, String stateName, Set<String> availableStates) {
        super(cause);
        initInfo(stateName, availableStates);
    }

    /**
     * Constructs a new exception with the specified detail message,
     * cause, suppression enabled or disabled, and writable stack
     * trace enabled or disabled.
     *
     * @param message            the detail message.
     * @param cause              the cause.  (A {@code null} value is permitted,
     *                           and indicates that the cause is nonexistent or unknown.)
     * @param enableSuppression  whether or not suppression is enabled
     *                           or disabled
     * @param writableStackTrace whether or not the stack trace should
     *                           be writable
     * @since 1.7
     */
    public UnknownSpellStateException(String message, Throwable cause, String stateName, Set<String> availableStates, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        initInfo(stateName, availableStates);
    }

    public String getStateName() {
        return mStateName;
    }

    public Set<String> getAvailableStates() {
        return new HashSet<>(mAvailableStates);
    }

    private void initInfo(String stateName, Set<String> availableStates) {
        this.mStateName = stateName;
        this.mAvailableStates = availableStates;
    }
}
