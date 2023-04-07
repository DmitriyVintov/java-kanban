package exceptions;

/**
 * Менеджер исключений
 */
public class ManagerSaveException extends Exception{
    public ManagerSaveException(String message) {
        super(message);
    }
}
