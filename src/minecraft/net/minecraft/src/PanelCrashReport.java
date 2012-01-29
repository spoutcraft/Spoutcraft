package net.minecraft.src;

import java.awt.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.lwjgl.Sys;
import org.lwjgl.opengl.GL11;

public class PanelCrashReport extends Panel {
	public PanelCrashReport(UnexpectedThrowable unexpectedthrowable) {
		setBackground(new Color(0x2e3444));
		setLayout(new BorderLayout());
		StringWriter stringwriter = new StringWriter();
		unexpectedthrowable.exception.printStackTrace(new PrintWriter(stringwriter));
		String s = stringwriter.toString();
		String s1 = "";
		String s2 = "";
		try {
			s2 = (new StringBuilder()).append(s2).append("Generated ").append((new SimpleDateFormat()).format(new Date())).append("\n").toString();
			s2 = (new StringBuilder()).append(s2).append("\n").toString();
			s2 = (new StringBuilder()).append(s2).append("Minecraft: Minecraft 1.1\n").toString();
			s2 = (new StringBuilder()).append(s2).append("OS: ").append(System.getProperty("os.name")).append(" (").append(System.getProperty("os.arch")).append(") version ").append(System.getProperty("os.version")).append("\n").toString();
			s2 = (new StringBuilder()).append(s2).append("Java: ").append(System.getProperty("java.version")).append(", ").append(System.getProperty("java.vendor")).append("\n").toString();
			s2 = (new StringBuilder()).append(s2).append("VM: ").append(System.getProperty("java.vm.name")).append(" (").append(System.getProperty("java.vm.info")).append("), ").append(System.getProperty("java.vm.vendor")).append("\n").toString();
			s2 = (new StringBuilder()).append(s2).append("LWJGL: ").append(Sys.getVersion()).append("\n").toString();
			s1 = GL11.glGetString(7936 /*GL_VENDOR*/);
			s2 = (new StringBuilder()).append(s2).append("OpenGL: ").append(GL11.glGetString(7937 /*GL_RENDERER*/)).append(" version ").append(GL11.glGetString(7938 /*GL_VERSION*/)).append(", ").append(GL11.glGetString(7936 /*GL_VENDOR*/)).append("\n").toString();
		}
		catch (Throwable throwable) {
			s2 = (new StringBuilder()).append(s2).append("[failed to get system properties (").append(throwable).append(")]\n").toString();
		}
		s2 = (new StringBuilder()).append(s2).append("\n").toString();
		s2 = (new StringBuilder()).append(s2).append(s).toString();
		String s3 = "";
		s3 = (new StringBuilder()).append(s3).append("\n").toString();
		s3 = (new StringBuilder()).append(s3).append("\n").toString();
		if (s.contains("Pixel format not accelerated")) {
			s3 = (new StringBuilder()).append(s3).append("      Bad video card drivers!      \n").toString();
			s3 = (new StringBuilder()).append(s3).append("      -----------------------      \n").toString();
			s3 = (new StringBuilder()).append(s3).append("\n").toString();
			s3 = (new StringBuilder()).append(s3).append("Minecraft was unable to start because it failed to find an accelerated OpenGL mode.\n").toString();
			s3 = (new StringBuilder()).append(s3).append("This can usually be fixed by updating the video card drivers.\n").toString();
			if (s1.toLowerCase().contains("nvidia")) {
				s3 = (new StringBuilder()).append(s3).append("\n").toString();
				s3 = (new StringBuilder()).append(s3).append("You might be able to find drivers for your video card here:\n").toString();
				s3 = (new StringBuilder()).append(s3).append("  http://www.nvidia.com/\n").toString();
			}
			else if (s1.toLowerCase().contains("ati")) {
				s3 = (new StringBuilder()).append(s3).append("\n").toString();
				s3 = (new StringBuilder()).append(s3).append("You might be able to find drivers for your video card here:\n").toString();
				s3 = (new StringBuilder()).append(s3).append("  http://www.amd.com/\n").toString();
			}
		}
		else {
			s3 = (new StringBuilder()).append(s3).append("      Minecraft has crashed!      \n").toString();
			s3 = (new StringBuilder()).append(s3).append("      ----------------------      \n").toString();
			s3 = (new StringBuilder()).append(s3).append("\n").toString();
			s3 = (new StringBuilder()).append(s3).append("Minecraft has stopped running because it encountered a problem.\n").toString();
			s3 = (new StringBuilder()).append(s3).append("\n").toString();
			s3 = (new StringBuilder()).append(s3).append("If you wish to report this, please copy this entire text and email it to support@mojang.com.\n").toString();
			s3 = (new StringBuilder()).append(s3).append("Please include a description of what you did when the error occured.\n").toString();
		}
		s3 = (new StringBuilder()).append(s3).append("\n").toString();
		s3 = (new StringBuilder()).append(s3).append("\n").toString();
		s3 = (new StringBuilder()).append(s3).append("\n").toString();
		s3 = (new StringBuilder()).append(s3).append("--- BEGIN ERROR REPORT ").append(Integer.toHexString(s3.hashCode())).append(" --------\n").toString();
		s3 = (new StringBuilder()).append(s3).append(s2).toString();
		s3 = (new StringBuilder()).append(s3).append("--- END ERROR REPORT ").append(Integer.toHexString(s3.hashCode())).append(" ----------\n").toString();
		s3 = (new StringBuilder()).append(s3).append("\n").toString();
		s3 = (new StringBuilder()).append(s3).append("\n").toString();
		TextArea textarea = new TextArea(s3, 0, 0, 1);
		textarea.setFont(new Font("Monospaced", 0, 12));
		add(new CanvasMojangLogo(), "North");
		add(new CanvasCrashReport(80), "East");
		add(new CanvasCrashReport(80), "West");
		add(new CanvasCrashReport(100), "South");
		add(textarea, "Center");
	}
}
