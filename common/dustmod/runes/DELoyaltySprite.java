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
public class DELoyaltySprite extends PoweredEvent
{
    public DELoyaltySprite()
    {
        super();
    }

    public void onInit(EntityDust e)
    {
        super.onInit(e);
//        e.renderStar = true;
//
//        ItemStack[] req = this.sacrifice(e, new ItemStack[]{new ItemStack(Item.ghastTear, 4, -1)});
//        if (!checkSacrifice(req) || !takeXP(e,10)) {
//            e.fizzle();
//            return;
//        }
//        ModLoader.getMinecraftInstance().thePlayer.sendChatMessage("The loyalty sprite rune is currently out of order.");
        e.reanimate = true;
    }

    public void onTick(EntityDust e)
    {
        super.onTick(e);

        if (e.data[0] == 0)
        {
            e.posY = e.getY() - 1 + MathHelper.sin((float)e.ticksExisted / 16F) * 0.5F;
            List<Entity> ents = this.getEntities(e, 3.5D);

            for (Entity i: ents)
            {
                if (i instanceof EntityLiving && !(i instanceof EntityPlayer))
                {
//                    System.out.println("FOUND NEW SLAVE");
                    e.data[0] = EntityList.getEntityID(i);
                    e.motionX = e.motionX;
                    e.motionY = e.motionY;
                    e.motionZ = e.motionZ;
                }
            }
        }
        else
        {
            EntityPlayer player = e.worldObj.getPlayerEntityByName(e.summonerUN);

//            if(true/*!e.worldObj.multiplayerWorld*/) player = ModLoader.getMinecraftInstance().thePlayer;
            if (player == null)
            {
                return;
            }

            EntityLiving slave = null;

//            e.posY -= 1.5D;
            if (e.toFollow == null)
            {
                e.posY -= 1;
                List<Entity> ents = this.getEntities(e, 3D);

                for (Entity i: ents)
                {
                    if (i instanceof EntityLiving && !(i instanceof EntityPlayer) && EntityList.getEntityID(i) == e.data[0])
                    {
                        //                    System.out.println("GOT MAH SLAVE");
                        slave = (EntityLiving)i;
                    }
                }
            }
            else
            {
                slave = (EntityLiving)e.toFollow;
            }

//            e.posY += 1.5D;
            if (slave == null || slave.isDead)
            {
//                System.out.println("LOST SLAVE");
                if (e.ram == 1)
                {
                    e.fade();
                }
                else if (e.ram == 0)
                {
                    e.ram = 10;
                }

                e.ram--;
                return;
            }

            slave.extinguish();
            e.toFollow = slave;
            e.follow = true;
            boolean attacking = false;

            if (slave instanceof EntityCreature)
            {
                attacking = (((EntityCreature)slave).getEntityToAttack() != null
                        && ((EntityCreature)slave).getEntityToAttack() != player);
            }

            List<Entity> ents = this.getEntities(e, 5D);

            if (!attacking)
                for (Entity i: ents)
                {
                    if (i == slave || i == player)
                    {
                        continue;
                    }

                    if (i instanceof IMob && (!(i instanceof EntityCreeper) /*|| slave instanceof EntityCreeper*/))
                    {
//                        System.out.println("Attacking mob " + (i instanceof EntityCreeper));
                    	DustModBouncer.setEntityToAttack((EntityCreature)slave, i);
                        attacking  = true;
                        break;
                    }
                    else if (i instanceof EntityLiving && /*i instanceof EntityAnimal && */slave instanceof IMob && slave instanceof EntityCreature && !attacking && !(i instanceof EntityCreeper))
                    {
//                        System.out.println("Attacking animal");
                    	DustModBouncer.setEntityToAttack((EntityCreature)slave, i);
                        attacking = true;
                        break;
                    }
                }

            if (slave instanceof EntityCreature)
            {
                EntityCreature cSlave = (EntityCreature)slave;

                if (cSlave.getEntityToAttack() == player && !(slave instanceof EntityAnimal || slave instanceof EntityWolf))
                {
                    cSlave.attackTime = 20;
                }

                if (!attacking)
                {
//                    System.out.println("SQDIST " + cSlave.getDistanceSqToEntity(player));
                    if (player.onGround && cSlave.getDistanceSqToEntity(player) > 256D)
                    {
                        cSlave.setLocationAndAngles(player.posX, player.posY + player.yOffset, player.posZ, 0, 0);
                    }

                    DustModBouncer.setEntityToAttack(cSlave, player);
                }
            }
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
