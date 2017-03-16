package com.yuukidach.ucount;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import at.markushi.ui.CircleButton;

public class MainActivity extends AppCompatActivity {
    private List<IOItem> ioItemList = new ArrayList<>();
    private RecyclerView  ioItemRecyclerView;
    private IOItemAdapter adapter;
    private Button        showBtn;
    private CircleButton  addBtn;
    private ImageView     headerImg;

    private Sum           sum;

    public static String PACKAGE_NAME;
    public static final int SELECT_GALLERY_PIC = 1;
    public DecimalFormat decimalFormat = new DecimalFormat("0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 获得包名，方便后面的程序使用
        PACKAGE_NAME = getApplicationContext().getPackageName();

        // litepal
        Connector.getDatabase();

        initIoItemList();

        showBtn = (Button) findViewById(R.id.show_money_button);
        addBtn = (CircleButton) findViewById(R.id.add_button);
        ioItemRecyclerView = (RecyclerView) findViewById(R.id.in_and_out_items);
        headerImg = (ImageView) findViewById(R.id.header_img);

        // 设置按钮监听
        showBtn.setOnClickListener(new ButtonListener());
        addBtn.setOnClickListener(new ButtonListener());

        // 设置首页header图片长按以更换图片
        headerImg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                selectPictureFromGallery();
                return false;
            }
        });

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
                        String sumString = decimalFormat.format( sum.getTotal() );
                        showBtn.setText(sumString);
                    } else showBtn.setText("显示余额");
                    break;
                default:
                    break;
            }
        }
    }


    public void initIoItemList() {
        ioItemList = DataSupport.findAll(IOItem.class);
    }

    public void selectPictureFromGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        // 设置选择类型为图片类型
        intent.setType("image/*");
        // 打开图片选择
        startActivityForResult(intent, SELECT_GALLERY_PIC);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case SELECT_GALLERY_PIC:
                if (data == null) return;
                // 用户从图库选择图片后会返回所选图片的Uri
                Uri uri = data.getData();
                this.headerImg.setImageURI(uri);
                break;
        }
    }
}
