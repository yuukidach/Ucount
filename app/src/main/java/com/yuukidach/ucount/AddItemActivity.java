package com.yuukidach.ucount;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;

import at.markushi.ui.CircleButton;

public class AddItemActivity extends AppCompatActivity {

    private static final String TAG = "AddItemActivity";

    private FragmentManager manager;
    private FragmentTransaction transaction;

    private Button addCostBtn;
    private Button addEarnBtn;
    private CircleButton addFinishBtn;

    private ImageView bannerImage;
    private TextView bannerText;

    private TextView moneyText;

    private SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        // 设置按钮监听
        addCostBtn = (Button) findViewById(R.id.add_cost_button);
        addEarnBtn = (Button) findViewById(R.id.add_earn_button);
        addFinishBtn = (CircleButton) findViewById(R.id.add_finish);
        addCostBtn.setOnClickListener(new ButtonListener());
        addEarnBtn.setOnClickListener(new ButtonListener());
        addFinishBtn.setOnClickListener(new ButtonListener());

        bannerText = (TextView) findViewById(R.id.chosen_title);
        bannerImage = (ImageView) findViewById(R.id.chosen_image);

        moneyText = (TextView) findViewById(R.id.input_money_text);

        manager = getSupportFragmentManager();

        transaction = manager.beginTransaction();
        transaction.replace(R.id.item_fragment, new CostFragment());
        transaction.commit();

    }

    private class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            transaction = manager.beginTransaction();

            switch (view.getId()) {
                case R.id.add_cost_button:
                    addCostBtn.setTextColor(0xffff8c00); // 设置“支出“按钮为灰色
                    addEarnBtn.setTextColor(0xff908070); // 设置“收入”按钮为橙色
                    transaction.replace(R.id.item_fragment, new CostFragment());
                    Log.d(TAG, "onClick: add_cost_button");

                    break;
                case R.id.add_earn_button:
                    addEarnBtn.setTextColor(0xffff8c00); // 设置“收入“按钮为灰色
                    addCostBtn.setTextColor(0xff908070); // 设置“支出”按钮为橙色
                    transaction.replace(R.id.item_fragment, new EarnFragment());
                    Log.d(TAG, "onClick: add_earn_button");

                    break;
                case R.id.add_finish:
                    String moneyString =  moneyText.getText().toString();
                    if (moneyString.equals(""))
                        Toast.makeText(getApplicationContext(),"唔姆，你还没输入金额",Toast.LENGTH_SHORT).show();
                    else {
                        putItemInData(Double.parseDouble(moneyText.getText().toString()));
                        finish();
                    }
                    break;
            }

            transaction.commit();
        }
    }

    public void putItemInData(double money) {
        IOItem ioItem = new IOItem();
        int tag = (int)bannerText.getTag();

        if (tag < 0) {
            ioItem.setType(-1);
            tag = -tag;
        } else ioItem.setType(1);

        ioItem.setName(bannerText.getText().toString());
        ioItem.setSrcId(tag);
        ioItem.setMoney(money);
        ioItem.setTimeStamp(format.format(new Date()));
        ioItem.save();

        Sum sum = new Sum();
        if (isThereASum()) {
            sum = DataSupport.find(Sum.class, 1);
            sum.setTotal(sum.getTotal() + money * ioItem.getType());
            sum.save();
        } else {
            sum.setId(1);
            sum.setName("All");
            sum.setTotal(money * ioItem.getType());
            sum.save();
        }
    }

    public boolean isThereASum() {
        if (DataSupport.find(Sum.class, 1) == null)
            return false;
        return true;
    }
}
