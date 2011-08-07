package org.getspout.spout.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.src.Packet;
import org.getspout.spout.gui.Color;

public abstract class PacketUtil {
	public static final int maxString = 32767;
	
	public static void writeString(DataOutputStream output, String string) {
		try {
			Packet.writeString(string, output);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String readString(DataInputStream input) {
		return readString(input, maxString);
	}
	
	public static int getNumBytes(String str) {
		return 2 + str.length() * 2;
	}
	
	public static String readString(DataInputStream input, int maxSize) {
		try {
			return Packet.readString(input, maxSize);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
    
    public static void writeColor(DataOutputStream output, Color color) {
        try {
			output.writeFloat(color.getRedF());
			output.writeFloat(color.getGreenF());
			output.writeFloat(color.getBlueF());
			output.writeFloat(color.getAlphaF());
		} catch(IOException e) {
			e.printStackTrace();
		}
    }
    
    public static Color readColor(DataInputStream input) {
		try {
			float r,g,b,a;
			r = input.readFloat();
			g = input.readFloat();
			b = input.readFloat();
			a = input.readFloat();
			return new Color(r,g,b,a);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
    }

}
