package net.minecraft.src;

public interface ILogAgent {
	void logInfo(String var1);

	void logWarning(String var1);

	void logWarningFormatted(String var1, Object ... var2);

	void logWarningException(String var1, Throwable var2);

	void logSevere(String var1);

	void logSevereException(String var1, Throwable var2);

	void logFine(String var1);
}
