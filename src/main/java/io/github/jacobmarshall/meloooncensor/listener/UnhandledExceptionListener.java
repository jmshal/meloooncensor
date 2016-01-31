package io.github.jacobmarshall.meloooncensor.listener;

import com.bugsnag.BeforeNotify;
import com.bugsnag.Error;

public class UnhandledExceptionListener implements BeforeNotify {

    @Override
    public boolean run (Error error) {
        StackTraceElement[] stackTrace = error.getStackTrace();

        for (StackTraceElement frame : stackTrace) {
            // Only send errors that occur within/include our namespace
            if (frame.getClassName().startsWith("io.github.jacobmarshall.meloooncensor")) {
                return true;
            }
        }

        return false;
    }

}
