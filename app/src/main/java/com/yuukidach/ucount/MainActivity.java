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

import com.yuukidach.ucount.adapter.BookItemAdapter;
import com.yuukidach.ucount.adapter.MoneyItemAdapter;
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

//    private List<BookItem> bookItemList = new ArrayList<>();

//    private RecyclerView MoneyItemRecyclerView;
    private final Button showBtn = (Button) findViewById(R.id.show_money_button);
    private final TextView monthlyCost = (TextView) findViewById(R.id.monthly_cost_money);
    private final TextView monthlyEarn = (TextView) findViewById(R.id.monthly_earn_money);
    private final ImageView headerImg = (ImageView) findViewById(R.id.header_img);
    private final CircleButton addBtn = (CircleButton) findViewById(R.id.add_button);
    private final ImageButton addBookButton = (ImageButton) findViewById(R.id.add_book_button);
    private final RecyclerView MoneyItemRecyclerView = (RecyclerView) findViewById(R.id.in_and_out_items);
//    private ImageView headerImg;
//    private TextView monthlyCost, monthlyEarn;

    // parameter for drawer
//    private DrawerLayout drawerLayout;
//    private LinearLayout bookLinearLayout;
//    private RecyclerView bookItemRecyclerView;
//    private ImageView drawerBanner;
    private final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_of_books);
    private final RecyclerView bookItemRecyclerView = (RecyclerView) findViewById(R.id.book_list);
    private final LinearLayout bookLinearLayout = (LinearLayout) findViewById(R.id.left_drawer);
    private final ImageView drawerBanner = (ImageView) findViewById(R.id.drawer_banner);

    public static String PACKAGE_NAME;
    public static Resources resources;
    public DecimalFormat decimalFormat = new DecimalFormat("0.00");

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // litepal
//        Connector.getDatabase();

        // 获得包名和资源，方便后面的程序使用
        PACKAGE_NAME = getApplicationContext().getPackageName();
        resources = getResources();

//        showBtn = (Button) findViewById(R.id.show_money_button);
//        final CircleButton addBtn = (CircleButton) findViewById(R.id.add_button);
//        final ImageButton addBookButton = (ImageButton) findViewById(R.id.add_book_button);
//        MoneyItemRecyclerView = (RecyclerView) findViewById(R.id.in_and_out_items);
//        headerImg = (ImageView) findViewById(R.id.header_img);
//        monthlyCost = (TextView) findViewById(R.id.monthly_cost_money);
//        monthlyEarn = (TextView) findViewById(R.id.monthly_earn_money);
        // drawer
//        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_of_books);
//        bookItemRecyclerView = (RecyclerView) findViewById(R.id.book_list);
//        bookLinearLayout = (LinearLayout) findViewById(R.id.left_drawer);
//        drawerBanner = (ImageView) findViewById(R.id.drawer_banner);

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
//        saveImageUri(requestCode, uri);
        mainPresenter.updateImgUtils(uri, requestCode);

        // get permanent permission to access the image
        int takeFlags = data.getFlags()
                        & (Intent.FLAG_GRANT_READ_URI_PERMISSION
                        | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        getContentResolver().takePersistableUriPermission(uri, takeFlags);
    }

//    // 利用SharedPreferences保存图片uri
//    public void saveImageUri(int id, Uri uri) {
//        SharedPreferences pref = getSharedPreferences("image" + id, MODE_PRIVATE);
//        SharedPreferences.Editor prefEditor = pref.edit();
//        prefEditor.putString("uri", uri.toString());
//        prefEditor.apply();
//    }

    @Override
    public void openPicGallery(ImageType type) {
        Log.d(TAG, "openPicGallery: " + type.ordinal());

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        startActivityForResult(intent, type.ordinal());
    }

    @Override
    public void updateHeaderImg(String uriStr) {
        Uri uri = Uri.parse(uriStr);
        this.headerImg.setImageURI(uri);
    }

    @Override
    public void updateDrawerImg(String uriStr) {
        Uri uri = Uri.parse(uriStr);
        this.drawerBanner.setImageURI(uri);
    }

//    @Override
//    public void updateHeaderImg() {
//        SharedPreferences pref = getSharedPreferences("image" + ImageType.HEADER.ordinal(),
//                MODE_PRIVATE);
//        String imageUri = pref.getString("uri", "");
//        if (!imageUri.isEmpty()) {
//            Uri contentUri = Uri.parse(imageUri);
//            this.headerImg.setImageURI(contentUri);
//        }
//    }
//
//    @Override
//    public void updateDrawerImg() {
//        SharedPreferences pref = getSharedPreferences("image" + ImageType.DRAWER.ordinal(),
//                MODE_PRIVATE);
//        String imageUri = pref.getString("uri", "");
//        if (!imageUri.isEmpty()) {
//            Uri contentUri = Uri.parse(imageUri);
//            this.drawerBanner.setImageURI(contentUri);
//        }
//    }

    @Override
    public void showBalance(String numStr) {
//        BookItem tmp = LitePal.find(BookItem.class, GlobalVariables.getmBookId());
//
//        String sumString = decimalFormat.format(tmp.getSumAll());
        showBtn.setText(numStr);
    }

    @Override
    public void hideBalance() {
        showBtn.setText(R.string.show_balance);
    }

    @Override
    public void updateMonthlyEarn(String numStr) {
//        BookItem tmp = LitePal.find(
//                BookItem.class,
//                bookItemList.get(GlobalVariables.getmBookPos()
//        ).getId());
//        monthlyEarn.setText(decimalFormat.format(tmp.getSumMonthlyEarn()));
        monthlyEarn.setText(numStr);
    }

    @Override
    public void updateMonthlyCost(String numStr) {
//        BookItem tmp = LitePal.find(
//                BookItem.class,
//                bookItemList.get(GlobalVariables.getmBookPos()
//        ).getId());
//        monthlyCost.setText(decimalFormat.format(tmp.getSumMonthlyCost()));
        monthlyCost.setText(numStr);
    }

    @Override
    public void navigateToAddItem() {
        Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
        startActivity(intent);
    }

    @Override
    public void setMainItemRecycler(List<MoneyItem> list) {
//        List<MoneyItem> moneyItemList = LitePal.where(
//                "bookId = ?",
//                String.valueOf(GlobalVariables.getmBookId())
//        ).find(MoneyItem.class);

        // 用于存储recyclerView的日期
//        GlobalVariables.setmDate("");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);    // show from bottom to top
        layoutManager.setReverseLayout(true);   // reverse the layout

        MoneyItemAdapter moneyItemAdapter = new MoneyItemAdapter(list);
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

//    @Override
//    public void setBookItemRecycler() {
//        bookItemList = LitePal.findAll(BookItem.class);
//        Log.d(TAG, "setBookItemRecycler: " + bookItemList);
//
//        if (bookItemList.isEmpty()) {
//            BookItem bookItem = new BookItem();
//            bookItem.addNewBookIntoStorage(1, "Default");
//            bookItemList = LitePal.findAll(BookItem.class);
//        }
//
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        bookItemRecyclerView.setLayoutManager(layoutManager);
//        bookItemRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
//        BookItemAdapter bookAdapter = new BookItemAdapter(bookItemList);
//        bookAdapter.setOnItemClickListener(new BookItemAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                // 选中之后的操作
//                GlobalVariables.setmBookPos(position);
//                GlobalVariables.setmBookId(bookItemList.get(position).getId());
//                drawerLayout.closeDrawer(bookLinearLayout);
//                onResume();
//            }
//        });
//
//        bookItemRecyclerView.setAdapter(bookAdapter);
//        ItemTouchHelper bookTouchHelper = new ItemTouchHelper(
//                new BookItemCallback(this, bookItemRecyclerView, bookAdapter)
//        );
//        bookTouchHelper.attachToRecyclerView(bookItemRecyclerView);
//    }

    @Override
    public void setNewBook() {
//        final BookItem bookItem = new BookItem();
        final EditText book_title = new EditText(MainActivity.this);
        // 弹窗输入
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(R.string.new_book_prompt);

        builder.setView(book_title);

        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!book_title.getText().toString().isEmpty()) {
//                    int id = bookItemList.get(bookItemList.size()-1).getId() + 1;
//                    bookItem.addNewBookIntoStorage(id, book_title.getText().toString());
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
}