/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minecraft.src.dustmod.events;

import net.minecraft.src.dustmod.*;
import net.minecraft.src.*;

/**
 *
 * @author billythegoat101
 */
public class DEHunterVision extends PoweredEvent
{
    public DEHunterVision()
    {
        super();
    }

    @Override
    public void onInit(EntityDust e)
    {
        super.onInit(e);
        ItemStack[] req = new ItemStack[] {new ItemStack(Item.blazePowder, 3), new ItemStack(Item.eyeOfEnder, 1)};
        req = this.sacrifice(e, req);

        if (!checkSacrifice(req) || !takeXP(e, 12))
        {
            e.fizzle();
            return;
        }
    }

    @Override
    public void onTick(EntityDust e)
    {
        super.onTick(e);
//        DustMod.hunterVisionActive = e.data[0] % 2 == 0;
    }

    @Override
    public void onRightClick(EntityDust e, TileEntityDust ted, EntityPlayer p)
    {
        super.onRightClick(e, ted, p);
        e.data[0]++;
    }

    @Override
    public void onUnload(EntityDust e)
    {
        super.onUnload(e);
//        DustMod.hunterVisionActive = false;
    }

    @Override
    public int getStartFuel()
    {
        return dayLength;
    }

    @Override
    public int getMaxFuel()
    {
        return dayLength * 2;
    }

    @Override
    public int getStableFuelAmount(EntityDust e)
    {
        return dayLength;
    }

    @Override
    public boolean isPaused(EntityDust e)
    {
        return e.data[0] % 2 == 1;
    }
}
