package Project.GameGrid;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Set;

import BryceImages.Operations.ImageFactory;
import BryceMath.Calculations.Colors;
import GUI.FontDrawing;
import GUI.OBJ2D;
import Images.Spr;
import Project.Logic_Blocks.block_AND;
import Project.Logic_Blocks.block_False;
import Project.Logic_Blocks.block_NOT;
import Project.Logic_Blocks.block_OR;
import Project.Logic_Blocks.block_True;
import Project.fonts.FontManager;
import Project.interfaces.Consumer;
import Project.interfaces.Logic_Block;
import SimpleEngine.Game_input;
import SimpleEngine.interfaces.MouseInput;

/*
 * Grid Class
 * 
 * Written by Bryce Summers.
 * 
 * This class represents the logical game world,
 * and allows the user to manipulate the state of the world. 
 */

public class Grid extends OBJ2D implements MouseInput
{

	gridSquare[][] squares;
	final int square_size;
	
	final int num_rows;
	final int num_columns;
	
	BufferedImage puzzle_components;
	
	final int w, h;
	
	final static int NOTHING_SELECTED = -1;
	
	// The current type of track selected.
	public static int track_type = NOTHING_SELECTED;
	
	final Color C_BACKGROUND = new Color(200, 200, 100);
	
	/* TRACK --> Build and Add tracks to the grid.
	 * CAR   --> Build a car spawner.
	 * SELECTION -->  
	 */
	private enum Mode{TRACK, CAR, SELECTION, LOGIC_BLOCK, DIRECTION};
	private Mode current_mode = Mode.TRACK;
	private int car_load = 0;
	private Car_controller car_controller;
	
	public enum LOGIC{TRUE, FALSE, AND, OR, NOT};
	private LOGIC current_logic = LOGIC.TRUE;
	
	private int current_direction = 0;
	
	gui_LogicMapping logic_mapper;
	
	
	public static int mouse_x;
	public static int mouse_y;
	public static boolean draw_ghost = false;
	
	public Grid(int x1, int y1, int x2, int y2, int size, gui_LogicMapping mapper)
	{
		super(x1, y1);
		
		mouse_x = x1;
		mouse_y = y1;
		
		square_size = size;
		
		w = x2 - x1;
		h = y2 - y1;

		num_rows    = (h)/size + 1;
		num_columns = (w)/size + 1;
		
		puzzle_components = ImageFactory.ColorRect(C_BACKGROUND, size*num_columns, size*num_rows);
		
		
		// [y][x]
		squares = new gridSquare[num_rows][num_columns];
		
		Color color_square = Colors.C_CLEAR;
		BufferedImage image = ImageFactory.ColorRect(color_square, size, size);
		
		car_controller = new Car_controller(this);
		
		for(int x = x1; x < x2; x += size)
		for(int y = y1; y < y2; y += size)
		{
			gridSquare square = new gridSquare(x, y, image, car_controller);
			square.setDrawBorders(false);
			int index_row 	 = getRow(y);
			int index_column = getColumn(x);
			
			square.setAction(()-> handle_click(index_row, index_column));
			
			squares[index_row][index_column] = square;
		}
		
		
		logic_mapper = mapper;
		mapper.setCurrentLogicBlock(squares[0][0]);

	}
	
	// Maps from world coordinate space to index space in the squares array.
	public int getRow(int y_in)
	{
		return (y_in - this.y)/square_size;
	}
	
	// Maps from world coordinate space to index space in the squares array.
	public int getColumn(int x_in)
	{
		return (x_in - this.x)/square_size;
	}
	
	public gridSquare getSquare(int index_x, int index_y)
	{
		return squares[index_y][index_x];
	}
		
	@Override
	public void draw(Graphics g)
	{
		
		// Draw RailroadTracks.
		g.drawImage(puzzle_components, x, y, null);
		
		actionAll(square -> (square.draw(g)));
		
		g.setFont(FontManager.font_gridBlock);
		if(current_mode == Mode.SELECTION)
		{
			// Draw the numbers on the 2d Grid.
			Logic_Block root = logic_mapper.getCurrentLogicBlock();

			for(int i = 0; i < gui_LogicMapping.NUM_INPUTS; i++)
			{
				gridSquare input = root.getInput(i);
				FontDrawing.drawText(g, "" + i, input);
			}
		}
		
		// Draw ghost images if the mouse is in bounds.
		if(mouseCollision(mouse_x,  mouse_y))
		{
		
			int x_index = (mouse_x - x)/square_size;
			int x_screen = x_index*square_size + x;
			int y_index = (mouse_y - y)/square_size;
			int y_screen = y_index*square_size + y;
			
			// Draw a ghost track to aid in track creation.
			if(current_mode == Mode.TRACK && track_type >= 0 && draw_ghost &&
					track_allowed(x_index, y_index))
			{
							
				
				g.drawImage(Spr.tracks_basalt[track_type],
						x_screen, y_screen, null);
				
			}
			
			gridSquare square = getSquare(x_index, y_index);
			
			if(current_mode == Mode.LOGIC_BLOCK && !square.containsLogicBlock())
			{
				String message = "";
								
				
				switch(current_logic)
				{
				
				case TRUE: message = "T"; break;
				case FALSE:message = "F"; break;
				case AND:  message = "&"; break;
				case OR:   message = "or";break;
				case NOT:  message = "~"; break;
					
				default:
					break;
				
				}
				g.setColor(Colors.Color_hsv(0, 0, 50));
				FontDrawing.drawText(g, message, square);
			}
			
			if(current_mode == Mode.CAR && !square.containsCarSpawner())
			{
				g.drawImage(Spr.car,
						x_screen, y_screen + 8, null);
			}
		}
		
		// Draw the positions of all of the cars.
		car_controller.draw(g);
	}

	// Returns true if it is safe to build the given track here.
	// This predicate is needed to prevent edge cases and null pointer exceptions.
	private boolean track_allowed(int x_index, int y_index) {
		// TODO Auto-generated method stub
		
		if(track_type > 1)
		{
			return
			x_index < num_columns - 1 &&
			y_index < num_rows - 1;
		}
		
		// Always good for straight tracks.
		return true;
	}

	@Override
	public void update()
	{
		actionAll(square -> (square.update()));
		
		// Update the positions of all of the cars.
		car_controller.update();
	}
	
	
	/*
	 * This method is called whenever the user clicks on a grid square.
	 * 
	 * 
	 * 
	 */
	private void handle_click(int index_y, int index_x)
	{		
		switch(current_mode)
		{
			case TRACK:
					if(track_allowed(index_x, index_y))
					{
						handle_track_mode(index_y, index_x);
					}
						break;
			case CAR : handle_car_mode(index_y, index_x);
						break;
			case SELECTION : handle_selection_mode(index_y, index_x);
						break;
			case LOGIC_BLOCK: handle_logic_mode(index_y, index_x);
						break;
			case DIRECTION: handle_direction_mode(index_x, index_y);
						break;
		}
		
		
	}

	private void handle_direction_mode(int index_x, int index_y) {

		
		gridSquare square = getSquare(index_x, index_y);
		
		if(Game_input.mouse_button == Game_input.LEFT_MOUSE)
		{
			square.setDirection(current_direction);				
		}
		else
		{
			square.setDirection(-1);
		}
		
		
		// Do some drawing.
		Graphics g = getStaticGraphics();
		square.drawComponents(g);
	}

	private void handle_logic_mode(int index_y, int index_x)
	{
		gridSquare square = getSquare(index_x, index_y);
		
		Logic_Block block = null;
		
		if(Game_input.mouse_button == Game_input.LEFT_MOUSE)
		{
			switch(current_logic)
			{
				case TRUE: block = new block_True();break;
				case FALSE:block = new block_False();break;
				case AND:  block = new block_AND(square);break;
				case OR:   block = new block_OR(square);break;
				case NOT:  block = new block_NOT(square);break;
					
				default:
					break;
			}
		}

		square.setLogicBlock(block);
		
		Graphics g = getStaticGraphics();
		square.drawComponents(g);

	}

	private void handle_selection_mode(int index_y, int index_x)
	{
		int current_button = logic_mapper.getCurrentButton();
		
		gridSquare square = getSquare(index_x, index_y);
		
		if(current_button != gui_LogicMapping.NO_INPUT)
		{
			Logic_Block block = logic_mapper.getCurrentLogicBlock();
			
			block.setInput(current_button, square);
			logic_mapper.reset_input();
			return;
		}
		
		logic_mapper.setCurrentLogicBlock(square);
		
		// logic_mapper
	}

	private void handle_track_mode(int index_y, int index_x)
	{
		// Build a track if a track piece is currently selected.
		if(track_type != NOTHING_SELECTED)
		{
			// Map from world space to composite image space.
			Graphics g = getStaticGraphics();
			
			if(Game_input.mouse_button == Game_input.LEFT_MOUSE)
			{
				gridSquare square = getSquare(index_x, index_y); 
				int image_x = square.getX();
				int image_y = square.getY();
				TrackPiece track = new TrackPiece(image_x, image_y, track_type, this, index_x, index_y);
				
				track.draw(g);
			}
			else if(Game_input.mouse_button == Game_input.RIGHT_MOUSE)
			{
				gridSquare square = getSquare(index_x, index_y);
				Set<TrackPiece> track_set = square.deleteAllTracks();
				
				// Update the drawing of the pieces.
				for(TrackPiece piece : track_set)
				{
					piece.draw(g);
				}
			}

			g.dispose();
		}
	}
	
	private void handle_car_mode(int index_y, int index_x)
	{
		gridSquare square = getSquare(index_x, index_y);
		
		// Map from world space to composite image space.
		Graphics g = getStaticGraphics();
		
		if(Game_input.mouse_button == Game_input.LEFT_MOUSE)
		{
			square.setCarSpawn(new Car(car_load));
			
			// FIXME Remove these lines when we go back to car spawners.
			square.forceCarSpawn();
			square.removeCarSpawn();
			// End of temporary code.
			
			square.drawComponents(g);
		}
		else if(Game_input.mouse_button == Game_input.RIGHT_MOUSE)
		{
			square.removeCarSpawn();
			square.drawComponents(g);
		}
		
		g.dispose();
	}

	private Graphics getStaticGraphics()
	{
		// Map from world space to composite image space.
		Graphics g = puzzle_components.getGraphics();
		g.translate(-x, -y);
		g.setColor(C_BACKGROUND);
		
		return g;
	}
	
	// Applies the given consumer to all grid squares.
	// This allows us to contain iteration to this function.
	private void actionAll(Consumer<gridSquare> consumer)
	{
		for(int r = 0; r < num_rows; r++)
		for(int c = 0; c < num_columns; c++)
		{
			gridSquare square = getSquare(c, r);
			
			if(square != null)
			consumer.consume(square);
		}
	}
	
	// Applies the given consumer to all grid squares.
	// This allows us to contain iteration to this function.
	private void actionInBounds(Consumer<gridSquare> consumer, int x, int y)
	{
		for(int r = 0; r < num_rows; r++)
		for(int c = 0; c < num_columns; c++)
		{
			gridSquare square = getSquare(c, r);
			
			if(square != null)
			{
				if(square.mouseCollision(x, y))
				consumer.consume(square);
			}
		}
	}
	
	// Trying to implement more user friendly creation.
	int mouse_x_initial;
	int mouse_y_initial;
	
	int mouse_x_current;
	int mouse_y_current;
	
	private void actionInMouseRegion(Consumer<gridSquare> consumer, int x, int y)
	{
		
		int x1 = Math.min(mouse_x_initial, mouse_x_current);
		int y1 = Math.min(mouse_y_initial, mouse_y_current);
		
		int x2 = Math.max(mouse_x_initial, mouse_x_current);
		int y2 = Math.max(mouse_y_initial, mouse_y_current);
		
		
		
		x1 -= getX();
		x2 -= getY();
		
		y1 -= getY();
		y2 -= getY();
		
		int r1 = getRow(x1);
		int r2 = getRow(x2);
		
		int c1 = getColumn(y1);
		int c2 = getColumn(y2);
		
		for(int r = r1; r <= r2; r++)
		for(int c = c1; c <= c2; c++)
		{
			gridSquare square = getSquare(c, r);
			
			if(square != null)
			{
				if(square.mouseCollision(x, y))
				consumer.consume(square);
			}
		}
	}
	
	@Override
	public int getW() {

		return w;
	}

	@Override
	public int getH() {

		return h;
	}
	
	/*
	 * (non-Javadoc)
	 * @see SimpleEngine.interfaces.MouseInput#global_mouseP()
	 */
	
	
	@Override
	public void global_mouseP()
	{
		actionAll(square -> (square.global_mouseP()));	
	}

	@Override
	public void global_mouseR() {
		actionAll(square -> (square.global_mouseR()));	
		
	}

	@Override
	public void global_mouseD(int x, int y) {
		actionAll(square -> (square.global_mouseD(x, y)));	
	}

	@Override
	public void global_mouseM(int x, int y) {
		
		actionAll(square -> (square.global_mouseM(x, y)));
		
		mouse_x = x;
		mouse_y = y;
	}

	@Override
	public void global_mouseScroll(int scroll) {

		actionAll(square -> (square.global_mouseScroll(scroll)));	
	}


	
	@Override
	public void mouseP(int x, int y) {

		mouse_x_initial = x;
		mouse_y_initial = y;
		
		actionInBounds(square -> (square.mouseP(x, y)), x, y);	
		
	}

	@Override
	public void mouseR(int x, int y) {
		actionInBounds(square -> (square.mouseR(x, y)), x, y);
		
		
		//actionInMouseRegion(square -> (square.mouseR(x, y)), x, y);
	}

	@Override
	public void mouseD(int x, int y) {

		mouse_x_current = x;
		mouse_y_current = y;
		
		actionInBounds(square -> (square.mouseD(x, y)), x, y);	
	}
	
	@Override
	public void mouseM(int x, int y) {

		actionInBounds(square -> (square.mouseM(x, y)), x, y);
	}

	@Override
	public void mouseScroll(int x, int y, int scroll)
	{
		actionInBounds(square -> (square.mouseP(x, y)), x, y);
		
	}

	// Allows external code to set the type of track built when the user clicks on a square.
	public void setTrackType(int type)
	{
		track_type = type;
		current_mode = Mode.TRACK;
	}

	public void setCarMode(int i)
	{
		current_mode = Mode.CAR;
		car_load  = i;
	}
	
	// Have the grid map clicks to updating the selection GUI.
	public void setSelectionMode()
	{
		current_mode = Mode.SELECTION;
	}
	
	public void setLogicBlockMode(LOGIC logic)
	{
		current_mode = Mode.LOGIC_BLOCK;
		current_logic = logic;
	}

	
	// FIXME : Toy version only.
	public void setDirection(int index)
	{	
		current_mode = Mode.DIRECTION;
		current_direction = index;
		
	}

}