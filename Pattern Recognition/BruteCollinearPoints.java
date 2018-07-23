import java.util.Arrays;

/**
 * Examines 4 points at a time and checks whether they all lie on the same line segment,
 * returning all such line segments.
 *
 * The 4 points p, q, r, and s are collinear if three slopes
 * between p and q, between p and r, and between p and s are all equal.
 *
 * Created by Philip Ivanov (https://github.com/personalaccount)
 */


public class BruteCollinearPoints {

    private int numberOfSegments = 0;
    private LineSegment[] lineSegments;
    private Point previousPoint = null; // Aux. variable to check for argument uniqueness

    // finds all line segments containing 4 points

    /*
        Throw a java.lang.IllegalArgumentException if the argument to the constructor is null,
        if any point in the array is null, or if the argument to the constructor contains a repeated point.
     */

    public BruteCollinearPoints(Point[] points) {
        if (points == null || points.length < 4) throw new IllegalArgumentException();

        Arrays.sort(points, 0, points.length);

        for (int i = 0; i <= points.length - 4; i++) {
            if (points[i] == null) throw new IllegalArgumentException();


            /* examine 4 points at a time and check whether the three slopes
                between p and q, between p and r, and between p and s are all equal
             */
        }

    }

    // the number of line segments
    public int numberOfSegments() {
        return numberOfSegments;
    }

    // the line segments, should include each line segment containing 4 points exactly once
    public LineSegment[] segments() {
        if (numberOfSegments() == 0) return null;
        return lineSegments;
    }
}