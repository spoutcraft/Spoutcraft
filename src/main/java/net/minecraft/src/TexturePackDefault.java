package net.minecraft.src;

public class TexturePackDefault extends TexturePackImplementation {
	public TexturePackDefault() {
		super("default", "Default");
	}

	/**
	 * Load texture pack description from /pack.txt file in the texture pack
	 */
	protected void loadDescription() {
		this.firstDescriptionLine = "The default look of Minecraft";
	}
}
