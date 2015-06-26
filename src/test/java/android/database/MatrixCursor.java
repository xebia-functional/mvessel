/*
 * Created on May 9, 2012
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package android.database;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MatrixCursor implements Cursor {

    protected String[] columnNames;

    protected int[] columnTypes;

    protected List<Object[]> rows;

    protected int currentPosition;

    public MatrixCursor(String[] columnNames) {
        this.columnNames = columnNames;
        if (columnNames != null) {
            columnTypes = new int[columnNames.length];
            for (int i = 0 ; i < columnTypes.length ; i++) {
                columnTypes[i] = FIELD_TYPE_STRING;
            }
        }
        rows = new ArrayList<>();
        currentPosition = -1;
    }

    public MatrixCursor(String[] columnNames, int[] columnTypes) {
        this.columnNames = columnNames;
        this.columnTypes = columnTypes;
        rows = new ArrayList<>();
        currentPosition = -1;
    }

    public void addRow(Object[] column) {
        rows.add(column);
    }

    @Override
    public int getCount() {
        return rows == null ? 0 : rows.size();
    }

    @Override
    public boolean moveToNext() {
        currentPosition++;
        return true;
    }

    @Override
    public String getString(int i) {
        Object value = rows.get(currentPosition)[i];
        return value == null ? null : value.toString();
    }

    @Override
    public void copyStringToBuffer(int columnIndex, CharArrayBuffer buffer) {

    }

    @Override
    public int getInt(int i) {
        return (int) getLong(i);
    }

    @Override
    public void close() {
        rows = null;
    }

    @Override
    public boolean moveToLast() {
        int previousPosition = currentPosition;
        if (getCount() > 0) {
            currentPosition = getCount() - 1;
        }
        return previousPosition != currentPosition;
    }

    @Override
    public boolean moveToFirst() {
        int previousPosition = currentPosition;
        if (getCount() > 0) {
            currentPosition = 0;
        }
        return previousPosition != currentPosition;
    }

    @Override
    public boolean moveToPrevious() {
        currentPosition--;
        return true;
    }

    @Override
    public int getColumnIndex(String columnName) {
        for (int counter = 0; counter < columnNames.length; counter++) {
            if (columnNames[counter].equals(columnName)) {
                return counter;
            }
        }
        return -1;
    }

    @Override
    public int getColumnIndexOrThrow(String columnName) throws IllegalArgumentException {
        return 0;
    }

    @Override
    public byte[] getBlob(int ci) {
        return null;
    }

    @Override
    public short getShort(int ci) {
        return (short) getInt(ci);
    }

    @Override
    public double getDouble(int ci) {
        return Double.parseDouble(rows.get(currentPosition)[ci].toString());
    }

    @Override
    public float getFloat(int ci) {
        return (float) getDouble(ci);
    }

    @Override
    public long getLong(int ci) {
        return Long.parseLong(rows.get(currentPosition)[ci].toString());
    }

    @Override
    public int getPosition() {
        return currentPosition;
    }

    @Override
    public boolean move(int offset) {
        if (getCount() == 0) {
            return false;
        }
        int previous = currentPosition;
        currentPosition = currentPosition + offset;
        if (currentPosition < -1) {
            currentPosition = -1;
        } else if (currentPosition >= getCount()) {
            currentPosition = getCount();
        }
        return previous != currentPosition;
    }

    @Override
    public boolean isAfterLast() {
        return (currentPosition > 0 && currentPosition >= getCount());
    }

    @Override
    public boolean isBeforeFirst() {
        return (currentPosition < 0);
    }

    @Override
    public boolean isFirst() {
        return (currentPosition == 0);
    }

    @Override
    public boolean isLast() {
        return (currentPosition > 0 && currentPosition == getCount() - 1);
    }

    @Override
    public boolean isNull(int ci) {
        return rows.get(currentPosition)[ci] == null;
    }

    @Override
    public void deactivate() {

    }

    @Override
    public boolean requery() {
        return false;
    }

    @Override
    public boolean isClosed() {
        return rows == null;
    }

    @Override
    public void registerContentObserver(ContentObserver observer) {

    }

    @Override
    public void unregisterContentObserver(ContentObserver observer) {

    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void setNotificationUri(ContentResolver cr, Uri uri) {

    }

    @Override
    public boolean getWantsAllOnMoveCalls() {
        return false;
    }

    @Override
    public Bundle getExtras() {
        return null;
    }

    @Override
    public Bundle respond(Bundle extras) {
        return null;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int i) {
        return columnNames[i];
    }

    @Override
    public String[] getColumnNames() {
        return new String[0];
    }

    @Override
    public boolean moveToPosition(int oldPos) {
        currentPosition = oldPos;
        return true;
    }

    @Override
    public int getType(int ci) {
        if (currentPosition < 0) {
            throw new SQLException("");
        }
        return columnTypes[ci];
    }


}
