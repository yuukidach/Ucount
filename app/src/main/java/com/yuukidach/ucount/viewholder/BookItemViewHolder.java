package com.yuukidach.ucount.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.yuukidach.ucount.R;

public class BookItemViewHolder extends RecyclerView.ViewHolder {
    View bookView;
    ImageView bookMark;
    TextView bookName;

    public BookItemViewHolder(View view) {
        super(view);
        bookView = view;
        bookMark = (ImageView) view.findViewById(R.id.book_mark);
        bookName = (TextView) view.findViewById(R.id.book_name);
    }
}
