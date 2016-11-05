import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdDraw;

public class PointSET {
    
    private SET<Point2D> points = new SET<Point2D>();
    
    public PointSET() {
        //
    } // construct an empty set of points 
    
    public boolean isEmpty() {
        return points.isEmpty();
    } // is the set empty? 
    
    public int size()  {
        return points.size(); 
    } // number of points in the set 
    
    public void insert(Point2D p)  {
        if (p == null) {
            throw new NullPointerException();
        }
        
        if (!points.contains(p)) {
            points.add(p);
        }
    } // add the point to the set (if it is not already in the set)
    
    public boolean contains(Point2D p)  {
        return points.contains(p);  
    } // does the set contain point p? 
    
    public void draw()  {
        for (Point2D p : points) {
            StdDraw.point(p.x(), p.y());
        }
    } // draw all points to standard draw 
    
    public Iterable<Point2D> range(RectHV rect)  {
        if (rect == null) {
            throw new NullPointerException();
        }
       
        SET<Point2D> intersecting = new SET<Point2D>();
        
        for (Point2D p: points) {
            if (rect.contains(p)) {
                intersecting.add(p);
            }
        }
        
        return intersecting;
    } // all points that are inside the rectangle 
    
    public Point2D nearest(Point2D p)  {
        if (p == null) {
            throw new NullPointerException();
        }
        
        if (isEmpty()) {
            return null;
        }
        
        Point2D nearestPoint = null;
        double nearestDistance = Double.POSITIVE_INFINITY;
        
        for (Point2D otherPoint: points) {
            double distance = p.distanceSquaredTo(otherPoint);
            
            if (distance < nearestDistance) {
                nearestPoint = otherPoint;
                nearestDistance = distance;
            }
        }
        
        return nearestPoint;
    } // a nearest neighbor in the set to point p; null if the set is empty 
    
    public static void main(String[] args)  {
        // 
    } // unit testing of the methods (optional) 
}