package org.spoutcraft.client.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.src.ScreenShotHelper;

import org.spoutcraft.client.client.SpoutClient;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketScreenshot implements SpoutPacket {
    byte[] ssAsPng = null;
    boolean isRequest = false;

    public PacketScreenshot() {
        isRequest = true;
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
            return 1;
        }
        return ssAsPng.length + 5;
    }

    public void readData(DataInputStream input) throws IOException {
        isRequest = input.readBoolean();
        if (!isRequest) {
            int ssLen = input.readInt();
            ssAsPng = new byte[ssLen];
            input.readFully(ssAsPng);
        }
    }

    public void writeData(DataOutputStream output) throws IOException {
        if (ssAsPng == null) {
            output.writeBoolean(true);
        } else {
            output.writeBoolean(false);
            output.writeInt(ssAsPng.length);
            output.write(ssAsPng);
        }
    }

    public void run(int playerId) {
        if (!isRequest) {
            return; // we can't do anything!
        }
        try {
            SpoutClient.getInstance().getActivePlayer().showAchievement("Sending screenshot...", "Screenshot requested", 321);
            BufferedImage screenshot = ScreenShotHelper.getScreenshot(Minecraft.theMinecraft.displayWidth, Minecraft.theMinecraft.displayHeight);
            PacketScreenshot packet = new PacketScreenshot(screenshot);
            SpoutClient.getInstance().getPacketManager().sendSpoutPacket(packet);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            SpoutClient.getInstance().getActivePlayer().showAchievement("Sending screenshot...", "Failed!", 321);
        }

    }

    public void failure(int playerId) {
    }

    public PacketType getPacketType() {
        return PacketType.PacketScreenshot;
    }

    public int getVersion() {
        return 1;
    }
}
