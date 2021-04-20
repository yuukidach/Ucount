package com.yuukidach.ucount;

import android.content.DialogInterface;
import android.content.Intent;
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

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yuukidach.ucount.view.adapter.BookItemAdapter;
import com.yuukidach.ucount.view.adapter.MoneyItemAdapter;
import com.yuukidach.ucount.callback.BookItemCallback;
import com.yuukidach.ucount.callback.MainItemCallback;
import com.yuukidach.ucount.model.BookItem;
import com.yuukidach.ucount.model.ImgUtils;
import com.yuukidach.ucount.model.MoneyItem;
import com.yuukidach.ucount.presenter.MainPresenter;
import com.yuukidach.ucount.view.MainView;

import java.text.DecimalFormat;
import java.util.List;

import at.markushi.ui.CircleButton;

public class MainActivity extends AppCompatActivity implements MainView {
    private final ImgUtils imgUtils = new ImgUtils(this);
    private final MainPresenter mainPresenter = new MainPresenter(this, imgUtils);

    private Button showBtn;
    private ImageButton statsBtn;
    private TextView monthlyCost;
    private TextView monthlyEarn;
    private ImageView headerImg;
    private RecyclerView MoneyItemRecyclerView;

    // parameter for drawer
    private DrawerLayout drawerLayout;
    private LinearLayout bookLinearLayout;
    private RecyclerView bookItemRecyclerView;
    private ImageView drawerBanner;

    public static String PACKAGE_NAME;
    public static Resources resources;
    public DecimalFormat decimalFormat = new DecimalFormat("0.00");

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 获得包名和资源，方便后面的程序使用
        PACKAGE_NAME = getApplicationContext().getPackageName();
        resources = getResources();

        showBtn = (Button) findViewById(R.id.show_money_button);
        statsBtn = (ImageButton) findViewById(R.id.stats_button);
        monthlyCost = (TextView) findViewById(R.id.monthly_cost_money);
        monthlyEarn = (TextView) findViewById(R.id.monthly_earn_money);
        headerImg = (ImageView) findViewById(R.id.header_img);
        CircleButton addBtn = (CircleButton) findViewById(R.id.add_button);
        ImageButton addBookButton = (ImageButton) findViewById(R.id.add_book_button);
        MoneyItemRecyclerView = (RecyclerView) findViewById(R.id.in_and_out_items);
        // drawer
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_of_books);
        bookItemRecyclerView = (RecyclerView) findViewById(R.id.book_list);
        bookLinearLayout = (LinearLayout) findViewById(R.id.left_drawer);
        drawerBanner = (ImageView) findViewById(R.id.drawer_banner);

        showBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = showBtn.getText().toString();
                mainPresenter.onShowBalanceClick(str);
            }
        });

        // start activity to add cost or earning item
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToAddItem();
            }
        });

        // start activity to statistics
        statsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToStatistics();
            }
        });

        addBookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainPresenter.onAddBookClick();
            }
        });

        // 设置首页header图片长按以更换图片
        headerImg.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mainPresenter.onImageLongClick(ImageType.HEADER);
                return false;
            }
        });

        drawerBanner.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mainPresenter.onImageLongClick(ImageType.DRAWER);
                return false;
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        mainPresenter.onResume();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);  // ACTION_MAIN  作为Task中第一个Activity启动
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);        // CATEGORY_HOME  设备启动时的第一个Activity

        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null) return;
        Uri uri = data.getData();
        mainPresenter.onActivityResult(uri, requestCode);

        // get permanent permission to access the image
        int takeFlags = data.getFlags()
                        & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        getContentResolver().takePersistableUriPermission(uri, takeFlags);
    }

    @Override
    public void openPicGallery(ImageType type) {
        Log.d(TAG, "openPicGallery: " + type.ordinal());

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        startActivityForResult(intent, type.ordinal());
    }

    @Override
    public void updateHeaderImg(String uriStr) {
        // If there is no picture in SharedPreferences, then use default picture
        if (uriStr.isEmpty()) return;
        Uri uri = Uri.parse(uriStr);
        this.headerImg.setImageURI(uri);
    }

    @Override
    public void updateDrawerImg(String uriStr) {
        // If there is no picture in SharedPreferences, then use default picture
        if (uriStr.isEmpty()) return;
        Uri uri = Uri.parse(uriStr);
        this.drawerBanner.setImageURI(uri);
    }

    @Override
    public void showBalance(String numStr) {
        showBtn.setText(numStr);
    }

    @Override
    public void hideBalance() {
        showBtn.setText(R.string.show_balance);
    }

    @Override
    public void updateMonthlyEarn(String numStr) {
        monthlyEarn.setText(numStr);
    }

    @Override
    public void updateMonthlyCost(String numStr) {
        monthlyCost.setText(numStr);
    }

    @Override
    public void navigateToAddItem() {
        Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
        Bundle bundle = new Bundle();
        // tell addItemActivity which book is on
        bundle.putInt("bookId", mainPresenter.getCurBookId());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void setMainItemRecycler(List<MoneyItem> list) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);    // show from bottom to top
        layoutManager.setReverseLayout(true);   // reverse the layout

        MoneyItemAdapter moneyItemAdapter = new MoneyItemAdapter(mainPresenter, list);
        MoneyItemRecyclerView.setAdapter(moneyItemAdapter);
        MoneyItemRecyclerView.setLayoutManager(layoutManager);
        ItemTouchHelper ioTouchHelper = new ItemTouchHelper(
                new MainItemCallback(this, MoneyItemRecyclerView, moneyItemAdapter)
        );
        ioTouchHelper.attachToRecyclerView(MoneyItemRecyclerView);
    }

    @Override
    public void setBookItemRecycler(List<BookItem> list) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        bookItemRecyclerView.setLayoutManager(layoutManager);
        bookItemRecyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        );
        BookItemAdapter bookAdapter = new BookItemAdapter(mainPresenter);
        bookAdapter.setOnItemClickListener(new BookItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mainPresenter.updateBookItemView(position);
                drawerLayout.closeDrawer(bookLinearLayout);
                onResume();
            }
        });

        bookItemRecyclerView.setAdapter(bookAdapter);
        ItemTouchHelper bookTouchHelper = new ItemTouchHelper(
                new BookItemCallback(this, bookItemRecyclerView, bookAdapter)
        );
        bookTouchHelper.attachToRecyclerView(bookItemRecyclerView);
    }

    @Override
    public void setNewBook() {
        final EditText book_title = new EditText(MainActivity.this);
        // 弹窗输入
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(R.string.new_book_prompt);

        builder.setView(book_title);

        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!book_title.getText().toString().isEmpty()) {
                    mainPresenter.onAddBookConfirmClick(book_title.getText().toString());
                    onResume();
                } else {
                    // TODO: use strings.xml
                    Toast.makeText(getApplicationContext(), "没有输入新账本名称哦", Toast.LENGTH_SHORT).show();
                }
            }
        }).setNegativeButton(R.string.cancle, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).show();
    }

    @Override
    public void navigateToStatistics() {
        Intent intent = new Intent(MainActivity.this, StatisticsActivity.class);
        Bundle bundle = new Bundle();
        // tell StatisticsActivity which book is on
        bundle.putInt("bookId", mainPresenter.getCurBookId());
        intent.putExtras(bundle);
        startActivity(intent);
    }
}