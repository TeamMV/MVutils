package dev.mv.utils.async;

import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class Promise<T> {

    private volatile T ret;
    private volatile boolean done = false;
    private volatile Thread thread;

    public Promise(@NotNull BiConsumer<Resolver<T>, Rejector> function) {
        thread = new Thread(() -> {
            function.accept(val -> {
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
            });
            if (!done) {
                done = true;
                ret = null;
            }
        });
        thread.start();
    }

    public Promise(@NotNull Consumer<Resolver<T>> function) {
        thread = new Thread(() -> {
            function.accept(val -> {
                thread.interrupt();
                ret = val;
                done = true;
            });
            if (!done) {
                done = true;
                ret = null;
            }
        });
        thread.start();
    }

    public Promise(@NotNull Supplier<T> function) {
        thread = new Thread(() -> {
            try {
                ret = function.get();
                done = true;
            } catch (Throwable t) {
                done = true;
                throwError(t);
            }
        });
        thread.start();
    }

    public PromiseNull then(@NotNull Consumer<T> consumer) {
        return new PromiseNull((res, rej) -> {
            while (true) {
                if (done) {
                    try {
                        consumer.accept(ret);
                        res.resolve();
                    } catch (Throwable err) {
                        rej.reject(err);
                    }
                    break;
                }
            }
        });
    }

    public <R> Promise<R> then(@NotNull Function<T, R> function) {
        return new Promise<R>((res, rej) -> {
            while (true) {
                if (done) {
                    try {
                        res.resolve(function.apply(ret));
                    } catch (Throwable err) {
                        rej.reject(err);
                    }
                    break;
                }
            }
        });
    }

    public void thenSync(@NotNull Consumer<T> consumer) {
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
