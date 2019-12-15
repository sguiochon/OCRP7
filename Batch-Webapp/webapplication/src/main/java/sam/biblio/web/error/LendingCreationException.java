package sam.biblio.web.error;

public class LendingCreationException extends RuntimeException {
    private String message;

    public LendingCreationException(String message) {
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
