package com.yuukidach.ucount.view.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yuukidach.ucount.R;
import com.yuukidach.ucount.model.MoneyItem;
import com.yuukidach.ucount.presenter.MainPresenter;
import com.yuukidach.ucount.view.MoneyItemViewHolder;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by yuukidach on 17-3-10.
 */

public class MoneyItemAdapter extends RecyclerView.Adapter<MoneyItemViewHolder> {
    private static final String TAG = "MoneyItemAdapter";

    private MainPresenter presenter;

    private List<MoneyItem> mMoneyItemList;
    private String showingDate;

    public DecimalFormat decimalFormat = new DecimalFormat("0.00");

    public MoneyItemAdapter(MainPresenter presenter, List<MoneyItem> moneyItemList) {
        this.presenter = presenter;
        mMoneyItemList = moneyItemList;
    }

    @NonNull
    @Override
    public MoneyItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.io_item, parent ,false);
        return new MoneyItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoneyItemViewHolder holder, int position) {
        presenter.onBindMoneyItemViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return mMoneyItemList.size();
    }

    // 返回子项目时间，便于在取消删除的时候判断是否应该显示项目时间
    public String getItemDate(int position) {
        MoneyItem moneyItem = mMoneyItemList.get(position);
        return moneyItem.getDate();
    }

    public void removeItem(int position) {
        mMoneyItemList = presenter.getMoneyItemsInCurBook();
        MoneyItem moneyItem = mMoneyItemList.get(position);
        moneyItem.delete();

        mMoneyItemList.remove(position);
        notifyItemRemoved(position);

        // update balance views
        presenter.hideBalance();
        presenter.updateMonthlyEarn();
        presenter.updateMonthlyCost();
    }
}