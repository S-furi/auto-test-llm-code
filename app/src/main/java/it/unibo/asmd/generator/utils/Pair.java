package it.unibo.asmd.generator.utils;

import java.util.function.Function;

public class Pair<X, Y> {
    private final X x;
    private final Y y;

    public Pair(X x, Y y) {
        this.x = x;
        this.y = y;
    }

    public X getX() {
        return x;
    }

    public Y getY() {
        return y;
    }

    public <Z> Pair<Z, Y> mapX(Function<X, Z> f) {
        return new Pair<>(f.apply(x), y);
    }

    public <Z> Pair<X, Z> mapY(Function<Y, Z> f) {
        return new Pair<>(x, f.apply(y));
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    public static <X, Y> Pair<X, Y> of(X x, Y y) {
        return new Pair<>(x, y);
    }
}
