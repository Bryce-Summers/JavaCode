package Project.GameGrid;

import java.awt.Graphics;
import java.util.HashSet;

import SimpleEngine.interfaces.OBJ;

/*
 * Trains are used to associate cars into sets.
 * 
 * This allows all of the cars to be moved at the same time.
 */


public class Train implements OBJ
{
	// The set of all cars in this train.
	HashSet<Car> cars;

	
	// -- Constructor.
	public Train()
	{
		cars = new HashSet<Car>();
	}
	
	
	@Override
	public void draw(Graphics g)
	{
		for(Car car: cars)
		{
			car.draw(g);
		}
	}

	// Update the positions of all cars in this train.
	@Override
	public void update()
	{
		for(Car car: cars)
		{
			car.update();
		}		
	}

}
