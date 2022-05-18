package de.fhws.utility.throwingintefaces;

import java.util.function.Consumer;

@FunctionalInterface
public interface ThrowingConsumer<T, E extends Exception>
{
	void accept(T t) throws E;

	static <T, E extends Exception> Consumer<T> unchecked(ThrowingConsumer<T, E> throwingConsumer){
		return t -> {
			try{
				throwingConsumer.accept(t);
			}
			catch (Exception e){
				throw new RuntimeException(e);
			}
		};


	}
}
