package android.database;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;

public interface Cursor {

    int FIELD_TYPE_NULL = 0;
    int FIELD_TYPE_INTEGER = 1;
    int FIELD_TYPE_FLOAT = 2;
    int FIELD_TYPE_STRING = 3;
    int FIELD_TYPE_BLOB = 4;

    int getCount();

    boolean moveToNext();

    String getString(int i);

    void copyStringToBuffer(int columnIndex, CharArrayBuffer buffer);

    int getInt(int i);

    void close();

    boolean moveToLast();

    boolean moveToFirst();

    boolean moveToPrevious();

    int getColumnIndex(String columnName);

    int getColumnIndexOrThrow(String columnName) throws IllegalArgumentException;

    byte[] getBlob(int ci);

    short getShort(int ci);

    double getDouble(int ci);

    float getFloat(int ci);

    long getLong(int ci);

    int getPosition();

    boolean move(int offset);

    boolean isAfterLast();

    boolean isBeforeFirst();

    boolean isFirst();

    boolean isLast();

    boolean isNull(int ci);

    void deactivate();

    boolean requery();

    boolean isClosed();

    void registerContentObserver(ContentObserver observer);

    void unregisterContentObserver(ContentObserver observer);

    void registerDataSetObserver(DataSetObserver observer);

    void unregisterDataSetObserver(DataSetObserver observer);

    void setNotificationUri(ContentResolver cr, Uri uri);

    boolean getWantsAllOnMoveCalls();

    Bundle getExtras();

    Bundle respond(Bundle extras);

    int getColumnCount();

    String getColumnName(int i);

    String[] getColumnNames();

    boolean moveToPosition(int oldPos);

    int getType(int ci);
}
