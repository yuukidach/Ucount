package com.yuukidach.ucount.model;

import android.content.res.Resources;

import com.yuukidach.ucount.MainActivity;

import org.litepal.crud.DataSupport;

/**
 * Created by yuukidach on 17-3-10.
 * 花费和收入的条目
 */

public class IOItem extends DataSupport {
    public final int TYPE_COST = -1;
    public final int TYPE_EARN =  1;

    private int id;
    private int type;                       // 收入还会支出
    private int bookId;
    private double money;
    private String name;
    private String description;
    private String timeStamp;
    private String srcName;                 // 项目资源名称

    public IOItem () {}

    public IOItem(String srcName, String name) {
        this.srcName = srcName;
        this.name = name;
    }

    // 构造函数（无具体描述）
    public IOItem(String srcName, int type, double money, String name) {
        this.srcName = srcName;
        this.money = money;
        this.type = type;
        this.name = name;
    }

    // 构造函数（有具体描述）
    public IOItem(String srcName, int type, double money, String name, String description) {
        this.money = money;
        this.type = type;
        this.srcName = srcName;
        this.name = name;
        this.description = description;
    }

    public double getMoney()                       { return money; }
    public int getType()                           { return type; }
    public String getName()                        { return name; }
    public String getDescription()                 { return description; }
    public String getTimeStamp()                   { return timeStamp; }
    public int getBookId()                         { return bookId; }
    public String getSrcName()                     { return srcName; }
    public int getId()                             { return id; }

    // 设定属性
    public void setMoney(double money)             { this.money = money; }
    public void setType(int type)                  { this.type = type; }
    public void setName(String name)               { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setTimeStamp(String timeStamp)     { this.timeStamp = timeStamp; }
    public void setBookId(int mId)                 { this.bookId = mId; }
    public void setSrcName(String srcName)         { this.srcName = srcName; }

    // 返回图片资源的id
    public int getSrcId() {
        Resources resources = MainActivity.resources;
        return resources.getIdentifier(srcName, "drawable", MainActivity.PACKAGE_NAME);
    }
}
