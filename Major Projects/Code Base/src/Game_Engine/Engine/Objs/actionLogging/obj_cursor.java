package Game_Engine.Engine.Objs.actionLogging;

import java.io.File;

import util.FileIO;
import Data_Structures.Structures.UBA;
import Game_Engine.Engine.Objs.obj_glidable;
import Game_Engine.GUI.SpriteLoader;
import Game_Engine.GUI.Components.small.gui_label;

/* The fake cursor class.
 * 
 * Written by Bryce Summers on 6 - 17 - 2013.
 * 
 * Purpose : This cursor simulates the actions of an actual cursor and can be used for tutorials.
 */

public class obj_cursor extends obj_glidable
{

	boolean mouse_pressed = false;
	
	int x_last, y_last;
	
	// Constants for mouse action commands.
	final int PRESS   = 1;
	final int UNPRESS = -1;
	
	// Every cursor has a list of commands that it wants to execute.
	private UBA<Command> commands;
	private int current_command_index = 0;
	
	// By default cursors loop through their programs only once.
	private boolean should_loop = false;
	
	private boolean global = false;
	
	public obj_cursor(double x_in, double y_in)
	{
		super(x_in, y_in);
		
		sprite = SpriteLoader.cursor_symbol;
		
		setInterpolationTime(30);
		
		x_last = (int)getX();
		y_last = (int)getY();
		
		setCollidable(false);
		
		setDepth(Integer.MIN_VALUE + 1);
	}
	
	// The user can communicate with the proxy cursor through the special called keyPSpecial method.
	
	// Currently we will terminate the proxy cursor if a key is pressed.
	// NOTE : this method is always called my this cursor's container, assuming that is not blocked by a super proxy_cursor.
	public void keyPSpecial(int key)
	{
		
		// If a key is pressed, then speedup this movement.
		if(global)
		{
			setTimeTillDone(0);			
			return;
		}
		
		myContainer.killProxyCursor();
	}
	
	@Override
	public void update()
	{
		if(!isEnabled())
		{
			return;
		}
		
		super.update();

		handleCommands();
						
		// Process all mouse movements.
		
		int x = (int)this.getX();
		int y = (int)this.getY();
		
		boolean changed = x != x_last || y != y_last;
		
		x_last = x;
		y_last = y;
		
		if(!changed)
		{
			return;
		}
		
		if(global && myContainer.proxy_cursor != null)
		{
			return;
		}
		
		// Process mouse dragged only if the mouse is depressed.
		if(mouse_pressed)
		{
			myContainer.mouseD(x, y);
			myContainer.global_mouseD(x, y);			
		}

		// Mouse moved is processed whether the mouse is depressed or not.
		myContainer.mouseM(x, y);
		myContainer.global_mouseM(x, y);

	}
	
	protected void handleCommands()
	{
		int x = (int)this.getX();
		int y = (int)this.getY();
		
		if(interpolator.done())
		{
			// Handle the end of the loop.
			if(current_command_index >= commands.size())
			{
				if(!should_loop)
				{
					die();
					

					if(global)
					{
						gui_label finis = new gui_label(myContainer.getW() / 2, myContainer.getH() / 2, myContainer.getW()/2, myContainer.getH()/2);
						finis.setText("Done");
						finis.xSub(finis.getW() / 2);
						finis.ySub(finis.getH() / 2);
						finis.setDepth(-Integer.MAX_VALUE);
						myContainer.obj_create(finis);
						return;
					}
					
					myContainer.killProxyCursor();
					
					return;
				}
				glide(x_start, y_start);
				current_command_index = 0;
				return;
			}
			
			Command current_command = commands.get(current_command_index);
			
			int val = current_command.getCommand();
		
			switch(val)
			{
				case Command.M_PRESS:

					if(global && myContainer.proxy_cursor != null)
					{
						break;
					}
					
					myContainer.mouseP(x, y);
					myContainer.global_mouseP();
					mouse_pressed = true;
					break;
				case Command.M_RELEASE:
					
					if(global && myContainer.proxy_cursor != null)
					{
						mouse_pressed = false;
						break;
					}
					
					myContainer.mouseR(x, y);
					myContainer.global_mouseR();
					mouse_pressed = false;
					break;
				case Command.K_PRESS:
															
					if(global && myContainer.proxy_cursor != null)
					{
						myContainer.proxy_cursor.keyPSpecial(current_command.getKey());
						break;
					}
					
					myContainer.keyP(current_command.getKey());
					break;
				case Command.K_RELEASE:
					myContainer.keyR(current_command.getKey());
					break;
				case Command.M_MOVE:
					
					int x_new = current_command.getDestX();
					int y_new = current_command.getDestY();
					int delay = current_command.getKey();
					
					setInterpolationTime(delay);
					
					// Skip movements that are trivial.
					if(Math.abs(x - x_new) < 2 && Math.abs(y - y_new) < 2)
					{
						break;
					}
					
					glide(x_new, y_new);
					break;
				default:
					throw new Error("COMMAND : " + val + " not supported");
			}
			
			current_command_index++;
		}
	}
	
	// Allows the user to program the cursor from an input file.
	public void program(String fileChild)
	{
		File file = FileIO.parseFile(fileChild);
		UBA<Command> program = Command.computeProgramFromFile(file);
		this.commands = program;
	}
	
	// Allows the user to program the cursor from a list of commands.
	public void program(UBA<Command> program)
	{
		this.commands = program;
	}
	
	// Allow outside users to specify this cursor to loop until terminated.
	public void loop()
	{
		should_loop = true;
	}
	
	public boolean isGlobal()
	{
		return global;
	}
	
	public void makeGlobal()
	{
		global = true;
		
		// The global cursor will be drawn overtop of everything, even local proxy cursors. 
		setDepth(Integer.MIN_VALUE);
	}
	
}
