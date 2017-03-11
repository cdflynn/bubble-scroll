package cdflynn.android.library.scroller;

import android.support.annotation.IntRange;

/**
 * Provides information about a sorted scrollable collection with sectioned contents.
 */
public interface SectionScrollAdapter {
    /**
     * Return the number of sections in your scrollable collection.<br>
     *
     * For instance, if your data set is an alphabetical list where each letter is represented
     * at least once, you would return 26.
     */
    int getSectionCount();

    /**
     * Return a title for the section at {@code position}.
     * <br>
     * In the alphabet example, returned values would be:
     * <br>
     * <pre>
     *     getSectionTitle(0)  -> "A"
     *     getSectionTitle(1)  -> "B"
     *     ...
     *     getSectionTitle(25) -> "Z"
     * </pre>
     *
     * @param position The zero-indexed position of the section.
     */
    String getSectionTitle(int position);

    /**
     * Return an integer that describes the size of the section at {@code position}.
     * The returned value may be in any arbitrary units, but should be comparable in relative
     * terms to other section sizes.  If all of your items in all sections have the same
     * visible dimensions (as is sometimes the case in a simple recycler view), you can just
     * return the number of items in the section at {@code position}.
     * <br><br>
     * Returned values should be greater than zero.  If you have an empty section that you'd like to
     * hide, instead of returning 0 here you should correctly report its absence by returning the
     * number of visible sections in {@link #getSectionCount()}, and inform the scroller of runtime
     * changes by calling {@link BubbleScroller#notifySectionsChanged()}.
     *
     * @param position the zero-indexed position of the section
     */
    @IntRange(from = 1)
    int getSectionWeight(int position);
}
