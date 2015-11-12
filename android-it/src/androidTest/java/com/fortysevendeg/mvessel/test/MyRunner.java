package com.fortysevendeg.mvessel.test;

import android.os.Bundle;
import android.support.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import android.support.test.internal.util.AndroidRunnerParams;
import android.support.test.runner.AndroidJUnitRunner;
import org.junit.runners.model.InitializationError;

public class MyRunner extends AndroidJUnit4ClassRunner {

    public MyRunner(Class<?> klass) throws InitializationError {
        super(klass, new AndroidRunnerParams(new AndroidJUnitRunner(), new Bundle(), false, 1000, false));
    }
}