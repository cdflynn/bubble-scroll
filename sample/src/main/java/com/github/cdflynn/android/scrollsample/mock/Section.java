package com.github.cdflynn.android.scrollsample.mock;

public class Section {
    private int mIndex;
    private String mTitle;
    private int mWeight;
    
    public Section(int index, String title, int weight) {
        mIndex = index;
        mTitle = title;
        mWeight = weight;
    }

    public int getIndex() {
        return mIndex;
    }

    public String getTitle() {
        return mTitle;
    }

    public int getWeight() {
        return mWeight;
    }

    @Override
    public String toString() {
        return "Section{" +
                "mIndex=" + mIndex +
                ", mTitle='" + mTitle + '\'' +
                ", mWeight=" + mWeight +
                '}';
    }
}
