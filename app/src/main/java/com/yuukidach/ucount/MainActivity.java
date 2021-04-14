package com.yuukidach.ucount;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yuukidach.ucount.model.BookItem;
import com.yuukidach.ucount.model.BookItemAdapter;
import com.yuukidach.ucount.model.IOItem;
import com.yuukidach.ucount.presenter.MainPresenter;
import com.yuukidach.ucount.view.MainView;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import at.markushi.ui.CircleButton;

public class MainActivity extends AppCompatActivity implements MainView {
    private MainPresenter mainPresenter;

    private List<BookItem> bookItemList = new ArrayList<>();

    private RecyclerView ioItemRecyclerView;
    private Button showBtn;
    private ImageView headerImg;
    private TextView monthlyCost, monthlyEarn;

    // parameter for drawer
    private DrawerLayout drawerLayout;
    private LinearLayout bookLinearLayout;
    private RecyclerView bookItemRecyclerView;
    private ImageView drawerBanner;

    public static String PACKAGE_NAME;
    public static Resources resources;
    public static final int SELECT_PIC4HEADER = 1;
    public static final int SELECT_PIC4DRAWER = 2;
    public DecimalFormat decimalFormat = new DecimalFormat("0.00");

    private static final String TAG = "MainActivity";
    private SimpleDateFormat formatSum = new SimpleDateFormat("yyyy年MM月", Locale.CHINA);
    String sumDate = formatSum.format(new Date());

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
        final CircleButton addBtn = (CircleButton) findViewById(R.id.add_button);
        final ImageButton addBookButton = (ImageButton) findViewById(R.id.add_book_button);
        ioItemRecyclerView = (RecyclerView) findViewById(R.id.in_and_out_items);
        headerImg = (ImageView) findViewById(R.id.header_img);
        monthlyCost = (TextView) findViewById(R.id.monthly_cost_money);
        monthlyEarn = (TextView) findViewById(R.id.monthly_earn_money);
        // drawer
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_of_books);
        bookItemRecyclerView = (RecyclerView) findViewById(R.id.book_list);
        bookLinearLayout = (LinearLayout) findViewById(R.id.left_drawer);
        drawerBanner = (ImageView) findViewById(R.id.drawer_banner);

        showBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = showBtn.getText().toString();
                mainPresenter.toggleBalanceVisibility(str);
            }
        });

        // start activity to add cost or earning item
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToAddItem();
            }
        });

        addBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final BookItem bookItem = new BookItem();
                final EditText book_title = new EditText(MainActivity.this);
                // 弹窗输入
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("请输入新的账本名字");

                builder.setView(book_title);

                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!book_title.getText().toString().isEmpty()) {
                            bookItem.setName(book_title.getText().toString());
                            bookItem.setSumAll(0.0);
                            bookItem.setSumMonthlyCost(0.0);
                            bookItem.setSumMonthlyEarn(0.0);
                            bookItem.setDate(sumDate);
                            bookItem.save();

                            onResume();
                        } else
                            Toast.makeText(getApplicationContext(), "没有输入新账本名称哦", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();  // 显示弹窗
            }
        });

        mainPresenter = new MainPresenter(this);

        // 设置首页header图片长按以更换图片
        headerImg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                selectPictureFromGallery(1);
                return false;
            }
        });

        drawerBanner.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                selectPictureFromGallery(2);
                return false;
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
//        initBookItemList(this);
        mainPresenter.onResume();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);  // ACTION_MAIN  作为Task中第一个Activity启动
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);        // CATEGORY_HOME  设备启动时的第一个Activity

        startActivity(intent);
    }

    public void selectPictureFromGallery(int id) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        // 设置选择类型为图片类型
        intent.setType("image/*");
        // 打开图片选择
        startActivityForResult(intent, id);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) return;
        Uri uri = data.getData();
        saveImageUri(requestCode, uri);

        // get permanent permission to access the image
        int takeFlags = data.getFlags()
                        & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        getContentResolver().takePersistableUriPermission(uri, takeFlags);
    }

    // 利用SharedPreferences保存图片uri
    public void saveImageUri(int id, Uri uri) {
        SharedPreferences pref = getSharedPreferences("image" + id, MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = pref.edit();
        prefEditor.putString("uri", uri.toString());
        prefEditor.apply();
    }

    @Override
    public void updateHeaderImg() {
        SharedPreferences pref = getSharedPreferences("image" + SELECT_PIC4HEADER,
                MODE_PRIVATE);
        String imageUri = pref.getString("uri", "");
        if (!imageUri.isEmpty()) {
            Uri contentUri = Uri.parse(imageUri);
            this.headerImg.setImageURI(contentUri);
        }
    }

    @Override
    public void updateDrawerImg() {
        SharedPreferences pref = getSharedPreferences("image" + SELECT_PIC4DRAWER,
                MODE_PRIVATE);
        String imageUri = pref.getString("uri", "");
        if (!imageUri.isEmpty()) {
            Uri contentUri = Uri.parse(imageUri);
            this.drawerBanner.setImageURI(contentUri);
        }
    }

    @Override
    public void showBalance() {
        BookItem tmp = DataSupport.find(BookItem.class, GlobalVariables.getmBookId());

        String sumString = decimalFormat.format(tmp.getSumAll());
        showBtn.setText(sumString);
    }

    @Override
    public void hideBalance() {
        showBtn.setText(R.string.show_balance);
    }

    @Override
    public void updateMonthlyEarn() {
        BookItem tmp = DataSupport.find(
                BookItem.class,
                bookItemList.get(GlobalVariables.getmBookPos()
        ).getId());
        monthlyEarn.setText(decimalFormat.format(tmp.getSumMonthlyEarn()));
    }

    @Override
    public void updateMonthlyCost() {
        BookItem tmp = DataSupport.find(
                BookItem.class,
                bookItemList.get(GlobalVariables.getmBookPos()
        ).getId());
        monthlyCost.setText(decimalFormat.format(tmp.getSumMonthlyCost()));
    }

    @Override
    public void navigateToAddItem() {
        Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
        startActivity(intent);
    }

    @Override
    public void setMainItemRecycler() {
        List<IOItem> ioItemList = DataSupport.where(
                "bookId = ?",
                String.valueOf(GlobalVariables.getmBookId())
        ).find(IOItem.class);

        // 用于存储recyclerView的日期
        GlobalVariables.setmDate("");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);    // show from bottom to top
        layoutManager.setReverseLayout(true);   // reverse the layout

        MainItemAdapter ioAdapter = new MainItemAdapter(ioItemList);
        ioItemRecyclerView.setAdapter(ioAdapter);
        ioItemRecyclerView.setLayoutManager(layoutManager);
        ItemTouchHelper ioTouchHelper = new ItemTouchHelper(
                new MainItemCallback(this, ioAdapter)
        );
        ioTouchHelper.attachToRecyclerView(ioItemRecyclerView);
    }

    @Override
    public void setBookItemRecycler() {
        bookItemList = DataSupport.findAll(BookItem.class);

        if (bookItemList.isEmpty()) {
            BookItem bookItem = new BookItem();

            bookItem.saveBook(bookItem, 1, "默认账本");
            bookItem.setSumAll(0.0);
            bookItem.setSumMonthlyCost(0.0);
            bookItem.setSumMonthlyEarn(0.0);
            bookItem.setDate(sumDate);
            bookItem.save();

            bookItemList = DataSupport.findAll(BookItem.class);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        bookItemRecyclerView.setLayoutManager(layoutManager);
        bookItemRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        BookItemAdapter bookAdapter = new BookItemAdapter(bookItemList);
        bookAdapter.setOnItemClickListener(new BookItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // 选中之后的操作
                GlobalVariables.setmBookPos(position);
                GlobalVariables.setmBookId(bookItemList.get(position).getId());
                onResume();
                drawerLayout.closeDrawer(bookLinearLayout);
            }
        });

        bookItemRecyclerView.setAdapter(bookAdapter);
        ItemTouchHelper bookTouchHelper = new ItemTouchHelper(
                new BookItemCallback(this, bookAdapter)
        );
        bookTouchHelper.attachToRecyclerView(bookItemRecyclerView);

    }
}