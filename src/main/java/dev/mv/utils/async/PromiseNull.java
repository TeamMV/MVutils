package dev.mv.utils.async;

import lombok.SneakyThrows;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

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

    public PromiseNull(Runnable function) {
        thread = new Thread(() -> {
            try {
                function.run();
                done = true;
            } catch (Throwable t) {
                done = true;
                throwError(t);
            }
        });
        thread.start();
    }

    public PromiseNull then(Runnable function) {
        return new PromiseNull((res, rej) -> {
            while (true) {
                if (done) {
                    try {
                        function.run();
                        res.resolve();
                    } catch (Throwable err) {
                        rej.reject(err);
                    }
                    break;
                }
            }
        });
    }

    public <R> Promise<R> then(Supplier<R> function) {
        return new Promise<R>((res, rej) -> {
            while (true) {
                if (done) {
                    try {
                        res.resolve(function.get());
                    } catch (Throwable err) {
                        rej.reject(err);
                    }
                    break;
                }
            }
        });
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
