package com.yuukidach.ucount.view;

public interface AddItemView {
    void highlightEarnButton();

    void highlightCostButton();

    void setAmount(String numStr);

    void setupTransaction();

    void beginTransaction();

    void endTransaction();

    void useEarnFragment();

    void useCostFragment();

    String getMoney();

    void alarmNoMoneyInput();

    void navigateToDescription();
}
