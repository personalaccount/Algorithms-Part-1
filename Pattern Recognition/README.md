# Programming Assignment 3: Pattern Recognition

> Please navigate to the official [assignment 3 URL](http://coursera.cs.princeton.edu/algs4/assignments/collinear.html) for details.

Write a program to recognize line patterns in a given set of points.

**The problem.** Given a set of n distinct points in the plane, find every (maximal) line segment that connects a subset of 4 or more of the points.

**Point data type.** Immutable data type that represents a point in the plane.

Create an immutable data type Point that represents a point in the plane by implementing the following API:

    public class Point implements Comparable<Point> {
       public Point(int x, int y)        // constructs the point (x, y)

       public void draw()                // draws this point
       public void drawTo(Point that)    // draws the line segment from this point to that point
       public String toString()          // string representation

       public int compareTo(Point that)      // compare two points by y-coordinates, breaking ties by x-coordinates
       public double slopeTo(Point that)      // the slope between this point and that point
       public Comparator<Point> slopeOrder()  // compare two points by slopes they make with this point
    }


- The compareTo() method should compare points by their y-coordinates, breaking ties by their x-coordinates. Formally, the invoking point (x0, y0) is less than the argument point (x1, y1) if and only if either y0 < y1 or if y0 = y1 and x0 < x1.

- The slopeTo() method should return the slope between the invoking point (x0, y0) and the argument point (x1, y1), which is given by the formula (y1 − y0) / (x1 − x0). Treat the slope of a horizontal line segment as positive zero; treat the slope of a vertical line segment as positive infinity; treat the slope of a degenerate line segment (between a point and itself) as negative infinity.

- The slopeOrder() method should return a comparator that compares its two argument points by the slopes they make with the invoking point (x0, y0). Formally, the point (x1, y1) is less than the point (x2, y2) if and only if the slope (y1 − y0) / (x1 − x0) is less than the slope (y2 − y0) / (x2 − x0). Treat horizontal, vertical, and degenerate line segments as in the slopeTo() method.

- Do not override the equals() or hashCode() methods.

*Corner cases*. To avoid potential complications with integer overflow or floating-point precision, you may assume that the constructor arguments x and y are each between 0 and 32,767.

*Line segment data type.* To represent line segments in the plane, use the data type LineSegment.java, which has the following API:

    public class LineSegment {
       public LineSegment(Point p, Point q)        // constructs the line segment between points p and q
       public void draw()                        // draws this line segment
       public String toString()                    // string representation
    }

**Brute force.** Write a program BruteCollinearPoints.java that examines 4 points at a time and checks whether they all lie on the same line segment, returning all such line segments. To check whether the 4 points p, q, r, and s are collinear, check whether the three slopes between p and q, between p and r, and between p and s are all equal.

    public class BruteCollinearPoints {
       public BruteCollinearPoints(Point[] points)    // finds all line segments containing 4 points
       public int numberOfSegments()        // the number of line segments
       public LineSegment[] segments()                // the line segments
    }

The method segments() should include each line segment containing 4 points exactly once. If 4 points appear on a line segment in the order p→q→r→s, then you should include either the line segment p→s or s→p (but not both) and you should not include subsegments such as p→r or q→r. For simplicity, we will not supply any input to BruteCollinearPoints that has 5 or more collinear points.

*Corner cases*. Throw a java.lang.IllegalArgumentException if the argument to the constructor is null, if any point in the array is null, or if the argument to the constructor contains a repeated point.

*Performance requirement*. The order of growth of the running time of your program should be n4 in the worst case and it should use space proportional to n plus the number of line segments returned.

*A faster, sorting-based solution.* Remarkably, it is possible to solve the problem much faster than the brute-force solution described above. Given a point p, the following method determines whether p participates in a set of 4 or more collinear points.

- Think of p as the origin.
- For each other point q, determine the slope it makes with p.
- Sort the points according to the slopes they makes with p.
- Check if any 3 (or more) adjacent points in the sorted order have equal slopes with respect to p. If so, these points, together with p, are collinear.

Applying this method for each of the n points in turn yields an efficient algorithm to the problem. The algorithm solves the problem because points that have equal slopes with respect to p are collinear, and sorting brings such points together. The algorithm is fast because the bottleneck operation is sorting.


 (#):fa-star
This assignment was developed by Kevin Wayne.
Copyright © 2005.