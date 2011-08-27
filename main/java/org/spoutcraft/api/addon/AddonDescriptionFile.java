package org.spoutcraft.api.addon;

import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.spoutcraft.api.addon.Addon.Mode;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;

public class AddonDescriptionFile {
	private static final Yaml yaml = new Yaml(new SafeConstructor());
	private String name = null;
	private String main = null;
	private ArrayList<String> depend = null;
	private ArrayList<String> softDepend = null;
	private String version = null;
	public Mode mode= null;
	private Object commands = null;
	private String description = null;
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private ArrayList<String> authors = new ArrayList();
	private String website = null;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public AddonDescriptionFile(InputStream stream) throws InvalidDescriptionException {
		loadMap((Map)yaml.load(stream));
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public AddonDescriptionFile(Reader reader) throws InvalidDescriptionException {
		loadMap((Map)yaml.load(reader));
	}

	public AddonDescriptionFile(String addonName, String addonVersion, String mainClass) {
		this.name = addonName;
		this.version = addonVersion;
		this.main = mainClass;
	}

	public void save(Writer writer) {
		yaml.dump(saveMap(), writer);
	}

	public String getName() {
		return this.name;
	}

	public String getVersion() {
		return this.version;
	}

	public String getFullName() {
		return this.name + " v" + this.version;
	}

	public String getMain() {
		return this.main;
	}

	public Object getCommands() {
		return this.commands;
	}

	public Object getDepend() {
		return this.depend;
	}

	public Object getSoftDepend() {
		return this.softDepend;
	}

	public String getDescription()
	{
		return this.description;
	}

	public ArrayList<String> getAuthors() {
		return this.authors;
	}

	public String getWebsite() {
		return this.website;
	}

	

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void loadMap(Map<String, Object> map) throws InvalidDescriptionException {
		try {
			this.name = map.get("name").toString();

			if (!this.name.matches("^[A-Za-z0-9 _.-]+$")) throw new InvalidDescriptionException("name '" + this.name + "' contains invalid characters.");
		} catch (NullPointerException ex) {
			throw new InvalidDescriptionException(ex, "name is not defined");
		} catch (ClassCastException ex) {
			throw new InvalidDescriptionException(ex, "name is of wrong type");
		}
		
		try {
			this.version = map.get("version").toString();
		} catch (NullPointerException ex) {
			throw new InvalidDescriptionException(ex, "version is not defined");
		} catch (ClassCastException ex) {
			throw new InvalidDescriptionException(ex, "version is of wrong type");
		}
		
		try {
			this.main = map.get("main").toString();
			if (this.main.startsWith("org.bukkit."))
				throw new InvalidDescriptionException("main may not be within the org.bukkit namespace");
		} catch (NullPointerException ex) {
			throw new InvalidDescriptionException(ex, "main is not defined");
		} catch (ClassCastException ex) {
			throw new InvalidDescriptionException(ex, "main is of wrong type");
		}
		
		try {
			String mode = map.get("mode").toString();
			
			this.mode = Mode.valueOf(mode);
			
			if (this.mode == null) {
				if (map.containsKey("mode")) {
					throw new InvalidDescriptionException(null, "mode is of wrong type");
				} else {
					throw new InvalidDescriptionException("mode is not defined");
				}
			}
			
		} catch (NullPointerException ex) {
			throw new InvalidDescriptionException(ex, "mode is not defined");
		} catch (ClassCastException ex) {
			throw new InvalidDescriptionException(ex, "mode is of wrong type");
		}

		if (map.containsKey("commands")) {
			try {
				this.commands = map.get("commands");
			} catch (ClassCastException ex) {
				throw new InvalidDescriptionException(ex, "commands are of wrong type");
			}
		}

		if (map.containsKey("depend")) {
			try {
				this.depend = ((ArrayList)map.get("depend"));
			} catch (ClassCastException ex) {
				throw new InvalidDescriptionException(ex, "depend is of wrong type");
			}
		}

		if (map.containsKey("softdepend")) {
			try {
				this.softDepend = ((ArrayList)map.get("softdepend"));
			} catch (ClassCastException ex) {
				throw new InvalidDescriptionException(ex, "softdepend is of wrong type");
			}
		}

		if (map.containsKey("website")) {
			try {
				this.website = ((String)map.get("website"));
			} catch (ClassCastException ex) {
				throw new InvalidDescriptionException(ex, "website is of wrong type");
			}
		}

		if (map.containsKey("description")) {
			try {
				this.description = ((String)map.get("description"));
			} catch (ClassCastException ex) {
				throw new InvalidDescriptionException(ex, "description is of wrong type");
			}
		}

		if (map.containsKey("author")) {
			try {
				String extra = (String)map.get("author");

				this.authors.add(extra);
			} catch (ClassCastException ex) {
				throw new InvalidDescriptionException(ex, "author is of wrong type");
			}
		}

		if (map.containsKey("authors")) {
			try {
				ArrayList extra = (ArrayList)map.get("authors");

				this.authors.addAll(extra);
			} catch (ClassCastException ex) {
				throw new InvalidDescriptionException(ex, "authors are of wrong type");
			}
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Map<String, Object> saveMap() {
		Map map = new HashMap();

		map.put("name", this.name);
		map.put("main", this.main);
		map.put("version", this.version);

		if (this.commands != null) {
			map.put("command", this.commands);
		}
		if (this.depend != null) {
			map.put("depend", this.depend);
		}
		if (this.softDepend != null) {
			map.put("softdepend", this.softDepend);
		}
		if (this.website != null) {
			map.put("website", this.website);
		}
		if (this.description != null) {
			map.put("description", this.description);
		}
		if (this.authors.size() == 1)
			map.put("author", this.authors.get(0));
		else if (this.authors.size() > 1) {
			map.put("authors", this.authors);
		}

		return map;
	}

}
