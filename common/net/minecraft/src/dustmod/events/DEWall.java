/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minecraft.src.dustmod.events;

import java.util.List;

import net.minecraft.src.Block;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.Entity;
import net.minecraft.src.ItemStack;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;
import net.minecraft.src.dustmod.DustEvent;
import net.minecraft.src.dustmod.DustMod;
import net.minecraft.src.dustmod.EntityDust;
import net.minecraft.src.dustmod.TileEntityDust;

/**
 *
 * @author billythegoat101
 */
public class DEWall extends DustEvent
{
    public static final int ticksperblock = 7;

    public void onInit(EntityDust e)
    {
        e.ignoreRune = true;
        ItemStack[] req = this.sacrifice(e, new ItemStack[] {new ItemStack(Block.oreIron, 5)});

        if (req[0].stackSize != 0 || !this.takeXP(e, 3))
        {
            e.fizzle();
            return;
        }

        World world = e.worldObj;
        Integer[][] fPoints = new Integer[2][3];
        int fPiter = 0;

        for (Integer[] i : e.dustPoints)
        {
            TileEntity te = world.getBlockTileEntity(i[0], i[1], i[2]);

            if (te != null && te instanceof TileEntityDust)
            {
                TileEntityDust ted = (TileEntityDust) te;
                int pamt = 5;
                int gamt = 5;

//                System.out.println("CHECKING");
                for (int x = 0; x < 4; x++)
                {
                    for (int y = 0; y < 4; y++)
                    {
//                        System.out.print(ted.getDust(x, y) + ",");
                        if (ted.getDust(x, y) == 1)
                        {
                            pamt--;
                        }

                        if (ted.getDust(x, y) == 2)
                        {
                            gamt--;
                        }
                    }

//                    System.out.println();
                }

                if (pamt == 0 && gamt == 0)
                {
                    fPoints[fPiter] = i;
                    fPiter++;

                    if (fPiter >= 2)
                    {
                        break;
                    }
                }
            }
        }

        int dx = fPoints[0][0] - fPoints[1][0];
        int dz = fPoints[0][2] - fPoints[1][2];

//        System.out.println("Deltas " + dx + " " + dz);
        if (dx == 0 && dz != 0)
        {
            e.data[0] = 1;
        }
        else if (dz == 0 && dx != 0)
        {
            e.data[0] = 0;
        }
        else
        {
            e.fizzle();
            return;
        }
    }

    public void onTick(EntityDust e)
    {
        if (e.ticksExisted % ticksperblock == 0)
        {
            World world = e.worldObj;
            int currentHeight = (int)(e.ticksExisted / ticksperblock);
//            System.out.println("Current height " + currentHeight);
            int x = (int) e.getX();
            int y = (int) e.getY();
            int z = (int) e.getZ();
            boolean dir = e.data[0] == 0;
            int width = 2;
            int height = 8;

            for (int w = -width; w <= width; w++)
            {
                List<Entity> ents = getEntities(e.worldObj, x + (dir ? w : 0), y + currentHeight, z + (dir ? 0 : w), 1.0D); //world.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getBoundingBoxFromPool((float)x-0.5, y+e.data[0]-1, (float)z-0.5, (float)(x + 1.5), (double)y+e.data[0]+1, (float)(z + 0.5)));

                for (Entity i : ents)
                {
                    i.setPosition(i.posX, y + currentHeight + 1.8, i.posZ);
                }
            }

            for (int t = -height; t <= height + 1; t++)
            {
                for (int w = -width; w <= width; w++)
                {
                    if (y - t + currentHeight <= 0)
                    {
                        e.fade();
                        return;
                    }

                    int b = world.getBlockId(x + (dir ? w : 0), y - t + currentHeight, z + (dir ? 0 : w));
                    int m = world.getBlockId(x + (dir ? w : 0), y - t + currentHeight, z + (dir ? 0 : w));
                    int nb = world.getBlockId(x + (dir ? w : 0), y - t + currentHeight + 1, z + (dir ? 0 : w));
                    Block B = Block.blocksList[b];
                    Block nB = Block.blocksList[nb];

                    if (B == DustMod.dust)
                    {
                        b = 0;
                        B = null;
                    }
                    else if (nB == DustMod.dust)
                    {
                        nb = 0;
                        nB = null;
                    }

                    if ((B != null && B instanceof BlockContainer) || (nB != null && nB instanceof BlockContainer))
                    {
                        e.fade();
                        return;
                    }

                    world.setBlockAndMetadataWithNotify(x + (dir ? w : 0), y - t + currentHeight + 1, z + (dir ? 0 : w), b, m);
                    world.setBlockWithNotify(x + (dir ? w : 0), y - t + currentHeight, z + (dir ? 0 : w), /*(t == height+1) ? Block.brick.blockID:*/0);
                }
            }

            if (currentHeight > 4)
            {
                e.fade();
            }
        }
    }
}
