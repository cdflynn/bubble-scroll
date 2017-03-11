package cdflynn.android.library.scroller;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import cdflynn.android.library.scroller.util.Geometry;

// TODO:

/**
 * - highlight current section header
 * - make the highlight styleable
 * - Use String instead of char for headers
 * - fix the weird long press thing
 * - Use gravity to place the header text top, center, or bottom
 * - Consider attaching {@link android.support.v4.view.ScrollingView} as the adapter
 */
public class BubbleScroller extends View {

    private static final String TAG = BubbleScroller.class.getSimpleName();

    private static final float CIRCLE_RADIUS = 200F;
    private static final long ANIM_DURATION = 150L;
    private static final int INTRINSIC_VERTICAL_PADDING = 40;
    private static final float TEXT_SCALE_FACTOR_MAX = 1.3F;
    private static final float TEXT_SCALE_FACTOR_MIN = 0.7F;
    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int TEXT_SIZE_DEFAULT = 50;
    private static final int TEXT_COLOR_DEFAULT = Color.BLACK;
    private static final boolean DEBUG = false;

    /**
     * A default adapter that shows the alphabet, evenly spaced.
     */
    private static final BubbleScrollerAdapter ADAPTER_DEFAULT = new BubbleScrollerAdapter() {
        @Override
        public int getSectionCount() {
            return ALPHABET.length();
        }

        @Override
        public String getSectionTitle(int position) {
            return ALPHABET.substring(position, position + 1);
        }

        @Override
        public int getSectionSize(int position) {
            return 1; // all equal size
        }

        @Override
        public int getTotalSize() {
            return ALPHABET.length();
        }
    };

    /**
     * Reports the start of a scroll
     */
    private GestureDetector mGestureDetector;
    private BubbleScrollerAdapter mAdapter;
    /**
     * The path along which to draw text
     */
    private Path mTextPath;
    private Paint mDebugPaint;
    /**
     * Text appearance
     */
    private TextPaint mTextPaint;
    /**
     * The base size of the text.  This will be scaled up and down as each section
     * is approached and passed respectively.
     */
    private float mTextSize;
    /**
     * The color of the text.
     */
    @ColorInt
    private int mTextColor;
    /**
     * A rectangle inside this view's bounds, that represents the drawable area.  This is intended
     * to account for padding.
     */
    private RectF mDrawableRect;
    /**
     * A rectangle around the bumper circle.  This is useful because only the newest Path APIs
     * have the ability to add arcs with raw {@code left, top, right, bottom} values.  Older Path
     * APIs demand you pass a {@link android.graphics.Rect} or {@link RectF}.
     */
    private RectF mCircleRect;
    /**
     * The current center point of the bumper circle.  This will animate back and forth as
     * the circle protrudes the scroll line.
     */
    private PointF mCircleCenter;
    /**
     * Where the text path starts.
     */
    private PointF mTextStart;
    /**
     * Where the text path ends.
     */
    private PointF mTextEnd;
    /**
     * Up to 2 y coordinates where the bumper circle intersects the vertical scrolling line.
     */
    private float[] mYIntersect = new float[2];
    /**
     * Horizontal offset values for each instance of text on the scroll line.  These are re-computed
     * as the bumper circle moves vertically and laterally.
     */
    private int[] mHorizontalOffsets;
    /**
     * Vertical offset values for each instance of text on the scroll line.  These are re-computed
     * when the number or relative magnitude of sections changes.
     */
    private int[] mVerticalOffsets;
    /**
     * The number of sections that this view will display.  This is set every time the adapter changes,
     * or when {@link #notifySectionsChanged()} is called.
     */
    private int mSectionCount;
    /**
     * The total size of all sections in aggregate.  This is set every time the adapter changes,
     * or when {@link #notifySectionsChanged()} is called.
     */
    private int mTotalSize;
    /**
     * The scale factor for each instance of text on the scroll line.  These are re-computed
     * as the bumper circle moves vertically and laterally.
     */
    private float[] mScaleFactors;
    /**
     * Cache of section titles, preventing frequent calls to the adapter.
     */
    private String[] mTitleHolder;
    /**
     * An animator to mutate the bumper circle X position over time.
     */
    private ValueAnimator mAnimator;
    /**
     * The x coordinate where the bumper circle is not intersecting the scroll line.
     */
    private int mBumperCircleXResting;
    /**
     * The x coordinate where the bumper circle is at it's most protruding.
     */
    private int mBumperCircleXProtruding;
    /**
     * The x coordinate of the vertical scroll line.
     */
    private int mHorizontalBaseline;
    /**
     * The farthest absolute distance that section headers will protrude.
     */
    private int mMaxHorizontalOffset;

    public BubbleScroller(Context context) {
        super(context);
        init(context, null);
    }

    public BubbleScroller(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BubbleScroller(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BubbleScroller(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    /**
     * Attach an adapter to inform this view of custom section values.
     */
    public void setBubbleScrollerAdapter(BubbleScrollerAdapter adapter) {
        if (adapter == null) {
            mAdapter = ADAPTER_DEFAULT;
        } else {
            mAdapter = adapter;
        }
        notifySectionsChangedInternal();
    }

    /**
     * Inform this view that the section details have changed, and it should re-calculate spacing
     * and display based on this new information.  The {@link BubbleScrollerAdapter} will be called
     * to assess the new state.
     */
    public void notifySectionsChanged() {
        notifySectionsChangedInternal();
    }

    private void init(Context c, @Nullable AttributeSet attrs) {
        resolveXmlAttributes(c, attrs);
        setClickable(true);
        setLongClickable(false);
        mGestureDetector = new GestureDetector(c, mGestureListener);
        mTextPath = new Path();
        mCircleCenter = new PointF();
        mTextStart = new PointF();
        mTextEnd = new PointF();
        mAnimator = ValueAnimator.ofFloat(0, 1);
        mDebugPaint = createDebugPaint(ContextCompat.getColor(c, R.color.green));
        mTextPaint = createTextPaint(mTextColor, mTextSize);
        mDrawableRect = new RectF();
        mCircleRect = new RectF();
        setBubbleScrollerAdapter(ADAPTER_DEFAULT);
    }

    private void resolveXmlAttributes(Context c, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }

        TypedArray a = c.getTheme().obtainStyledAttributes(attrs, R.styleable.BubbleScroller, 0, 0);

        try {
            mTextColor = a.getColor(R.styleable.BubbleScroller_bubbleScroller_textColor, TEXT_COLOR_DEFAULT);
            mTextSize = a.getDimension(R.styleable.BubbleScroller_bubbleScroller_textSize, TEXT_SIZE_DEFAULT);
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            mDrawableRect.left = getPaddingLeft();
            mDrawableRect.top = getPaddingTop() + INTRINSIC_VERTICAL_PADDING;
            mDrawableRect.right = right - getPaddingRight();
            mDrawableRect.bottom = bottom - getPaddingBottom();

            mTextStart.x = mDrawableRect.centerX();
            mTextStart.y = mDrawableRect.top;
            mTextEnd.x = mDrawableRect.centerX();
            mTextEnd.y = mDrawableRect.bottom;

            mBumperCircleXResting = (int) (mDrawableRect.centerX() + CIRCLE_RADIUS);
            mBumperCircleXProtruding = (int) (mDrawableRect.centerX() + CIRCLE_RADIUS / 2);

            mHorizontalBaseline = (int) mTextStart.x;

            mMaxHorizontalOffset = Math.abs(mHorizontalBaseline - mBumperCircleXProtruding);

            setCircleCenter(mBumperCircleXResting, mDrawableRect.centerY());
            notifySectionsChangedInternal();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (DEBUG) {
            canvas.drawPath(mTextPath, mDebugPaint);
        }

        for (int i = 0; i < mSectionCount; i++) {

            final float verticalPosition = mVerticalOffsets[i];
            final float horizontalPosition = mHorizontalBaseline - mHorizontalOffsets[i];
            mTextPaint.setTextSize(mScaleFactors[i] * mTextSize);

            if (mTitleHolder[i] == null) {
                mTitleHolder[i] = mAdapter.getSectionTitle(i);
            }

            canvas.drawText(mTitleHolder[i], 0, mTitleHolder[i].length(), horizontalPosition,
                    verticalPosition, mTextPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                animateCircleTranslationX(mBumperCircleXResting);
            default:
                // do nothing
        }
        return mGestureDetector.onTouchEvent(event) || super.onTouchEvent(event);
    }

    // region Private Methods
    private Paint createDebugPaint(@ColorInt int color) {
        Paint p = new Paint();
        p.setStrokeWidth(20);
        p.setStyle(Paint.Style.STROKE);
        p.setAntiAlias(true);
        p.setStrokeCap(Paint.Cap.ROUND);
        p.setColor(color);
        return p;
    }

    private TextPaint createTextPaint(@ColorInt int color, float textSize) {
        TextPaint p = new TextPaint();
        p.setTextAlign(Paint.Align.CENTER);
        p.setTextSize(textSize);
        p.setColor(color);
        return p;
    }

    /**
     * Calculate the two y coordinate values on vertical line {@code atX} that intersects with a circle
     * at center {@code withCircle} and radius {@code andRadius}.  Once calculated, place the results into
     * the given float array of length 2.
     * If there is only one intersecting point, the value will be placed into both
     * {@code intoFloatArray[0]} and {@code intoFloatArray[1]}.
     *
     * @param atX            the vertical line
     * @param withCircle     the circle center point
     * @param andRadius      the radius of the circle
     * @param intoFloatArray the float array that will hold both y intersect values.
     */
    private void setYIntersect(float atX, PointF withCircle, float andRadius, float[] intoFloatArray) {
        if (intoFloatArray.length < 2) {
            throw new IllegalArgumentException("Must pass a float array of at least length = 2");
        }

        final float horizontalDistance = Math.abs(withCircle.x - atX);

        if (horizontalDistance > andRadius) {
            intoFloatArray[0] = withCircle.y;
            intoFloatArray[1] = withCircle.y;
            return;
        }

        if (Geometry.approximately(horizontalDistance, andRadius, 0.1f)) {
            intoFloatArray[0] = withCircle.y;
            intoFloatArray[1] = withCircle.y;
            return;
        }

        final float b = (float) Math.sqrt((andRadius * andRadius) - (horizontalDistance * horizontalDistance));
        intoFloatArray[0] = withCircle.y - b;
        intoFloatArray[1] = withCircle.y + b;

    }

    /**
     * Given a circle of {@code radius}, y intersects {@code yIntersections}, {@code distance}
     * from the vertical baseline, and a total drawable area of height {@code height}, calculate
     * {@code intoArray.length} horizontal offsets at evenly spaced vertical positions and write them
     * to {@code intoArray}.
     *
     * @param distance        How far is the circle from the vertical baseline?
     * @param radius          How large is the circle's radius?
     * @param yIntersections  Where does the circle intersect the vertical baseline?
     * @param verticalOffsets The vertical position offset for each section.
     * @param intoArray       The array to write each horizontal offset value.
     */
    private void setHorizontalOffsets(float distance, float radius, float[] yIntersections,
                                      int[] verticalOffsets, int[] intoArray) {
        final int count = intoArray.length;
        for (int i = 0; i < count; i++) {
            final float verticalPosition = verticalOffsets[i];
            if (verticalPosition <= yIntersections[0]) {
                intoArray[i] = 0;
                continue;
            }

            if (verticalPosition >= yIntersections[1]) {
                intoArray[i] = 0;
                continue;
            }

            final int arcHeight = (int) (yIntersections[1] - yIntersections[0]);
            final int distanceInsideArcHeight = (int) (verticalPosition - yIntersections[0]);
            final int b = Math.abs(arcHeight / 2 - distanceInsideArcHeight);

            final int sideALength = (int) Math.sqrt((radius * radius) - (b * b));
            intoArray[i] = (int) (sideALength - distance);
        }
    }

    private void setVerticalOffsets(int[] intoArray) {
        final int totalSize = mTotalSize;
        final float height = mDrawableRect.height();
        int offset = INTRINSIC_VERTICAL_PADDING;

        for (int i = 0; i < mSectionCount; i++) {
            final float sectionSize = mAdapter.getSectionSize(i);
            intoArray[i] = offset;
            offset += (sectionSize / (float) totalSize) * height;
        }
    }

    /**
     * Set the center of the bumper circle.  This will also update the circle's bounding box.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     */
    private void setCircleCenter(float x, float y) {
        mCircleCenter.x = x;
        mCircleCenter.y = y;
        mCircleRect.left = mCircleCenter.x - CIRCLE_RADIUS;
        mCircleRect.top = mCircleCenter.y - CIRCLE_RADIUS;
        mCircleRect.right = mCircleCenter.x + CIRCLE_RADIUS;
        mCircleRect.bottom = mCircleCenter.y + CIRCLE_RADIUS;
    }

    /**
     * Calculate a scale factor for each horizontal offset, based on its protrusion.
     *
     * @param horizontalOffsets How far each text line is offset, based on the circle bumper.
     * @param intoArray         The array to store the scale factor for each line.
     */
    private void setScaleFactors(int[] horizontalOffsets, float[] intoArray) {
        if (horizontalOffsets.length != intoArray.length) {
            throw new IllegalArgumentException("Tried to compute scale factors, but the destination array length" +
                    " does not match the horizontal offsets array length.");
        }

        for (int i = 0; i < horizontalOffsets.length; i++) {
            if (horizontalOffsets[i] == 0) {
                intoArray[i] = TEXT_SCALE_FACTOR_MIN;
                continue;
            }

            intoArray[i] = TEXT_SCALE_FACTOR_MIN + ((TEXT_SCALE_FACTOR_MAX - TEXT_SCALE_FACTOR_MIN)
                    * (horizontalOffsets[i] / (float)mMaxHorizontalOffset));
        }
    }

    /**
     * Use the current position of the bumper circle to calculate the text path, the y intersection points,
     * and the scale factors for each text line.  This is typically invoked after mutating those values
     * and just before calling invalidate() to prompt the next draw call.
     */
    private void calculateDrawableElements() {
        setYIntersect(mTextStart.x, mCircleCenter, CIRCLE_RADIUS, mYIntersect);
        setHorizontalOffsets(mCircleCenter.x - mHorizontalBaseline,
                CIRCLE_RADIUS,
                mYIntersect,
                mVerticalOffsets,
                mHorizontalOffsets);
        setScaleFactors(mHorizontalOffsets, mScaleFactors);
        final boolean drawArc = mYIntersect[0] != mYIntersect[1];
        mTextPath.reset();
        mTextPath.moveTo(mTextStart.x, Math.min(mTextStart.y, mYIntersect[0]));
        if (drawArc) {
            final float absSweepAngle = sweepAngle(Math.abs(mCircleCenter.x - mTextStart.x), CIRCLE_RADIUS);
            final float startAngle = 180 + absSweepAngle / 2;
            mTextPath.lineTo(mTextStart.x, mYIntersect[0]);
            mTextPath.arcTo(mCircleRect, startAngle, -absSweepAngle, false);
            mTextPath.moveTo(mTextStart.x, mYIntersect[1]);
        }

        mTextPath.lineTo(mTextEnd.x, mTextEnd.y);
        mTextPath.close();
    }

    /**
     * Calculate the sweep angle of the protruding bumper.
     *
     * @param horizontalDistance The distance from the center of the circle to the vertical scroll line.
     * @param circleRadius       The circle's radius
     */
    private float sweepAngle(float horizontalDistance, float circleRadius) {
        return (float) (2 * Math.toDegrees(Math.acos((horizontalDistance / circleRadius))));
    }

    private void animateCircleTranslationX(final int translationX) {
        mAnimator.removeAllUpdateListeners();
        mAnimator.removeAllListeners();
        mAnimator.cancel();
        mAnimator = ValueAnimator.ofInt((int) mCircleCenter.x, translationX);
        mAnimator.setDuration(ANIM_DURATION);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                final int animatedValue = (int) animation.getAnimatedValue();
                setCircleCenter(animatedValue, mCircleCenter.y);
                calculateDrawableElements();
                invalidate();
            }
        });
        mAnimator.start();
    }

    private void notifySectionsChangedInternal() {
        if (!ViewCompat.isLaidOut(this) && !isInLayout()) {
            // we'll come back to this in the layout pass.
            return;
        }

        final int sectionCount = mAdapter.getSectionCount();
        if (sectionCount != mSectionCount) { // if the section count changed
            mSectionCount = sectionCount;
            mTotalSize = mAdapter.getTotalSize();
            mHorizontalOffsets = new int[sectionCount];
            mVerticalOffsets = new int[sectionCount];
            mScaleFactors = new float[sectionCount];
            mTitleHolder = new String[sectionCount];
        }
        setVerticalOffsets(mVerticalOffsets);
        calculateDrawableElements();
        invalidate();
    }

    private final GestureDetector.OnGestureListener mGestureListener = new GestureDetector.OnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            setCircleCenter(mCircleCenter.x, e.getY());
            animateCircleTranslationX(mBumperCircleXProtruding);
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {
            // do nothing
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            // TODO: highlight location
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            setCircleCenter(mCircleCenter.x, e2.getY());
            calculateDrawableElements();
            invalidate();
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            // do nothing
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return true;
        }
    };
}
