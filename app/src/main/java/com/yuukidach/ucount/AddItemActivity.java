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

import com.yuukidach.ucount.model.MoneyItem;
import com.yuukidach.ucount.presenter.AddItemPresenter;
import com.yuukidach.ucount.view.AddItemView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
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

        bannerText = (TextView) findViewById(R.id.chosen_title);
        bannerImage = (ImageView) findViewById(R.id.chosen_image);

        moneyText = (TextView) findViewById(R.id.input_money_text);

        presenter.onCreate();

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

    // 数字输入按钮
    public void calculatorNumOnclick(View v) {
        presenter.OnNumPadNumClick(v);
    }

    // 小数点处理工作
    public void calculatorPushDot(View view) {
        presenter.onNumPadDotClock();
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
        startActivityForResult(intent, REQUEST_DESCRIPTION);
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
