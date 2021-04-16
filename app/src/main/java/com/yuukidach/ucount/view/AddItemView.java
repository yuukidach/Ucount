package com.yuukidach.ucount.view;

import android.view.View;

import com.yuukidach.ucount.model.MoneyItem;

public interface AddItemView {
    void highlightEarnButton();

    void highlightCostButton();

    /**
     * Set money amount
     * @param numStr
     */
    void setAmount(String numStr);

    void setupTransaction();

    void beginTransaction();

    void endTransaction();

    void useEarnFragment();

    void useCostFragment();

    /**
     * Get string of money value
     * @return
     */
    String getMoney();

    String getTypeName();

    String getTypeImgResourceName();

    String getPressedNumPadValue(View view);

    MoneyItem.InOutType getInOutFlag();

    void alarmNoMoneyInput();

    void alarmCanNotContinueToInput();

    void alarmAlreadyHasDot();

    void navigateToDescription();
}
