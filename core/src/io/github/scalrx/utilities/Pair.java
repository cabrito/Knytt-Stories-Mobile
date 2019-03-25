package io.github.scalrx.utilities;

final class Pair<T> {
    private final T x, y;

    public Pair(T x, T y) {
        this.x = x;
        this.y = y;
    }

    public T getX() {
        return x;
    }

    public T getY() {
        return y;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Pair))
            return false;
        if (obj == this)
            return true;

        Pair couple = (Pair) obj;
        return (this.x != null && this.x.equals(couple.x)
                && this.y != null && this.y.equals(couple.y));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((x == null) ? 0 : x.hashCode());
        result = prime * result + ((y == null) ? 0 : y.hashCode());
        return result;
    }
}
