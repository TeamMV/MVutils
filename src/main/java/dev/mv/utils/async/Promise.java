package dev.mv.utils.async;

import lombok.SneakyThrows;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class Promise<T> {

    private volatile T ret;
    private volatile boolean done = false, threw = false;
    private volatile Thread thread;

    public Promise(BiConsumer<Resolver<T>, Rejector> function) {
        thread = new Thread(() -> function.accept(val -> {
            thread.interrupt();
            ret = val;
            done = true;
        }, new Rejector() {
            @Override
            public void reject(Throwable t) {
                thread.interrupt();
                done = true;
                throwError(t);
            }
            @Override
            public void reject(String s) {
                thread.interrupt();
                done = true;
                throwError(s);
            }
            @Override
            public void reject(String s, Throwable t) {
                thread.interrupt();
                done = true;
                throwError(s, t);
            }
        }));
        thread.start();
    }

    public Promise(Consumer<Resolver<T>> function) {
        thread = new Thread(() -> function.accept(val -> {
            thread.interrupt();
            ret = val;
            done = true;
        }));
        thread.start();
    }

    public PromiseNull then(Consumer<T> consumer) {
        return new PromiseNull((res, rej) -> {
            while (true) {
                if (done) {
                    try {
                        consumer.accept(ret);
                        res.resolve();
                    }
                    catch (Throwable err) {
                        rej.reject(err);
                    }
                    break;
                }
            }
        });
    }

    public <R> Promise<R> then(Function<T, R> function) {
        return new Promise<R>((res, rej) -> {
            while (true) {
                if (done) {
                    try {
                        res.resolve(function.apply(ret));
                    }
                    catch (Throwable err) {
                        rej.reject(err);
                    }
                    break;
                }
            }
        });
    }

    public void thenSync(Consumer<T> consumer) {
        while (true) {
            if (done) {
                consumer.accept(ret);
                break;
            }
        }
    }

    @SneakyThrows
    private void throwError(Throwable cause) {
        throw new PromiseRejectedException(cause);
    }

    @SneakyThrows
    private void throwError(String message) {
        throw new PromiseRejectedException(message);
    }

    @SneakyThrows
    private void throwError(String message, Throwable cause) {
        throw new PromiseRejectedException(message, cause);
    }
}
