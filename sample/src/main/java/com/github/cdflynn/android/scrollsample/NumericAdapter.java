package com.github.cdflynn.android.scrollsample;

import cdflynn.android.library.scroller.BubbleScrollerAdapter;

public class NumericAdapter implements BubbleScrollerAdapter {

    private static final String NUMBERS = "0123456789";

    @Override
    public int getSectionCount() {
        return NUMBERS.length();
    }

    @Override
    public String getSectionTitle(int position) {
        return NUMBERS.substring(position, position + 1);
    }

    @Override
    public int getSectionSize(int position) {
        return (position * 2) + 1;
    }

    @Override
    public int getTotalSize() {
        return 100;
    }
}
