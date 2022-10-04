package dev.mv.utils.async;

import lombok.SneakyThrows;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Promise<T> {

    private volatile T ret;
    private volatile boolean done = false, threw = false;
    private volatile Throwable cause;
    private volatile Thread thread;

    public Promise(BiConsumer<Resolver<T>, Rejector> function) {
        thread = new Thread(() -> function.accept(val -> {
            thread.interrupt();
            done = true;
            ret = val;
        }, new Rejector() {
            @Override
            public void reject(Throwable t) {
                done = true;
                throwError(t);
            }
            @Override
            public void reject(String s) {
                done = true;
                throwError(s);
            }
            @Override
            public void reject(String s, Throwable t) {
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

    public void then(Consumer<T> consumer) {
        Thread then = new Thread(() -> {
            while (true) {
                if (done) {
                    consumer.accept(ret);
                    break;
                }
            }
        });
        then.start();
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
