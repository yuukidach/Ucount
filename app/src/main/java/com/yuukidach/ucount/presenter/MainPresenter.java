package com.yuukidach.ucount.presenter;

import com.yuukidach.ucount.view.MainView;

public class MainPresenter {
    final private MainView mainView;

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

//    public void updateMonthlyCost() {
//        if (mainView == null) return;
//        mainView.updateMonthlyCost();
//    }
//
//    public void UpdateMonthlyEarn() {
//        if (mainView == null) return;
//        mainView.updateMonthlyEarn();
//    }
}
