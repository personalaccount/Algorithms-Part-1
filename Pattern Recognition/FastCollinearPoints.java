import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.InputMismatchException;

/**
 * Created by Philip Ivanov (https://github.com/personalaccount)
 *
 */
public class FastCollinearPoints {

    private int numberOfSegments = 0;
    private LineSegment[] lineSegments;
    private Point[] points;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] inputArr) {

        if (inputArr == null) throw new IllegalArgumentException();

        int totalPoints = inputArr.length;

        lineSegments = new LineSegment[totalPoints];

        for (int i = 0; i < totalPoints; i++) {

            // Sort the points according to the slopes they make with p.
            Arrays.sort(inputArr, inputArr[i].SLOPE_ORDER);

            // Array is now sorted, therefore target slope will always be between the first and the second entries
            double targetSlope = inputArr[0].slopeTo(inputArr[1]);

            /*  Check if any 3 (or more) adjacent points in the sorted order have equal slopes with respect to p.
                If so, these points, together with p, are collinear.
              */
            int j = 2;
            for (; j < totalPoints; j++) {
                if (Double.compare(targetSlope, inputArr[0].slopeTo(inputArr[j])) != 0) break;
            }

            if ( j >= 3) {
                lineSegments[numberOfSegments] = new LineSegment(inputArr[0], inputArr[j-1]);
                numberOfSegments++;
            }
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return numberOfSegments;
    }

    // the line segments
    public LineSegment[] segments() {
        LineSegment[] returnSegments = new LineSegment[lineSegments.length];
        for (int i = 0; i < lineSegments.length; i++) {
            returnSegments[i] = lineSegments[i];
        }
        return returnSegments;
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In("collinear-testing/input9.txt");
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            try {
                int x = in.readInt();
                int y = in.readInt();
                points[i] = new Point(x, y);
            }
            catch (InputMismatchException e) {
                points[i] = null;
            }
        }

        // draw the points
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            try {
                p.draw();
            }
            catch (NullPointerException e) {
                // Encountered a null point, skip
            }
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        if (collinear.numberOfSegments() > 0) {
            StdOut.println(collinear.numberOfSegments());
            for (LineSegment segment : collinear.segments()) {
                StdOut.println(segment);
                segment.draw();
            }
            StdDraw.show();
        }
    }
}