/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minecraft.src.dustmod.runes;

import net.minecraft.src.Block;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
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
public class DELillyBridge extends DustEvent
{
    public DELillyBridge()
    {
        super();
    }

    public void onInit(EntityDust e)
    {
        World world = e.worldObj;
        ItemStack[] req = this.sacrifice(e, new ItemStack[] {new ItemStack(Block.leaves, 4, -1)});

        if (req[0].stackSize != 0)
        {
            e.fizzle();
            return;
        }

        Integer[] fnd = null;

        for (Integer[] i : e.dustPoints)
        {
            TileEntity te = world.getBlockTileEntity(i[0], i[1], i[2]);

            if (te != null && te instanceof TileEntityDust)
            {
                TileEntityDust ted = (TileEntityDust) te;
                int pamt = 6;
                int gamt = 6;

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
                    fnd = i;
                    break;
                }
            }
        }

        for (int x = -1; x <= 1 && fnd != null; x++)
        {
            for (int z = -1; z <= 1; z++)
            {
                if (x == 0 || z == 0)
                {
                    if (DustMod.isDust(world.getBlockId(fnd[0] + x, fnd[1], fnd[2] + z)))
                    {
                        TileEntityDust ted = (TileEntityDust) world.getBlockTileEntity(x + fnd[0], fnd[1], fnd[2] + z);
                        int pamt = 7;
                        int gamt = 7;

                        for (int i = 0; i < 4; i++)
                        {
                            for (int j = 0; j < 4; j++)
                            {
                                if (ted.getDust(i, j) == 1)
                                {
                                    pamt--;
                                }

                                if (ted.getDust(i, j) == 2)
                                {
                                    gamt--;
                                }
                            }
                        }

                        if (pamt == 0 && gamt == 0)
                        {
                            e.posX = (fnd[0] + x) + 0.5D;
                            e.posY = (fnd[1]) + 1.5D + EntityDust.yOffset;
                            e.posZ = (fnd[2] + z) + 0.5D;

                            if (x == -1)
                            {
                                e.rotationYaw = 270;
                            }
                            else if (x == 1)
                            {
                                e.rotationYaw = 90;
                            }
                            else if (z == -1)
                            {
                                e.rotationYaw = 0;
                            }
                            else if (z == 1)
                            {
                                e.rotationYaw = 180;
                            }
                        }
                    }
                }
            }
        }

        e.renderStar = true;
        e.setColorStarOuter(0, 255, 0);
    }

    public void onTick(EntityDust e)
    {
        int period = 20;

        if (e.ticksExisted % period == 0)
        {
            World world = e.worldObj;
            int dist = (int)(e.ticksExisted / period + 1) * 2;
            int y = e.getY() - 1;
            int x = e.getX();
            int z = e.getZ();

            if (e.rotationYaw == 90)
            {
                x -= dist;
            }
            else if (e.rotationYaw == 270)
            {
                x += dist;
            }
            else if (e.rotationYaw == 180)
            {
                z -= dist;
            }
            else if (e.rotationYaw == 0)
            {
                z += dist;
            }

            for (int i = -1; i <= 1; i++)
            {
                if (world.getBlockMaterial(x, y + i - 1, z) == Material.water &&
                        world.getBlockId(x, y + i, z) == 0)
                {
                    world.setBlockWithNotify(x, y + i, z, Block.waterlily.blockID);
                }
            }

//            world.setBlockWithNotify(x,y,z,Block.brick.blockID);
        }

        if (e.ticksExisted > 16 * period)
        {
            e.fade();
        }
    }
}
