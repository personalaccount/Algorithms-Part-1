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
//    private Point[] points;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] inputArr) {

        if (inputArr == null) throw new IllegalArgumentException();

        int totalPoints = inputArr.length;

        lineSegments = new LineSegment[totalPoints];

        for (int i = 0; i < totalPoints; i++) {

            if (inputArr[i] == null) throw new IllegalArgumentException();

            Point targetPoint = inputArr[i];

            // Sort the points according to the slopes they make with p.
            Arrays.sort(inputArr, targetPoint.slopeOrder());

            StdOut.println("\nOrder for " + targetPoint);
            for(Point q: inputArr) {
                StdOut.println("The slope " + q + " makes with " + targetPoint + " = " + q.slopeTo(targetPoint));
            }

            // Start from the beginning and find the starting point of a segment, which is this one.
            int j = 0;
            while (targetPoint.slopeTo(inputArr[j]) != Double.NEGATIVE_INFINITY) j++;

            double targetSlope = targetPoint.slopeTo(inputArr[++j]);

            int k = 1;
            for (; k + j < totalPoints; k++) {
                Double nextPointSlope = targetPoint.slopeTo(inputArr[k + j]);
                if (Double.compare(targetSlope, nextPointSlope) != 0) break;
            }

            if (k >= 3) {
                lineSegments[numberOfSegments] = new LineSegment(targetPoint, inputArr[k]);
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
        In in = new In("collinear-testing/input8.txt");
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