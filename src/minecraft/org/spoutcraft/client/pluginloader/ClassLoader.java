package org.spoutcraft.client.pluginloader;

import java.net.URL;
import java.net.URLClassLoader;

public class ClassLoader extends URLClassLoader{

	public ClassLoader(final URL[] urls, final ClassLoader parent) {
		super(urls, parent);
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		if (!whitelisted(name)) {
			throw new SecurityException("Plugin attempted to access non-whitelisted class: " + name);
		}
		return super.findClass(name);
	}

	private boolean whitelisted(String classname) {
		return true;
	}
	
}
