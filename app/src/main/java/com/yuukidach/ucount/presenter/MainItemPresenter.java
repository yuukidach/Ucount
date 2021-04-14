package com.yuukidach.ucount.presenter;

import com.yuukidach.ucount.view.MainItemView;

public class MainItemPresenter {
    private MainItemView mainItemView;

    public MainItemPresenter(MainItemView mainItemView) {
        this.mainItemView = mainItemView;
    }

    public void onResume() {
        mainItemView.init();
    }
}
