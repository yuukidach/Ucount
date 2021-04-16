package com.yuukidach.ucount;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.yuukidach.ucount.presenter.AddItemPresenter;
import com.yuukidach.ucount.presenter.DescriptionPresenter;
import com.yuukidach.ucount.view.DescriptionView;

import java.text.SimpleDateFormat;
import java.util.Date;

import at.markushi.ui.CircleButton;

public class DescriptionActivity extends AppCompatActivity implements DescriptionView {
    private EditText inputTxt;
    private TextView countTxt;
    private TextView dateTxt;
    private CircleButton doneBtn;

    private final Bundle bundle = getIntent().getExtras();

    DescriptionPresenter presenter = new DescriptionPresenter(
            this,
            bundle.getString("description")
    );

//    private final EditText inputTxt = (EditText) findViewById(R.id.page3_edit);
//    private final TextView countTxt = (TextView) findViewById(R.id.page3_count);
//    private final TextView dateTxt = (TextView) findViewById(R.id.page3_date);
//    private final CircleButton doneBtn = (CircleButton) findViewById(R.id.page3_done);

    private final SimpleDateFormat formatItem = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_descrpition);

        inputTxt = (EditText) findViewById(R.id.page3_edit);
        countTxt = (TextView) findViewById(R.id.page3_count);
        dateTxt = (TextView) findViewById(R.id.page3_date);
        doneBtn = (CircleButton) findViewById(R.id.page3_done);

        // 显示日期
        dateTxt.setText(formatItem.format(new Date()));

        // 获取焦点
        inputTxt.setFocusable(true);
        presenter.onCreated();
//        inputTxt.setText(GlobalVariables.getmDescription());
        inputTxt.setSelection(inputTxt.getText().length());
//        countTxt.setText(String.valueOf(inputTxt.getText().length()) +"/30");

        // 设置输入文本监听，实时显示字数
        inputTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                countTxt.setText(String.valueOf(s.length())+"/30");
                presenter.onTextChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onDoneButtonClick();
//                GlobalVariables.setmDescription(inputTxt.getText().toString());
                Intent intent = new Intent();
                intent.putExtra(Intent.EXTRA_TEXT, presenter.getDescription());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public String getDescriptionText() {
        return inputTxt.getText().toString();
    }

    @Override
    public void setWordsCounter(String str) {
        countTxt.setText(String.valueOf(str.length()) + "/30");
    }

    @Override
    public void setDescriptionText(String str) {
        inputTxt.setText(str);
    }

}
