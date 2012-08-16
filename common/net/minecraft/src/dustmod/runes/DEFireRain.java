/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minecraft.src.dustmod.runes;

import net.minecraft.src.*;
import net.minecraft.src.dustmod.*;

/**
 *
 * @author billythegoat101
 */
public class DEFireRain extends PoweredEvent
{
    public DEFireRain()
    {
        super();
    }

    public void onInit(EntityDust e)
    {
        super.onInit(e);
        e.renderStar = true;
        e.setColorStarOuter(255, 0, 0);
        ItemStack[] req = new ItemStack[] {new ItemStack(Item.blazeRod, 4)};
        req = this.sacrifice(e, req);

        if (!checkSacrifice(req))
        {
            e.fizzle();
            return;
        }
    }

    public void onTick(EntityDust e)
    {
        super.onTick(e);
        int rad = 100;
        int amt = 20;

        for (int i = 0; i < amt && e.ticksExisted % 5 == 0; i++)
        {
            EntityArrow ea = new EntityArrow(e.worldObj, e.posX + Math.random() * rad * 2 - rad, 158, e.posZ + Math.random() * rad * 2 - rad);
            ea.motionX = 0;
            ea.motionY = -2D;
            ea.motionZ = 0;
            ea.setFire(100);
            ea.canBePickedUp = 0;
            e.worldObj.spawnEntityInWorld(ea);
        }
    }

    @Override
    public int getStartFuel()
    {
        return dayLength / 2;
    }

    @Override
    public int getMaxFuel()
    {
        return dayLength * 3;
    }

    @Override
    public int getStableFuelAmount(EntityDust e)
    {
        return dayLength / 2;
    }

    @Override
    public boolean isPaused(EntityDust e)
    {
        return false;
    }
}
