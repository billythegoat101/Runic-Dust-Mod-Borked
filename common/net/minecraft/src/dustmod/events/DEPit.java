/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minecraft.src.dustmod.events;

import java.util.List;
import net.minecraft.src.*;
import net.minecraft.src.dustmod.*;

/**
 *
 * @author billythegoat101
 */
public class DEPit extends DustEvent
{
    public DEPit()
    {
        super();
    }

    public void onInit(EntityDust e)
    {
        int dustID = e.dustID;
        int dist = 1;

        switch (dustID)
        {
            case 1:
                dist = 8;
                break;

            case 2:
                dist = 16;
                break;

            case 3:
                dist = 20;
                break;

            case 4:
                dist = 48;
                break;
        }

        boolean advanced = (dustID > 2);
        ItemStack[] sac = new ItemStack[] {new ItemStack(!advanced ? Block.wood.blockID : Item.ingotIron.shiftedIndex, 2, -1)};
        sac = this.sacrifice(e, sac);

        if (!this.checkSacrifice(sac))
        {
            e.fizzle();
            return;
        }

        int x = e.getX();
        int y = e.getY() - 1;
        int z = e.getZ();
        World world = e.worldObj;

//            world.setBlock(x, y-1, z, Block.brick.blockID);
        if (world.getBlockId(x, y, z) != 0)
        {
            e.fizzle();
            return;
        }

        for (int dy = 0; dy <= dist; dy++)
        {
            int bid = world.getBlockId(x, y - dy, z);
            Block block = Block.blocksList[bid];

//            System.out.println("DERPBLOCK " + bid +" [" + (i+dy) + "," + (i+dj) + "," + (k+dk) + "] ");
            if (block != null && bid != Block.bedrock.blockID)
            {
                block.onBlockDestroyedByPlayer(world, x, y - dy, z, world.getBlockMetadata(x, y - dy, z));
                block.dropBlockAsItem(world, x, y - dy, z, world.getBlockMetadata(x, y - dy, z), 0);
                world.setBlockWithNotify(x, y - dy, z, 0);
            }
        }

        world.addWeatherEffect(new EntityLightningBolt(world, x, y, z));
    }

    public void onTick(EntityDust e)
    {
        List<Entity> ents = this.getEntities(e, 5D);

        for (Entity i: ents)
        {
            if (i instanceof EntityPlayer)
            {
                ((EntityPlayer)i).extinguish();
            }
        }

        if (e.ticksExisted > 5)
        {
            e.kill();
        }
    }
}
