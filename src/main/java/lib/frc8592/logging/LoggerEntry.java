package lib.frc8592.logging;

import java.util.function.Supplier;

public class LoggerEntry<T> {
    private Supplier<T> data;

    public LoggerEntry(Supplier<T> data) {
        this.data = data;
    }

    public Class<?> getDataClass() {
        return data.get().getClass();
    }

    public double getDouble() {
        return (double)data.get();
    }

    public boolean getBoolean() {
        return (boolean)data.get();
    }

    public String getString() {
        return data.get().toString();
    }

    public T getEnum() {
        return (T)data.get();
    }

    public <E extends Enum<E>> int getEnumOrdinal() {
        return ((E)data.get()).ordinal();
    }

    public String getEnumName() {
        return getEnum().toString();
    }
}