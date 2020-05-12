package com.dsource.idc.jellowintl.makemyboard.utility;

public class CustomPair<F,S> {
    private F first;
    private S second;

    public CustomPair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    public F getFirst() {
        return first;
    }

    public void setFirst(F first) {
        this.first = first;
    }

    public S getSecond() {
        return second;
    }

    public void setSecond(S second) {
        this.second = second;
    }
}
