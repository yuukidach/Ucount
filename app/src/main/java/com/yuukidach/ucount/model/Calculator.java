package com.yuukidach.ucount.model;

public class Calculator {
    String money;
    boolean hasDot;

    Calculator() {
        clear();
    }

    public void clear() {
        money = "";
        hasDot = false;
    }
}
