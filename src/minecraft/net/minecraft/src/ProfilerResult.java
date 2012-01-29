package net.minecraft.src;

public final class ProfilerResult
	implements Comparable {
	public double sectionPercentage;
	public double globalPercentage;
	public String name;

	public ProfilerResult(String s, double d, double d1) {
		name = s;
		sectionPercentage = d;
		globalPercentage = d1;
	}

	public int compareProfilerResult(ProfilerResult profilerresult) {
		if (profilerresult.sectionPercentage < sectionPercentage) {
			return -1;
		}
		if (profilerresult.sectionPercentage > sectionPercentage) {
			return 1;
		}
		else {
			return profilerresult.name.compareTo(name);
		}
	}

	public int getDisplayColor() {
		return (name.hashCode() & 0xaaaaaa) + 0x444444;
	}

	public int compareTo(Object obj) {
		return compareProfilerResult((ProfilerResult)obj);
	}
}
