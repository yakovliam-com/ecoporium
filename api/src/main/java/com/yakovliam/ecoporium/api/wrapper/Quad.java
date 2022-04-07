package com.yakovliam.ecoporium.api.wrapper;

public class Quad<A, B, C, D> {
    A a;
    B b;
    C c;
    D d;

    public Quad() {
    }

    public Quad(A a, B b, C c, D d) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
    }

    public A a() {
        return a;
    }

    public void a(A a) {
        this.a = a;
    }

    public B b() {
        return b;
    }

    public void b(B b) {
        this.b = b;
    }

    public C c() {
        return c;
    }

    public void c(C c) {
        this.c = c;
    }

    public D d() {
        return d;
    }

    public void d(D d) {
        this.d = d;
    }
}