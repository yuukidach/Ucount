package com.yuukidach.ucount.presenter;

import android.net.Uri;

import com.yuukidach.ucount.model.BookItem;
import com.yuukidach.ucount.model.ImgUtils;
import com.yuukidach.ucount.view.MainView;

import java.util.List;

public class MainPresenter {
    final private MainView mainView;
    final private ImgUtils imgUtils;
    private List<BookItem> bookItemList;

    /**
     * Check if a string only contains numeric character
     * eg. "-1" -> true
     *     "-8df" -> false;
     * @param str
     * @return
     */
    private static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public MainPresenter(MainView mainView, ImgUtils imgUtils) {
        this.mainView = mainView;
        this.imgUtils = imgUtils;
    }

    public void onResume() {
        if (mainView == null) return;

        // TODO: Now due to the bad code structure, the setBookItemRecycler needs to be called in
        // front of hide / update... But it seems it more reasonable to put it behind.
        mainView.setBookItemRecycler();
        mainView.setMainItemRecycler();

        mainView.hideBalance();
        mainView.updateHeaderImg();
        mainView.updateDrawerImg();
        mainView.updateMonthlyEarn();
        mainView.updateMonthlyCost();
    }

    public void onImageLongClick(MainView.ImageType type) {
        mainView.openPicGallery(type);
    }

    public void updateImgUtils(Uri uri, int id) {
        imgUtils.find(id);
        imgUtils.update(uri);
    }

    public void toggleBalanceVisibility(String str) {
        if (isNumeric(str)) mainView.hideBalance();
        else mainView.showBalance();
    }

    public void setNewBook() {
        mainView.setNewBook();
    }
}
