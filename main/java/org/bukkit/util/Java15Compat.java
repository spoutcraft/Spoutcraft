package org.bukkit.util;

import java.lang.reflect.Array;

public class Java15Compat {
	@SuppressWarnings("unchecked")
	public static <T> T[] Arrays_copyOfRange(T[] original, int start, int end) {
		if ((original.length >= start) && (0 <= start)) {
			if (start <= end) {
				int length = end - start;
				int copyLength = Math.min(length, original.length - start);
				Object[] copy = (Object[]) (Object[]) Array.newInstance(original.getClass().getComponentType(), length);

				System.arraycopy(original, start, copy, 0, copyLength);
				return (T[]) copy;
			}
			throw new IllegalArgumentException();
		}
		throw new ArrayIndexOutOfBoundsException();
	}
}