package Project.GameGrid;

import java.awt.Graphics;
import java.util.HashSet;
import java.util.Iterator;

import SimpleEngine.interfaces.OBJ;

/*
 * This object will be responsible for maintaining, moving, and drawing all of the cars.
 */

public class Car_controller implements OBJ
{
	
	private HashSet<Car> cars;
	private Grid world_grid;
		
	
	// -- Constructor.
	public Car_controller(Grid grid)
	{
		cars = new HashSet<Car>();
		world_grid = grid;
	}
	
	// Creation and Deletion of Cars.
	public void addCar(Car car)
	{
		cars.add(car);
	}
	
	public void deleteCar(Car car)
	{
		cars.remove(car);
	}
	
	// Draw all of the cars.
	@Override
	public void draw(Graphics g)
	{
		for(Car c : cars)
		{
			c.draw(g);
		}
	}

	// Move all of the cars along train tracks.
	@Override
	public void update()
	{
		// Update the positions of all of the trains.
		Iterator<Car> iter = cars.iterator();
		while(iter.hasNext())
		{
			Car c = iter.next();
			
			c.update();
			if(c.alive == false)
			{
				iter.remove();
			}
		}
	}

}
