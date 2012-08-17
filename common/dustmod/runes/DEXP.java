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
public class DEXP extends PoweredEvent
{
    public DEXP()
    {
        super();
    }

    public void onInit(EntityDust e)
    {
        super.onInit(e);
        e.renderBeam = true;
        e.renderStar = true;
        e.renderFlamesDust = true;
        e.setColorFire(0,255,0);
        e.data[0] = 24000;
        e.setColorBeam(255, 255, 255);
        e.posY += 1D;
        int hostile = 1;
        int passive = 1;
        List l = getEntities(e);

        for (Object o: l)
        {
            if (o instanceof EntityItem)
            {
                EntityItem ei = (EntityItem)o;
                ItemStack item = ei.item;

                if (item.itemID == DustMod.negateSacrifice.shiftedIndex)
                {
                    hostile = passive = 0;
                    break;
                }

                if (item.itemID == Item.monsterPlacer.shiftedIndex)
                {
                    int entClass = item.getItemDamage();

                    if (DustMod.isMobIDHostile(entClass))
                    {
                        while (passive > 0 && item.stackSize > 0)
                        {
                            item.stackSize--;

                            if (item.stackSize <= 0)
                            {
                                ei.setDead();
                            }

                            passive--;
                        }
                    }
                    else
                    {
                        while (hostile > 0 && item.stackSize > 0)
                        {
                            item.stackSize--;

                            if (item.stackSize <= 0)
                            {
                                ei.setDead();
                            }

                            hostile--;
                        }
                    }
                }
            }
        }

//        ItemStack[] req = new ItemStack[]{new ItemStack(Item.enderPearl, 4)};
//        req = this.sacrifice(e, req);
        if (/*!checkSacrifice(req) || */hostile > 0 || passive > 0 || !takeXP(e, 80))
        {
            e.fizzle();
            return;
        }
    }

    public void onTick(EntityDust e)
    {
        super.onTick(e);

//        System.out.println("HUNGRY " + e.data);
        if (e.ticksExisted < 60)
        {
            e.renderBeam = false;
            return;
        }
        else
        {
            e.renderBeam = true;
        }

        EntityPlayer player = e.worldObj.getPlayerEntityByName(e.summonerUN);

//        if(true/*!e.worldObj.multiplayerWorld*/) player = ModLoader.getMinecraftInstance().thePlayer;
        if (player == null)
        {
            return;
        }

        double percent = (double)e.getFuel() / 2400D;
        int col = (int)Math.floor(percent * 255);
        e.setColorBeam(255, col, col);
        int x = e.getX();
        int y = e.getY();
        int z = e.getZ();
        List<Entity> omnom = getEntities(e, 3D);

        for (Entity et: omnom)
        {
            if (et.posY <= e.getY() - 2 || et.posY >= e.getY() + 1.5D)
            {
                continue;
            }

            if (et instanceof EntityItem)
            {
                et.setDead();
                this.addFuel(e, ((EntityItem)et).item.stackSize * 10);
            }

            if (et instanceof EntityLiving && et != player)
            {
                if (et.motionY >= 0)
                {
                    continue;
                }

                EntityLiving el = (EntityLiving)et;

                if (el.getHealth() <= 0)
                {
                    continue;
                }

                int exp = DustModBouncer.getExperiencePoints(el, null);
                el.attackEntityFrom(DamageSource.magic, 10000000);

                for (int mul = 0; mul < 2; mul++)
                    for (int i = exp; i > 0;)
                    {
                        int k = EntityXPOrb.getXPSplit(i);
                        i -= k;
                        double tx = x + ((Math.random() > 0.5D) ? 1 : -1) + Math.random() * 0.4D - 0.2D;
                        double tz = z + ((Math.random() > 0.5D) ? 1 : -1) + Math.random() * 0.4D - 0.2D;
                        EntityXPOrb ex = new EntityXPOrb(e.worldObj, tx + 0.5D, y, tz + 0.5D, k);
                        ex.motionX = ex.motionY = ex.motionZ = 0;
                        e.worldObj.spawnEntityInWorld(ex);
                    }

                this.addFuel(e, 1000);
                et.setDead();
                break;
            }
            else if (et == player)
            {
                if (e.ticksExisted % 20 == 0 && e.ticksExisted != 0)
                {
                    ((EntityPlayer)player).attackEntityFrom(DamageSource.magic, 2);
                }
            }
        }
    }

    @Override
    public int getStartFuel()
    {
        return dayLength;
    }

    @Override
    public int getMaxFuel()
    {
        return dayLength * 7;
    }

    @Override
    public int getStableFuelAmount(EntityDust e)
    {
        return dayLength + dayLength / 2;
    }

    @Override
    public boolean isPaused(EntityDust e)
    {
        return false;
    }
}
