package org.getspout.spout;

public class SpoutVersion {
	private final int major;
	private final int minor;
	private final int build;
	private final int subbuild;
	
	public SpoutVersion() {
		major = 0;
		minor = 0;
		build = -1;
		subbuild = 0;
	}
	
	public SpoutVersion(int major, int minor, int build, int subbuild) {
		this.major = major;
		this.minor = minor;
		this.build = build;
		this.subbuild = subbuild;
	}
	
	public long getVersion() {
		return major * 100 + minor * 10 + build;
	}
	
	public String toString() {
		return (new StringBuilder("")).append(major).append(".").append(minor).append(".").append(build).append(".").append(subbuild).toString();
	}

}
