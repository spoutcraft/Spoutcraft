package com.pclewis.mcpatcher.mod;

import com.pclewis.mcpatcher.TexturePackAPI;
import com.pclewis.mcpatcher.mod.CustomAnimation$Delegate;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import org.lwjgl.opengl.GL11;

class CustomAnimation$Tile implements CustomAnimation$Delegate {
	private final int minScrollDelay;
	private final int maxScrollDelay;
	private final boolean isScrolling;

	final CustomAnimation this$0;

	CustomAnimation$Tile(CustomAnimation var1, int var2, int var3) throws IOException {
		this.this$0 = var1;
		this.minScrollDelay = var2;
		this.maxScrollDelay = var3;
		this.isScrolling = this.minScrollDelay >= 0;
		BufferedImage var4 = TexturePackAPI.getImage(CustomAnimation.access$000(var1));
		int[] var5 = new int[CustomAnimation.access$100(var1) * CustomAnimation.access$200(var1)];
		byte[] var6 = new byte[4 * CustomAnimation.access$100(var1) * CustomAnimation.access$200(var1)];
		var4.getRGB(CustomAnimation.access$300(var1), CustomAnimation.access$400(var1), CustomAnimation.access$100(var1), CustomAnimation.access$200(var1), var5, 0, CustomAnimation.access$100(var1));
		CustomAnimation.ARGBtoRGBA(var5, var6);
		CustomAnimation.access$500(var1).put(var6);
	}

	public void update(int var1, int var2, int var3) {
		if (this.isScrolling) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, var1);
			int var4 = CustomAnimation.access$200(this.this$0) - CustomAnimation.access$600(this.this$0);
			GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, CustomAnimation.access$300(this.this$0) + var2, CustomAnimation.access$400(this.this$0) + var3 + CustomAnimation.access$200(this.this$0) - var4, CustomAnimation.access$100(this.this$0), var4, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer)CustomAnimation.access$500(this.this$0).position(0));
			GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, CustomAnimation.access$300(this.this$0) + var2, CustomAnimation.access$400(this.this$0) + var3, CustomAnimation.access$100(this.this$0), CustomAnimation.access$200(this.this$0) - var4, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer)CustomAnimation.access$500(this.this$0).position(4 * CustomAnimation.access$100(this.this$0) * var4));
		}
	}

	public int getDelay() {
		return this.maxScrollDelay > 0 ? CustomAnimation.access$700().nextInt(this.maxScrollDelay - this.minScrollDelay + 1) + this.minScrollDelay : 0;
	}
}
