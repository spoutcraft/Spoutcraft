package net.minecraft.src;

public interface IProgressUpdate {
	public abstract void displaySavingString(String s);

	public abstract void displayLoadingString(String s);

	public abstract void setLoadingProgress(int i);
}
