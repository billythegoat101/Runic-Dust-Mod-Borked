/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minecraft.src.dustmod;

import java.util.Random;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ModLoader;

import org.lwjgl.opengl.GL11;

/**
 *
 * @author billythegoat101
 */
public class GuiTome extends GuiScreen
{
	
    public static int page = 0;

    /** The X size of the inventory window in pixels. */
    protected int xSize;

    /** The Y size of the inventory window in pixels. */
    protected int ySize;

    /**
     * Starting X position for the Gui. Inconsistent use for Gui backgrounds.
     */
    protected int guiLeft;

    /**
     * Starting Y position for the Gui. Inconsistent use for Gui backgrounds.
     */
    protected int guiTop;


    public GuiButton button;
//    public GuiTextField nameField;

    public int offX;

    public static boolean showSacrifices = true;
    public ItemStack itemstack;
    public GuiTome(ItemStack itemstack)
    {
        super();
        this.itemstack = itemstack;
        xSize = 206;//176;
        ySize = 166;
        offX = xSize / 4;
    }

    @Override
    public void drawScreen(int par1, int par2, float par3)
    {
        super.drawScreen(par1, par2, par3);
        drawDefaultBackground();
        drawGuiContainerBackgroundLayer(par3, par1, par2);
        drawGuiContainerForegroundLayer();

        for (int i = 0; i < controlList.size(); i++)
        {
            GuiButton guibutton = (GuiButton)controlList.get(i);
            guibutton.drawButton(mc, par1, par2);
        }
    }

    @Override
    public void initGui()
    {
        super.initGui();
        guiLeft = (width - xSize) / 2;
        guiTop = (height - ySize) / 2;
        this.controlList.add(button = new GuiButton(1, (width + xSize) / 2 + 2 - offX, (height - ySize) / 2 + 2 + ySize - 20, (width - xSize) / 2 + offX - 2, 20, "Description >"));
//        nameField = new GuiTextField(this.fontRenderer, (width-xSize)/2 - offX,(height-ySize)/2-fontRenderer.FONT_HEIGHT-2, xSize,12);
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed()
    {
    }


    String[] derp = new String[]{"Hi", "I should get rid of this page...", 
    		"Hope you enjoy!", "Make some runes!","Space for rent.", 
    		"Modders: Make custom runes!", "Insert joke here.","Direwolf20 is cool!",
    		"Notch is cool!","Jeb_ is cool!", "Stop annoying LexManos!","Go play Thaumcraft.", 
    		"Don't play tekkit!"};
    int randAuthor = (int)(Math.random()*derp.length);
    /**
     * Draw the foreground layer for the GuiContainer (everythin in front of the items)
     */
    protected void drawGuiContainerForegroundLayer()
    {
        String name = "";
        String author = "";
        String notes = "";
        boolean recolor = false;

        if (getPage() == 0)
        {
            name = "Legend";
            notes = "\n\n\n"
                    + "Meat: Pork, Beef, or Chicken raw or cooked.\n---\n"
                    + "Drops: Any item corresponding to a particular mob.\n---\n"
                    + "Variable: The dust is interchangable and allows you to set traits of the rune.\n---\n"
                    + "Powered: If the name is red, then it requires fueling via smeltables.";
            
            author = derp[randAuthor];
        }
        else
        {
            DustShape shape = DustManager.getShape(getPage() - 1);
            name = shape.getRuneName();
            notes = showSacrifices ? shape.getNotes() : shape.getDescription();
            author = "by " + shape.getAuthor();
            if (shape.isPower)
            {
                recolor = true;
            }
            Random rand = new Random();
            randAuthor = (int)(rand.nextInt(derp.length));
        }

        GL11.glColor3f(255, 0, 0);
        fontRenderer.drawString(name, (width - xSize) / 2 - offX, (height - ySize) / 2 - fontRenderer.FONT_HEIGHT - 2, recolor ? 0xFF0000 : 0xEEEEEE);
        fontRenderer.drawSplitString(notes, (width + xSize) / 2 + 2 - offX, (height - ySize) / 2 + 2, (width - xSize) / 2 + offX, 0xffa0a0a0);
        GL11.glPushMatrix();
        float scale = 0.6666F;
        GL11.glTranslated((width - xSize) / 2 - offX, (height - ySize) / 2 + ySize, 0);
        GL11.glScalef(scale,scale,scale);
        fontRenderer.drawString(author, 0,0, 0xffa0a0a0);
        GL11.glPopMatrix();
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        super.updateScreen();
    }
    
    public void setPage(int p){
    	page = p;
//    	itemstack.setItemDamage(p);
    }

    public int getPage(){
    	return page;
//    	return itemstack.getItemDamage();
    }
    
    @Override
    protected void keyTyped(char par1, int key)
    {
        super.keyTyped(par1, key);

        if (key == 1 || key == this.mc.gameSettings.keyBindInventory.keyCode)
        {
            this.mc.thePlayer.closeScreen();
//        	this.mc.displayGuiScreen((GuiScreen)null);
        }

        if (key == mc.gameSettings.keyBindLeft.keyCode)
        {
            retreatPage();
        }
        else if (key == mc.gameSettings.keyBindRight.keyCode)
        {
            advancePage();
        }

        if (DustMod.debug && key == mc.gameSettings.keyBindChat.keyCode)
        {
            EntityPlayer player = ModLoader.getMinecraftInstance().thePlayer;
            int scroll = 0;

            if (getPage() != 0)
            {
                scroll = DustManager.getShape(getPage() - 1).id;
                ItemStack to = new ItemStack(DustMod.dustScroll, 1, scroll);
                player.inventory.addItemStackToInventory(to);
            }
            else
            {
                ItemStack to = new ItemStack(DustMod.negateSacrifice, 64);
                player.inventory.addItemStackToInventory(to);
            }
        }
    }
    /**
     * Called when the mouse is clicked.
     */
    protected void mouseClicked(int x, int y, int m)
    {
        super.mouseClicked(x, y, m);

        if (y >= (height / 2 - ySize / 2) && y <= (height / 2 + ySize / 2))
        {
            if (x >= width / 2 - xSize / 2 - offX && x <= width / 2 + xSize / 2 - offX)
            {
                if (m == 0)
                {
                    advancePage();
                }
                else if (m == 1)
                {
                    retreatPage();
                }
            }
        }

//        System.out.println("Click " + par1 + " " + par2 + " " + par3 + " " + width + " " + height);
    }
    private void advancePage()
    {
//        itemstack.setItemDamage(itemstack.getItemDamage() + 1);
    	setPage(getPage() + 1);

        if (getPage() >= DustManager.getShapes().size() - DustMod.numSec + 1)
        {
        	setPage(0);
        }
    }

    private void retreatPage()
    {
        setPage(getPage()-1);

        if (getPage() < 0)
        {
        	setPage(DustManager.getShapes().size() - DustMod.numSec);
//        	itemstack.setItemDamage(DustManager.getShapes().size() - DustMod.numSec);
//            page = DustManager.getShapes().size() - DustMod.numSec;
        }
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the items)
     */
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3)
    {
        int i = mc.renderEngine.getTexture("/dust/tomeGui.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(i);
//        mc.renderEngine.bindTexture(mc.renderEngine.getTexture(RenderDustTable.getPagePath(page)));
        int j = (width - xSize) / 2 - offX;
        int k = (height - ySize) / 2;
        int pageWidth = 70;
        int pageHeight = 56;
        int ox = 4;
        int oy = 4;
        float scalex = (float)(xSize - ox * 2) / 256F;
        float scaley = (float)(ySize - oy * 2) / 256F;
        float res = xSize / ySize;
        drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
        GL11.glPushMatrix();
        GL11.glScalef(1 / res, res, 1);
        GL11.glTranslatef(j + ox, k + oy, 0);
        GL11.glScalef(scalex, scaley, 1f);
//        System.out.println("Scale " + scalex + " " + scaley);
        if(getPage() == 0)
            mc.renderEngine.bindTexture(mc.renderEngine.getTexture(DustMod.path + "/pages/info.png"));
        else PageHelper.bindExternalTexture(PageHelper.folder + RenderDustTable.getPageName(getPage()) + ".png");
        drawTexturedModalRect(0, 0, 0, 0, 256, 256);
        GL11.glPopMatrix();
    }

    @Override
    protected void actionPerformed(GuiButton but)
    {
        if (but == button)
        {
            showSacrifices = !showSacrifices;

            if (showSacrifices)
            {
                but.displayString = "Description >";
            }
            else
            {
                but.displayString = "< Information";
            }
        }
        else
        {
            super.actionPerformed(but);
        }
    }
}
