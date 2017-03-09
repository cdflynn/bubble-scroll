package cdflynn.android.library.scroller.util;

import android.graphics.Path;
import android.graphics.PathMeasure;

public class Geometry {

    private static final PathMeasure sPathMeasure = new PathMeasure();

    public static float distance(float x1, float y1, float x2, float y2) {
        final float xAbs = Math.abs(x1 - x2);
        final float yAbs = Math.abs(y1 - y2);
        return (float) Math.sqrt((yAbs * yAbs) + (xAbs * xAbs));
    }

    /**
     * Check if x and y are within affordance of each other.
     */
    public static boolean approximately(float x, float y, float affordance) {
        float difference = Math.abs(x - y);
        return difference <= affordance;
    }

    /**
     * Given some path and its length, find the point ([x,y]) on that path at
     * the given percentage of length.  Store the result in {@code points}.
     *
     * @param path    any path
     * @param length  the length of {@code path}
     * @param percent the percentage along the path's length to find a point
     * @param points  a float array of length 2, where the coordinates will be stored
     */
    public static void setPointFromPercent(Path path, float length, float percent, float[] points) {
        synchronized (sPathMeasure) {
            sPathMeasure.setPath(path, false);
            sPathMeasure.getPosTan(length * percent, points, null);
        }
    }
}
