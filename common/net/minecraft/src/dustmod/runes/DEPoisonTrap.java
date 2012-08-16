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
public class DEPoisonTrap extends DETrap
{
    @Override
    public void onInit(EntityDust e)
    {
        super.onInit(e);
        ItemStack[] sac = new ItemStack[] {new ItemStack(Item.spiderEye, 3)};
        sac = this.sacrifice(e, sac);

        if (sac[0].stackSize > 0)
        {
            e.fizzle();
            return;
        }
    }

    @Override
    public void trigger(EntityDust e, int dustLevel)
    {
        int rad = 0;
        int poisondambase = 0;
        int poisondamrand = 0;

        switch (dustLevel)
        {
            case 2:
                rad = 3;
                poisondambase = 5;
                poisondamrand = 2;
                break;

            case 3:
                rad = 4;
                poisondambase = 7;
                poisondamrand = 4;
                break;

            case 4:
                rad = 6;
                poisondambase = 10;
                poisondamrand = 8;
                break;
        }

        List<Entity> kill = getEntities(e, rad);

        for (Entity k: kill)
        {
            if (k instanceof EntityLiving)
            {
                ((EntityLiving)k).addPotionEffect(new PotionEffect(Potion.poison.id, (poisondambase + ((int)Math.floor(Math.random() * (double)poisondamrand))) * 20, 2));
            }
        }

        e.fade();
    }
}
