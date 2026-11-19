package progetto.exception;

public class DAOException extends Exception{
    public DAOException(DAOException e) {
        super();
    }

    public DAOException(String message, Throwable cause) {
        super(message, cause);
    }

    public DAOException(String message) {super(message);}
}
