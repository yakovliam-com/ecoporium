package net.ecoporium.ecoporium.api.wrapper;

public class Pair<L, R> {

    L left;
    R right;

    public Pair() {
    }

    public Pair(L left, R right) {
        this.left = left;
        this.right = right;
    }

    public L getLeft() {
        return left;
    }

    public void setLeft(L left) {
        this.left = left;
    }

    public R getRight() {
        return right;
    }

    public void setRight(R right) {
        this.right = right;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Pair<?, ?> pair)) {
            return false;
        }

        return pair.getLeft().equals(this.left) && pair.getRight().equals(this.right);
    }
}