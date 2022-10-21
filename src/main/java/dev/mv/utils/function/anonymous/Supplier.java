package dev.mv.utils.function.anonymous;

@FunctionalInterface
public interface Supplier<T> extends AnonymousFunction {
    T get();
}
