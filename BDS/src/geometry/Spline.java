package geometry;

import Data_Structures.Structures.UBA;

public class Spline
{
  
  UBA<Point> _point_discretization;
  
  // A Spline of curves.
  UBA<Hermite_Curve> _curves;
  
  public Spline()
  {
      _point_discretization = new UBA<Point>();
      _curves = new UBA<Hermite_Curve>();
  }
  
  public void addPoint(Point p)
  {
      _point_discretization.add(p);
      
      if(_point_discretization.size() == 2)
      {
         Point p0 = _point_discretization.get(0);
         Point p1 = _point_discretization.get(1);
         Point tangent = p1.sub(p0);
        
         Hermite_Curve curve = new Hermite_Curve(p0, tangent, p1, tangent, false);
         _curves.add(curve);
      }
      else if(_point_discretization.size() == 3)
      {
         // Add Two new curves.
         _curves.pop();
         
         // Add first curve.
         Point p0 = _point_discretization.get(0);
         Point p1 = _point_discretization.get(1);
         Point p2 = _point_discretization.get(2);
         
         Point t0 = p1.sub(p0);
         Point t1 = p2.sub(p0).divScalar(2);
         Point t2 = p2.sub(p1);
         
         Hermite_Curve curve1 = new Hermite_Curve(p0, t0, p1, t1, false);
         _curves.add(curve1);
       
         Hermite_Curve curve2 = new Hermite_Curve(p1, t1, p2, t2, false);
         _curves.add(curve2);
         
      }
      else if(_point_discretization.size() > 3)
      {
        // Remove last curve, add an intermediate, and an ending curve.
        _curves.pop();
        
        int index0 = _point_discretization.size() - 4;
        Point p0 = _point_discretization.get(index0 + 0);
        Point p1 = _point_discretization.get(index0 + 1);// previous
        Point p2 = _point_discretization.get(index0 + 2);//   ending spline.
        Point p3 = _point_discretization.get(index0 + 3); // New Point.
        
        Point t1 = p2.sub(p0).divScalar(2);
        Point t2 = p3.sub(p1).divScalar(2);
        Point t3 = p3.sub(p2);
        
        Hermite_Curve curve1 = new Hermite_Curve(p1, t1, p2, t2, false);
         _curves.add(curve1);
       
         Hermite_Curve curve2 = new Hermite_Curve(p2, t2, p3, t3, false);
         _curves.add(curve2);
      }
  }
  
  public int numPoints()
  {
     return _point_discretization.size();
  }
  
  public Point getPointAtIndex(int i)
  {
    return _point_discretization.get(i);
  }
  
  public Point removeLastPoint()
  {
     return _point_discretization.pop();
  }
  
  // Given a time between 0 and 1, returns the position on this spline.
  public Point getPoint(double time)
  {
    int numCurves = _curves.size();
    
    time *= numCurves;
    
    // Get doubleing point part.
    int curveIndex = (int)time;
    double curveTime = time - curveIndex;
    
    if(curveIndex >= _curves.size())
    {
      curveIndex = _curves.size() - 1;
      curveTime = 1;
    }
    
    if(_curves.size() <= 0)
    {
    	throw new Error("Curves object is empty!");
    }
    
    Hermite_Curve curve = _curves.get(curveIndex);
    return curve.position(curveTime);
  }

  // Given a time between 0 and 1, returns the tangent vector on this spline.
  public Point getTangent(double time)
  {
    int numCurves = _curves.size();
    
    time *= numCurves;
    
    // Get doubleing point part.
    int curveIndex = (int)time;
    double curveTime = time - curveIndex;
    
    if(curveIndex >= _curves.size())
    {
      curveIndex = _curves.size() - 1;
      curveTime = 1;
    }
    
    Hermite_Curve curve = _curves.get(curveIndex);
    return curve.tangent(curveTime);
  }
}