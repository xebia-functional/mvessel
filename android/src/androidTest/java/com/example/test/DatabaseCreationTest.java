package com.example.test;

import android.database.Cursor;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import com.fortysevendeg.mvessel.Database;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class DatabaseCreationTest {

    private static MyOpenHelper helper;

    @BeforeClass
    public static void setUp() {
        System.out.println("DatabaseCreationTest.setUp");
        helper = new MyOpenHelper(InstrumentationRegistry.getContext());
        helper.open();
        System.out.println("Database opened");
    }

    @AfterClass
    public static void tearDown() {
        System.out.println("DatabaseCreationTest.tearDown");
        if (helper != null) {
            helper.close();
            System.out.println("Database closed");
        }
    }

    @Test
    public void databaseCreationTest() {
        Database database = helper.getDatabase();
        database.execSQL("CREATE TABLE test(_id INT, name TEXT, age INT)");
        database.execSQL("INSERT INTO test VALUES(1, 'test', 30)");
        Cursor cursor = database.rawQuery("SELECT * FROM test");
        boolean expected = false;
        while (cursor.moveToNext()) {
            System.out.println(String.format("id: %d name: %s age: %d%n", cursor.getInt(0), cursor.getString(1), cursor.getInt(2)));
            expected = true;
        }
        cursor.close();
        Assert.assertTrue(expected);
    }
}