/******************************************************************************
 *  An immutable data type for points in the plane.
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 * Extended by Philip Ivanov (https://github.com/personalaccount)
 ******************************************************************************/

import java.util.Comparator;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class Point implements Comparable<Point> {

    private final int x;     // x-coordinate of this point
    private final int y;     // y-coordinate of this point

    /**
     * Initializes a new point.
     *
     * @param  x the <em>x</em>-coordinate of the point
     * @param  y the <em>y</em>-coordinate of the point
     */
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    /**
     * Draws this point to standard draw.
     */
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    /**
     * Draws the line segment between this point and the specified point
     * to standard draw.
     *
     * @param that the other point
     */
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    /**
     * Returns the slope between this point and the specified point.
     * Formally, if the two points are (x0, y0) and (x1, y1), then the slope
     * is (y1 - y0) / (x1 - x0). For completeness, the slope is defined to be
     * +0.0 if the line segment connecting the two points is horizontal;
     * Double.POSITIVE_INFINITY if the line segment is vertical;
     * and Double.NEGATIVE_INFINITY if (x0, y0) and (x1, y1) are equal.
     *
     * @param  that the other point
     * @return the slope between this point and the specified point
     */
    public double slopeTo(Point that) {

        if (this.x == that.x && this.y == that.y) return Double.NEGATIVE_INFINITY; // Point is itself

        double dY = that.y - y;
        double dX = that.x - x;

        if (dY == 0) return +0.0; // Vertical
        if (dX == 0) return Double.POSITIVE_INFINITY; // Horizontal

        return dY / dX;
    }

    /**
     * Compares two points by y-coordinate, breaking ties by x-coordinate.
     * Formally, the invoking point (x0, y0) is less than the argument point
     * (x1, y1) if and only if either y0 < y1 or if y0 = y1 and x0 < x1.
     *
     * @param  that the other point
     * @return the value <tt>0</tt> if this point is equal to the argument
     *         point (x0 = x1 and y0 = y1);
     *         a negative integer if this point is less than the argument
     *         point; and a positive integer if this point is greater than the
     *         argument point
     */
    public int compareTo(Point that) {
        if (y < that.y) return -1;
        if (y > that.y) return +1;
        // otherwise y's are equal
        if (x < that.x) return -1;
        if (x > that.x) return +1;
        return 0;
    }

    /**
     * Compares two points by the slope they make with this point.
     * The slope is defined as in the slopeTo() method.
     *
     * @return the Comparator that defines this ordering on points
     */
    public Comparator<Point> slopeOrder() {

        return new SlopeOrder();
    }

    // Define a nested inner class that implements the Comparator interface
    private class SlopeOrder implements Comparator<Point> {

        public int compare(Point q1, Point q2) {

            double slopeToQ1 = slopeTo(q1);
            double slopeToQ2 = slopeTo(q2);

            if (slopeToQ1 > slopeToQ2) return +1;
            if (slopeToQ1 < slopeToQ2) return -1;
            return 0;
        }
    }

    /**
     * Returns a string representation of this point.
     * This method is provide for debugging;
     * your program should not rely on the format of the string representation.
     *
     * @return a string representation of this point
     */
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    /**
     * Unit tests the Point data type.
     */
    public static void main(String[] args) {

        StdOut.println("Create two points: ");
        Point p = new Point(378, 396);
        Point q = new Point(378, 396);

        StdOut.println(p.slopeTo(q));

        StdOut.println("Create three points: ");
        Point[] points = {new Point(1, 1), new Point(1, 4), new Point(4, 4)};

        for (int i = 0; i < points.length; i++) {
            StdOut.println("Point " + (i + 1) + ": " + points[i].toString());
        }

        StdOut.println("\nCompare slopes: ");
        for (int i = 0; i < points.length; i++) {
            for (int j = 1; i < points.length; j++) {
                try {
                    StdOut.println(points[i].toString() + " / " + points[i + j].toString() + " = " + points[i].slopeTo(points[i + j]));
                }
                catch (ArrayIndexOutOfBoundsException e) {
                    break;
                }
            }
        }

        StdOut.println("\nComparing points 2 & 3 by slope to point 1:");
        if (points[0].slopeOrder().compare(points[1], points[2]) == 0) {
            StdOut.println("2 is greater");
        }

    }
}