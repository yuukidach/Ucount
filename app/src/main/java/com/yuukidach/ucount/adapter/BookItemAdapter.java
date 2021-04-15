package com.yuukidach.ucount.adapter;

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

//    private List<BookItem> mBookList;
    private OnItemClickListener onItemClickListener = null;

//    static class ViewHolder extends RecyclerView.ViewHolder {
//        View bookView;
//        ImageView book_mark;
//        TextView book_name;
//
//        public ViewHolder(View view) {
//            super(view);
//            bookView = view;
//            book_mark = (ImageView) view.findViewById(R.id.book_mark);
//            book_name = (TextView) view.findViewById(R.id.book_name);
//        }
//    }

//    public BookItemAdapter(List<BookItem> bookItemList) {
//        mBookList = bookItemList;
//    }
    public BookItemAdapter(MainPresenter presenter) {
        mainPresenter = presenter;
    }

    @Override
    public BookItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item, parent ,false);

        final  BookItemViewHolder holder = new BookItemViewHolder(view);
//        final ViewHolder holder = new ViewHolder(view);

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

//        BookItem bookItem = mBookList.get(position);
//        holder.book_name.setText(bookItem.getName());
//
//        // 判断是否被选中
////        if (position == GlobalVariables.getmBookPos()) {
//        if (position == mainPresenter.getCurBookId()) {
//            holder.bookView.setBackgroundColor(ContextCompat.getColor(holder.bookView.getContext(), R.color.blue));
//            holder.book_mark.setImageResource(R.drawable.ic_yellow_yes);
//        } else {
//            holder.bookView.setBackgroundColor(ContextCompat.getColor(holder.bookView.getContext(), R.color.colorWhite));
//            holder.book_mark.setImageResource(R.drawable.home_detail_btn_n);
//        }
    }

    @Override
    public int getItemCount() {
        return mainPresenter.getItemCount();
//        return mBookList.size();
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

//        BookItem bookItem = mBookList.get(position);
//
//        LitePal.deleteAll(MoneyItem.class, "bookId = ?", String.valueOf(mBookList.get(position).getId()));
//        LitePal.delete(BookItem.class, mBookList.get(position).getId());
//
//        mBookList.remove(position);
//        GlobalVariables.setmBookPos(0);
        notifyItemRemoved(position);
    }
}
