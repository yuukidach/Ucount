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
import java.util.StringTokenizer;

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

    private SimpleDateFormat formatItem = new SimpleDateFormat("yyyy年MM月dd日");
    private SimpleDateFormat formatSum  = new SimpleDateFormat("yyyy年MM月");

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
        Sum sum = new Sum();
        IOItem ioItem = new IOItem();
        String tagName = (String) bannerText.getTag();
        int tagType = (int) bannerImage.getTag();

        if (tagType < 0) {
            ioItem.setType(ioItem.TYPE_COST);
        } else ioItem.setType(ioItem.TYPE_EARN);

        ioItem.setName(bannerText.getText().toString());
        ioItem.setSrcName(tagName);
        ioItem.setMoney(money);
        ioItem.setTimeStamp(formatItem.format(new Date()));         // 存储记账时间
        ioItem.save();

        int type = ioItem.getType();
        String sumDate = formatSum.format(new Date());
        // 计算总额
        sum.calculateMoneyIncludeNull(sum.SUM, "All", money, type, sumDate);
        calculateMonthlyMoney(type, ioItem);
    }

    public void calculateMonthlyMoney(int type, IOItem ioItem) {
        Sum sum = new Sum();
        Sum tmpSum = new Sum();
        String sumDate = formatSum.format(new Date());
        int id = (int)((double)type / 2 + 2.5);

        // 保证一定现有2号id，避免出现当月支出不更新的bug
        if (!tmpSum.isThereASum(tmpSum.MONTHLY_COST))
            tmpSum.saveSum(tmpSum, tmpSum.MONTHLY_COST, 0.0, 1, sumDate);

        if (sum.isThereASum(id)) {
            sum = DataSupport.find(Sum.class, id);
            if (sum.getDate().equals(ioItem.getTimeStamp().substring(0, 8))) {
                sum.calculateMoney(id, ioItem.getMoney(), type*type);
            } else {
                sum.saveSum(sum, id, ioItem.getMoney(), type*type, sumDate);
            }
        } else {
            sum.saveSum(sum, id, ioItem.getMoney(), type*type, sumDate);
        }
    }
}
