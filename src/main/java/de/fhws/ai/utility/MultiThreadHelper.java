package de.fhws.ai.utility;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class MultiThreadHelper
{
	/**
	 * Transforms the Collection to a Stream and calls {@link #callConsumerOnStream(ExecutorService, Stream, Consumer)}
	 * @param executorService provides the needed Threadpool
	 * @param collection collection to iterate over
	 * @param consumer provides accept Method for each element of the collection
	 * @param <T> is the Type of the elements
	 */

	public static <T> void callConsumerOnCollection(ExecutorService executorService, Collection<T> collection, Consumer<T> consumer){
		callConsumerOnStream( executorService, collection.stream(), consumer );
	}


	/**
	 * Uses the executor Service to call the accept Method of the consumer on every element of the stream
	 * @param executorService provides the needed Threadpool
	 * @param stream stream to iterate over
	 * @param consumer provides accept Method for each element of the collection
	 * @param <T> is the Type of the elements
	 */
	public static <T> void callConsumerOnStream( ExecutorService executorService, Stream<T> stream, Consumer<T> consumer){

		//DONT SIMPLFY THOSE 2 LINES
		//Otherwise JVM is forced to process on a single Thread
		List<CompletableFuture<Void>> futures = stream
				.map( element -> CompletableFuture.runAsync( () -> consumer.accept( element ), executorService) )
				.collect( Collectors.toList());

		futures.forEach( CompletableFuture::join );
	}

	/**
	 * Uses the executor Service to create a List based on the {@code supplier} and {@code finalSize}
	 * @param executorService provides the needed Threadpool
	 * @param supplier supplies the elements for the List
	 * @param finalSize the size of the creating List
	 * @param <T> type of the List
	 * @return the created List
	 */
	public static <T> List<T> getListOutOfSupplier(ExecutorService executorService, Supplier<T> supplier, int finalSize){
		List<CompletableFuture<T>> futures = new ArrayList<>();
		for ( int i = 0; i < finalSize; i++ ) {
			futures.add( CompletableFuture.supplyAsync( supplier, executorService) );
		}

		return futures.stream().map( CompletableFuture::join ).collect( Collectors.toList());

	}

	/**
	 * Transforms the given {@link java.lang.Runnable} to a {@link java.util.concurrent.Callable} of the type Void.
	 * @param runnable that should be transformed
	 * @return created Callable
	 */
	public static Callable<Void> transformToCallableVoid(Runnable runnable){
		return () -> {
			runnable.run();
			return null;
		};
	}


}
