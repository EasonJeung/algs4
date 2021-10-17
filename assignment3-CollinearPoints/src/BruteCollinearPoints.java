/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 *
 * TODO:
 * Work not properly if the input has 5 or more collinear points
 **************************************************************************** */


import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {

    private final ArrayList<LineSegment> lineSegments = new ArrayList<>();

    public BruteCollinearPoints(Point[] points) {
        validate(points);
        final int numOfPoints = points.length;
        if (numOfPoints < 4) return;

        for (int i = 0; i < numOfPoints - 3; i++) {
            for (int j = i + 1; j < numOfPoints - 2; j++) {
                for (int k = j + 1; k < numOfPoints - 1; k++) {
                    for (int m = k + 1; m < numOfPoints; m++) {
                        if (points[i].slopeTo(points[j]) == points[i].slopeTo(points[k])
                                && points[i].slopeTo(points[j]) == points[i].slopeTo(points[m])) {
                            Point[] segment = { points[i], points[j], points[k], points[m] };
                            Arrays.sort(segment);
                            lineSegments.add(new LineSegment(segment[0], segment[3]));
                        }
                    }
                }

            }
        }
    }

    public int numberOfSegments() {
        return lineSegments.size();
    }

    public LineSegment[] segments() {
        return lineSegments.toArray(new LineSegment[numberOfSegments()]);
    }

    private static void validate(Point[] points) {
        if (points == null) throw new IllegalArgumentException(
                "argument to BruteCollinearPoints constructor is null");

        for (Point p : points) {
            if (p == null) throw new IllegalArgumentException("one item of points is null");
        }

        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].equals(points[j])) {
                    throw new IllegalArgumentException("duplicate points: " + points[i].toString());
                }
            }
        }

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
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        StdDraw.setPenRadius(0.025);
        StdDraw.setPenColor(Color.RED);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.setPenRadius();
        StdDraw.setPenColor();
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
