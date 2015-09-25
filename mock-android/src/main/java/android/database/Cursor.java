/*
 * The MIT License (MIT)
 *
 * Copyright (C) 2012 47 Degrees, LLC http://47deg.com hello@47deg.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 */

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
