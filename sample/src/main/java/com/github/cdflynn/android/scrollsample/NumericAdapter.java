package com.github.cdflynn.android.scrollsample;

import cdflynn.android.library.scroller.BubbleScrollerAdapter;

public class NumericAdapter implements BubbleScrollerAdapter {

    private static final char[] NUMBERS = "0123456789".toCharArray();

    @Override
    public int getSectionCount() {
        return NUMBERS.length;
    }

    @Override
    public char getSectionTitle(int position) {
        return NUMBERS[position];
    }

    @Override
    public int getSectionSize(int position) {
        return 1;
    }
}
