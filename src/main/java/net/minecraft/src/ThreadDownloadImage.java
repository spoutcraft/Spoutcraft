package net.minecraft.src;

import java.net.HttpURLConnection;
import java.net.URL;
import javax.imageio.ImageIO;
// Spout Start
import java.awt.image.BufferedImage;
// Spout End

class ThreadDownloadImage extends Thread {

	/** The URL of the image to download. */
	final String location;

	/** The image buffer to use. */
	final IImageBuffer buffer;

	/** The image data. */
	final ThreadDownloadImageData imageData;

	ThreadDownloadImage(ThreadDownloadImageData par1, String par2Str, IImageBuffer par3IImageBuffer) {
		this.imageData = par1;
		this.location = par2Str;
		this.buffer = par3IImageBuffer;
	}

	public void run() {
		HttpURLConnection var1 = null;

		try {
			// Spout Start
			HttpURLConnection.setFollowRedirects(true);
			// Spout End
			URL var2 = new URL(this.location);
			var1 = (HttpURLConnection)var2.openConnection();
			// Spout Start
			System.setProperty("http.agent", "");
			var1.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/534.30 (KHTML, like Gecko) Chrome/12.0.742.100 Safari/534.30");
			// Spout End
			var1.setDoInput(true);
			var1.setDoOutput(false);
			var1.connect();

			if (var1.getResponseCode() / 100 == 4) {
				return;
			}

			if (this.buffer == null) {
				this.imageData.image = ImageIO.read(var1.getInputStream());
			} else {
				// Spout Start
				BufferedImage image = ImageIO.read(var1.getInputStream());
				if (image != null) {
				this.imageData.image = this.buffer.parseUserSkin(image);
				} else {
					//System.out.println("No image data found for " + location);
				}
				// Spout End
			}
		} catch (Exception var6) {
			// Spout Start
			//var6.printStackTrace();
			// Spout End
		} finally {
			var1.disconnect();
		}
	}
}
