package dev.mv.utils.generic;

import lombok.Getter;

public class Triplet<T, U, R> {

    public final T a;
    public final U b;
    public final R c;

    public Triplet(T a, U b, R c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

}
