package net.minecraft.src;

import java.awt.Toolkit;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListMap;

import org.lwjgl.input.Mouse;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.spoutcraft.api.gui.Button;
import org.spoutcraft.api.gui.CheckBox;
import org.spoutcraft.api.gui.ComboBox;
import org.spoutcraft.api.gui.Control;
import org.spoutcraft.api.gui.GenericComboBox;
import org.spoutcraft.api.gui.GenericComboBox.ComboBoxView;
import org.spoutcraft.api.gui.GenericGradient;
import org.spoutcraft.api.gui.GenericOverlayScreen;
//import org.spoutcraft.api.gui.Keyboard;
import org.spoutcraft.api.gui.ListWidget;
import org.spoutcraft.api.gui.ListWidgetItem;
import org.spoutcraft.api.gui.Orientation;
import org.spoutcraft.api.gui.OverlayScreen;
import org.spoutcraft.api.gui.RadioButton;
import org.spoutcraft.api.gui.RenderPriority;
import org.spoutcraft.api.gui.Screen;
import org.spoutcraft.api.gui.ScreenType;
import org.spoutcraft.api.gui.Scrollable;
import org.spoutcraft.api.gui.Slider;
import org.spoutcraft.api.gui.Slot;
import org.spoutcraft.api.gui.TextField;
import org.spoutcraft.api.gui.Widget;
import org.spoutcraft.api.inventory.ItemStack;
import org.spoutcraft.client.ScheduledTextFieldUpdate;
import org.spoutcraft.client.SpoutClient;
import org.spoutcraft.client.config.Configuration;
import org.spoutcraft.client.controls.SimpleKeyBindingManager;
import org.spoutcraft.client.gui.CustomScreen;
import org.spoutcraft.client.gui.MCRenderDelegate;
import org.spoutcraft.client.gui.ScreenUtil;
import org.spoutcraft.client.packet.builtin.PacketComboBox;
import org.spoutcraft.client.packet.builtin.PacketControlAction;
import org.spoutcraft.client.packet.builtin.PacketKeyPress;
import org.spoutcraft.client.packet.builtin.PacketSlotClick;

public class GuiScreen extends Gui {	

	/** Reference to the Minecraft object. */
	protected Minecraft mc;

	/** The width of the screen object. */
	public int width;

	/** The height of the screen object. */
	public int height;

	/** A list of all the buttons in this container. */
	protected List buttonList = new ArrayList();
	public boolean allowUserInput;

	/** The FontRenderer used by GuiScreen */
	protected FontRenderer fontRenderer;
	public GuiParticle guiParticles;

	/** The button that was just pressed. */
	private GuiButton selectedButton;
	private int eventButton;
	private long field_85043_c;
	private int field_92018_d;

	// Spout Start
	public GenericGradient bg;
	public Screen screen = null;
	private long updateTicks;
	private Scrollable holding = null;
	private Orientation holdingScrollBar = Orientation.VERTICAL;
	private long lastMouseMove = 0;
	public static int TOOLTIP_DELAY = 500;
	long renderEndNanoTime = 0L;
	protected static int limitedFramerate = 120;
	private long lastClick = 0;
	protected static RenderItem ourItemRenderer = new RenderItem();
	private boolean firstrun = true;
	protected IdentityHashMap<TextField, ScheduledTextFieldUpdate> scheduledTextFieldUpdates = new IdentityHashMap<TextField, ScheduledTextFieldUpdate>();

	/**
	 * Draws the screen with widgets - do not override - use drawScreen() instead
	 */
	public void drawScreenPre(int x, int y, float z) {
		drawScreen(x, y, z);
		drawWidgets(x, y, z);

		// Limit main menu framerate to 120 FPS as long as we aren't in a game already
		if (this.mc.theWorld == null) {
			long sleeptime = (this.renderEndNanoTime + (long) (1000000000 / limitedFramerate) - System.nanoTime()) / 1000000L;
			if (sleeptime > 0L && sleeptime < 500L) {
				try {
					Thread.sleep(sleeptime);
				} catch (InterruptedException var12) {
					var12.printStackTrace();
				}
			}

			this.renderEndNanoTime = System.nanoTime();
		}

		int i = 0;
		int j = 0;
		if(mc.thePlayer != null) {
			InventoryPlayer inventoryplayer = mc.thePlayer.inventory;
			if(inventoryplayer.getItemStack() != null) {
				RenderHelper.disableStandardItemLighting();
				RenderHelper.enableGUIStandardItemLighting();
				GL11.glTranslatef(0.0F, 0.0F, 32F);
				zLevel = 200F;
				ourItemRenderer.zLevel = 200F;
				ourItemRenderer.renderItemIntoGUI(fontRenderer, mc.renderEngine, inventoryplayer.getItemStack(), x - i - 8, y - j - 8);
				ourItemRenderer.renderItemOverlayIntoGUI(fontRenderer, mc.renderEngine, inventoryplayer.getItemStack(), x - i - 8, y - j - 8);
				zLevel = 0.0F;
				ourItemRenderer.zLevel = 0.0F;
				RenderHelper.enableStandardItemLighting();
			}
		}
	}
	// Spout End

	/**
	 * Draws the screen and all the components in it.
	 */
	public void drawScreen(int par1, int par2, float par3) {
		for (int var4 = 0; var4 < this.buttonList.size(); ++var4) {
			GuiButton var5 = (GuiButton)this.buttonList.get(var4);
			var5.drawButton(this.mc, par1, par2);
		}
	}

	/**
	 * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
	 */
	protected void keyTyped(char par1, int par2) {
		if (par2 == 1) {
			this.mc.displayGuiScreen((GuiScreen)null);
			// Spout Start
			if (mc.currentScreen == null) {
			this.mc.setIngameFocus();
			}
			// Spout End
		}
	}

	// Spout Start
	public final void update(Screen screen) {
		if (this.screen != null) {
			for (Widget w : this.screen.getAttachedWidgets()) {
				screen.attachWidget(w.getAddon(), w);
			}
		}
		this.screen = screen;
	}
	// Spout End

	/**
	 * Returns a string stored in the system clipboard.
	 */
	public static String getClipboardString() {
		try {
			Transferable var0 = Toolkit.getDefaultToolkit().getSystemClipboard().getContents((Object)null);

			if (var0 != null && var0.isDataFlavorSupported(DataFlavor.stringFlavor)) {
				return (String)var0.getTransferData(DataFlavor.stringFlavor);
			}
		} catch (Exception var1) {
			;
		}

		return "";
	}

	// Spout Start - Wrap ALL the methods!
	// Making these protected so you can always override behaviour you don't want.
	protected void mouseClickedPre(int mouseX, int mouseY, int eventButton) {
		mouseClicked(mouseX, mouseY, eventButton); // Call vanilla method
		if (getScreen() == null) {
			return;
		}
		screen.setMouseX(mouseX);
		screen.setMouseY(mouseY);

		ComboBox openCombobox = null;

		if (eventButton == 0) {
			boolean handled = false;
			for (int i = 4; i >= 0; i--) {
				Widget lastWidget = null;
				for (Widget widget : screen.getAttachedWidgets(true)) {
					lastWidget = widget;
					if (widget.getPriority().getId() != i) {
						continue;
					}
					if (widget instanceof Control) {
						Control control = (Control) widget;
						if (control.isEnabled() && control.isVisible() && isInBoundingRect(control, mouseX, mouseY)) {
							if (control.getScreen() instanceof Scrollable) {
								if (!isInBoundingRect(control.getScreen(), mouseX, mouseY)) {
									continue;
								}
							}
							control.setFocus(true);
							if (control instanceof Scrollable) {
								handled = handled || handleClickOnScrollable((Scrollable) control, mouseX, mouseY);
								if (!handled && control instanceof ListWidget) {
									handled = handled || handleClickOnListWidget((ListWidget) control, mouseX, mouseY);
								}
							}
							if (!handled) {
								if (control instanceof Button) {
									handleButtonClick((Button) control);
									handled = true;
								} else if (control instanceof Slider) {
									((Slider) control).setDragging(true);
									handled = true;
								} else if (control instanceof TextField) {
									((TextField) control).setCursorPosition(((TextField) control).getText().length());
									handled = true;
								} else if (control instanceof Slot) {
									handleClickOnSlot((Slot) control, 0);
								}
							}
						}
					}

					if (lastWidget instanceof ComboBox) {
						ComboBox box = (ComboBox) lastWidget;
						if (box.isOpen()) {
							openCombobox = box;
						}
					}
					if (handled) {
						break;
					}
				}
				if (handled) {
					if (lastWidget == openCombobox) {
						openCombobox = null;
					}
					playSoundFX("random.click", 1.0F, 1.0F);
					break;
				}
			}
		} else if (eventButton == 1) {
			for (Widget widget : screen.getAttachedWidgets(true)) {
				if (widget instanceof Control) {
					Control c = (Control) widget;
					if (c.isEnabled() && c.isVisible() && isInBoundingRect(widget, mouseX, mouseY)) {
						if (widget instanceof Slot) {
							handleClickOnSlot((Slot) widget, 1);
							break;
						}
					}
				}
			}
		}
		if (openCombobox != null) {
			openCombobox.closeList();
		}
	}

	private void handleClickOnSlot(Slot slot, int button) {
		try {
			if (mc.thePlayer == null) {
				return;
			}
			PacketSlotClick packet = new PacketSlotClick(slot, button,Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT));
			SpoutClient.getInstance().getPacketManager().sendSpoutPacket(packet);
			ItemStack stackOnCursor = new ItemStack(0);
			if (mc.thePlayer.inventory.getItemStack() != null) {
				net.minecraft.src.ItemStack mcStack = mc.thePlayer.inventory.getItemStack();
				stackOnCursor = new ItemStack(mcStack.itemID, mcStack.stackSize, (short) mcStack.getItemDamage());
			}
			ItemStack stackInSlot = slot.getItem();
			if ((stackOnCursor == null || stackOnCursor.getTypeId() == 0) && stackInSlot.getTypeId() == 0) {
				return; // Nothing to do
			}
			if (stackOnCursor.getTypeId() == 0 && stackInSlot.getTypeId() != 0 && button == 1) { // Split item
				int amountSlot = stackInSlot.getAmount() / 2;
				int amountCursor = stackInSlot.getAmount() - amountSlot;
				if (stackInSlot.getAmount() == 1) {
					amountSlot = 0;
					amountCursor = 1;
				}
				stackOnCursor = stackInSlot.clone();
				stackOnCursor.setAmount(amountCursor);
				stackInSlot.setAmount(amountSlot);
				if (amountSlot == 0) {
					stackInSlot = new ItemStack(0);
				}
				boolean success = slot.onItemTake(stackOnCursor);
				if (success) {
					slot.setItem(stackInSlot);
				} else {
					return;
				}
			} else if (stackOnCursor != null && (stackInSlot.getTypeId() == 0 || (stackInSlot.getTypeId() == stackOnCursor.getTypeId() && stackInSlot.getDurability() == stackOnCursor.getDurability()))) { //Put item
				ItemStack toPut = stackOnCursor.clone();
				int putAmount = toPut.getAmount();
				if (button == 1) {
					putAmount = 1;
				}
				int amount = stackInSlot.getTypeId() == 0 ? 0 : stackInSlot.getAmount();
				amount += putAmount;
				int maxStackSize = toPut.getMaxStackSize();
				if (maxStackSize == -1) {
					maxStackSize = 64;
				}
				if (amount > maxStackSize) {
					putAmount -= amount - maxStackSize;
					amount = maxStackSize;
				}
				if (putAmount <= 0) {
					return;
				}
				toPut.setAmount(putAmount);
				boolean success = slot.onItemPut(toPut);
				if (success) {
					stackOnCursor.setAmount(stackOnCursor.getAmount() - putAmount);
					if (stackOnCursor.getAmount() == 0) {
						stackOnCursor = new ItemStack(0);
					}
					ItemStack put = toPut.clone();
					put.setAmount(amount);
					slot.setItem(put);
				}
			} else if (stackOnCursor == null || stackOnCursor.getTypeId() == 0) { // Take item or shift click
				if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
					slot.onItemShiftClicked();
				} else { // Take item
					boolean success = slot.onItemTake(stackInSlot);
					if (success) {
						stackOnCursor = stackInSlot;
						slot.setItem(new ItemStack(0));
					}
				}
			} else if (stackOnCursor.getTypeId() != stackInSlot.getTypeId() || stackOnCursor.getDurability() != stackInSlot.getDurability()) { // Exchange slot stack and cursor stack
				boolean success = slot.onItemExchange(stackInSlot, stackOnCursor.clone());
				if (success) {
					slot.setItem(stackOnCursor.clone());
					stackOnCursor = stackInSlot;
				}
			}

			if (stackOnCursor == null || stackOnCursor.getTypeId() == 0) {
				mc.thePlayer.inventory.setItemStack(null);
			} else {
				net.minecraft.src.ItemStack mcStack = new net.minecraft.src.ItemStack(stackOnCursor.getTypeId(), stackOnCursor.getAmount(), stackOnCursor.getDurability());
				mc.thePlayer.inventory.setItemStack(mcStack);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void playSoundFX(String sound, float f, float f1) {
		this.mc.sndManager.playSoundFX(sound, 1.0F, 1.0F);
	}

	private boolean handleClickOnListWidget(ListWidget lw, int mouseX, int mouseY) {
		int x = (int) (mouseX - lw.getActualX());
		int y = (int) (mouseY - lw.getActualY());
		if (x < 5) {
			return false;
		}
		int scroll = lw.getScrollPosition(Orientation.VERTICAL);
		y += scroll;
		y -= 5;
		int currentHeight = 0;
		int n = 0;
		for (ListWidgetItem item : lw.getItems()) {

			if (currentHeight <= y && y <= currentHeight + item.getHeight()) {
				boolean doubleclick = false;
				if (System.currentTimeMillis() - 200 < lastClick) {
					doubleclick = true;
				}
				lw.setSelection(n);
				PacketControlAction action = null;
				if (!doubleclick) {
					action = new PacketControlAction(lw.getScreen(), lw, "click", lw.getSelectedRow());
				} else {
					action = new PacketControlAction(lw.getScreen(), lw, "doubleclick", lw.getSelectedRow());
				}
				ListWidgetItem current = lw.getSelectedItem();
				current.onClick(x - 5, y - currentHeight, doubleclick);
				lw.onSelected(lw.getSelectedRow(), doubleclick);
				if (lw instanceof ComboBoxView) {
					PacketComboBox packet = new PacketComboBox(((ComboBoxView) lw).getComboBox());
					SpoutClient.getInstance().getPacketManager().sendSpoutPacket(packet);
				} else {
					SpoutClient.getInstance().getPacketManager().sendSpoutPacket(action);
				}

				lastClick = System.currentTimeMillis();
				return true;
			}
			n++;
			currentHeight += item.getHeight();
		}
		return false;
	}

	private boolean handleClickOnScrollable(Scrollable lw, int mouseX, int mouseY) {
		int x = (int) (mouseX - lw.getActualX());
		int y = (int) (mouseY - lw.getActualY());
		int scrollY = lw.getScrollPosition(Orientation.VERTICAL);
		int scrollX = lw.getScrollPosition(Orientation.HORIZONTAL);
		if (x > lw.getWidth() - 16 && lw.needsScrollBar(Orientation.VERTICAL)) {
			double scrollFactor = 0;
			double p = (double) scrollY / (double) lw.getMaximumScrollPosition(Orientation.VERTICAL);
			scrollFactor = 3 + p * (lw.getViewportSize(Orientation.VERTICAL) - 16.0 - 6.0);
			if (scrollFactor <= y && y <= scrollFactor + 16) {
				holding = lw;
				holdingScrollBar = Orientation.VERTICAL;
			}
		}
		if (y > lw.getHeight() - 16 && lw.needsScrollBar(Orientation.HORIZONTAL)) {
			double scrollFactor = 0;
			double p = (double) scrollX / (double) lw.getMaximumScrollPosition(Orientation.HORIZONTAL);
			scrollFactor = 3 + p * (lw.getViewportSize(Orientation.HORIZONTAL) - 16.0 - 6.0);
			if (scrollFactor <= x && x <= scrollFactor + 16) {
				holding = lw;
				holdingScrollBar = Orientation.HORIZONTAL;
			}
		}
		if (holding != null) {
			return true;
		}
		return false;
	}

	private void handleButtonClick(Button control) {
		if (control instanceof CheckBox) {
			CheckBox check = (CheckBox) control;
			check.setChecked(!check.isChecked());
		}
		if (control instanceof RadioButton) {
			RadioButton radio = (RadioButton) control;
			radio.setSelected(true);
		}
		this.buttonClicked((Button) control);
		SpoutClient.getInstance().getPacketManager().sendSpoutPacket(new PacketControlAction(screen, control, 1));
		((Button) control).onButtonClick();
		if (control instanceof GenericComboBox) {
			PacketComboBox packet = new PacketComboBox((GenericComboBox) control);
			SpoutClient.getInstance().getPacketManager().sendSpoutPacket(packet);
		}
	}
	// Spout End

	/**
	 * store a string in the system clipboard
	 */
	public static void setClipboardString(String par0Str) {
		try {
			StringSelection var1 = new StringSelection(par0Str);
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(var1, (ClipboardOwner)null);
		} catch (Exception var2) {
			;
		}
	}

	/**
	 * Called when the mouse is clicked.
	 */
	protected void mouseClicked(int par1, int par2, int par3) {
		if (par3 == 0) {
			for (int var4 = 0; var4 < this.buttonList.size(); ++var4) {
				GuiButton var5 = (GuiButton)this.buttonList.get(var4);

				if (var5.mousePressed(this.mc, par1, par2)) {
					this.selectedButton = var5;
					this.mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
					this.actionPerformed(var5);
				}
			}
		}
	}
	// Spout Start
	protected void mouseMovedOrUpPre(int mouseX, int mouseY, int eventButton) {
		lastMouseMove = System.currentTimeMillis();
		mouseMovedOrUp(mouseX, mouseY, eventButton);
		if (getScreen() == null) {
			return;
		}
		screen.setMouseX(mouseX);
		screen.setMouseY(mouseY);
		for (Widget widget : screen.getAttachedWidgets(true)) {
			if (widget instanceof Control) {
				Control control = (Control) widget;
				if (control.isEnabled() && control.isVisible()) {
					if (eventButton == 0) {
						if (control.isFocus() && !isInBoundingRect(control, mouseX, mouseY)) { // released control
							control.setFocus(false);
						}
						if (control instanceof Slider && ((Slider) control).isDragging()) {
							((Slider) control).setDragging(false);
							SpoutClient.getInstance().getPacketManager().sendSpoutPacket(new PacketControlAction(screen, control, ((Slider) control).getSliderPosition()));
							((Slider) control).onSliderDrag(((Slider) control).getSliderPosition(), ((Slider) control).getSliderPosition());
						}
					}
				}
			}
		}
		if (holding != null && holdingScrollBar != null) {
			double p = 0;
			if (holdingScrollBar == Orientation.VERTICAL) {
				int y = (int) (mouseY - holding.getActualY());
				p = (double) y / holding.getViewportSize(Orientation.VERTICAL);
			} else {
				int x = (int) (mouseX - holding.getActualX());
				p = (double) x / holding.getViewportSize(Orientation.HORIZONTAL);
			}
			holding.setScrollPosition(holdingScrollBar, (int) ((double) holding.getMaximumScrollPosition(holdingScrollBar) * p));

			if (eventButton == 0) {
				holding = null;
			}
		}
	}

	protected boolean shouldShowTooltip() {
		return !Configuration.isDelayedTooltips() || System.currentTimeMillis() - TOOLTIP_DELAY > lastMouseMove;
	}
	// Spout End

	/**
	 * Called when the mouse is moved or a mouse button is released.  Signature: (mouseX, mouseY, which) which==-1 is
	 * mouseMove, which==0 or which==1 is mouseUp
	 */
	protected void mouseMovedOrUp(int par1, int par2, int par3) {
		if (this.selectedButton != null && par3 == 0) {
			this.selectedButton.mouseReleased(par1, par2);
			this.selectedButton = null;
		}
	}

	/**
	 * Called when a mouse button is pressed and the mouse is moved around. Parameters are : mouseX, mouseY,
	 * lastButtonClicked & timeSinceMouseClick.
	 */
	protected void mouseClickMove(int par1, int par2, int par3, long par4) {}

	/**
	 * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
	 */
	protected void actionPerformed(GuiButton par1GuiButton) {}

	/**
	 * Causes the screen to lay out its subcomponents again. This is the equivalent of the Java call Container.validate()
	 */
	public void setWorldAndResolution(Minecraft par1Minecraft, int par2, int par3) {		
		this.mc = par1Minecraft;
		this.guiParticles = new GuiParticle(par1Minecraft);
		this.fontRenderer = par1Minecraft.fontRenderer;
		this.width = par2;
		this.height = par3;
		this.buttonList.clear();
		// Spout Start
		if (!(this instanceof CustomScreen) && screen != null && !firstrun) {
			for (Widget w : screen.getAttachedWidgets()) {
				screen.removeWidget(w);
			}
		}
		firstrun = false;
		bg = (GenericGradient) new GenericGradient().setHeight(this.height).setWidth(this.width);
		// Spout End
		this.initGui();
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	public void initGui() {}

	/**
	 * Delegates mouse and keyboard input.
	 */
	public void handleInput() {
		while (Mouse.next()) {
			this.handleMouseInput();
		}

		while (Keyboard.next()) {
			// Spout Start
			if (mc.thePlayer instanceof EntityClientPlayerMP && SpoutClient.getInstance().isSpoutEnabled()) {
				EntityClientPlayerMP player = (EntityClientPlayerMP) mc.thePlayer;
				ScreenType screen = ScreenUtil.getType(this);
				int i = Keyboard.getEventKey();
				boolean keyReleased = Keyboard.getEventKeyState();
				PacketKeyPress packet = new PacketKeyPress(i, keyReleased, (MovementInputFromOptions) player.movementInput, screen);
				SpoutClient.getInstance().getPacketManager().sendSpoutPacket(packet);
			}
			((SimpleKeyBindingManager)SpoutClient.getInstance().getKeyBindingManager()).pressKey(Keyboard.getEventKey(), Keyboard.getEventKeyState(), ScreenUtil.getType(this).getCode());
			// Spout End
			this.handleKeyboardInput();
		}
	}

	/**
	 * Handles mouse input.
	 */
	// Spout Start - Rewritten
	// ToDo: needs a rewritten rewrite AND Mac keybind toggle.
	public void handleMouseInput() {
		int x;
		int y;
		if (Mouse.getEventButtonState()) {
			if (this.mc.gameSettings.touchscreen && this.field_92018_d++ > 0) {
				return;
			}
			x = Mouse.getEventX() * this.width / this.mc.displayWidth;
			y = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
			this.mouseClickedPre(x, y, Mouse.getEventButton());
		} else {
			if (this.mc.gameSettings.touchscreen && this.field_92018_d++ > 0) {
				return;
			}
			x = Mouse.getEventX() * this.width / this.mc.displayWidth;
			y = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
			this.mouseMovedOrUpPre(x, y, Mouse.getEventButton());
		}
		int scroll = Mouse.getEventDWheel();
		if (scroll != 0) {
			scroll *= 7;
			handleScroll(x, y, scroll);
		}
	}

	protected void handleScroll(int x, int y, int scroll) {
		Orientation axis = Orientation.VERTICAL;
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			axis = Orientation.HORIZONTAL;
		}
		for (Widget w : getScreen().getAttachedWidgets(true)) {
			if (w != null && isInBoundingRect(w, x, y)) {
				if (w instanceof Scrollable) {
					// Stupid LWJGL not recognizing vertical scrolls :(
					Scrollable lw = (Scrollable) w;
					if (axis == Orientation.VERTICAL) {
						lw.scroll(0, -scroll / 30);
					} else {
						lw.scroll(-scroll / 30, 0);
					}
					PacketControlAction action = new PacketControlAction(lw.getScreen() != null ? lw.getScreen() : getScreen(), lw, axis.toString(), lw.getScrollPosition(axis));
					SpoutClient.getInstance().getPacketManager().sendSpoutPacket(action);
				}
			}
		}
	}
	// Spout End

	/**
	 * Handles keyboard input.
	 */
	// ToDo: needs isMacOS variations added.
	public void handleKeyboardInput() {
		// Spout Start
		boolean handled = false;
		if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
			Keyboard.enableRepeatEvents(false);
		} else if (getScreen() != null) {
			boolean tab = Keyboard.getEventKey() == Keyboard.KEY_TAB;
			TextField focusedTF = null;
			ConcurrentSkipListMap<Integer, TextField> tabIndexMap = tab ? new ConcurrentSkipListMap<Integer, TextField>() : null;

			for (Widget widget : screen.getAttachedWidgets(true)) {
				if (widget instanceof Control) {
					Control control = (Control) widget;
					if (control.isFocus()) {
						if (Keyboard.getEventKeyState()) {
							handled = control.onKeyPressed(org.spoutcraft.api.gui.Keyboard.getKey(Keyboard.getEventKey()));
						} else {
							handled = control.onKeyReleased(org.spoutcraft.api.gui.Keyboard.getKey(Keyboard.getEventKey()));
						}
					}
					if (handled) {
						break;
					}
				}
				if (!Keyboard.getEventKeyState()) {
					break;
				}
				if (widget instanceof TextField) {
					TextField tf = (TextField) widget;
					// Handle tabbing get all textfields of this screen and start looking for the next bigger tab-index
					if (tab) {
						if (tf.isFocus()) {
							focusedTF = tf;
						}
						tabIndexMap.put(tf.getTabIndex(), tf);
					// Pass typed key to text processor
					} else if (tf.isEnabled() && tf.isFocus()) {
						if (tf.getTextProcessor().handleInput(Keyboard.getEventCharacter(), Keyboard.getEventKey())) {
							tf.onTextFieldChange();
							ScheduledTextFieldUpdate updateThread = null;
							if (scheduledTextFieldUpdates.containsKey(tf)) {
								updateThread = scheduledTextFieldUpdates.get(tf);
								if (updateThread.isAlive()) {
									updateThread.delay();
								} else {
									updateThread.start();
								}
							} else {
								updateThread = new ScheduledTextFieldUpdate(screen, tf);
								scheduledTextFieldUpdates.put(tf, updateThread);
								updateThread.start();
							}
						}
						handled = true;
						break;
					}
				}
				if (widget instanceof ListWidget) {
					ListWidget lw = (ListWidget) widget;
					if (lw.isEnabled() && lw.isFocus()) {
						PacketControlAction action = null;
						if (Keyboard.getEventKey() == Keyboard.KEY_DOWN && Keyboard.getEventKeyState()) {
							handled = true;
							lw.shiftSelection(1);
							lw.onSelected(lw.getSelectedRow(), false);
							lw.getSelectedItem().onClick(-1, -1, false);
							action = new PacketControlAction(lw.getScreen() != null ? lw.getScreen() : getScreen(), lw, "selected", lw.getSelectedRow());
						}
						if (Keyboard.getEventKey() == Keyboard.KEY_UP && Keyboard.getEventKeyState()) {
							handled = true;
							lw.shiftSelection(-1);
							lw.onSelected(lw.getSelectedRow(), false);
							lw.getSelectedItem().onClick(-1, -1, false);
							action = new PacketControlAction(lw.getScreen() != null ? lw.getScreen() : getScreen(), lw, "selected", lw.getSelectedRow());
						}
						if (Keyboard.getEventKey() == Keyboard.KEY_RETURN && Keyboard.getEventKeyState()) {
							handled = true;
							if (lw.getSelectedRow() != -1) {
								lw.onSelected(lw.getSelectedRow(), true);
								lw.getSelectedItem().onClick(-1, -1, true);
								action = new PacketControlAction(lw.getScreen() != null ? lw.getScreen() : getScreen(), lw, "doubleclick", lw.getSelectedRow());
							}
						}
						if (action != null) {
							SpoutClient.getInstance().getPacketManager().sendSpoutPacket(action);
							break;
						}
					}
				}
			}

			// Start looking for the next bigger tab-index
			if (tab && focusedTF != null) {
				Integer index = tabIndexMap.higherKey(focusedTF.getTabIndex());
				if (index == null) {
					index = tabIndexMap.ceilingKey(0);
				}
				if (index != null) {
					focusedTF.setFocus(false);
					tabIndexMap.get(index).setFocus(true);
					handled = true;
				}
			}
		}
		if (!handled) {
			// Start of vanilla code - got wrapped with this if
			if (Keyboard.getEventKeyState()) {
				if (Keyboard.getEventKey() == 87) {
					this.mc.toggleFullscreen();
					return;
				}
				this.keyTyped(Keyboard.getEventCharacter(), Keyboard.getEventKey());
			}
			// End of vanilla code
		}
		// Spout End
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	// Spout Start
	public void updateScreen() {
		updateTicks++;
		MCRenderDelegate.shouldRenderCursor = updateTicks / 6 % 2 == 0;
	}
	// Spout End

	/**
	 * Called when the screen is unloaded. Used to disable keyboard repeat events
	 */
	public void onGuiClosed() {}

	/**
	 * Draws either a gradient over the background screen (when it exists) or a flat gradient over background.png
	 */
	public void drawDefaultBackground() {
		this.drawWorldBackground(0);
	}

	public void drawWorldBackground(int par1) {
		if (this.mc.theWorld != null) {
			this.drawGradientRect(0, 0, this.width, this.height, -1072689136, -804253680);
		} else {
			this.drawBackground(par1);
		}
	}

	/**
	 * Draws the background (i is always 0 as of 1.2.2)
	 */
	public void drawBackground(int par1) {
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_FOG);
		Tessellator var2 = Tessellator.instance;
		this.mc.getTextureManager().bindTexture(optionsBackground);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		float var3 = 32.0F;
		var2.startDrawingQuads();
		var2.setColorOpaque_I(4210752);
		var2.addVertexWithUV(0.0D, (double)this.height, 0.0D, 0.0D, (double)((float)this.height / var3 + (float)par1));
		var2.addVertexWithUV((double)this.width, (double)this.height, 0.0D, (double)((float)this.width / var3), (double)((float)this.height / var3 + (float)par1));
		var2.addVertexWithUV((double)this.width, 0.0D, 0.0D, (double)((float)this.width / var3), (double)par1);
		var2.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, (double)par1);
		var2.draw();
	}

	/**
	 * Returns true if this GUI should pause the game when it is displayed in single-player
	 */
	public boolean doesGuiPauseGame() {
		return true;
	}

	public void confirmClicked(boolean par1, int par2) {}

	// Spout Start
	public void drawWidgets(int x, int y, float z) {
		if (getScreen() == null) {
			return;
		}
		// Draw ALL the widgets!
		screen.render();
		if (shouldShowTooltip()) {
			drawTooltips(x, y);
		}
	}

	// Note: already inside of the sandbox
	protected void drawTooltips(int x, int y) {
		// Draw the tooltip!
		String tooltip = "";
		// Widget tooltipWidget = null;
		for (RenderPriority priority : RenderPriority.values()) {
			for (Widget widget : screen.getAttachedWidgets(true)) { // We need ALL the tooltips now
				if (widget.getPriority() == priority) {
					if (widget.isVisible() && isInBoundingRect(widget, x, y) && widget.getTooltip() != null && !widget.getTooltip().equals("")) {
						if (widget.getScreen() instanceof Scrollable) {
							if (!isInBoundingRect(widget.getScreen(), x, y)) {
								continue;
							}
						}
						tooltip = widget.getTooltip();
						//tooltipWidget = widget;
						// No return here, when a widget that is over it comes next, tooltip will be overwritten.
					} else if (widget.getTooltip() == null && widget.isVisible()) {
						return;
					}
				}
			}
		}

		if (!tooltip.equals("")) {
			drawTooltip(tooltip, x, y);
		}
	}

	public void drawTooltip(String tooltip, int x, int y) {
		GL11.glPushMatrix();
		String lines[] = this.fontRenderer.wrapFormattedStringToWidth(tooltip,(width-22)).split("\n");	//Autowrap tooltips to reported screen width
		int tooltipWidth = 0;
		int tooltipHeight = 8 * lines.length + 3;
		for (String line : lines) {
			tooltipWidth = Math.max(this.fontRenderer.getStringWidth(line), tooltipWidth);
		}
		int offsetX = 0;
		if (x + tooltipWidth > width) {
			offsetX = -tooltipWidth - 11;
			if (offsetX + x < 0) {
				offsetX = -x;
			}
		}
		int offsetY = 0;
		if (y + tooltipHeight + 2 > height) {
			offsetY = -tooltipHeight;
			if (offsetY + y < 0) {
				offsetY = -y;
			}
		}

		x += 6;
		y -= 6;

		int j2 = 0;
		for (int k2 = 0; k2 < lines.length; k2++) {
			int i3 = fontRenderer.getStringWidth(lines[k2]);
			if (i3 > j2) {
				j2 = i3;
			}
		}

		int l2 = x + offsetX;
		int j3 = y + offsetY;
		int k3 = j2;
		int l3 = 8;
		if (lines.length > 1) {
			l3 += (lines.length - 1) * 10;
		}
		// zLevel = 300F;
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

			this.drawGradientRect(x - 3 + offsetX, y - 3 + offsetY, x + tooltipWidth + 3 + offsetX, y + tooltipHeight + offsetY, -1073741824, -1073741824);

			//int i = 0;
			GL11.glColor4f(1f, 1f, 1f, 1f);
			for (String line : lines) {
				this.fontRenderer.drawStringWithShadow(line, l2, j3, -1);
				j3 += 10;
			}
			GL11.glPopMatrix();
	}

	public boolean isInBoundingRect(Widget widget, int x, int y) {
		int left = (int) widget.getActualX();
		int top = (int) widget.getActualY();
		int height = (int) widget.getHeight();
		int width = (int) widget.getWidth();
		int right = left + width;
		int bottom = top + height;
		if (left <= x && x < right && top <= y && y < bottom) {
			return true;
		}
		return false;
	}

	public boolean isInBoundingRect(int widgetX, int widgetY, int height, int width, int x, int y) {
		int left = widgetX;
		int top = widgetY;
		int right = left + width;
		int bottom = top + height;
		if (left <= x && x < right && top <= y && y < bottom) {
			return true;
		}
		return false;
	}

	public Screen getScreen() {
		if (screen == null) {
			ScreenType type = ScreenUtil.getType(this);
			if (type == ScreenType.GAME_SCREEN || type == ScreenType.CUSTOM_SCREEN) {
				return screen;
			}
			screen = new GenericOverlayScreen();
			((OverlayScreen) screen).setScreenType(type);
		}
		return screen;
	}

	protected void buttonClicked(Button btn) {
	}
	// Spout End

	public static boolean isCtrlKeyDown() {
		return Minecraft.isRunningOnMac ? Keyboard.isKeyDown(219) || Keyboard.isKeyDown(220) : Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157);
	}

	public static boolean isShiftKeyDown() {
		return Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54);
	}
}
