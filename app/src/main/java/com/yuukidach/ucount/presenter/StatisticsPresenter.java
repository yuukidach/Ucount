package com.yuukidach.ucount.presenter;

import com.yuukidach.ucount.view.AddItemView;
import com.yuukidach.ucount.view.StatisticsView;

public class StatisticsPresenter {
    private final StatisticsView view;
    private int bookId;

    public StatisticsPresenter(StatisticsView view, int bookId) {
        this.view = view;
        this.bookId = bookId;
    }

    public void onCreate() {
    }

    public void onPrevButtonClick() {
        view.prevMonth();
        view.drawPieChart();
    }
    public void onNextButtonClick() {
        view.nextMonth();
        view.drawPieChart();
    }
    public void onSelectButtonClick() {
        view.selectMonth();
        view.drawPieChart();
    }
    public int getBookId(){return bookId;}


}
