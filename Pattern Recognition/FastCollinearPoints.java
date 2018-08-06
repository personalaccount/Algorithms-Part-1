import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

/**
 * Created by Philip Ivanov (https://github.com/personalaccount)
 *
 */
public class FastCollinearPoints {

    private int numberOfSegments = 0;
    private LineSegment[] lineSegments;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] inputArr) {
        if (inputArr == null) throw new IllegalArgumentException();
        inputArr[0].slopeOrder();
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
        In in = new In("collinear-testing/input20.txt");
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }



    }

    public static void main2(String[] args) {
        // read the n points from a file
        In in = new In("collinear-testing/input20.txt");
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