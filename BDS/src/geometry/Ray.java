package geometry;

/*
Ray Class.

Written by Bryce Summers on 1 - 6 - 2017.

Purpose:
*/

public class Ray
{

	Point p1, p2;
	Point dir;
	double _time_scale;
	
    // Originating location, the direction the ray is going.
    // And an option time scale factor that may be used in various applications that want to compute intersections over a bounded area
    // or care about the distance a ray has travelled.
    public Ray(Point point, Point dir)
    {
    	this.dir = dir;
        
        // By default, the user probably cares most about the magnitude of the orignal ray.
        this._time_scale = dir.norm();
        
        // If we do this then to save time, we compute the normalized direction without recomputing the euclidean norm.
        dir = this.dir.divScalar(this._time_scale);            

        // Useful data for splitting lines by rays.
        p1 = point;
        p2 = p1.add(dir);
    }
    
    public Ray(Point point, Point dir, double _time_scale)
    {
    	this.dir = dir;
    	this._time_scale = _time_scale;
    	dir = dir.normalize();
    	
        // Useful data for splitting lines by rays.
        this.p1 = point;
        this.p2 = p1.add(dir);
    }


    public Point getPoint()
    {
        return p1.clone();
    }

    // Gurantted to return a normalized direction vector.
    public Point getDirection()
    {
        return dir.clone();
    }

    // Returns the right Perpendicular.
    // Normalized.
    public Point getRightPerpendicularDirection()
    {
        return new Point(-dir.y, dir.x);
    }

    // Normalized.
    public Point getLeftPerpendicularDirection()
    {
        return new Point(dir.y, -dir.x);
    }

    public double getTimeScale()
    {
        return this._time_scale;
    }

    public Point getPointAtTime(double t)
    {
        return p1.add(dir.multScalar(t*_time_scale));
    }

    // BDS.Line -> bool
    public boolean detect_intersection_with_line(Line line)
    {

        double side1 = line_side_test(line.p1);
        double side2 = line_side_test(line.p2);

        Point normal = line.getNormal(p1);

        // Ray shoots through the line, rather than away form it.
        // the line normal and ray direction should be going in opposition.
        boolean correct_direction = normal.dot(dir) < 0;

        // intersection if the ray goes towards the line segment and is on the proper side.
        return side1*side2 <= 0; // and correct_direction;
    }


    // Returns >0 if c is to the right.
    // Returns =0 if c is on the ray.
    // Returns <0 if c is on the left.
    public double line_side_test(Point c)
    {
        return (p2.x - p1.x)*(c.y - p1.y) - (p2.y - p1.y)*(c.x - p1.x);
    }

    public double getAngle()
    {
        return Math.atan2(p2.y - p1.y, p2.x - p1.x);
    }

    // Returns the pt of intersection or null if none exists.
    public Point intersect_ray(Ray other)
    {

        // Find the intersection point.

        /*
        u = ((bs.y - as.y) * bd.x - (bs.x - as.x) * bd.y) / (bd.x * ad.y - bd.y * ad.x)
        v = ((bs.y - as.y) * ad.x - (bs.x - as.x) * ad.y) / (bd.x * ad.y - bd.y * ad.x)
        Factoring out the common terms, this comes to:

        dx = bs.x - as.x
        dy = bs.y - as.y
        det = bd.x * ad.y - bd.y * ad.x
        u = (dy * bd.x - dx * bd.y) / det
        v = (dy * ad.x - dx * ad.y) / det
        */

        // Extract the relevant points.
        // s = source, d is direction.
        Point as = p1;
        Point bs = other.p1;
        Point ad = dir;
        Point bd = other.dir;

        
        double dx = bs.x - as.x;
        double dy = bs.y - as.y;
        double det = bd.x * ad.y - bd.y * ad.x;
        double u = (dy * bd.x - dx * bd.y) / det;
        double v = (dy * ad.x - dx * ad.y) / det;

        // Elliminate all collisions that are in the negative portion of one of the rays.
        /*
        if u < 0 or v < 0
            return null
        */

        //, then the two lines are collinear.
        if(det == 0)
        {
            return null;
        }

        Point intersection_point = as.add(ad.multScalar(u));

        return intersection_point;
    }

    // Takes the given point an returns [perpendicular distance of pt from ray, signed distance of the projection along the ray in [0, 1] space.]
    public double[] getPerpAndParLengths(Point pt)
    {
        Point displacement = pt.sub(p1);
        // Note: @dir is assumed to be normalized.
        double parrallel_component = displacement.dot(dir);

        double perp_component = displacement.dot(getRightPerpendicularDirection());

        double[] output = new double[2];
        output[0] = Math.abs(perp_component);
        output[1] = parrallel_component / _time_scale; 
        return output;
    }

    public Point projectPoint(Point pt)
    {
        Point displacement = pt.sub(p1);
        double parrallel_component = displacement.dot(dir);

        return p1.add(dir.multScalar(parrallel_component));
    }
}