package com.yakovliam.ecoporium.api.wrapper;

public class Pair<L, R> {

    L left;
    R right;

    public Pair() {
    }

    public Pair(L left, R right) {
        this.left = left;
        this.right = right;
    }

    public L left() {
        return left;
    }

    public void left(L left) {
        this.left = left;
    }

    public R right() {
        return right;
    }

    public void right(R right) {
        this.right = right;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Pair<?, ?> pair)) {
            return false;
        }

        return pair.left().equals(this.left) && pair.right().equals(this.right);
    }
}