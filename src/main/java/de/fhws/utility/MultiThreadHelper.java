package de.fhws.utility;

import de.fhws.utility.throwingintefaces.ThrowingFunction;
import de.fhws.utility.throwingintefaces.ThrowingRunnable;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class MultiThreadHelper
{
	/**
	 * Uses the executor Service to call the accept Method of the consumer on every element of the collection
	 * @param executorService provides the needed Threadpool
	 * @param collection collection to iterate over
	 * @param consumer provides accept Method for each element of the collection
	 * @param <T> is the Type of the elements
	 */

	public static <T> void callConsumerOnCollection(ExecutorService executorService, Collection<T> collection, Consumer<T> consumer){

		List<Callable<Void>> calls = collection.stream().
			map( element -> transformToCallableVoid( () -> consumer.accept(element) ) ).collect(Collectors.toList());

		ThrowingRunnable.unchecked( () -> executorService.invokeAll(calls) ).run();
	}

	public static Callable<Void> transformToCallableVoid(Runnable runnable){
		return () -> {
			runnable.run();
			return null;
		};
	}

	public static <T> List<Future<?>> callConsumerOnCollectionNonBlocking(ExecutorService executorService, Collection<T> collection, Consumer<T> consumer){
		 return collection.stream()
			.map(element -> executorService.submit((() -> consumer.accept(element))))
			 .collect(Collectors.toList());
	}



	public static <T, R> Collection<R> doMappingOnCollection(ExecutorService executorService, Collection<T> collection, Function<T, R> mappingFunction){

		return collection.stream()
			.map( element -> executorService.submit( () -> mappingFunction.apply(element)) )
			.map(ThrowingFunction.unchecked(Future::get))
			.collect(Collectors.toList());

	}

	private static void doBlockingUntilAllFuturesAreDone(Collection<Future<?>> futures){
		for (Future<?> future : futures)
			while(!future.isDone());
	}

}
