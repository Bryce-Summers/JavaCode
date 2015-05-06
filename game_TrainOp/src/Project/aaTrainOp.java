package Project;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import util.interfaces.Action;
import BryceImages.Operations.ImageFactory;
import GUI.ImageObj;
import GUI.UI_Button;
import GUI.UI_SelectionButton;
import GUI.UI_Slider;
import GUI.UI_TextBox;
import Images.Spr;
import Project.GameGrid.Grid;
import Project.GameGrid.gridSquare;
import Project.GameGrid.gui_LogicMapping;
import Project.fonts.FontManager;
import Project.interfaces.Consumer;
import SimpleEngine.MainRoom;
import SimpleEngine.interfaces.OBJ;

/*
 * This class sets up the main game room.
 * 
 * Written by Bryce Summers.
 * 
 * Adapted to my train sorting game on 3/30/2015.
 */

public class aaTrainOp extends MainRoom
{

	public static void main(String[] args)
	{
		// Render the track images.
		Spr.renderTracks();
		
		//new aaTrainOp("Train Ops!", 1200, 800);
		new aaTrainOp("Train Ops!", 1920, 1080);
	}
	
	public aaTrainOp(String gameName, int w, int h)
	{
		super(gameName, w, h);
		
	}

	@Override
	public void initialize(int w, int h)
	{
		int wr = 200;
		int hr = 64;
		
		int menu_y = 64;
		
		// -- The Tool tip. Replace with a movable textbox.
		UI_TextBox box = new UI_TextBox(0, menu_y/4, 400, 32, "Please Click on a Button");
		box.setFont(FontManager.block_label);
		addOBJ(box);
		
		gui_LogicMapping logicMapper = new gui_LogicMapping(x, y + 64);
		//addOBJ(logicMapper);
		
		Grid world = new Grid(8, 216, w - 16, h - 32, 32, logicMapper);
		addOBJ(world);
		
		int separation = 32;		
		
		int x = separation;
		
		//x += separation +  gui_logic_blocks(x, menu_y, world, box);
		
		x += separation + gui_track_pieces(x, menu_y, world, box);

		x += separation + gui_car(x, menu_y, world, box);
		
		//gui_selection_button(x, menu_y, world, box, logicMapper);
		
		x += separation + gui_arrows(x, menu_y, world, box);
		
		box = new UI_TextBox(x, menu_y, 300, 64, "Click grid to build!");
		box.setFont(FontManager.font_LOGIC_BLOCK);
		addOBJ(box);
		
		box = new UI_TextBox(x, menu_y + 64 + 8, 300, 64, "Right Click to remove");
		box.setFont(FontManager.font_LOGIC_BLOCK);
		addOBJ(box);
		
		
		BufferedImage down_arrow = Spr.arrow_icon[0];
		UI_TextBox label = new UI_TextBox(x + 150 - down_arrow.getWidth()/2,
				menu_y + 64 + separation/2, "", down_arrow);
		
		
		//label.setFont(FontManager.font_LOGIC_BLOCK);
		addOBJ(label);

	}
	
	// -- Decomposition functions for each segment of the GUI.
	private void gui_world(int x, int h)
	{
		
	}
	
	// Returns width.
	private int gui_logic_blocks(int x, int y, Grid world, UI_TextBox box)
	{
		

		
		int y2 = y; // 64*2;
		
		int x2 = x;
		
		{
			UI_Button b;
			b = new UI_SelectionButton(x2, y2, "True", ImageFactory.blank(64,  64));
			b.setAction(() -> world.setLogicBlockMode(Grid.LOGIC.TRUE));
			String message = "Create a True block.";
			b.setOnMouseMove(() -> do_and_move(() -> box.setText(message), box, b.getX(), b.getY()));
			b.setFont(FontManager.font_LOGIC_BLOCK);
			addOBJ(b);
			x2 += 64;
		}
		
		{
			UI_Button b;
		
			b = new UI_SelectionButton(x2, y2, "False", ImageFactory.blank(64,  64));
			b.setAction(() -> world.setLogicBlockMode(Grid.LOGIC.FALSE));
			String message = "Create a False Block.";
			b.setOnMouseMove(() -> do_and_move(() -> box.setText(message), box, b.getX(), b.getY()));
			b.setFont(FontManager.font_LOGIC_BLOCK);
			addOBJ(b);
			x2 += 64;
		}
		
		{
			UI_Button b;
		
			b = new UI_SelectionButton(x2, y2, "and", ImageFactory.blank(64,  64));
			b.setAction(() -> world.setLogicBlockMode(Grid.LOGIC.AND));
						
			String message = "Create a conjunctive AND block.";
			b.setOnMouseMove(() -> do_and_move(() -> box.setText(message), box, b.getX(), b.getY()));
			b.setFont(FontManager.font_LOGIC_BLOCK);
			addOBJ(b);
			x2 += 64;
		}
		
		{
			UI_Button b;
			b = new UI_SelectionButton(x2, y2, "or", ImageFactory.blank(64,  64));
			b.setAction(() -> world.setLogicBlockMode(Grid.LOGIC.OR));
						
			String message = "Create a disjunctive OR block.";
			b.setOnMouseMove(() -> do_and_move(() -> box.setText(message), box, b.getX(), b.getY()));
			
			b.setFont(FontManager.font_LOGIC_BLOCK);
			addOBJ(b);
			x2 += 64;
		}
		
		{
			UI_Button b;
		
			b = new UI_SelectionButton(x2, y2, "not", ImageFactory.blank(64,  64));
			b.setAction(() -> world.setLogicBlockMode(Grid.LOGIC.NOT));
			
			String message = "Create a Negation Block.";
			b.setOnMouseMove(() -> do_and_move(() -> box.setText(message), box, b.getX(), b.getY()));
			
			b.setFont(FontManager.font_LOGIC_BLOCK);
			addOBJ(b);
			x2 += 64;
		}
				
		
		int w = x2 - x;
		
		
		UI_TextBox label = new UI_TextBox(x, y + 64, w, 32, "logic");
		label.setFont(FontManager.block_label);
		addOBJ(label);
		
		return w;
	}
	
	private int gui_track_pieces(int x, int y, Grid world, UI_TextBox box)
	{
		
		int x2 = x;
		
		// Track Creation Buttons.
		for(int i = 0; i < 6; i++)
		{
		
			
			UI_SelectionButton b;
			
			b = new UI_SelectionButton(x + i*64, y, "", Spr.full_tracks[i]);
			int index = i;
			b.setAction(() -> world.setTrackType(index));
			b.setOnKeyP((Integer key) -> do_if(key, KeyEvent.VK_1 + index, () ->(world.setTrackType(index))));
							
			String message = "Create a Track Piece";
			b.setOnMouseMove(() -> do_and_move(() -> box.setText(message), box, b.getX(), b.getY()));
			
			if(i == 0 || i == 1)
			{
				b.setImageOffsetX(16);
				b.setImageOffsetY(16);
			}
			
		
			b.setW(64);
			b.setH(64);
			addOBJ(b);
			
			if(i == 1)
			{
				b.select();
				world.setTrackType(1);
			}
			
			/*
			UI_TextBox l = new UI_TextBox(x + 64*i + 16, y + 64 + 16, "" + index, ImageFactory.blank(32,  32));
			l.setFont(FontManager.font_gridBlock);
			addOBJ(l);
			*/

			createHotKey(("" + (i + 1)).charAt(0), x2, y);
			
					
			x2 += 64;
			

			
		}
		
		int w = x2 - x;
		
		UI_TextBox label = new UI_TextBox(x, y + 64, x2 - x, 32, "Track Pieces");
		label.setFont(FontManager.block_label);
		addOBJ(label);
		
		return w;
	}
	
	// Slider plus selection button.
	private int gui_car(int x, int y, Grid world, UI_TextBox box)
	{
		UI_Button b;
		
		int i = 0;
		
		x += 16;
		
		// Car button.
		b = new UI_SelectionButton(x, y + Spr.car.getHeight()*i, "" + i, Spr.car_icon);

		
		b.setFont(FontManager.font_15);
		//b.setTextColor(Color.WHITE);
		b.setTextColor(Color.BLACK);

		b.setW(64);
		b.setH(64);
		b.setImageOffsetY(16);
		b.setImageOffsetX(8);
		
		b.setTextOffset(0, -4);
		
		addOBJ(b);
		
		UI_Slider slider = new UI_Slider(x - 16, y + 64, 64 + 32, 32, 10);
		
		// Link up the functionality of the label and slider.
		slider.setOnSlide((Integer state) -> b.setText("" + state));
		slider.setAction(() -> ((UI_SelectionButton)b).select());
		b.setAction(() -> world.setCarMode(slider.getState()));
				
		b.setOnKeyP((Integer key) -> do_if(key, KeyEvent.VK_C, () ->(world.setCarMode(slider.getState()))));
		
		String message = "Create a car with load ";
		b.setOnMouseMove(() -> do_and_move(() -> box.setText(message + slider.getState()),
				box, b.getX(), b.getY()));
		
		addOBJ(slider);
		
		createHotKey('c', x, y);
		
		
		return 64 + 32;
	}
	

	private void gui_selection_button(int x, int y, Grid world, UI_TextBox box, gui_LogicMapping logic)
	{
		UI_Button b;
	
		// Selection Button.
		b = new UI_Button(x, y, "Select", ImageFactory.blank(256,  64));
		b.setAction(() -> world.setSelectionMode());
		b.setOnMouseMove(() -> box.setText("Go into relationship mapping mode."));
		addOBJ(b);
		
		logic.setX(x);
		logic.setY(y + 64);
	}
	

	private int gui_arrows(int x, int y, Grid world, UI_TextBox box)
	{
		
		int x2 = x;
		
		// Arrow Buttons.
		for(int i = 0; i < 4; i++)
		{
			UI_Button b;
			b = new UI_SelectionButton(x + i*64, y, "", Spr.arrow_icon[i]);
			int index = i;
			b.setAction(() -> world.setDirection(index));

			String message = gridSquare.getMessageForTrackIndex(i) + " switching.";
			b.setOnMouseMove(() -> do_and_move(() -> box.setText(message), box, b.getX(), b.getY()));
			
			// Hotkey mapping and visual display.
			// Special case for 0. <7,8,9,0>
			int keycode = index != 3 ? KeyEvent.VK_7 + index
					: KeyEvent.VK_0;
			
			b.setOnKeyP((Integer key) -> do_if(key, keycode, () ->(world.setTrackType(index))));
			
			// Special case for 0.
			// <7,8,9,0>
			if(index != 3)
			{
				createHotKey(("" + (i + 7)).charAt(0), x2, y);
			}
			else
			{
				createHotKey('0', x2, y);
			}
			
			// Center arrow icons.
			b.setImageOffsetX(7);
			b.setImageOffsetY(7);
						
			b.setW(64);
			b.setH(64);
			addOBJ(b);
			
			/*
			UI_TextBox l = new UI_TextBox(x + 64*i + 16, y + 64 + 16, "" + index, ImageFactory.blank(32,  32));
			l.setFont(FontManager.font_gridBlock);
			addOBJ(l);
			*/
			
			
			
			
			x2 += 64;
		}
		
		int w = x2 - x;
		
		UI_TextBox label = new UI_TextBox(x, y + 64, x2 - x, 32, "Switch Directions");
		label.setFont(FontManager.block_label);
		addOBJ(label);
		
		return w;
	}

	
	
	// A function that allows us to do actions according to some matching procedure.
	
	private static void do_if(Object o, Object expected, Action action)
	{
		if(o.equals(expected))
		{
			action.action();
		}
	}
	
	private static void do_and_move(Action action, UI_TextBox box, int x, int y)
	{
		action.action();
		
		box.setX(x);
		//box.setY(y);
	}
		
	public void draw(Graphics g)
	{
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, getW() - 1, getH() - 1);
		super.draw(g);
				
	}
	
	// Creates a label that displays a character.
	// These should be used to tell inform the user of a particular key.
	private void createHotKey(char c, int x, int y)
	{
		UI_TextBox hotkey = new UI_TextBox(x + 16, y + 64 + 32 + 16, 32, 32, "" + c);
		hotkey.setFont(FontManager.font_12);
		addOBJ(hotkey);
	}

}
