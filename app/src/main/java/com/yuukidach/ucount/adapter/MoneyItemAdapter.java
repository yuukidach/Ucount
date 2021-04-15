package com.yuukidach.ucount.adapter;

import androidx.annotation.NonNull;
import androidx.percentlayout.widget.PercentRelativeLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

//import com.yuukidach.ucount.GlobalVariables;
import com.yuukidach.ucount.R;
import com.yuukidach.ucount.model.BookItem;
import com.yuukidach.ucount.model.MoneyItem;

import org.litepal.LitePal;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by yuukidach on 17-3-10.
 */

public class MoneyItemAdapter extends RecyclerView.Adapter<MoneyItemAdapter.ViewHolder> {
    private static final String TAG = "IOItemAdapter";
    private final int TYPE_COST = -1;
    private final int TYPE_EARN =  1;

    private List<MoneyItem> mMoneyItemList;
    private String mDate;

    public DecimalFormat decimalFormat = new DecimalFormat("0.00");

    static class ViewHolder extends RecyclerView.ViewHolder {
        PercentRelativeLayout earnLayout, costLayout;
        RelativeLayout dateBar;

        ImageView itemImageEarn, itemImageCost;
        TextView itemNameEarn, itemNameCost;
        TextView itemMoneyEarn, itemMoneyCost;
        TextView itemDspEarn, itemDspCost;
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
            itemDspEarn   = (TextView ) view.findViewById(R.id.earn_item_decription);
            itemDspCost   = (TextView ) view.findViewById(R.id.cost_item_decription);
            itemDate      = (TextView ) view.findViewById(R.id.iotem_date);
        }
    }

    public MoneyItemAdapter(List<MoneyItem> moneyItemList) {
        mMoneyItemList = moneyItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.io_item, parent ,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MoneyItem moneyItem = mMoneyItemList.get(position);
        showItemDate(holder, moneyItem.getDate());
        // 表示支出的布局
        if (moneyItem.getInOutType() == MoneyItem.InOutType.COST) {
            holder.earnLayout.setVisibility(View.GONE);
            holder.costLayout.setVisibility(View.VISIBLE);
            holder.itemImageCost.setImageResource(moneyItem.getSrcId());
            holder.itemNameCost.setText(moneyItem.getName());
            holder.itemMoneyCost.setText(decimalFormat.format(moneyItem.getMoney()));
            handleDescription(moneyItem, holder.itemDspCost, holder.itemNameCost, holder.itemMoneyCost);
        //表示收入的布局
        } else if (moneyItem.getInOutType() == MoneyItem.InOutType.EARN) {
            holder.earnLayout.setVisibility(View.VISIBLE);
            holder.costLayout.setVisibility(View.GONE);
            holder.itemImageEarn.setImageResource(moneyItem.getSrcId());
            holder.itemNameEarn.setText(moneyItem.getName());
            holder.itemMoneyEarn.setText(decimalFormat.format(moneyItem.getMoney()));
            handleDescription(moneyItem, holder.itemDspEarn, holder.itemNameEarn, holder.itemMoneyEarn);
        }

    }

    @Override
    public int getItemCount() {
        return mMoneyItemList.size();
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
        MoneyItem moneyItem = mMoneyItemList.get(position);
        return moneyItem.getDate();
    }

    public void removeItem(int position) {
        MoneyItem moneyItem = mMoneyItemList.get(position);
        BookItem bookItem = LitePal.find(BookItem.class, GlobalVariables.getmBookId());
        MoneyItem.InOutType type = moneyItem.getInOutType();
//        bookItem.setSumAll(bookItem.getSumAll() - moneyItem.getMoney()*type);
        // 判断收支类型
//        if (type < 0) bookItem.setSumMonthlyCost(bookItem.getSumMonthlyCost() - moneyItem.getMoney());
//        else bookItem.setSumMonthlyEarn(bookItem.getSumMonthlyEarn() - moneyItem.getMoney());
        bookItem.save();
        LitePal.delete(MoneyItem.class, mMoneyItemList.get(position).getId());

        mMoneyItemList.remove(position);
        notifyItemRemoved(position);
    }

    public boolean isThereADescription(MoneyItem moneyItem) {
        return (moneyItem.getDescription()!=null && !moneyItem.getDescription().equals(""));
    }

    public void handleDescription(MoneyItem moneyItem, TextView Dsp, TextView Name, TextView Money) {
        if (isThereADescription(moneyItem)) {
            RelativeLayout.LayoutParams nameParams = (RelativeLayout.LayoutParams)Name.getLayoutParams();
            nameParams.removeRule(RelativeLayout.CENTER_VERTICAL);
            RelativeLayout.LayoutParams moneyParams = (RelativeLayout.LayoutParams)Money.getLayoutParams();
            moneyParams.removeRule(RelativeLayout.CENTER_VERTICAL);
            Dsp.setText(moneyItem.getDescription());
            Name.setLayoutParams(nameParams);
            Money.setLayoutParams(moneyParams);
        } else {
            Dsp.setVisibility(View.GONE);
        }
    }
}