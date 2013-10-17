package net.minecraft.src;

import java.awt.image.BufferedImage;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.imageio.ImageIO;

class ThreadDownloadImageDataINNER1 extends Thread {
	final ThreadDownloadImageData theThreadDownloadImageData;

	ThreadDownloadImageDataINNER1(ThreadDownloadImageData par1ThreadDownloadImageData) {
		this.theThreadDownloadImageData = par1ThreadDownloadImageData;
	}

	public void run() {
		HttpURLConnection var1 = null;

		try {
			// Spout Start
			HttpURLConnection.setFollowRedirects(true);
			// Spout End
			var1 = (HttpURLConnection)(new URL(ThreadDownloadImageData.getImageUrl(this.theThreadDownloadImageData))).openConnection(Minecraft.getMinecraft().getProxy());
			// Spout Start
			System.setProperty("http.agent", "");
			var1.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/534.30 (KHTML, like Gecko) Chrome/12.0.742.100 Safari/534.30");
			// Spout End
			var1.setDoInput(true);
			var1.setDoOutput(false);
			var1.connect();

			if (var1.getResponseCode() / 100 == 2) {
				BufferedImage var2 = ImageIO.read(var1.getInputStream());

				if (ThreadDownloadImageData.getImageBuffer(this.theThreadDownloadImageData) != null) {
					var2 = ThreadDownloadImageData.getImageBuffer(this.theThreadDownloadImageData).parseUserSkin(var2);
				}

				this.theThreadDownloadImageData.getBufferedImage(var2);
				return;
			}
		} catch (Exception var6) {
			var6.printStackTrace();
			return;
		} finally {
			if (var1 != null) {
				var1.disconnect();
			}
		}
	}
}
