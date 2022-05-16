package de.fhws.utility;

import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.function.Consumer;

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
		collection.stream()
			.map(element -> executorService.submit(() -> consumer.accept(element)))
			.forEach(ThrowingConsumer.unchecked(Future::get));
	}


}
