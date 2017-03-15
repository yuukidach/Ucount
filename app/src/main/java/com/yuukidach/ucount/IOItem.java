package com.yuukidach.ucount;

import org.litepal.crud.DataSupport;

/**
 * Created by yuukidach on 17-3-10.
 * 花费和收入的条目
 */

public class IOItem extends DataSupport {
    private int type;                       // 收入还会支出
    private int id;                         // 项目资源id
    private double money;
    private String name;
    private String description;

    public IOItem () {}

    public IOItem(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // 构造函数（无具体描述）
    public IOItem(int id, int type, double money, String name) {
        this.money = money;
        this.type = type;
        this.id = id;
        this.name = name;
    }

    // 构造函数（有具体描述）
    public IOItem(int id, int type, double money, String name, String description) {
        this.money = money;
        this.type = type;
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public double getMoney() {
        return money;
    }

    public int getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    // 设定属性
    public void setMoney(double money)             { this.money = money; }
    public void setType(int type)                  { this.type = type; }
    public void setId(int id)                      { this.id = id; }
    public void setName(String name)               { this.name = name; }
    public void setDescription(String description) { this.description = description; }
}
