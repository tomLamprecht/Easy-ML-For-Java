package de.fhws.networks;

import de.fhws.linearalgebra.Vector;

public interface Network<T> {
    Vector calcOutput(T t);
}
