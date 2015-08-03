package android.database;

public class SQLException extends java.lang.RuntimeException {

    public SQLException() {
        super();
    }

    public SQLException(java.lang.String error) {
        super(error);
    }

    public SQLException(java.lang.String error, java.lang.Throwable cause) {
        super(error, cause);
    }
}
