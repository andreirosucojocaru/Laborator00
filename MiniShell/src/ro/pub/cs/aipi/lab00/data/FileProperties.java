package ro.pub.cs.aipi.lab00.data;

public final class FileProperties {

	final private String permissions;
	final private String owner;
	final private long size;
	final private FileTime lastModified;
	final private String name;
	
	private void check(String permissions,
			String owner,
			long size,
			FileTime lastModified,
			String name) {
		if (permissions == null || permissions.isEmpty() ||
				owner == null || owner.isEmpty() ||
				size < 0 ||
				name == null || name.isEmpty())
			throw new IllegalArgumentException();
	}
	
	public FileProperties(String permissions,
			String owner,
			long size,
			FileTime lastModified,
			String name) {
		check(permissions, owner, size, lastModified, name);
		this.permissions = permissions;
		this.owner = owner;
		this.size = size;
		this.lastModified = lastModified;
		this.name = name;
	}
	
	public String getPermissions() {
		return permissions;
	}
	
	public String getOwner() {
		return owner;
	}
	
	public long getSize() {
		return size;
	}
	
	public FileTime getLastModified() {
		return lastModified;
	}
	
	public String getName() {
		return name;
	}

}
