package ro.pub.cs.aipi.lab00.general;

public interface Constants {

	final public static boolean DEBUG							= false;

	final public static String PROMPT1							= "minishell:";
	final public static String PROMPT2							= "> ";

	final public static String CHANGE_DIRECTORY					= "cd";	
	final public static String MAKE_DIRECTORY1					= "mkdir";
	final public static String MAKE_DIRECTORY2					= "md";
	final public static String TOUCH							= "touch";
	final public static String COPY1							= "cp";
	final public static String COPY2							= "copy";	
	final public static String MOVE1							= "mv";
	final public static String MOVE2							= "move";

	final public static String EXIT_COMMAND						= "exit";
	final public static String QUIT_COMMAND						= "quit";

	final public static String ACCEPT							= "yes";
	final public static String ACCEPT_SHORT						= "y";

	final public static int OPERATION_COPY						= 1;
	final public static int OPERATION_MOVE						= 2;
}
