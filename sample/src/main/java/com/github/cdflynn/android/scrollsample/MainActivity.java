package com.github.cdflynn.android.scrollsample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.github.cdflynn.android.scrollsample.mock.Contact;
import com.github.cdflynn.android.scrollsample.mock.Section;

import java.util.List;

import cdflynn.android.library.scroller.BubbleScroller;
import cdflynn.android.library.scroller.ScrollerListener;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    static class Views {
        BubbleScroller scroller;
        RecyclerView recycler;

        Views(MainActivity activity) {
            scroller = (BubbleScroller) activity.findViewById(R.id.bubble_scroller);
            recycler = (RecyclerView) activity.findViewById(R.id.recycler);
        }
    }

    private Views mViews;
    private ContactScrollerAdapter mContactScrollerAdapter;
    private ContactAdapter mContactAdapter;
    private LinearLayoutManager mLayoutManager;
    private boolean mProgrammaticScroll = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViews = new Views(this);
        List<Contact> contactList = Contact.mocks(this);
        mContactScrollerAdapter = new ContactScrollerAdapter(contactList);
        mContactAdapter = new ContactAdapter(this, contactList, mContactScrollerAdapter);
        mLayoutManager = new LinearLayoutManager(this);
        mViews.scroller.setScrollerListener(mScrollerListener);
        mViews.scroller.setSectionScrollAdapter(mContactScrollerAdapter);
        mViews.recycler.setLayoutManager(mLayoutManager);
        mViews.recycler.setAdapter(mContactAdapter);
        mViews.scroller.showSectionHighlight(0);
        mViews.recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (mProgrammaticScroll) {
                    mProgrammaticScroll = false;
                    return;
                }
                final int firstVisibleItemPosition = mLayoutManager.findFirstCompletelyVisibleItemPosition();
                mViews.scroller.showSectionHighlight(
                        mContactScrollerAdapter.sectionFromPosition(firstVisibleItemPosition));
            }
        });
    }

    private final ScrollerListener mScrollerListener = new ScrollerListener() {
        @Override
        public void onSectionClicked(int sectionPosition) {
            mViews.recycler.smoothScrollToPosition(
                    mContactScrollerAdapter.positionFromSection(sectionPosition));
            mProgrammaticScroll = true;
        }

        @Override
        public void onScrollPositionChanged(float percentage, int sectionPosition) {
            mViews.recycler.smoothScrollToPosition(
                    mContactScrollerAdapter.positionFromSection(sectionPosition));
            mProgrammaticScroll = true;
        }
    };
}
