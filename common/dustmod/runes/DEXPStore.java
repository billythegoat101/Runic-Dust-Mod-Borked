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
public class DEXPStore extends DustEvent
{
    public DEXPStore()
    {
        super();
    }
    public void onInit(EntityDust e)
    {
        ItemStack[] req = new ItemStack[]
        {
            new ItemStack(Item.ingotIron, 16, -1)
        };
        sacrifice(e, req);

        if (req[0].stackSize > 0 || (!this.takeXP(e, 6) && !DustMod.debug))
        {
            e.fizzle();
            return;
        }

        e.renderStar = true;
        e.setColorStarInner(0, 255, 0);
        e.setColorStarOuter(0, 255, 0);
    }

    public void onTick(EntityDust e)
    {
        List<Entity> ents = this.getEntities(e);

        if(e.bb != -1) {
            e.data[1] = e.bb;
            e.bb = -1;
        }
        
        if (e.ram > 0)
        {
            e.setColorStarInner(255, 255, 0);
            e.setColorStarOuter(255, 255, 0);
        }
        else
        {
            e.setColorStarInner(0, 255, 0);
            e.setColorStarOuter(0, 255, 0);
        }

        for (Entity i: ents)
        {
//            if (i instanceof EntityItem)
//            {
//                EntityItem ei = (EntityItem)i;
//
//                if (ei.item.itemID == mod_DustMod.negateSacrifice.shiftedIndex)
//                {
//                    e.data[0] = 99999;
//                    e.bb = 0;
//                }
//            }
            if (i instanceof EntityPlayer && e.ram <= 0)
            {
                EntityPlayer p = (EntityPlayer)i;

                if (p.username.equals(e.summonerUN))
                {
                    //                System.out.println("XP " + p.experienceTotal);
                    //                if(p.experienceTotal > 10){
                    //                    p.experienceTotal-= 10;
                    //                    e.bb+=10;
                    //                }else if(p.experienceTotal > 0){
                    //                    p.experienceTotal --;
                    //                    e.bb++;
                    //                }
                    //                if(p.experience > 0){
                    //                    e.bb += (int)(p.experience*p.xpBarCap());
                    //                    p.addExperience(-p.experience);
                    //                    p.experience = 0;
                    //                    e.fallDistance +=0.01;
                    //                }else if(p.experience <= 0){
                    //                    p.experience = 0;
                    if (p.experienceLevel > 0)
                    {
                        p.addExperience(-1);
                        e.data[0]++;
                    }

                    //                }
                }
            }
            else if (i instanceof EntityXPOrb)
            {
                e.data[1] += ((EntityXPOrb)i).getXpValue();
                i.setDead();
            }
        }

        double d = 8D;
        ents = this.getEntities(e, 4D);

        for (Entity i: ents)
        {
            if (i instanceof EntityXPOrb)
            {
                EntityXPOrb exp = (EntityXPOrb)i;
                e.data[1] += ((EntityXPOrb)i).getXpValue();
                i.setDead();
//                double d1 = (e.posX - exp.posX) / d;
//                double d2 = ((e.posY + e.yOffset) - exp.posY) / d;
//                double d3 = (e.posZ - exp.posZ) / d;
//                double d4 = Math.sqrt(d1 * d1 + d2 * d2 + d3 * d3);
//                double d5 = 1.0D - d4;
//
//                if (d5 > 0.0D)
//                {
//                    d5 *= d5;
//                    exp.motionX += (d1 / d4) * d5 * 0.10000000000000001D;
//                    exp.motionY += (d2 / d4) * d5 * 0.10000000000000001D;
//                    exp.motionZ += (d3 / d4) * d5 * 0.10000000000000001D;
//                }
            }
        }

        if (e.ram > 0)
        {
            e.ram--;
        }
    }

    @Override
    public void onRightClick(EntityDust e, TileEntityDust ted, EntityPlayer p)
    {
        super.onRightClick(e, ted, p);
        if(p.username.equals(e.summonerUN)){
            e.ram = 100;
            drop(e);
        }
    }

    @Override
    public void onUnload(EntityDust e)
    {
        drop(e);
//        for (int i = e.bb; i > 0;)
//        {
//            int k = e.worldObj.rand.nextInt(i>2477? 2477:i);
//            i -= k;
//            System.out.println("new orb " + k);
//            double tx = x + ((Math.random() > 0.5D) ? 1:-1) + Math.random()*0.4D-0.2D;
//            double tz = z + ((Math.random() > 0.5D) ? 1:-1) + Math.random()*0.4D-0.2D;
//            EntityXPOrb ex = new EntityXPOrb(e.worldObj, tx + 0.5D, y, tz+0.5D, k);
//            ex.motionX = ex.motionY = ex.motionZ = 0;
//            e.worldObj.spawnEntityInWorld(ex);
//        }
//        EntityXPOrb eob = new EntityXPOrb(e.worldObj, e.posX, e.posY + e.yOffset, e.posZ);
        super.onUnload(e);
    }

    public void drop(EntityDust e)
    {
        Entity i = e.worldObj.getClosestPlayerToEntity(e, 12D);

        if (i instanceof EntityPlayer)
        {
            EntityPlayer p = (EntityPlayer)i;
//            p.experienceLevel += e.data[0];
            p.addExperience(e.data[0]);
            e.data[0] = 0;
            p.addExperience(e.data[1]);
            e.data[1] = 0;
        }
    }
}
