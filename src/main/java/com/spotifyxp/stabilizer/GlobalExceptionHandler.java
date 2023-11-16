package com.spotifyxp.stabilizer;

import com.spotifyxp.Initiator;
import com.spotifyxp.exception.ExceptionDialog;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.utils.GraphicalMessage;

@SuppressWarnings("CallToPrintStackTrace")
public class GlobalExceptionHandler implements Thread.UncaughtExceptionHandler {
    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        ConsoleLogging.Throwable(throwable);
        throwable.printStackTrace();
        ExceptionDialog.open(throwable);
        if(throwable instanceof OutOfMemoryError) {
            Initiator.past = true;
            GraphicalMessage.sorryErrorExit("Out of memory");
        }
    }
}
