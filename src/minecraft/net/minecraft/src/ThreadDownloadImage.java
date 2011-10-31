package net.minecraft.src;

import java.net.HttpURLConnection;
import java.net.URL;
import javax.imageio.ImageIO;
import net.minecraft.src.ImageBuffer;
import net.minecraft.src.ThreadDownloadImageData;

class ThreadDownloadImage extends Thread {

   // $FF: synthetic field
   final String location;
   // $FF: synthetic field
   final ImageBuffer buffer;
   // $FF: synthetic field
   final ThreadDownloadImageData imageData;


   ThreadDownloadImage(ThreadDownloadImageData var1, String var2, ImageBuffer var3) {
      this.imageData = var1;
      this.location = var2;
      this.buffer = var3;
   }

   public void run() {
      HttpURLConnection var1 = null;

      try {
         URL var2 = new URL(this.location);
         var1 = (HttpURLConnection)var2.openConnection();
         System.setProperty("http.agent", ""); //Spoofing the user agent is required to win at life
         var1.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/534.30 (KHTML, like Gecko) Chrome/12.0.742.100 Safari/534.30");
         var1.setDoInput(true);
         var1.setDoOutput(false);
         var1.connect();
         if(var1.getResponseCode() / 100 == 4) {
            return;
         }

         if(this.buffer == null) {
            this.imageData.image = ImageIO.read(var1.getInputStream());
         } else {
            this.imageData.image = this.buffer.parseUserSkin(ImageIO.read(var1.getInputStream()));
         }
      } catch (Exception var6) {
         var6.printStackTrace();
      } finally {
         var1.disconnect();
      }

   }
}
