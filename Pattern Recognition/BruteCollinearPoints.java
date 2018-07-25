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
        int totalPoints = points.length;
        if (points == null || totalPoints < 4) throw new IllegalArgumentException();

        // Initialize LineSegments array, which can be at most half the length of points (two points per segment)
        lineSegments = new LineSegment[totalPoints/2];

        // Sort points using the type's natural order to check for duplicates
        Arrays.sort(points, 0, points.length);

        /* examine 4 points at a time and check whether the three slopes
            between p and q, between p and r, and between p and s are all equal
         */

        for (int i = 0; i <= points.length - 4; i++) {
            if (points[i] == null) throw new IllegalArgumentException();
            if (points[i].slopeTo(points[i]) == points[i].slopeTo(points[i+1]) && points[i].slopeTo(points[i+2]) == points[i].slopeTo(points[i+3]) ) {
                lineSegments[numberOfSegments] = new LineSegment(points[i],points[i+3]);
            }
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