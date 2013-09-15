package com.prupe.mcpatcher.ctm;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.minecraft.src.Block;
import net.minecraft.src.Icon;

abstract class CTMUtils$TileOverrideIterator implements Iterator<ITileOverride> {
	private final Block block;
	private Icon currentIcon;
	private ITileOverride[] blockOverrides;
	private ITileOverride[] iconOverrides;
	private final Set<ITileOverride> skipOverrides = new HashSet();
	private int blockPos;
	private int iconPos;
	private boolean foundNext;
	private ITileOverride nextOverride;
	private ITileOverride lastMatchedOverride;

	CTMUtils$TileOverrideIterator(Block block, Icon icon) {
		this.block = block;
		this.currentIcon = icon;
		this.blockOverrides = CTMUtils.access$100()[block.blockID];
		this.iconOverrides = (ITileOverride[])CTMUtils.access$200().get(this.currentIcon.getIconName());
	}

	private void resetForNextPass() {
		this.blockOverrides = null;
		this.iconOverrides = (ITileOverride[])CTMUtils.access$200().get(this.currentIcon.getIconName());
		this.blockPos = 0;
		this.iconPos = 0;
		this.foundNext = false;
	}

	public boolean hasNext() {
		if (this.foundNext) {
			return true;
		} else {
			if (this.iconOverrides != null) {
				while (this.iconPos < this.iconOverrides.length) {
					if (this.checkOverride(this.iconOverrides[this.iconPos++])) {
						return true;
					}
				}
			}

			if (this.blockOverrides != null) {
				while (this.blockPos < this.blockOverrides.length) {
					if (this.checkOverride(this.blockOverrides[this.blockPos++])) {
						return true;
					}
				}
			}

			return false;
		}
	}

	public ITileOverride rNext() {
		if (!this.foundNext) {
			throw new IllegalStateException("next called before hasNext() == true");
		} else {
			this.foundNext = false;
			return this.nextOverride;
		}
	}

	public void remove() {
		throw new UnsupportedOperationException("remove not supported");
	}

	private boolean checkOverride(ITileOverride override) {
		if (override != null && !override.isDisabled() && !this.skipOverrides.contains(override)) {
			this.foundNext = true;
			this.nextOverride = override;
			return true;
		} else {
			return false;
		}
	}

	ITileOverride go() {
		for (int pass = 0; pass < CTMUtils.access$1000(); ++pass) {
			ITileOverride override;
			Icon newIcon;

			do {
				if (!this.hasNext()) {
					return this.lastMatchedOverride;
				}

				override = this.rNext();
				newIcon = this.getTile(override, this.block, this.currentIcon);
			} while (newIcon == null);

			this.lastMatchedOverride = override;
			this.skipOverrides.add(override);
			this.currentIcon = newIcon;
			this.resetForNextPass();
		}

		return this.lastMatchedOverride;
	}

	Icon getIcon() {
		return this.currentIcon;
	}

	abstract Icon getTile(ITileOverride var1, Block var2, Icon var3);
	
}
