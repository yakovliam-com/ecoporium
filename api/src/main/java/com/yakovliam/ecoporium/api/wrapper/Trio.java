package com.yakovliam.ecoporium.api.wrapper;

public class Trio<L, M, R> {

    L left;

    R right;

    M mid;

    public Trio() {
    }

    public Trio(L l, M m, R r) {
        this.left = l;
        this.mid = m;
        this.right = r;
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

    public M mid() {
        return mid;
    }

    public void mid(M mid) {
        this.mid = mid;
    }
}
