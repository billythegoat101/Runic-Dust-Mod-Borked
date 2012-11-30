/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dustmod.runes;

import dustmod.DustEvent;
import dustmod.EntityDust;
import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityItem;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;

/**
 *
 * @author billythegoat101
 */
public class DECompression extends DustEvent
{
    public DECompression()
    {
        super();
    }
	
	@Override
    public void initGraphics(EntityDust e){
    	super.initGraphics(e);

        e.setRenderBeam(true);
        e.setRenderStar(true);
        e.setColorStarOuter(0, 0, 255);
        e.setColorBeam(0, 0, 255);
    	
    }

    public void onInit(EntityDust e)
    {
        if (!this.takeItems(e, new ItemStack(Block.blockSteel.blockID, 1, -1)))
        {
            e.fizzle();
            return;
        }

        int diamondAmt = 0;
        ItemStack[] req = new ItemStack[]
        {
            new ItemStack(Item.coal, 0, 0)
        };

        while (req[0].stackSize == 0)
        {
            req[0].stackSize = 32;
            req = sacrifice(e, req);

            if (req[0].stackSize <= 0)
            {
                diamondAmt++;
            }

            System.out.println("DERP : " + diamondAmt + " " + req[0].stackSize);
        }

        System.out.println("Diamond amt " + diamondAmt);
        e.data[0] = diamondAmt;
        e.setRenderBeam(true);
        e.setRenderStar(true);
        e.setColorStarOuter(0, 0, 255);
        e.setColorBeam(0, 0, 255);
    }

    public void onTick(EntityDust e)
    {
        e.setStarScale(e.getStarScale() + 0.001F);
        
        if (e.ticksExisted > 20)
        {
            int dAmt = e.data[0];
            int stacks = (dAmt) / 64;
            int leftover = dAmt % 64;
            System.out.println("Dropping " + dAmt + " diamonds in " + stacks + "." + leftover + " stacks");

            for (int i = 0; i < stacks; i++)
            {
                Entity en = null;
                ItemStack create =  new ItemStack(Item.diamond.shiftedIndex, 64, 0);
                en = new EntityItem(e.worldObj, e.posX, e.posY - EntityDust.yOffset, e.posZ, create);

                if (en != null)
                {
                    en.setPosition(e.posX, e.posY - EntityDust.yOffset, e.posZ);
                    e.worldObj.spawnEntityInWorld(en);
                }
            }

            if (leftover > 0)
            {
                Entity en = null;
                ItemStack create =  new ItemStack(Item.diamond.shiftedIndex, leftover, 0);
                en = new EntityItem(e.worldObj, e.posX, e.posY - EntityDust.yOffset, e.posZ, create);

                if (en != null)
                {
                    en.setPosition(e.posX, e.posY - EntityDust.yOffset, e.posZ);
                    e.worldObj.spawnEntityInWorld(en);
                }
            }

            e.fade();
        }
    }
}
