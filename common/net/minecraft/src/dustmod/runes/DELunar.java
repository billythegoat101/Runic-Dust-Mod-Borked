/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minecraft.src.dustmod.runes;

import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.dustmod.DustEvent;
import net.minecraft.src.dustmod.EntityDust;

/**
 *
 * @author billythegoat101
 */
public class DELunar extends DustEvent
{
    public static boolean oneActive = false;
    public void onInit(EntityDust e)
    {
        e.renderBeam = false;
        e.renderStar = true;
        ItemStack[] req = this.sacrifice(e, new ItemStack[] {new ItemStack(Item.netherStalkSeeds, 4), new ItemStack(Item.dyePowder, 1, 4)});

        if (req[0].stackSize != 0 || req[1].stackSize != 0)
        {
            e.fizzle();
            return;
        }

        e.data[0] = (byte)0;
    }

    public void onTick(EntityDust e)
    {
        if (e.data[0] == 1 && !oneActive)
        {
            oneActive = false;
            e.data[0] = 0;
            e.renderBeam = false;
            e.renderStar = true;
        }

        long time = e.worldObj.getWorldTime() + 1000;

        if (e.data[0] == 1 && e.worldObj.isDaytime())
        {
            e.worldObj.setWorldTime(e.worldObj.getWorldTime() + 25);
        }
        else if (e.data[0] == 0 && e.worldObj.isDaytime() && !oneActive)
        {
            oneActive = true;
            e.data[0] = (byte)1;
        }

        if (e.data[0] == 1)
        {
            e.renderBeam = true;
            e.renderStar = false;
        }

        if (e.data[0] == 1 && !e.worldObj.isDaytime())
        {
            e.kill();
        }
    }

    @Override
    protected void onUnload(EntityDust e)
    {
        super.onUnload(e);

        if (e.data[0] == 1)
        {
            oneActive = false;
        }
    }
}