package net.minecraft.src;

import java.util.List;

public interface ISaveFormat {
	public abstract String getFormatName();

	public abstract ISaveHandler getSaveLoader(String s, boolean flag);

	public abstract List getSaveList();

	public abstract void flushCache();

	public abstract WorldInfo getWorldInfo(String s);

	public abstract void deleteWorldDirectory(String s);

	public abstract void renameWorld(String s, String s1);

	public abstract boolean isOldMapFormat(String s);

	public abstract boolean convertMapFormat(String s, IProgressUpdate iprogressupdate);
}
