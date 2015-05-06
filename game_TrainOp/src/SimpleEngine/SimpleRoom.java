package SimpleEngine;

import java.awt.Graphics;
import java.util.HashSet;

import GUI.OBJ2D;
import Game_Engine.Engine.Objs.Obj;
import SimpleEngine.interfaces.KeyInput;
import SimpleEngine.interfaces.MouseInput;
import SimpleEngine.interfaces.OBJ;
import SimpleEngine.interfaces.Room;

public abstract class SimpleRoom extends OBJ2D implements Room
{

	private int w, h;
	
	// The set of currently active objects.
	HashSet<OBJ> set;
	
	public SimpleRoom(int x, int y, int w, int h)
	{
		super(x, y);
		
		this.w = w;
		this.h = h;
		
		set = new HashSet<OBJ>();
		
	}
	
	public SimpleRoom(int w, int h)
	{
		super(0, 0);
		
		this.w = w;
		this.h = h;
		
		set = new HashSet<OBJ>();
		
	}

	@Override
	public int getW()
	{
		return w;
	}

	@Override
	public int getH()
	{
		// TODO Auto-generated method stub
		return h;
	}
	
	public void update()
	{
		for(OBJ o : set)
		{
			o.update();
		}
	}
	
	public void draw(Graphics g)
	{
		for(OBJ o : set)
		{
			o.draw(g);
		}
	}
	
	// Efficient functions that enable and disable objects.
	public void addOBJ(OBJ obj)
	{
		set.add(obj);
	}
	
	public void removeOBJ(OBJ obj)
	{
		set.remove(obj);
	}

	@Override
	public void global_mouseP()
	{
		for(OBJ o : set)
		{
			if(o instanceof MouseInput)
			{
				MouseInput o2 = (MouseInput)o;
				o2.global_mouseP();
			}
		}
	}

	@Override
	public void global_mouseR()
	{
	
		for(OBJ o : set)
		{
			if(o instanceof MouseInput)
			{
				MouseInput o2 = (MouseInput)o;
				o2.global_mouseR();
			}
		}	
		
	}

	@Override
	public void global_mouseD(int x, int y) {

		for(OBJ o : set)
		{
			if(o instanceof MouseInput)
			{
				MouseInput o2 = (MouseInput)o;
				o2.global_mouseD(x, y);
			}
		}	
		
	}

	@Override
	public void global_mouseM(int x, int y)
	{
	
		for(OBJ o : set)
		{
			if(o instanceof MouseInput)
			{
				MouseInput o2 = (MouseInput)o;
				o2.global_mouseM(x, y);
			}
		}	
		
	}
	
	@Override
	public void global_mouseScroll(int scroll) 
	{
		for(OBJ o : set)
		{
			if(o instanceof MouseInput)
			{
				MouseInput o2 = (MouseInput)o;
				o2.global_mouseScroll(scroll);
			}
		}	
	}

	@Override
	public void mouseP(int x, int y)
	{
		for(OBJ o : set)
		{
			if(o instanceof MouseInput)
			{
				MouseInput o2 = (MouseInput)o;
				if(o2.mouseCollision(x, y))
				{
					o2.mouseP(x, y);
				}
			}
		}
	}

	@Override
	public void mouseR(int x, int y)
	{
		for(OBJ o : set)
		{
			if(o instanceof MouseInput)
			{
				MouseInput o2 = (MouseInput)o;
				if(o2.mouseCollision(x, y))
				{
					o2.mouseR(x, y);
				}
			}
		}		
	}

	@Override
	public void mouseD(int x, int y)
	{
		for(OBJ o : set)
		{
			if(o instanceof MouseInput)
			{
				MouseInput o2 = (MouseInput)o;
				if(o2.mouseCollision(x, y))
				{
					o2.mouseD(x, y);
				}
			}
		}
	}

	@Override
	public void mouseM(int x, int y)
	{
		for(OBJ o : set)
		{
			if(o instanceof MouseInput)
			{
				MouseInput o2 = (MouseInput)o;
				if(o2.mouseCollision(x, y))
				{
					o2.mouseM(x, y);
				}
			}
		}		
	}
	
	public void mouseScroll(int x, int y, int scroll)
	{
		for(OBJ o : set)
		{
			if(o instanceof MouseInput)
			{
				MouseInput o2 = (MouseInput)o;
				if(o2.mouseCollision(x, y))
				{
					o2.mouseScroll(x, y, scroll);
				}
			}
		}		
	}

	@Override
	public void keyP(int key)
	{
		for(OBJ o : set)
		{
			if(o instanceof KeyInput)
			{
				KeyInput o2 = (KeyInput)o;
				o2.keyP(key);
			}
		}
	}

	@Override
	public void keyR(int key)
	{
		for(OBJ o : set)
		{
			if(o instanceof KeyInput)
			{
				KeyInput o2 = (KeyInput)o;
				o2.keyR(key);
			}
		}		
	}

	// FIXME : This should be constrained to the bounding box of the room
	public boolean mouseCollision(int x, int y)
	{
		
		// Bounding box.
		if(x < getX() || x >= getX() + w || y < getY() || y >= getY() + getH())
		{
			return false;
		}
		
		// Try to send collision notifications to all of the sub objects first.
		for(OBJ o : set)
		{
			if(o instanceof MouseInput)
			{
				MouseInput o2 = (MouseInput)o;
				if(o2.mouseCollision(x, y))
				{
					return true;
				}
			}			
		}		
		
		return true;
	}
	
}
