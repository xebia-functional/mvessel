package android.database;

public class SQLiteException extends android.database.SQLException {

    public SQLiteException() {
        super();
    }

    public SQLiteException(java.lang.String error) {
        super(error);
    }

    public SQLiteException(java.lang.String error, java.lang.Throwable cause) {
        super(error, cause);
    }
}
