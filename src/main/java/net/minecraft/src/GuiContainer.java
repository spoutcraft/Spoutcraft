package net.minecraft.src;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
// Spout Start
import org.spoutcraft.client.MCItemStackComparator;
import org.spoutcraft.client.config.Configuration;
import org.spoutcraft.client.inventory.InventoryUtil;
import org.spoutcraft.api.Spoutcraft;
import org.spoutcraft.api.addon.Addon;
import org.spoutcraft.api.gui.Button;
import org.spoutcraft.api.gui.GenericButton;
import org.spoutcraft.api.gui.GenericCheckBox;
import org.spoutcraft.api.gui.Widget;
import org.spoutcraft.api.material.MaterialData;
// Spout End

public abstract class GuiContainer extends GuiScreen
{
	/** Stacks renderer. Icons, stack size, health, etc... */
	protected static RenderItem itemRenderer;

	/** The X size of the inventory window in pixels. */
	protected int xSize;

	/** The Y size of the inventory window in pixels. */
	protected int ySize;

	/** A list of the players inventory slots. */
	public Container inventorySlots;

	/**
	 * Starting X position for the Gui. Inconsistent use for Gui backgrounds.
	 */
	protected int guiLeft;

	/**
	 * Starting Y position for the Gui. Inconsistent use for Gui backgrounds.
	 */
	protected int guiTop;
	
	// Spout Start
	private Button orderByAlphabet, orderById, replaceTools, replaceBlocks;
	// Spout Start
	
	static {
	    itemRenderer = GuiScreen.ourItemRenderer; // Spout
	}

	public GuiContainer(Container par1Container) {
		xSize = 176;
		ySize = 166;
		inventorySlots = par1Container;
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	@Override
	public void initGui() {
		super.initGui();
		mc.thePlayer.craftingInventory = inventorySlots;
		guiLeft = (width - xSize) / 2;
		guiTop = (height - ySize) / 2;

	// Spout Start	
		if(Spoutcraft.hasPermission("spout.client.sortinventory")) {
			Addon spoutcraft = Spoutcraft.getAddonManager().getAddon("Spoutcraft");
			orderByAlphabet = new GenericButton("Sort A-Z");
			orderByAlphabet.setGeometry(10, 80, 75, 20);
			orderById = new GenericButton("Sort by Id");
			orderById.setGeometry(10, 105, 75, 20);
			orderByAlphabet.setTooltip("Will sort the inventory contents by their name");
			orderById.setTooltip("Will sort the inventory contents by their id");
			
			replaceTools = new GenericCheckBox("Replace tools").setChecked(Configuration.isReplaceTools());
			replaceTools.setGeometry(10, 130, 75, 20);
			replaceTools.setTooltip("Replaces used up tools with spares from your inventory");
			
			replaceBlocks = new GenericCheckBox("Replace blocks").setChecked(Configuration.isReplaceBlocks());
			replaceBlocks.setGeometry(10, 155, 75, 20);
			replaceBlocks.setTooltip("Replaces used up blocks with spares from your inventory");
			
			IInventory inv = inventorySlots.getIInventory();
			if (inv != null && inventorySlots.isSortableInventory()) {
				getScreen().attachWidgets(spoutcraft, orderByAlphabet, orderById, replaceTools, replaceBlocks);
				if (!(inv instanceof InventoryPlayer)) {
					replaceTools.setVisible(false);
					replaceBlocks.setVisible(false);
				}
			}
		}
	}
	
	@Override
	public void drawWidgets(int x, int y, float z) {
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		super.drawWidgets(x, y, z);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}
	
	
	public void buttonClicked(Button btn) {
		if (btn == orderByAlphabet || btn == orderById) {
			try {
				IInventory inv = inventorySlots.getIInventory();
				if (inv != null) {
					if (inv instanceof InventoryPlayer) {
						compactInventory(inv, true);
						sortPlayerInventory(btn == orderByAlphabet);
					}
					else {
						compactInventory(inv, false);
						sortInventory(inv, (btn == orderByAlphabet));
					}
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (btn == replaceTools) {
			Configuration.setReplaceTools(!Configuration.isReplaceTools());
			((GenericCheckBox)replaceTools).setChecked(Configuration.isReplaceTools());
			Configuration.write();
		}
		if (btn == replaceBlocks) {
			Configuration.setReplaceBlocks(!Configuration.isReplaceBlocks());
			((GenericCheckBox)replaceBlocks).setChecked(Configuration.isReplaceBlocks());
			Configuration.write();
		}
	}
	
	public int getNumItems(IInventory inventory) {
		int used = 0;
		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			if (inventory.getStackInSlot(i) != null) {
				used++;
			}
		}
		return used;
	}
	
	public void compactInventory(IInventory inventory, boolean player) {
		//To keep mp compatibility, fake window clicks
		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			ItemStack item = inventory.getStackInSlot(i);
			if (item != null && item.stackSize < item.getMaxStackSize()) {
				
				//Find a place to put this
				int orig = i;
				//Avoid the hotbar
				if (orig < 9 && player) {
					continue;
				}
				handleMouseClick(null, orig, 0, false); //pick up the item
				
				for (int j = 0; j < inventory.getSizeInventory(); j++) {
					if (j != i) {
						ItemStack other = inventory.getStackInSlot(j);
						if (other != null && other.itemID == item.itemID && other.getItemDamage() == item.getItemDamage()) {
							int slot = j;
							//Avoid the hotbar
							if (slot < 9 && player) {
								continue;
							}
							handleMouseClick(null, slot, 0, false); //merge with the existing stack we found
							
							//Move onto the next item to merge if this one is completely used up
							ItemStack cursor = Minecraft.theMinecraft.thePlayer.inventory.getItemStack();
							if (cursor == null) {
								break;
							}
						}
					}
				}
				
				//If we didn't merge all of the item, put it back
				ItemStack cursor = Minecraft.theMinecraft.thePlayer.inventory.getItemStack();
				if (cursor != null) {
					handleMouseClick(null, orig, 0, false);
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void sortPlayerInventory(boolean byName) {
		//To keep mp compatibility, fake window clicks
		InventoryPlayer inventory = Minecraft.theMinecraft.thePlayer.inventory;
		for (int itemPass = 0; itemPass < getNumItems(inventory); itemPass++) {
			for (int pass = 0; pass < inventory.mainInventory.length; pass++) {
				ArrayList<ItemStack> items = new ArrayList<ItemStack>();
				for (int i = 0; i < inventory.mainInventory.length; i++) {
					ItemStack item = inventory.mainInventory[i];
					if (item == null || i < 9) {
						items.add(null);
					}
					else {
						items.add(new PositionedItemStack(item, i));
					}
				}
				Collections.sort(items, new MCItemStackComparator(byName));
				
				while (true) {
					if (items.get(pass) instanceof PositionedItemStack) {
						PositionedItemStack item = (PositionedItemStack) items.get(pass);
						//Left click pick up item
						int origSlot = item.position;
						if (origSlot < 9) {
							break;
						}
						int newSlot = pass;
						if (origSlot != newSlot) {
							//Left click pick up item
							handleMouseClick(null, origSlot, 0, false);
							
							//Left click place item down
							handleMouseClick(null, newSlot, 0, false);
							
							ItemStack cursor = Minecraft.theMinecraft.thePlayer.inventory.getItemStack();
							if (cursor != null) {
								handleMouseClick(null, origSlot, 0, false);
							}
						}
						break;
					}
					pass++;
					if (pass >= inventory.mainInventory.length) {
						break;
					}
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void sortInventory(IInventory inventory, boolean byName) {
		//To keep mp compatibility, fake window clicks
		for (int itemPass = 0; itemPass < getNumItems(inventory); itemPass++) {
			for (int pass = 0; pass < inventory.getSizeInventory(); pass++) {
				ArrayList<ItemStack> items = new ArrayList<ItemStack>();
				for (int i = 0; i < inventory.getSizeInventory(); i++) {
					ItemStack item = inventory.getStackInSlot(i);
					if (item == null) {
						items.add(null);
					}
					else {
						items.add(new PositionedItemStack(item, i));
					}
				}
				Collections.sort(items, new MCItemStackComparator(byName));
				
				while (true) {
					if (items.get(pass) instanceof PositionedItemStack) {
						PositionedItemStack item = (PositionedItemStack) items.get(pass);
						//Left click pick up item
						Slot origSlot = getSlotFromPosition(item.position);
						Slot newSlot = getSlotFromPosition(pass);
						if (newSlot != origSlot) {
							handleMouseClick(origSlot, 0, 0, false);
							
							//Left click place item down
							
							handleMouseClick(newSlot, 0, 0, false);
							
							ItemStack cursor = Minecraft.theMinecraft.thePlayer.inventory.getItemStack();
							if (cursor != null) {
								handleMouseClick(origSlot, 0, 0, false);
							}
						}
						break;
					}
					pass++;
					if (pass >= inventory.getSizeInventory()) {
						break;
					}
				}
			}
		}
	}
	
	public Slot getSlotFromPosition(int pos) {
		return InventoryUtil.getSlotFromPosition(pos, inventorySlots);
	}
	// Spout End

	/**
	 * Draws the screen and all the components in it.
	 */
	public void drawScreen(int par1, int par2, float par3)
	{
		drawDefaultBackground();
		int i = guiLeft;
		int j = guiTop;
		drawGuiContainerBackgroundLayer(par3, par1, par2);
		RenderHelper.enableGUIStandardItemLighting();
		GL11.glPushMatrix();
		GL11.glTranslatef(i, j, 0.0F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		Slot slot = null;
		int k = 240;
		int i1 = 240;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)k / 1.0F, (float)i1 / 1.0F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		for (int l = 0; l < inventorySlots.inventorySlots.size(); l++)
		{
			Slot slot1 = (Slot)inventorySlots.inventorySlots.get(l);
			drawSlotInventory(slot1);

			if (isMouseOverSlot(slot1, par1, par2))
			{
				slot = slot1;
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glDisable(GL11.GL_DEPTH_TEST);
				int j1 = slot1.xDisplayPosition;
				int k1 = slot1.yDisplayPosition;
				drawGradientRect(j1, k1, j1 + 16, k1 + 16, 0x80ffffff, 0x80ffffff);
				GL11.glEnable(GL11.GL_LIGHTING);
				GL11.glEnable(GL11.GL_DEPTH_TEST);
			}
		}

		InventoryPlayer inventoryplayer = mc.thePlayer.inventory;

		// Spout Start
//		if (inventoryplayer.getItemStack() != null)
//		{
//			GL11.glTranslatef(0.0F, 0.0F, 32F);
//			zLevel = 200F;
//			itemRenderer.zLevel = 200F;
//			itemRenderer.renderItemIntoGUI(fontRenderer, mc.renderEngine, inventoryplayer.getItemStack(), par1 - i - 8, par2 - j - 8);
//			itemRenderer.renderItemOverlayIntoGUI(fontRenderer, mc.renderEngine, inventoryplayer.getItemStack(), par1 - i - 8, par2 - j - 8);
//			zLevel = 0.0F;
//			itemRenderer.zLevel = 0.0F;
//		}
		// Spout End
		
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		drawGuiContainerForegroundLayer();

		if (inventoryplayer.getItemStack() == null && slot != null && slot.getHasStack())
		{
			ItemStack itemstack = slot.getStack();
			// Spout Start
			List<String> list = itemstack.getItemNameandInformation();
			org.spoutcraft.api.material.Material item = MaterialData.getMaterial(slot.getStack().itemID, (short)(slot.getStack().getItemDamage()));
			String custom = item != null ? String.format(item.getName(), String.valueOf(slot.getStack().getItemDamage())) : null;
			if (custom != null && slot.getStack().itemID != Item.potion.shiftedIndex && !item.equals(MaterialData.writtenBook)) {
				list.set(0, custom);
			}
			if(list.size() > 0)
			{
				/*int j2 = 0;
				for(int k2 = 0; k2 < list.size(); k2++)
				{
					int i3 = fontRenderer.getStringWidth((String)list.get(k2));
					if(i3 > j2)
					{
						j2 = i3;
					}
				}

				int l2 = (i - k) + 12;
				int j3 = j - l - 12;
				int k3 = j2;
				int l3 = 8;
				if(list.size() > 1)
				{
					l3 += 2 + (list.size() - 1) * 10;
				}
				zLevel = 300F;
				itemRenderer.zLevel = 300F;
				int i4 = 0xf0100010;
				drawGradientRect(l2 - 3, j3 - 4, l2 + k3 + 3, j3 - 3, i4, i4);
				drawGradientRect(l2 - 3, j3 + l3 + 3, l2 + k3 + 3, j3 + l3 + 4, i4, i4);
				drawGradientRect(l2 - 3, j3 - 3, l2 + k3 + 3, j3 + l3 + 3, i4, i4);
				drawGradientRect(l2 - 4, j3 - 3, l2 - 3, j3 + l3 + 3, i4, i4);
				drawGradientRect(l2 + k3 + 3, j3 - 3, l2 + k3 + 4, j3 + l3 + 3, i4, i4);
				int j4 = 0x505000ff;
				int k4 = (j4 & 0xfefefe) >> 1 | j4 & 0xff000000;
				drawGradientRect(l2 - 3, (j3 - 3) + 1, (l2 - 3) + 1, (j3 + l3 + 3) - 1, j4, k4);
				drawGradientRect(l2 + k3 + 2, (j3 - 3) + 1, l2 + k3 + 3, (j3 + l3 + 3) - 1, j4, k4);
				drawGradientRect(l2 - 3, j3 - 3, l2 + k3 + 3, (j3 - 3) + 1, j4, j4);
				drawGradientRect(l2 - 3, j3 + l3 + 2, l2 + k3 + 3, j3 + l3 + 3, k4, k4);
				 */
				String tooltip = "";
				int lines = 0;
				for(int l4 = 0; l4 < list.size(); l4++)
				{
					String s = (String)list.get(l4);
					if(l4 == 0)
					{
						s = (new StringBuilder()).append("\247").append(Integer.toHexString(itemstack.getRarity().rarityColor)).append(s).toString();
					} else
					{
						s = (new StringBuilder()).append("\2477").append(s).toString();
					}
					//fontRenderer.drawStringWithShadow(s, l2, j3, -1);
					/*if(l4 == 0)
					{
						j3 += 2;
					}
					j3 += 10;*/
					tooltip += s + "\n";
					lines++;
				}
				tooltip = tooltip.trim();
				super.drawTooltip(tooltip, (par1 - guiLeft) + 8, par2 - guiTop - lines * 6);
				// Spout End
				zLevel = 0.0F;
				itemRenderer.zLevel = 0.0F;
			}
		}

		GL11.glPopMatrix();
		super.drawScreen(par1, par2, par3);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

	protected void func_74184_a(ItemStack par1ItemStack, int par2, int par3) {
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		List var4 = par1ItemStack.getItemNameandInformation();

		if (!var4.isEmpty()) {
			int var5 = 0;
			Iterator var6 = var4.iterator();

			while (var6.hasNext()) {
				String var7 = (String)var6.next();
				int var8 = this.fontRenderer.getStringWidth(var7);

				if (var8 > var5) {
					var5 = var8;
				}
			}

			int var15 = par2 + 12;
			int var16 = par3 - 12;
			int var9 = 8;

			if (var4.size() > 1) {
				var9 += 2 + (var4.size() - 1) * 10;
			}

			this.zLevel = 300.0F;
			itemRenderer.zLevel = 300.0F;
			int var10 = -267386864;
			this.drawGradientRect(var15 - 3, var16 - 4, var15 + var5 + 3, var16 - 3, var10, var10);
			this.drawGradientRect(var15 - 3, var16 + var9 + 3, var15 + var5 + 3, var16 + var9 + 4, var10, var10);
			this.drawGradientRect(var15 - 3, var16 - 3, var15 + var5 + 3, var16 + var9 + 3, var10, var10);
			this.drawGradientRect(var15 - 4, var16 - 3, var15 - 3, var16 + var9 + 3, var10, var10);
			this.drawGradientRect(var15 + var5 + 3, var16 - 3, var15 + var5 + 4, var16 + var9 + 3, var10, var10);
			int var11 = 1347420415;
			int var12 = (var11 & 16711422) >> 1 | var11 & -16777216;
			this.drawGradientRect(var15 - 3, var16 - 3 + 1, var15 - 3 + 1, var16 + var9 + 3 - 1, var11, var12);
			this.drawGradientRect(var15 + var5 + 2, var16 - 3 + 1, var15 + var5 + 3, var16 + var9 + 3 - 1, var11, var12);
			this.drawGradientRect(var15 - 3, var16 - 3, var15 + var5 + 3, var16 - 3 + 1, var11, var11);
			this.drawGradientRect(var15 - 3, var16 + var9 + 2, var15 + var5 + 3, var16 + var9 + 3, var12, var12);

			for (int var13 = 0; var13 < var4.size(); ++var13) {
				String var14 = (String)var4.get(var13);

				if (var13 == 0) {
					var14 = "\u00a7" + Integer.toHexString(par1ItemStack.getRarity().rarityColor) + var14;
				} else {
					var14 = "\u00a77" + var14;
				}

				this.fontRenderer.drawStringWithShadow(var14, var15, var16, -1);

				if (var13 == 0) {
					var16 += 2;
				}

				var16 += 10;
			}

			this.zLevel = 0.0F;
			itemRenderer.zLevel = 0.0F;
		}
	}

	protected void func_74190_a(String par1Str, int par2, int par3) {
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		int var4 = this.fontRenderer.getStringWidth(par1Str);
		int var5 = par2 + 12;
		int var6 = par3 - 12;
		byte var8 = 8;
		this.zLevel = 300.0F;
		itemRenderer.zLevel = 300.0F;
		int var9 = -267386864;
		this.drawGradientRect(var5 - 3, var6 - 4, var5 + var4 + 3, var6 - 3, var9, var9);
		this.drawGradientRect(var5 - 3, var6 + var8 + 3, var5 + var4 + 3, var6 + var8 + 4, var9, var9);
		this.drawGradientRect(var5 - 3, var6 - 3, var5 + var4 + 3, var6 + var8 + 3, var9, var9);
		this.drawGradientRect(var5 - 4, var6 - 3, var5 - 3, var6 + var8 + 3, var9, var9);
		this.drawGradientRect(var5 + var4 + 3, var6 - 3, var5 + var4 + 4, var6 + var8 + 3, var9, var9);
		int var10 = 1347420415;
		int var11 = (var10 & 16711422) >> 1 | var10 & -16777216;
		this.drawGradientRect(var5 - 3, var6 - 3 + 1, var5 - 3 + 1, var6 + var8 + 3 - 1, var10, var11);
		this.drawGradientRect(var5 + var4 + 2, var6 - 3 + 1, var5 + var4 + 3, var6 + var8 + 3 - 1, var10, var11);
		this.drawGradientRect(var5 - 3, var6 - 3, var5 + var4 + 3, var6 - 3 + 1, var10, var10);
		this.drawGradientRect(var5 - 3, var6 + var8 + 2, var5 + var4 + 3, var6 + var8 + 3, var11, var11);
		this.fontRenderer.drawStringWithShadow(par1Str, var5, var6, -1);
		this.zLevel = 0.0F;
		itemRenderer.zLevel = 0.0F;
	}
	
	/**
	 * Draw the foreground layer for the GuiContainer (everythin in front of the items)
	 */
	protected void drawGuiContainerForegroundLayer()
	{
	}

	/**
	 * Draw the background layer for the GuiContainer (everything behind the items)
	 */
	protected abstract void drawGuiContainerBackgroundLayer(float f, int i, int j);

	/**
	 * Draws an inventory slot
	 */
	private void drawSlotInventory(Slot par1Slot)
	{
		int i = par1Slot.xDisplayPosition;
		int j = par1Slot.yDisplayPosition;
		ItemStack itemstack = par1Slot.getStack();
		boolean flag = false;
		int k = i;
		int i1 = j;
		zLevel = 100F;
		itemRenderer.zLevel = 100F;

		if (itemstack == null)
		{
			int j1 = par1Slot.getBackgroundIconIndex();

			if (j1 >= 0)
			{
				GL11.glDisable(GL11.GL_LIGHTING);
				mc.renderEngine.bindTexture(mc.renderEngine.getTexture("/gui/items.png"));
				drawTexturedModalRect(k, i1, (j1 % 16) * 16, (j1 / 16) * 16, 16, 16);
				GL11.glEnable(GL11.GL_LIGHTING);
				flag = true;
			}
		}

		if (!flag)
		{
			itemRenderer.renderItemIntoGUI(fontRenderer, mc.renderEngine, itemstack, k, i1);
			itemRenderer.renderItemOverlayIntoGUI(fontRenderer, mc.renderEngine, itemstack, k, i1);
		}

		itemRenderer.zLevel = 0.0F;
		zLevel = 0.0F;

		if (this == null)
		{
			zLevel = 100F;
			itemRenderer.zLevel = 100F;

			if (itemstack == null)
			{
				int l = par1Slot.getBackgroundIconIndex();

				if (l >= 0)
				{
					GL11.glDisable(GL11.GL_LIGHTING);
					mc.renderEngine.bindTexture(mc.renderEngine.getTexture("/gui/items.png"));
					drawTexturedModalRect(i, j, (l % 16) * 16, (l / 16) * 16, 16, 16);
					GL11.glEnable(GL11.GL_LIGHTING);
					flag = true;
				}
			}

			if (!flag)
			{
				itemRenderer.renderItemIntoGUI(fontRenderer, mc.renderEngine, itemstack, i, j);
				itemRenderer.renderItemOverlayIntoGUI(fontRenderer, mc.renderEngine, itemstack, i, j);
			}

			itemRenderer.zLevel = 0.0F;
			zLevel = 0.0F;
		}
	}

	/**
	 * Returns the slot at the given coordinates or null if there is none.
	 */
	private Slot getSlotAtPosition(int par1, int par2)
	{
		for (int i = 0; i < inventorySlots.inventorySlots.size(); i++)
		{
			Slot slot = (Slot)inventorySlots.inventorySlots.get(i);

			if (isMouseOverSlot(slot, par1, par2))
			{
				return slot;
			}
		}

		return null;
	}

	/**
	 * Called when the mouse is clicked.
	 */
	protected void mouseClicked(int par1, int par2, int par3)
	{
		super.mouseClicked(par1, par2, par3);

		if (par3 == 0 || par3 == 1)
		{
			Slot slot = getSlotAtPosition(par1, par2);
			int i = guiLeft;
			int j = guiTop;
			boolean flag = par1 < i || par2 < j || par1 >= i + xSize || par2 >= j + ySize;
			int k = -1;

			if (slot != null)
			{
				k = slot.slotNumber;
			}

			if (flag)
			{
				// Spout Start
				boolean isSpoutSlot = false;
				for(Widget w : getScreen().getAttachedWidgets(true)) {
					if(isInBoundingRect(w, par1, par2)) {
						if(w instanceof org.spoutcraft.api.gui.Slot) {
							isSpoutSlot = true;
							break;
						}
					}
				}
				if(!isSpoutSlot) {
					k = -999;
				}
				// Spout End
			}

			if (k != -1)
			{
				boolean flag1 = k != -999 && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));
				handleMouseClick(slot, k, par3, flag1);
			}
		}
	}

	/**
	 * Returns if the passed mouse position is over the specified slot.
	 */
	private boolean isMouseOverSlot(Slot par1Slot, int par2, int par3)
	{
		return this.func_74188_c(par1Slot.xDisplayPosition, par1Slot.yDisplayPosition, 16, 16, par2, par3);
	}
	
	protected boolean func_74188_c(int par1, int par2, int par3, int par4, int par5, int par6) {
		int var7 = this.guiLeft;
		int var8 = this.guiTop;
		par5 -= var7;
		par6 -= var8;
		return par5 >= par1 - 1 && par5 < par1 + par3 + 1 && par6 >= par2 - 1 && par6 < par2 + par4 + 1;
	}

	protected void handleMouseClick(Slot par1Slot, int par2, int par3, boolean par4)
	{
		if (par1Slot != null)
		{
			par2 = par1Slot.slotNumber;
		}
		mc.playerController.windowClick(inventorySlots.windowId, par2, par3, par4, mc.thePlayer);
	}

	/**
	 * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
	 */
	protected void keyTyped(char par1, int par2)
	{
		if (par2 == 1 || par2 == mc.gameSettings.keyBindInventory.keyCode)
		{
			mc.thePlayer.closeScreen();
		}
	}

	/**
	 * Called when the screen is unloaded. Used to disable keyboard repeat events
	 */
	public void onGuiClosed()
	{
		if (mc.thePlayer == null)
		{
			return;
		}
		else
		{
			inventorySlots.onCraftGuiClosed(mc.thePlayer);
			return;
		}
	}

	/**
	 * Returns true if this GUI should pause the game when it is displayed in single-player
	 */
	public boolean doesGuiPauseGame()
	{
		return false;
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	public void updateScreen()
	{
		super.updateScreen();

		if (!mc.thePlayer.isEntityAlive() || mc.thePlayer.isDead)
		{
			mc.thePlayer.closeScreen();
		}
	}
}

class PositionedItemStack extends ItemStack {
	final int position;
	public PositionedItemStack(ItemStack item, int position) {
		super(item.itemID, item.stackSize, item.getItemDamage());
		this.position = position;
	}
	
}