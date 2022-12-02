package de.fhws.ai.utility;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class StreamUtil<T> {

    private Stream<T> stream;

    private StreamUtil( Stream<T> stream ) {
        this.stream = stream;
    }

    public static <T> StreamUtil<T> of( Stream<T> stream ) {
        return new StreamUtil<>( stream );
    }

    public void forEachIndexed( IndexedConsumer<T> consumer ) {
        final AtomicInteger indexCount = new AtomicInteger( );
        stream.forEachOrdered( t -> consumer.accept( t, indexCount.getAndIncrement( ) ) );
    }

    public void forEachWithBefore( T firstBefore, WithBeforeConsumer<T> consumer ) {
        Container<T> container = new Container<>( firstBefore );

        stream.forEachOrdered( t -> {
            consumer.accept( t, container.t );
            container.t = t;
        } );

    }

    @FunctionalInterface
    public interface IndexedConsumer<T> {
        void accept( T t, int index );
    }

    @FunctionalInterface
    public interface WithBeforeConsumer<T> {
        void accept( T current, T before );
    }

    private static class Container<T> {
        private T t;

        private Container( T t ) {
            this.t = t;
        }
    }

}
