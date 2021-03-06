package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

import Data_Structures.Structures.List;
import Data_Structures.Structures.HashingClasses.AArray;

/*
 * The File input output class.
 * 
 * This class will provide functionality to easily map the printing stream to an output folder.
 * 
 * Written by Bryce Summers on 6 - 25 - 2013.
 */

//http://stackoverflow.com/questions/5288966/changing-standard-output-in-java.

public class FileIO
{
	private static AArray<File, PrintStream> files = new AArray<File, PrintStream>();
	
	// REQUIRES : A list of the ordered path of directories with either a directory or a file at the end.
	// ENSURES  : Returns the proper file that is pathed from the default project or application directory.
	// NOTE : The user should use this method to find valid file names.
	public static File parseFile(String ... path)
	{
		int len = path.length;
		
		if(len == 0)
		{
			throw new Error("Null path names are not accepted");
		}
		
		File output = new File(path[0]);
		
		for(int i = 1; i < len; i++)
		{
			output = new File(output, path[i]);
		}
		
		return output;
	}
	
	// REQUIRES : A list of the ordered path of directories with either a directory or a file at the end.
	// ENSURES  : Returns the proper file that is pathed from the default project or application directory.
	// NOTE : The user should use this method to find valid file names.
	public static File parseFile(File file_dir, String ... path)
	{
		File output = file_dir;
		
		int len = path.length;
		
		if(len == 0)
		{
			throw new Error("Null path names are not accepted");
		}
		
		for(int i = 0; i < len; i++)
		{
			output = new File(output, path[i]);
		}
		
		return output;
	}

	// REQUIRES : The given file should not be open.
	// ENSURES : This method should be used to create a new file for saving information.
	// opens the file if it already exists.
	public static void createFile(File file)
	{
		if(files.lookup(file) != null)
		{
			throw new Error("Files should never be created when they are already open.");
		}
		
		File parent = file.getParentFile();
		
		// Create the requisite parent directories, it they do not exist.
		if(parent != null && !parent.exists())
		{
			// We will assume that any user of this library will be trying to open non directory files,
			// so we will only create the list of directories that will be needed to find a file at the given location.
			file.getParentFile().mkdirs();
		}
		
		openFile(file);
	}
	
	// Opens the given file if is exists.
	// This method should be used to open already existing files, probably with the intent to read in information.
	public static void openFile(File file)
	{
		// Never allow the user to get away with an attempt to reopen an already open file.
		if(files.lookup(file) != null)
		{
			throw new Error("Files should never be opened multiple times before they are closed!");
		}
		
		File parent = file.getParentFile();
		
		// Do nothing if the given file does not exist.
		if(parent != null && !parent.exists())
		{
			throw new Error("The file " + file + " does not exist!\n" +
							"Try using FileIO.createFile() instead.");
		}
		
		try
		{
			files.insert(file, new PrintStream(file));
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		
	}
	
	// Returns true if and only if it successfully closed the desired file.
	public static boolean closeFile(File file)
	{
		return files.remove_key(file);
	}
	
	public void restoreDefaultPrintStream()
	{
		// The location of the default standard output.
		System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
	}
	
	// Allow the user to print to the current open file.
	public static void print(File file, Object o)
	{
		PrintStream stream = files.lookup(file);
		
		if(stream == null)
		{
			throw new Error("File not open");
		}
		
		stream.println(o);
	}
	
	// Allows the user to print multiple lines to a file.
	public static void print(File file, Object ... objs)
	{
		PrintStream stream = files.lookup(file);
		
		if(stream == null)
		{
			throw new Error("File not open");
		}
		
		for(Object o : objs)
		stream.println(o);
	}
	
	// Returns whether or not the given file is open.
	public static boolean isFileOpen(File file)
	{
		return files.lookup(file) != null;
	}
	
	// Returns the proper print stream.
	public static PrintStream getStream(File file)
	{
		PrintStream stream = files.lookup(file);
		
		if(stream == null)
		{
			throw new Error("File not open");
		}
		
		return stream;
	}
	
	//http://stackoverflow.com/questions/4716503/best-way-to-read-a-text-file
	
	public static List<String> readFile(File file)
	{
		List<String> output = new List<String>();
		
	    BufferedReader br = null;
		try
		{
			br = new BufferedReader(new FileReader(file));
		}
		catch (FileNotFoundException e)
		{
			// FIXME : Consider throwing an exception that can be caught.
			e.printStackTrace();
		}
		
		try
		{
			String line = br.readLine();

			while (line != null)
			{
				output.add(line);
				line = br.readLine();
			}
			br.close();
		}
	    catch (IOException e){e.printStackTrace();}

		return output;	
	}

	// Returns the presumed extension tacked onto a file with a dot separator;
	public static String getExtension(File file)
	{
		String path = file.getAbsolutePath();

		int dot_index = getDotIndex(path);
		
		if(dot_index == -1)
		{
			return null;
		}
		
		String extension = path.substring(dot_index);

		return extension;
	}
	
	// Returns the child's name, without its extension.
	public static String getChildName(File file)
	{
		String path = file.getName();

		int dot_index = getDotIndex(path);
		
		// Return everything, if no extension exists.
		if(dot_index == -1)
		{
			return path;
		}
		
		String non_extension = path.substring(0, dot_index - 1);

		return non_extension;
	}

	// A wrapper function for determining a file's directory name.
	public static String getDirectoryName(File file)
	{
		return file.getPath();
	}
	
	// Returns the index of the last dot in the string. If no dots exist, it returns -1;
	private static int getDotIndex(String filename)
	{
		int len = filename.length();
		int len_ext = 0;

		// Scan for the length of the extension.
		while(filename.charAt(len - len_ext - 1) != '.')
		{
			len_ext++;
			
			if(len_ext == len)
			{
				return -1;
			}
		}
		
		return len - len_ext;
	}
}