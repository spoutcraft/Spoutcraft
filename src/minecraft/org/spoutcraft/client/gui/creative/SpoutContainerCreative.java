/*
 * This file is part of Spoutcraft (http://www.spout.org/).
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
package org.spoutcraft.client.gui.creative;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.GuiInventory;
import net.minecraft.src.Packet107CreativeSetSlot;
import net.minecraft.src.PlayerControllerMP;
import net.minecraft.src.Slot;
import org.spoutcraft.spoutcraftapi.Client;
import org.spoutcraft.spoutcraftapi.Client.Mode;
import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.addon.Addon;
import org.spoutcraft.spoutcraftapi.inventory.ItemStack;
import org.spoutcraft.spoutcraftapi.material.Material;
import org.spoutcraft.spoutcraftapi.material.MaterialData;

public class SpoutContainerCreative extends GuiInventory {

	private static int page = 1;
	private static final Set<Material> gamed = new HashSet<Material>();
	private List<Material> materials;
	private List<CreativeSlot> slots = new ArrayList<CreativeSlot>();
	private NextButton next;
	private PrevButton prev;
	private int startId;

	static {
		gamed.add(MaterialData.redstoneRepeaterOff);
		gamed.add(MaterialData.redstoneRepeaterOn);
		gamed.add(MaterialData.bedBlock);
		gamed.add(MaterialData.movedByPiston);
	}

	public SpoutContainerCreative(EntityPlayer eplayer) {
		super(eplayer);
	}

	@Override
	public void drawScreen(int par1, int par2, float par3) {
		super.drawScreen(par1, par2, par3);
	}

	@Override
	public void initGui() {
		super.initGui();
		Addon addon = Spoutcraft.getAddonManager().getAddon("Spoutcraft");
		slots.clear();
		materials = MaterialData.getMaterials();
		int y = 10;
		for (int i = 1; i <= 11; i++) {
			int x = width - 15 * 8;
			for (int j = 1; j <= 6; j++) {
				CreativeSlot cslot = new CreativeSlot();
				cslot.setGeometry(x, y, 15, 15);
				slots.add(cslot);
				cslot.setItem(new ItemStack(MaterialData.stone, 1));
				getScreen().attachWidget(addon, cslot);
				x += 18;
			}
			y += 18;
		}

		prev = new PrevButton(this);
		prev.setText("Prev");
		prev.setGeometry(width - 15 * 8, y, 30, 20);
		prev.setVisible(false);
		getScreen().attachWidget(addon, prev);

		next = new NextButton(this);
		next.setText("Next");
		next.setGeometry(width - 15 * 8 + 18 * 4, y, 30, 20);
		getScreen().attachWidget(addon, next);
		refreshPage();
	}

	public void refreshPage() {
		startId = (page - 1) * 66 + 1;
		for (CreativeSlot slot : slots) {
			check(slot);
			startId++;
		}
	}

	public void nextPage() {
		prev.setVisible(true);
		page++;
		if (page == materials.size() / 66 + 1) {
			next.setVisible(false);
		}
		refreshPage();
	}

	public void previousPage() {
		next.setVisible(true);
		page--;
		if (page == 1) {
			prev.setVisible(false);
		}
		refreshPage();
	}

	private void check(CreativeSlot slot) {
		if (startId >= materials.size()) {
			slot.setVisible(false);
			return;
		}
		if (gamed.contains(materials.get(startId))) {
			startId++;
			check(slot);
			return;
		}
		slot.setVisible(true);
		try {
			slot.setSpecialItem(new ItemStack(materials.get(startId), 1, (short) materials.get(startId).getRawData()), materials.get(startId).getName());
		} catch (Exception ex) {
			System.out.println("Error adding: " + materials.get(startId).getName() + "!");
			ex.printStackTrace();

		}
	}

	@Override
	protected void handleMouseClick(Slot par1Slot, int par2, int par3, boolean par4) {
		super.handleMouseClick(par1Slot, par2, par3, par4);
		if(par1Slot == null) return;
		if(par2 == -1) return;
		if (Minecraft.theMinecraft.playerController instanceof PlayerControllerMP) {
			Packet107CreativeSetSlot packet = new Packet107CreativeSetSlot(par2, par1Slot.getStack());
			((EntityClientPlayerMP) Minecraft.theMinecraft.thePlayer).sendQueue.addToSendQueue(packet);
		}
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
	}
}
