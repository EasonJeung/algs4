/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {

    // for lineDirection of Node
    private static final int VERTICAL = 1;
    private static final int HORIZONTAL = 0;

    private Node root;
    private int size = 0;                   // number of nodes in KdTree

    private static class Node {

        private final Point2D p;            // the point
        private final RectHV rect;          // axis-aligned rectangle corresponding to the node
        private final int lineDirection;    // direction of line dividing rect into two
        private Node lb, rt;                // the left/bottom, right/top subtree


        Node(Point2D p, RectHV rect, int lineDirection) {
            this.p = p;
            this.rect = rect;
            this.lineDirection = lineDirection;
        }


        /**
         * @param that - point to be compared
         * @return 1 if this on right/top of that, -1 if this on left/bottom of that
         */
        int compareTo(Point2D that) {
            validate(that);
            if (this.p.equals(that)) return 0;
            if (lineDirection == VERTICAL) {
                // 若这个点所在线竖直，比较的是其他点(that)在它(this)的左侧或右侧
                return this.p.x() > that.x() ? 1 : -1;
            }
            else {
                // 非竖直(水平)，比较的是其他点(that)在它(this)的上方/下方
                return this.p.y() > that.y() ? 1 : -1;
            }
        }

    }


    // construct an empty set of points
    public KdTree() {
    }

    // is the set empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        validate(p);
        root = insert(p, root, null, 0);
    }

    private Node insert(Point2D p, Node cur, Node parent, int location) {
        if (cur == null) {
            // construct a new tree or meet null node
            if (size == 0) {
                // construct a new tree
                size++;
                return new Node(p, new RectHV(0, 0, 1, 1), VERTICAL);
            }
            else {
                // create new node of tree
                size++;
                return new Node(p, correspondRect(parent, location),
                                parent.lineDirection == VERTICAL ? HORIZONTAL : VERTICAL);
            }
        }
        else {
            // travel(search) in tree
            int cmp = cur.compareTo(p);
            if (cmp > 0) {
                // cur point on right/top of p, so p in left/bottom subtree
                cur.lb = insert(p, cur.lb, cur, cmp);
            }
            else if (cmp < 0) {
                // cur point on left/bottom of p, so p in right/top subtree
                cur.rt = insert(p, cur.rt, cur, cmp);
            }
            return cur;
        }
    }

    /**
     * create rect corresponding to current node
     * ____________________|    location > 0    |      location < 0    |
     * parent.p.VERTICAL   | parent.rect.LEFT   |   parent.rect.RIGHT  |
     * parent.p.HORIZONTAL | parent.rect.BOTTOM |   parent.rect.TOP    |
     *
     * @param parent   - parent node of current node
     * @param location - 1 if cur in left/bottom subtree of parent,
     *                 -1 if cur in right/top subtree
     * @return rect - rect corresponding to current node
     */
    private RectHV correspondRect(Node parent, int location) {

        RectHV rect;
        if (parent.lineDirection == VERTICAL) {
            // parent rect is subdivided into left, right two part
            if (location > 0) {
                // left part of parent rect
                rect = new RectHV(parent.rect.xmin(), parent.rect.ymin(), parent.p.x(),
                                  parent.rect.ymax());
            }
            else {
                // right part of parent rect
                rect = new RectHV(parent.p.x(), parent.rect.ymin(), parent.rect.xmax(),
                                  parent.rect.ymax());
            }
        }
        else {
            // parent rect is subdivided into top, bottom two part
            if (location > 0) {
                // bottom part of parent rect
                rect = new RectHV(parent.rect.xmin(), parent.rect.ymin(), parent.rect.xmax(),
                                  parent.p.y());
            }
            else {
                // top part of parent rect
                rect = new RectHV(parent.rect.xmin(), parent.p.y(), parent.rect.xmax(),
                                  parent.rect.ymax());
            }
        }
        return rect;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        validate(p);
        return contains(p, root);
    }

    private boolean contains(Point2D p, Node cur) {
        if (cur == null) return false;
        int cmp = cur.compareTo(p);
        if (cmp > 0) {
            // cur point on right/top of p, so p in left/bottom subtree if p in set
            return contains(p, cur.lb);
        }
        else if (cmp < 0) {
            // cur point on left/bottom of p, so p in right/top subtree if p in set
            return contains(p, cur.rt);
        }
        else return true;
    }

    // draw all points to standard draw
    public void draw() {
        draw(root);
    }

    private void draw(Node cur) {
        if (cur == null) return;
        draw(cur.lb);

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        cur.p.draw();

        StdDraw.setPenRadius();
        if (cur.lineDirection == VERTICAL) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(cur.p.x(), cur.rect.ymin(), cur.p.x(), cur.rect.ymax());
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(cur.rect.xmin(), cur.p.y(), cur.rect.xmax(), cur.p.y());
        }

        draw(cur.rt);
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        validate(rect);
        Stack<Point2D> pointStack = new Stack<>();
        range(rect, root, pointStack);
        return pointStack;
    }

    private void range(RectHV rect, Node cur, Stack<Point2D> pointStack) {
        if (cur == null) return;
        if (rect.contains(cur.p)) pointStack.push(cur.p);
        if (cur.lb != null && rect.intersects(cur.lb.rect)) range(rect, cur.lb, pointStack);
        if (cur.rt != null && rect.intersects(cur.rt.rect)) range(rect, cur.rt, pointStack);
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        validate(p);
        if (isEmpty()) return null;
        return nearest(p, root, root.p);
    }

    private Point2D nearest(Point2D p, Node cur, Point2D tmpNearest) {
        if (cur != null) {
            if (p.distanceSquaredTo(cur.p) < p.distanceSquaredTo(tmpNearest))
                tmpNearest = cur.p;

            int cmp = cur.compareTo(p);
            if (cmp > 0) {
                tmpNearest = nearest(p, cur.lb, tmpNearest);
                if (cur.rt != null && cur.rt.rect.distanceSquaredTo(p) < p
                        .distanceSquaredTo(tmpNearest)) {
                    tmpNearest = nearest(p, cur.rt, tmpNearest);
                }
            }
            else if (cmp < 0) {
                tmpNearest = nearest(p, cur.rt, tmpNearest);
                if (cur.lb != null && cur.lb.rect.distanceSquaredTo(p) < p
                        .distanceSquaredTo(tmpNearest)) {
                    tmpNearest = nearest(p, cur.lb, tmpNearest);
                }
            }
        }
        return tmpNearest;
    }

    // validate argument is null
    private static void validate(Object obj) {
        if (obj == null) throw new IllegalArgumentException("argument is null");
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        PointSET pointSET = new PointSET();
        System.out.println(pointSET.nearest(new Point2D(0, 0)) == null);
    }

}
