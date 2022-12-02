package testmultithreadhelper;

import de.fhws.ai.utility.MultiThreadHelper;
import de.fhws.ai.utility.WarningLogger;
import de.fhws.ai.utility.throwingintefaces.ThrowingRunnable;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class TestDoOnCollectionMethods {
    private static final int AMOUNT_THREADS = Runtime.getRuntime().availableProcessors();

    private static class IntegerWrapper {
        int value;

        public IntegerWrapper( int value ) {
            this.value = value;
        }

        public void makeZero() {
            value = 0;
            ThrowingRunnable.unchecked( () -> Thread.sleep( 1 ) ).run();
        }
    }

    private static class Measure<T> {
        private final long time;
        private final T value;

        public Measure( long measurement, T value ) {
            this.time = measurement;
            this.value = value;
        }

        public long getTime() {
            return time;
        }

        public T getValue() {
            return value;
        }
    }

    private static final ExecutorService executorService = Executors.newFixedThreadPool( AMOUNT_THREADS );

    private final Logger logger = WarningLogger.createWarningLogger( TestDoOnCollectionMethods.class.getName() );

    @Test
    public void testCallConsumerOnCollection() {
        Consumer<IntegerWrapper> zeroValueConsumer = IntegerWrapper::makeZero;
        int size = 1000;

        long executionTimeMultiThreading = doConsumerMultiThreadTest( zeroValueConsumer, size );

        long executionTimeSingleThreading = getConsumerSingleThreadExecutionTime( zeroValueConsumer, size );

        checkExecutionTimes( executionTimeMultiThreading, executionTimeSingleThreading, "testCallConsumerOnCollection" );
    }

    @Test
    public void testBuildListWithSupplier() {
        final Supplier<Integer> negativeOneSupplier = getDelayedNegativeOneSupplier();

        int size = 1000;

        long executionTimeMultiThreading = doListBuildingMultiThreaded( negativeOneSupplier, size );

        long executionTimeSingleThreading = doListBuildingSingleThreaded( negativeOneSupplier, size );

        checkExecutionTimes( executionTimeMultiThreading, executionTimeSingleThreading, "testBuildListWithSupplier" );
    }

    @NotNull
    private Supplier<Integer> getDelayedNegativeOneSupplier() {
        return () -> {
            ThrowingRunnable.unchecked( () -> Thread.sleep( 1 ) ).run();
            return -1;
        };
    }

    private long doListBuildingSingleThreaded( Supplier<Integer> zeroSupplier, int size ) {
        return measureTime( () -> Stream.generate( zeroSupplier ).limit( size ).collect( Collectors.toList()) ).getTime();
    }

    private long doListBuildingMultiThreaded( Supplier<Integer> zeroSupplier, int size ) {
        Measure<List<Integer>> measure = measureTime( () -> MultiThreadHelper.getListOutOfSupplier( executorService, zeroSupplier, size ) );
        assertTrue( measure.getValue().stream().allMatch( i -> i == -1 ) );
        return measure.getTime();
    }

    private static class TestException extends RuntimeException {
    }

    @Test
    public void testCallOnConsumerExceptionHandling() {
        Consumer<Integer> exceptionConsumer = ( i ) -> {
            throw new TestException();
        };
        try {
            MultiThreadHelper.callConsumerOnStream( executorService, IntStream.range( 0, 5 ).boxed(), exceptionConsumer );
            fail();
        } catch ( Exception e ) {
            assertTrue( e.getCause() instanceof TestException );
        }
    }


    private long getConsumerSingleThreadExecutionTime( Consumer<IntegerWrapper> consumer, int size ) {
        Collection<IntegerWrapper> testCollectionSingleThread = createTestCollection( size );
        Measure<Void> executionTimeSingleThreading = measureTime( () -> testCollectionSingleThread.forEach( consumer ) );
        return executionTimeSingleThreading.getTime();
    }

    private long doConsumerMultiThreadTest( Consumer<IntegerWrapper> consumer, int size ) {
        final Collection<IntegerWrapper> testCollection = createTestCollection( size );

        Measure<Void> measureMultiThreading = measureTime(
                () -> MultiThreadHelper.callConsumerOnCollection( executorService, testCollection, consumer ) );

        testCollection.forEach( i -> assertEquals( 0, i.value ) );


        return measureMultiThreading.time;
    }

    private void checkExecutionTimes( long executionTimeMultiThreading, long executionTimeSingleThreading, String methodName ) {
        if ( executionTimeSingleThreading < executionTimeMultiThreading )
            logger.warning(
                    "Multi Threading implementation in " + methodName + " was slower than single Threading by " + ( executionTimeMultiThreading
                            - executionTimeSingleThreading ) + " milliseconds" );
    }

    private static Measure<Void> measureTime( Runnable runnable ) {
        return measureTime( () -> {
            runnable.run();
            return null;
        } );
    }

    private static <T> Measure<T> measureTime( Supplier<T> supplier ) {
        long startTime = System.currentTimeMillis();
        T value = supplier.get();
        long endTime = System.currentTimeMillis();
        return new Measure<>( endTime - startTime, value );
    }

    private static Collection<IntegerWrapper> createTestCollection( int size ) {
        return IntStream.range( 0, size ).boxed().map( IntegerWrapper::new ).collect( Collectors.toList() );
    }

}
