package net.minecraft.src;

import java.util.List;

// Spout Start
import org.spoutcraft.client.gui.inventory.CreativeTabCustom;
// Spout End

public class CreativeTabs {
	public static final CreativeTabs[] creativeTabArray = new CreativeTabs[13]; // Spout - one moar
	public static final CreativeTabs tabBlock = new CreativeTabBlock(0, "buildingBlocks");
	public static final CreativeTabs tabDecorations = new CreativeTabDeco(1, "decorations");
	public static final CreativeTabs tabRedstone = new CreativeTabRedstone(2, "redstone");
	public static final CreativeTabs tabTransport = new CreativeTabTransport(3, "transportation");
	public static final CreativeTabs tabMisc = new CreativeTabMisc(4, "misc");
	public static final CreativeTabs tabAllSearch = (new CreativeTabSearch(5, "search")).setBackgroundImageName("search.png");
	public static final CreativeTabs tabFood = new CreativeTabFood(6, "food");
	public static final CreativeTabs tabTools = new CreativeTabTools(7, "tools");
	public static final CreativeTabs tabCombat = new CreativeTabCombat(8, "combat");
	public static final CreativeTabs tabBrewing = new CreativeTabBrewing(9, "brewing");
	public static final CreativeTabs tabMaterials = new CreativeTabMaterial(10, "materials");
	public static final CreativeTabs tabInventory = (new CreativeTabInventory(11, "inventory")).setBackgroundImageName("survival_inv.png").setNoScrollbar().setNoTitle();
	public static final CreativeTabs tabSpout = new CreativeTabCustom(12, "custom"); // Spout
	private final int tabIndex;
	private final String tabLabel;

	/** Texture to use. */
	private String backgroundImageName = "list_items.png";
	private boolean hasScrollbar = true;

	/** Whether to draw the title in the foreground of the creative GUI */
	private boolean drawTitle = true;

	public CreativeTabs(int par1, String par2Str) {
		this.tabIndex = par1;
		this.tabLabel = par2Str;
		creativeTabArray[par1] = this;
	}

	public int getTabIndex() {
		return this.tabIndex;
	}

	public String getTabLabel() {
		return this.tabLabel;
	}

	/**
	 * Gets the translated Label.
	 */
	public String getTranslatedTabLabel() {
		return StringTranslate.getInstance().translateKey("itemGroup." + this.getTabLabel());
	}

	public Item getTabIconItem() {
		return Item.itemsList[this.getTabIconItemIndex()];
	}

	/**
	 * the itemID for the item to be displayed on the tab
	 */
	public int getTabIconItemIndex() {
		return 1;
	}

	public String getBackgroundImageName() {
		return this.backgroundImageName;
	}

	public CreativeTabs setBackgroundImageName(String par1Str) {
		this.backgroundImageName = par1Str;
		return this;
	}

	public boolean drawInForegroundOfTab() {
		return this.drawTitle;
	}

	public CreativeTabs setNoTitle() {
		this.drawTitle = false;
		return this;
	}

	public boolean shouldHidePlayerInventory() {
		return this.hasScrollbar;
	}

	public CreativeTabs setNoScrollbar() {
		this.hasScrollbar = false;
		return this;
	}

	/**
	 * returns index % 6
	 */
	public int getTabColumn() {
		return this.tabIndex % 6;
	}

	/**
	 * returns tabIndex < 6
	 */
	public boolean isTabInFirstRow() {
		return this.tabIndex < 6;
	}

	/**
	 * only shows items which have tabToDisplayOn == this
	 */
	public void displayAllReleventItems(List par1List) {
		Item[] var2 = Item.itemsList;
		int var3 = var2.length;

		for (int var4 = 0; var4 < var3; ++var4) {
			Item var5 = var2[var4];

			if (var5 != null && var5.getCreativeTab() == this) {
				var5.getSubItems(var5.shiftedIndex, this, par1List);
			}
		}
	}
}
