package net.minecraft.src;

import java.io.*;
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import net.minecraft.client.Minecraft;
import org.w3c.dom.*;

public class ThreadDownloadResources extends Thread {
	public File resourcesFolder;
	private Minecraft mc;
	private boolean closing;

	public ThreadDownloadResources(File file, Minecraft minecraft) {
		closing = false;
		mc = minecraft;
		setName("Resource download thread");
		setDaemon(true);
		resourcesFolder = new File(file, "resources/");
		if (!resourcesFolder.exists() && !resourcesFolder.mkdirs()) {
			throw new RuntimeException((new StringBuilder()).append("The working directory could not be created: ").append(resourcesFolder).toString());
		}
		else {
			return;
		}
	}

	public void run() {
		try {
			URL url = new URL("http://s3.amazonaws.com/MinecraftResources/");
			DocumentBuilderFactory documentbuilderfactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentbuilder = documentbuilderfactory.newDocumentBuilder();
			Document document = documentbuilder.parse(url.openStream());
			NodeList nodelist = document.getElementsByTagName("Contents");
			for (int i = 0; i < 2; i++) {
				for (int j = 0; j < nodelist.getLength(); j++) {
					Node node = nodelist.item(j);
					if (node.getNodeType() != 1) {
						continue;
					}
					Element element = (Element)node;
					String s = ((Element)element.getElementsByTagName("Key").item(0)).getChildNodes().item(0).getNodeValue();
					long l = Long.parseLong(((Element)element.getElementsByTagName("Size").item(0)).getChildNodes().item(0).getNodeValue());
					if (l <= 0L) {
						continue;
					}
					downloadAndInstallResource(url, s, l, i);
					if (closing) {
						return;
					}
				}
			}
		}
		catch (Exception exception) {
			loadResource(resourcesFolder, "");
			exception.printStackTrace();
		}
	}

	public void reloadResources() {
		loadResource(resourcesFolder, "");
	}

	private void loadResource(File file, String s) {
		File afile[] = file.listFiles();
		for (int i = 0; i < afile.length; i++) {
			if (afile[i].isDirectory()) {
				loadResource(afile[i], (new StringBuilder()).append(s).append(afile[i].getName()).append("/").toString());
				continue;
			}
			try {
				mc.installResource((new StringBuilder()).append(s).append(afile[i].getName()).toString(), afile[i]);
			}
			catch (Exception exception) {
				System.out.println((new StringBuilder()).append("Failed to add ").append(s).append(afile[i].getName()).toString());
			}
		}
	}

	private void downloadAndInstallResource(URL url, String s, long l, int i) {
		try {
			int j = s.indexOf("/");
			String s1 = s.substring(0, j);
			if (s1.equals("sound") || s1.equals("newsound")) {
				if (i != 0) {
					return;
				}
			}
			else if (i != 1) {
				return;
			}
			File file = new File(resourcesFolder, s);
			if (!file.exists() || file.length() != l) {
				file.getParentFile().mkdirs();
				String s2 = s.replaceAll(" ", "%20");
				downloadResource(new URL(url, s2), file, l);
				if (closing) {
					return;
				}
			}
			mc.installResource(s, file);
		}
		catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	private void downloadResource(URL url, File file, long l)
	throws IOException {
		byte abyte0[] = new byte[4096];
		DataInputStream datainputstream = new DataInputStream(url.openStream());
		DataOutputStream dataoutputstream = new DataOutputStream(new FileOutputStream(file));
		for (int i = 0; (i = datainputstream.read(abyte0)) >= 0;) {
			dataoutputstream.write(abyte0, 0, i);
			if (closing) {
				return;
			}
		}

		datainputstream.close();
		dataoutputstream.close();
	}

	public void closeMinecraft() {
		closing = true;
	}
}
