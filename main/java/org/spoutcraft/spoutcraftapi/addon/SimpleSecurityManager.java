/*
 * This file is part of SpoutcraftAPI (http://wiki.getspout.org/).
 * 
 * SpoutcraftAPI is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SpoutcraftAPI is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.spoutcraft.spoutcraftapi.addon;

import org.spoutcraft.spoutcraftapi.Spoutcraft;

import java.io.FileDescriptor;
import java.lang.reflect.Member;
import java.net.InetAddress;
import java.security.Permission;

public final class SimpleSecurityManager extends SecurityManager {
	private final double key;
	private boolean locked = false;

	public SimpleSecurityManager(double key) {
		if (System.getSecurityManager() instanceof SimpleSecurityManager) {
			throw new SecurityException("Warning, Duplicate SimpleSecurityManager created!");
		}
		this.key = key;
	}

	public void lock(double key) {
		if (key == this.key) {
			locked = true;
		} else {
			throw new SecurityException("Incorrect key!");
		}
	}

	public void unlock(double key) {
		if (key == this.key) {
			locked = false;
		} else {
			throw new SecurityException("Incorrect key!");
		}
	}

	public boolean isLocked() {
		return locked && Thread.currentThread().getName().equals("Minecraft main thread");
	}

	private void checkAccess() {
		if (isLocked()) {
			//throw new SecurityException("Access is restricted!");
			Thread.dumpStack();
		}
	}

	public void checkAccept(String host, int port) {
		checkAccess();
	}

	@Override
	public void checkAccess(Thread t) {
		checkAccess();
	}

	@Override
	public void checkAccess(ThreadGroup g) {
		checkAccess();
	}

	@Override
	public void checkAwtEventQueueAccess() {
		//checkAccess();
	}

	@Override
	public void checkConnect(String host, int port) {
		checkAccess();
	}

	@Override
	public void checkConnect(String host, int port, Object context) {
		checkAccess();
	}

	@Override
	public void checkCreateClassLoader() {
		//checkAccess(); // TODO : Commented out so that Addons can load
	}

	@Override
	public void checkDelete(String file) {
	 	if (isLocked()) {
			if (!hasFileAccess(file)) {
				throw new SecurityException("Access is restricted! Addon tried to delete " + file);
			}
		}
	}

	@Override
	public void checkExec(String cmd) {
		checkAccess();
	}

	@Override
	public void checkExit(int status) {
		checkAccess();
	}

	@Override
	public void checkLink(String lib) {
		checkAccess();
	}

	@Override
	public void checkListen(int port) {
		checkAccess();
	}

	@Override
	public void checkMemberAccess(Class<?> clazz, int which) {
		if (clazz == null) {
			throw new NullPointerException("class can't be null");
		}
		if (which != Member.PUBLIC && isLocked()) {
			Class<?> stack[] = getClassContext();
			/*
			* stack depth of 4 should be the caller of one of the
			* methods in java.lang.Class that invoke checkMember
			* access. The stack should look like:
			*
			* someCaller						[3]
			* java.lang.Class.someReflectionAPI [2]
			* java.lang.Class.checkMemberAccess [1]
			* SecurityManager.checkMemberAccess [0]
			*
			*/
			if ((stack.length<4) || (stack[3].getClassLoader() != clazz.getClassLoader())) {
				checkAccess();
			}
		}
	}

	@Override
	public void checkMulticast(InetAddress maddr) {
		checkAccess();
	}

	@Override
	public void checkPackageAccess(String pckg) {
		//TODO doesn't the classloader handle this already?
	}

	@Override
	public void checkPackageDefinition(String pckg) {
		//TODO doesn't the classloader handle this already?
	}

	@Override
	public void checkPermission(Permission perm) {
		checkAccess(); //TODO handle on case by case basis
	}

	@Override
	public void checkPermission(Permission per, Object context) {
		checkAccess(); //TODO handle on case by case basis
	}

	@Override
	public void checkPrintJobAccess() {
		checkAccess();
	}

	@Override
	public void checkPropertiesAccess() {
		checkAccess();
	}

	@Override
	public void checkPropertyAccess(String property) {
		checkAccess();
	}

	@Override
	public void checkRead(String file) {
		if (isLocked()) {
			if (file.endsWith(".class") || file.endsWith(".jar")) {
				return; //class loader will have already decided it's safe if we got here
			}
			if (!hasFileAccess(file)) {
				System.out.println("Reading from " + file);
				throw new SecurityException("Access is restricted! Addon tried to read " + file);
			}
		}
	}

	@Override
	public void checkRead(String file, Object context) {
		checkRead(file);
	}

	@Override
	public void checkSecurityAccess(String target) {
		checkAccess();
	}

	@Override
	public void checkSetFactory() {
		checkAccess();
	}

	@Override
	public void checkSystemClipboardAccess() {
		checkAccess(); //TODO check launcher options?
	}

	@Override
	public boolean checkTopLevelWindow(Object window) {
		return !locked;
	}

	@Override
	public void checkWrite(FileDescriptor fd) {
		checkAccess();
	}

	@Override
	public void checkWrite(String file) {
		if (isLocked()) {
			if (!hasFileAccess(file)) {
				System.out.println("Writing to " + file);
				throw new SecurityException("Access is restricted! Addon tried to write to " + file);
			}
		}
	}
	
	public static boolean hasFileAccess(String file) {
		if (file.startsWith(Spoutcraft.getAddonFolder().getPath())) {
			return true; //allow access
		}
		if (file.startsWith(Spoutcraft.getAudioCache().getPath())) {
			return true; //allow access
		}
		if (file.startsWith(Spoutcraft.getTemporaryCache().getPath())) {
			return true; //allow access
		}
		if (file.startsWith(Spoutcraft.getTextureCache().getPath())) {
			return true; //allow access
		}
		if (file.startsWith(Spoutcraft.getTexturePackFolder().getPath())) {
			return true; //allow access
		}
		if (file.startsWith(Spoutcraft.getStatsFolder().getPath())) {
			return true; //allow access
		}
		return false;
	}
}
