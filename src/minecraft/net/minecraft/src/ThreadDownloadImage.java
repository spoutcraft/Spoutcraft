package net.minecraft.src;

import java.awt.image.BufferedImage;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.imageio.ImageIO;

class ThreadDownloadImage extends Thread {

	/** The URL of the image to download. */
	final String location;

	/** The image buffer to use. */
	final ImageBuffer buffer;

	/** The image data. */
	final ThreadDownloadImageData imageData;

	ThreadDownloadImage(ThreadDownloadImageData par1ThreadDownloadImageData, String par2Str, ImageBuffer par3ImageBuffer) {
		this.imageData = par1ThreadDownloadImageData;
		this.location = par2Str;
		this.buffer = par3ImageBuffer;
	}

	public void run() {
		HttpURLConnection var1 = null;

		try {
			//Spout start
			HttpURLConnection.setFollowRedirects(true);
			//Spout end
			URL var2 = new URL(this.location);
			var1 = (HttpURLConnection)var2.openConnection();
			//Spout Start
			System.setProperty("http.agent", "");
			var1.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/534.30 (KHTML, like Gecko) Chrome/12.0.742.100 Safari/534.30");
			//Spout End
			var1.setDoInput(true);
			var1.setDoOutput(false);
			var1.connect();

			if (var1.getResponseCode() / 100 == 4) {
				return;
			}

			if (this.buffer == null) {
				this.imageData.image = ImageIO.read(var1.getInputStream());
			} else {
				//Spout start
				BufferedImage image = ImageIO.read(var1.getInputStream());
				if (image != null) {
					this.imageData.image = this.buffer.parseUserSkin(image);
				}
				else {
					//System.out.println("No image data found for " + location);
				}
				//Spout end
			}
		} catch (Exception var6) {
			//var6.printStackTrace(); //Spout
		} finally {
			var1.disconnect();
		}
	}
}
