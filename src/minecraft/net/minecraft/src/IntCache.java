package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;

public class IntCache {
	private static int intCacheSize = 256;
	private static List freeSmallArrays = new ArrayList();
	private static List inUseSmallArrays = new ArrayList();
	private static List freeLargeArrays = new ArrayList();
	private static List inUseLargeArrays = new ArrayList();

	public IntCache() {
	}

	public static int[] getIntCache(int i) {
		if (i <= 256) {
			if (freeSmallArrays.size() == 0) {
				int ai[] = new int[256];
				inUseSmallArrays.add(ai);
				return ai;
			}
			else {
				int ai1[] = (int[])freeSmallArrays.remove(freeSmallArrays.size() - 1);
				inUseSmallArrays.add(ai1);
				return ai1;
			}
		}
		if (i > intCacheSize) {
			intCacheSize = i;
			freeLargeArrays.clear();
			inUseLargeArrays.clear();
			int ai2[] = new int[intCacheSize];
			inUseLargeArrays.add(ai2);
			return ai2;
		}
		if (freeLargeArrays.size() == 0) {
			int ai3[] = new int[intCacheSize];
			inUseLargeArrays.add(ai3);
			return ai3;
		}
		else {
			int ai4[] = (int[])freeLargeArrays.remove(freeLargeArrays.size() - 1);
			inUseLargeArrays.add(ai4);
			return ai4;
		}
	}

	public static void resetIntCache() {
		if (freeLargeArrays.size() > 0) {
			freeLargeArrays.remove(freeLargeArrays.size() - 1);
		}
		if (freeSmallArrays.size() > 0) {
			freeSmallArrays.remove(freeSmallArrays.size() - 1);
		}
		freeLargeArrays.addAll(inUseLargeArrays);
		freeSmallArrays.addAll(inUseSmallArrays);
		inUseLargeArrays.clear();
		inUseSmallArrays.clear();
	}
}
