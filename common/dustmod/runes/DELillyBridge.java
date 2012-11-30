/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dustmod.runes;

import dustmod.DustEvent;
import dustmod.DustMod;
import dustmod.EntityDust;
import dustmod.TileEntityDust;
import net.minecraft.src.Block;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

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
	
	@Override
    public void initGraphics(EntityDust e){
    	super.initGraphics(e);

		e.setRenderStar(true);
        e.setColorStarOuter(0, 255, 0);
		
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

        e.rotationYaw = ((e.rot+1)%4)*90;

		e.setRenderStar(true);
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
