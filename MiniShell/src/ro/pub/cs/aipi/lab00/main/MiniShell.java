package ro.pub.cs.aipi.lab00.main;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import ro.pub.cs.aipi.lab00.applicationlogic.CommandParser;
import ro.pub.cs.aipi.lab00.general.Constants;

public class MiniShell {
	
	private static void displayPrompt() {
		Path path;
		String command;
		Scanner scanner = new Scanner(System.in);
		CommandParser commandParser = new CommandParser();
		try {
			path = Paths.get(".").toRealPath();
			do {
		        
		        System.out.print(Constants.PROMPT1+path.toString()+Constants.PROMPT2);
		        command = scanner.nextLine();
		        path = commandParser.analyze(command, path, scanner);
			} while (!command.equals(Constants.QUIT_COMMAND) && !command.equals(Constants.EXIT_COMMAND));			
		} catch (IOException ioException) {
			System.out.println("An exception has occurred "+ioException.getMessage());
		}
		scanner.close();
	}
	
	public static void main(String[] args) {
		displayPrompt();
	}

}
