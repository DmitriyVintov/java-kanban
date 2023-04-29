package exception;

/**
 * Менеджер исключений
 */
public class ManagerException extends RuntimeException {
    public ManagerException(String message) {
        super(message);
    }
}