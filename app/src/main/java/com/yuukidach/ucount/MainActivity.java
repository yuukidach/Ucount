package com.yuukidach.ucount;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.List;

import at.markushi.ui.CircleButton;

public class MainActivity extends AppCompatActivity {
    private List<IOItem> ioItemList = new ArrayList<>();
    private RecyclerView ioItemRecyclerView;
    private IOItemAdapter adapter;
    private Button showBtn;
    private CircleButton addBtn;

    private Sum sum;

    public static String PACKAGE_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 获得包名，方便后面的程序使用
        PACKAGE_NAME = getApplicationContext().getPackageName();

        // litepal
        Connector.getDatabase();

        showBtn = (Button) findViewById(R.id.show_money_button);
        addBtn = (CircleButton) findViewById(R.id.add_button);
        ioItemRecyclerView = (RecyclerView) findViewById(R.id.in_and_out_items);

        // 设置按钮监听
        showBtn.setOnClickListener(new ButtonListener());
        addBtn.setOnClickListener(new ButtonListener());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);    // 列表从底部开始展示，反转后从上方开始展示
        layoutManager.setReverseLayout(true);   // 列表反转

        ioItemRecyclerView.setLayoutManager(layoutManager);
        adapter = new IOItemAdapter(ioItemList);
        ioItemRecyclerView.setAdapter(adapter);
    }


    @Override
    protected void onResume() {
        super.onResume();
        showBtn.setText("显示余额");
    }


    // 各个按钮的活动
    private class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                // 按住加号按钮以后，切换到AddItemActivity
                case R.id.add_button:
                    Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
                    startActivity(intent);
                    break;
                case R.id.show_money_button:
                    if (showBtn.getText() == "显示余额") {
                        sum = DataSupport.find(Sum.class, 1);
                        String sumString = String.valueOf(sum.getTotal());
                        String.format(sumString, "%.2f");
                        showBtn.setText(sumString);
                    } else showBtn.setText("显示余额");
                    break;
                default:
                    break;
            }
        }
    }
}
