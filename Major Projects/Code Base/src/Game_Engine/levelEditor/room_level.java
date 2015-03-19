package Game_Engine.levelEditor;

import java.awt.event.KeyEvent;
import java.io.File;

import util.deSerialB;
import Game_Engine.Engine.Objs.Room;


/* Level loading and playing room.
 *  
 * Written by Bryce Summers on 10 - 16 - 2013.
 * 
 * The room that specifies the loading of levels and provides the
 * container for the myriad of array of objects that interact to
 * form the game experience.
 * 
 * Updated 5/9/2014 : Wall fills are now centered on the glyph to prevent problems due to numerical errors in wall creation.
 */

public abstract class room_level extends Room implements deSerialB
{
	// Private data.
	
	private final File myFile;
	
	// Necessary Default Constructor.
	public room_level(File file)
	{
		set_dimensions(getGameW(), getGameW());
		
		// Require the looper to run this room in full animation mode.
		animation_preference = Animation_preference.FULL;
		
		myFile = file;
	}
	
	// Provides the game width to the abstract room_editor.
	public abstract int getGameW();
	
	// Provides the game height to the abstract room_editor.
	public abstract int getGameH();


	@Override
	public void iObjs()
	{
		set_dimensions(getGameW(), getGameH());
		
		//File file = FileIO.parseFile("Hoth_Levels", "Demo Levels", "level.Hoth");
		
		iObjectHandlers();
		serializeFrom(myFile);
	}
	
	// Should specify the various global object handlers such as lighting, solids, physics, particle systems ect.
	public abstract void iObjectHandlers();

	@Override
	public abstract String getExtension();


	// The Level initiation process.
	@Override
	public abstract void serializeFrom(File file);

	
	@Override
	public void keyP(int key)
	{
		super.keyP(key);
		
		// Allows the user to go back to the level editor.
		if(key == KeyEvent.VK_L)
		{
			returnToEditor();
		}
		
		if(key == KeyEvent.VK_M)
		{
			Room menu = createMenuRoom();
			room_goto(menu);
			return;
		}
		
		
	}
	
	// Returns this room_level back to the editor.
	public void returnToEditor()
	{
		room_editor temp = createEditorRoom();
		room_goto(temp);
		temp.openLevel(myFile);
		return;
	}
	
	public abstract room_editor createEditorRoom();
	public abstract Room createMenuRoom();
}
