/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minecraft.src.dustmod.events;

import java.util.List;

import net.minecraft.src.Entity;
import net.minecraft.src.dustmod.DustEvent;
import net.minecraft.src.dustmod.DustMod;
import net.minecraft.src.dustmod.EntityDust;

/**
 *
 * @author billythegoat101
 */
public abstract class DETrap extends DustEvent
{
    protected double range = 1D;

    public DETrap()
    {
        super();
    }

    public void onInit(EntityDust e)
    {
        int compare = DustMod.compareDust(DustMod.gunDID, e.dustID);

        if (compare < 0)
        {
            e.fizzle();
            return;
        }

        e.renderStar = true;
    }
    public void onTick(EntityDust e)
    {
        e.renderStar = true;

        if (e.ticksExisted < 80)
        {
            e.setColorInner(140, 140, 140);
            e.setColorOuter(140, 140, 140);
            return;
        }

        e.setColorInner(0, 0, 255);
        e.setColorOuter(0, 0, 255);
        List<Entity> entities = this.getEntitiesExcluding(e.worldObj, e, e.posX, e.posY, e.posZ, 2D);

        if (entities.size() > 0)
        {
            trigger(e, e.dustID);
            e.fade();
        }
    }

    public abstract void trigger(EntityDust e, int dustLevel);
}
