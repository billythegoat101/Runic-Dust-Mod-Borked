/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dustmod.runes;

import java.util.List;

import dustmod.*;
import net.minecraft.src.*;

/**
 *
 * @author billythegoat101
 */
public class DESpawnTorch extends DustEvent
{
    public DESpawnTorch()
    {
        super();
    }

    public void onInit(EntityDust e)
    {
        e.data[0] = 0;
        ItemStack[] sac = new ItemStack[] {new ItemStack(Item.flint, 1)};
        sac = this.sacrifice(e, sac);

        if (sac[0].stackSize <= 0)
        {
            e.data[0] = 1;
        }

        World world = e.worldObj;
        int x = e.getX();
        int y = e.getY();
        int z = e.getZ();

        if (e.data[0] == 1)
        {
            e.renderBeam = true;
            e.setColorBeam(255,255,255);
        }
        else
        {
            e.posY += 0.35;
            e.renderStar = true;
            e.ignoreRune = true;
            world.setBlock(x, y, z, 0);
            world.setBlockAndMetadataWithNotify(x, y, z, Block.torchWood.blockID, 0);
        }
    }

    @Override
    protected void onTick(EntityDust e)
    {
        super.onTick(e);

        if(e.data[0] == 1 && e.ticksExisted%10 == 0){
            List<EntityItem> items = this.getItems(e);
            for(EntityItem i:items){
                ItemStack item = i.item;
                if(item.itemID == Item.dyePowder.shiftedIndex && e.data[1] != item.getItemDamage()){
                    e.data[1] = item.getItemDamage();
                    int[] color = this.getColor(item.getItemDamage());
                    e.setColorBeam(color[0], color[1], color[2]);
                    item.stackSize--;
                    if(item.stackSize <= 0){
                        i.setDead();
                        break;
                    }
                }
            }
        }
        if (e.data[0] == 0)
        {
            if (e.worldObj.getBlockId(e.getX(), e.getY(), e.getZ()) != Block.torchWood.blockID)
            {
//                e.fade();
                e.kill();
            }
        }
    }
    
    public int[] getColor(int meta){
        int[][] ref = new int[][]
        {
            {25,21,21},
            {151,52,49},
            {53,71,28},
            {80,51,31},
            {47,57,142},
            {126,61,181},
            {46,110,136},
            {155,161,161},
            {64,64,64},
            {208,132,153},
            {66,175,57},
            {178,167,40},
            {106,137,200},
            {180,80,189},
            {219,124,61},
            {255,255,255},
        };
        return ref[meta];
    }
    
}
