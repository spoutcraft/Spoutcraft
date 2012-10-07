/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011-2012, Spout LLC <http://www.spout.org/>
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
package org.spoutcraft.api.gui;

public interface RenderDelegate {
	public void render(ArmorBar bar);

	public void render(BubbleBar bar);

	public void render(ExpBar bar);

	public void render(GenericButton button);

	public void render(GenericGradient gradient);

	public void render(GenericItemWidget item);

	public void render(GenericLabel label);

	public void render(GenericSlider slider);

	public void render(GenericTextField text);

	public void render(GenericTexture texture);

	public void render(HealthBar bar);

	public void render(HungerBar bar);

	public void render(GenericEntityWidget entityWidget);

	public void render(GenericRadioButton radio);

	public void render(GenericCheckBox check);

	public void render(GenericBitmap bitmap);

	public void downloadTexture(String plugin, String url);

	public int getScreenWidth();

	public int getScreenHeight();

	public MinecraftFont getMinecraftFont();

	public MinecraftTessellator getTessellator();

	void render(GenericListWidgetItem lwi, int x, int y, int width, int height);

	public void render(GenericScrollable genericScrollable);

	public void renderContents(GenericListWidget genericListWidget);

	public void renderContents(GenericScrollArea genericScrollArea);

	public void render(GenericComboBox comboBox);

	public void render(GenericSlot genericSlot);

	public String getFittingText(String text, int width);

	public boolean bindTexture(String path);

	public boolean bindTexture(String addon, String path);
}
