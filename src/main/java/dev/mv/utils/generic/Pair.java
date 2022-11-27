package dev.mv.utils.generic;

import lombok.Getter;

public class Pair<T, U> {
    public T a;
    public U b;

    public Pair() {
        a = null;
        b = null;
    }
    
    public Pair(T a, U b) {
        this.a = a;
        this.b = b;
    }

}
