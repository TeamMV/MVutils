package dev.mv.utils.async;

import lombok.SneakyThrows;

public interface Rejector {

    void reject(Throwable t);

    void reject(String s);

    void reject(String s, Throwable t);
    
}
