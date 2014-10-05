Minishell
=========

Minishell is a platform-independent shell writen in Java (exploring the New I/O API), supporting commands (though without any options) to manipulate the content of the file system (files and directories).


In the case of overwriting existing files or deleting non-empty directories, the user will be prompted whether he/she is sure to do so.

There is no support at the moment for symbolic links.

* v4.0.2
Added support for:
 - cat <file>: displays the content of the file

* v4.0.1
Added support for:
 - pwd: displays the absolute path of the current directory

* v3.0.0
Added support for:
 - ls <directory> / dir <directory>: displays the content of the directory, indicating for each entry the type and permissions, the size, the creation date and the name
Note: the output of the command also includes general statistics like total size of the content, number of files and (sub)directories

* v2.1.0
Added support for:
 - rm <file> / del <file>: deletes the file from the disk
 - rmdir <directory> / delete <directory>: deletes the directory from the disk; if the directory is not empty, the user is prompted wheter to continue the deletion of its content recursively

* v2.0.0
Added support for:
 - cp <source> <target> / copy <source> <target>: copies the source into target, whether the source / the target are files or directories; the only operation that cannot be performed is copying the content of a directory into a file
 - mv <source> <target> / move <source> <target>: moves the source into target, whether the source / the target are files or directories
Note: 
1) in the case of directories, the content is copied / moved recursively
2) if the files in the source are also to be found in the target, the user is prompted whether to override them or not

* v1.1.0
Added support for:
 - touch <new_file> / touch <existing_file>: creates a new file with the specified content; appends the specified content to an existing file; the content is to be added line by line until /quit is entered

* v1.0.0
The commands supported so far are:
 - cd <new_directory>: changes the current directory to the new_directory, which may denote a relative or an absolute path
 - mkdir <new_directory> / md <new_directory>: creates a new directory within the current directory, if the parameter is a relative path, or at the exact location specified by an absolute path
 - quit / exit: terminates the program