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
     * @return X
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
     * @return Y
     */
    public Y tail() {
        return y;
    }

    /**
     * @return String
     */
    public String toString() {
        return "(" + x.toString() + ", " + y.toString() + ")";
    }
}