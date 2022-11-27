package dev.mv.utils.generic;

import lombok.Getter;

public class Pair<T, U> {
    @Getter
    private final T a;
    @Getter
    private final U b;

    public Pair(T a, U b) {
        this.a = a;
        this.b = b;
    }

}
