package com.yuukidach.ucount.view;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.percentlayout.widget.PercentRelativeLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.yuukidach.ucount.R;

public class MoneyItemViewHolder extends RecyclerView.ViewHolder {
    PercentRelativeLayout earnLayout, costLayout;
    RelativeLayout dateBar;

    ImageView itemImageEarn, itemImageCost;
    TextView itemNameEarn, itemNameCost;
    TextView itemMoneyEarn, itemMoneyCost;
    TextView itemDspEarn, itemDspCost;
    TextView itemDate;

    public MoneyItemViewHolder(View view) {
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

    public void showAsCostItem() {
        earnLayout.setVisibility(View.GONE);
        costLayout.setVisibility(View.VISIBLE);
    }

    public void showAsEarnItem() {
        earnLayout.setVisibility(View.VISIBLE);
        costLayout.setVisibility(View.GONE);
    }

    public void setCostItemTypeText(String str) {
        itemNameCost.setText(str);
    }

    public void setEarnItemTypeText(String str) {
        itemNameEarn.setText(str);
    }

    public void setCostItemTypeImage(int resId) {
        itemImageCost.setImageResource(resId);
    }

    public void setEarnItemTypeImage(int resId) {
        itemImageCost.setImageResource(resId);
    }

    public void setCostItemMoney(String str) {
        itemMoneyCost.setText(str);
    }

    public void setEarnItemMoney(String str) {
        itemMoneyEarn.setText(str);
    }
}
