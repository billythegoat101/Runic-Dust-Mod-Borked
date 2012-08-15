/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minecraft.src.dustmod.events;

import java.util.ArrayList;
import net.minecraft.src.*;
import net.minecraft.src.dustmod.*;

/**
 *
 * @author billythegoat101
 */
public class DEEarthSprite  extends PoweredEvent
{
    public DEEarthSprite()
    {
        super();
    }

    public void onInit(EntityDust e)
    {
        super.onInit(e);
//        e.renderBeam = true;
        e.follow = true;
        e.renderStar = true;
        e.setColorInner(255, 0, 0);
        e.setColorOuter(255, 0, 0);
        ItemStack[] req = new ItemStack[] {new ItemStack(Block.glass.blockID, 16, 0),
                      new ItemStack(Item.ghastTear.shiftedIndex, 1, 0),
                      new ItemStack(Block.oreIron.blockID, 20, 0)
        };
        req = this.sacrifice(e, req);

        if (req[0].stackSize != 0 || req[1].stackSize != 0 || req[2].stackSize != 0 || !takeXP(e, 25))
        {
            e.fizzle();
            return;
        }

        for (int i = 0; i < 8; i++)
        {
            EntityBlock eb = new EntityBlock(e.worldObj, e.getX(), e.getY() + 2, e.getZ(), Block.glass.blockID);
            eb.hasParentDust = true;
            eb.parentDustID = e.entityDustID;
            eb.parentDust = e;
            registerFollower(e, eb);
            e.worldObj.spawnEntityInWorld(eb);
//            eb.save = false;
        }
    }

    public void onTick(EntityDust e)
    {
        super.onTick(e);
        e.renderStar = false;
        e.renderBeam = false;
        e.follow = true;
        World worldObj = e.worldObj;
        EntityPlayer p = e.worldObj.getPlayerEntityByName(e.summonerUN);

//        if(/*!e.worldObj.multiplayerWorld*/true) p = ModLoader.getMinecraftInstance().thePlayer;
        if (p == null)
        {
            return;
        }

        if (e.genericList == null)
        {
            e.genericList = new ArrayList<EntityBlock>();
        }

        if (e.genericList.size() > 0)
        {
            int ind = 0;
//            Vec3D v = Vec3D.createVector(p.motionX, p.motionY, p.motionZ);
//            double vel = v.lengthVector();
            float vel = DustModBouncer.getMoveForward(p);
            boolean protect = (vel == 0) && p.isSneaking() && Math.abs(p.motionY) < 0.08D;
//            System.out.println("derp " + p.motionY);
            int px = MathHelper.floor_double(p.posX);
            int py = MathHelper.floor_double(p.posY);
            int pz = MathHelper.floor_double(p.posZ);

            if (protect)
            {
//                p.posX = px+0.5D;
//                p.posY = py + p.yOffset+0.1D;
//                p.posZ = pz+0.5D;
                p.setPosition((double)px + 0.5D, (double)py + p.yOffset - 1.06, (double)pz + 0.5D);
                p.setMoveForward(0);
//                p.setVelocity(0,0,0);
            }

//            System.out.println("what the dick " + p.yOffset + " " + p.posX + " " + p.posY + " " + p.posZ);
            for (Object o: e.genericList)
            {
                EntityBlock eb = (EntityBlock)o;
//                if(eb.gv > vel) {
//                    ind++;
//                    continue;
//                }
                int bx = 0, by = 0, bz = 0;

                if (ind % 2 == 0)
                {
                    by = 1;
                }

                if (ind < 4)
                {
                    bx = (ind < 2) ? 1 : -1;
                }
                else
                {
                    bz = (ind < 6) ? 1 : -1;
                }

                if (protect /*&& worldObj.getBlockId(px+bx,py+by,pz+bz) == 0*/)
                {
                    eb.setPosition(px + bx, py + by, pz + bz);
                    eb.placeAndLinger(0.6D, px + bx, py + by, pz + bz);
                }
                else
                {
                    int period = 60;
                    double dist = 3D;
                    double ticks = (e.ticksExisted + ind * 8) % period;
                    double ticksOff = (e.ticksExisted + ind * 30) % period;
                    double sin = Math.sin((ticks / period) * Math.PI * 2);
                    double sinY = Math.sin((ticksOff / period) * Math.PI * 2);
                    double cos = Math.cos((ticks / period) * Math.PI * 2);
                    double dx = cos * dist;
                    double dz = sin * dist;
                    double dy = sinY * 1D;
                    eb.goTo(2.8D, p.posX + dx, p.posY + dy, p.posZ + dz);
                }

                ind++;
            }
        }

//        if(seb != null){
//            seb.setPosition(p.posX, e.posY, e.posZ);
//        }

        if (e.ticksExisted > 24000 * 3)
        {
            e.fade();
        }
    }

    public void onUnload(EntityDust e)
    {
        super.onUnload(e);


        if(e.genericList == null) return;
        for (Object o: e.genericList)
        {
            EntityBlock eb = (EntityBlock)o;
            int y = (int)eb.posY;//e.worldObj.getHeightValue((int)eb.posX, (int)eb.posZ);

            for (int i = y; i >= 0; i--)
            {
                if (e.worldObj.getBlockId((int)eb.posX, i, (int)eb.posZ) != 0)
                {
                    y = i + 1;
                    break;
                }
            }

//            System.out.println("Position " + eb.posX + " " + " " + eb.posY + " " + eb.posZ);
//            System.out.println("SettingPosition " + (int)eb.posX + " " + " " + y + " " + (int)eb.posZ);
            eb.setOriginal((int)eb.posX, y + 1, (int)eb.posZ);
            eb.returnToOrigin(0.2D);
        }
    }

    @Override
    public void registerFollower(EntityDust e, Object o)
    {
        if (o instanceof EntityBlock)
        {
            if (e.genericList == null)
            {
                e.genericList = new ArrayList<EntityBlock>();
            }

            e.genericList.add(o);
        }
    }

    @Override
    public int getStartFuel()
    {
        return dayLength * 3;
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
