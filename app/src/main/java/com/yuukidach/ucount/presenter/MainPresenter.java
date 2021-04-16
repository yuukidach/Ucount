package com.yuukidach.ucount.presenter;

import android.net.Uri;
import android.util.Log;

//import com.yuukidach.ucount.GlobalVariables;
import com.yuukidach.ucount.model.BookItem;
import com.yuukidach.ucount.model.ImgUtils;
import com.yuukidach.ucount.model.MoneyItem;
import com.yuukidach.ucount.view.MainView;
import com.yuukidach.ucount.view.BookItemViewHolder;
import com.yuukidach.ucount.view.MoneyItemViewHolder;

import org.litepal.LitePal;

import java.security.AlgorithmConstraints;
import java.text.DecimalFormat;
import java.util.List;

public class MainPresenter {
    final private MainView mainView;
    final private ImgUtils imgUtils;

    final DecimalFormat decimalFormat = new DecimalFormat("0.00");
    private int curBookId = 0;
    private int curBookPos = 0;
    private String description;

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

        updateBookItemView(curBookPos);
        updateMoneyItemView();

        mainView.hideBalance();
        // transfer enum to int, then get the string
        updateImgView(MainView.ImageType.HEADER.ordinal());
        updateImgView(MainView.ImageType.DRAWER.ordinal());
        updateMonthlyEarn();
        updateMonthlyCost();

    }

    public void onImageLongClick(MainView.ImageType type) {
        mainView.openPicGallery(type);
    }

    private void showBalance() {
        BookItem book = LitePal.where("uuid = ?", String.valueOf(curBookId))
                               .findFirst(BookItem.class);
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

    public void updateImgView(int id) {
        if (id == MainView.ImageType.DRAWER.ordinal()) {
            mainView.updateDrawerImg(imgUtils.getUriStr(id));
        } else if (id == MainView.ImageType.HEADER.ordinal()) {
            mainView.updateHeaderImg(imgUtils.getUriStr(id));
        }
    }

    public void onActivityResult(Uri uri, int requestCode) {
        updateImgUtils(uri, requestCode);
        updateImgView(requestCode);
    }

    public void updateMonthlyEarn() {
        BookItem book = LitePal.where("uuid = ?", String.valueOf(curBookId))
                               .findFirst(BookItem.class);
        String str = decimalFormat.format(book.getEarnSum());

        mainView.updateMonthlyEarn(str);
    }

    public void updateMonthlyCost() {
        BookItem book = LitePal.where("uuid = ?", String.valueOf(curBookId))
                .findFirst(BookItem.class);
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
        curBookId = bookItems.get(position).getUuid();
        curBookPos = position;
        mainView.setBookItemRecycler(bookItems);

        Log.d("default book uuid", "updateBookItemView: " + bookItems.get(0).getUuid());
        Log.d("position", "updateBookItemView: " + position);
        Log.d("curBookId", "updateBookItemView: " + curBookId);
    }

    public int getCurBookId() {
        return curBookId;
    }

    public int getItemCount() {
        List<BookItem> bookItems = LitePal.findAll(BookItem.class);
        return bookItems.size();
    }

    /**
     * delete bookitem from database
     * if there is only one bookitem, then the deletion won't happen
     * @param position
     */
    public void deleteBookItem(int position) {
        List<BookItem> bookItems = LitePal.findAll(BookItem.class);
        if (bookItems.size() == 0 || bookItems.size() <= position) return;

        BookItem item = bookItems.get(position);

        LitePal.deleteAll(MoneyItem.class, "bookId = ?", String.valueOf(item.getUuid()));
        if (item.isSaved()) {
            Log.d("item", "deleteBookItem: " + "saved, can be deleted");
            item.delete();
        }

        // after deletion, stay at first book
        bookItems = LitePal.findAll(BookItem.class);
        curBookId = bookItems.get(0).getUuid();
        curBookPos = 0;
    }

    public void onAddBookClick() {
        mainView.setNewBook();
    }

    public void onAddBookConfirmClick(String name) {
        List<BookItem> bookItems = LitePal.findAll(BookItem.class);
        int uuid = bookItems.get(bookItems.size()-1).getUuid() + 1;

        BookItem item = new BookItem();
        item.addNewBookIntoStorage(uuid, name);
    }

    public void updateMoneyItemView() {
        List<MoneyItem> moneyItems = LitePal.where(
                "bookId = ?",
                String.valueOf(curBookId)
        ).find(MoneyItem.class);
        mainView.setMainItemRecycler(moneyItems);
    }

    public void onBindBookItemViewHolder(BookItemViewHolder holder, int position) {
        List<BookItem> bookItems = LitePal.findAll(BookItem.class);
        BookItem item = bookItems.get(position);
        holder.setBookNameText(item.getName());

        if (position == curBookId) {
            holder.setAsChose();
        } else {
            holder.setAsNotChose();
        }
    }

    public void onBindMoneyItemViewHolder(MoneyItemViewHolder holder, int position) {
        Log.d("position", "onBindMoneyItemViewHolder: " + position);
        Log.d("current book uuid", "onBindMoneyItemViewHolder: " + curBookId);
        BookItem book = LitePal.where("uuid = ?", String.valueOf(curBookId))
                               .findFirst(BookItem.class);
        MoneyItem money = book.getMoneyItemList().get(position);

        Log.d("inout type", "onBindMoneyItemViewHolder: " + money.getInOutType());

        holder.showItemDate(money);

        if (money.getInOutType() == MoneyItem.InOutType.COST) {
            Log.d("main page cost item", "onBindMoneyItemViewHolder: " + money.getMoney());
            holder.showAsCostItem();
            holder.setCostItemTypeImage(money.getTypeImageId());
            holder.setCostItemTypeText(money.getTypeName());
            holder.setCostItemMoney(decimalFormat.format(money.getMoney()));
            // TODO: just pass a description string
            holder.handleCostDescription(money);
        } else {
            Log.d("main page earn item", "onBindMoneyItemViewHolder: " + money.getTypeImgId());
            holder.showAsEarnItem();
            holder.setEarnItemTypeImage(money.getTypeImageId());
            holder.setEarnItemTypeText(money.getTypeName());
            holder.setEarnItemMoney(decimalFormat.format(money.getMoney()));
            holder.handleEarnDescription(money);
        }
    }
}
