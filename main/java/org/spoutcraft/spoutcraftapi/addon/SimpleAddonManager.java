package org.spoutcraft.spoutcraftapi.addon;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.util.FileUtil;
import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.command.AddonCommandYamlParser;
import org.spoutcraft.spoutcraftapi.command.Command;
import org.spoutcraft.spoutcraftapi.command.SimpleCommandMap;
import org.spoutcraft.spoutcraftapi.event.Event;
import org.spoutcraft.spoutcraftapi.event.Event.Priority;
import org.spoutcraft.spoutcraftapi.event.Listener;

public class SimpleAddonManager implements AddonManager {

	private Spoutcraft spoutcraft;
	private final Map<Pattern, AddonLoader> fileAssociations = new HashMap<Pattern, AddonLoader>();
    private final List<Addon> addons = new ArrayList<Addon>();
    private final Map<String, Addon> lookupNames = new HashMap<String, Addon>();
    private final Map<Event.Type, SortedSet<RegisteredListener>> listeners = new EnumMap<Event.Type, SortedSet<RegisteredListener>>(Event.Type.class);
    private static File updateDirectory = null;
    private final SimpleCommandMap commandMap;
    private final Comparator<RegisteredListener> comparer = new Comparator<RegisteredListener>() {
        public int compare(RegisteredListener i, RegisteredListener j) {
            int result = i.getPriority().compareTo(j.getPriority());

            if ((result == 0) && (i != j)) {
                result = 1;
            }

            return result;
        }
    };

    public SimpleAddonManager(Spoutcraft instance, SimpleCommandMap commandMap) {
        spoutcraft = instance;
        this.commandMap = commandMap;
    }

    /**
     * Registers the specified addon loader
     *
     * @param loader Class name of the AddonLoader to register
     * @throws IllegalArgumentException Thrown when the given Class is not a valid AddonLoader
     */
    public void registerInterface(Class<? extends AddonLoader> loader) throws IllegalArgumentException {
        AddonLoader instance;

        if (AddonLoader.class.isAssignableFrom(loader)) {
            Constructor<? extends AddonLoader> constructor;

            try {
                constructor = loader.getConstructor(Spoutcraft.class);
                instance = constructor.newInstance(spoutcraft);
            } catch (NoSuchMethodException ex) {
                String className = loader.getName();

                throw new IllegalArgumentException(String.format("Class %s does not have a public %s(Spoutcraft) constructor", className, className), ex);
            } catch (Exception ex) {
                throw new IllegalArgumentException(String.format("Unexpected exception %s while attempting to construct a new instance of %s", ex.getClass().getName(), loader.getName()), ex);
            }
        } else {
            throw new IllegalArgumentException(String.format("Class %s does not implement interface AddonLoader", loader.getName()));
        }

        Pattern[] patterns = instance.getAddonFileFilters();

        synchronized (this) {
            for (Pattern pattern : patterns) {
                fileAssociations.put(pattern, instance);
            }
        }
    }

    /**
     * Loads the addons contained within the specified directory
     *
     * @param directory Directory to check for addons
     * @return A list of all addons loaded
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public Addon[] loadAddons(File directory) {
        List<Addon> result = new ArrayList<Addon>();
        File[] files = directory.listFiles();

        boolean allFailed = false;
        boolean finalPass = false;

        LinkedList<File> filesList = new LinkedList(Arrays.asList(files));

        if (!(spoutcraft.getUpdateFolder().equals(""))) {
            updateDirectory = new File(directory, spoutcraft.getUpdateFolder());
        }

        while (!allFailed || finalPass) {
            allFailed = true;
            Iterator<File> itr = filesList.iterator();

            while (itr.hasNext()) {
                File file = itr.next();
                Addon addon = null;

                try {
                    addon = loadAddon(file, finalPass);
                    itr.remove();
                } catch (UnknownDependencyException ex) {
                    if (finalPass) {
                        spoutcraft.getLogger().log(Level.SEVERE, "Could not load '" + file.getPath() + "' in folder '" + directory.getPath() + "': " + ex.getMessage(), ex);
                        itr.remove();
                    } else {
                        addon = null;
                    }
                } catch (InvalidAddonException ex) {
                    spoutcraft.getLogger().log(Level.SEVERE, "Could not load '" + file.getPath() + "' in folder '" + directory.getPath() + "': ", ex.getCause());
                    itr.remove();
                } catch (InvalidDescriptionException ex) {
                    spoutcraft.getLogger().log(Level.SEVERE, "Could not load '" + file.getPath() + "' in folder '" + directory.getPath() + "': " + ex.getMessage(), ex);
                    itr.remove();
                }

                if (addon != null) {
                    result.add(addon);
                    allFailed = false;
                    finalPass = false;
                }
            }
            if (finalPass) {
                break;
            } else if (allFailed) {
                finalPass = true;
            }
        }

        return result.toArray(new Addon[result.size()]);
    }

    /**
     * Loads the addon in the specified file
     *
     * File must be valid according to the current enabled Addon interfaces
     *
     * @param file File containing the addon to load
     * @return The Addon loaded, or null if it was invalid
     * @throws InvalidAddonException Thrown when the specified file is not a valid addon
     * @throws InvalidDescriptionException Thrown when the specified file contains an invalid description
     */
    public synchronized Addon loadAddon(File file) throws InvalidAddonException, InvalidDescriptionException, UnknownDependencyException {
        return loadAddon(file, true);
    }

    /**
     * Loads the addon in the specified file
     *
     * File must be valid according to the current enabled Addon interfaces
     *
     * @param file File containing the addon to load
     * @param ignoreSoftDependencies Loader will ignore soft dependencies if this flag is set to true
     * @return The Addon loaded, or null if it was invalid
     * @throws InvalidAddonException Thrown when the specified file is not a valid addon
     * @throws InvalidDescriptionException Thrown when the specified file contains an invalid description
     */
    public synchronized Addon loadAddon(File file, boolean ignoreSoftDependencies) throws InvalidAddonException, InvalidDescriptionException, UnknownDependencyException {
        File updateFile = null;

        if (updateDirectory != null && updateDirectory.isDirectory() && (updateFile = new File(updateDirectory, file.getName())).isFile()) {
            if (FileUtil.copy(updateFile, file)) {
                updateFile.delete();
            }
        }

        Set<Pattern> filters = fileAssociations.keySet();
        Addon result = null;

        for (Pattern filter : filters) {
            String name = file.getName();
            Matcher match = filter.matcher(name);

            if (match.find()) {
                AddonLoader loader = fileAssociations.get(filter);

                result = loader.loadAddon(file, ignoreSoftDependencies);
            }
        }

        if (result != null) {
            addons.add(result);
            lookupNames.put(result.getDescription().getName(), result);
        }

        return result;
    }

    /**
     * Checks if the given addon is loaded and returns it when applicable
     *
     * Please note that the name of the addon is case-sensitive
     *
     * @param name Name of the addon to check
     * @return Addon if it exists, otherwise null
     */
    public synchronized Addon getAddon(String name) {
        return lookupNames.get(name);
    }

    public synchronized Addon[] getAddons() {
        return addons.toArray(new Addon[0]);
    }

    /**
     * Checks if the given addon is enabled or not
     *
     * Please note that the name of the addon is case-sensitive.
     *
     * @param name Name of the addon to check
     * @return true if the addon is enabled, otherwise false
     */
    public boolean isAddonEnabled(String name) {
        Addon addon = getAddon(name);

        return isAddonEnabled(addon);
    }

    /**
     * Checks if the given addon is enabled or not
     *
     * @param addon Addon to check
     * @return true if the addon is enabled, otherwise false
     */
    public boolean isAddonEnabled(Addon addon) {
        if ((addon != null) && (addons.contains(addon))) {
            return addon.isEnabled();
        } else {
            return false;
        }
    }

    public void enableAddon(final Addon addon) {
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

    public void disableAddons() {
        for (Addon addon: getAddons()) {
            disableAddon(addon);
        }
    }

    public void disableAddon(final Addon addon) {
        if (addon.isEnabled()) {
            try {
                addon.getAddonLoader().disableAddon(addon);
            } catch (Throwable ex) {
                spoutcraft.getLogger().log(Level.SEVERE, "Error occurred (in the addon loader) while disabling " + addon.getDescription().getFullName() + " (Is it up to date?): " + ex.getMessage(), ex);
            }
        }
    }

    public void clearAddons() {
        synchronized (this) {
            disableAddons();
            addons.clear();
            lookupNames.clear();
            listeners.clear();
        }
    }

    /**
     * Calls a player related event with the given details
     *
     * @param type Type of player related event to call
     * @param event Event details
     */
    public synchronized void callEvent(Event event) {
        SortedSet<RegisteredListener> eventListeners = listeners.get(event.getType());

        if (eventListeners != null) {
            for (RegisteredListener registration : eventListeners) {
                try {
                    registration.callEvent(event);
                } catch (AuthorNagException ex) {
                    Addon addon = registration.getAddon();

                    if (addon.isNaggable()) {
                        addon.setNaggable(false);

                        String author = "<NoAuthorGiven>";

                        if (addon.getDescription().getAuthors().size() > 0) {
                            author = addon.getDescription().getAuthors().get(0);
                        }
                        spoutcraft.getLogger().log(Level.SEVERE, String.format(
                            "Nag author: '%s' of '%s' about the following: %s",
                            author,
                            addon.getDescription().getName(),
                            ex.getMessage()
                        ));
                    }
                } catch (Throwable ex) {
                    spoutcraft.getLogger().log(Level.SEVERE, "Could not pass event " + event.getType() + " to " + registration.getAddon().getDescription().getName(), ex);
                }
            }
        }
    }

    /**
     * Registers the given event to the specified listener
     *
     * @param type EventType to register
     * @param listener PlayerListener to register
     * @param priority Priority of this event
     * @param addon Addon to register
     */
    public void registerEvent(Event.Type type, Listener listener, Priority priority, Addon addon) {
        if (!addon.isEnabled()) {
            throw new IllegalAddonAccessException("Addon attempted to register " + type + " while not enabled");
        }

        getEventListeners(type).add(new RegisteredListener(listener, priority, addon, type));
    }

    /**
     * Registers the given event to the specified listener using a directly passed EventExecutor
     *
     * @param type EventType to register
     * @param listener PlayerListener to register
     * @param executor EventExecutor to register
     * @param priority Priority of this event
     * @param addon Addon to register
     */
    public void registerEvent(Event.Type type, Listener listener, EventExecutor executor, Priority priority, Addon addon) {
        if (!addon.isEnabled()) {
            throw new IllegalAddonAccessException("Addon attempted to register " + type + " while not enabled");
        }

        getEventListeners(type).add(new RegisteredListener(listener, executor, priority, addon));
    }

    /**
     * Returns a SortedSet of RegisteredListener for the specified event type creating a new queue if needed
     *
     * @param type EventType to lookup
     * @return SortedSet<RegisteredListener> the looked up or create queue matching the requested type
     */
    private SortedSet<RegisteredListener> getEventListeners(Event.Type type) {
        SortedSet<RegisteredListener> eventListeners = listeners.get(type);

        if (eventListeners != null) {
            return eventListeners;
        }

        eventListeners = new TreeSet<RegisteredListener>(comparer);
        listeners.put(type, eventListeners);
        return eventListeners;
    }

   


}
