package com.yuukidach.ucount.presenter;

import com.yuukidach.ucount.model.Calculator;
import com.yuukidach.ucount.model.MoneyItem;
import com.yuukidach.ucount.view.AddItemView;

public class AddItemPresenter {
    AddItemView view;
    Calculator calculator;
    MoneyItem moneyItem = new MoneyItem();
    int bookId;

    public AddItemPresenter(AddItemView view, int bookId) {
        this.view = view;
        this.bookId = bookId;
    }

    public void onCreate() {
        view.setAmount("0.00");
        view.setupTransaction();
    }

    public void onAddEarnButtonClick() {
        view.beginTransaction();
        view.highlightEarnButton();
        view.useEarnFragment();
        view.endTransaction();
    }

    public void onAddCostButtonClick() {
        view.beginTransaction();
        view.highlightCostButton();
        view.useCostFragment();
        view.endTransaction();
    }

    public void onAddFinishButtonClick() {
        String money = view.getMoney();
        if (money.equals("0.00")) {
            view.alarmNoMoneyInput();
        } else {
            moneyItem.addNewMoneyItemIntoStorage(name, );
        }
        calculator.clear();
    }

    public void onClearButtonClick() {
        calculator.clear();
        view.setAmount("0.00");
    }

    public void onDescriptionButtonClick() {
        view.navigateToDescription();
    }
}
