package sam.biblio.web.error;

public class LendingExtensionException extends RuntimeException {
    private String message;

    public LendingExtensionException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
