package com.yuukidach.ucount;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuukidach.ucount.model.IOItem;

import java.util.List;

/**
 * Created by yuukidach on 17-3-19.
 */

public class GridRecyclerAdapter extends RecyclerView.Adapter<GridRecyclerAdapter.ViewHolder> implements View.OnClickListener {
    private static final String TAG = "GridRecyclerAdapter";

    private List<IOItem> mDatas;
    private int curIndex;
    private int pageSize;

    public static interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener = null;

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView itemImage;
        private TextView itemTitle;

        public ViewHolder(View view) {
            super(view);
            itemImage = (ImageView) view.findViewById(R.id.item_grid_icon);
            itemTitle = (TextView) view.findViewById(R.id.item_grid_title);
        }
    }

    public GridRecyclerAdapter(List<IOItem> Datas, int curIndex, int pageSize) {
        this.mDatas = Datas;
        this.curIndex = curIndex;
        this.pageSize = pageSize;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chose_io_item, parent, false);
        // 重新设计子项高度
        int height = parent.getHeight();
        view.getLayoutParams().height = height / 4 + 20;
        // 将创建的View注册点击事件
        view.setOnClickListener(this);
        return(new ViewHolder(view));
    }


    @Override
    public long getItemId(int position) {
        return position + curIndex * pageSize;
    }

    @Override
    public int getItemCount() {
        return mDatas.size() > (curIndex + 1) * pageSize ? pageSize : (mDatas.size() - curIndex * pageSize);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: I am in here:" + position);
        int realPositon = position + curIndex * pageSize;
        IOItem ioItem = mDatas.get(realPositon);
        holder.itemImage.setImageResource(ioItem.getSrcId());
        holder.itemTitle.setText(ioItem.getName());
        // 将数据保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(realPositon);
    }

    public void onClick(View view) {
        if (mOnItemClickListener != null ) {
            mOnItemClickListener.onItemClick(view, (int)view.getTag());
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
