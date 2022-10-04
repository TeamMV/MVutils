package dev.mv.utils.nullHandler;

public class NullHandlerReturn {

    private Object value;

    public NullHandlerReturn(Object value) {
        this.value = value;
    }

    public <T> T value() {
        return (T) value;
    }

    public Object any() {
        return value;
    }

}
