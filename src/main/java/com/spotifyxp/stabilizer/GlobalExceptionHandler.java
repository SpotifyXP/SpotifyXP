package com.spotifyxp.stabilizer;

import com.spotifyxp.exception.ExceptionDialog;
import com.spotifyxp.logging.ConsoleLogging;

public class GlobalExceptionHandler implements Thread.UncaughtExceptionHandler {
    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        ConsoleLogging.Throwable(throwable);
        throwable.printStackTrace();
        ExceptionDialog.open(throwable);
    }
}
