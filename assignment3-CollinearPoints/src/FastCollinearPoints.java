/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {

    private final ArrayList<LineSegment> lineSegments = new ArrayList<>();

    public FastCollinearPoints(Point[] points) {
        validate(points);
        final int numOfPoints = points.length;
        if (numOfPoints < 4) return;

        Arrays.sort(points);

        for (int i = 0; i < numOfPoints; i++) {
            final Point anchor = points[i];
            Point[] auxPoints = points.clone();
            Arrays.sort(auxPoints, points[i].slopeOrder());
            // After sorting, auxPoints[0](NEG_INFINITY) is anchor itself.
            double slopeSlow = anchor.slopeTo(auxPoints[1]);
            double slopeFast;
            // the for loop is shitty!
            // easy to understand, however, handling of boundary situations is ugly!
            for (int idxSlow = 1, idxFast; idxSlow < numOfPoints - 2;
                 idxSlow = idxFast, slopeSlow = slopeFast) {
                idxFast = idxSlow + 1;
                do {
                    slopeFast = anchor.slopeTo(auxPoints[idxFast++]);
                } while (slopeSlow == slopeFast && idxFast < numOfPoints);
                /**
                 * when last point of auxPoints is a point of line segment, do while loop will end
                 * bcz "idxFast = numOfPoints", idxFast doesn't self-increase, so should not minus 1.
                 */
                // Only if the loop is terminated by "slopeSlow != slopeFast", idxFast self-decreases 1.
                if (slopeSlow != slopeFast) idxFast--;
                int numOfAdjacentPoint = idxFast - idxSlow;
                if (numOfAdjacentPoint >= 3) {
                    Point[] collinear = new Point[numOfAdjacentPoint + 1];
                    collinear[0] = anchor;
                    System.arraycopy(auxPoints, idxSlow, collinear, 1, numOfAdjacentPoint);
                    Arrays.sort(collinear);
                    // After sorting, if anchor is not the "lowest", the collinear combination has
                    // occurred, drop the collinear combination.
                    if (anchor == collinear[0]) {
                        lineSegments.add(new LineSegment(anchor, collinear[numOfAdjacentPoint]));
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
            StdDraw.show();
        }
    }
}
