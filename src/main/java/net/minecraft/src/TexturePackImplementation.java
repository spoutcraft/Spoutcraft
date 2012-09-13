package net.minecraft.src;

import com.pclewis.mcpatcher.MCPatcherUtils; // Spout HD
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.imageio.ImageIO;
import org.lwjgl.opengl.GL11;

public abstract class TexturePackImplementation implements TexturePackBase {
	public final String field_77545_e; // Spout HD private -> public
	private final String field_77542_f;
	public File field_77548_a; // Spout HD protected final -> public
	protected String field_77546_b;
	protected String field_77547_c;
	protected BufferedImage field_77544_d;
	private int field_77543_g;

	protected TexturePackImplementation(String par1Str, String par2Str) {
		this(par1Str, (File)null, par2Str);
	}

	protected TexturePackImplementation(String par1Str, File par2File, String par3Str) {
		this.field_77543_g = -1;
		this.field_77545_e = par1Str;
		this.field_77542_f = par3Str;
		this.field_77548_a = par2File;
		this.func_77539_g();
		this.func_77540_a();
	}

	private static String func_77541_b(String par0Str) {
		if (par0Str != null && par0Str.length() > 34) {
			par0Str = par0Str.substring(0, 34);
		}

		return par0Str;
	}

	private void func_77539_g() {
		InputStream var1 = null;

		try {
			var1 = this.getResourceAsStream("/pack.png");
			this.field_77544_d = ImageIO.read(var1);
		} catch (IOException var11) {
			;
		} finally {
			try {
				var1.close();
			} catch (IOException var10) {
				;
			}
		}
	}

	protected void func_77540_a() {
		InputStream var1 = null;
		BufferedReader var2 = null;

		try {
			var1 = this.getResourceAsStream("/pack.txt");
			var2 = new BufferedReader(new InputStreamReader(var1));
			this.field_77546_b = func_77541_b(var2.readLine());
			this.field_77547_c = func_77541_b(var2.readLine());
		} catch (IOException var12) {
			;
		} finally {
			try {
				var2.close();
				var1.close();
			} catch (IOException var11) {
				;
			}
		}
	}

	public void func_77533_a(RenderEngine par1RenderEngine) {
		if (this.field_77544_d != null && this.field_77543_g != -1) {
			par1RenderEngine.deleteTexture(this.field_77543_g);
		}
	}

	public void func_77535_b(RenderEngine par1RenderEngine) {
		if (this.field_77544_d != null) {
			if (this.field_77543_g == -1) {
				this.field_77543_g = par1RenderEngine.allocateAndSetupTexture(this.field_77544_d);
			}

			par1RenderEngine.bindTexture(this.field_77543_g);
		} else {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, par1RenderEngine.getTexture("/gui/unknown_pack.png"));
		}
	}

	/**
	 * Gives a texture resource as InputStream.
	 */
	public InputStream getResourceAsStream(String par1Str) {
		return TexturePackBase.class.getResourceAsStream(par1Str);
	}

	public String func_77536_b() {
		return this.field_77545_e;
	}

	public String func_77538_c() {
		return this.field_77542_f;
	}

	public String func_77531_d() {
		return this.field_77546_b;
	}

	public String func_77537_e() {
		return this.field_77547_c;
	}

	public int func_77534_f() {
		return 16;
	}
	// Spout HD Start
	public void openTexturePackFile() {
		this.func_77535_b(MCPatcherUtils.getMinecraft().renderEngine);
	}

	public void closeTexturePackFile() {
		this.func_77533_a(MCPatcherUtils.getMinecraft().renderEngine);
	}
	// Spout HD End
}
