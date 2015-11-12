package com.fortysevendeg.mvessel.test;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import com.fortysevendeg.mvessel.Database;
import com.fortysevendeg.mvessel.api.impl.AndroidCursor;
import org.junit.*;
import org.junit.runner.RunWith;

@RunWith(MyRunner.class)
public class DatabaseTest {

    private static MyOpenHelper helper;

    private static final String TABLE_NAME = "test";

    @BeforeClass
    public static void setUp() {
        System.out.println("DatabaseTest.setUp");
        helper = new MyOpenHelper(InstrumentationRegistry.getContext());
        helper.open();
        System.out.println("Database opened");
    }

    @After
    public void afterTest() {
        System.out.println("DatabaseTest.afterTest");
        helper.getDatabase().execSQL(String.format("DROP TABLE %s", TABLE_NAME));
    }

    @AfterClass
    public static void tearDown() {
        System.out.println("DatabaseTest.tearDown");
        if (helper != null) {
            helper.close();
            System.out.println("Database closed");
        }
    }

    @Test
    public void databaseCreationTest() {
        Database<AndroidCursor> database = helper.getDatabase();
        database.execSQL(String.format("CREATE TABLE %s(_id INT, name TEXT, age INT)", TABLE_NAME));
        AndroidCursor cursor = database.rawQuery(String.format("SELECT * FROM %s", TABLE_NAME));
        if (cursor != null) {
            cursor.close();
        }
        Assert.assertNotNull(cursor);
    }

    @Test
    public void databaseInsertTest() {
        Database<AndroidCursor> database = helper.getDatabase();
        database.execSQL(String.format("CREATE TABLE %s(_id INT, name TEXT, age INT)", TABLE_NAME));
        database.execSQL(String.format("INSERT INTO %s VALUES(1, 'test', 30)", TABLE_NAME));
        AndroidCursor cursor = database.rawQuery(String.format("SELECT * FROM %s", TABLE_NAME));
        boolean expected = false;
        if (cursor != null) {
            while (cursor.moveToNext()) {
                System.out.println(String.format("id: %d name: %s age: %d%n", cursor.getInt(0), cursor.getString(1), cursor.getInt(2)));
                expected = true;
            }
            cursor.close();
        }
        Assert.assertTrue(expected);
    }
}