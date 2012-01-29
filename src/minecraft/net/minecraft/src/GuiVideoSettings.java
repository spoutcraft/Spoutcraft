package net.minecraft.src;

import java.util.List;
import net.minecraft.client.Minecraft;

public class GuiVideoSettings extends GuiScreen {
	private GuiScreen parentGuiScreen;
	protected String screenTitle;
	private GameSettings guiGameSettings;
	private boolean field_40231_d;
	private static EnumOptions videoOptions[];

	public GuiVideoSettings(GuiScreen guiscreen, GameSettings gamesettings) {
		screenTitle = "Video Settings";
		field_40231_d = false;
		parentGuiScreen = guiscreen;
		guiGameSettings = gamesettings;
	}

	public void initGui() {
		StringTranslate stringtranslate = StringTranslate.getInstance();
		screenTitle = stringtranslate.translateKey("options.videoTitle");
		int i = 0;
		EnumOptions aobj[] = videoOptions;
		int j = aobj.length;
		for (int k = 0; k < j; k++) {
			EnumOptions enumoptions = aobj[k];
			if (!enumoptions.getEnumFloat()) {
				controlList.add(new GuiSmallButton(enumoptions.returnEnumOrdinal(), (width / 2 - 155) + (i % 2) * 160, height / 6 + 24 * (i >> 1), enumoptions, guiGameSettings.getKeyBinding(enumoptions)));
			}
			else {
				controlList.add(new GuiSlider(enumoptions.returnEnumOrdinal(), (width / 2 - 155) + (i % 2) * 160, height / 6 + 24 * (i >> 1), enumoptions, guiGameSettings.getKeyBinding(enumoptions), guiGameSettings.getOptionFloatValue(enumoptions)));
			}
			i++;
		}

		controlList.add(new GuiButton(200, width / 2 - 100, height / 6 + 168, stringtranslate.translateKey("gui.done")));
		field_40231_d = false;
		String aobj2[] = (new String[] {
		            "sun.arch.data.model", "com.ibm.vm.bitmode", "os.arch"
		        });
		String as[] = ((String []) (aobj2));
		int l = as.length;
		int i1 = 0;
		do {
			if (i1 >= l) {
				break;
			}
			String s = as[i1];
			String s1 = System.getProperty(s);
			if (s1 != null && s1.indexOf("64") >= 0) {
				field_40231_d = true;
				break;
			}
			i1++;
		}
		while (true);
	}

	protected void actionPerformed(GuiButton guibutton) {
		if (!guibutton.enabled) {
			return;
		}
		int i = guiGameSettings.guiScale;
		if (guibutton.id < 100 && (guibutton instanceof GuiSmallButton)) {
			guiGameSettings.setOptionValue(((GuiSmallButton)guibutton).returnEnumOptions(), 1);
			guibutton.displayString = guiGameSettings.getKeyBinding(EnumOptions.getEnumOptions(guibutton.id));
		}
		if (guibutton.id == 200) {
			mc.gameSettings.saveOptions();
			mc.displayGuiScreen(parentGuiScreen);
		}
		if (guiGameSettings.guiScale != i) {
			ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
			int j = scaledresolution.getScaledWidth();
			int k = scaledresolution.getScaledHeight();
			setWorldAndResolution(mc, j, k);
		}
	}

	public void drawScreen(int i, int j, float f) {
		drawDefaultBackground();
		drawCenteredString(fontRenderer, screenTitle, width / 2, 20, 0xffffff);
		if (!field_40231_d && guiGameSettings.renderDistance == 0) {
			drawCenteredString(fontRenderer, StatCollector.translateToLocal("options.farWarning1"), width / 2, height / 6 + 144, 0xaf0000);
			drawCenteredString(fontRenderer, StatCollector.translateToLocal("options.farWarning2"), width / 2, height / 6 + 144 + 12, 0xaf0000);
		}
		super.drawScreen(i, j, f);
	}

	static {
		videoOptions = (new EnumOptions[] {
		            EnumOptions.GRAPHICS, EnumOptions.RENDER_DISTANCE, EnumOptions.AMBIENT_OCCLUSION, EnumOptions.FRAMERATE_LIMIT, EnumOptions.ANAGLYPH, EnumOptions.VIEW_BOBBING, EnumOptions.GUI_SCALE, EnumOptions.ADVANCED_OPENGL, EnumOptions.GAMMA, EnumOptions.RENDER_CLOUDS,
		            EnumOptions.PARTICLES
		        });
	}
}
