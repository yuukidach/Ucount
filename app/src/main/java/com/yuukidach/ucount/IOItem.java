package com.yuukidach.ucount;

import org.litepal.crud.DataSupport;

/**
 * Created by yuukidach on 17-3-10.
 * 花费和收入的条目
 */

public class IOItem extends DataSupport {
    private int id;
    private int type;                       // 收入还会支出
    private int srcId;                      // 项目资源id
    private double money;
    private String name;
    private String description;
    private String timeStamp;

    public IOItem () {}

    public IOItem(int srcId, String name) {
        this.srcId = srcId;
        this.name = name;
    }

    // 构造函数（无具体描述）
    public IOItem(int srcId, int type, double money, String name) {
        this.money = money;
        this.type = type;
        this.srcId = srcId;
        this.name = name;
    }

    // 构造函数（有具体描述）
    public IOItem(int srcId, int type, double money, String name, String description) {
        this.money = money;
        this.type = type;
        this.srcId = srcId;
        this.name = name;
        this.description = description;
    }

    public double getMoney()                       { return money; }
    public int getType()                           { return type; }
    public int getSrcId()                          { return srcId; }
    public String getName()                        { return name; }
    public String getDescription()                 { return description; }
    public String getTimeStamp()                   { return timeStamp; }

    // 设定属性
    public void setMoney(double money)             { this.money = money; }
    public void setType(int type)                  { this.type = type; }
    public void setSrcId(int srcId)                { this.srcId = srcId; }
    public void setName(String name)               { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setTimeStamp(String timeStamp)     { this.timeStamp = timeStamp; }
}
