package com.mrcrayfish.controllable.registry;


import java.security.PrivilegedActionException;

/**
 * @author Fernthedev
 * {@link "https://github.com/Fernthedev"}
 */
public class ButtonExistsException extends IllegalArgumentException {

    private String action;
    private int button;

    public String getAction()
    {
        return action;
    }

    public int getButton()
    {
        return button;
    }

    /**
     * Constructs an <code>IllegalArgumentException</code> with no
     * detail message.
     */
    public ButtonExistsException(String action, int button) {
        super();
        this.action = action;
        this.button = button;
    }

    /**
     * Constructs an <code>IllegalArgumentException</code> with the
     * specified detail message.
     *
     * @param s the detail message.
     */
    public ButtonExistsException(String action, int button, String s) {
        super(s);
        this.action = action;
        this.button = button;
    }

    /**
     * Constructs a new exception with the specified detail message and
     * cause.
     *
     * <p>Note that the detail message associated with <code>cause</code> is
     * <i>not</i> automatically incorporated in this exception's detail
     * message.
     *
     * @param message the detail message (which is saved for later retrieval
     *                by the {@link Throwable#getMessage()} method).
     * @param cause   the cause (which is saved for later retrieval by the
     *                {@link Throwable#getCause()} method).  (A <tt>null</tt> value
     *                is permitted, and indicates that the cause is nonexistent or
     *                unknown.)
     * @since 1.5
     */
    public ButtonExistsException(String action, int button, String message, Throwable cause) {
        super(message, cause);
        this.action = action;
        this.button = button;
    }

    /**
     * Constructs a new exception with the specified cause and a detail
     * message of <tt>(cause==null ? null : cause.toString())</tt> (which
     * typically contains the class and detail message of <tt>cause</tt>).
     * This constructor is useful for exceptions that are little more than
     * wrappers for other throwables (for example, {@link
     * PrivilegedActionException}).
     *
     * @param cause the cause (which is saved for later retrieval by the
     *              {@link Throwable#getCause()} method).  (A <tt>null</tt> value is
     *              permitted, and indicates that the cause is nonexistent or
     *              unknown.)
     * @since 1.5
     */
    public ButtonExistsException(String action, int button, Throwable cause) {
        super(cause);
        this.action = action;
        this.button = button;
    }
}
