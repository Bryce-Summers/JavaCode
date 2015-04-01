package Project.GameGrid;

/*
 * An interface that specifies functions that 
 * consume particular types of inputs.
 */

public interface Consumer<E> 
{
	public void consume(E input);
}
