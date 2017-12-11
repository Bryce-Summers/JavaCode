package structures;

import geometry.Ray;

/*

Ray Queryable Abstract parent class
for geometric objects that support ray casting.

Written by Bryce Summers on July. 25th. 2017


This class handles a lot of the different return logics.

Child classes at minnimum need to implement the following functions:
    @_isect_ray(ray) --> returns the time of intersection. < 0 if not found.
    @_exit_ray(ray, enter_time) --> returns {needed:, ray:, time:,}
     - needed is true if the geometry has a volume and finding an exit location makes sense.
       needed is false if this geometry is just a flat primitive, like a triangle, quad, plane, etc.
     - ray contains the exit ray if needed was true.
     - time contains the time along the input ray that the exit ray starts at,
       the exit ray then proceeds in the opposite direction of the input ray.


###
# Here is the public interface.
#  - bool rayQueryMin(rayQuery) :  updates .min_time, .obj, and .time, if a closer intersection is found,
#                                 returns true iff an update has been made.
#  - bool rayQueryTime(rayQuery) : stores the time of earliest intersection in .time, returns true iff an intersection was found.
#                                   this stores the time even if it is greater than min_time.
#
#  - bool rayQueryTimes(rayQuery): .time = float[], appends these times to the times array.
#  - bool rayQueryAll(rayQuery): pushes .objs and .times for all intersections between the ray and the object, returns true if any were found,
#                                appends new objs and times onto their cooresponding arrays.
#                                if the object is filled, then the entance and exit times are pushed.
#                                1 obj if a box or mesh, 2 if sub triangles within a mesh.
#
# Future: skip objects of a certain orientation.
#
*/

public abstract class RayQueryable
{

	public RayQueryable()
	{
	}
	
    /*
    Ray Queries:
     Here are some top of the brain approaches:
     - Raycast all 6 quad faces.
     - Raycast 12 triangle faces.
     - Since we are axis aligned, we could the ray reletive to center of box space, 
       then prune faces that the ray won't hit. This seems to be the major win for Axis-Alignment.

     I looked online and found a better quadrant based approach and modified it to find the exit point for the box as well.
     */
	 
	/*
	@_isect_ray(ray) --> returns the time of intersection. < 0 if not found.
    @_exit_ray(ray, enter_time) --> returns {needed:, ray:, time:,}
     - needed is true if the geometry has a volume and finding an exit location makes sense.
       needed is false if this geometry is just a flat primitive, like a triangle, quad, plane, etc.
     - ray contains the exit ray if needed was true.
     - time contains the time along the input ray that the exit ray starts at,
       the exit ray then proceeds in the opposite direction of the input ray.
     */
	protected abstract double _isect_ray(Ray ray);
	
	protected class Exit_Ray_Event
	{
		public boolean needed;
		public Ray ray;
		public double time;
		
		public Exit_Ray_Event()
		{
			
		}
	}
	
	protected abstract Exit_Ray_Event _exit_ray(Ray ray, double enter_time);
	
	
	// Updates the rayQuery with an intersection with this triangle if found.
    public boolean rayQueryMin(RayQuery rayQuery)
    {

        double new_time = _isect_ray(rayQuery.ray);      
        if(0 <= new_time && new_time < rayQuery.min_time)
        {
            rayQuery.time     = new_time;
            rayQuery.min_time = new_time;
            rayQuery.obj      = this;
            return true;
        }
        return false;
    }

    // Adds to the given rayQuery all intersections and times that are found.
    public boolean rayQueryAll(RayQuery rayQuery)
    {
        // Reduction to times query.
        if(rayQueryTimes(rayQuery))
        {
            rayQuery.objs.push(this);
            return true;
        }

        return false;
    }


    // Updates rayQuery.time value if true,
    // true if time found.
    public boolean rayQueryTime(RayQuery rayQuery)
    {
        double new_time = _isect_ray(rayQuery.ray);
        if(0 <= new_time)
        {
            rayQuery.time = new_time;
            return true;
        }
        return false;
    }

    // Adds intersection times to rayQuery.times[] value if true,
    // true if at least 1 time is found.
    public boolean rayQueryTimes(RayQuery rayQuery)
    {

        double enter_time = this._isect_ray(rayQuery.ray);

        // No intersections.
        if(enter_time < 0)
        {
            return false;
        }

        rayQuery.times.push(enter_time);

        // NOTE: if enter_time == 0,
        // then the ray starts in the box and we treat the start as the enter time.

        // Find the exit time.
        Exit_Ray_Event exit_search = this._exit_ray(rayQuery.ray, enter_time);

        // Return just the enter time if exit time unneeded.
        if(exit_search.needed)
        {
        	return true;
        }

        // If the exit ray is needed, we perform the backwards query.
        Ray exit_ray  = exit_search.ray;
        double exit_time = exit_search.time - this._isect_ray(exit_ray);

        // Two times
        
        rayQuery.times.push(exit_time);
        return true;
    }
}