package net.minecraft.src;

import java.util.ArrayList; // Spout
import java.util.Collections; // Spout
import java.util.Iterator; // Spout
import java.util.List;

import net.minecraft.client.Minecraft;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
// Spout start
import org.spoutcraft.api.Spoutcraft;
import org.spoutcraft.api.addon.Addon;
import org.spoutcraft.api.gui.Button;
import org.spoutcraft.api.gui.GenericButton;
import org.spoutcraft.api.gui.GenericCheckBox;
import org.spoutcraft.api.gui.Widget;
import org.spoutcraft.client.MCItemStackComparator;
import org.spoutcraft.client.config.Configuration;
import org.spoutcraft.client.inventory.InventoryUtil;
//Spout end

public abstract class GuiContainer extends GuiScreen {

	/** Stacks renderer. Icons, stack size, health, etc... */
	protected static RenderItem itemRenderer; //Spout

	/** The X size of the inventory window in pixels. */
	protected int xSize = 176;

	/** The Y size of the inventory window in pixels. */
	protected int ySize = 166;

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
	private Slot theSlot;
	private Slot field_85051_p = null;
	private boolean field_90018_r = false;
	private ItemStack field_85050_q = null;
	private int field_85049_r = 0;
	private int field_85048_s = 0;
	private Slot field_85047_t = null;
	private long field_85046_u = 0L;
	private ItemStack field_85045_v = null;

	// Spout Start
	private Button orderByAlphabet, orderById, replaceTools, replaceBlocks;
	static {
	    itemRenderer = GuiScreen.ourItemRenderer;
	}
	// Spout Start

	public GuiContainer(Container par1Container) {
		this.inventorySlots = par1Container;
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	public void initGui() {
		super.initGui();
		this.mc.thePlayer.openContainer = this.inventorySlots;
		this.guiLeft = (this.width - this.xSize) / 2;
		this.guiTop = (this.height - this.ySize) / 2;
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
				handleMouseClick(null, orig, 0, 0); //pick up the item
				
				for (int j = 0; j < inventory.getSizeInventory(); j++) {
					if (j != i) {
						ItemStack other = inventory.getStackInSlot(j);
						if (other != null && other.itemID == item.itemID && other.getItemDamage() == item.getItemDamage()) {
							int slot = j;
							//Avoid the hotbar
							if (slot < 9 && player) {
								continue;
							}
							handleMouseClick(null, slot, 0, 0); //merge with the existing stack we found
							
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
					handleMouseClick(null, orig, 0, 0);
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
							handleMouseClick(null, origSlot, 0, 0);
							
							//Left click place item down
							handleMouseClick(null, newSlot, 0, 0);
							
							ItemStack cursor = Minecraft.theMinecraft.thePlayer.inventory.getItemStack();
							if (cursor != null) {
								handleMouseClick(null, origSlot, 0, 0);
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
							handleMouseClick(origSlot, 0, 0, 0);
							
							//Left click place item down
							
							handleMouseClick(newSlot, 0, 0, 0);
							
							ItemStack cursor = Minecraft.theMinecraft.thePlayer.inventory.getItemStack();
							if (cursor != null) {
								handleMouseClick(origSlot, 0, 0, 0);
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
	public void drawScreen(int par1, int par2, float par3) {
		this.drawDefaultBackground();
		int var4 = this.guiLeft;
		int var5 = this.guiTop;
		this.drawGuiContainerBackgroundLayer(par3, par1, par2);
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		super.drawScreen(par1, par2, par3);
		RenderHelper.enableGUIStandardItemLighting();
		GL11.glPushMatrix();
		GL11.glTranslatef((float)var4, (float)var5, 0.0F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		this.theSlot = null;
		short var6 = 240;
		short var7 = 240;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)var6 / 1.0F, (float)var7 / 1.0F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int var8;
		int var9;

		for (int var13 = 0; var13 < this.inventorySlots.inventorySlots.size(); ++var13) {
			Slot var14 = (Slot)this.inventorySlots.inventorySlots.get(var13);
			this.drawSlotInventory(var14);

			if (this.isMouseOverSlot(var14, par1, par2)) {
				this.theSlot = var14;
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glDisable(GL11.GL_DEPTH_TEST);
				var8 = var14.xDisplayPosition;
				var9 = var14.yDisplayPosition;
				this.drawGradientRect(var8, var9, var8 + 16, var9 + 16, -2130706433, -2130706433);
				GL11.glEnable(GL11.GL_LIGHTING);
				GL11.glEnable(GL11.GL_DEPTH_TEST);
			}
		}

		this.drawGuiContainerForegroundLayer(par1, par2);
		InventoryPlayer var15 = this.mc.thePlayer.inventory;
		ItemStack var16 = this.field_85050_q == null ? var15.getItemStack() : this.field_85050_q;

		if (var16 != null) {
			var8 = this.field_85050_q == null ? 8 : 0;

			if (this.field_85050_q != null && this.field_90018_r) {
				var16 = var16.copy();
				var16.stackSize = MathHelper.ceiling_float_int((float)var16.stackSize / 2.0F);
			}

			this.func_85044_b(var16, par1 - var4 - var8, par2 - var5 - var8);
		}

		if (this.field_85045_v != null) {
			float var17 = (float)(Minecraft.getSystemTime() - this.field_85046_u) / 100.0F;

			if (var17 >= 1.0F) {
				var17 = 1.0F;
				this.field_85045_v = null;
			}

			var9 = this.field_85047_t.xDisplayPosition - this.field_85049_r;
			int var10 = this.field_85047_t.yDisplayPosition - this.field_85048_s;
			int var11 = this.field_85049_r + (int)((float)var9 * var17);
			int var12 = this.field_85048_s + (int)((float)var10 * var17);
			this.func_85044_b(this.field_85045_v, var11, var12);
		}

		if (var15.getItemStack() == null && this.theSlot != null && this.theSlot.getHasStack()) {
			ItemStack var18 = this.theSlot.getStack();
			this.func_74184_a(var18, par1 - var4 + 8, par2 - var5 + 8);
		}

		GL11.glPopMatrix();
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		RenderHelper.enableStandardItemLighting();
	}

	private void func_85044_b(ItemStack par1ItemStack, int par2, int par3) {
		GL11.glTranslatef(0.0F, 0.0F, 32.0F);
		this.zLevel = 200.0F;
		itemRenderer.zLevel = 200.0F;
		itemRenderer.renderItemAndEffectIntoGUI(this.fontRenderer, this.mc.renderEngine, par1ItemStack, par2, par3);
		itemRenderer.renderItemOverlayIntoGUI(this.fontRenderer, this.mc.renderEngine, par1ItemStack, par2, par3);
		this.zLevel = 0.0F;
		itemRenderer.zLevel = 0.0F;
	}

	protected void func_74184_a(ItemStack par1ItemStack, int par2, int par3) {
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		List var4 = par1ItemStack.getTooltip(this.mc.thePlayer, this.mc.gameSettings.advancedItemTooltips);

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

	/**
	 * Draws the text when mouse is over creative inventory tab. Params: current creative tab to be checked, current mouse
	 * x position, current mouse y position.
	 */
	protected void drawCreativeTabHoveringText(String par1Str, int par2, int par3) {
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
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		RenderHelper.enableStandardItemLighting();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
	}

	/**
	 * Draw the foreground layer for the GuiContainer (everything in front of the items)
	 */
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {}

	/**
	 * Draw the background layer for the GuiContainer (everything behind the items)
	 */
	protected abstract void drawGuiContainerBackgroundLayer(float var1, int var2, int var3);

	/**
	 * Draws an inventory slot
	 */
	private void drawSlotInventory(Slot par1Slot) {
		int var2 = par1Slot.xDisplayPosition;
		int var3 = par1Slot.yDisplayPosition;
		ItemStack var4 = par1Slot.getStack();
		boolean var5 = par1Slot == this.field_85051_p && this.field_85050_q != null && !this.field_90018_r;

		if (par1Slot == this.field_85051_p && this.field_85050_q != null && this.field_90018_r && var4 != null) {
			var4 = var4.copy();
			var4.stackSize /= 2;
		}

		this.zLevel = 100.0F;
		itemRenderer.zLevel = 100.0F;

		if (var4 == null) {
			int var6 = par1Slot.getBackgroundIconIndex();

			if (var6 >= 0) {
				GL11.glDisable(GL11.GL_LIGHTING);
				this.mc.renderEngine.bindTexture(this.mc.renderEngine.getTexture("/gui/items.png"));
				this.drawTexturedModalRect(var2, var3, var6 % 16 * 16, var6 / 16 * 16, 16, 16);
				GL11.glEnable(GL11.GL_LIGHTING);
				var5 = true;
			}
		}

		if (!var5) {
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			itemRenderer.renderItemAndEffectIntoGUI(this.fontRenderer, this.mc.renderEngine, var4, var2, var3);
			itemRenderer.renderItemOverlayIntoGUI(this.fontRenderer, this.mc.renderEngine, var4, var2, var3);
		}

		itemRenderer.zLevel = 0.0F;
		this.zLevel = 0.0F;
	}

	/**
	 * Returns the slot at the given coordinates or null if there is none.
	 */
	private Slot getSlotAtPosition(int par1, int par2) {
		for (int var3 = 0; var3 < this.inventorySlots.inventorySlots.size(); ++var3) {
			Slot var4 = (Slot)this.inventorySlots.inventorySlots.get(var3);

			if (this.isMouseOverSlot(var4, par1, par2)) {
				return var4;
			}
		}

		return null;
	}

	/**
	 * Called when the mouse is clicked.
	 */
	protected void mouseClicked(int par1, int par2, int par3) {
		super.mouseClicked(par1, par2, par3);
		boolean var4 = par3 == this.mc.gameSettings.keyBindPickBlock.keyCode + 100;

		if (par3 == 0 || par3 == 1 || var4) {
			Slot var5 = this.getSlotAtPosition(par1, par2);
			int var6 = this.guiLeft;
			int var7 = this.guiTop;
			boolean var8 = par1 < var6 || par2 < var7 || par1 >= var6 + this.xSize || par2 >= var7 + this.ySize;
			int var9 = -1;

			if (var5 != null) {
				var9 = var5.slotNumber;
			}

			if (var8) {
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
					var9 = -999;
				}
				// Spout End
			}

			if (this.mc.gameSettings.field_85185_A && var8 && this.mc.thePlayer.inventory.getItemStack() == null) {
				this.mc.displayGuiScreen((GuiScreen)null);
				return;
			}

			if (var9 != -1) {
				if (this.mc.gameSettings.field_85185_A) {
					if (var5 != null && var5.getHasStack()) {
						this.field_85051_p = var5;
						this.field_85050_q = null;
						this.field_90018_r = par3 == 1;
					} else {
						this.field_85051_p = null;
					}
				} else if (var4) {
					this.handleMouseClick(var5, var9, par3, 3);
				} else {
					boolean var10 = var9 != -999 && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));
					this.handleMouseClick(var5, var9, par3, var10 ? 1 : 0);
				}
			}
		}
	}

	protected void func_85041_a(int par1, int par2, int par3, long par4) {
		if (this.field_85051_p != null && this.mc.gameSettings.field_85185_A && this.field_85050_q == null) {
			if (par3 == 0 || par3 == 1) {
				Slot var6 = this.getSlotAtPosition(par1, par2);

				if (var6 != this.field_85051_p) {
					this.field_85050_q = this.field_85051_p.getStack();
				}
			}
		}
	}

	/**
	 * Called when the mouse is moved or a mouse button is released.  Signature: (mouseX, mouseY, which) which==-1 is
	 * mouseMove, which==0 or which==1 is mouseUp
	 */
	protected void mouseMovedOrUp(int par1, int par2, int par3) {
		if (this.field_85051_p != null && this.mc.gameSettings.field_85185_A) {
			if (par3 == 0 || par3 == 1) {
				Slot var4 = this.getSlotAtPosition(par1, par2);
				int var5 = this.guiLeft;
				int var6 = this.guiTop;
				boolean var7 = par1 < var5 || par2 < var6 || par1 >= var5 + this.xSize || par2 >= var6 + this.ySize;
				int var8 = -1;

				if (var4 != null) {
					var8 = var4.slotNumber;
				}

				if (var7) {
					var8 = -999;
				}

				if (this.field_85050_q == null && var4 != this.field_85051_p) {
					this.field_85050_q = this.field_85051_p.getStack();
				}

				boolean var9 = var4 == null || !var4.getHasStack();

				if (var4 != null && var4.getHasStack() && this.field_85050_q != null && ItemStack.areItemStackTagsEqual(var4.getStack(), this.field_85050_q)) {
					var9 |= var4.getStack().stackSize + this.field_85050_q.stackSize <= this.field_85050_q.getMaxStackSize();
				}

				if (var8 != -1 && this.field_85050_q != null && var9) {
					this.handleMouseClick(this.field_85051_p, this.field_85051_p.slotNumber, par3, 0);
					this.handleMouseClick(var4, var8, 0, 0);

					if (this.mc.thePlayer.inventory.getItemStack() != null) {
						this.handleMouseClick(this.field_85051_p, this.field_85051_p.slotNumber, par3, 0);
						this.field_85049_r = par1 - var5;
						this.field_85048_s = par2 - var6;
						this.field_85047_t = this.field_85051_p;
						this.field_85045_v = this.field_85050_q;
						this.field_85046_u = Minecraft.getSystemTime();
					} else {
						this.field_85045_v = null;
					}
				} else if (this.field_85050_q != null) {
					this.field_85049_r = par1 - var5;
					this.field_85048_s = par2 - var6;
					this.field_85047_t = this.field_85051_p;
					this.field_85045_v = this.field_85050_q;
					this.field_85046_u = Minecraft.getSystemTime();
				}

				this.field_85050_q = null;
				this.field_85051_p = null;
			}
		}
	}

	/**
	 * Returns if the passed mouse position is over the specified slot.
	 */
	private boolean isMouseOverSlot(Slot par1Slot, int par2, int par3) {
		return this.func_74188_c(par1Slot.xDisplayPosition, par1Slot.yDisplayPosition, 16, 16, par2, par3);
	}

	protected boolean func_74188_c(int par1, int par2, int par3, int par4, int par5, int par6) {
		int var7 = this.guiLeft;
		int var8 = this.guiTop;
		par5 -= var7;
		par6 -= var8;
		return par5 >= par1 - 1 && par5 < par1 + par3 + 1 && par6 >= par2 - 1 && par6 < par2 + par4 + 1;
	}

	protected void handleMouseClick(Slot par1Slot, int par2, int par3, int par4) {
		if (par1Slot != null) {
			par2 = par1Slot.slotNumber;
		}

		this.mc.playerController.windowClick(this.inventorySlots.windowId, par2, par3, par4, this.mc.thePlayer);
	}

	/**
	 * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
	 */
	protected void keyTyped(char par1, int par2) {
		if (par2 == 1 || par2 == this.mc.gameSettings.keyBindInventory.keyCode) {
			this.mc.thePlayer.closeScreen();
		}

		this.func_82319_a(par2);

		if (par2 == this.mc.gameSettings.keyBindPickBlock.keyCode && this.theSlot != null && this.theSlot.getHasStack()) {
			this.handleMouseClick(this.theSlot, this.theSlot.slotNumber, this.ySize, 3);
		}
	}

	protected boolean func_82319_a(int par1) {
		if (this.mc.thePlayer.inventory.getItemStack() == null && this.theSlot != null) {
			for (int var2 = 0; var2 < 9; ++var2) {
				if (par1 == 2 + var2) {
					this.handleMouseClick(this.theSlot, this.theSlot.slotNumber, var2, 2);
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Called when the screen is unloaded. Used to disable keyboard repeat events
	 */
	public void onGuiClosed() {
		if (this.mc.thePlayer != null) {
			this.inventorySlots.onCraftGuiClosed(this.mc.thePlayer);
		}
	}

	/**
	 * Returns true if this GUI should pause the game when it is displayed in single-player
	 */
	public boolean doesGuiPauseGame() {
		return false;
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	public void updateScreen() {
		super.updateScreen();

		if (!this.mc.thePlayer.isEntityAlive() || this.mc.thePlayer.isDead) {
			this.mc.thePlayer.closeScreen();
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