package sploit.socialnetwork.shared.exception;

public class InvalidRoleException extends RuntimeException {
    public InvalidRoleException(String message) {
        super("Invalid role: " + message);
    }
}
