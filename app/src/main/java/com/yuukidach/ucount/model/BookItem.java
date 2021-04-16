package com.yuukidach.ucount.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by dash on 18-3-12.
 */

public class BookItem extends LitePalSupport {
    private int id;
    private String name;
//    private double sumAll = 0.0;
//    private double sumMonthlyCost = 0.0;
//    private double sumMonthlyEarn = 0.0;
//    private String date;
    private List<MoneyItem> moneyItemList = new ArrayList<MoneyItem>();

    public BookItem() {}
    public BookItem(String name) {
        this.name = name;

    }

    public int getId()                          { return id; }
    public String getName()                     { return name; }
    public List<MoneyItem> getMoneyItemList()   { return moneyItemList; }
//    public double getSumAll()                   { return sumAll; }
//    public double getSumMonthlyCost()           { return sumMonthlyCost; }
//    public double getSumMonthlyEarn()           { return sumMonthlyEarn; }
//    public String getDate()                     { return date; }

    public void setId(int id)                   { this.id = id; }
    public void setName(String name)            { this.name = name; }
//    public void setSumAll(double all)           { this.sumAll = all; }
//    public void setSumMonthlyCost(double cost)  { this.sumMonthlyCost = cost; }
//    public void setSumMonthlyEarn(double earn)  { this.sumMonthlyEarn = earn; }
//    public void setDate(String date)            { this.date = date; }
    public void setMoneyItemList(List<MoneyItem> moneyItemList) {
        this.moneyItemList = moneyItemList;
    }

    public boolean isThereABook(int id) {
        return LitePal.find(BookItem.class, id) != null;
    }

    public double getEarnSum() {
        return moneyItemList.stream()
                            .filter(o -> o.getInOutType() == MoneyItem.InOutType.EARN)
                            .mapToDouble(MoneyItem::getMoney)
                            .sum();
    }

    public double getCostSum() {
        return moneyItemList.stream()
                            .filter(o -> o.getInOutType() == MoneyItem.InOutType.COST)
                            .mapToDouble(MoneyItem::getMoney)
                            .sum();
    }

    public double getSum() {
        return getEarnSum() - getCostSum();
    }

    public void addNewBookIntoStorage(int id, String name) {
        setId(id);
        setName(name);
//        setSumAll(0.0);
//        setSumMonthlyCost(0.0);
//        setSumMonthlyEarn(0.0);

        String nowDate = new Date().toString();
//        setDate(nowDate);

        save();
    }

    public void insertMoneyItem(MoneyItem moneyItem) {
        moneyItemList.add(moneyItem);
        save();
    }


//    public void saveBook(BookItem bookItem, int id, String name) {
//        bookItem.setId(id);
//        bookItem.setName(name);
//        bookItem.save();
//    }
}
