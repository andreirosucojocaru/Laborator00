package ro.pub.cs.aipi.lab00.applicationlogic;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

import ro.pub.cs.aipi.lab00.general.Constants;

public class FileSystemOperations {
	
	public Path checkIfExists(String entity, Path currentDirectory) {
		// relative path
		try {
			return Paths.get(currentDirectory.toString()+"/"+entity).toRealPath();
		} catch (IOException | InvalidPathException exception) {
			if (Constants.DEBUG)
				System.out.println("Operation could not be performed! "+exception.getMessage());
		}		
		// absolute path
		try {
			return Paths.get(entity).toRealPath();
		} catch (IOException | InvalidPathException exception) {
			if (Constants.DEBUG)
				System.out.println("Operation could not be performed! "+exception.getMessage());
		}	
		return null;
	}
	
	public Path changeDirectory(String newDirectory, Path oldDirectory) {
		Path path = checkIfExists(newDirectory, oldDirectory);
		if (path != null)
			return path;
		return oldDirectory;
	}
	
	public void makeDirectory(String name, Path currentDirectory) {
		Path path = null;
		boolean succeeded = false;
		try {
			path = Paths.get(name);
			if (path.isAbsolute()) {
				Files.createDirectories(path);
				succeeded = true;			
			}
		} catch (IOException ioException) {
			if (Constants.DEBUG)
				System.out.format("Creation of directory %s failed: %s!%n",(path!=null)?path.toString():name,ioException.getMessage());
		}
		if (!succeeded) {
			try {
				path = Paths.get(currentDirectory+"/"+name);
				if (path.isAbsolute()) {
					Files.createDirectories(path);
					succeeded = true;					
				}
			} catch (IOException ioException) {
				if (Constants.DEBUG)
					System.out.format("Creation of directory %s failed: %s!%n",(path!=null)?path.toString():name,ioException.getMessage());
			}			
		}
	}
}
