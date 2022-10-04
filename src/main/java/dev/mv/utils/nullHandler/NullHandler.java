package dev.mv.utils.nullHandler;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class NullHandler<T> {

    private T t;
    private boolean also = true;
    private Object returnValue;

    public NullHandler(T t) {
        this.t = t;
    }

    public NullHandler<T> then(Consumer<T> func) {
        if (t != null && also) {
            func.accept(t);
        }
        return this;
    }

    public NullHandler<T> otherwise(Runnable func) {
        if (t == null || !also) {
            func.run();
        }
        return this;
    }

    public <R> NullHandler<T> thenReturn(Function<T, R> func) {
        if (t != null && also) {
            returnValue = func.apply(t);
        }
        return this;
    }

    public <R> NullHandler<T> otherwiseReturn(Supplier<R> func) {
        if (t == null || !also) {
            returnValue = func.get();
        }
        return this;
    }

    public <R> NullHandler<T> thenReturn(R value) {
        if (t != null && also) {
            returnValue = value;
        }
        return this;
    }

    public <R> NullHandler<T> otherwiseReturn(R value) {
        if (t == null || !also) {
            returnValue = value;
        }
        return this;
    }

    public NullHandler<T> alsoIf(Predicate<T>... tests) {
        for (Predicate test : tests) {
            if (!test.test(t)) {
                also = false;
            }
        }
        return this;
    }

    public NullHandler<T> resetIf() {
        also = true;
        return this;
    }

    public NullHandlerReturn getGenericReturnValue() {
        return new NullHandlerReturn(returnValue);
    }

    public Object getReturnValue() {
        return returnValue;
    }
}
