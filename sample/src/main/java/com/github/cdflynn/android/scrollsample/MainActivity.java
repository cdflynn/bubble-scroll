package com.github.cdflynn.android.scrollsample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import cdflynn.android.library.scroller.BubbleScroller;
import cdflynn.android.library.scroller.ScrollerListener;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    static class Views {
        BubbleScroller scroller;

        Views(MainActivity activity) {
            scroller = (BubbleScroller) activity.findViewById(R.id.bubble_scroller);
        }
    }

    private Views mViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViews = new Views(this);
        mViews.scroller.setScrollerListener(mScrollerListener);
        mViews.scroller.setSectionScrollAdapter(new NumericAdapter());
    }

    private final ScrollerListener mScrollerListener = new ScrollerListener() {
        @Override
        public void onSectionClicked(int sectionPosition) {

        }

        @Override
        public void onScrollPositionChanged(float percentage, int sectionPosition) {

        }
    };
}
