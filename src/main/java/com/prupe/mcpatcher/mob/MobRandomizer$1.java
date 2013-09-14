package com.prupe.mcpatcher.mob;

import com.prupe.mcpatcher.TexturePackChangeHandler;

final class MobRandomizer$1 extends TexturePackChangeHandler {
	MobRandomizer$1(String x0, int x1) {
		super(x0, x1);
	}

	public void beforeChange() {
		MobRandomizer.access$000().clear();
	}

	public void afterChange() {
		MobRuleList.clear();
		MobOverlay.reset();
		LineRenderer.reset();
	}
}
