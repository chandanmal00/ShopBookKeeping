package com.bookkeeping.model;

/**
 * Created by chandanm on 7/22/16.
 */
public class Tuple<T> {

    T first;
    T second;

    public Tuple() {
    }

    public T getFirst() {
        return first;
    }

    public void setFirst(T first) {
        this.first = first;
    }

    public T getSecond() {
        return second;
    }

    public void setSecond(T second) {
        this.second = second;
    }
}
