package com.github.cdflynn.android.scrollsample;

import cdflynn.android.library.scroller.SectionScrollAdapter;

public class NumericAdapter implements SectionScrollAdapter {

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
    public int getSectionWeight(int position) {
        return (position * 2) + 1;
    }
}
