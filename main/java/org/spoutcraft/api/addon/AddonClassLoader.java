package org.spoutcraft.api.addon;

import java.net.URL;
import java.net.URLClassLoader;

public class AddonClassLoader extends URLClassLoader{

	public AddonClassLoader(URL[] urls) {
		super(urls);
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		if (name.startsWith("net.minecraft")) {
			throw new RestrictedClassException("Accessing net.minecraft is not allowed");
		}
		return super.findClass(name);
	}

}
