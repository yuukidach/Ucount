package com.yuukidach.ucount.presenter;

import com.yuukidach.ucount.view.DescriptionView;

public class DescriptionPresenter {
    DescriptionView view;
    String description;
    final int MAX_LEN = 30;

    public DescriptionPresenter(DescriptionView view, String description) {
        this.view = view;
        this.description = description;
        restrictDescriptionLength();
    }

    public String getDescription() {
        return description;
    }

    public void onCreated() {
        view.setDescriptionText(description);
        view.setWordsCounter(description);
    }

    public void onTextChanged() {
        view.setWordsCounter(description);
    }

    public void onDoneButtonClick() {
        description = view.getDescriptionText();
        restrictDescriptionLength();
    }

    public void restrictDescriptionLength() {
        if (description.length() > MAX_LEN) {
            description = description.substring(0, MAX_LEN);
        }
    }
}
