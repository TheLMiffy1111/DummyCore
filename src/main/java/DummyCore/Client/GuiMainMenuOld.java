package DummyCore.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.Charsets;
import org.lwjgl.opengl.GL11;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import DummyCore.Utils.DrawUtils;
import DummyCore.Utils.IMainMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.fml.common.FMLCommonHandler;

/**
 * An old(alpha and beta) main menu GUI 
 * @author modbder
 *
 */
public class GuiMainMenuOld extends GuiMainMenu implements IMainMenu{

	private static final ResourceLocation minecraftTitleTextures = new ResourceLocation("textures/gui/title/minecraft.png");
	private String splashText;
	protected static final ResourceLocation buttonTextures = new ResourceLocation("textures/gui/widgets.png");
	private static final Random rand = new Random(); private static final ResourceLocation splashTexts = new ResourceLocation("texts/splashes.txt");
	 
	    @Override
		public void initGui()
	    {
	    	super.initGui();
	        BufferedReader bufferedreader = null;

	        try
	        {
	            ArrayList<String> arraylist = new ArrayList<String>();
	            bufferedreader = new BufferedReader(new InputStreamReader(Minecraft.getMinecraft().getResourceManager().getResource(splashTexts).getInputStream(), Charsets.UTF_8));
	            String s;

	            while ((s = bufferedreader.readLine()) != null)
	            {
	                s = s.trim();

	                if (!s.isEmpty())
	                {
	                    arraylist.add(s);
	                }
	            }

	            if (!arraylist.isEmpty())
	            {
	                do
	                {
	                    this.splashText = arraylist.get(rand.nextInt(arraylist.size()));
	                }
	                while (this.splashText.hashCode() == 125780783);
	            }
	        }
	        catch (IOException ioexception1)
	        {
	            
	        }
	        finally
	        {
	            if (bufferedreader != null)
	            {
	                try
	                {
	                    bufferedreader.close();
	                }
	                catch (IOException ioexception)
	                {
	                    
	                }
	            }
	        }
	    }
	 
	@Override
    public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
        short short1 = 274;
        int k = this.width / 2 - short1 / 2;
        byte b0 = 30;
        this.drawGradientRect(0, 0, this.width, this.height, -2130706433, 16777215);
        this.drawGradientRect(0, 0, this.width, this.height, 0, Integer.MIN_VALUE);
        this.drawBackground(10);
        this.mc.getTextureManager().bindTexture(minecraftTitleTextures);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        {
            this.drawTexturedModalRect(k + 0, b0 + 0, 0, 0, 155, 44);
            this.drawTexturedModalRect(k + 155, b0 + 0, 0, 45, 155, 44);
        }

        //TessellatorWrapper.instance.setColorOpaque_I(-1);
        GL11.glPushMatrix();
        GL11.glTranslatef(this.width / 2 + 90, 70.0F, 0.0F);
        GL11.glRotatef(-20.0F, 0.0F, 0.0F, 1.0F);
        float f1 = 1.8F - MathHelper.abs(MathHelper.sin(Minecraft.getSystemTime() % 1000L / 1000.0F * (float)Math.PI * 2.0F) * 0.1F);
        f1 = f1 * 100.0F / (this.fontRenderer.getStringWidth(this.splashText) + 32);
        GL11.glScalef(f1, f1, f1);
        this.drawCenteredString(this.fontRenderer, this.splashText, 0, -8, -256);
        GL11.glPopMatrix();
        String s = "Minecraft 1.7.10";

        if (this.mc.isDemo())
        {
            s = s + " Demo";
        }

        List<String> brandings = Lists.reverse(FMLCommonHandler.instance().getBrandings(true));
        for (int i = 0; i < brandings.size(); i++)
        {
            String brd = brandings.get(i);
            if (!Strings.isNullOrEmpty(brd))
            {
                this.drawString(this.fontRenderer, brd, 2, this.height - ( 10 + i * (this.fontRenderer.FONT_HEIGHT + 1)), 16777215);
            }
        }
        ForgeHooksClient.renderMainMenu(this, fontRenderer, width, height, "");
        String s1 = "Copyright Mojang AB. Do not distribute!";
        this.drawString(this.fontRenderer, s1, this.width - this.fontRenderer.getStringWidth(s1) - 2, this.height - 10, -1);

        int k1;

        for (k1 = 0; k1 < this.buttonList.size(); ++k1)
        {
            drawButton(((GuiButton)this.buttonList.get(k1)),this.mc, p_73863_1_, p_73863_2_);
        }

        for (k1 = 0; k1 < this.labelList.size(); ++k1)
        {
            ((GuiLabel)this.labelList.get(k1)).drawLabel(this.mc, p_73863_1_, p_73863_2_);
        }
    }
	
    public void drawButton(GuiButton button,Minecraft p_146112_1_, int p_146112_2_, int p_146112_3_)
    {
        if (button.enabled)
        {
            FontRenderer fontrenderer = p_146112_1_.fontRenderer;
            p_146112_1_.getTextureManager().bindTexture(buttonTextures);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            boolean field_146123_n = p_146112_2_ >= button.x && p_146112_3_ >= button.y && p_146112_2_ < button.x + button.width && p_146112_3_ < button.y + button.height;
            int k = DrawUtils.getButtonHoverState(button, field_146123_n);
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            this.drawTexturedModalRect(button.x, button.y, 0, 46 + k * 20, button.width / 2, button.height);
            this.drawTexturedModalRect(button.x + button.width / 2, button.y, 200 - button.width / 2, 46 + k * 20, button.width / 2, button.height);
            int l = 14737632;

            if (button.packedFGColour != 0)
            {
                l = button.packedFGColour;
            }
            else if (!button.enabled)
            {
                l = 10526880;
            }
            else if (DrawUtils.getButtonHoverState(button, true) == 0)
            {
                l = 16777120;
            }

            this.drawCenteredString(fontrenderer, button.displayString, button.x + button.width / 2, button.y + (button.height - 8) / 2, l);
        }
    }
}
