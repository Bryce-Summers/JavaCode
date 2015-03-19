package Game_Engine.Engine.Objs.actionLogging;

import java.io.File;
import java.util.Iterator;

import util.FileIO;
import util.KeyParser;
import Data_Structures.Structures.List;
import Data_Structures.Structures.UBA;
import Game_Engine.Engine.engine.Game_input;
import Game_Engine.Engine.engine.Game_looper;

/*
 * The Command is a data structure that stores actions that a cursor could do.
 */

public class Command
{
	
	public static final int M_PRESS   = 0;
	public static final int M_RELEASE = 1;
	public static final int M_MOVE	  = 2;

	public static final int K_PRESS   = 3;
	public static final int K_RELEASE = 4;
	
	public static final int SET_DELAY = 5;

	// Every command has a constant.
	private final int command;
	
	// The location that this command wants the cursor to move to.
	private final int x_dest, y_dest;
	
	// represents a key event constant.
	private final int key;

	public Command(int val)
	{
		command = val;
		
		x_dest = 0;
		y_dest = 0;
		
		key = 0;
	}
	
	// The standard way to create key commands and delays.
	public Command(int val, int input)
	{
		command = val;
		
		x_dest = 0;
		y_dest = 0;
		
		this.key = input;
	}

	// The standard way to create movement commands.
	public Command(int val, int x, int y)
	{
		
		command = val;
		
		x_dest = x;
		y_dest = y;
		
		// Delay amount.
		key = 30;
	}
	
	// The standard way to create movement commands with a set delay.
	public Command(int val, int x, int y, int delay)
	{
		
		command = val;
		
		x_dest = x;
		y_dest = y;
		
		// Delay amount.
		key = delay;
	}
	
	public int getCommand()
	{
		return command;
	}
	
	public int getKey()
	{
		return key;
	}
	
	public int getDestX()
	{
		return x_dest;			
	}

	public int getDestY()
	{
		return y_dest;
	}
	
	// FIXME : Find a way to use the static variable names of keys instead of their temporary keycodes.
	// FIXME : Log both serialized data and pure java code.
	
	// -- logging output functions.
	// These functions define the format of text logging files.
	
	// Files to contain the output logs.
	public final static File serialFile = FileIO.parseFile("serialKeyLog.txt");
	private final static File javaFile  = FileIO.parseFile("javaKeyLog.txt");
	
	public static void openLogs()
	{
		FileIO.openFile(serialFile);
		FileIO.openFile(javaFile);
	}
	
	public static void logKeyP(int key)
	{		
		FileIO.print(serialFile, "K_PRESS");
		//FileIO.print(serialFile, KeyParser.getKeyText(key));
		FileIO.print(serialFile, key);
		
		// -- Java log.
		FileIO.print(javaFile, "program.add(new Command(Command.M_MOVE, " + Game_input.mouse_x + ", " + Game_input.mouse_y  + "));" );
		FileIO.print(javaFile, "program.add(new Command(Command.K_PRESS, " + KeyParser.getKeyText(key) + "));");
	}
	
	public static void logKeyR(int key)
	{
		FileIO.print(serialFile, "K_RELEASE");
		//FileIO.print(serialFile, KeyParser.getKeyText(key));
		FileIO.print(serialFile, key);
		
		
		FileIO.print(javaFile, "program.add(new Command(Command.K_RELEASE, " + KeyParser.getKeyText(key) + "));");
	}
	
	public static void logMove(int mouse_x, int mouse_y)
	{
		
		int delay = Game_looper.getElapsedSteps();
		
		FileIO.print(serialFile, "M_MOVE");
		FileIO.print(serialFile, mouse_x);
		FileIO.print(serialFile, mouse_y);
		FileIO.print(serialFile, delay);

		
		FileIO.print(javaFile, "program.add(new Command(Command.M_MOVE, " + mouse_x + ", " + mouse_y + ", " + delay + "));" );
	}
	
	public static void logMouseP()
	{
		FileIO.print(serialFile, "M_PRESS");
		
		FileIO.print(javaFile, "program.add(new Command(Command.M_PRESS));" );
	}
	
	public static void logMouseR()
	{
		FileIO.print(serialFile, "M_RELEASE");
		
		FileIO.print(javaFile, "program.add(new Command(Command.M_RELEASE));" );
	}
	
	// -- Log input.

	// REQUIRES : The input list should have been serialized through this Command's output logging functions.
	public static UBA<Command> computeProgramFromFile(File file)
	{
		List<String> data = FileIO.readFile(file);

		List<Command> output = new List<Command>();
		
		Iterator<String> iter = data.iterator();
		
		// Create the command list.
		while(iter.hasNext())
		{
			output.add(getNextCommand(iter));
				
		}// end of while loop.
		
		return output.toUBA();
	}
	
	// REQUIRES : iter must have a next element.
	// The input list should have been serialized through this Command's output loggin functions.
	private static Command getNextCommand(Iterator<String> iter)
	{
		String command = iter.next();
		
		if(command.equals("K_PRESS"))
		{
			int key = new Integer(iter.next());
			return new Command(K_PRESS, key);
		}
		else if(command.equals("K_RELEASE"))
		{
			int key = new Integer(iter.next());
			return new Command(K_RELEASE, key);
		}
		else if(command.equals("M_MOVE"))
		{
			// Extract data.
			int x = new Integer(iter.next());
			int y = new Integer(iter.next());
			int delay = new Integer(iter.next());

			return new Command(M_MOVE, x, y, delay);
		}
		else if(command.equals("M_PRESS"))
		{
			// Extract data.
			return new Command(M_PRESS);
		}
		else if(command.equals("M_RELEASE"))
		{
			return new Command(M_RELEASE);
		}
		
		return null;
	}
	
	
}
