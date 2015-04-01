package Project.GameGrid;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import BryceImages.Operations.ImageFactory;
import BryceMath.Calculations.Colors;
import GUI.OBJ2D;
import GUI.UI_Button;
import Images.Spr;
import SimpleEngine.Game_input;
import SimpleEngine.interfaces.MouseInput;

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
	private int track_type = NOTHING_SELECTED;
	
	public Grid(int x1, int y1, int x2, int y2, int size)
	{
		super(x1, y1);
		
		square_size = size;
		
		w = x2 - x1;
		h = y2 - y1;

		num_rows    = (h)/size + 1;
		num_columns = (w)/size;
		
		puzzle_components = ImageFactory.ColorRect(new Color(0, 255, 0), size*num_columns, size*num_rows);
		
		
		// [y][x]
		squares = new gridSquare[num_rows][num_columns];
		
		Color color_square = Colors.C_CLEAR;
		BufferedImage image = ImageFactory.ColorRect(color_square, size, size);
		
		for(int x = x1; x < x2; x += size)
		for(int y = y1; y < y2; y += size)
		{
			gridSquare square = new gridSquare(x, y, image);
			
			int index_row 	 = getRow(y);
			int index_column = getColumn(x);
			
			square.setAction(()-> handle_click(index_row, index_column));

			squares[index_row][index_column] = square;
		}
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
	}

	@Override
	public void update()
	{
		actionAll(square -> (square.update()));		
	}
	
	
	/*
	 * This method is called whenever the user clicks on a grid square.
	 * 
	 * 
	 * 
	 */
	private void handle_click(int index_y, int index_x)
	{
		
		
		
		// Build a track if a track piece is currently selected.
		if(track_type != NOTHING_SELECTED)
		{
			// Map from world space to composite image space.
			Graphics g = puzzle_components.getGraphics();
			g.translate(-x, -y);
			
			
			int num_x = 1;
			int num_y = 1;
			
			if(Spr.isCurve(track_type))
			{
				num_x = 2;
				num_y = 2;
			}
			
			zzz
			
			// FIXME : Implement the block additions and deletions of the tracks inside of the gridSquare class.
			
			gridSquare square = getSquare(index_x, index_y); 
			int image_x = square.getX();
			int image_y = square.getY();
			Sprite spr = new Sprite(image_x, image_y,
					Spr.tracks_basalt[track_type],
					Spr.tracks_rails[track_type]);
			
			for(int r = index_y; r < index_y + num_x; r++)
			for(int c = index_x; c < index_x + num_y; c++)
			{
				square = getSquare(c, r);
				square.addTrack(spr);
				square.drawComponents(g);
			}
			
			g.dispose();
			
			// FIXME : Add curves to multiple squares.
		}
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
		
	}

	@Override
	public void global_mouseScroll(int scroll) {

		actionAll(square -> (square.global_mouseScroll(scroll)));	
	}

	@Override
	public void mouseP(int x, int y) {

		actionInBounds(square -> (square.mouseP(x, y)), x, y);	
		
	}

	@Override
	public void mouseR(int x, int y) {
		actionInBounds(square -> (square.mouseR(x, y)), x, y);	
	}

	@Override
	public void mouseD(int x, int y) {

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
	}

}
