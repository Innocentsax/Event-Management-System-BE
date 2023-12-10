package dev.Innocent.EventManagementSystem.exception;

public class InvalidPasswordException extends RuntimeException{
    private String message;

    /**
     * Constructs a new runtime exception with {@code null} as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public InvalidPasswordException(String message) {
        this.message = message;
    }
}
