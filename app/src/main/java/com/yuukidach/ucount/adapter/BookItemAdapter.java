package com.yuukidach.ucount.adapter;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuukidach.ucount.GlobalVariables;
import com.yuukidach.ucount.R;
import com.yuukidach.ucount.model.BookItem;
import com.yuukidach.ucount.model.MoneyItem;

import org.litepal.LitePal;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by dash on 18-3-12.
 */

public class BookItemAdapter extends RecyclerView.Adapter<BookItemAdapter.ViewHolder> {
    private List<BookItem> mBookList;
    private OnItemClickListener onItemClickListener = null;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View bookView;
        ImageView book_mark;
        TextView book_name;

        public ViewHolder(View view) {
            super(view);
            bookView = view;
            book_mark = (ImageView) view.findViewById(R.id.book_mark);
            book_name = (TextView) view.findViewById(R.id.book_name);
        }
    }

    public BookItemAdapter(List<BookItem> bookItemList) {
        mBookList = bookItemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item, parent ,false);

        final ViewHolder holder = new ViewHolder(view);

        holder.bookView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();

                onItemClickListener.onItemClick(holder.itemView, position);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        BookItem bookItem = mBookList.get(position);
        holder.book_name.setText(bookItem.getName());

        // 判断是否被选中
        if (position == GlobalVariables.getmBookPos()) {
            holder.bookView.setBackgroundColor(ContextCompat.getColor(holder.bookView.getContext(), R.color.blue));
            holder.book_mark.setImageResource(R.drawable.ic_yellow_yes);
        } else {
            holder.bookView.setBackgroundColor(ContextCompat.getColor(holder.bookView.getContext(), R.color.colorWhite));
            holder.book_mark.setImageResource(R.drawable.home_detail_btn_n);
        }
    }

    @Override
    public int getItemCount() {
        return mBookList.size();
    }


    // 暴露给外部的方法
    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }


    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    public void removeItem(int position) {
        Log.d(TAG, "removeItem: " + position);
        Log.d(TAG, "removeItem: " + mBookList);
        BookItem bookItem = mBookList.get(position);

        LitePal.deleteAll(MoneyItem.class, "bookId = ?", String.valueOf(mBookList.get(position).getId()));
        LitePal.delete(BookItem.class, mBookList.get(position).getId());

        mBookList.remove(position);
        GlobalVariables.setmBookPos(0);
        notifyItemRemoved(position);
    }
}
