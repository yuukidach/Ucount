package com.yuukidach.ucount.presenter;

import android.view.View;

import com.yuukidach.ucount.model.Calculator;
import com.yuukidach.ucount.model.MoneyItem;
import com.yuukidach.ucount.view.AddItemView;

import java.util.Date;

public class AddItemPresenter {
    private final AddItemView view;
    private final Calculator calculator = new Calculator();
    private final MoneyItem moneyItem = new MoneyItem();
    private int bookId;
    private String description = "";

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
            MoneyItem.InOutType inOutType = view.getInOutFlag();
//            if (view.getInOutFlag() == MoneyItem.InOutType.COST.ordinal()) {
//                inOutType = MoneyItem.InOutType.COST;
//            } else {
//                inOutType = MoneyItem.InOutType.EARN;
//            }

            moneyItem.setTypeName(view.getTypeName());
            moneyItem.setDate(new Date().toString());
            moneyItem.setMoney(Double.parseDouble(money));
            moneyItem.setBookId(bookId);
            moneyItem.setInOutType(inOutType);
            moneyItem.setDescription(description);
            moneyItem.setTypeImgId(view.getTypeImgResourceName());
            moneyItem.save();

//            moneyItem.addNewMoneyItemIntoStorage(
//                    view.getTypeName(),
//                    Double.parseDouble(money),
//                    bookId,
//                    inOutType,
//                    description
//            );
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

    public void OnNumPadNumClick(View v) {
        String digit = view.getPressedNumPadValue(v);
        boolean state = calculator.inputDigit(digit);
        if (!state) view.alarmCanNotContinueToInput();
        else view.setAmount(calculator.getDecimalFormatMoney());
    }

    public void onNumPadDotClock() {
        if (!calculator.inputDot()) view.alarmAlreadyHasDot();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String str) {
        description = str;
    }
}
