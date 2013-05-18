package com.prupe.mcpatcher.mod;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import net.minecraft.src.Block;
import net.minecraft.src.Icon;

abstract class CTMUtils$TileOverrideIterator implements Iterator {
	private final Block block;
	private Icon currentIcon;
	private ITileOverride[] blockOverrides;
	private ITileOverride[] iconOverrides;
	private final Set skipOverrides = new HashSet();
	private int blockPos;
	private int iconPos;
	private boolean foundNext;
	private ITileOverride nextOverride;
	private ITileOverride lastMatchedOverride;

	CTMUtils$TileOverrideIterator(Block var1, Icon var2) {
		this.block = var1;
		this.currentIcon = var2;
		this.blockOverrides = CTMUtils.access$200()[var1.blockID];
		this.iconOverrides = (ITileOverride[])CTMUtils.access$300().get(this.currentIcon.getIconName());
	}

	private void resetForNextPass() {
		this.blockOverrides = null;
		this.iconOverrides = (ITileOverride[])CTMUtils.access$300().get(this.currentIcon.getIconName());
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

	public ITileOverride next() {
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

	private boolean checkOverride(ITileOverride var1) {
		if (var1 != null && !var1.isDisabled() && !this.skipOverrides.contains(var1)) {
			this.foundNext = true;
			this.nextOverride = var1;
			return true;
		} else {
			return false;
		}
	}

	ITileOverride go() {
		for (int var1 = 0; var1 < CTMUtils.access$1400(); ++var1) {
			ITileOverride var2;
			Icon var3;

			do {
				if (!this.hasNext()) {
					return this.lastMatchedOverride;
				}

				var2 = this.next();
				var3 = this.getTile(var2, this.block, this.currentIcon);
			} while (var3 == null);

			this.lastMatchedOverride = var2;
			this.skipOverrides.add(var2);
			this.currentIcon = var3;
			this.resetForNextPass();
		}

		return this.lastMatchedOverride;
	}

	Icon getIcon() {
		return this.currentIcon;
	}

	abstract Icon getTile(ITileOverride var1, Block var2, Icon var3);
}