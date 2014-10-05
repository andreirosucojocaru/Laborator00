package ro.pub.cs.aipi.lab00.applicationlogic;

import java.nio.file.Path;
import java.util.Scanner;

import ro.pub.cs.aipi.lab00.general.Constants;

public class CommandParser {
	
	public Path analyze(String command, Path currentDirectory, Scanner scanner) {
		String[] parts = command.split(" ");
		FileSystemOperations fileSystemOperations = new FileSystemOperations();
		switch (parts[0]) {
			case Constants.CHANGE_DIRECTORY:
				currentDirectory = fileSystemOperations.changeDirectory(parts[1], currentDirectory);
				break;			
			case Constants.MAKE_DIRECTORY1:
			case Constants.MAKE_DIRECTORY2:
				fileSystemOperations.makeDirectory(parts[1], currentDirectory);
				break;
			case Constants.TOUCH:
				fileSystemOperations.touch(parts[1], currentDirectory, scanner);
				break;
			case Constants.COPY1:
			case Constants.COPY2:
				fileSystemOperations.manipulate(parts[1], parts[2], currentDirectory, scanner, Constants.OPERATION_COPY);
				break;
			case Constants.MOVE1:
			case Constants.MOVE2:
				fileSystemOperations.manipulate(parts[1], parts[2], currentDirectory, scanner, Constants.OPERATION_MOVE);
				break;				
			case Constants.EXIT_COMMAND:
			case Constants.QUIT_COMMAND:
				System.out.println("Bye bye!");
				break;
			default:
				System.out.println("This command is not supported");
				break;
		}
		return currentDirectory;
	}

}
