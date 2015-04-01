package SimpleEngine.interfaces;


/*
 * Interface the specifies the properties that a room must have.
 * 
 * Written by Bryce Summers on 3/18/2015.
 */

public interface Room extends OBJ, MouseInput, KeyInput
{
	public int getW();
	public int getH();
}
