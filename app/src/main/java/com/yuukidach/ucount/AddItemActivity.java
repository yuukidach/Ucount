package com.yuukidach.ucount;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yuukidach.ucount.model.BookItem;
import com.yuukidach.ucount.model.MoneyItem;
import com.yuukidach.ucount.presenter.AddItemPresenter;
import com.yuukidach.ucount.view.AddItemView;

import org.litepal.LitePal;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddItemActivity extends AppCompatActivity implements AddItemView {
    private final int REQUEST_DESCRIPTION = 1;

    private AddItemPresenter presenter;

    private static final String TAG = "AddItemActivity";

    private FragmentManager manager;
    private FragmentTransaction transaction;

    private Button addCostBtn;
    private Button addEarnBtn;
    private Button clearBtn;
    private ImageButton addFinishBtn;
    private ImageButton addDescription;


    private ImageView bannerImage;
    private TextView bannerText;

    private TextView moneyText;

//    private final Button addCostBtn = (Button) findViewById(R.id.add_cost_button);
//    private final Button addEarnBtn = (Button) findViewById(R.id.add_earn_button);
//    private final Button clearBtn = (Button) findViewById(R.id.clear);
//    private final ImageButton addFinishBtn = (ImageButton) findViewById(R.id.add_finish);
//    private final ImageButton addDescription = (ImageButton) findViewById(R.id.add_description);
//
//
//    private final ImageView bannerImage = (ImageView) findViewById(R.id.chosen_image);
//    private final TextView bannerText = (TextView) findViewById(R.id.chosen_title);
//
//    private final TextView moneyText = (TextView) findViewById(R.id.input_money_text);

    private TextView words;

    private SimpleDateFormat formatItem = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
    private SimpleDateFormat formatSum  = new SimpleDateFormat("yyyy年MM月", Locale.CHINA);
    private DecimalFormat decimalFormat = new DecimalFormat("0.00");

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        addCostBtn = (Button) findViewById(R.id.add_cost_button);
        addEarnBtn = (Button) findViewById(R.id.add_earn_button);
        addFinishBtn   = (ImageButton) findViewById(R.id.add_finish);
        addDescription = (ImageButton) findViewById(R.id.add_description);
        clearBtn = (Button) findViewById(R.id.clear);
        words = (TextView) findViewById(R.id.anime_words);
        // 设置字体颜色
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/chinese_character.ttf");
        clearBtn.setTypeface(typeface);
        words.setTypeface(typeface);

        Bundle bundle = getIntent().getExtras();
        presenter = new AddItemPresenter(this, bundle.getInt("bookId"));


//        addCostBtn.setOnClickListener(new ButtonListener());
//        addEarnBtn.setOnClickListener(new ButtonListener());
//        addFinishBtn.setOnClickListener(new ButtonListener());
//        addDescription.setOnClickListener(new ButtonListener());
//        clearBtn.setOnClickListener(new ButtonListener());


        bannerText = (TextView) findViewById(R.id.chosen_title);
        bannerImage = (ImageView) findViewById(R.id.chosen_image);

        moneyText = (TextView) findViewById(R.id.input_money_text);
        // 及时清零

        presenter.onCreate();
//        moneyText.setText("0.00");

//        manager = getSupportFragmentManager();
//
//        transaction = manager.beginTransaction();
//        transaction.replace(R.id.item_fragment, new CostFragment());
//        transaction.commit();

        addCostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onAddCostButtonClick();
            }
        });

        addEarnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onAddEarnButtonClick();
            }
        });

        addFinishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onAddFinishButtonClick();
                finish();
            }
        });

        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onClearButtonClick();
            }
        });

        addDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onDescriptionButtonClick();
            }
        });
    }

//    private class ButtonListener implements View.OnClickListener {
//        @Override
//        public void onClick(View view) {
//            transaction = manager.beginTransaction();

//            switch (view.getId()) {
//                case R.id.add_cost_button:
//                    addCostBtn.setTextColor(0xffff8c00); // 设置“支出“按钮为灰色
//                    addEarnBtn.setTextColor(0xff908070); // 设置“收入”按钮为橙色
//                    transaction.replace(R.id.item_fragment, new CostFragment());
//                    Log.d(TAG, "onClick: add_cost_button");
//
//                    break;
//                case R.id.add_earn_button:
//                    addEarnBtn.setTextColor(0xffff8c00); // 设置“收入“按钮为灰色
//                    addCostBtn.setTextColor(0xff908070); // 设置“支出”按钮为橙色
//                    transaction.replace(R.id.item_fragment, new EarnFragment());
//                    Log.d(TAG, "onClick: add_earn_button");
//
//                    break;
//                case R.id.add_finish:
//                    String moneyString =  moneyText.getText().toString();
//                    if (moneyString.equals("0.00") || GlobalVariables.getmInputMoney().equals(""))
//                        Toast.makeText(getApplicationContext(),"唔姆，你还没输入金额",Toast.LENGTH_SHORT).show();
//                    else {
//                        putItemInData(Double.parseDouble(moneyText.getText().toString()));
//                        calculatorClear();
//                        finish();
//                    }
//                    break;
//                case R.id.clear:
//                    calculatorClear();
//                    moneyText.setText("0.00");
//                    break;
//                case R.id.add_description:
//                    Intent intent = new Intent(AddItemActivity.this, AddDescription.class);
//                    startActivity(intent);
//            }
//
//            transaction.commit();
//        }
//    }

//    public void putItemInData(double money) {
//        MoneyItem moneyItem = new MoneyItem();
//        BookItem bookItem = LitePal.find(BookItem.class, GlobalVariables.getmBookId());
//        String tagName = (String) bannerText.getTag();
//        int tagType = (int) bannerImage.getTag();
//
//        if (tagType < 0) {
//            moneyItem.setInOutType(moneyItem.TYPE_COST);
//        } else moneyItem.setInOutType(moneyItem.TYPE_EARN);
//
//        moneyItem.setTypeName(bannerText.getText().toString());
//        moneyItem.setSrcName(tagName);
//        moneyItem.setMoney(money);
//        moneyItem.setDate(formatItem.format(new Date()));         // 存储记账时间
//        moneyItem.setDescription(GlobalVariables.getmDescription());
//        moneyItem.setBookId(GlobalVariables.getmBookId());
//        moneyItem.save();
//
//        // 将收支存储在对应账本下
//        bookItem.getMoneyItemList().add(moneyItem);
////        bookItem.setSumAll(bookItem.getSumAll() + money* moneyItem.getInOutType());
//        bookItem.save();
//
//        calculateMonthlyMoney(bookItem, moneyItem.getInOutType(), moneyItem);
//
//        // 存储完之后及时清空备注
//        GlobalVariables.setmDescription("");
//    }

//    // 计算月收支
//    public void calculateMonthlyMoney(BookItem bookItem, int money_type, MoneyItem moneyItem) {
//        String sumDate = formatSum.format(new Date());
//
//        // 求取月收支类型
//        if (bookItem.getDate().equals(moneyItem.getDate().substring(0, 8))) {
//            if (money_type == 1) {
//                bookItem.setSumMonthlyEarn(bookItem.getSumMonthlyEarn() + moneyItem.getMoney());
//            } else {
//                bookItem.setSumMonthlyCost(bookItem.getSumMonthlyCost() + moneyItem.getMoney());
//            }
//        } else {
//            if (money_type == 1) {
//                bookItem.setSumMonthlyEarn(moneyItem.getMoney());
//                bookItem.setSumMonthlyCost(0.0);
//            } else {
//                bookItem.setSumMonthlyCost(moneyItem.getMoney());
//                bookItem.setSumMonthlyEarn(0.0);
//            }
//            bookItem.setDate(sumDate);
//        }
//
//        bookItem.save();
//    }

    // 数字输入按钮
    public void calculatorNumOnclick(View v) {
        presenter.OnNumPadNumClick(v);

//        Button view = (Button) v;
//        String digit = view.getText().toString();
//        String money = GlobalVariables.getmInputMoney();
//        if (GlobalVariables.getmHasDot() && GlobalVariables.getmInputMoney().length()>2) {
//            String dot = money.substring(money.length() - 3, money.length() - 2);
//            Log.d(TAG, "calculatorNumOnclick: " + dot);
//            if (dot.equals(".")) {
//                Toast.makeText(getApplicationContext(), "唔，已经不能继续输入了", Toast.LENGTH_SHORT).show();
//                return;
//            }
//        }
//        GlobalVariables.setmInputMoney(money+digit);
//        moneyText.setText(decimalFormat.format(Double.valueOf(GlobalVariables.getmInputMoney())));
    }

//    // 清零按钮
//    public void calculatorClear() {
//        GlobalVariables.setmInputMoney("");
//        GlobalVariables.setHasDot(false);
//    }

    // 小数点处理工作
    public void calculatorPushDot(View view) {
        presenter.onNumPadDotClock();
//
//        if (GlobalVariables.getmHasDot()) {
//            Toast.makeText(getApplicationContext(), "已经输入过小数点了 ━ω━●", Toast.LENGTH_SHORT).show();
//        } else {
//            GlobalVariables.setmInputMoney(GlobalVariables.getmInputMoney()+".");
//            GlobalVariables.setHasDot(true);
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_DESCRIPTION) {
            if (resultCode == RESULT_OK) {
                presenter.setDescription(data.getStringExtra(Intent.EXTRA_TEXT));
            }
        }
    }

    @Override
    public void highlightEarnButton() {
        addCostBtn.setTextColor(0xff908070); // set cost button as gray
        addEarnBtn.setTextColor(0xffff8c00); // set earn button as orange
    }

    @Override
    public void highlightCostButton() {
        addEarnBtn.setTextColor(0xff908070); // set earn button as gray
        addCostBtn.setTextColor(0xffff8c00); // set cost button as orange
    }

    @Override
    public void setAmount(String numStr) {
        moneyText.setText(numStr);
    }

    @Override
    public void useEarnFragment() {
        transaction.replace(R.id.item_fragment, new EarnFragment());
    }

    @Override
    public void useCostFragment() {
        transaction.replace(R.id.item_fragment, new CostFragment());
    }

    @Override
    public void setupTransaction() {
        manager = getSupportFragmentManager();
        beginTransaction();
        transaction.replace(R.id.item_fragment, new CostFragment());
        endTransaction();
    }

    @Override
    public void beginTransaction() {
        transaction = manager.beginTransaction();
    }

    @Override
    public void endTransaction() {
        transaction.commit();
    }

    @Override
    public String getMoney() {
        return moneyText.getText().toString();
    }

    @Override
    public void navigateToDescription() {
        Intent intent = new Intent(AddItemActivity.this, DescriptionActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("description", presenter.getDescription());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void alarmNoMoneyInput() {
        Toast.makeText(getApplicationContext(),"唔姆，你还没输入金额",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void alarmCanNotContinueToInput() {
        Toast.makeText(getApplicationContext(), "唔，已经不能继续输入了", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void alarmAlreadyHasDot() {
        Toast.makeText(getApplicationContext(), "已经输入过小数点了 ━ω━●", Toast.LENGTH_SHORT).show();
    }

    @Override
    public String getTypeName() {
        return bannerText.getText().toString();
    }

    @Override
    public String getTypeImgResourceName() {
        return bannerText.getTag().toString();
    }

    @Override
    public MoneyItem.InOutType getInOutFlag() {
        Log.d(TAG, "getInOutFlag: " + (MoneyItem.InOutType)bannerImage.getTag());
        return (MoneyItem.InOutType) bannerImage.getTag();
    }

    @Override
    public String getPressedNumPadValue(View view) {
        Button button = (Button) view;
        return button.getText().toString();
    }
}
