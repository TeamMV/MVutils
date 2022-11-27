package dev.mv.utils.generic;

import lombok.Getter;

public class Triplet<T, U, R> {

    @Getter
    private final T a;
    @Getter
    private final U b;
    @Getter
    private final R c;

    public Triplet(T a, U b, R c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

}
