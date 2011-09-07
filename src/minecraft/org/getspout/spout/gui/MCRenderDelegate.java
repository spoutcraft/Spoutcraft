package org.getspout.spout.gui;

import java.util.HashMap;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.Gui;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiIngame;
import net.minecraft.src.Item;
import net.minecraft.src.Material;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.RenderHelper;
import net.minecraft.src.RenderManager;
import net.minecraft.src.ScaledResolution;
import net.minecraft.src.Tessellator;

import org.getspout.spout.client.SpoutClient;
import org.getspout.spout.io.CustomTextureManager;
import org.lwjgl.opengl.GL11;
import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.gui.*;

public class MCRenderDelegate implements RenderDelegate{
	protected final RenderItemCustom renderer;
	protected HashMap<UUID, GuiButton> customFields = new HashMap<UUID, GuiButton>();
	public MCRenderDelegate() {
		renderer = new RenderItemCustom();
		renderer.setRenderManager(RenderManager.instance);
	}

	public void downloadTexture(String plugin, String url) {
		CustomTextureManager.downloadTexture(plugin, url);
	}

	public int getScreenHeight() {
		ScaledResolution resolution = new ScaledResolution(SpoutClient.getHandle().gameSettings, SpoutClient.getHandle().displayWidth, SpoutClient.getHandle().displayHeight);
		return resolution.getScaledHeight();
	}

	public int getScreenWidth() {
		ScaledResolution resolution = new ScaledResolution(SpoutClient.getHandle().gameSettings, SpoutClient.getHandle().displayWidth, SpoutClient.getHandle().displayHeight);
		return resolution.getScaledWidth();
	}

	public int getTextWidth(String text) {
		return Minecraft.theMinecraft.fontRenderer.getStringWidth(text);
	}

	public void render(ArmorBar bar) {
		float armorPercent = Minecraft.theMinecraft.thePlayer.getPlayerArmorValue() / 0.2f;
		if (bar.isVisible() && bar.getMaxNumShields() > 0) {
			int y = (int)bar.getScreenY();
			float armorPercentPerIcon = 100f / bar.getMaxNumShields();
			for (int icon = 0; icon < bar.getMaxNumShields(); icon++) {
				if (armorPercent > 0 || bar.isAlwaysVisible()) {
					int x = (int)bar.getScreenX() - icon * bar.getIconOffset();
					boolean full = (icon + 1) * armorPercentPerIcon <= armorPercent;
					boolean half = (icon + 1) * armorPercentPerIcon < armorPercent + armorPercentPerIcon;
					if (full) { //white armor (filled in)
						Gui.drawStaticTexturedModalRect(x, y, 34, 9, 9, 9, 0f);
					}
					else if (half) { //half filled in
						Gui.drawStaticTexturedModalRect(x, y, 25, 9, 9, 9, 0f);
					}
					else {
						Gui.drawStaticTexturedModalRect(x, y, 16, 9, 9, 9, 0f);
					}
				}
			}
		}
	}

	public void render(BubbleBar bar) {
		if(Minecraft.theMinecraft.thePlayer.isInsideOfMaterial(Material.water)) {
			int bubbles = (int)Math.ceil(((double)(Minecraft.theMinecraft.thePlayer.air - 2) * (double)bar.getMaxNumBubbles()) / (Minecraft.theMinecraft.thePlayer.maxAir * 1D));
			int poppingBubbles = (int)Math.ceil(((double)Minecraft.theMinecraft.thePlayer.air * (double)bar.getMaxNumBubbles()) / (Minecraft.theMinecraft.thePlayer.maxAir * 1D)) - bubbles;
			if (bar.isVisible()) {
				for(int bubble = 0; bubble < bubbles + poppingBubbles; bubble++) {
					if(bubble < bubbles) {
						Gui.drawStaticTexturedModalRect((int)bar.getScreenX() + bubble * bar.getIconOffset(), (int)bar.getScreenY(), 16, 18, 9, 9, 0f);
					} else {
						Gui.drawStaticTexturedModalRect((int)bar.getScreenX() + bubble * bar.getIconOffset(), (int)bar.getScreenY(), 25, 18, 9, 9, 0f);
					}
				}
			}
		}
	}

	public void render(GenericButton button) {
		if(button.isVisible()) {
			FontRenderer font = Minecraft.theMinecraft.fontRenderer;
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, Minecraft.theMinecraft.renderEngine.getTexture("/gui/gui.png"));
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glTranslatef((float) button.getScreenX(), (float) button.getScreenY(), 0);
			float width = (float) (button.getWidth() < 200 ? button.getWidth() : 200);
			GL11.glScalef((float) button.getWidth() / width, (float) button.getHeight() / 20f, 1);
			
			double mouseX = button.getScreen().getMouseX();
			double mouseY = button.getScreen().getMouseY();
			
			boolean hovering = mouseX >= button.getScreenX() && mouseY >= button.getScreenY() && mouseX < button.getScreenX() + button.getWidth() && mouseY < button.getScreenY() + button.getHeight();
			int hoverState = getHoverState(button, hovering);
			Gui.drawStaticTexturedModalRect(0, 0, 0, 46 + hoverState * 20, (int) Math.ceil(width / 2), 20, 0f);
			Gui.drawStaticTexturedModalRect((int) Math.floor(width / 2), 0, 200 - (int) Math.ceil(width / 2), 46 + hoverState * 20, (int) Math.ceil(width / 2), 20, 0f);
			Color color = button.getTextColor();
			if(!button.isEnabled()) {
				color = button.getDisabledColor();
			} else if(hovering) {
				color = button.getHoverColor();
			}
			int left = (int) 5;
			switch (button.getAlign()) {
				case TOP_CENTER:
				case CENTER_CENTER:
				case BOTTOM_CENTER:
					left = (int) ((width / 2) - (font.getStringWidth(button.getText()) / 2)); break;
				case TOP_RIGHT:
				case CENTER_RIGHT:
				case BOTTOM_RIGHT:
					left = (int) (width - font.getStringWidth(button.getText())) - 5; break;
			}
			font.drawStringWithShadow(button.getText(), left, 6, color.toInt());
		}
	}
	
	protected int getHoverState(Control control, boolean hover) {
		int state = 1;
		if (!control.isEnabled()) {
			state = 0;
		}
		else if (hover) {
			state = 2;
		}

		return state;
	}

	public void render(GenericGradient gradient) {
		GL11.glDisable(3553 /*GL_TEXTURE_2D*/);
		GL11.glEnable(3042 /*GL_BLEND*/);
		GL11.glDisable(3008 /*GL_ALPHA_TEST*/);
		GL11.glBlendFunc(770, 771);
		GL11.glShadeModel(7425 /*GL_SMOOTH*/);
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.setColorRGBA_F(gradient.getTopColor().getRedF(), gradient.getTopColor().getGreenF(), gradient.getTopColor().getBlueF(), gradient.getTopColor().getAlphaF());
		tessellator.addVertex(gradient.getWidth() + gradient.getScreenX(), gradient.getScreenY(), 0.0D);
		tessellator.addVertex(gradient.getScreenX(), gradient.getScreenY(), 0.0D);
		tessellator.setColorRGBA_F(gradient.getBottomColor().getRedF(), gradient.getBottomColor().getGreenF(), gradient.getBottomColor().getBlueF(), gradient.getBottomColor().getAlphaF());
		tessellator.addVertex(gradient.getScreenX(), gradient.getHeight() + gradient.getScreenY(), 0.0D);
		tessellator.addVertex(gradient.getWidth() + gradient.getScreenX(), gradient.getHeight() + gradient.getScreenY(), 0.0D);
		tessellator.draw();
		GL11.glShadeModel(7424 /*GL_FLAT*/);
		GL11.glDisable(3042 /*GL_BLEND*/);
		GL11.glEnable(3008 /*GL_ALPHA_TEST*/);
		GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
	}

	public void render(GenericItemWidget item) {
		GL11.glDepthFunc(515);
		RenderHelper.enableStandardItemLighting();
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_COLOR_MATERIAL);
		GL11.glDisable(GL11.GL_LIGHTING);
		double oldX = 1;
		double oldY = 1;
		double oldZ = 1;
		Block block = null;
		if (item.getTypeId() < 255 && RenderBlocks.renderItemIn3d(Block.blocksList[item.getTypeId()].getRenderType())) {
			block = Block.blocksList[item.getTypeId()];
			oldX = block.maxX;
			oldY = block.maxY;
			oldZ = block.maxZ;
			block.maxX = block.maxX * item.getWidth() / 8;
			block.maxY = block.maxY * item.getHeight() / 8;
			block.maxZ = block.maxZ * item.getDepth() / 8;
		}
		else {
			renderer.setScale((item.getWidth() / 8D), (item.getHeight() / 8D), 1);
		}
		GL11.glPushMatrix();
		GL11.glTranslatef((float) item.getScreenX(), (float) item.getScreenY(), 0);
		GL11.glScalef((float) (item.getScreen().getWidth() / 427f), (float) (item.getScreen().getHeight() / 240f), 1);
		renderer.drawItemIntoGui(SpoutClient.getHandle().fontRenderer, SpoutClient.getHandle().renderEngine, item.getTypeId(), item.getData(), Item.itemsList[item.getTypeId()].getIconFromDamage(item.getData()), 0, 0);
		GL11.glPopMatrix();
		if (item.getTypeId() < 255 && RenderBlocks.renderItemIn3d(Block.blocksList[item.getTypeId()].getRenderType())){
			block.maxX = oldX;
			block.maxY = oldY;
			block.maxZ = oldZ;
		}
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_LIGHTING);
		RenderHelper.disableStandardItemLighting();
	}

	public void render(GenericLabel label) {
		FontRenderer font = SpoutClient.getHandle().fontRenderer;
		String lines[] = label.getText().split("\\n");
		
		double swidth = label.getTextWidth();
		double sheight = label.getTextHeight();
		
		GL11.glPushMatrix();
		
		double top = label.getScreenY();
		switch (label.getAlign()) {
			case CENTER_LEFT:
			case CENTER_CENTER:
			case CENTER_RIGHT:
				top -= (int) (label.getAuto() ? label.getActualHeight() : label.getHeight()) / 2; break;
			case BOTTOM_LEFT:
			case BOTTOM_CENTER:
			case BOTTOM_RIGHT:
				top -= (int) (label.getAuto() ? label.getActualHeight() : label.getHeight()); break;
		}
		
		double aleft = label.getScreenX();
		switch (label.getAlign()) {
			case TOP_CENTER:
			case CENTER_CENTER:
			case BOTTOM_CENTER:
				aleft -= (int) (label.getAuto() ? label.getActualWidth() : label.getWidth()) / 2; break;
			case TOP_RIGHT:
			case CENTER_RIGHT:
			case BOTTOM_RIGHT:
				aleft -= (int) (label.getAuto() ? label.getActualWidth() : label.getWidth()); break;
		}
		
		GL11.glTranslatef((float) aleft, (float) top, 0);
		if (!label.getAuto()) {
			GL11.glScalef((float) (label.getWidth() / swidth), (float) (label.getHeight() / sheight), 1);
		} else if (label.getAnchor() == WidgetAnchor.SCALE) {
			GL11.glScalef((float) (label.getScreen().getWidth() / 427f), (float) (label.getScreen().getHeight() / 240f), 1);
		}
		for (int i = 0; i < lines.length; i++) {
			double left = 0;
			switch (label.getAlign()) {
				case TOP_CENTER:
				case CENTER_CENTER:
				case BOTTOM_CENTER:
					left = (swidth / 2) - (font.getStringWidth(lines[i]) / 2); break;
				case TOP_RIGHT:
				case CENTER_RIGHT:
				case BOTTOM_RIGHT:
					left = swidth - font.getStringWidth(lines[i]); break;
			}
			font.drawStringWithShadow(lines[i], (int) left, i * 10, label.getTextColor().toInt());
		}
		GL11.glPopMatrix();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}

	public void render(GenericSlider slider) {
		if(slider.isVisible()) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, Minecraft.theMinecraft.renderEngine.getTexture("/gui/gui.png"));
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			float width = (float) (slider.getWidth() < 200 ? slider.getWidth() : 200);
			GL11.glTranslatef((float) slider.getScreenX(), (float) slider.getScreenY(), 0);
			GL11.glScalef((float) slider.getWidth() / width, (float) slider.getHeight() / 20f, 1);
			
			double mouseX = slider.getScreen().getMouseX();
			double mouseY = slider.getScreen().getMouseY();
			
			boolean hovering = mouseX >= slider.getScreenX() && mouseY >= slider.getScreenY() && mouseX < slider.getScreenX() + slider.getWidth() && mouseY < slider.getScreenY() + slider.getHeight();
			
			int hoverState = getHoverState(slider, hovering);
			Gui.drawStaticTexturedModalRect(0, 0, 0, 46 + hoverState * 20, (int) Math.ceil(width / 2), 20, 0f);
			Gui.drawStaticTexturedModalRect((int) Math.floor(width / 2), 0, 200 - (int) Math.ceil(width / 2), 46 + hoverState * 20, (int) Math.ceil(width / 2), 20, 0f);
			
			if(slider.isDragging()) {
				slider.setSliderPosition((float)(mouseX - (slider.getScreenX() + 4)) / (float)(slider.getWidth() - 8));
			}

			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			width -= 8;
			Gui.drawStaticTexturedModalRect((int) (slider.getSliderPosition() * width), 0, 0, 66, 4, 20, 0f);
			Gui.drawStaticTexturedModalRect((int) (slider.getSliderPosition() * width) + 4, 0, 196, 66, 4, 20, 0f);
		}
	}

	public void render(GenericTextField textField) {
		Gui.drawRect((int) (textField.getScreenX() - 1), (int) (textField.getScreenY() - 1), (int) (textField.getScreenX() + textField.getWidth() + 1), (int) (textField.getScreenY() + textField.getHeight() + 1), textField.getBorderColor().toInt());
		Gui.drawRect((int)textField.getScreenX(), (int) textField.getScreenY(), (int) (textField.getScreenX() + textField.getWidth()), (int) (textField.getScreenY() + textField.getHeight()), textField.getFieldColor().toInt());
		if(textField.isEnabled()) {
			boolean showCursor = textField.isFocus() && Spoutcraft.getClient().getTick() % 40 < 15;
			String text = textField.getText();
			if (textField.getCursorPosition() < 0) textField.setCursorPosition(0);
			if (textField.getCursorPosition() > text.length()) textField.setCursorPosition(text.length());
			if (showCursor) {
				text = "";
				if (textField.getCursorPosition() > 0) {
					text += textField.getText().substring(0, textField.getCursorPosition());
				}
				text += "_";
				if (textField.getCursorPosition() < textField.getText().length()) {
					text += textField.getText().substring(textField.getCursorPosition() + 1);
				}
			}
			Minecraft.theMinecraft.fontRenderer.drawStringWithShadow(text, (int) (textField.getScreenX() + 4), (int) (textField.getScreenY() + (textField.getHeight() - 8) / 2), textField.getColor().toInt());
		} else {
			Minecraft.theMinecraft.fontRenderer.drawStringWithShadow(textField.getText(), (int) (textField.getScreenX() + 4), (int) (textField.getScreenY() + (textField.getHeight() - 8) / 2), textField.getDisabledColor().toInt());
		}
	}

	public void render(GenericTexture texture) {
		org.newdawn.slick.opengl.Texture textureBinding = CustomTextureManager.getTextureFromUrl(texture.getPlugin(), texture.getUrl());
		if (textureBinding != null) {
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glDepthMask(false);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glTranslatef((float) texture.getScreenX(), (float) texture.getScreenY(), 0); //moves texture into place
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureBinding.getTextureID());
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			Tessellator tessellator = Tessellator.instance;
			tessellator.startDrawingQuads();
			tessellator.addVertexWithUV(0.0D, texture.getHeight(), -90, 0.0D, 0.0D); //draw corners
			tessellator.addVertexWithUV(texture.getWidth(), texture.getHeight(), -90, textureBinding.getWidth(), 0.0D);
			tessellator.addVertexWithUV(texture.getWidth(), 0.0D, -90, textureBinding.getWidth(), textureBinding.getHeight());
			tessellator.addVertexWithUV(0.0D, 0.0D, -90, 0.0D, textureBinding.getHeight());
			tessellator.draw();
			GL11.glDepthMask(true);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		}
	}

	public void render(HealthBar bar) {
		int health = Minecraft.theMinecraft.thePlayer.health;
		boolean whiteOutlinedHearts = Minecraft.theMinecraft.thePlayer.heartsLife / 3 % 2 == 1;
		if(Minecraft.theMinecraft.thePlayer.heartsLife < 10) {
			whiteOutlinedHearts = false;
		}
		float healthPercent = health / 0.2f;
		int y = (int)bar.getScreenY();
		if (healthPercent <= bar.getDangerPercent()) {
			y += GuiIngame.rand.nextInt(2);
		}
		float healthPercentPerIcon = 100f / bar.getMaxNumHearts();
		if (bar.isVisible() && bar.getMaxNumHearts() > 0) {
			for (int icon = 0; icon < bar.getMaxNumHearts(); ++icon) {
				boolean full = (icon + 1) * healthPercentPerIcon <= healthPercent;
				boolean half = (icon + 1) * healthPercentPerIcon < healthPercent + healthPercentPerIcon;
				int x = (int)bar.getScreenX() + icon * bar.getIconOffset();
				
				Gui.drawStaticTexturedModalRect(x, y, 16 + (whiteOutlinedHearts ? 1 : 0) * 9, 0, 9, 9, 0f);
				if (whiteOutlinedHearts) {
					if (full) {
						Gui.drawStaticTexturedModalRect(x, y, 70, 0, 9, 9, 0f);
					}
					else if (half) {
						Gui.drawStaticTexturedModalRect(x, y, 79, 0, 9, 9, 0f);
					}
				}

				if (full) {
					Gui.drawStaticTexturedModalRect(x, y, 52, 0, 9, 9, 0f);
				}
				else if (half) {
					Gui.drawStaticTexturedModalRect(x, y, 61, 0, 9, 9, 0f);
				}

			}
		}
	}
	
	public void render(GenericEntityWidget entityWidget) {
		Entity entity = SpoutClient.getInstance().getEntityFromId(entityWidget.getEntityID());
		if (entity != null) {
			GL11.glEnable(32826 /*GL_RESCALE_NORMAL_EXT*/);
	        GL11.glEnable(2903 /*GL_COLOR_MATERIAL*/);
			GL11.glPushMatrix();
			GL11.glTranslated(entityWidget.getX() + entityWidget.getWidth()/2, entityWidget.getY() + entityWidget.getHeight(), 50F);
			RenderHelper.enableStandardItemLighting();
	        float f1 = (float) Math.min(entityWidget.getWidth(), entityWidget.getHeight());
	        GL11.glScalef(-f1, f1, f1);
	        GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
	        GL11.glRotatef(entity.prevRotationYaw, 0, 1.0F, 0);
			RenderHelper.enableStandardItemLighting();
	        RenderManager.instance.playerViewY = 180F;
	        RenderManager.instance.renderEntityWithPosYaw(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
			GL11.glPopMatrix();
			RenderHelper.disableStandardItemLighting();
	        GL11.glDisable(32826 /*GL_RESCALE_NORMAL_EXT*/);
		}
	}
}
