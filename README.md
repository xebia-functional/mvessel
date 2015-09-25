[![Build Status](https://travis-ci.org/47deg/mvessel.svg?branch=master)](https://travis-ci.org/47deg/mvessel)

[![Codacy Badge](https://www.codacy.com/project/badge/3e561d02afd44b8287ab0e84fc66daaf)](https://www.codacy.com/app/47deg/mvessel)

mvessel
=============

**mvessel** is a JDBC driver written in Scala. The main goal is to be used on Android.

The project is organized in three modules:

* [mockAndroid](mock-android)
Contains classes from the Android Open Source project without the final modifiers in order to be mocked. Used only for test purposes.

* [core](core)
Contains all core classes but relies on some [api classes](core/src/main/scala/com/fortysevendeg/mvessel/api) to do all the work. The main benefit of this module is that is not dependant of the android classes

* [androidDriver](android-driver)
Implements the [api classes](core/src/main/scala/com/fortysevendeg/mvessel/api) using [SQLiteDatabase](http://developer.android.com/reference/android/database/sqlite/SQLiteDatabase.html) and derived classes.

Also contains the [android-it](android-it) subdirectory with [instrumentation tests](http://developer.android.com/tools/testing/testing_android.html#Instrumentation) and a subdirectory [samples](samples) with some example projects about how to use.

# How to use

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

## Use as a JDBC Driver

## Construct the connection

# Build

# Colaborate

# License

The MIT License (MIT)

Copyright (C) 2012 47 Degrees, LLC http://47deg.com hello@47deg.com

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions: 

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE. 