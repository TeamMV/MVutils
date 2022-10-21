package dev.mv.utils.function;

public class IllegalFunctionTypeException extends Exception {
    public IllegalFunctionTypeException(String s) {
        super(s);
    }

    public IllegalFunctionTypeException() {
        super();
    }

    public IllegalFunctionTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalFunctionTypeException(Throwable cause) {
        super(cause);
    }

    protected IllegalFunctionTypeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
