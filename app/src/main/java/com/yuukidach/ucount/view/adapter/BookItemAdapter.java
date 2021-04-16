package com.yuukidach.ucount.view.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//import com.yuukidach.ucount.GlobalVariables;
import com.yuukidach.ucount.R;
import com.yuukidach.ucount.presenter.MainPresenter;
import com.yuukidach.ucount.view.BookItemViewHolder;

import static android.content.ContentValues.TAG;

public class BookItemAdapter extends RecyclerView.Adapter<BookItemViewHolder> {
    private final MainPresenter mainPresenter;

    private OnItemClickListener onItemClickListener = null;

    public BookItemAdapter(MainPresenter presenter) {
        mainPresenter = presenter;
    }

    @NonNull
    @Override
    public BookItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item, parent ,false);

        final  BookItemViewHolder holder = new BookItemViewHolder(view);

        holder.getBookView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getBindingAdapterPosition();
                onItemClickListener.onItemClick(holder.itemView, position);
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final BookItemViewHolder holder, int position) {
        mainPresenter.onBindBookItemViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return mainPresenter.getItemCount();
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

        mainPresenter.deleteBookItem(position);
        notifyItemRemoved(position);
    }
}
