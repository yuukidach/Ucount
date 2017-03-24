package com.yuukidach.ucount;

import android.content.SharedPreferences;
import android.net.Uri;
import android.support.percent.PercentRelativeLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.litepal.crud.DataSupport;

import java.text.DecimalFormat;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by yuukidach on 17-3-10.
 */

public class IOItemAdapter extends RecyclerView.Adapter<IOItemAdapter.ViewHolder> {
    private static final String TAG = "IOItemAdapter";
    private final int TYPE_COST = -1;
    private final int TYPE_EARN =  1;

    private List<IOItem> mIOItemList;
    private String mDate;

    public DecimalFormat decimalFormat = new DecimalFormat("0.00");

    static class ViewHolder extends RecyclerView.ViewHolder {
        PercentRelativeLayout earnLayout, costLayout;
        RelativeLayout dateBar;

        ImageView itemImageEarn, itemImageCost;
        TextView itemNameEarn, itemNameCost;
        TextView itemMoneyEarn, itemMoneyCost;
        TextView itemDate;

        public ViewHolder(View view) {
            super(view);
            earnLayout = (PercentRelativeLayout) view.findViewById(R.id.earn_left_layout);
            costLayout = (PercentRelativeLayout) view.findViewById(R.id.cost_right_layout);
            dateBar    = (RelativeLayout) view.findViewById(R.id.date_bar);

            itemImageEarn = (ImageView) view.findViewById(R.id.earn_item_img_main);
            itemImageCost = (ImageView) view.findViewById(R.id.cost_item_img_main);
            itemNameEarn  = (TextView ) view.findViewById(R.id.earn_item_name_main);
            itemNameCost  = (TextView ) view.findViewById(R.id.cost_item_name_main);
            itemMoneyEarn = (TextView ) view.findViewById(R.id.earn_item_money_main);
            itemMoneyCost = (TextView ) view.findViewById(R.id.cost_item_money_main);
            itemDate      = (TextView ) view.findViewById(R.id.iotem_date);
        }
    }

    public IOItemAdapter(List<IOItem> ioItemList) {
        mIOItemList = ioItemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.io_item, parent ,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        IOItem ioItem = mIOItemList.get(position);
        showItemDate(holder, ioItem.getTimeStamp());
        // 表示支出的布局
        if (ioItem.getType() == TYPE_COST) {       // -1代表支出
            holder.earnLayout.setVisibility(View.GONE);
            holder.costLayout.setVisibility(View.VISIBLE);
            holder.itemImageCost.setImageResource(ioItem.getSrcId());
            holder.itemNameCost.setText(ioItem.getName());
            holder.itemMoneyCost.setText(decimalFormat.format(ioItem.getMoney()));
        //表示收入的布局
        } else if (ioItem.getType() == TYPE_EARN) {
            holder.earnLayout.setVisibility(View.VISIBLE);
            holder.costLayout.setVisibility(View.GONE);
            holder.itemImageEarn.setImageResource(ioItem.getSrcId());
            holder.itemNameEarn.setText(ioItem.getName());
            holder.itemMoneyEarn.setText(decimalFormat.format(ioItem.getMoney()));
        }

    }

    @Override
    public int getItemCount() {
        return mIOItemList.size();
    }

    // 利用全局变量进行判定
    public void showItemDate(ViewHolder holder, String Date) {
        if (GlobalVariables.getmDate().equals(Date)) holder.dateBar.setVisibility(View.GONE);
        else {
            holder.dateBar.setVisibility(View.VISIBLE);
            holder.itemDate.setText(Date);
            GlobalVariables.setmDate(Date);
            Log.d(TAG, "showItemDate: "+Date);
        }
    }

    // 返回子项目时间，便于在取消删除的时候判断是否应该显示项目时间
    public String getItemDate(int position) {
        IOItem ioItem = mIOItemList.get(position);
        return ioItem.getTimeStamp();
    }

    public void removeItem(int position) {
        IOItem ioItem = mIOItemList.get(position);
        Sum sum = DataSupport.find(Sum.class, 1);  // 1 代表sum;
        Sum month;
        int type = ioItem.getType();
        sum.setTotal(sum.getTotal()-ioItem.getMoney() * type);
        sum.save();
        // 判断收支类型
        if (type < 0) month = DataSupport.find(Sum.class, 2);     // 2 代表cost
        else month = DataSupport.find(Sum.class, 3);              // 3 代表earn
        month.setTotal(month.getTotal()-ioItem.getMoney());
        month.save();
        DataSupport.delete(IOItem.class, mIOItemList.get(position).getId());

        mIOItemList.remove(position);
        notifyItemRemoved(position);
    }
}