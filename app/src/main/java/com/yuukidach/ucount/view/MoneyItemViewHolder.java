package com.yuukidach.ucount.view;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.percentlayout.widget.PercentRelativeLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.yuukidach.ucount.R;
import com.yuukidach.ucount.model.MoneyItem;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class MoneyItemViewHolder extends RecyclerView.ViewHolder {
    PercentRelativeLayout earnLayout, costLayout;
    RelativeLayout dateBar;

    ImageView itemImageEarn, itemImageCost;
    TextView itemNameEarn, itemNameCost;
    TextView itemMoneyEarn, itemMoneyCost;
    TextView itemDspEarn, itemDspCost;
    TextView itemDate;

    private static String date = "";

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
        itemImageEarn.setImageResource(resId);
    }

    public void setCostItemMoney(String str) {
        itemMoneyCost.setText(str);
    }

    public void setEarnItemMoney(String str) {
        itemMoneyEarn.setText(str);
    }

    public boolean isThereADescription(MoneyItem moneyItem) {
        return (moneyItem.getDescription()!=null && !moneyItem.getDescription().equals(""));
    }

    public void handleCostDescription(MoneyItem moneyItem) {
        if (isThereADescription(moneyItem)) {
            RelativeLayout.LayoutParams nameParams = (RelativeLayout.LayoutParams)itemNameCost.getLayoutParams();
            nameParams.removeRule(RelativeLayout.CENTER_VERTICAL);
            RelativeLayout.LayoutParams moneyParams = (RelativeLayout.LayoutParams)itemMoneyCost.getLayoutParams();
            moneyParams.removeRule(RelativeLayout.CENTER_VERTICAL);
            itemDspCost.setText(moneyItem.getDescription());
            itemNameCost.setLayoutParams(nameParams);
            itemMoneyCost.setLayoutParams(moneyParams);
        } else {
            itemDspCost.setVisibility(View.GONE);
        }
    }

    public void handleEarnDescription(MoneyItem moneyItem) {
        if (isThereADescription(moneyItem)) {
            RelativeLayout.LayoutParams nameParams = (RelativeLayout.LayoutParams)itemNameEarn.getLayoutParams();
            nameParams.removeRule(RelativeLayout.CENTER_VERTICAL);
            RelativeLayout.LayoutParams moneyParams = (RelativeLayout.LayoutParams)itemMoneyEarn.getLayoutParams();
            moneyParams.removeRule(RelativeLayout.CENTER_VERTICAL);
            itemDspEarn.setText(moneyItem.getDescription());
            itemNameEarn.setLayoutParams(nameParams);
            itemMoneyEarn.setLayoutParams(moneyParams);
        } else {
            itemDspEarn.setVisibility(View.GONE);
        }
    }

    public void showItemDate(MoneyItem item) {
        Log.d("origin date", "showItemDate: " + date);

        if (date.equals(item.getDate())) dateBar.setVisibility(View.GONE);
        else {
            dateBar.setVisibility(View.VISIBLE);
            date = item.getDate();
            itemDate.setText(date);

            Log.d("new date", "showItemDate: " + date);
        }
    }

    /**
     * reset date to "" otherwise when we go to another activity and then go back, the last date
     * would not be shown
     */
    static public void resetDate() {
        date = "";
    }
}
