package cdflynn.android.library.scroller;

/**
 * Provides information about a sorted collection.
 */
public interface BubbleScrollerAdapter {
    /**
     * Return the number of sections in your scrollable collection.
     */
    int getSectionCount();

    /**
     * Return a character that represents the section at {@code position}.
     *
     * @param position The zero-indexed position of the section.
     */
    String getSectionTitle(int position);

    /**
     * Return an integer that describes the size of the section at {@code position}.
     * The returned value may be of any magnitude you want, but should be comparable in relative
     * terms to other section sizes.
     *
     * @param position the zero-indexed position of the section
     */
    int getSectionSize(int position);

    /**
     * Return the total size of all sections in aggregate.
     */
    int getTotalSize();
}
