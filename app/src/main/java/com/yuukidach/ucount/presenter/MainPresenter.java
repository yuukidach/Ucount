package com.yuukidach.ucount.presenter;

import android.net.Uri;

//import com.yuukidach.ucount.GlobalVariables;
import com.yuukidach.ucount.model.BookItem;
import com.yuukidach.ucount.model.ImgUtils;
import com.yuukidach.ucount.model.MoneyItem;
import com.yuukidach.ucount.view.MainView;
import com.yuukidach.ucount.view.BookItemViewHolder;

import org.litepal.LitePal;

import java.text.DecimalFormat;
import java.util.List;

public class MainPresenter {
    final private MainView mainView;
    final private ImgUtils imgUtils;

    final DecimalFormat decimalFormat = new DecimalFormat("0.00");
    private int curBookId;
//    private List<BookItem> bookItemList;

    /**
     * Check if a string only contains numeric character
     * eg. "-1" -> true
     *     "-8df" -> false;
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

//        mainView.setBookItemRecycler();
        updateBookItemView(0);
        updateMoneyItemView();
//        mainView.setMainItemRecycler();

        mainView.hideBalance();
        // transfer enum to int, then get the string
        mainView.updateHeaderImg(imgUtils.getUriStr(MainView.ImageType.HEADER.ordinal()));
        mainView.updateDrawerImg(imgUtils.getUriStr(MainView.ImageType.DRAWER.ordinal()));
        updateMonthlyEarn();
        updateMonthlyCost();

    }

    public void onImageLongClick(MainView.ImageType type) {
        mainView.openPicGallery(type);
    }

    private void showBalance() {
        BookItem book = LitePal.find(BookItem.class, curBookId);
        String sumStr = decimalFormat.format(book.getSum());

        mainView.showBalance(sumStr);
    }

    public void onShowBalanceClick(String str) {
        if (isNumeric(str)) mainView.hideBalance();
        else showBalance();
    }

    public void updateImgUtils(Uri uri, int id) {
        imgUtils.find(id);
        imgUtils.update(uri);
    }

    public void updateMonthlyEarn() {
        BookItem book = LitePal.find(BookItem.class, curBookId);
        String str = decimalFormat.format(book.getEarnSum());

        mainView.updateMonthlyEarn(str);
    }

    public void updateMonthlyCost() {
        BookItem book = LitePal.find(BookItem.class, curBookId);
        String str = decimalFormat.format(book.getCostSum());

        mainView.updateMonthlyCost(str);
    }

    public void updateBookItemView(int position) {
        List<BookItem> bookItems = LitePal.findAll(BookItem.class);

        if (bookItems.isEmpty()) {
            BookItem bookItem = new BookItem();
            bookItem.addNewBookIntoStorage(0, "Default");
            bookItems = LitePal.findAll(BookItem.class);
        }

        // mark current book's ID
        curBookId = bookItems.get(position).getId();
        mainView.setBookItemRecycler(bookItems);
    }

    public int getCurBookId() {
        return curBookId;
    }

    public int getItemCount() {
        List<BookItem> bookItems = LitePal.findAll(BookItem.class);
        return bookItems.size();
    }

    public void deleteBookItem(int position) {
        List<BookItem> bookItems = LitePal.findAll(BookItem.class);

        if (position >= bookItems.size()) return;

        BookItem item = bookItems.get(position);

        LitePal.deleteAll(MoneyItem.class, "bookId = ?", String.valueOf(item.getId()));
        LitePal.delete(BookItem.class, item.getId());
        curBookId = bookItems.get(0).getId();
    }

    public void onAddBookClick() {
        mainView.setNewBook();
    }

    public void onAddBookConfirmClick(String name) {
        List<BookItem> bookItems = LitePal.findAll(BookItem.class);
        int id = bookItems.get(bookItems.size()-1).getId() + 1;

        BookItem item = new BookItem();
        item.addNewBookIntoStorage(id, name);
    }

    public void updateMoneyItemView() {
        List<MoneyItem> moneyItems = LitePal.where(
                "bookId = ?",
                String.valueOf(curBookId)
        ).find(MoneyItem.class);
        mainView.setMainItemRecycler(moneyItems);
    }

    public void onBindBookItemViewHolder(BookItemViewHolder holder, int postion) {
        List<BookItem> bookItems = LitePal.findAll(BookItem.class);
        BookItem item = bookItems.get(postion);
        holder.setBookNameText(item.getName());

        if (postion == curBookId) {
            holder.setAsChose();
        } else {
            holder.setAsNotChose();
        }
    }
}
