package org.getspout.spout.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.src.ScreenShotHelper;
import org.getspout.spout.client.SpoutClient;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


public class PacketScreenshot implements SpoutPacket {
    byte[] ssAsPng = null;

    public PacketScreenshot() {
    }

    public PacketScreenshot(BufferedImage ss) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(ss, "png", baos);
        baos.flush();
        ssAsPng = baos.toByteArray();
        baos.close();
    }

    public int getNumBytes() {
        if (ssAsPng == null) {
            return 0;
        }
        return ssAsPng.length + 4;
    }

    public void readData(DataInputStream input) throws IOException {
    }

    public void writeData(DataOutputStream output) throws IOException {
        output.writeInt(ssAsPng.length);
        output.write(ssAsPng);
    }

    public void run(int playerId) {
        try {
            Minecraft.theMinecraft.ingameGUI.addChatMessage("The server has requested a screenshot of your Minecraft screen.");
            BufferedImage screenshot = ScreenShotHelper.getScreenshot(Minecraft.theMinecraft.displayWidth, Minecraft.theMinecraft.displayHeight);
            PacketScreenshot packet = new PacketScreenshot(screenshot);
            SpoutClient.getInstance().getPacketManager().sendSpoutPacket(packet);
        } catch (IOException ioe) {
            Minecraft.theMinecraft.ingameGUI.addChatMessage("Failed to send screenshot to server.");
        }

    }

    public void failure(int playerId) {
    }

    public PacketType getPacketType() {
        return PacketType.PacketScreenshot;
    }

    public int getVersion() {
        return 0;
    }
}
