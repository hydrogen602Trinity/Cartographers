package com.ctmrepository.model;

public class Tuple<X, Y> {
    public X x;
    public Y y;

    public Tuple(X x, Y y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @param x
     */
    public void setHead(X x) {
        this.x = x;
    }

    /**
     * @return
     */
    public X head() {
        return x;
    }

    /**
     * @param y
     */
    public void setTail(Y y) {
        this.y = y;
    }

    /**
     * @return
     */
    public Y tail() {
        return y;
    }

    /**
     * @return
     */
    public String toString() {
        return "(" + x.toString() + ", " + y.toString() + ")";
    }
}