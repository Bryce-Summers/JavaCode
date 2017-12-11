package components;

import geometry.Point;

public class FillTriangle
{
  // Points.
  Point p1;
  Point p2;
  Point p3;
  
  // Tangents.
  Point t1;
  Point t2;
  Point t3;
  
  double time1;
  double time2;
  double time3;
  
  photonColor path_start;
  photonColor path_end;
  
  // Cached barycentric coordinates for a given pt.
  double bary1;
  double bary2;
  double bary3;
  
  // Standard reference line side tests for pts of triangle in
  // relation to their perpendcularity to opposite lines.
  double test_val1;
  double test_val2;
  double test_val3;
  
  double offset_val1;
  double offset_val2;
  double offset_val3;
  
  
  public FillTriangle(Point a, Point b, Point c, Point t1, Point t2, Point t3)
  {
       p1 = a;
       p2 = b;
       p3 = c;
       
       this.t1 = t1;
       this.t2 = t2;
       this.t3 = t3;
       
       // Reference values for 3 pts, used for scaling results for query points.
       test_val1 = line_side_test(p2, p3, p1);
       test_val2 = line_side_test(p3, p1, p2);
       test_val3 = line_side_test(p1, p2, p3);
  }
  
  void setTimes(double t1, double t2, double t3)
  {
    time1 = t1;
    time2 = t2;
    time3 = t3;
  }
  
  void setSignedOffsetVals(double o1, double o2, double o3)
  {
	  this.offset_val1 = o1;
	  this.offset_val2 = o2;
	  this.offset_val3 = o3;
  }
  
  void setphotonColors(photonColor c1, photonColor c2)
  {
    path_start = c1;
    path_end = c2;
  }
  
  boolean containsPt(Point pt)
  {
    computeBaryCentricCoordinates(pt);
    return 0 <= bary1 && bary1 <= 1 &&
           0 <= bary2 && bary2 <= 1 &&
           0 <= bary3 && bary3 <= 1;
  }
  
  public Point getRandomPoint()
  {
    double per1 = Math.random();
    double per2 = Math.random()*(1.0 - per1);
    double per3 = 1.0 - per1 - per2;
    
    return p1.multScalar(per1).add(p2.multScalar(per2)).add(p3.multScalar(per3));
  }

  // Uses barycentric coordinates to interpolate the tangent at the given pt.
  public Point interpolateTangentAtPt(Point pt)
  {
    computeBaryCentricCoordinates(pt);
    
    // Compute barycentric coordinates.
    return t1.multScalar(bary1).add(t2.multScalar(bary2)).add(t3.multScalar(bary3));
  }
  
  public double interpolateTimeAtPt(Point pt)
  {
    computeBaryCentricCoordinates(pt);
    
    return time1*bary1 + time2*bary2 + time3*bary3;
  }
  
  public double interpolateOffsetAtPt(Point pt)
  {
	 computeBaryCentricCoordinates(pt);
	 return offset_val1*bary1 + offset_val2*bary2 + offset_val3*bary3;
  }
  
  public photonColor interpolatePhotonColorAtPt(Point pt)
  {
    double time = interpolateTimeAtPt(pt);
    return photonColor.lerp(path_start, path_end, time);
  }
  
  
  // Sets bary1, bary2, and bary3 from pt.
  public void computeBaryCentricCoordinates(Point pt)
  {
       double v1 = line_side_test(p2, p3, pt);
       double v2 = line_side_test(p3, p1, pt);
       double v3 = line_side_test(p1, p2, pt);
       
       bary1 = v1 / test_val1;
       bary2 = v2 / test_val2;
       bary3 = v3 / test_val3;
  }
  
  private double line_side_test(Point p1, Point p2, Point c)
  {
    return (p2.x - p1.x)*(c.y - p1.y) - (p2.y - p1.y)*(c.x - p1.x); 
  }
}