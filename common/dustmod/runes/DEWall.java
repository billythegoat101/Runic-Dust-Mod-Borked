/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dustmod.runes;

import java.util.List;

import net.minecraft.src.Block;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityFallingSand;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;
import dustmod.DustEvent;
import dustmod.DustMod;
import dustmod.EntityDust;

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
        e.data[0] = (e.rot+1)%2;
        System.out.println("AWRASDF " + e.data[0]);
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

            int entC = 0;
//            for (int w = -width; w <= width; w++)
//            {
//                List<Entity> ents = getEntities(e.worldObj, x + (dir ? w : 0), y + currentHeight-0.5, z + (dir ? 0 : w),1.2D); //world.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getBoundingBoxFromPool((float)x-0.5, y+e.data[0]-1, (float)z-0.5, (float)(x + 1.5), (double)y+e.data[0]+1, (float)(z + 0.5)));
//
//                for (Entity i : ents)
//                {
//                	entC++;
//                	if(!(i instanceof EntityFallingSand))
//                		i.setPosition(Math.floor(i.posX) + 0.5, y + currentHeight+11.5, Math.floor(i.posZ) + 0.5);
//                }
//            }
            System.out.println("TICK " + (y + currentHeight) + " " + entC);

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
