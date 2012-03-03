package net.minecraft.src;

import java.io.PrintStream;
import java.util.List;
import net.minecraft.client.Minecraft;

public class GuiConnecting extends GuiScreen
{
	//Spout Start
	private int counter = 0;
	private String currentMsg = null;
	private final String[] highlyInformativeMessages = {
			"Adding Hidden Agendas",
			"Adjusting Bell Curves",
			"Aesthesizing Industrial Areas",
			"Aligning Covariance Matrices",
			"Applying Feng Shui Shaders",
			"Applying Theatre Soda Layer",
			"Asserting Packed Exemplars",
			"Attempting to Lock Back-Buffer",
			"Binding Sapling Root System",
			"Breeding Fauna",
			"Building Data Trees",
			"Bureacritizing Bureaucracies",
			"Calculating Inverse Probability Matrices",
			"Calculating Llama Expectoration Trajectory",
			"Calibrating Blue Skies",
			"Charging Ozone Layer",
			"Coalescing Cloud Formations",
			"Cohorting Exemplars",
			"Collecting Meteor Particles",
			"Compounding Inert Tessellations",
			"Compressing Fish Files",
			"Computing Optimal Bin Packing",
			"Concatenating Sub-Contractors",
			"Containing Existential Buffer",
			"Dialing Mother",
			"Debarking Ark Ramp",
			"Debunching Unionized Commercial Services",
			"Deciding What Message to Display Next",
			"Decomposing Singular Values",
			"Decrementing Tectonic Plates",
			"Deleting Ferry Routes",
			"Depixelating Inner Mountain Surface Back Faces",
			"Depositing Slush Funds",
			"Destabilizing Economic Indicators",
			"Determining Width of Blast Fronts",
			"Deunionizing Bulldozers",
			"Dicing Models",
			"Diluting Livestock Nutrition Variables",
			"Doing A Barrel Roll",
			"Downloading Satellite Terrain Data",
			"Doubting The Spoon",
			"Exposing Flash Variables to Streak System",
			"Extracting Resources",
			"Factoring Pay Scale",
			"Fixing Election Outcome Matrix",
			"Flood-Filling Ground Water",
			"Flushing Pipe Network",
			"Gathering Particle Sources",
			"Generating Jobs",
			"Gesticulating Mimes",
			"Graphing Whale Migration",
			"Hiding Willio Webnet Mask",
			"Hiring Consultant",
			"Implementing Impeachment Routine",
			"Increasing Accuracy of Memory Leaks",
			"Increasing Magmafacation",
			"Initializing Tracking Mechanism",
			"Initializing Breeding Timetable",
			"Initializing Robotic Click-Path AI",
			"Inserting Sublimated Messages",
			"Integrating Curves",
			"Integrating Illumination Form Factors",
			"Integrating Population Graphs",
			"Iterating Cellular Automata",
			"Lecturing Errant Subsystems",
			"Losing The Game",
			"Mixing Genetic Pool",
			"Modeling Object Components",
			"Mopping Occupant Leaks",
			"Normalizing Power",
			"Obfuscating Quigley Matrix",
			"Perturbing Matrices",
			"Pixalating Nude Patch",
			"Polishing Water Highlights",
			"Populating Block Templates",
			"Preparing Sprites for Random Walks",
			"Prioritizing Slimes",
			"Projecting Law Enforcement Pastry Intake",
			"Promising Cake",
			"Realigning Alternate Time Frames",
			"Reconfiguring User Mental Processes",
			"Relaxing Splines",
			"Removing Texture Gradients",
			"Restoring World From Backups",
			"Resolving GUID Conflict",
			"Reticulating Splines",
			"Retracting Phong Shader",
			"Retrieving from Back Store",
			"Reverse Engineering Image Consultant",
			"Routing Neural Network Infanstructure",
			"Scattering Rhino Food Sources",
			"Scrubbing Terrain",
			"Searching for Llamas",
			"Seeding Architecture Simulation Parameters",
			"Sequencing Particles",
			"Setting Advisor Moods",
			"Setting Inner Deity Indicators",
			"Setting Universal Physical Constants",
			"Sonically Enhancing Occupant-Free Timber",
			"Speculating Stock Market Indices",
			"Splatting Transforms",
			"Stratifying Ground Layers",
			"Sub-Sampling Water Data",
			"Synthesizing Gravity", 
			"Synthesizing Wavelets",
			"Time-Compressing Simulator Clock",
			"Unable to Reveal Current Activity",
			"Wanting To Believe",
			"Weathering Landforms",
			"Zeroing Creeper Network",
			"Teaching Wibbly Wobbly Timey Wimey to humans"
	};
	//Spout End

	/** A reference to the NetClientHandler. */
	private NetClientHandler clientHandler;

	/** True if the connection attempt has been cancelled. */
	private boolean cancelled;

	public GuiConnecting(Minecraft par1Minecraft, String par2Str, int par3)
	{
		cancelled = false;
		System.out.println((new StringBuilder()).append("Connecting to ").append(par2Str).append(", ").append(par3).toString());
		par1Minecraft.changeWorld1(null);
		(new ThreadConnectToServer(this, par1Minecraft, par2Str, par3)).start();
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	public void updateScreen()
	{
		if (clientHandler != null)
		{
			clientHandler.processReadPackets();
		}
	}

	/**
	 * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
	 */
	protected void keyTyped(char c, int i)
	{
	}

	/**
	 * Adds the buttons (and other controls) to the screen in question.
	 */
	public void initGui()
	{
		StringTranslate stringtranslate = StringTranslate.getInstance();
		controlList.clear();
		controlList.add(new GuiButton(0, width / 2 - 100, height / 4 + 120 + 12, stringtranslate.translateKey("gui.cancel")));
	}

	/**
	 * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
	 */
	protected void actionPerformed(GuiButton par1GuiButton)
	{
		if (par1GuiButton.id == 0)
		{
			cancelled = true;

			if (clientHandler != null)
			{
				clientHandler.disconnect();
			}

			mc.displayGuiScreen(new GuiMainMenu());
		}
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	public void drawScreen(int par1, int par2, float par3)
	{
		drawDefaultBackground();
		StringTranslate stringtranslate = StringTranslate.getInstance();

		if (clientHandler == null)
		{
			drawCenteredString(fontRenderer, stringtranslate.translateKey("connect.connecting"), width / 2, height / 2 - 50, 0xffffff);
			drawCenteredString(fontRenderer, "", width / 2, height / 2 - 10, 0xffffff);
		}
		else
		{
			drawCenteredString(fontRenderer, stringtranslate.translateKey("connect.authorizing"), width / 2, height / 2 - 50, 0xffffff);
			drawCenteredString(fontRenderer, clientHandler.field_1209_a, width / 2, height / 2 - 10, 0xffffff);
		}

		//Spout Start
		if (counter == 4500 || currentMsg == null) {
			counter = 0;
			currentMsg = highlyInformativeMessages[(new java.util.Random()).nextInt(highlyInformativeMessages.length)];
			currentMsg = org.bukkit.ChatColor.GREEN.toString() + currentMsg + "...";
		}
		else {
			counter++;
		}
		drawString(fontRenderer, currentMsg, 7, height / 2 - 115, 0xffffff);
		//Spout End

		super.drawScreen(par1, par2, par3);
	}

	/**
	 * Sets the NetClientHandler.
	 */
	static NetClientHandler setNetClientHandler(GuiConnecting par0GuiConnecting, NetClientHandler par1NetClientHandler)
	{
		return par0GuiConnecting.clientHandler = par1NetClientHandler;
	}

	/**
	 * Returns true if the connection attempt has been cancelled, false otherwise.
	 */
	static boolean isCancelled(GuiConnecting par0GuiConnecting)
	{
		return par0GuiConnecting.cancelled;
	}

	/**
	 * Gets the NetClientHandler.
	 */
	static NetClientHandler getNetClientHandler(GuiConnecting par0GuiConnecting)
	{
		return par0GuiConnecting.clientHandler;
	}
}
