package net.minecraft.client.main;

import java.io.File;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.util.List;

import joptsimple.ArgumentAcceptingOptionSpec;
import joptsimple.NonOptionArgumentSpec;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import net.minecraft.src.GameWindowListener;
import net.minecraft.src.MainProxyAuthenticator;
import net.minecraft.src.MainShutdownHook;
import net.minecraft.src.Minecraft;
import net.minecraft.src.Session;

import org.spoutcraft.client.SpoutClient;

public class Main {
	public static void main(String[] par0ArrayOfStr) {
		System.setProperty("java.net.preferIPv4Stack", "true");
		OptionParser var1 = new OptionParser();
		var1.allowsUnrecognizedOptions();
		var1.accepts("demo");
		var1.accepts("fullscreen");
		ArgumentAcceptingOptionSpec var2 = var1.accepts("server").withRequiredArg();
		ArgumentAcceptingOptionSpec var3 = var1.accepts("port").withRequiredArg().ofType(Integer.class).defaultsTo(Integer.valueOf(25565), new Integer[0]);
		ArgumentAcceptingOptionSpec var4 = var1.accepts("gameDir").withRequiredArg().ofType(File.class).defaultsTo(new File("."), new File[0]);
		ArgumentAcceptingOptionSpec var5 = var1.accepts("assetsDir").withRequiredArg().ofType(File.class);
		ArgumentAcceptingOptionSpec var6 = var1.accepts("resourcePackDir").withRequiredArg().ofType(File.class);
		ArgumentAcceptingOptionSpec var7 = var1.accepts("proxyHost").withRequiredArg();
		ArgumentAcceptingOptionSpec var8 = var1.accepts("proxyPort").withRequiredArg().defaultsTo("8080", new String[0]).ofType(Integer.class);
		ArgumentAcceptingOptionSpec var9 = var1.accepts("proxyUser").withRequiredArg();
		ArgumentAcceptingOptionSpec var10 = var1.accepts("proxyPass").withRequiredArg();
		ArgumentAcceptingOptionSpec var11 = var1.accepts("username").withRequiredArg().defaultsTo("Player" + Minecraft.getSystemTime() % 1000L, new String[0]);
		ArgumentAcceptingOptionSpec var12 = var1.accepts("session").withRequiredArg();
		ArgumentAcceptingOptionSpec var13 = var1.accepts("version").withRequiredArg().required();
		ArgumentAcceptingOptionSpec var14 = var1.accepts("width").withRequiredArg().ofType(Integer.class).defaultsTo(Integer.valueOf(854), new Integer[0]);
		ArgumentAcceptingOptionSpec var15 = var1.accepts("height").withRequiredArg().ofType(Integer.class).defaultsTo(Integer.valueOf(480), new Integer[0]);
		NonOptionArgumentSpec var16 = var1.nonOptions();
		OptionSet var17 = var1.parse(par0ArrayOfStr);
		List var18 = var17.valuesOf(var16);
		String var19 = (String)var17.valueOf(var7);
		Proxy var20 = Proxy.NO_PROXY;

		if (var19 != null) {
			try {
				var20 = new Proxy(Type.SOCKS, new InetSocketAddress(var19, ((Integer)var17.valueOf(var8)).intValue()));
			} catch (Exception var34) {
				;
			}
		}

		String var21 = (String)var17.valueOf(var9);
		String var22 = (String)var17.valueOf(var10);

		if (!var20.equals(Proxy.NO_PROXY) && func_110121_a(var21) && func_110121_a(var22)) {
			Authenticator.setDefault(new MainProxyAuthenticator(var21, var22));
		}

		int var23 = ((Integer)var17.valueOf(var14)).intValue();
		int var24 = ((Integer)var17.valueOf(var15)).intValue();
		boolean var25 = var17.has("fullscreen");
		boolean var26 = var17.has("demo");
		String var27 = (String)var17.valueOf(var13);
		File var28 = (File)var17.valueOf(var4);
		File var29 = var17.has(var5) ? (File)var17.valueOf(var5) : new File(var28, "assets/");
		File var30 = var17.has(var6) ? (File)var17.valueOf(var6) : new File(var28, "resourcepacks/");
		Session var31 = new Session((String)var11.value(var17), (String)var12.value(var17));
		Minecraft var32 = new Minecraft(var31, var23, var24, var25, var26, var28, var29, var30, var20, var27);		
		String var33 = (String)var17.valueOf(var2);

		if (var33 != null) {
			var32.setServer(var33, ((Integer)var17.valueOf(var3)).intValue());
		}

		Runtime.getRuntime().addShutdownHook(new MainShutdownHook());

		if (!var18.isEmpty()) {
			System.out.println("Completely ignored arguments: " + var18);
		}

		Thread.currentThread().setName("Minecraft main thread");
		
		// Spout Start
		SpoutClient.getHandle().shutdown();
		try {
			SpoutClient.getHandle().mainThread.join(10000L);
		} catch (InterruptedException var4) {
		}
		System.exit(0);
		// Spout End
		
		var32.func_99999_d();
	}

	private static boolean func_110121_a(String par0Str) {
		return par0Str != null && !par0Str.isEmpty();
	}
}
