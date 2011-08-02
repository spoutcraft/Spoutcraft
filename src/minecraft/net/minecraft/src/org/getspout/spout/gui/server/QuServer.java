package org.getspout.spout.gui.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.src.GuiMultiplayer;

public class QuServer implements Runnable {

	GuiMultiplayer parentScreen;
	String addr;
	private static Thread instance = null;
	private List tempServerList = new ArrayList();


	public QuServer(GuiMultiplayer var1, String var2) {
		this.parentScreen = var1;
		this.addr = var2;
	}

	public void StartGet() {
		if (instance == null) {
			instance = new Thread(this);
			instance.start();
		}
	}

	public void run() {
		try {
			this.parentScreen.status = "Getting Server List";
			String[] var1 = null;
			URL var2 = new URL(this.addr);
			BufferedReader var3 = new BufferedReader(new InputStreamReader(var2.openStream()));
			String var4 = var3.readLine();
			var3.close();
			this.parentScreen.status = "Processing Servers";
			String[] var5 = var4.split("\\{");

			for(int var6 = 0; var6 < var5.length; ++var6) {
				if(!var5[var6].contains("[Private]")) {
					mcSBServer var7 = new mcSBServer(var6);
					String[] var8 = var5[var6].split("\\,");

					for(int var9 = 0; var9 < var8.length; ++var9) {
						var1 = var8[var9].split("\\:");

						for(int var10 = 0; var10 < var1.length; ++var10) {
							var1[var10] = stripLeadingAndTrailingQuotes(var1[var10]);
						}

						var7 = this.setServer(var6, var1, var7);
					}

					tempServerList.add(var7);
				}
			}

			this.parentScreen.serverList.clear();
			this.parentScreen.serverList = tempServerList;
			Collections.sort(this.parentScreen.serverList);
			this.parentScreen.status = "Done";
		} catch (IOException var11) {
			this.parentScreen.status = "An Error Occured.";
			var11.printStackTrace();
		}
		instance = null;
	}

	private mcSBServer setServer(int var1, String[] var2, mcSBServer var3) {
		if(var2.length >= 2) {
			if(var2[0].equalsIgnoreCase("ServerAddress")) {
				var3.ip = var2[1];
				if(var2.length == 3) {
					var3.port = "" + var2[2];
				}
			} else if(var2[0].equalsIgnoreCase("ServerName")) {
				var3.name = var2[1];
				if(var3.name.length() > 35) {
					var3.name = var3.name.substring(0, 32) + "...";
				}
			} else if(var2[0].equalsIgnoreCase("ServerPlayers")) {
				var3.players = Integer.parseInt(var2[1]);
			} else if(var2[0].equalsIgnoreCase("ServerMaxPlayers")) {
				var3.maxPlayers = Integer.parseInt(var2[1]);
			} else if(var2[0].equalsIgnoreCase("CountryName")) {
				var3.country = var2[1];
			}
		}

		return var3;
	}

	static String stripLeadingAndTrailingQuotes(String var0) {
		if(var0.startsWith("\"")) {
			var0 = var0.substring(1, var0.length());
		}

		if(var0.endsWith("\"")) {
			var0 = var0.substring(0, var0.length() - 1);
		}

		return var0;
	}
}
