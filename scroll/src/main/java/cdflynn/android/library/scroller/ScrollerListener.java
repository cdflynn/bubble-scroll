package cdflynn.android.library.scroller;

public interface ScrollerListener {
    /**
     * A section at {@code position} has been clicked.  You may wish to
     * update the scroll position to show that section.
     */
    void onSectionClicked(int sectionPosition);

    /**
     * The scroll percentage has changed.  This is usually because the user is manually scrolling
     * over the section headers.  You may wish to reflect this change in your scrollable collection.
     *
     * @param percentage      The current percentage progress from start to end, 0 <= percentage <= 1
     * @param sectionPosition The section index associated with this progress
     */
    void onScrollPositionChanged(float percentage, int sectionPosition);
}
