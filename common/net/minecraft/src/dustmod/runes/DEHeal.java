/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minecraft.src.dustmod.runes;

import java.util.List;
import net.minecraft.src.*;
import net.minecraft.src.dustmod.*;

/**
 *
 * @author billythegoat101
 */
public class DEHeal extends DustEvent
{
    public DEHeal()
    {
        super();
    }

    public void onInit(EntityDust e)
    {
        ItemStack[] req = new ItemStack[] {new ItemStack(Item.coal.shiftedIndex, 2, -1)};
        req = this.sacrifice(e, req);

        if (!checkSacrifice(req) || !takeXP(e, 2))
        {
            e.fizzle();
            return;
        }

        int dustID = e.dustID;
        int healMul = 1;
        int healDurBase = 0;

        switch (dustID)
        {
            case 1:
                healMul = 1;
                healDurBase = 4; //3 hearts
                break;

            case 2:
                healMul = 2;
                healDurBase = 5; //n-2 hearts
                break;

            case 3:
                healMul = 2;
                healDurBase = 10;
                break;

            case 4:
                healMul = 5;
                healDurBase = 32;
                break;
        }

        List<Entity> ents = getEntities(e, 5D);

        for (Entity i: ents)
        {
            if (i instanceof EntityLiving)
            {
                EntityLiving l = (EntityLiving)i;
//                System.out.println("DURR heal");
                l.addPotionEffect(new PotionEffect(Potion.regeneration.id, healDurBase * 20, healMul));
            }

            if (i instanceof EntityPlayer)
            {
                EntityPlayer p = (EntityPlayer)i;

                if (dustID == 3)
                {
                    p.getFoodStats().addStats(5, 0.6F);
                }
                else if (dustID == 4)
                {
                    p.getFoodStats().addStats(8, 0.8F);
                }
            }
        }

        e.starScale = 1.12F;
        e.setColorStarInner(255, 255, 255);
        e.setColorStarOuter(255, -255, -255);
        e.renderStar = true;
        //entityplayersp.addPotionEffect(new PotionEffect(Potion.regeneration.id, 3, 2));
    }

    public void onTick(EntityDust e)
    {
        if (e.ticksExisted > 100)
        {
            e.fade();
        }
    }
}
