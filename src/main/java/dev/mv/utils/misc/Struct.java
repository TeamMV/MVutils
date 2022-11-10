package dev.mv.utils.misc;

public interface Struct<T> {
    T get();
    void initialize(Object... params);
}
