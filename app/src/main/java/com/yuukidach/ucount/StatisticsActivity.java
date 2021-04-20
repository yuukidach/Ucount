package com.yuukidach.ucount;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.yuukidach.ucount.model.MoneyItem;
import com.yuukidach.ucount.presenter.StatisticsPresenter;
import com.yuukidach.ucount.view.StatisticsView;

import org.litepal.LitePal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class StatisticsActivity extends AppCompatActivity implements StatisticsView {
    private static final String TAG = "StatisticsActivity";
    private StatisticsPresenter presenter;

    private TextView selectText;

    private Calendar calendar;
    private String yearMonth;
    private SimpleDateFormat fmtYM;

    private final int[]  PIE_COLORS={
            Color.rgb(181, 194, 202), Color.rgb(129, 216, 200), Color.rgb(241, 214, 145),
            Color.rgb(108, 176, 223), Color.rgb(195, 221, 155), Color.rgb(251, 215, 191),
            Color.rgb(237, 189, 189), Color.rgb(172, 217, 243)
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        ImageButton prevBtn = (ImageButton) findViewById(R.id.prev_month);
        ImageButton nextBtn = (ImageButton) findViewById(R.id.next_month);
        selectText = (TextView) findViewById(R.id.selected_month);

        Bundle bundle = getIntent().getExtras();
        presenter = new StatisticsPresenter(this, bundle.getInt("bookId"));
        presenter.onCreate();

        fmtYM = new SimpleDateFormat("yyyy-MM", Locale.getDefault());
        calendar = Calendar.getInstance();
        yearMonth = fmtYM.format(calendar.getTime());
        selectText.setText(yearMonth);
        Log.d("calendar", "format:"+ yearMonth);
        drawPieChart();

        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onPrevButtonClick();
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onNextButtonClick();
            }
        });
    }

    @Override
    public void prevMonth() {
        calendar.add(Calendar.MONTH, -1);
        yearMonth = fmtYM.format(calendar.getTime());
        Log.d("calendar", "format:"+ fmtYM.format(calendar.getTime()));
        selectText.setText(yearMonth);
    }

    @Override
    public void nextMonth() {
        calendar.add(Calendar.MONTH, 1);
        yearMonth = fmtYM.format(calendar.getTime());
        Log.d("calendar", "format:"+ fmtYM.format(calendar.getTime()));
        selectText.setText(yearMonth);
    }

    @Override
    public void selectMonth() {

    }

    @Override
    public void drawPieChart() {
        PieChart chart_cost = (PieChart) findViewById(R.id.chart_cost);
        PieChart chart_earn = (PieChart) findViewById(R.id.chart_earn);
        List<PieEntry> entries_cost = new ArrayList<PieEntry>();
        List<PieEntry> entries_earn = new ArrayList<PieEntry>();
        Cursor cursor_cost = LitePal.findBySQL("select sum(money),typename from MoneyItem " +
                "where bookId = ? and " +
                "inOutType = ? and " +
                "date like ? " +
                "group by typename", String.valueOf(presenter.getBookId()), MoneyItem.InOutType.COST.toString(), yearMonth+"%");
        Cursor cursor_earn = LitePal.findBySQL("select sum(money),typename from MoneyItem " +
                "where bookId = ? and " +
                "inOutType = ? and " +
                "date like ? " +
                "group by typename", String.valueOf(presenter.getBookId()), MoneyItem.InOutType.EARN.toString(), yearMonth+"%");
        if (cursor_cost != null && cursor_cost.moveToFirst()) {
            do {
                Log.d("database", "#######"+cursor_cost.getString(1)+"########");
                Log.d("database", "#######"+cursor_cost.getDouble(0)+"########");
                entries_cost.add(new PieEntry((float) cursor_cost.getDouble(cursor_cost.getColumnIndex("sum(money)")),
                        cursor_cost.getString(cursor_cost.getColumnIndex("typename"))));
            } while (cursor_cost.moveToNext());
        }
        if (cursor_earn != null && cursor_earn.moveToFirst()) {
            do {
                Log.d("database", "#######"+cursor_earn.getString(1)+"########");
                Log.d("database", "#######"+cursor_earn.getDouble(0)+"########");
                entries_earn.add(new PieEntry((float) cursor_earn.getDouble(cursor_earn.getColumnIndex("sum(money)")),
                        cursor_earn.getString(cursor_earn.getColumnIndex("typename"))));
            } while (cursor_earn.moveToNext());
        }
        PieDataSet dataSet_cost = new PieDataSet(entries_cost, "");
        dataSet_cost.setColors(PIE_COLORS);
        dataSet_cost.setValueLinePart1OffsetPercentage(60f);
        dataSet_cost.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet_cost.setValueLinePart1Length(0.4f);
        dataSet_cost.setValueLinePart2Length(0.4f);
        PieData pieData_cost = new PieData(dataSet_cost);
        pieData_cost.setValueTextSize(18f);

        Legend l = chart_cost.getLegend();
        l.setTextSize(15f);
        l.setFormSize(12f);
        l.setXEntrySpace(10f);
        chart_cost.setData(pieData_cost);
        chart_cost.getDescription().setText("");
        chart_cost.setExtraOffsets(10f, 0, 10f, 0);
        chart_cost.setEntryLabelColor(0xff000000);
        chart_cost.setEntryLabelTextSize(15f);
        chart_cost.invalidate();
        
        PieDataSet dataSet_earn = new PieDataSet(entries_earn, "");
        dataSet_earn.setColors(PIE_COLORS);
        dataSet_earn.setValueLinePart1OffsetPercentage(60f);
        dataSet_earn.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet_earn.setValueLinePart1Length(0.4f);
        dataSet_earn.setValueLinePart2Length(0.4f);
        PieData pieData_earn = new PieData(dataSet_earn);
        pieData_earn.setValueTextSize(18f);

        l = chart_earn.getLegend();
        l.setTextSize(15f);
        l.setFormSize(12f);
        l.setXEntrySpace(10f);
        chart_earn.setData(pieData_earn);
        chart_earn.getDescription().setText("");
        chart_earn.setExtraOffsets(10f, 0, 10f, 0);
        chart_earn.setEntryLabelColor(0xff000000);
        chart_earn.setEntryLabelTextSize(15f);
        chart_earn.invalidate();

    }
}
