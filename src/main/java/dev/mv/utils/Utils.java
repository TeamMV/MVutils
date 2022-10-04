package dev.mv.utils;

import dev.mv.utils.async.Async;
import dev.mv.utils.async.Promise;
import dev.mv.utils.async.PromiseNull;
import dev.mv.utils.nullHandler.NullHandler;

import java.io.File;
import java.util.concurrent.atomic.AtomicReference;

public class Utils {

    public static String getPath(String... dirs) {
        String res = "";

        for (int i = 0; i < dirs.length - 1; i++) {
            res += dirs[i];
            res += File.separator;
        }

        res += dirs[dirs.length - 1];

        return res;
    }

    public static String getInnerPath(String... dirs) {
        String res = File.separator;

        for (int i = 0; i < dirs.length - 1; i++) {
            res += dirs[i];
            res += File.separator;
        }

        res += dirs[dirs.length - 1];

        return res;
    }

    public static double radToDeg(double rad) {
        return rad * (180 / Math.PI);
    }

    public static float radToDeg(float rad) {
        return (float) (rad * (180 / Math.PI));
    }

    public static double degToRad(double deg) {
        return deg * (Math.PI / 180.0);
    }

    public static float degToRad(float deg) {
        return (float) (deg * (Math.PI / 180.0));
    }

    public static int getPercent(int value, int total) {
        return (int) ((float) value / (float) total * 100f);
    }

    public static <T> NullHandler<T> ifNotNull(T t) {
        return new NullHandler<T>(t);
    }

    public static <T> T await(Promise<T> promise) {
        AtomicReference<T> ref = new AtomicReference<T>();
        promise.thenSync(ret -> ref.set(ret));
        return ref.get();
    }

    public static void await(PromiseNull promise) {
        promise.thenSync(() -> {});
    }

    @Async
    public static PromiseNull sleep(int ms) {
        return new PromiseNull((res, rej) -> {
            try {
                Thread.sleep(ms);
                res.resolve();
            } catch (InterruptedException e) {
                rej.reject(e);
            }
        });
    }
}
