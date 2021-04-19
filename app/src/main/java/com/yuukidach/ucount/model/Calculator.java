package com.yuukidach.ucount.model;

import java.text.DecimalFormat;

import static java.lang.Math.max;

public class Calculator {
    String money;
    boolean hasDot;

    public Calculator() {
        clear();
    }

    public void clear() {
        money = "";
        hasDot = false;
    }

    public void setMoney (String money) {
        this.money = money;
    }

    public void setHasDot (boolean hasDot) {
        this.hasDot = hasDot;
    }

    public String getMoney() {
        return money;
    }

    public boolean hasDot() {
        return hasDot;
    }

    public int cntFreePlaceForDecimal() {
        if (!hasDot()) return 2;

        int cnt = 2;
        for (int i = money.length()-1; i >= 0; --i) {
            if (money.charAt(i) == '.') break;;
            cnt--;
        }
        return max(cnt, 0);
    }

    public boolean inputDot() {
        if (!hasDot()) {
            money += '.';
            setHasDot(true);
            return true;
        }
        return false;
    }

    public boolean inputDigit(String digit) {
        if (cntFreePlaceForDecimal() <= 0) return false;
        money += digit;
        return true;
    }

    public String getDecimalFormatMoney() {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return decimalFormat.format(Double.valueOf(money));
    }
}
