/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minecraft.src.dustmod.runes;

import java.util.List;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Potion;
import net.minecraft.src.PotionEffect;
import net.minecraft.src.dustmod.DustEvent;
import net.minecraft.src.dustmod.EntityDust;

/**
 *
 * @author billythegoat101
 */
public class DESpeed extends DustEvent
{
    public DESpeed()
    {
        super();
    }

    public void onInit(EntityDust e)
    {
        ItemStack[] req = new ItemStack[]
        {
            new ItemStack(Item.sugar, 4, -1),
            new ItemStack(Item.blazePowder, 2, -1),
        };
        sacrifice(e, req);

        if (req[0].stackSize > 0 || req[1].stackSize > 0)
        {
            e.fizzle();
            return;
        }

        int dustId = e.dusts[e.dusts.length - 1][e.dusts[0].length - 1];
        int p = 0;
        int d = 0;

        switch (dustId)
        {
            case 1:
                p = 1;
                d = 25 * 30;
                break;

            case 2:
                p = 1;
                d = 25 * 60;
                break;

            case 3:
                p = 2;
                d = 25 * 120;
                break;

            case 4:
                p = 4;
                d = 25 * 180;
                break;
        }

        List<Entity> ents = this.getEntities(e, 3D);

        for (Entity i: ents)
        {
            if (i instanceof EntityLiving)
            {
                ((EntityLiving)i).addPotionEffect(new PotionEffect(Potion.moveSpeed.id, d, p));
            }
        }

        e.starScale = 1.12F;
        e.setColorStarOuter(0, 255, 0);
        e.renderStar = true;
        e.fade();
    }

    public void onTick(EntityDust e)
    {
    }
}
