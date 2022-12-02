package de.fhws.ai.utility.throwingintefaces;

@FunctionalInterface
public interface ExceptionPrintingRunnable<E extends Exception> {

    void run() throws E;

    static <E extends Exception> Runnable printException( ExceptionPrintingRunnable<E> exceptionRunnable ) {
        return () -> {
            try {
                exceptionRunnable.run();
            } catch ( Exception e ) {
                e.printStackTrace();
            }

        };
    }

}