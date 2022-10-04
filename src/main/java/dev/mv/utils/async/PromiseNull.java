package dev.mv.utils.async;

import lombok.SneakyThrows;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class PromiseNull {

    private volatile boolean done = false;

    private volatile Thread thread;

    public PromiseNull(BiConsumer<ResolverNull, Rejector> function) {
        thread = new Thread(() -> function.accept(() -> {
            thread.interrupt();
            done = true;
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

    public PromiseNull(Consumer<ResolverNull> function) {
        thread = new Thread(() -> function.accept(() -> {
            thread.interrupt();
            done = true;
        }));
        thread.start();
    }

    public void then(Runnable function) {
        Thread then = new Thread(() -> {
            while (true) {
                if (done) {
                    function.run();
                    break;
                }
            }
        });
        then.start();
    }

    public void thenSync(Runnable function) {
        while (true) {
            if (done) {
                function.run();
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
