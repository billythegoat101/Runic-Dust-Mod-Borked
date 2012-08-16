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
public class DETimeLock extends PoweredEvent
{
    public DETimeLock()
    {
        super();
    }

    public void onInit(EntityDust e)
    {
        super.onInit(e);
        ItemStack[] req = new ItemStack[] {new ItemStack(Block.obsidian, 8), new ItemStack(Item.slimeBall, 4), new ItemStack(Item.dyePowder, 1, 4)};
        req = this.sacrifice(e, req);

        if (!checkSacrifice(req))
        {
            e.fizzle();
            return;
        }

        e.renderStar = true;
        e.setColorInner(0, 255, 255);
        e.setColorOuter(0, 255, 255);
        int[] center = new int[4];
        int[][] dusts = e.dusts;
        center[0] = dusts[3][2];
        center[1] = dusts[4][2];
        center[2] = dusts[3][3];
        center[3] = dusts[4][3];

        for (int i = 0; i < 4; i++)
        {
            if (center[0] != center[i])
            {
                e.fizzle();
                return;
            }
        }

        switch (center[0])
        {
            case 1:
                e.data[0] = 9000;
                break;

            case 2:
                e.data[0] = 18000;
                break;

            case 3:
                e.data[0] = 24000;
                break;

            case 4:
                e.data[0] = 24000 * 2;
                break;
        }
    }

    public void onTick(EntityDust e)
    {
        super.onTick(e);
        e.setColorOuter(0, 255, 255);

        if (e.ram == 0)
        {
            if (e.worldObj.getWorldTime() > Integer.MAX_VALUE)
            {
                e.ram = (int)e.worldObj.getWorldTime() % 24000;
            }
            else
            {
                e.ram = (int)e.worldObj.getWorldTime();
            }
        }

//        System.out.println("Delta : " + (e.worldObj.getWorldTime()-e.ram));
        e.worldObj.setWorldTime(e.ram);
        e.ticksExisted = 100;
        e.data[0]--;

//        System.out.println("DATA: " + e.data[0]);
        if (e.data[0] <= 0)
        {
            e.fade();
        }
    }

    @Override
    public int getStartFuel()
    {
        return dayLength;
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
