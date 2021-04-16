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
//    private double sumAll = 0.0;
//    private double sumMonthlyCost = 0.0;
//    private double sumMonthlyEarn = 0.0;
//    private String date;
//    private List<MoneyItem> moneyItemList = new ArrayList<MoneyItem>();

    public BookItem() {}
    public BookItem(String name) {
        this.name = name;
    }

    public int getUuid()                          { return uuid; }
    public String getName()                     { return name; }
//    public double getSumAll()                   { return sumAll; }
//    public double getSumMonthlyCost()           { return sumMonthlyCost; }
//    public double getSumMonthlyEarn()           { return sumMonthlyEarn; }
//    public String getDate()                     { return date; }

    public void setUuid(int uuid)                   { this.uuid = uuid; }
    public void setName(String name)            { this.name = name; }
//    public void setSumAll(double all)           { this.sumAll = all; }
//    public void setSumMonthlyCost(double cost)  { this.sumMonthlyCost = cost; }
//    public void setSumMonthlyEarn(double earn)  { this.sumMonthlyEarn = earn; }
//    public void setDate(String date)            { this.date = date; }
//    public void setMoneyItemList(List<MoneyItem> moneyItemList) {
//        this.moneyItemList = moneyItemList;
//    }

    public boolean isThereABook(int id) {
        return LitePal.find(BookItem.class, id) != null;
    }

    public List<MoneyItem> getMoneyItemList() {
        return LitePal.where("bookId = ?", String.valueOf(uuid))
                      .find(MoneyItem.class);
    }

//    public List<MoneyItem> getMoneyItemList() {
//        updateMoneyItemList();
//        return moneyItemList;
//    }

    public double getEarnSum() {
        List<MoneyItem> moneyItemList = getMoneyItemList();
        if (moneyItemList.size() == 0) return 0.0;
        Log.d("moneyItemList size", "getEarnSum: " + moneyItemList.size());

        double sum = 0.0;
        for (MoneyItem item : moneyItemList) {
            if (item.getInOutType() == MoneyItem.InOutType.EARN) {
                sum += item.getMoney();
            }
        }

        Log.d("sum of earn", "getEarnSum: "+ sum);
        return sum;

//        return moneyItemList.stream()
//                            .filter(o -> o.getInOutType() == MoneyItem.InOutType.EARN)
//                            .mapToDouble(MoneyItem::getMoney)
//                            .sum();
    }

    public double getCostSum() {
        List<MoneyItem> moneyItemList = getMoneyItemList();
        if (moneyItemList.size() == 0) return 0.0;

        double sum = 0.0;
        for (MoneyItem item : moneyItemList) {
            if (item.getInOutType() == MoneyItem.InOutType.COST) {
                sum += item.getMoney();
            }
        }

        Log.d("sum of cost", "getCostSum: " + sum);
        return sum;
//
//        return moneyItemList.stream()
//                            .filter(o -> o.getInOutType() == MoneyItem.InOutType.COST)
//                            .mapToDouble(MoneyItem::getMoney)
//                            .sum();
    }

    public double getSum() {
        return getEarnSum() - getCostSum();
    }

    public void addNewBookIntoStorage(int uuid, String name) {
        setUuid(uuid);
        setName(name);
//        setSumAll(0.0);
//        setSumMonthlyCost(0.0);
//        setSumMonthlyEarn(0.0);

//        String nowDate = new Date().toString();
//        setDate(nowDate);

        save();
    }

//    public void insertMoneyItem(MoneyItem moneyItem) {
//        moneyItemList.add(moneyItem);
//        save();
//    }


//    public void saveBook(BookItem bookItem, int id, String name) {
//        bookItem.setId(id);
//        bookItem.setName(name);
//        bookItem.save();
//    }
}
