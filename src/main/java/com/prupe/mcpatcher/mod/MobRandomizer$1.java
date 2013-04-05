package com.prupe.mcpatcher.mod;

import com.prupe.mcpatcher.TexturePackChangeHandler;

final class MobRandomizer$1 extends TexturePackChangeHandler {
	MobRandomizer$1(String var1, int var2) {
		super(var1, var2);
	}

	public void beforeChange() {
		MobRandomizer.access$000().clear();
	}

	public void afterChange() {
		MobRuleList.clear();
		MobOverlay.reset();
	}
}
