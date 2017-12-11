package geometry;

import Data_Structures.Structures.UBA;

//
//Bryce Summer's Spline Class.
//
//Written on 11/29/2016. Transcribed to processing on 10/31/2017
//
//Purpose: Extends the THREE.js spline classes with better features.
/*
//
//Planned:
//1. Offset Curves.
//2. Inset Curves.
//3. Maximum-length interval discretizations for producing renderable line segments.
//
//Currently we are implementing this as a reduction to THREE.CatmullRomCurve3, but we may remove the dependancy if we have time and go sufficiently beyond it.
//FIXME: Standardize the curve class and instantiate it from interfacial curves.
*/
public class Curve
{
 public Spline _spline;
 public UBA<Point> _point_discretization; 

 public Curve(Spline spline)
 {
     // A spline that dynamically parameterizes itself to between 0 and 1.
     this._spline = spline;

     // A list of points in the discretization.
     _point_discretization = new UBA<Point>();
 }

 // p : THREE.Vector3.
 public void addPoint(Point p)
 {
     _spline.addPoint(p);
 }

 public int numPoints()
 {
     return _spline.numPoints();
 }

 public Point getPointAtIndex(int i)
 {
     return _spline.getPointAtIndex(i);
 }

 public Point getLastPoint()
 {
     return getPointAtIndex(numPoints() - 1);
 }

 public Point removeLastPoint()
 {
     return _spline.removeLastPoint();
 }

 public Point position(double t)
 {
     return _spline.getPoint(t);
 }

 public Point tangent(double t)
 {
     return _spline.getTangent(t);
 }

 public Point offset(double t, double amount)
 {
     Point tan = tangent(t);
     tan.setMag(amount);
     
     // Perpendicularlize the vector.
     double x = tan.x;
     double y = tan.y;
     tan.x =  y;
     tan.y = -x;
     
     return position(t).add(tan);
 }


 /*
 # Returns a list of points representing this spline.
 # They will be no more than max_length apart.
 # They will be as sparse as is practical. # FIXME: Do some studying of this.
 # See: https://github.com/Bryce-Summers/Bryce-Summers.github.io/blob/master/p5/Physics/Visuals/visual_conservation_of_energy.js
 # This is more efficient than the built in THREE.js version, because it does the binary searches for all of the points at the same time.
 # It may produce up to 2 times as many points though...
 # FIXME: Do an analysis of differnt spline discretization techniques.
 # I believe I will compensate for this algorithms problems, by designing my user interactions such that when they click near the original spline, that is a signal to go back.
 */
 public UBA<Point> getDiscretization()
 {
     return _point_discretization;
 }

 public void updateDiscretization(double max_length)
 {
     UBA<Point> output = new UBA<Point>();
     Point p0 = _spline.getPoint(0);
     output.add(p0);

     UBA<Double> S = new UBA<Double>(); //# Stack.
     S.push(1.0);
     
     double low   = 0;
     Point p_low = _spline.getPoint(low);

     // The stack stores the right next upper interval.
     // The lower interval starts at 0 and is set to the upper interval
     // every time an interval is less than the max_length, subdivision is terminated.

     // Left to right subdivision loop. Performs a binary search across all intervals.
     while(S.size() != 0)
     {
         double high   = S.pop();
         Point p_high = _spline.getPoint(high);
     
         // Subdivision is sufficient, move on to the next point.
         while(Point.dist(p_low, p_high) > max_length)
         {
             // Otherwise subdivide the interval and keep going.
             S.add(high);
             high   = (low + high)/2.0;
             p_high = _spline.getPoint(high);
         }
     
         output.add(p_high);
         low   = high;
         p_low = p_high;
         continue;
     }

     _point_discretization = output;
 }
 
 // max_length:float, maximum length out output segment.
 // amount: the distance the offset curve is away from the main curve. positive or negative is fine.
 // time_output (optional) will be populated with the times for the output points.
 public UBA<Point> getOffsets(double max_length, double amount, UBA<Double> times_output)
 {
     Point o0 = offset(0, amount);
     UBA<Point> output = new UBA<Point>();
     output.add(o0);
     if (times_output != null)
     {
       times_output.push(0.0);
     }

     UBA<Double> S = new UBA<Double>(); // Stack.
     S.push(1.0);
     double low = 0;
     Point p_low = offset(low, amount);

     // The stack stores the right next upper interval.
     // The lower interval starts at 0 and is set to the upper interval.
     // every time an interval is terminated after subdivision is sufficient.

     // Left to right subdivision loop.
     while(S.size() != 0)
     {        
         double high = S.pop();
         Point p_high = offset(high, amount);

         // Subdivision is sufficient, move on to the next point.
         while(Point.dist(p_low, p_high) > max_length)
         {            
             // Otherwise subdivide the interval and keep going.
             S.push(high);
             high = (low + high)/2.0;
             p_high = offset(high, amount);
         }

         output.add(p_high);
         if(times_output != null)
         {
           times_output.push(high);
         }
         low = high;
         p_low = p_high;
         continue;
     }
     
     return output;
 }
}