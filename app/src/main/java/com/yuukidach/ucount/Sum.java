package com.yuukidach.ucount;

import org.litepal.crud.DataSupport;

/**
 * Created by yuukidach on 17-3-14.
 */

public class Sum extends DataSupport {
    private double total = 0;
    private String name;
    private int id;

    public void setTotal(double total) { this.total = total; }
    public void setName(String name) { this.name = name; }
    public void setId(int id) { this.id = id; }

    public double getTotal() { return total; }
    public String getName() { return name; }
    public int getId() { return id; }
}
