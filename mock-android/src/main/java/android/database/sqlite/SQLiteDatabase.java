/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.database.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;

import java.io.File;
import java.util.Locale;
import java.util.Map;

public class SQLiteDatabase extends SQLiteClosable {

    public static final int CONFLICT_ROLLBACK = 1;
    public static final int CONFLICT_ABORT = 2;
    public static final int CONFLICT_FAIL = 3;
    public static final int CONFLICT_IGNORE = 4;
    public static final int CONFLICT_REPLACE = 5;
    public static final int CONFLICT_NONE = 0;
    public static final int SQLITE_MAX_LIKE_PATTERN_LENGTH = 50000;
    public static final int OPEN_READWRITE = 0;
    public static final int OPEN_READONLY = 1;
    public static final int NO_LOCALIZED_COLLATORS = 16;
    public static final int CREATE_IF_NECESSARY = 268435456;
    public static final int ENABLE_WRITE_AHEAD_LOGGING = 536870912;
    public static final int MAX_SQL_CACHE_SIZE = 100;

    public enum ConflictAlgorithm {
        ROLLBACK("ROLLBACK"),
        ABORT("ABORT"),
        FAIL("FAIL"),
        IGNORE("IGNORE"),
        REPLACE("REPLACE");

        private final String mValue;
        ConflictAlgorithm(String value) {
            mValue = value;
        }
        public String value() {
            return mValue;
        }
    }

    void addSQLiteClosable(SQLiteClosable closable) { }

    void removeSQLiteClosable(SQLiteClosable closable) { }

    @Override
    protected void onAllReferencesReleased() { }

    static public native int releaseMemory();

    public void setLockingEnabled(boolean lockingEnabled) { }

    private boolean mLockingEnabled = true;

    void onCorruption() { }

    void lock() { }

    private void lockForced() { }

    void unlock() { }

    private void unlockForced() { }

    private void checkLockHoldTime() { }

    public void beginTransaction() {
        beginTransactionWithListener(null /* transactionStatusCallback */);
    }

    public void beginTransactionWithListener(SQLiteTransactionListener transactionListener) { }

    public void endTransaction() { }

    public void setTransactionSuccessful() { }

    public boolean inTransaction() { return false; }

    public boolean isDbLockedByCurrentThread() { return false; }

    public boolean isDbLockedByOtherThreads() { return false; }

    @Deprecated
    public boolean yieldIfContended() { return false; }

    public boolean yieldIfContendedSafely() { return false; }

    public boolean yieldIfContendedSafely(long sleepAfterYieldDelay)  { return false; }

    private boolean yieldIfContendedHelper(boolean checkFullyYielded, long sleepAfterYieldDelay) { return false; }

    public Map<String, String> getSyncedTables() { return null; }

    static private class SyncUpdateInfo {
        SyncUpdateInfo(String masterTable, String deletedTable, String foreignKey) { }
    }

    public interface CursorFactory {
        public Cursor newCursor(SQLiteDatabase db,
                SQLiteCursorDriver masterQuery, String editTable,
                SQLiteQuery query);
    }

    public static SQLiteDatabase openDatabase(String path, CursorFactory factory, int flags) { return null; }

    public static SQLiteDatabase openOrCreateDatabase(File file, CursorFactory factory) { return null; }

    public static SQLiteDatabase openOrCreateDatabase(String path, CursorFactory factory) { return null; }

    public static SQLiteDatabase create(CursorFactory factory) { return null; }

    public void close() { }

    private void closeClosable() { }

    private native void dbclose();

    public int getVersion() { return 0; }

    public void setVersion(int version) { }

    public long getMaximumSize() { return 0; }

    public long setMaximumSize(long numBytes) { return 0; }

    public long getPageSize() { return 0; }

    public void setPageSize(long numBytes) { }

    public void markTableSyncable(String table, String deletedTable) { }

    public void markTableSyncable(String table, String foreignKey, String updateTable) { }

    private void markTableSyncable(String table, String foreignKey, String updateTable, String deletedTable) { }

    void rowUpdated(String table, long rowId) { }

    public static String findEditTable(String tables) { return null; }

    public SQLiteStatement compileStatement(String sql) throws SQLException { return null; }

    public Cursor query(boolean distinct, String table, String[] columns,
            String selection, String[] selectionArgs, String groupBy,
            String having, String orderBy, String limit) { return null; }

    public Cursor queryWithFactory(CursorFactory cursorFactory,
            boolean distinct, String table, String[] columns,
            String selection, String[] selectionArgs, String groupBy,
            String having, String orderBy, String limit) { return null; }

    public Cursor query(String table, String[] columns, String selection,
            String[] selectionArgs, String groupBy, String having,
            String orderBy) { return null; }

    public Cursor query(String table, String[] columns, String selection,
            String[] selectionArgs, String groupBy, String having,
            String orderBy, String limit) { return null; }

    public Cursor rawQuery(String sql, String[] selectionArgs) { return null; }

    public Cursor rawQueryWithFactory(
            CursorFactory cursorFactory, String sql, String[] selectionArgs,
            String editTable) { return null; }

    public Cursor rawQuery(String sql, String[] selectionArgs,
            int initialRead, int maxRead) { return null; }

    public long insert(String table, String nullColumnHack, ContentValues values) { return 0; }

    public long insertOrThrow(String table, String nullColumnHack, ContentValues values)
            throws SQLException  { return 0; }

    public long replace(String table, String nullColumnHack, ContentValues initialValues) { return 0; }

    public long replaceOrThrow(String table, String nullColumnHack,
            ContentValues initialValues) throws SQLException  { return 0; }

    public long insertWithOnConflict(String table, String nullColumnHack,
            ContentValues initialValues, ConflictAlgorithm algorithm) { return 0; }

    public int delete(String table, String whereClause, String[] whereArgs) { return 0; }

    public int update(String table, ContentValues values, String whereClause, String[] whereArgs) { return 0; }

    public int updateWithOnConflict(String table, ContentValues values,
            String whereClause, String[] whereArgs, ConflictAlgorithm algorithm) { return 0; }

    public void execSQL(String sql) throws SQLException { }

    public void execSQL(String sql, Object[] bindArgs) throws SQLException { }

    @Override
    protected void finalize() { }

    private SQLiteDatabase(String path, CursorFactory factory, int flags) { }

    public boolean isReadOnly() { return false; }

    public boolean isOpen() { return false; }

    public boolean needUpgrade(int newVersion) { return false; }

    public final String getPath() { return null; }

    void logTimeStat(boolean read, long begin, long end) { }

    public void setLocale(Locale locale) { }

    private native void dbopen(String path, int flags);

    native void native_execSQL(String sql) throws SQLException;

    native void native_setLocale(String loc, int flags);

    native long lastInsertRow();

    private int lastChangeCount() {
        return -1;
    }
}
