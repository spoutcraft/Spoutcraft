package net.minecraft.src;

//Spout Start
import org.getspout.spout.gui.controls.GuiPluginControls;
import org.spoutcraft.spoutcraftapi.gui.Button;
import org.spoutcraft.spoutcraftapi.gui.GenericButton;
import org.spoutcraft.spoutcraftapi.gui.WidgetAnchor;
//Spout End

public class GuiControls extends GuiScreen
{



	private GuiScreen parentScreen;
    protected String screenTitle;
    private GameSettings options;
    private int buttonId;
    
    public GuiControls(GuiScreen guiscreen, GameSettings gamesettings)
    {
        screenTitle = "Controls";
        buttonId = -1;
        parentScreen = guiscreen;
        options = gamesettings;
    }

    private int func_20080_j()
    {
        return width / 2 - 155;
    }

    public void initGui()
    {
        StringTranslate stringtranslate = StringTranslate.getInstance();
        int i = func_20080_j();
        for(int j = 0; j < options.keyBindings.length; j++)
        {
            controlList.add(new GuiSmallButton(j, i + (j % 2) * 160, height / 6 + 24 * (j >> 1), 70, 20, options.getOptionDisplayString(j)));
        }

        controlList.add(new GuiButton(200, width / 2 - 205, height / 6 + 168, stringtranslate.translateKey("gui.done"))); // Spout
        controlList.add(new GuiButton(201, width / 2 +   5, height / 6 + 168, "Plugin Controls")); // Spout
        screenTitle = stringtranslate.translateKey("controls.title");

    }

    protected void actionPerformed(GuiButton guibutton)
    {
        for(int i = 0; i < options.keyBindings.length; i++)
        {
            ((GuiButton)controlList.get(i)).displayString = options.getOptionDisplayString(i);
        }
        //Spout rewritten Start
        switch(guibutton.id){
        case 200:
        	mc.displayGuiScreen(parentScreen);
        	break;
    	case 201:
    		mc.displayGuiScreen(new GuiPluginControls(this));
    		break;
    	default:
    		buttonId = guibutton.id;
            guibutton.displayString = (new StringBuilder()).append("> ").append(options.getOptionDisplayString(guibutton.id)).append(" <").toString();
            break;
        }
        //Spout End
    }

    protected void mouseClicked(int i, int j, int k)
    {
        if(buttonId >= 0)
        {
            options.setKeyBinding(buttonId, -100 + k);
            ((GuiButton)controlList.get(buttonId)).displayString = options.getOptionDisplayString(buttonId);
            buttonId = -1;
            KeyBinding.resetKeyBindingArrayAndHash();
        } else
        {
            super.mouseClicked(i, j, k);
        }
    }

    protected void keyTyped(char c, int i)
    {
        if(buttonId >= 0)
        {
            options.setKeyBinding(buttonId, i);
            ((GuiButton)controlList.get(buttonId)).displayString = options.getOptionDisplayString(buttonId);
            buttonId = -1;
            KeyBinding.resetKeyBindingArrayAndHash();
        } else
        {
            super.keyTyped(c, i);
        }
    }

    public void drawScreen(int i, int j, float f)
    {
        drawDefaultBackground();
        drawCenteredString(fontRenderer, screenTitle, width / 2, 20, 0xffffff);
        int k = func_20080_j();
        for(int l = 0; l < options.keyBindings.length; l++)
        {
            boolean flag = false;
            for(int i1 = 0; i1 < options.keyBindings.length; i1++)
            {
                if(i1 != l && options.keyBindings[l].keyCode == options.keyBindings[i1].keyCode)
                {
                    flag = true;
                }
            }

            int j1 = l;
            if(buttonId == l)
            {
                ((GuiButton)controlList.get(j1)).displayString = "\247f> \247e??? \247f<";
            } else
            if(flag)
            {
                ((GuiButton)controlList.get(j1)).displayString = (new StringBuilder()).append("\247c").append(options.getOptionDisplayString(j1)).toString();
            } else
            {
                ((GuiButton)controlList.get(j1)).displayString = options.getOptionDisplayString(j1);
            }
            drawString(fontRenderer, options.getKeyBindingDescription(l), k + (l % 2) * 160 + 70 + 6, height / 6 + 24 * (l >> 1) + 7, -1);
        }

        super.drawScreen(i, j, f);
    }
}
