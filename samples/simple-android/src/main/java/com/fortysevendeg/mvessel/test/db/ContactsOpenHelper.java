package com.fortysevendeg.mvessel.test.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.fortysevendeg.mvessel.Database;

public class ContactsOpenHelper extends SQLiteOpenHelper {

    public static final String TABLE = "contacts";
    public static final String C_ID = "_id";
    public static final String C_NAME = "name";
    public static final String C_AGE = "age";

    private Database database;

    public ContactsOpenHelper(Context context) {
        super(context, "contacts.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.beginTransaction();
        db.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s(%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s INTEGER)", TABLE, C_ID, C_NAME, C_AGE));
        for (int i = 1 ; i <= 100 ; i++) {
            db.execSQL(String.format("INSERT INTO %s(%s, %s) VALUES('%s', %d)", TABLE, C_NAME, C_AGE, "Contact " + i, i + 20));
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Database open() {
        SQLiteDatabase database = getReadableDatabase();
        this.database = new Database(database.getPath(), 50, 0, 0);
        return this.database;
    }

    @Override
    public synchronized void close() {
        try {
            database.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.close();
    }

    public Database getDatabase() {
        return database;
    }

}
