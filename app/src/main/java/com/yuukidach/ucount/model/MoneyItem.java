package com.yuukidach.ucount.model;

import android.content.res.Resources;

import com.yuukidach.ucount.MainActivity;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.util.Date;

/**
 * Created by yuukidach on 17-3-10.
 * 花费和收入的条目
 */

public class MoneyItem extends LitePalSupport {
    public enum InOutType {
        EARN,
        COST
    }

    public enum ItemType{

    }

//    public final int TYPE_COST = -1;
//    public final int TYPE_EARN =  1;

    private int id;
    private InOutType inOutType;
    private int bookId;
    private double money;
    private String typeName;
    private String description = "";
    private String date;
    private String srcName;                 // 项目资源名称 // TODO WHAT'S this for?
    private ItemType itemType;

    public MoneyItem() {}

    public MoneyItem(String srcName, String typeName) {
        this.srcName = srcName;
        this.typeName = typeName;
    }

    // constructor without description
    public MoneyItem(String srcName, InOutType inOutType, double money, String typeName) {
        this(srcName, typeName);
        this.money = money;
        this.inOutType = inOutType;
    }

    // constructor with description
    public MoneyItem(String srcName,
                     InOutType inOutType,
                     double money,
                     String name,
                     String description) {
        this(srcName, inOutType, money, name);
        this.description = description;
    }

    public double getMoney()                       { return money; }
    public InOutType getInOutType()                { return inOutType; }
    public String getTypeName()                        { return typeName; }
    public String getDescription()                 { return description; }
    public String getDate()                        { return date; }
    public int getBookId()                         { return bookId; }
    public String getSrcName()                     { return srcName; }
    public int getId()                             { return id; }

    // 设定属性
    public void setMoney(double money)             { this.money = money; }
    public void setInOutType(InOutType inOutType)  { this.inOutType = inOutType; }
    public void setTypeName(String typeName)               { this.typeName = typeName; }
    public void setDescription(String description) { this.description = description; }
    public void setDate(String date)               { this.date = date; }
    public void setBookId(int mId)                 { this.bookId = mId; }
    public void setSrcName(String srcName)         { this.srcName = srcName; }

    // 返回图片资源的id
    public int getSrcId() {
        Resources resources = MainActivity.resources;
        return resources.getIdentifier(srcName, "drawable", MainActivity.PACKAGE_NAME);
    }

    public void addNewMoneyItemIntoStorage(String name,
                                           double money,
                                           int bookId,
                                           InOutType inOutType,
                                           String description) {
        BookItem bookItem = LitePal.find(BookItem.class, bookId);

        setTypeName(name);
        setMoney(money);
        setBookId(bookId);
        setInOutType(inOutType);
        setDescription(description);

        setDate(new Date().toString());

        save();

        bookItem.getMoneyItemList().add(this);
        bookItem.save();
    }
}
