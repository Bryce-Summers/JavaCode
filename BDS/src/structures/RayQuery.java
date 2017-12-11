package structures;

import Data_Structures.Structures.UBA;
import geometry.Ray;

/*
#
# BDS.RayQuery Structure
# Written on July.23.2017
#
# Purpose: Stores the results of different types of queries.
# Any technical information about queries in the BDS library can be relocated to this file.
#
# - .ray # the ray performing the query.
#          Typically this will be used to feed the ray into BVH and other query structures.
# - .isects, {obj:, time:} # the results of a query.
#
# Standard names and behaviors for ray query calls specified in BDS.RayQueryable.
*/


public class RayQuery
{
	public Ray ray;
	public double min_time;
	public Object obj = null;
	public UBA<Object> objs;
	public double time;
	public UBA<Double> times;
	
    public RayQuery(Ray ray)
    {
        this.initialize(ray);
    }

    void initialize(Ray ray)
    {
        this.ray = ray;

        this.min_time = Double.MAX_VALUE;

        obj   = null;         // Any object.
        objs  = new UBA<Object>(); // []
        time  = -1;                // positive float.
        times = new UBA<Double>(); // positive float[].
    }    

    void reset()
    {
        initialize(ray);
    }

    // Given an intersection time, this.min_time is updated.
    void updateMinTime(double new_time)
    {
        if(new_time < min_time)
        {
            min_time = new_time;
        }
    }

    boolean hasIntersection()
    {
        return this.times.size() > 0 || this.min_time < Double.MAX_VALUE;
    }

    /*
    TODO: Add isect adding and updating functions.
     */

    /*
    # Pops the latest isect_obj to be found.
    popNewest: () ->
        return this.isects.pop()

    # Pops the oldest isect_obj that was found.
    popOldest: () ->
        output = this.isects[0]
        this.isects.slice(1)
        return output
    */

    // Sorts all of the isects and associated times from near to far
    // by least to greatest time value.
    // This allows users to post sort values after they have received them.
    void sortByTime()
    {
        throw new Error("Rayquery: Implement Me Please!");
    }
}