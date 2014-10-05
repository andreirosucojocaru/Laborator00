package ro.pub.cs.aipi.lab00.applicationlogic;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.AccessDeniedException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.DosFileAttributes;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermissions;
import java.nio.file.attribute.UserPrincipal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Scanner;

import ro.pub.cs.aipi.lab00.data.FileProperties;
import ro.pub.cs.aipi.lab00.data.FileTime;
import ro.pub.cs.aipi.lab00.general.Constants;
import ro.pub.cs.aipi.lab00.general.Utilities;

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

	public void printWorkingDirectory(Path currentDirectory) {
		System.out.println(currentDirectory.toString());
	}
	
	public Path changeDirectory(String newDirectory, Path oldDirectory) {
		Path path = checkIfExists(newDirectory, oldDirectory);
		if (path != null)
			return path;
		return oldDirectory;
	}
	
	public void listDirectory(Path currentDirectory) {
		int numberOfFiles = 0, numberOfDirectories = 0;
		String directory;
		long size, totalFileSize = 0, maxFileSize = 0;
		Instant lastModified;
		UserPrincipal owner;
		int maxOwnerLength = 0;
		BasicFileAttributes attributes = null;
		String filePermissions = null;
		ArrayList<FileProperties> result = new ArrayList<>();
		try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(currentDirectory)) {
			for (Path entry: directoryStream) {
				try {
					size = Files.size(entry);
					if (Files.isDirectory(entry)) {
						directory = "d";
						numberOfDirectories++;
						size = 0;
					}
					else {
						directory = "-";
						numberOfFiles++;
						totalFileSize += size;
						if (size > maxFileSize)
							maxFileSize = size;
					}
					lastModified = Files.getLastModifiedTime(entry).toInstant();
					owner = Files.getOwner(entry);
					if (owner.getName().toString().length() > maxOwnerLength)
						maxOwnerLength = owner.getName().toString().length();
					// UNIX-based OS
					try {
						attributes = Files.readAttributes(entry,PosixFileAttributes.class);
						filePermissions = PosixFilePermissions.toString(((PosixFileAttributes)attributes).permissions());
					} catch(UnsupportedOperationException exception) {
						if (Constants.DEBUG)
							System.out.println("Operations could not be performed! "+exception.getMessage());
					}
					// DOS-based OS
					try {
						attributes = Files.readAttributes(entry,DosFileAttributes.class);
						filePermissions = (((DosFileAttributes)attributes).isReadOnly()?"r":"-")+
								(((DosFileAttributes)attributes).isHidden()?"h":"-")+
								(((DosFileAttributes)attributes).isArchive()?"a":"-")+
								(((DosFileAttributes)attributes).isSystem()?"s":"-");
					} catch(UnsupportedOperationException exception) {
						if (Constants.DEBUG)
							System.out.println("Operations could not be performed! "+exception.getMessage());
					}
					String[] parts = lastModified.toString().split("[TZ:/.-]");
					result.add(new FileProperties(directory+filePermissions,
							owner.getName(),
							size,
							new FileTime(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), Integer.parseInt(parts[3]), Integer.parseInt(parts[4])),
							entry.getFileName().toString()));
				} catch (AccessDeniedException accessDeniedException) {
					if (Constants.DEBUG)
						System.out.format("Access denied on file %s!%n", entry.toString());
				}
			}
		} catch (IOException | DirectoryIteratorException exception) {
			if (Constants.DEBUG)
				System.out.println("Operation could not be performed! "+exception.getMessage());
		}
		for(FileProperties content: result)
			System.out.format("%-10s %-"+maxOwnerLength+"s %"+Utilities.numberOfDigits(maxFileSize)+"s %-16s %s %n", 
					content.getPermissions(),
					content.getOwner(),
					Utilities.format(content.getSize()),
					content.getLastModified(),
					content.getName());
		System.out.format("%n%,d bytes in %d directories and %d files%n",totalFileSize,numberOfDirectories,numberOfFiles);
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
	
	public void touch(String name, Path currentDirectory, Scanner scanner) {
		Path path = null;
		boolean succeeded = false;
		path = Paths.get(name);
		if (path.isAbsolute())
			succeeded = true;
		if (!succeeded) {
			path = Paths.get(currentDirectory+"/"+name);
			if (path.isAbsolute())
				succeeded = true;			
		}
		if (succeeded) {
			Charset charset = Charset.forName("UTF-8");
			try (BufferedWriter bufferedWriter = Files.newBufferedWriter(path, charset, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
				System.out.println("Enter the text to be written in the file, line by line, until /quit is encountered");
				String line = null;
				do {
					line = scanner.nextLine();
					if (!line.equals("/"+Constants.QUIT_COMMAND)) {
						line += "\n";
						bufferedWriter.write(line, 0, line.length());
					}
				} while (!line.equals("/"+Constants.QUIT_COMMAND));
			} catch (IOException ioException) {
				System.out.println("Operation could not be performed !"+ioException.getMessage());
			}			
		} else 
			System.out.format("File %s could not be created!", name);
	}
	
	private static boolean mayReplaceExisting(Path file, Scanner scanner) {
		System.out.format("File %s already exists! Overwrite? [yes/no] ",file.getFileName());
		String answer = scanner.nextLine();
		return answer.equals(Constants.ACCEPT) || answer.equals(Constants.ACCEPT_SHORT);
	}
	
	protected static void manipulate(Path source, Path target, Scanner scanner, int operation) {
		if (target == null || Files.notExists(target) || mayReplaceExisting(target, scanner))
			try {
				switch (operation) {
					case Constants.OPERATION_COPY:
						Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
						break;
					case Constants.OPERATION_MOVE:
						Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
						break;
					case Constants.OPERATION_DELETE:
						Files.delete(source);
						break;
				}
				
			} catch (IOException ioException) {
				System.out.format ("File %s has not been copied or moved: %s!",source.getFileName(),ioException.getMessage());
			}
	}
	
	static class DirectoryScanner implements FileVisitor<Path> {
		final private Path source;
		final private Path target;
		final private Scanner scanner;
		final private int operation;
		
		DirectoryScanner(Path source, Path target, Scanner scanner, int operation) {
			this.source = source;
			this.target = target;
			this.scanner = scanner;
			this.operation = operation;
		}
		
		@Override
		public FileVisitResult preVisitDirectory(Path directory, BasicFileAttributes attributes) {
			if (operation != Constants.OPERATION_DELETE) {
				Path newDirectory = target.resolve(source.relativize(directory));
				try {
					Files.copy(directory, newDirectory);
				} catch (IOException ioException) {
					if (!(ioException instanceof FileAlreadyExistsException))
						return FileVisitResult.SKIP_SUBTREE;
					System.out.format("Directory %s could not be created: %s!%n", directory.getFileName(), ioException.getMessage());
				}
			}
			return FileVisitResult.CONTINUE;
		}
		
		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) {
			manipulate(file, (operation != Constants.OPERATION_DELETE)?target.resolve(source.relativize(file)):null, scanner, operation);
			return FileVisitResult.CONTINUE;
		}
		
		@Override
		public FileVisitResult postVisitDirectory(Path directory, IOException ioException) {
			if (operation != Constants.OPERATION_COPY)
				try {
					Files.delete(directory);
				} catch (DirectoryNotEmptyException directoryNotEmptyException) {
					System.out.println("Directory is not empty!");
				} catch (IOException exception) {
					System.out.println("Operation could not be performed! "+exception.getMessage());				
				}			
			return FileVisitResult.CONTINUE;
		}
		
		@Override
		public FileVisitResult visitFileFailed(Path file, IOException ioException) {
			System.out.format("Copy of %s could not be performed: %s!%n",file.getFileName(),ioException.getMessage());
			return FileVisitResult.CONTINUE;
		}
	}
	
	public void manipulate(String source, String target, Path currentDirectory, Scanner scanner, int operation) {
		Path sourcePath = null, targetPath = null;
		sourcePath = checkIfExists(source, currentDirectory);
		if (operation != Constants.OPERATION_DELETE)
			targetPath = checkIfExists(target, currentDirectory);
		switch (operation) {
			case Constants.OPERATION_COPY:
			case Constants.OPERATION_MOVE:
				if (sourcePath != null && targetPath != null) {		
					if (!Files.isDirectory(sourcePath)) {
						// source is file
						if (Files.isDirectory(targetPath))
							targetPath = targetPath.resolve(sourcePath.getFileName());				
						manipulate(sourcePath, targetPath, scanner, operation);
					} else {
						// source is directory
						if (Files.isDirectory(targetPath)) {
							targetPath = targetPath.resolve(sourcePath.getFileName());
							DirectoryScanner directoryScanner = new DirectoryScanner(sourcePath, targetPath, scanner, operation);
							try {
								Files.walkFileTree(sourcePath, directoryScanner);
							} catch (IOException ioException) {
								System.out.format("Directory could not be walked: %s!%n",ioException.getMessage());
							}
						}					
						else
							System.out.println("Cannot copy a directory into a file!"+Files.isDirectory(targetPath)+" "+Files.isRegularFile(targetPath)+" "+targetPath.toString());
					}
				}
				else
					System.out.println("Operation could not be performed: source and/or target does not exist!");
				break;
			case Constants.OPERATION_DELETE:
				if (sourcePath != null)
					try {
						Files.delete(sourcePath);
					} catch (DirectoryNotEmptyException directoryNotEmptyException) {
						System.out.print("Directory is not empty! Do you still wish to delete it? [yes|no] ");
						String answer = scanner.nextLine();
						if (answer.equals(Constants.ACCEPT) || answer.equals(Constants.ACCEPT_SHORT)) {
							DirectoryScanner directoryScanner = new DirectoryScanner(sourcePath, null, scanner, Constants.OPERATION_DELETE);
							try {
								Files.walkFileTree(sourcePath, directoryScanner);
							} catch (IOException ioException) {
								System.out.format("Directory could not be scanned: %s!%n",ioException.getMessage());
							}
						}
					} catch (IOException ioException) {
						System.out.println("Operation could not be performed! "+ioException.getMessage());				
					}
				else
					System.out.println("File does not exist!");				
				break;
		}
	}
	
}
