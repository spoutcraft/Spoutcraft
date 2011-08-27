package org.spoutcraft.api.addon.java;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.spoutcraft.api.addon.RestrictedClassException;

public class AddonClassLoader extends URLClassLoader {
	private final JavaAddonLoader loader;
	private final Map<String, Class<?>> classes = new HashMap<String, Class<?>>();

	public AddonClassLoader(final JavaAddonLoader loader, final URL[] urls, final ClassLoader parent) {
		super(urls, parent);

		this.loader = loader;
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		if (name.startsWith("net.minecraft")) {
			throw new RestrictedClassException("Accessing net.minecraft is not allowed");
		}
		return findClass(name, true);
	}

	protected Class<?> findClass(String name, boolean checkGlobal) throws ClassNotFoundException {
		Class<?> result = classes.get(name);

		if (result == null) {
			if (checkGlobal) {
				result = loader.getClassByName(name);
			}

			if (result == null) {
				result = super.findClass(name);

				if (result != null) {
					loader.setClass(name, result);
				}
			}

			classes.put(name, result);
		}

		return result;
	}

	public Set<String> getClasses() {
		return classes.keySet();
	}
}
