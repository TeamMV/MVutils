package dev.mv.utils.async;

public interface Rejector {

    void reject(Throwable t);

    void reject(String s);

    void reject(String s, Throwable t);

}
