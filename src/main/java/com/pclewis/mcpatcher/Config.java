package com.pclewis.mcpatcher;

import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;

class Config {
	private File xmlFile = null;
	Document xml;
	Element selectedProfile;

	/*
	* <mcpatcher-profile>
	*	 <config>
	*		 <debug>false</debug>
	*		 <java-heap-size>1024</java-heap-size>
	*		 <last-version>2.0.1</last-version>
	*		 <beta-warning-shown>false</beta-warning-shown>
	*	 </config>
	*	 <mods profile="Minecraft 1.8.1">
	*		 <mod>
	*			 <name>HD Textures</name>
	*			 <type>builtin</type>
	*			 <enabled>true</enabled>
	*			 <config>
	*				 <useAnimatedTextures>true</useAnimatedTextures>
	*			 </config>
	*		 </mod>
	*		 <mod>
	*			 <name>ModLoader</name>
	*			 <type>external zip</type>
	*			 <path>/home/user/.minecraft/mods/ModLoader.zip</path>
	*			 <prefix />
	*			 <enabled>true</enabled>
	*		 </mod>
	*	 </mods>
	*	 <mods profile="Minecraft 1.9">
	*		 ...
	*	 </mods>
	* </mcpatcher-profile>
	*/
	static final String TAG_ROOT = "mcpatcherProfile";
	static final String TAG_CONFIG1 = "config";
	static final String TAG_SELECTED_PROFILE = "selectedProfile";
	static final String TAG_LAST_MOD_DIRECTORY = "lastModDirectory";
	static final String TAG_DEBUG = "debug";
	static final String TAG_JAVA_HEAP_SIZE = "javaHeapSize";
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

	private static final int XML_INDENT_AMOUNT = 2;
	private static final String XSLT_REFORMAT =
		"<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">" +
			"<xsl:output method=\"xml\" omit-xml-declaration=\"no\"/>" +
			"<xsl:strip-space elements=\"*\"/>" +
			"<xsl:template match=\"@*|node()\">" +
			"<xsl:copy>" +
			"<xsl:apply-templates select=\"@*|node()\"/>" +
			"</xsl:copy>" +
			"</xsl:template>" +
			"</xsl:stylesheet>";

	Config(File minecraftDir) throws ParserConfigurationException {
		xmlFile = new File(minecraftDir, "mcpatcher.xml");
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		boolean save = false;
		if (xmlFile.exists()) {
			try {
				xml = builder.parse(xmlFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (xml == null) {
			xml = builder.newDocument();
			buildNewProperties();
			save = true;
		}

		if (save) {
			saveProperties();
		}
	}

	Element getElement(Element parent, String tag) {
		if (parent == null) {
			return null;
		}
		NodeList list = parent.getElementsByTagName(tag);
		Element element;
		if (list.getLength() == 0) {
			element = xml.createElement(tag);
			parent.appendChild(element);
		} else {
			element = (Element) list.item(0);
		}
		return element;
	}

	String getText(Node node) {
		if (node == null) {
			return null;
		}
		switch (node.getNodeType()) {
			case Node.TEXT_NODE:
				return ((Text) node).getData();

			case Node.ATTRIBUTE_NODE:
				return ((Attr) node).getValue();

			case Node.ELEMENT_NODE:
				NodeList list = node.getChildNodes();
				for (int i = 0; i < list.getLength(); i++) {
					Node node1 = list.item(i);
					if (node1.getNodeType() == Node.TEXT_NODE) {
						return ((Text) node1).getData();
					}
				}

			default:
				break;
		}
		return null;
	}

	void setText(Element parent, String tag, String value) {
		if (parent == null) {
			return;
		}
		Element element = getElement(parent, tag);
		while (element.hasChildNodes()) {
			element.removeChild(element.getFirstChild());
		}
		Text text = xml.createTextNode(value);
		element.appendChild(text);
	}

	void remove(Node node) {
		if (node != null) {
			Node parent = node.getParentNode();
			parent.removeChild(node);
		}
	}

	String getText(Element parent, String tag) {
		return getText(getElement(parent, tag));
	}

	Element getRoot() {
		if (xml == null) {
			return null;
		}
		Element root = xml.getDocumentElement();
		if (root == null) {
			root = xml.createElement(TAG_ROOT);
			xml.appendChild(root);
		}
		return root;
	}

	Element getConfig() {
		return getElement(getRoot(), TAG_CONFIG1);
	}

	Element getConfig(String tag) {
		return getElement(getConfig(), tag);
	}

	String getConfigValue(String tag) {
		return getText(getConfig(tag));
	}

	void setConfigValue(String tag, String value) {
		Element element = getConfig(tag);
		if (element != null) {
			while (element.hasChildNodes()) {
				element.removeChild(element.getFirstChild());
			}
			element.appendChild(xml.createTextNode(value));
		}
	}

	static String getDefaultProfileName(String mcVersion) {
		return "Minecraft " + mcVersion;
	}

	static boolean isDefaultProfile(String profileName) {
		return profileName.startsWith("Minecraft ");
	}

	void setDefaultProfileName(String profileName) {
		Element root = getRoot();
		NodeList list = root.getElementsByTagName(TAG_MODS);
		String name = getConfigValue(TAG_SELECTED_PROFILE);
		if (name == null || name.equals("")) {
			setConfigValue(TAG_SELECTED_PROFILE, profileName);
		}
		Element element;
		boolean found = false;
		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			if (node instanceof Element) {
				element = (Element) node;
				name = element.getAttribute(ATTR_PROFILE);
				if (name == null || name.equals("")) {
					if (found) {
						root.removeChild(element);
					} else {
						element.setAttribute(ATTR_PROFILE, profileName);
						found = true;
					}
				}
			}
		}
	}

	Element findProfileByName(String profileName, boolean create) {
		Element profile = null;
		Element root = getRoot();
		NodeList list = root.getElementsByTagName(TAG_MODS);
		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			if (node instanceof Element) {
				Element element = (Element) node;
				String name = element.getAttribute(ATTR_PROFILE);
				if (profileName == null || profileName.equals(name)) {
					return element;
				}
			}
		}
		if (create) {
			profile = xml.createElement(TAG_MODS);
			if (selectedProfile != null) {
				list = selectedProfile.getElementsByTagName(TAG_MOD);
				for (int i = 0; i < list.getLength(); i++) {
					Node node = list.item(i);
					if (node instanceof Element) {
						Element element = (Element) node;
						String name = getText(element, TAG_TYPE);
						if (VAL_BUILTIN.equals(name)) {
							profile.appendChild(node.cloneNode(true));
						}
					}
				}
			}
			profile.setAttribute(ATTR_PROFILE, profileName);
			root.appendChild(profile);
		}
		return profile;
	}

	void selectProfile() {
		selectProfile(getConfigValue(TAG_SELECTED_PROFILE));
	}

	void selectProfile(String profileName) {
		selectedProfile = findProfileByName(profileName, true);
		setConfigValue(TAG_SELECTED_PROFILE, profileName);
	}

	void deleteProfile(String profileName) {
		Element root = getRoot();
		Element profile = findProfileByName(profileName, false);
		if (profile != null) {
			if (profile == selectedProfile) {
				selectedProfile = null;
			}
			root.removeChild(profile);
		}
		getMods();
	}

	void renameProfile(String oldName, String newName) {
		if (!oldName.equals(newName)) {
			Element profile = findProfileByName(oldName, false);
			if (profile != null) {
				profile.setAttribute(ATTR_PROFILE, newName);
				String selectedProfile = getConfigValue(TAG_SELECTED_PROFILE);
				if (oldName.equals(selectedProfile)) {
					setConfigValue(TAG_SELECTED_PROFILE, newName);
				}
			}
		}
	}

	void rewriteModPaths(File oldDir, File newDir) {
		NodeList profiles = getRoot().getElementsByTagName(TAG_MODS);
		for (int i = 0; i < profiles.getLength(); i++) {
			Element profile = (Element) profiles.item(i);
			rewriteModPaths(profile, oldDir, newDir);
		}
	}

	void rewriteModPaths(Element profile, File oldDir, File newDir) {
		NodeList mods = profile.getElementsByTagName(TAG_MOD);
		for (int i = 0; i < mods.getLength(); i++) {
			Element mod = (Element) mods.item(i);
			String type = getText(mod, TAG_TYPE);
			if (VAL_EXTERNAL_ZIP.equals(type)) {
				String currentPath = getText(mod, TAG_PATH);
				if (currentPath != null && !currentPath.equals("")) {
					File currentFile = new File(currentPath);
					if (oldDir.equals(currentFile.getParentFile())) {
						setText(mod, TAG_PATH, new File(newDir, currentFile.getName()).getPath());
					}
				}
			}
		}
	}

	ArrayList<String> getProfiles() {
		ArrayList<String> profiles = new ArrayList<String>();
		Element root = getRoot();
		NodeList list = root.getElementsByTagName(TAG_MODS);
		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			if (node instanceof Element) {
				Element element = (Element) node;
				String name = element.getAttribute(ATTR_PROFILE);
				if (name != null && !name.equals("")) {
					profiles.add(name);
				}
			}
		}
		Collections.sort(profiles);
		return profiles;
	}

	Element getMods() {
		if (selectedProfile == null) {
			selectProfile();
		}
		return selectedProfile;
	}

	boolean hasMod(String mod) {
		Element parent = getMods();
		if (parent != null) {
			NodeList list = parent.getElementsByTagName(TAG_MOD);
			for (int i = 0; i < list.getLength(); i++) {
				Element element = (Element) list.item(i);
				NodeList list1 = element.getElementsByTagName(TAG_NAME);
				if (list1.getLength() > 0) {
					element = (Element) list1.item(0);
					if (mod.equals(getText(element))) {
						return true;
					}
				}
			}
		}
		return false;
	}

	Element getMod(String mod) {
		Element parent = getMods();
		if (parent == null) {
			return null;
		}
		NodeList list = parent.getElementsByTagName(TAG_MOD);
		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			if (node instanceof Element) {
				Element element = (Element) node;
				if (mod.equals(getText(element, TAG_NAME))) {
					return element;
				}
			}
		}
		Element element = xml.createElement(TAG_MOD);
		parent.appendChild(element);
		Element element1 = xml.createElement(TAG_NAME);
		Text text = xml.createTextNode(mod);
		element1.appendChild(text);
		element.appendChild(element1);
		element1 = xml.createElement(TAG_ENABLED);
		element.appendChild(element1);
		element1 = xml.createElement(TAG_TYPE);
		element.appendChild(element1);
		return element;
	}

	void setModEnabled(String mod, boolean enabled) {
		setText(getMod(mod), TAG_ENABLED, Boolean.toString(enabled));
	}

	Element getModConfig(String mod) {
		return getElement(getMod(mod), TAG_CONFIG1);
	}

	Element getModConfig(String mod, String tag) {
		return getElement(getModConfig(mod), tag);
	}

	String getModConfigValue(String mod, String tag) {
		return getText(getModConfig(mod, tag));
	}

	void setModConfigValue(String mod, String tag, String value) {
		Element element = getModConfig(mod, tag);
		if (element != null) {
			while (element.hasChildNodes()) {
				element.removeChild(element.getFirstChild());
			}
			element.appendChild(xml.createTextNode(value));
		}
	}

	private Element getLogLevelElement(String category) {
		Element config = getConfig();
		NodeList list = config.getElementsByTagName(TAG_LOGGING);
		for (int i = 0; i < list.getLength(); i++) {
			Element element = (Element) list.item(i);
			if (category.equals(element.getAttribute(TAG_CATEGORY))) {
				return element;
			}
		}
		Element element = xml.createElement(TAG_LOGGING);
		config.appendChild(element);
		element.setAttribute(TAG_CATEGORY, category);
		return element;
	}

	void setLogLevel(String category, Level level) {
		getLogLevelElement(category).setAttribute(TAG_LEVEL, level.toString());
	}

	Level getLogLevel(String category) {
		Level level = Level.INFO;
		Element element = getLogLevelElement(category);
		try {
			String attribute = element.getAttribute(TAG_LEVEL);
			if (attribute != null) {
				level = Level.parse(attribute.trim().toUpperCase());
			}
		} catch (Throwable e) {
		}
		element.setAttribute(TAG_LEVEL, level.toString());
		return level;
	}

	private void buildNewProperties() {
		if (xml != null) {
			getRoot();
			getConfig();
			if (selectedProfile != null) {
				getMods();
				setText(getMod(MCPatcherUtils.HD_TEXTURES), TAG_ENABLED, "true");
				setText(getMod(MCPatcherUtils.HD_FONT), TAG_ENABLED, "true");
				setText(getMod(MCPatcherUtils.BETTER_GRASS), TAG_ENABLED, "false");
			}
		}
	}

	/**
	 * Save all properties to mcpatcher.xml.
	 *
	 * @return true if successful
	 */
	boolean saveProperties() {
		boolean saved = false;
		if (xml != null && xmlFile != null) {
			FileOutputStream os = null;
			try {
				TransformerFactory factory = TransformerFactory.newInstance();
				Transformer trans;
				try {
					factory.setAttribute("indent-number", XML_INDENT_AMOUNT);
					trans = factory.newTransformer(new StreamSource(new StringReader(XSLT_REFORMAT)));
					trans.setOutputProperty(OutputKeys.INDENT, "yes");
					trans.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "" + XML_INDENT_AMOUNT);
				} catch (Throwable e) {
					trans = factory.newTransformer();
				}
				DOMSource source = new DOMSource(xml);
				os = new FileOutputStream(xmlFile);
				trans.transform(source, new StreamResult(new OutputStreamWriter(os, "UTF-8")));
				saved = true;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				MCPatcherUtils.close(os);
			}
		}
		return saved;
	}
}