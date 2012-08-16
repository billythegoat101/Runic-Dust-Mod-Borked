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
public class DELightning extends DETrap
{
    @Override
    public void onInit(EntityDust e)
    {
        super.onInit(e);
        ItemStack[] sac = new ItemStack[] {new ItemStack(Item.ingotIron, 3)};
        sac = this.sacrifice(e, sac);

        if (sac[0].stackSize > 0)
        {
            e.fizzle();
            return;
        }
    }

    public DELightning()
    {
        super();
    }
    public void trigger(EntityDust e, int level)
    {
        List<Entity> entities = getEntities(e, 2D * level);

        for (Entity i: entities)
        {
            if (i instanceof EntityLiving && e.getDistanceToEntity(i) < 2D * level)
            {
                e.worldObj.addWeatherEffect(new EntityLightningBolt(e.worldObj, e.posX, e.posY - EntityDust.yOffset, e.posZ));
                e.fade();
            }
        }
    }
}
