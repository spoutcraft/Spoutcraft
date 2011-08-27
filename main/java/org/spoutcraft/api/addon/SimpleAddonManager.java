package org.spoutcraft.api.addon;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.spoutcraft.api.Spoutcraft;
import org.spoutcraft.api.command.AddonCommandYamlParser;
import org.spoutcraft.api.command.Command;
import org.spoutcraft.api.command.SimpleCommandMap;
import org.spoutcraft.api.event.Event;
import org.spoutcraft.api.event.Event.Priority;
import org.spoutcraft.api.event.Event.Type;
import org.spoutcraft.api.event.Listener;

public class SimpleAddonManager implements AddonManager {

	private Spoutcraft spoutcraft;
	private final List<Addon> addons = new ArrayList<Addon>();
	private final Map<String, Addon> lookupNames = new HashMap<String, Addon>();
	private final SimpleCommandMap commandMap;

	public SimpleAddonManager(Spoutcraft instance, SimpleCommandMap commandMap) {
		spoutcraft = instance;
        this.commandMap = commandMap;
	}

	public synchronized Addon getAddon(String paramString) {
		return (Addon) this.lookupNames.get(paramString);
	}

	public synchronized Addon[] getAddons() {
		return (Addon[]) this.addons.toArray(new Addon[0]);
	}

	public boolean isAddonEnabled(String paramString) {
		Addon addon = getAddon(paramString);
		return isAddonEnabled(addon);
	}

	public boolean isAddonEnabled(Addon paramAddon) {
		if ((paramAddon != null) && (this.addons.contains(paramAddon))) {
			return paramAddon.isEnabled();
		}
		return false;
	}

	public synchronized Addon loadAddon(File paramFile) throws InvalidAddonException, InvalidDescriptionException, UnknownDependencyException {
		return loadAddon(paramFile, true);
	}

	private synchronized Addon loadAddon(File file, boolean ignoreSoftDepenencies) throws InvalidAddonException, InvalidDescriptionException, UnknownDependencyException {
		Addon result = null;

		if (file.exists() && file.getPath().toLowerCase().endsWith(".jar")) {
			AddonLoader loader = new SimpleAddonLoader();
			result = loader.loadAddon(file, ignoreSoftDepenencies);
		}

		if (result != null) {
			this.addons.add(result);
			this.lookupNames.put(result.getDescription().getName(), result);
		}

		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Addon[] loadAddons(File paramFile) {
		List result = new ArrayList();
		File[] files = paramFile.listFiles();

		boolean allFailed = false;
		boolean finalPass = false;

		LinkedList filesList = new LinkedList(Arrays.asList(files));

		while ((!allFailed) || (finalPass)) {
			allFailed = true;
			Iterator itr = filesList.iterator();

			while (itr.hasNext()) {
				File file = (File) itr.next();
				Addon addon = null;
				try {
					addon = loadAddon(file, finalPass);
					itr.remove();
				} catch (UnknownDependencyException ex) {
					if (finalPass) {
						this.spoutcraft.getLogger().log(Level.SEVERE, "Could not load '" + file.getPath() + "' in folder '" + paramFile.getPath() + "': " + ex.getMessage(), ex);
						itr.remove();
					} else {
						addon = null;
					}
				} catch (InvalidAddonException ex) {
					this.spoutcraft.getLogger().log(Level.SEVERE, "Could not load '" + file.getPath() + "' in folder '" + paramFile.getPath() + "': ", ex.getCause());
					itr.remove();
				} catch (InvalidDescriptionException ex) {
					this.spoutcraft.getLogger().log(Level.SEVERE, "Could not load '" + file.getPath() + "' in folder '" + paramFile.getPath() + "': " + ex.getMessage(), ex);
					itr.remove();
				}

				if (addon != null) {
					result.add(addon);
					allFailed = false;
					finalPass = false;
				}
			}
			if (finalPass)
				break;
			if (allFailed) {
				finalPass = true;
			}
		}

		return (Addon[]) result.toArray(new Addon[result.size()]);
	}

	public void disableAddons() {
		for (Addon addon : getAddons()) {
			disableAddon(addon);
		}

	}

	public void clearAddons() {
		synchronized (this) {
			disableAddons();
			this.addons.clear();
			this.lookupNames.clear();
		}

	}

	public synchronized void callEvent(Event paramEvent) {
		// TODO Auto-generated method stub

	}

	public void registerEvent(Type paramType, Listener paramListener, Priority paramPriority, Addon paramAddon) {
		// TODO Auto-generated method stub

	}

	public void registerEvent(Type paramType, Listener paramListener, EventExecutor paramEventExecutor, Priority paramPriority, Addon paramAddon) {
		// TODO Auto-generated method stub
	}

	public void enableAddon(Addon addon) {
		if (!addon.isEnabled()) {
            List<Command> addonCommands = AddonCommandYamlParser.parse(addon);

            if (!addonCommands.isEmpty()) {
                commandMap.registerAll(addon.getDescription().getName(), addonCommands);
            }
            
            try {
                addon.getAddonLoader().enableAddon(addon);
            } catch (Throwable ex) {
                spoutcraft.getLogger().log(Level.SEVERE, "Error occurred (in the addon loader) while enabling " + addon.getDescription().getFullName() + " (Is it up to date?): " + ex.getMessage(), ex);
            }
        }


	}

	public void disableAddon(Addon addon) {
		 if (addon.isEnabled()) {
	            try {
	                addon.getAddonLoader().disableAddon(addon);
	            } catch (Throwable ex) {
	                spoutcraft.getLogger().log(Level.SEVERE, "Error occurred (in the addon loader) while disabling " + addon.getDescription().getFullName() + " (Is it up to date?): " + ex.getMessage(), ex);
	            }
	        }
	}

}
