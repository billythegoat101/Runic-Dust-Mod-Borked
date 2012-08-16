/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minecraft.src.dustmod.runes;

import java.util.Random;

import net.minecraft.src.EntityItem;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.dustmod.DustEvent;
import net.minecraft.src.dustmod.EntityDust;

/**
 *
 * @author billythegoat101
 */
public class DESpawnRecord extends DustEvent
{
    public DESpawnRecord()
    {
        super();
    }

    public void onInit(EntityDust e)
    {
        e.renderBeam = e.renderStar = true;
        e.setColorStarOuter(0, 255, 0);
        e.setColorBeam(0, 255, 0);
        ItemStack[] sacrifice = new ItemStack[] {new ItemStack(Item.diamond, 1)};
        this.sacrifice(e, sacrifice);

        if (sacrifice[0].stackSize > 0)
        {
            e.fizzle();
            return;
        }
    }

    public void onTick(EntityDust e)
    {
        e.starScale += 0.0001;

        if (e.ticksExisted > 120)
        {
            Random r = new Random();
            EntityItem en = new EntityItem(e.worldObj, e.posX, e.posY - EntityDust.yOffset - 1, e.posZ, new ItemStack(2000 + r.nextInt(11) + 256, 1, 0));
            e.worldObj.spawnEntityInWorld(en);
            e.fade();
        }
    }
}
