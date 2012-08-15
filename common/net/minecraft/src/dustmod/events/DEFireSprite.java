/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minecraft.src.dustmod.events;

import java.util.List;

import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.dustmod.EntityDust;
import net.minecraft.src.dustmod.PoweredEvent;

/**
 *
 * @author billythegoat101
 */
public class DEFireSprite extends PoweredEvent
{
    public DEFireSprite()
    {
        super();
    }

    public void onInit(EntityDust e)
    {
        super.onInit(e);
        e.renderStar = true;
        e.follow = true;
        e.setColorInner(255, 0, 0);
        ItemStack[] sacrifice = new ItemStack[] {new ItemStack(Item.ghastTear, 1), new ItemStack(Item.fireballCharge, 2)};
        this.sacrifice(e, sacrifice);

        if (!checkSacrifice(sacrifice) || !takeXP(e, 22))
        {
            e.fizzle();
            return;
        }
    }

    public void onTick(EntityDust e)
    {
        super.onTick(e);
        e.setColorOuter(255, 0, 0);
        
        EntityPlayer player = e.worldObj.getPlayerEntityByName(e.summonerUN);

//        if (true/*!e.worldObj.multiplayerWorld*/)
//        {
//            player = ModLoader.getMinecraftInstance().thePlayer;
//        }

        if (player == null)
        {
            e.data[0] = 1;
            return;
        }else {
            e.data[0] = 0;
        }
            

        e.setFire(0);
        int rad = 3;
        List<Entity> kill = getEntities(e, rad);

        for (Entity k: kill)
        {
            if (k == player || k == e)
            {
                continue;
            }

            if (k instanceof EntityLiving)
            {
                k.setFire(2 + (int)(Math.random() * 5));
            }
        }

        if (e.ticksExisted % 100 == 0 && Math.random() < 0.5)
        {
//            System.out.println("ignite");
            int ex = e.getX();
            int ey = e.getY();
            int ez = e.getZ();
            boolean ignited = false;

            for (int x = -rad; x <= rad && !ignited; x++)
            {
                for (int y = -rad; y <= rad && !ignited; y++)
                {
                    for (int z = -rad; z <= rad && !ignited; z++)
                    {
                        if (e.worldObj.getBlockId(ex + x, ey + y - 1, ez + z) != 0 && e.worldObj.getBlockId(ex + x, ey + y, ez + z) == 0 && Math.random() < 0.05D)
                        {
                            e.worldObj.setBlockWithNotify(ex + x, ey + y, ez + z, Block.fire.blockID);
                            ignited = true;
                        }
                    }
                }
            }
        }
    }

    @Override
    public int getStartFuel()
    {
        return dayLength * 3;
    }

    @Override
    public int getMaxFuel()
    {
        return dayLength * 7;
    }

    @Override
    public int getStableFuelAmount(EntityDust e)
    {
        return dayLength + dayLength / 2;
    }

    @Override
    public boolean isPaused(EntityDust e)
    {
        return e.data[0] == 1;
    }
}
