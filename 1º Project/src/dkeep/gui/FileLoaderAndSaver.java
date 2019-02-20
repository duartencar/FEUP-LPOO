package dkeep.gui;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.swing.JFileChooser;

import dkeep.logic.Level;

public class FileLoaderAndSaver
{
	private JFileChooser chooseDestination;
	private Component parent;
	
	public FileLoaderAndSaver(Component window)
	{
		parent = window;
	}
	
	public FileOutputStream getFileForSaving()
	{
		chooseDestination = new JFileChooser();
		
		FileOutputStream  fileToSave = null;

		chooseDestination.setDialogTitle("Select directory to save the created level");
		
		if(chooseDestination.showSaveDialog(parent) != JFileChooser.APPROVE_OPTION)
			return null;
		
		try
		{
			fileToSave = new FileOutputStream(chooseDestination.getSelectedFile());
		} 
		catch (FileNotFoundException e1)
		{
			System.out.println("Failed to get file!");
			e1.printStackTrace();
			return null;
		}
		
		return fileToSave;
	}
	
	public FileInputStream getFileForLoading()
	{
		FileInputStream fileToLoad = null;
		
		chooseDestination = new JFileChooser();
		
		chooseDestination.setDialogTitle("Select a file to load a level");
		
		if(chooseDestination.showSaveDialog(parent) != JFileChooser.APPROVE_OPTION)
			return null;
		
		try
		{
			fileToLoad = new FileInputStream(chooseDestination.getSelectedFile());
		} 
		catch (FileNotFoundException e1)
		{
			System.out.println("Failed to get file!");
			e1.printStackTrace();
			return null;
		}
		
		return fileToLoad;
	}
	
	public String[] getMapFromFile(FileInputStream f) throws IOException
	{		
		ArrayList<String> readLines = new ArrayList<String>();
		
		String mapLine;
		
		BufferedReader buffer = new BufferedReader(new InputStreamReader(f));
		
		while((mapLine = buffer.readLine()) != null)
		{
			readLines.add(mapLine);
		}
		
		String[] map = new String[readLines.size()];
		
		for(int i = 0; i < readLines.size(); i++)
			map[i] = readLines.get(i);
		
		return map;
	}

	public boolean saveToFile(Level userLevel ,FileOutputStream f) throws IOException
	{
		if(userLevel == null || f == null)
			throw new NullPointerException("One of the elements is NULL");
		
		byte[] strToBytes = userLevel.toString().getBytes();
		
		f.write(strToBytes);
		
		f.close();
		
		return true;
	}
}
