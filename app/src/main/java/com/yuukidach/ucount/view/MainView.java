package com.yuukidach.ucount.view;

import android.media.MediaSession2Service;

import com.yuukidach.ucount.model.BookItem;
import com.yuukidach.ucount.model.MoneyItem;

import java.util.List;

public interface MainView {
    enum ImageType {
        HEADER,
        DRAWER
    }

    void openPicGallery(ImageType type);

    /**
     * Update header image in Main Activity.
     */
    void updateHeaderImg(String uriStr);

    /**
     * Update header image for drawer
     */
    void updateDrawerImg(String uriStr);

    /**
     * Show total balance in top center
     */
    void showBalance(String numStr);

    /**
     * Hide total balance
     */
    void hideBalance();

    /**
     * Update sum of earning in current month
     */
    void updateMonthlyEarn(String numStr);

    /**
     * Update sum of cost in current month
     */
    void updateMonthlyCost(String numStr);

    /**
     * Navigate to addItem activity
     */
    void navigateToAddItem();

    void setMainItemRecycler(List<MoneyItem> list);

    void setBookItemRecycler(List<BookItem> list);

    void setNewBook();
}
