package dev.mv.utils.generic;

public class Triplet<T, U, R> {

    public T a;
    public U b;
    public R c;

    public Triplet() {
        a = null;
        b = null;
        c = null;
    }

    public Triplet(T a, U b, R c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

}
