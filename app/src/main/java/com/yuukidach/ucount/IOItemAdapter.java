package com.yuukidach.ucount;

import android.support.percent.PercentFrameLayout;
import android.support.percent.PercentRelativeLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by yuukidach on 17-3-10.
 */

public class IOItemAdapter extends RecyclerView.Adapter<IOItemAdapter.ViewHolder> {
    private List<IOItem> mIOItemList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        PercentRelativeLayout earnLayout, costLayout;
        ImageView itemImageEarn, itemImageCost;
        TextView itemNameEarn, itemNameCost;
        TextView itemMoneyEarn, itemMoneyCost;

        public ViewHolder(View view) {
            super(view);
            earnLayout = (PercentRelativeLayout) view.findViewById(R.id.earn_left_layout);
            costLayout = (PercentRelativeLayout) view.findViewById(R.id.cost_right_layout);
            itemImageEarn = (ImageView) view.findViewById(R.id.earn_item_img_main);
            itemImageCost = (ImageView) view.findViewById(R.id.cost_item_img_main);
            itemNameEarn  = (TextView ) view.findViewById(R.id.earn_item_name_main);
            itemNameCost  = (TextView ) view.findViewById(R.id.cost_item_name_main);
            itemMoneyEarn = (TextView ) view.findViewById(R.id.earn_item_money_main);
            itemMoneyCost = (TextView ) view.findViewById(R.id.cost_item_money_main);
        }
    }

    public IOItemAdapter(List<IOItem> ioItemList) {
        mIOItemList = ioItemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.io_item, parent ,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        IOItem ioItem = mIOItemList.get(position);
        // 表示支出的布局
        if (ioItem.getType() == -1) {       // -1代表支出
            holder.earnLayout.setVisibility(View.GONE);
            holder.costLayout.setVisibility(View.VISIBLE);
            holder.itemImageCost.setImageResource(ioItem.getId());
            holder.itemNameCost.setText(ioItem.getName());
            holder.itemMoneyCost.setText(String.valueOf(ioItem.getMoney()));
        //表示收入的布局
        } else if (ioItem.getType() == 1) {
            holder.earnLayout.setVisibility(View.VISIBLE);
            holder.costLayout.setVisibility(View.GONE);
            holder.itemImageEarn.setImageResource(ioItem.getId());
            holder.itemNameEarn.setText(ioItem.getName());
            holder.itemMoneyEarn.setText(String.valueOf(ioItem.getMoney()));
        }

    }

    @Override
    public int getItemCount() {
        return mIOItemList.size();
    }
}
