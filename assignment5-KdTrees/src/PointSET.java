/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.TreeSet;

public class PointSET {

    private final TreeSet<Point2D> pointSet;

    // construct an empty set of points
    public PointSET() {
        pointSet = new TreeSet<>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return pointSet.isEmpty();
    }

    // number of points in the set
    public int size() {
        return pointSet.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        validate(p);
        pointSet.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        validate(p);
        return pointSet.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p : pointSet) {
            p.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        validate(rect);
        TreeSet<Point2D> pointInRectSet = new TreeSet<>();
        for (Point2D p : pointSet) {
            if (rect.contains(p)) pointInRectSet.add(p);
        }
        return pointInRectSet;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        validate(p);
        double minDistance = Double.POSITIVE_INFINITY;
        Point2D nearestPoint = null;
        for (Point2D q : pointSet) {
            double qpDistance = q.distanceSquaredTo(p);
            if (qpDistance < minDistance) {
                nearestPoint = q;
                minDistance = qpDistance;
            }
        }
        return nearestPoint;
    }

    // validate argument is null
    private void validate(Object obj) {
        if (obj == null) throw new IllegalArgumentException("argument is null");
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        PointSET pointSET = new PointSET();
        System.out.println(pointSET.nearest(new Point2D(0, 0)) == null);
    }
}
