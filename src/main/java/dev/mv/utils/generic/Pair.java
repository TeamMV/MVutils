package dev.mv.utils.generic;

import lombok.Getter;

public class Pair<T, U> {
    public final T a;
    public final U b;

    public Pair(T a, U b) {
        this.a = a;
        this.b = b;
    }

}
