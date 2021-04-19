package com.yuukidach.ucount.view;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.yuukidach.ucount.R;

public class BookItemViewHolder extends RecyclerView.ViewHolder {
    private final View bookView;
    private final ImageView bookMark;
    private final TextView bookName;

    public BookItemViewHolder(View view) {
        super(view);
        bookView = view;
        bookMark = (ImageView) view.findViewById(R.id.book_mark);
        bookName = (TextView) view.findViewById(R.id.book_name);
    }

    public View getBookView() {
        return bookView;
    }

    public void setBookNameText(String name) {
        bookName.setText(name);
    }

    public void setAsChose() {
        bookView.setBackgroundColor(
                ContextCompat.getColor(bookView.getContext(), R.color.blue)
        );
        bookMark.setImageResource(R.drawable.ic_yellow_yes);
    }

    public void setAsNotChose() {
        bookView.setBackgroundColor(
                ContextCompat.getColor(bookView.getContext(), R.color.colorWhite)
        );
        bookMark.setImageResource(R.drawable.home_detail_btn_n);
    }
}
