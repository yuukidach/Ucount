package com.yuukidach.ucount.presenter;

import com.yuukidach.ucount.model.BookItem;
import com.yuukidach.ucount.view.MainView;

import java.util.List;

public class MainPresenter {
    final private MainView mainView;
    private List<BookItem> bookItemList;

    private static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public MainPresenter(MainView mainView) {
        this.mainView = mainView;
    }

    public void onResume() {
        if (mainView == null) return;

        // TODO: Now due to the bad code structure, the setBookItemRecycler needs to be called in
        // front of hide / update... But it seems it more reasonable to put it behind.
        mainView.setMainItemRecycler();
        mainView.setBookItemRecycler();

        mainView.hideBalance();
        mainView.updateHeaderImg();
        mainView.updateDrawerImg();
        mainView.updateMonthlyEarn();
        mainView.updateMonthlyCost();
    }

    public void toggleBalanceVisibility(String str) {
        if (isNumeric(str)) mainView.hideBalance();
        else mainView.showBalance();
    }
}
