package exceptions;

public class AuthorizationException extends SecurityException {
    @Override
    public String getMessage() {
        return "Could not authorize";
    }
}
