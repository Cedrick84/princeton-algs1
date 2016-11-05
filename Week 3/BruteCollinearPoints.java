/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Cedrick
 */
import java.util.Arrays;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;

public class BruteCollinearPoints {

    private LineSegment[] segments;

    public BruteCollinearPoints(Point[] points) {
        if (points == null || Arrays.asList(points).contains(null)) {
            throw new NullPointerException();
        }

        Point[] copiedPoints = Arrays.copyOf(points, points.length);
        Arrays.sort(copiedPoints);

        for (int i = 0; i < copiedPoints.length - 1; i++) {
            Point p1 = copiedPoints[i];
            Point p2 = copiedPoints[i + 1];

            if (p1.compareTo(p2) == 0) {
                throw new IllegalArgumentException();
            }
        }

        processPoints(copiedPoints);
    } // finds all line segments containing 4 points

    private void processPoints(Point[] points) {
     
        LineSegment[] hits = new LineSegment[points.length];
        int hitCount = 0;
        
        for (int i = 0; i < points.length - 3; i++) {
            Point p = points[i];
            
            for (int j = i + 1; j < points.length - 2; j++) {
                Point q = points[j];
                double slopePQ = p.slopeTo(q);
           
                for (int k = j + 1; k < points.length - 1; k++) {
                    Point r = points[k];
                    double slopeQR = q.slopeTo(r);
                    
                    if (slopePQ != slopeQR) {
                        continue;
                    }
                    
                    int hitIndex = 0;
                    
                    for (int l = k + 1; l < points.length; l++) {
                        Point s = points[l]; 
                        double slopeRS = r.slopeTo(s);
                        
                        if (slopeQR == slopeRS) {
                            hitIndex = l;
                        }
                    }
                    
                    if (hitIndex != 0) {
                        LineSegment segment = new LineSegment(p, points[hitIndex]);
                        hits[hitCount] = segment;
                        hitCount++;
                    }
                }   
            }
   
        }
        
        segments = new LineSegment[hitCount];
            for (int m = 0; m < hitCount; m++) {
                segments[m] = hits[m];
            }
    }

    public int numberOfSegments() {
        return segments.length;
    } // the number of line segments

    public LineSegment[] segments() {
        return Arrays.copyOf(segments, segments.length);
    } // the line segments

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
        for (Point p : points) {
            p.draw();
        }
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