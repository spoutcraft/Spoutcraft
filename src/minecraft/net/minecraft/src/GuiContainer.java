package net.minecraft.src;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
//Spout Start
import org.spoutcraft.client.MCItemStackComparator;
import org.spoutcraft.spoutcraftapi.Spoutcraft;
import org.spoutcraft.spoutcraftapi.addon.Addon;
import org.spoutcraft.spoutcraftapi.gui.Button;
import org.spoutcraft.spoutcraftapi.gui.GenericButton;
import org.spoutcraft.spoutcraftapi.gui.Widget;
import org.spoutcraft.spoutcraftapi.material.MaterialData;
//Spout End

public abstract class GuiContainer extends GuiScreen
{
	//Spout moved up to GuiScreen
	/** Stacks renderer. Icons, stack size, health, etc... */
//	protected static RenderItem itemRenderer = new RenderItem();
	//Spout End

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
	
	//Spout start
	private Button orderByAlphabet, orderById;
	//Spout start

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

	//Spout start	
		if(Spoutcraft.hasPermission("spout.client.sortinventory")) {
			Addon spoutcraft = Spoutcraft.getAddonManager().getAddon("Spoutcraft");
			orderByAlphabet = new GenericButton("Sort A-Z");
			orderByAlphabet.setGeometry(15, 105, 75, 20);
			orderById = new GenericButton("Sort by Id");
			orderById.setGeometry(15, 80, 75, 20);
			orderByAlphabet.setTooltip("Experimental Feature");
			orderById.setTooltip("Experimental Feature");
			IInventory inv = inventorySlots.getInventory();
			if (inv != null) {
				getScreen().attachWidgets(spoutcraft, orderByAlphabet, orderById);
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
				IInventory inv = inventorySlots.getInventory();
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
	}
	
	public void compactInventory(IInventory inventory, boolean player) {
		//To keep mp compatibility, fake window clicks
			for (int i = 0; i < inventory.getSizeInventory(); i++) {
				ItemStack item = inventory.getStackInSlot(i);
				if (item != null && item.stackSize < item.getMaxStackSize()) {
					
					//Find a place to put this
					int orig = i;
					if (orig < 9 && player) orig = 36 + orig;
					handleMouseClick(null, orig, 0, false); //pick up the item
					
					for (int j = 0; j < inventory.getSizeInventory(); j++) {
						if (j != i) {
							ItemStack other = inventory.getStackInSlot(j);
							if (other != null && other.itemID == item.itemID && other.getItemDamage() == item.getItemDamage()) {
								int slot = j;
								if (slot < 9 && player) slot = 36 + slot;
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
	
	public void sortPlayerInventory(boolean byName) {
		//To keep mp compatibility, fake window clicks
		InventoryPlayer inventory = Minecraft.theMinecraft.thePlayer.inventory;
		for (int pass = 0; pass < inventory.mainInventory.length; pass++) {
			ArrayList<ItemStack> items = new ArrayList<ItemStack>();
			for (int i = 0; i < inventory.mainInventory.length; i++) {
				ItemStack item = inventory.mainInventory[i];
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
					int origSlot = item.position;
					if (origSlot < 9) origSlot = 36 + origSlot;
					handleMouseClick(null, origSlot, 0, false);
					
					//Left click place item down
					int newSlot = pass + 9; 
					handleMouseClick(null, newSlot, 0, false);
					
					ItemStack cursor = Minecraft.theMinecraft.thePlayer.inventory.getItemStack();
					if (cursor != null) {
						handleMouseClick(null, origSlot, 0, false);
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
	
	public void sortInventory(IInventory inventory, boolean byName) {
		//To keep mp compatibility, fake window clicks
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
					Slot orig = getSlotFromPosition(item.position);
					handleMouseClick(orig, 0, 0, false);
					
					//Left click place item down
					Slot slot = getSlotFromPosition(pass);
					handleMouseClick(slot, 0, 0, false);
					
					ItemStack cursor = Minecraft.theMinecraft.thePlayer.inventory.getItemStack();
					if (cursor != null) {
						handleMouseClick(orig, 0, 0, false);
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
	
	public Slot getSlotFromPosition(int pos) {
		for (int i = 0; i < inventorySlots.inventorySlots.size(); i++) {
			if (inventorySlots.inventorySlots.get(i) != null) {
				if (((Slot)inventorySlots.inventorySlots.get(i)).slotIndex == pos) {
					return ((Slot)inventorySlots.inventorySlots.get(i));
				}
			}
		}
		return null;
	}
	//Spout end

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

		//Spout Start
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
		//Spout End
		
		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		drawGuiContainerForegroundLayer();

		if (inventoryplayer.getItemStack() == null && slot != null && slot.getHasStack())
		{
			ItemStack itemstack = slot.getStack();
			//Spout Start
			List<String> list = itemstack.getItemNameandInformation();
			org.spoutcraft.spoutcraftapi.material.Material item = MaterialData.getMaterial(slot.getStack().itemID, (short)(slot.getStack().getItemDamage()));
			String custom = item != null ? String.format(item.getName(), String.valueOf(slot.getStack().getItemDamage())) : null;
			if (custom != null && slot.getStack().itemID != Item.potion.shiftedIndex) {
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
						s = (new StringBuilder()).append("\247").append(Integer.toHexString(itemstack.getRarity().nameColor)).append(s).toString();
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
				//Spout end
				zLevel = 0.0F;
				itemRenderer.zLevel = 0.0F;
			}
		}

		GL11.glPopMatrix();
		super.drawScreen(par1, par2, par3);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
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
				//Spout Start
				boolean isSpoutSlot = false;
				for(Widget w : getScreen().getAttachedWidgets(true)) {
					if(isInBoundingRect(w, par1, par2)) {
						if(w instanceof org.spoutcraft.spoutcraftapi.gui.Slot) {
							isSpoutSlot = true;
							break;
						}
					}
				}
				if(!isSpoutSlot) {
					k = -999;
				}
				//Spout End
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
		int i = guiLeft;
		int j = guiTop;
		par2 -= i;
		par3 -= j;
		return par2 >= par1Slot.xDisplayPosition - 1 && par2 < par1Slot.xDisplayPosition + 16 + 1 && par3 >= par1Slot.yDisplayPosition - 1 && par3 < par1Slot.yDisplayPosition + 16 + 1;
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
			mc.playerController.func_20086_a(inventorySlots.windowId, mc.thePlayer);
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