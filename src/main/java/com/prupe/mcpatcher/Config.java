package com.prupe.mcpatcher;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class Config {
	static Config instance = null;
	private File xmlFile = null;
	Document xml;
	Element selectedProfile;
	static final String TAG_ROOT = "mcpatcherProfile";
	static final String TAG_CONFIG1 = "config";
	static final String TAG_SELECTED_PROFILE = "selectedProfile";
	static final String TAG_LAST_MOD_DIRECTORY = "lastModDirectory";
	static final String TAG_DEBUG = "debug";
	static final String TAG_JAVA_HEAP_SIZE = "javaHeapSize";
	static final String TAG_DIRECT_MEMORY_SIZE = "directMemorySize";
	static final String TAG_LAST_VERSION = "lastVersion";
	static final String TAG_BETA_WARNING_SHOWN = "betaWarningShown";
	static final String TAG_LOGGING = "logging";
	static final String TAG_LEVEL = "level";
	static final String TAG_MODS = "mods";
	static final String ATTR_PROFILE = "profile";
	static final String TAG_MOD = "mod";
	static final String TAG_CATEGORY = "category";
	static final String TAG_NAME = "name";
	static final String TAG_TYPE = "type";
	static final String TAG_PATH = "path";
	static final String TAG_FILES = "files";
	static final String TAG_FILE = "file";
	static final String TAG_FROM = "from";
	static final String TAG_TO = "to";
	static final String TAG_CLASS = "class";
	static final String TAG_ENABLED = "enabled";
	static final String ATTR_VERSION = "version";
	static final String VAL_BUILTIN = "builtIn";
	static final String VAL_EXTERNAL_ZIP = "externalZip";
	static final String VAL_EXTERNAL_JAR = "externalJar";
	static final String MCPATCHER_PROPERTIES = "mcpatcher.properties";
	static final String TAG_MINECRAFT_VERSION = "minecraftVersion";
	static final String TAG_PATCHER_VERSION = "patcherVersion";
	static final String TAG_PRE_PATCH_STATE = "prePatchState";
	static final String TAG_MODIFIED_CLASSES = "modifiedClasses";
	static final String TAG_ADDED_CLASSES = "addedClasses";
	static final String XML_FILENAME = "mcpatcher4.xml";
	private static final int XML_INDENT_AMOUNT = 2;
	private static final String XSLT_REFORMAT = "<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\"><xsl:output method=\"xml\" omit-xml-declaration=\"no\"/><xsl:strip-space elements=\"*\"/><xsl:template match=\"@*|node()\"><xsl:copy><xsl:apply-templates select=\"@*|node()\"/></xsl:copy></xsl:template></xsl:stylesheet>";

	Config(File minecraftDir) throws ParserConfigurationException {
		this.xmlFile = new File(minecraftDir, "mcpatcher4.xml");
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		boolean save = false;

		if (this.xmlFile.exists() && this.xmlFile.length() > 0L) {
			try {
				this.xml = builder.parse(this.xmlFile);
			} catch (Exception var6) {
				var6.printStackTrace();
			}
		}

		if (this.xml == null) {
			this.xml = builder.newDocument();
			this.buildNewProperties();
			save = true;
		}

		if (save) {
			this.saveProperties();
		}
	}

	public static String getString(String mod, String tag, Object defaultValue) {
		if (instance == null) {
			return defaultValue == null ? null : defaultValue.toString();
		} else {
			String value = instance.getModConfigValue(mod, tag);

			if (value == null && defaultValue != null) {
				value = defaultValue.toString();
				instance.setModConfigValue(mod, tag, value);
			}

			return value;
		}
	}

	public static String getString(String tag, Object defaultValue) {
		if (instance == null) {
			return defaultValue == null ? null : defaultValue.toString();
		} else {
			String value = instance.getConfigValue(tag);

			if (value == null && defaultValue != null) {
				value = defaultValue.toString();
				instance.setConfigValue(tag, value);
			}

			return value;
		}
	}

	public static int getInt(String mod, String tag, int defaultValue) {
		int value;

		try {
			value = Integer.parseInt(getString(mod, tag, Integer.valueOf(defaultValue)));
		} catch (NumberFormatException var5) {
			value = defaultValue;
		}

		return value;
	}

	public static int getInt(String tag, int defaultValue) {
		int value;

		try {
			value = Integer.parseInt(getString(tag, Integer.valueOf(defaultValue)));
		} catch (NumberFormatException var4) {
			value = defaultValue;
		}

		return value;
	}

	public static boolean getBoolean(String mod, String tag, boolean defaultValue) {
		String value = getString(mod, tag, Boolean.valueOf(defaultValue)).toLowerCase();
		return value.equals("false") ? false : (value.equals("true") ? true : defaultValue);
	}

	public static boolean getBoolean(String tag, boolean defaultValue) {
		String value = getString(tag, Boolean.valueOf(defaultValue)).toLowerCase();
		return value.equals("false") ? false : (value.equals("true") ? true : defaultValue);
	}

	public static void set(String mod, String tag, Object value) {
		if (instance != null) {
			instance.setModConfigValue(mod, tag, value.toString());
		}
	}

	static void set(String tag, Object value) {
		if (instance != null) {
			instance.setConfigValue(tag, value.toString());
		}
	}

	public static void remove(String mod, String tag) {
		if (instance != null) {
			instance.remove((Node)instance.getModConfig(mod, tag));
		}
	}

	static void remove(String tag) {
		if (instance != null) {
			instance.remove((Node)instance.getConfig(tag));
		}
	}

	static void setLogLevel(String category, Level level) {
		if (instance != null) {
			instance.setLogLevel1(category, level);
		}
	}

	static Level getLogLevel(String category) {
		return instance == null ? Level.INFO : instance.getLogLevel1(category);
	}

	static boolean load(File minecraftDir) {
		instance = null;

		if (minecraftDir != null && minecraftDir.exists()) {
			try {
				instance = new Config(minecraftDir);
			} catch (Exception var2) {
				var2.printStackTrace();
			}

			return true;
		} else {
			return false;
		}
	}

	Element getElement(Element parent, String tag) {
		if (parent == null) {
			return null;
		} else {
			NodeList list = parent.getElementsByTagName(tag);
			Element element;

			if (list.getLength() == 0) {
				element = this.xml.createElement(tag);
				parent.appendChild(element);
			} else {
				element = (Element)list.item(0);
			}

			return element;
		}
	}

	String getText(Node node) {
		if (node == null) {
			return null;
		} else {
			switch (node.getNodeType()) {
				case 1:
					NodeList list = node.getChildNodes();

					for (int i = 0; i < list.getLength(); ++i) {
						Node node1 = list.item(i);

						if (node1.getNodeType() == 3) {
							return ((Text)node1).getData();
						}
					}

				case 2:
					return ((Attr)node).getValue();

				case 3:
					return ((Text)node).getData();

				default:
					return null;
			}
		}
	}

	void setText(Element parent, String tag, String value) {
		if (parent != null) {
			Element element = this.getElement(parent, tag);

			while (element.hasChildNodes()) {
				element.removeChild(element.getFirstChild());
			}

			Text text = this.xml.createTextNode(value);
			element.appendChild(text);
		}
	}

	void remove(Node node) {
		if (node != null) {
			Node parent = node.getParentNode();
			parent.removeChild(node);
		}
	}

	String getText(Element parent, String tag) {
		return this.getText(this.getElement(parent, tag));
	}

	Element getRoot() {
		if (this.xml == null) {
			return null;
		} else {
			Element root = this.xml.getDocumentElement();

			if (root == null) {
				root = this.xml.createElement("mcpatcherProfile");
				this.xml.appendChild(root);
			}

			return root;
		}
	}

	Element getConfig() {
		return this.getElement(this.getRoot(), "config");
	}

	Element getConfig(String tag) {
		return this.getElement(this.getConfig(), tag);
	}

	String getConfigValue(String tag) {
		return this.getText(this.getConfig(tag));
	}

	void setConfigValue(String tag, String value) {
		Element element = this.getConfig(tag);

		if (element != null) {
			while (element.hasChildNodes()) {
				element.removeChild(element.getFirstChild());
			}

			element.appendChild(this.xml.createTextNode(value));
		}
	}

	static String getDefaultProfileName(String mcVersion) {
		return "Minecraft " + mcVersion;
	}

	static boolean isDefaultProfile(String profileName) {
		return profileName.startsWith("Minecraft ");
	}

	static String getVersionForDefaultProfile(String profileName) {
		return isDefaultProfile(profileName) ? profileName.replaceFirst("Minecraft\\s+", "") : null;
	}

	void setDefaultProfileName(String profileName) {
		Element root = this.getRoot();
		NodeList list = root.getElementsByTagName("mods");
		String name = this.getConfigValue("selectedProfile");

		if (name == null || name.equals("")) {
			this.setConfigValue("selectedProfile", profileName);
		}

		boolean found = false;

		for (int i = 0; i < list.getLength(); ++i) {
			Node node = list.item(i);

			if (node instanceof Element) {
				Element element = (Element)node;
				name = element.getAttribute("profile");

				if (name == null || name.equals("")) {
					if (found) {
						root.removeChild(element);
					} else {
						element.setAttribute("profile", profileName);
						found = true;
					}
				}
			}
		}
	}

	Element findProfileByName(String profileName, boolean create) {
		Element profile = null;
		Element root = this.getRoot();
		NodeList list = root.getElementsByTagName("mods");
		int i;
		Node node;
		Element element;
		String name;

		for (i = 0; i < list.getLength(); ++i) {
			node = list.item(i);

			if (node instanceof Element) {
				element = (Element)node;
				name = element.getAttribute("profile");

				if (profileName == null || profileName.equals(name)) {
					return element;
				}
			}
		}

		if (create) {
			profile = this.xml.createElement("mods");

			if (this.selectedProfile != null) {
				list = this.selectedProfile.getElementsByTagName("mod");

				for (i = 0; i < list.getLength(); ++i) {
					node = list.item(i);

					if (node instanceof Element) {
						element = (Element)node;
						name = this.getText(element, "type");

						if ("builtIn".equals(name)) {
							profile.appendChild(node.cloneNode(true));
						}
					}
				}
			}

			profile.setAttribute("profile", profileName);
			root.appendChild(profile);
		}

		return profile;
	}

	void selectProfile() {
		this.selectProfile(this.getConfigValue("selectedProfile"));
	}

	void selectProfile(String profileName) {
		if (profileName == null) {
			profileName = "";
		}

		this.selectedProfile = this.findProfileByName(profileName, true);
		this.setConfigValue("selectedProfile", profileName);
	}

	void deleteProfile(String profileName) {
		Element root = this.getRoot();
		Element profile = this.findProfileByName(profileName, false);

		if (profile != null) {
			if (profile == this.selectedProfile) {
				this.selectedProfile = null;
			}

			root.removeChild(profile);
		}

		this.getMods();
	}

	void renameProfile(String oldName, String newName) {
		if (!oldName.equals(newName)) {
			Element profile = this.findProfileByName(oldName, false);

			if (profile != null) {
				profile.setAttribute("profile", newName);
				String selectedProfile = this.getConfigValue("selectedProfile");

				if (oldName.equals(selectedProfile)) {
					this.setConfigValue("selectedProfile", newName);
				}
			}
		}
	}

	void rewriteModPaths(File oldDir, File newDir) {
		NodeList profiles = this.getRoot().getElementsByTagName("mods");

		for (int i = 0; i < profiles.getLength(); ++i) {
			Element profile = (Element)profiles.item(i);
			this.rewriteModPaths(profile, oldDir, newDir);
		}
	}

	void rewriteModPaths(Element profile, File oldDir, File newDir) {
		NodeList mods = profile.getElementsByTagName("mod");

		for (int i = 0; i < mods.getLength(); ++i) {
			Element mod = (Element)mods.item(i);
			String type = this.getText(mod, "type");

			if ("externalZip".equals(type)) {
				String currentPath = this.getText(mod, "path");

				if (currentPath != null && !currentPath.equals("")) {
					File currentFile = new File(currentPath);

					if (oldDir.equals(currentFile.getParentFile())) {
						this.setText(mod, "path", (new File(newDir, currentFile.getName())).getPath());
					}
				}
			}
		}
	}

	ArrayList<String> getProfiles() {
		ArrayList profiles = new ArrayList();
		Element root = this.getRoot();
		NodeList list = root.getElementsByTagName("mods");

		for (int i = 0; i < list.getLength(); ++i) {
			Node node = list.item(i);

			if (node instanceof Element) {
				Element element = (Element)node;
				String name = element.getAttribute("profile");

				if (name != null && !name.equals("")) {
					profiles.add(name);
				}
			}
		}

		Collections.sort(profiles);
		return profiles;
	}

	Element getMods() {
		if (this.selectedProfile == null) {
			this.selectProfile();
		}

		return this.selectedProfile;
	}

	boolean hasMod(String mod) {
		Element parent = this.getMods();

		if (parent != null) {
			NodeList list = parent.getElementsByTagName("mod");

			for (int i = 0; i < list.getLength(); ++i) {
				Element element = (Element)list.item(i);
				NodeList list1 = element.getElementsByTagName("name");

				if (list1.getLength() > 0) {
					element = (Element)list1.item(0);

					if (mod.equals(this.getText(element))) {
						return true;
					}
				}
			}
		}

		return false;
	}

	Element getMod(String mod) {
		Element parent = this.getMods();

		if (parent == null) {
			return null;
		} else {
			NodeList list = parent.getElementsByTagName("mod");

			for (int element = 0; element < list.getLength(); ++element) {
				Node element1 = list.item(element);

				if (element1 instanceof Element) {
					Element text = (Element)element1;

					if (mod.equals(this.getText(text, "name"))) {
						return text;
					}
				}
			}

			Element var7 = this.xml.createElement("mod");
			parent.appendChild(var7);
			Element var8 = this.xml.createElement("name");
			Text var9 = this.xml.createTextNode(mod);
			var8.appendChild(var9);
			var7.appendChild(var8);
			var8 = this.xml.createElement("enabled");
			var7.appendChild(var8);
			var8 = this.xml.createElement("type");
			var7.appendChild(var8);
			return var7;
		}
	}

	void setModEnabled(String mod, boolean enabled) {
		this.setText(this.getMod(mod), "enabled", Boolean.toString(enabled));
	}

	Element getModConfig(String mod) {
		return this.getElement(this.getMod(mod), "config");
	}

	Element getModConfig(String mod, String tag) {
		return this.getElement(this.getModConfig(mod), tag);
	}

	String getModConfigValue(String mod, String tag) {
		return this.getText(this.getModConfig(mod, tag));
	}

	void setModConfigValue(String mod, String tag, String value) {
		Element element = this.getModConfig(mod, tag);

		if (element != null) {
			while (element.hasChildNodes()) {
				element.removeChild(element.getFirstChild());
			}

			element.appendChild(this.xml.createTextNode(value));
		}
	}

	private Element getLogLevelElement(String category) {
		Element config = this.getConfig();
		NodeList list = config.getElementsByTagName("logging");

		for (int element = 0; element < list.getLength(); ++element) {
			Element element1 = (Element)list.item(element);

			if (category.equals(element1.getAttribute("category"))) {
				return element1;
			}
		}

		Element var6 = this.xml.createElement("logging");
		config.appendChild(var6);
		var6.setAttribute("category", category);
		return var6;
	}

	void setLogLevel1(String category, Level level) {
		this.getLogLevelElement(category).setAttribute("level", level.toString());
	}

	Level getLogLevel1(String category) {
		Level level = Level.INFO;
		Element element = this.getLogLevelElement(category);

		try {
			String e = element.getAttribute("level");

			if (e != null) {
				level = Level.parse(e.trim().toUpperCase());
			}
		} catch (Throwable var5) {
			;
		}

		element.setAttribute("level", level.toString());
		return level;
	}

	private void buildNewProperties() {
		if (this.xml != null) {
			this.getRoot();
			this.getConfig();
			this.getMods();
		}
	}

	boolean saveProperties() {
		boolean saved = false;

		if (this.xml != null && this.xmlFile != null) {
			File tmpFile = new File(this.xmlFile.getParentFile(), this.xmlFile.getName() + ".tmp");
			FileOutputStream output = null;

			try {
				TransformerFactory e = TransformerFactory.newInstance();
				Transformer trans;

				try {
					e.setAttribute("indent-number", Integer.valueOf(2));
					trans = e.newTransformer(new StreamSource(new StringReader("<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\"><xsl:output method=\"xml\" omit-xml-declaration=\"no\"/><xsl:strip-space elements=\"*\"/><xsl:template match=\"@*|node()\"><xsl:copy><xsl:apply-templates select=\"@*|node()\"/></xsl:copy></xsl:template></xsl:stylesheet>")));
					trans.setOutputProperty("indent", "yes");
					trans.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
				} catch (Throwable var11) {
					trans = e.newTransformer();
				}

				DOMSource source = new DOMSource(this.xml);
				output = new FileOutputStream(tmpFile);
				trans.transform(source, new StreamResult(new OutputStreamWriter(output, "UTF-8")));
				output.close();
				this.xmlFile.delete();
				saved = tmpFile.renameTo(this.xmlFile);
			} catch (Throwable var12) {
				var12.printStackTrace();
			} finally {
				MCPatcherUtils.close((Closeable)output);
				tmpFile.delete();
			}
		}

		return saved;
	}
}
