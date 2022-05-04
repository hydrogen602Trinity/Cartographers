package com.ctmrepository.model;

public class Tuple<X, Y> {
    public X x;
    public Y y;

    public Tuple(X x, Y y) {
        this.x = x;
        this.y = y;
    }

    public void setHead(X x) {
        this.x = x;
    }

    public X head() {
        return x;
    }

    public void setTail(Y y) {
        this.y = y;
    }

    public Y tail() {
        return y;
    }

    public String toString() {
        return "(" + x.toString() + ", " + y.toString() + ")";
    }
}