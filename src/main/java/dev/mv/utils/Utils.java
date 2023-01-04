package dev.mv.utils;

import dev.mv.utils.async.Promise;
import dev.mv.utils.async.PromiseNull;
import dev.mv.utils.nullHandler.NullHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
        return rad * 57.29577951308232;
    }

    public static float radToDeg(float rad) {
        return (float) (rad * 57.29577951308232);
    }

    public static double degToRad(double deg) {
        return deg * 0.017453292519943295;
    }

    public static float degToRad(float deg) {
        return (float) (deg * 0.017453292519943295);
    }

    public static float getPercent(float value, int total) {
        return ((float) value / (float) total * 100f);
    }

    public static int getValue(float percentage, int total) {
        return (int) (percentage / 100f * (float) total);
    }

    public static boolean isAnyOf(Object obj, Object... objs) {
        for (Object o : objs) {
            if (obj.equals(o)) {
                return true;
            }
        }
        return false;
    }

    public static <T> List<T> merge(List<T>... lists) {
        List<T> mergedList = new ArrayList<>();
        for (List<T> list : lists) {
            mergedList.addAll(list);
        }
        return mergedList;
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
        promise.thenSync(() -> {
        });
    }

    public static PromiseNull sleep(int ms) {
        return new PromiseNull((res, rej) -> {
            try {
                Thread.sleep(ms);
            } catch (InterruptedException e) {
                rej.reject(e);
            }
        });
    }

    public static float[] toPrimitive(Float[] array) {
        float[] ret = new float[array.length];
        for (int i = 0; i < array.length; i++) {
            ret[i] = array[i].floatValue();
        }
        return ret;
    }

    public static double[] toPrimitive(Double[] array) {
        double[] ret = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            ret[i] = array[i].doubleValue();
        }
        return ret;
    }

    public static int[] toPrimitive(Integer[] array) {
        int[] ret = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            ret[i] = array[i].intValue();
        }
        return ret;
    }

    public static long[] toPrimitive(Long[] array) {
        long[] ret = new long[array.length];
        for (int i = 0; i < array.length; i++) {
            ret[i] = array[i].longValue();
        }
        return ret;
    }

    public static byte[] toPrimitive(Byte[] array) {
        byte[] ret = new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            ret[i] = array[i].byteValue();
        }
        return ret;
    }

    public static char[] toPrimitive(Character[] array) {
        char[] ret = new char[array.length];
        for (int i = 0; i < array.length; i++) {
            ret[i] = array[i].charValue();
        }
        return ret;
    }

    public static boolean[] toPrimitive(Boolean[] array) {
        boolean[] ret = new boolean[array.length];
        for (int i = 0; i < array.length; i++) {
            ret[i] = array[i].booleanValue();
        }
        return ret;
    }

    public static short[] toPrimitive(Short[] array) {
        short[] ret = new short[array.length];
        for (int i = 0; i < array.length; i++) {
            ret[i] = array[i].shortValue();
        }
        return ret;
    }

    public static <T> T[] array(Collection<T> collection) {
        return (T[]) collection.toArray();
    }

    public static boolean isCharLetterOrDigit(char c) {
        return (c >= 'a' && c <= 'z') ||
            (c >= 'A' && c <= 'Z') ||
            (c >= '0' && c <= '9');
    }

    public static boolean isCharAscii(char c) {
        return c <= 255;
    }

    public static int clamp(int min, int value, int max) {
        return Math.min(max, Math.max(min, value));
    }
}
