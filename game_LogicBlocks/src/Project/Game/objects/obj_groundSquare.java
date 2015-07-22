package Project.Game.objects;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import util.interfaces.Function;

import BryceGraphs.ADTs.AdjacencyNode;
import BryceGraphs.Algorithms.GraphSearch;
import Data_Structures.Structures.BitSet;
import Data_Structures.Structures.List;
import Data_Structures.Structures.SingleLinkedList;
import Data_Structures.Structures.HashingClasses.AArray;
import Game_Engine.Engine.Objs.ImageB;
import Game_Engine.Engine.Objs.Obj;
import Game_Engine.GUI.Components.small.gui_handle;
import Game_Engine.SpriteFactories.ArrowImageFactory;
import Project.Editor.Components.Spr;

/* 
 * Written by Bryce Summers on 6 - 21 - 2014.
 * 
 * Purpose : Ground defines all of the possible movement in the game.
 * 			 Defines the movement graph and the playable squares. Provides shape to the game.
 */

public class obj_groundSquare extends Obj implements AdjacencyNode<obj_groundSquare>
{

	// Keep track of last ground targeted with no click (move) and click (drag).
	public static obj_groundSquare last_move = null;// start node.
	public static obj_groundSquare last_drag = null;// end node.
	
	public static obj_groundSquare source = null;
	public static AArray<obj_groundSquare, SingleLinkedList<obj_groundSquare>> shortest_paths_from_move;
	
	public static SingleLinkedList<obj_groundSquare> empty_list = new SingleLinkedList<obj_groundSquare>();
	
	public static obj_mover current_mover;
	
	// A path from the destination back to the source due to immutable stacks.
	public static SingleLinkedList<obj_groundSquare> current_shortest_path = empty_list;
	
	private boolean in_path;
	final BitSet direction_properties;
	
	public obj_groundSquare(double x_in, double y_in, BitSet b)
	{
		super(x_in, y_in);
		
		sprite = Spr.ground_square;
		
		direction_properties = b;
	}
	
	@Override
	protected void update()
	{
		if(last_drag == this)
		{
			setSprite(Spr.ground_square_drag);
			return;
		}

		if(in_path)
		{
			setSprite(Spr.ground_square_in_path);
			return;
		}
		
		
		if(last_move == this)
		{
			setSprite(Spr.ground_square_move);
			return;
		}
		
		setSprite(Spr.ground_square);

	}
	
	public void setSprite(BufferedImage spr)
	{
		if(sprite != spr)
		{
			redraw();
			sprite = spr;
			redraw();
		}
	}
	
	// FIXME : I need to make sure the 
	@Override
	public void global_mouseM(int mx, int my)
	{
		if(mouseCollision(mx, my))
		{			
			last_move = getCorrectSquare();
		}

	}
	
	@Override
	public void global_mouseD(int mx, int my)
	{
		if(mouseCollision(mx, my))
		{			
			last_drag = getCorrectSquare();
			updateCurrentShortestPath();
		}
	
	}
	
	@Override
	public void global_mouseR()
	{
		// Only do this once.
		if(last_drag == this)
		{
			deHighlightShortestPath();
			// Forget all remembered nodes.
			//last_move = null;
			//last_drag = null;
		}
	}
	
	public void global_mouseP()
	{
		if(last_move == this)
		{
			computeShortestPaths();
		}
	}
	
	// Returns the left top square of the obj_mover at this location if present, otherwise returns this.
	private obj_groundSquare getCorrectSquare()
	{
		int w_h = sprite.getWidth()/2;
		int h_h = sprite.getHeight()/2;
		obj_mover mover = (obj_mover) instance_position(getX() + w_h, getY() + h_h, obj_mover.class);

		obj_groundSquare result = null;
		
		if(mover != null)
		{
			result = (obj_groundSquare) instance_position(mover.getX() + w_h,
										  mover.getY() + h_h, obj_groundSquare.class);
		}
		
		return result == null ? this : result;
	}
	
	@Override
	public List<obj_groundSquare> getNeighbors()
	{
		List<obj_groundSquare> output = new List<obj_groundSquare>();
	
		obj_mover mover = (obj_mover)gui_handle.handle_held;
		
		int w = sprite.getWidth();
		int h = sprite.getHeight();
		
		double x = getX();
		double y = getY();
		
		// Do not propagate when a teleporter is present.
		if(mover.instance_place(x - mover.holdOffsetX(),
								y - mover.holdOffsetY(),
								obj_teleporter.class) != null)
		{
			return output;
		}
		
		BitSet b_mover = current_mover.getDirectionSet();

		// Compute the set of directions allowed by the square and the mover.
		BitSet b = b_mover.AND(direction_properties); 
		
		/* Add positions in directions that the piece is allowed to go. */
		
		// Try non diagonal directions first.
		if(b.getBit(0)){addPosition( w,  0, output);}
		if(b.getBit(2)){addPosition( 0, -h, output);}
		if(b.getBit(4)){addPosition(-w,  0, output);}
		if(b.getBit(6)){addPosition( 0,  h, output);}
		
		// Try diagonal directions second.
		if(b.getBit(1)){addPosition( w, -h, output);}
		if(b.getBit(3)){addPosition(-w, -h, output);}
		if(b.getBit(5)){addPosition(-w,  h, output);}
		if(b.getBit(7)){addPosition( w,  h, output);}
		
		return output;
		
	}
	
	@Override
	public int getNeighborSize()
	{
		return getNeighbors().size();
	}

	private class find_color implements Function<Obj, Boolean>
	{

		private final int index;
		
		public find_color(int index)
		{
			this.index = index;
		}
		
		@Override
		public Boolean eval(Obj input)
		{
			if(input instanceof obj_mover)
			{
				return ((obj_mover)input).index == this.index;
			}
			
			return false;
		}
		
	}
	
	private void addPosition(int x_inc, int y_inc, List<obj_groundSquare> L)
	{
		int x = (int)getX() + x_inc;
		int y = (int)getY() + y_inc;
		
		obj_mover mover = (obj_mover)gui_handle.handle_held;
		
		Obj o;

		// Search for a non jumpable obstacle.
		o = mover.instance_place(x, y, obj_mover.class, new find_color(mover.index));
		
		if(o != null)
		{
			return;
		}
		
		// Search for a jumpable obstacle.
		o = mover.instance_place(x, y, obj_mover.class);
		
		// Try to jump over it.
		if(o != null)
		{
			x += x_inc;
			y += y_inc;
			o = mover.instance_place(x, y, obj_mover.class);

			
			if(o != null)
			{
				return;
			}
		}
		
		// Check all positions for in bounds.
		int x_max = x + mover.getW();
		int y_max = y + mover.getH();
		
		
		for(int xx = x; xx < x_max; xx += 64)
		for(int yy = y; yy < y_max; yy += 64)
		{
			Obj o2 = mover.instance_position(xx, yy, obj_groundSquare.class);
			if(o2 == null)
			{
				return;
			}
		}
		
		o = instance_position(x, y, obj_groundSquare.class);
		
		if(o != null && o.getClass() == obj_groundSquare.class)
		{
			L.add((obj_groundSquare)o);
		}		
	}
	
	// Computes the single source shortest paths problem according to move.
	// Warning : Can take a very long time. Be careful.
	private static void computeShortestPaths()
	{
		// Do not recompute when not needed.
		if(source == last_move)
		{
			return;
		}
		
		// Compute the shortest paths if a mover is being held.
		gui_handle h = gui_handle.handle_held;
		
		if(h instanceof obj_mover)
		{
			current_mover = (obj_mover)gui_handle.handle_held;
			shortest_paths_from_move = GraphSearch.shortestPaths(last_move);		
		}

	}
	
	// Looks up a shortest path according to drag.
	private static void updateCurrentShortestPath()
	{
		deHighlightShortestPath();

		current_shortest_path = shortest_paths_from_move.lookup(last_drag);
		
		// Handle no posible path case.
		if(current_shortest_path == null)
		{
			current_shortest_path = empty_list;
		}
		
		highlight_shortest_path();
	}
	
	private static void highlight_shortest_path()
	{
		for(obj_groundSquare o : current_shortest_path)
		{
			obj_groundSquare gs = (obj_groundSquare)o;
			gs.in_path = true;
		}
		
	}
	
	private static void deHighlightShortestPath()
	{
		for(obj_groundSquare o : current_shortest_path)
		{
			obj_groundSquare gs = (obj_groundSquare)o;
			gs.in_path = false;
		}
		
		// Keep the last path for movement calculations.
		//current_shortest_path = empty_list;
	}
	
	// Equality check.
	public boolean equals(Object other)
	{
		return this == other;
	}
	
	public String toString()
	{
		return "X : " + (int)getX()/64 + ", Y : " + (int)getY()/64;
	}

	public static int getMoveLen()
	{
		return Math.max(current_shortest_path.size() - 1, 0);
	}

	public static obj_groundSquare getLastSquareInPath()
	{
		if(current_shortest_path.isEmpty())
		{
			return last_move;
		}
		
		return (obj_groundSquare) current_shortest_path.getFirst();
	}
	
	// Draw arrows on component.
	@Override
	public void draw(ImageB image, AffineTransform AT)
	{
		// Draw image.
		super.draw(image, AT);

		// Draw arrows.
		drawRadialArray(image.getGraphics(), AT, ArrowImageFactory.getArrows(32), direction_properties);
	}


}
