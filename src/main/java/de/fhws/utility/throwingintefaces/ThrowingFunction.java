package de.fhws.utility.throwingintefaces;

import java.util.function.Consumer;
import java.util.function.Function;

public interface ThrowingFunction<T, R, E extends Exception>
{
	R apply(T t) throws E;

	static <T, R, E extends Exception> Function<T, R> unchecked(ThrowingFunction<T,R , E> throwingFunction){
		return t -> {
			try{
				return throwingFunction.apply(t);
			}
			catch (Exception e){
				throw new RuntimeException(e);
			}
		};


	}
}
