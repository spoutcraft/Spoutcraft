package net.minecraft.src;

import java.net.HttpURLConnection;
import java.net.URL;
import net.minecraft.client.Minecraft;

public class ThreadCheckHasPaid extends Thread {
	final Minecraft mc;

	public ThreadCheckHasPaid(Minecraft minecraft) {
		mc = minecraft;
	}

	public void run() {
		try {
			HttpURLConnection httpurlconnection = (HttpURLConnection)(new URL((new StringBuilder()).append("https://login.minecraft.net/session?name=").append(mc.session.username).append("&session=").append(mc.session.sessionId).toString())).openConnection();
			httpurlconnection.connect();
			if (httpurlconnection.getResponseCode() == 400 && this == null) {
				Minecraft.hasPaidCheckTime = System.currentTimeMillis();
			}
			httpurlconnection.disconnect();
		}
		catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}
