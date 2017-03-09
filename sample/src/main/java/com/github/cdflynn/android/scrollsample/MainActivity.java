package com.github.cdflynn.android.scrollsample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cdflynn.android.library.scroller.BubbleScroller;

public class MainActivity extends AppCompatActivity {

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
    }
}
