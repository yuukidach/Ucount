package com.yuukidach.ucount.callback;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

//import com.yuukidach.ucount.GlobalVariables;
import com.yuukidach.ucount.R;
import com.yuukidach.ucount.adapter.MoneyItemAdapter;

public class MainItemCallback extends ItemTouchHelper.SimpleCallback {
    // DO NOT make them as final
    private MoneyItemAdapter adapter;
    private Context context;
    private RecyclerView recyclerView;

    public MainItemCallback(Context context, RecyclerView recyclerView, MoneyItemAdapter adapter) {
        super(0, ItemTouchHelper.RIGHT);
        this.context = context;
        this.adapter = adapter;
        this.recyclerView = recyclerView;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView,
                          @NonNull RecyclerView.ViewHolder viewHolder,
                          @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
        // get selected item position
        final int position = viewHolder.getBindingAdapterPosition();

        if (direction == ItemTouchHelper.RIGHT) {
            // Confirm if the user want to delete the item or not
            AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
            builder.setMessage(R.string.delete_item_alarm);

            builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    adapter.removeItem(position);
                    // refresh the activity
                    adapter.notifyDataSetChanged();
                    recyclerView.setAdapter(adapter);
                }
            }).setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    LinearLayout sonView = (LinearLayout) viewHolder.itemView;
                    TextView grandsonTextView = (TextView) sonView.findViewById(R.id.iotem_date);
                    // check if time is needed to show
                    if (sonView.findViewById(R.id.date_bar).getVisibility() == View.VISIBLE)
                        GlobalVariables.setmDate("");
                    else GlobalVariables.setmDate(adapter.getItemDate(position));
                    adapter.notifyItemChanged(position);
                    recyclerView.setAdapter(adapter);
                }
            }).show();  // pop the window
        }
    }
}
