import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

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

    // finds all line segments containing 4 points

    /*
        Throw a java.lang.IllegalArgumentException if the argument to the constructor is null,
        if any point in the array is null, or if the argument to the constructor contains a repeated point.
     */

    public BruteCollinearPoints(Point[] points) {
        int totalPoints = points.length;
        if (points == null || totalPoints < 4) throw new IllegalArgumentException();

        // Initialize LineSegments array, which can be at most half the length of points array (two points per segment)
        lineSegments = new LineSegment[totalPoints/2];

        /* examine 4 points at a time and check whether the three slopes
            between p and q, between p and r, and between p and s are all equal
         */

//        Arrays.sort(points);

        for (int i = 0; i <= totalPoints - 4; i++) {
            // make sure the point is not null
            if (points[i] == null) throw new IllegalArgumentException();

            // check it it's a duplicate
            if (i < totalPoints && points[i] == points[i+1]) throw new IllegalArgumentException();

            if (points[i].slopeTo(points[i]) == points[i].slopeTo(points[i+1]) && points[i].slopeTo(points[i+2]) == points[i].slopeTo(points[i+3]) ) {
                lineSegments[numberOfSegments] = new LineSegment(points[i],points[i+3]);
                numberOfSegments++;
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

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
//        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}