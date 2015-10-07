[![Build Status](https://travis-ci.org/47deg/mvessel.svg?branch=master)](https://travis-ci.org/47deg/mvessel)

[![Codacy Badge](https://www.codacy.com/project/badge/3e561d02afd44b8287ab0e84fc66daaf)](https://www.codacy.com/app/47deg/mvessel)

mvessel
=============

**mvessel** is a JDBC driver written in Scala. The main goal is to allow the use of ORMs in Android.

The project is organized in three modules:

* [mockAndroid](mock-android)
  Contains classes from the Android Open Source project without the `final` modifier in order to be mocked. Used only for test purposes.

* [core](core)
  Contains all core classes but relies on some [api classes](core/src/main/scala/com/fortysevendeg/mvessel/api) to do all the work. The main benefit of this module is that is not dependent of the Android API.

* [androidDriver](android-driver)
  Implements the [api classes](core/src/main/scala/com/fortysevendeg/mvessel/api) using [SQLiteDatabase](http://developer.android.com/reference/android/database/sqlite/SQLiteDatabase.html) and derived classes.

Also contains the [android-it](android-it) subdirectory with [instrumentation tests](http://developer.android.com/tools/testing/testing_android.html#Instrumentation) and a subdirectory [samples](samples) with some example projects about how to use.

# How to use in Android

## Import in your project

*SBT*
```scala
libraryDependencies += "com.fortysevendeg" %% "mvessel-android" % "0.1-SNAPSHOT"
```

*Gradle*
```
compile 'com.fortysevendeg:mvessel-android:0.1-SNAPSHOT'
```

*Maven*
```xml
<dependency>
	<groupId>com.fortysevendeg</groupId>
	<artifactId>mvessel-android</artifactId>
	<version>0.1-SNAPSHOT</version>
</dependency>
```

## Create a SQLiteOpenHelper

Android comes with a helper class to manage the creation and version management of our database. This class is [SQLiteOpenHelper](http://developer.android.com/reference/android/database/sqlite/SQLiteOpenHelper.html). We can use this helper class to let Android platform create the database file for us and later access to that file through our JDBC driver or favorite ORM.

Our `SQLiteOpenHelper` could be something like:

*Scala*
```scala
class ContactsOpenHelper(context: Context)
  extends SQLiteOpenHelper(context, "database.db", javaNull, 1) {

  def onCreate(db: SQLiteDatabase) {
    // Use this method to initialize your database
  }

  def onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) = {
		// Use this method to upgrade your database
  }
}
```

*Java*
```java
public class ContactsOpenHelper extends SQLiteOpenHelper {

    public ContactsOpenHelper(Context context) {
        super(context, "database.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       // Use this method to initialize your database
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       // Use this method to upgrade your database
    }

}
```

## Use java.sql classes

If you want to work directly with the `java.sql` classes you could create a method in our `SQLiteOpenHelper` that returns the `java.sql.Connection`.

*Scala*
```scala
def openConnection() = {
	// 1. This statement will create the database and trigger the `onCreate` and `onUpgrade` methods.
	val database: SQLiteDatabase = getReadableDatabase()
	// 2. Register the driver, making available for `java.sql`
	com.fortysevendeg.mvessel.AndroidDriver.register()
	// 3. Open a connection using the path provided by the database
	DriverManager.getConnection("jdbc:sqlite:" + database.getPath)
}
```

*Java*
```java
public Connection openConnection() {
	// 1. This statement will create the database and trigger the `onCreate` and `onUpgrade` methods.
	SQLiteDatabase database = getReadableDatabase();
	// 2. Register the driver, making available for `java.sql`
	com.fortysevendeg.mvessel.AndroidDriver.register();
	// 3. Open a connection using the path provided by the database
	return DriverManager.getConnection("jdbc:sqlite:" + database.getPath);
}
```

**Note:** You should add a method to `close` the connection.

## Use the `Database` class

You can directly create a connection with the `Database` class provided in the library. Again in your `SQLiteOpenHelper`:

*Scala*
```scala
def openDatabase() = {
	// 1. This statement will create the database and trigger the `onCreate` and `onUpgrade` methods.
	val database: SQLiteDatabase = getReadableDatabase()
	// 2. Open the database using the path provided by the database
	new Database[AndroidCursor](
	      databaseFactory = new AndroidDatabaseFactory,
	      name = database.getPath,
	      timeout = 50,
	      retry = 0,
	      flags = 0)
}
```

*Java*
```java
public Database openDatabase() {
	// 1. This statement will create the database and trigger the `onCreate` and `onUpgrade` methods.
	SQLiteDatabase database = getReadableDatabase();
	// 2. Open the database using the path provided by the database
	return new Database<AndroidCursor>(new AndroidDatabaseFactory(), database.getPath(), 50, 0, 0);
}
```

See the [sample in Java](samples/simple-android) and the [sample in Scala](samples/scala-android) for a more detailed instructions.

## Use with Slick

See the [sample with Slick](samples/slick) for a sample of usage.

# Build

This project is built with [SBT](http://www.scala-sbt.org/). To build the artifacts you could use:

`sbt package`

# Collaborate

Actually there are multiples ways to collaborate.

The first place to look is the [GitHub issue tracking](https://github.com/47deg/mvessel/issues). Also, if you found a not listed bug, please submit it with a detailed description.

The [samples](samples) always need to be improved (upgrade versions, make it more attractive). Also, feel free to add new samples.

At last, there are a lot of functionalities [not implemented yet](https://github.com/47deg/mvessel/search?q=notimplemented&type=Code&utf8=%E2%9C%93). The tests are prepared to fail when a new functionality is added so don't forget to add the tests cases along with the new implementation.

# License

The MIT License (MIT)

Copyright (C) 2012 47 Degrees, LLC http://47deg.com hello@47deg.com

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
