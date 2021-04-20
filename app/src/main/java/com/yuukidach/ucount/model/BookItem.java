package com.yuukidach.ucount.model;

import android.util.Log;

import org.litepal.LitePal;
import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.List;

/**
 * Created by dash on 18-3-12.
 */

public class BookItem extends LitePalSupport {
    @Column(unique = true)
    private int uuid;
    private String name;

    public BookItem() {
//        MoneyItem moneyItem = new MoneyItem("000", MoneyItem.InOutType.COST, 100.0, "normal", "note");
//        moneyItem.setDate("2021-04-01");
//        moneyItem.setBookId(0);
//        moneyItem.save();
//        MoneyItem moneyItem2 = new MoneyItem("000", MoneyItem.InOutType.COST, 200.0, "normal", "note");
//        moneyItem2.setDate("2021-04-25");
//        moneyItem2.setBookId(0);
//        moneyItem2.save();
//        MoneyItem moneyItem3 = new MoneyItem("000", MoneyItem.InOutType.COST, 300.0, "rent", "note");
//        moneyItem3.setDate("2021-04-03");
//        moneyItem3.setBookId(0);
//        moneyItem3.save();
//        MoneyItem moneyItem4 = new MoneyItem("000", MoneyItem.InOutType.COST, 400.0, "normal", "note");
//        moneyItem4.setDate("2021-03-01");
//        moneyItem4.setBookId(0);
//        moneyItem4.save();
//        MoneyItem moneyItem5 = new MoneyItem("000", MoneyItem.InOutType.COST, 500.0, "tax", "note");
//        moneyItem5.setDate("2021-04-26");
//        moneyItem5.setBookId(0);
//        moneyItem5.save();

    }
    public BookItem(String name) {
        this.name = name;
    }

    public int getUuid()                          { return uuid; }
    public String getName()                     { return name; }

    public void setUuid(int uuid)                   { this.uuid = uuid; }
    public void setName(String name)            { this.name = name; }

    public boolean isThereABook(int id) {
        return LitePal.find(BookItem.class, id) != null;
    }

    public List<MoneyItem> getMoneyItemList() {
        return LitePal.where("bookId = ?", String.valueOf(uuid))
                      .find(MoneyItem.class);
    }

    public double getEarnSum() {
        List<MoneyItem> moneyItemList = getMoneyItemList();
        if (moneyItemList.size() == 0) return 0.0;
        Log.d("moneyItemList size", "getEarnSum: " + moneyItemList.size());

        return moneyItemList.stream()
                            .filter(o -> o.getInOutType() == MoneyItem.InOutType.EARN)
                            .mapToDouble(MoneyItem::getMoney)
                            .sum();
    }

    public double getCostSum() {
        List<MoneyItem> moneyItemList = getMoneyItemList();
        if (moneyItemList.size() == 0) return 0.0;

        return moneyItemList.stream()
                            .filter(o -> o.getInOutType() == MoneyItem.InOutType.COST)
                            .mapToDouble(MoneyItem::getMoney)
                            .sum();
    }

    public double getSum() {
        return getEarnSum() - getCostSum();
    }

    public void addNewBookIntoStorage(int uuid, String name) {
        setUuid(uuid);
        setName(name);
        save();
    }
}
