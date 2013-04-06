/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011 Spout LLC <http://www.spout.org/>
 * Spoutcraft is licensed under the GNU Lesser General Public License.
 *
 * Spoutcraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spoutcraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.spoutcraft.client.gui.texturepacks;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.ITexturePack;
import net.minecraft.src.StringTranslate;
import net.minecraft.src.TexturePackCustom;
import net.minecraft.src.TexturePackList;

import org.bukkit.ChatColor;

import org.spoutcraft.api.Spoutcraft;
import org.spoutcraft.api.gui.Button;
import org.spoutcraft.api.gui.GenericButton;
import org.spoutcraft.api.gui.GenericLabel;
import org.spoutcraft.api.gui.GenericListView;
import org.spoutcraft.api.gui.Label;
import org.spoutcraft.client.SpoutClient;

public class GuiTexturePacks extends GuiScreen {
	private GenericListView view;
	private Label screenTitle;
	private Label loadingTexture;
	private Button buttonDone, buttonOpenFolder, buttonSelect, buttonDelete;
	private boolean instancesCreated = false;
	private TexturePacksModel model = SpoutClient.getInstance().getTexturePacksModel();

	private void createInstances() {
		model.setCurrentGui(this);
		if (instancesCreated) {
			return;
		}

		StringTranslate t = StringTranslate.getInstance();

		model.update();
		screenTitle = new GenericLabel(t.translateKey("texturePack.title", "Texture Packs"));
		loadingTexture = new GenericLabel(ChatColor.GREEN + t.translateKey("spout.texturepack.loading", "Loading texture..."));
		view = new GenericListView(model);
		buttonDone = new GenericButton(t.translateKey("gui.done", "Main Menu"));
		buttonOpenFolder = new GenericButton(t.translateKey("texturePack.openFolder", "Open Folder"));
		buttonSelect = new GenericButton(t.translateKey("spout.texturepack.select", "Select"));
		buttonDelete = new DeleteTexturepackButton(this, t.translateKey("spout.texturepack.delete", "Delete"));
	}

	public void initGui() {
		createInstances();

		int top = 10;

		int swidth = mc.fontRenderer.getStringWidth(screenTitle.getText());
		screenTitle.setY(top).setX(width / 2 - swidth / 2).setHeight(11).setWidth(swidth);
		getScreen().attachWidget("Spoutcraft", screenTitle);

		swidth = mc.fontRenderer.getStringWidth(loadingTexture.getText());
		loadingTexture.setVisible(false);
		loadingTexture.setY(top).setX(width / 2 + swidth).setHeight(11).setWidth(swidth);
		getScreen().attachWidget("Spoutcraft", loadingTexture);

		top+=15;

		view.setX(5).setY(top).setWidth(width - 10).setHeight(height - top - 55);
		getScreen().attachWidget("Spoutcraft", view);

		top += 5 + view.getHeight();

		int totalWidth = Math.min(width - 10, 200 * 3 + 10);
		int cellWidth = (totalWidth - 10) / 3;
		int left = width / 2 - totalWidth / 2;
		int center = left + 5 + cellWidth;
		int right = center + 5 + cellWidth;

		buttonSelect.setX(center).setY(top).setWidth(cellWidth).setHeight(20);
		getScreen().attachWidget("Spoutcraft", buttonSelect);

		buttonDelete.setX(left).setY(top).setWidth(cellWidth).setHeight(20);
		getScreen().attachWidget("Spoutcraft", buttonDelete);

		buttonOpenFolder.setX(right).setY(top).setWidth(cellWidth).setHeight(20);
		getScreen().attachWidget("Spoutcraft", buttonOpenFolder);

		top += 25;

		buttonDone.setX(center).setY(top).setWidth(cellWidth).setHeight(20);
		getScreen().attachWidget("Spoutcraft", buttonDone);

		if (!instancesCreated) {
			int selected;
			selected = model.getTextures().indexOf(SpoutClient.getHandle().texturePackList.getSelectedTexturePack());
			view.setSelection(selected);
		}

		instancesCreated = true;
		updateButtons();
	}

	public void drawScreen(int x, int y, float f) {
		drawDefaultBackground();
	}

	@Override
	public void buttonClicked(Button btn) {
		if (btn.equals(buttonDone)) {
			SpoutClient.getHandle().displayGuiScreen(new org.spoutcraft.client.gui.mainmenu.MainMenu());
		}
		if (btn.equals(buttonOpenFolder)) {
			try {
				Desktop.getDesktop().open(SpoutClient.getInstance().getTexturePackFolder());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (btn.equals(buttonSelect) && view.getSelectedRow() != -1) {
			TexturePackItem item = model.getItem(view.getSelectedRow());
			boolean current = item.getPack() == SpoutClient.getHandle().texturePackList.getSelectedTexturePack();
			if (!current) {
				item.select();
				updateButtons();
			} else {
				GuiPreviewTexturePack preview = new GuiPreviewTexturePack(this);
				mc.displayGuiScreen(preview);
			}
		}
	}

	public void updateButtons() {
		try {
			StringTranslate t = StringTranslate.getInstance();
			TexturePackItem item = model.getItem(view.getSelectedRow());
			boolean current = item.getPack() == SpoutClient.getHandle().texturePackList.getSelectedTexturePack();
			buttonSelect.setEnabled(true);
			if (current) {
				buttonSelect.setText(t.translateKey("spout.texturepack.preview.button", "Preview"));
				buttonSelect.setEnabled(false);
				updateScreen();
			} else {
				buttonSelect.setText(t.translateKey("spout.texturepack.select", "Select"));
			}
			buttonDelete.setEnabled(!current && (item.getPack() instanceof TexturePackCustom));
		} catch(Exception e) {}
	}

	public void deleteCurrentTexturepack() {
		for (int tries = 0; tries < 3; tries++) {
			try {
				ITexturePack pack = model.getItem(view.getSelectedRow()).getPack();
				if (pack instanceof TexturePackCustom) {
					TexturePackCustom custom = (TexturePackCustom) pack;
					custom.closeTexturePackFile();
					File d = new File(SpoutClient.getInstance().getTexturePackFolder(), custom.texturePackFileName);
					if (!d.exists()) {
						d = new File(new File(Minecraft.getAppDir("minecraft"), "texturepacks"), custom.texturePackFileName);
					}
					d.setWritable(true);
					FileUtils.forceDelete(d);
					model.update();

					if (!d.exists()) {
						break;
					}

					Thread.sleep(25);
				}
			} catch(Exception e) {
			}
		}
	}

	public void setLoading(boolean newValue) {
		loadingTexture.setVisible(newValue);
	}
}
