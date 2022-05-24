package testmultithreadhelper;

import de.fhws.utility.MultiThreadHelper;
import de.fhws.utility.WarningLogger;
import de.fhws.utility.throwingintefaces.ThrowingRunnable;
import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;

public class TestDoOnCollectionMethods
{
	private static final int AMOUNT_THREADS = Runtime.getRuntime().availableProcessors();

	private static class IntegerWrapper
	{
		int value;

		public IntegerWrapper(int value)
		{
			this.value = value;
		}

		public void makeZero()
		{
			value = 0;
			ThrowingRunnable.unchecked(() -> Thread.sleep(1)).run();
		}
	}

	private static class Measure<T>
	{
		private long time;
		private T value;

		public Measure(long measurement, T value)
		{
			this.time = measurement;
			this.value = value;
		}

		public long getTime()
		{
			return time;
		}

		public T getValue()
		{
			return value;
		}
	}

	private static final ExecutorService executorService = Executors.newFixedThreadPool(AMOUNT_THREADS);

	private final Logger logger = WarningLogger.createWarningLogger(TestDoOnCollectionMethods.class.getName());

	@Test public void testCallConsumerOnCollection()
	{
		Consumer<IntegerWrapper> zeroValueConsumer = i -> i.makeZero();
		int size = 1000;

		long executionTimeMultiThreading = doConsumerMultiThreadTest(zeroValueConsumer, size);

		long executionTimeSingleThreading = getConsumerSingleThreadExecutionTime(zeroValueConsumer, size);

		checkExecutionTimes(executionTimeMultiThreading, executionTimeSingleThreading);
	}

	@Test public void testDoMappingOnCollection()
	{
		int size = 10;
		Function<IntegerWrapper, Integer> unwrap = getUnwrapFunction();

		Measure<Collection<Integer>> measureSingleThreading = doMappingSingleThreaded(size, unwrap);

		Measure<Collection<Integer>> measureMultiThreading = doMappingMultiThreaded(size, unwrap, measureSingleThreading.getValue());

		checkExecutionTimes(measureMultiThreading.getTime(), measureSingleThreading.getTime());
	}

	private Function<IntegerWrapper, Integer> getUnwrapFunction()
	{
		Function<IntegerWrapper, Integer> unwrap = wrapped -> {
			try
			{
				Thread.sleep(150);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			return wrapped.value;
		};
		return unwrap;
	}

	private <T> Measure<Collection<T>> doMappingSingleThreaded(int size, Function<IntegerWrapper, T> mappingFunction)
	{
		return measureTime(() -> createTestCollection(size).stream().map(mappingFunction).collect(Collectors.toList()));
	}

	private <T> Measure<Collection<T>> doMappingMultiThreaded(int size, Function<IntegerWrapper, T> mappingFunction,
		Collection<T> expectedResult)
	{
		Measure<Collection<T>> measure = measureTime(
			() -> MultiThreadHelper.doMappingOnCollection(executorService, createTestCollection(size),
				mappingFunction));

		assertEquals(new HashSet<>(expectedResult), new HashSet<>(measure.getValue()));
		assertEquals(expectedResult.size(), measure.getValue().size());
		return measure;
	}

	private long getConsumerSingleThreadExecutionTime(Consumer<IntegerWrapper> consumer, int size)
	{
		Collection<IntegerWrapper> testCollectionSingleThread = createTestCollection(size);
		Measure executionTimeSingleThreading = measureTime(() -> testCollectionSingleThread.forEach(consumer));
		return executionTimeSingleThreading.getTime();
	}

	private long doConsumerMultiThreadTest(Consumer<IntegerWrapper> consumer, int size)
	{
		final Collection<IntegerWrapper> testCollection = createTestCollection(size);

		Measure measureMultiThreading = measureTime(
			() -> MultiThreadHelper.callConsumerOnCollection(executorService, testCollection, consumer));

		testCollection.forEach(i -> assertEquals(0, i.value));


		return measureMultiThreading.time;
	}

	private void checkExecutionTimes(long executionTimeMultiThreading, long executionTimeSingleThreading)
	{
		if (executionTimeSingleThreading < executionTimeMultiThreading)
			logger.warning(
				"Multi Threading implementation was slower than single Threading by " + (executionTimeMultiThreading
					- executionTimeSingleThreading) + " milliseconds");
	}

	private static Measure<Void> measureTime(Runnable runnable)
	{
		return measureTime(() -> {
			runnable.run();
			return null;
		});
	}

	private static <T> Measure<T> measureTime(Supplier<T> supplier)
	{
		long startTime = System.currentTimeMillis();
		T value = supplier.get();
		long endTime = System.currentTimeMillis();
		return new Measure<T>(endTime - startTime, value);
	}

	private static Collection<IntegerWrapper> createTestCollection(int size)
	{
		return IntStream.range(0, size).boxed().map(i -> new IntegerWrapper(i)).collect(Collectors.toList());
	}

}
