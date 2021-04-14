package com.yuukidach.ucount.view;

public interface MainView {
    /**
     * Update header image in Main Activity.
     */
    void updateHeaderImg();

    /**
     * Update header image for drawer
     */
    void updateDrawerImg();

    /**
     * Show total balance in top center
     */
    void showBalance();

    /**
     * Hide total balance
     */
    void hideBalance();

    /**
     * Update sum of earning in current month
     */
    void updateMonthlyEarn();

    /**
     * Update sum of cost in current month
     */
    void updateMonthlyCost();

    /**
     * Navigate to addItem activity
     */
    void navigateToAddItem();

    void setMainItemRecycler();

    void setBookItemRecycler();

    void setNewBook();
}
