/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dustmod.runes;

import java.util.ArrayList;
import java.util.List;

import dustmod.DustEvent;
import dustmod.EntityDust;
import dustmod.VoidStorageManager;

import net.minecraft.src.Entity;
import net.minecraft.src.EntityItem;
import net.minecraft.src.ItemStack;

/**
 *
 * @author billythegoat101
 */
public class DEVoid extends DustEvent
{
    public DEVoid()
    {
        super();
    }

    public void onInit(EntityDust e)
    {
        if (!this.takeXP(e, 3))
        {
            e.fizzle();
            return;
        }

        e.renderStar = true;
        e.setColorStarInner(255, 0, 255);
        e.setColorStarOuter(255, 0, 255);
        List<EntityItem> sacrifice = this.getItems(e);

        if (sacrifice == null || sacrifice.isEmpty())
        {
            e.starScale = 1.02F;
            e.data[0] = 1;
        }
        else
        {
            for (EntityItem i: sacrifice)
            {
            	VoidStorageManager.addItemToVoidInventory(e, i.item);
                i.setDead();
            }

            VoidStorageManager.updateVoidInventory();
            e.data[0] = 0;
        }
    }

    public void onTick(EntityDust e)
    {
        if (e.data[0] == 1)
        {
            if (e.ticksExisted > 100)
            {
                e.fade();
                ArrayList<ItemStack> list = VoidStorageManager.getVoidInventory(e);
                if(list == null) return;
                for (ItemStack i: list)
                {
                    Entity en = null;
                    en = new EntityItem(e.worldObj, e.posX, e.posY - EntityDust.yOffset, e.posZ, i);

                    if (en != null)
                    {
                        en.setPosition(e.posX, e.posY, e.posZ);
                        e.worldObj.spawnEntityInWorld(en);
                    }
                }

                VoidStorageManager.clearVoidInventory(e);
                VoidStorageManager.updateVoidInventory();
            }
        }
        else
        {
            if (e.ticksExisted > 35)
            {
                e.ticksExisted += 3;
                e.starScale -= 0.001;
            }

            if (e.ticksExisted > 100)
            {
                e.kill();
            }
        }
    }
}
