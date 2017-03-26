package com.yuukidach.ucount;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.litepal.crud.DataSupport;
import org.litepal.crud.callback.FindMultiCallback;
import org.litepal.tablemanager.Connector;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import at.markushi.ui.CircleButton;

import static android.provider.CalendarContract.CalendarCache.URI;

public class MainActivity extends AppCompatActivity {
    private List<IOItem> ioItemList = new ArrayList<>();
    private RecyclerView  ioItemRecyclerView;
    private IOItemAdapter adapter;
    private Button        showBtn;
    private CircleButton  addBtn;
    private ImageView     headerImg;
    private TextView      monthlyCost, monthlyEarn;

    private Sum           sum = new Sum();

    public static String PACKAGE_NAME;
    public static Resources resources;
    public static final int SELECT_GALLERY_PIC = 1;
    public DecimalFormat decimalFormat = new DecimalFormat("0.00");

    private static final String TAG = "MainActivity";
    private SimpleDateFormat formatItem = new SimpleDateFormat("yyyy年MM月dd日");

    // 为recyclerView设置滑动动作
    private ItemTouchHelper.Callback mCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
            // 获得滑动位置
            final int position = viewHolder.getAdapterPosition();

            if (direction == ItemTouchHelper.RIGHT) {
                // 弹窗确认
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("你确定要删除么？");

                builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adapter.removeItem(position);
                        // 刷新界面
                        onResume();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LinearLayout sonView = (LinearLayout) viewHolder.itemView;
                        TextView grandsonTextView = (TextView) sonView.findViewById(R.id.iotem_date);
                        // 判断是否应该显示时间
                        if (sonView.findViewById(R.id.date_bar).getVisibility() == View.VISIBLE)
                            GlobalVariables.setmDate("");
                        else GlobalVariables.setmDate(adapter.getItemDate(position));
                        adapter.notifyItemChanged(position);
                    }
                }).show();  // 显示弹窗
            }
        }
    };
    private ItemTouchHelper itemTouchHelper = new ItemTouchHelper(mCallback);

//=============================================================================================================//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // litepal
        Connector.getDatabase();

        // 获得包名和资源，方便后面的程序使用
        PACKAGE_NAME = getApplicationContext().getPackageName();
        resources = getResources();

        showBtn = (Button) findViewById(R.id.show_money_button);
        addBtn = (CircleButton) findViewById(R.id.add_button);
        ioItemRecyclerView = (RecyclerView) findViewById(R.id.in_and_out_items);
        headerImg = (ImageView) findViewById(R.id.header_img);
        monthlyCost = (TextView) findViewById(R.id.monthly_cost_money);
        monthlyEarn = (TextView) findViewById(R.id.monthly_earn_money);

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

        setImageForHeader();
    }


    @Override
    protected void onResume() {
        super.onResume();

        initIoItemList(this);

        showBtn.setText("显示余额");

        sum.setMoneyText(sum.MONTHLY_COST, monthlyCost);
        sum.setMoneyText(sum.MONTHLY_EARN, monthlyEarn);
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();   不调用父类的方法
        Intent intent = new Intent(Intent.ACTION_MAIN);  // ACTION_MAIN  作为Task中第一个Activity启动
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);        // CATEGORY_HOME  设备启动时的第一个Activity

        startActivity(intent);
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
                        sum = DataSupport.find(Sum.class, sum.SUM);
                        String sumString = decimalFormat.format( sum.getTotal() );
                        showBtn.setText(sumString);
                    } else showBtn.setText("显示余额");
                    break;
                default:
                    break;
            }
        }
    }


    public void initIoItemList(final Context context) {
        DataSupport.findAllAsync(IOItem.class).listen(new FindMultiCallback() {
            @Override
            public <T> void onFinish(List<T> t) {
                ioItemList = (List<IOItem>) t;
                setRecyclerView(context);
            }
        });
    }

    public void selectPictureFromGallery() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
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
                saveImageUri(uri);

                // 获取永久访问图片URI的权限
                int takeFlags = data.getFlags();
                takeFlags &=(Intent.FLAG_GRANT_READ_URI_PERMISSION
                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                getContentResolver().takePersistableUriPermission(uri, takeFlags);
                break;
        }
    }

    // 利用SharedPreferences保存图片uri
    public void saveImageUri(Uri uri) {
        SharedPreferences pref = getSharedPreferences("image", MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = pref.edit();
        prefEditor.putString("uri", uri.toString());
        prefEditor.apply();
    }

    public void setImageForHeader() {
        SharedPreferences pref = getSharedPreferences("image", MODE_PRIVATE);
        String imageUri = pref.getString("uri", "");

        if (!imageUri.equals("")) {
            Uri contentUri = Uri.parse(imageUri);
            this.headerImg.setImageURI(contentUri);
        }
    }

    public void setRecyclerView(Context context) {
        // 用于存储recyclerView的日期
        GlobalVariables.setmDate("");

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setStackFromEnd(true);    // 列表从底部开始展示，反转后从上方开始展示
        layoutManager.setReverseLayout(true);   // 列表反转

        ioItemRecyclerView.setLayoutManager(layoutManager);
        adapter = new IOItemAdapter(ioItemList);
        ioItemRecyclerView.setAdapter(adapter);
        itemTouchHelper.attachToRecyclerView(ioItemRecyclerView);
    }
}
