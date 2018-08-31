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

        lineSegments = new LineSegment[totalPoints*totalPoints];

        for (int i = 0; i < totalPoints; i++) {

            if (inputArr[i] == null) throw new IllegalArgumentException();

            Point targetPoint = inputArr[i];

            // Sort the points according to the slopes they make with p.
            Arrays.sort(inputArr, targetPoint.slopeOrder());

            StdOut.println("\nOrder for " + targetPoint);
            for(Point q: inputArr) {
                StdOut.println("The slope " + q + " makes with " + targetPoint + " = " + q.slopeTo(targetPoint));
            }

            // Sorted array starts with the point having the lowest slope to the target one - the point itself (NEGATIVE_INFINITY)

            int count = 0; // Count the number of points in the segment

            // Start searching for a matching slope pair from the third element
            int j = 2;
            for (; j < totalPoints; j++) {
                if (Double.compare(inputArr[j-1].slopeTo(targetPoint), inputArr[j].slopeTo(targetPoint)) == 0 ) break;
            }

            // Found the first matching pair of slopes (2 points in the segment)
            count = 2;
            Double targetSlope = inputArr[j].slopeTo(targetPoint);

            // Continue looking for more points with the same slope
            int k = j + 1; // Start from the next item in the array

            for (; k < totalPoints; k++) {
                if (Double.compare(inputArr[k].slopeTo(targetPoint), targetSlope) != 0) break;
                count++;
            }

            StdOut.println(count);
            // There are no more points with a matching slope. Count the total points and add a segment if over 3
            if (count >= 3) {
                Point segmentEnd = inputArr[k - 1];
                lineSegments[this.numberOfSegments++] = new LineSegment(targetPoint, segmentEnd);
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
            StdOut.println("Total number of segments: " + collinear.numberOfSegments());
            for (LineSegment segment : collinear.segments()) {
                StdOut.println(segment);
                segment.draw();
            }
            StdDraw.show();
        }
    }
}