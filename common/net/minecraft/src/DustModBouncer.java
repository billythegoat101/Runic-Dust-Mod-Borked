package net.minecraft.src;

import java.util.ArrayList;
import java.util.logging.Level;

import cpw.mods.fml.common.FMLLog;

public class DustModBouncer {
	public static void setEntityToAttack(EntityCreature e, Entity target)
    {
        e.entityToAttack = target;
    }
    public static Entity getEntityToAttack(EntityCreature e)
    {
        return e.entityToAttack;
    }
    public static EntityAITasks getTasks(EntityLiving e)
    {
        return e.tasks;
    }
    public static EntityAITasks getTargetTasks(EntityLiving e)
    {
        return e.targetTasks;
    }
    public static void setCantSee(EntityLiving e, EntityLiving target)
    {
        EntitySenses senses = e.getEntitySenses();

        if (senses.canSee(target))
        {
            senses./*canSeeCachePositive*/field_75524_b.remove(target);
            senses./*canSeeCacheNegative*/field_75525_c.add(target);
        }
    }
    public static void setCanSee(EntityLiving e, EntityLiving target)
    {
        EntitySenses senses = e.getEntitySenses();

        if (!senses.canSee(target))
        {
            senses./*canSeeCacheNegative*/field_75525_c.remove(target);
            senses./*canSeeCachePositive*/field_75524_b.add(target);
        }
    }
    public static void addAITask(EntityLiving e, EntityAIBase ai, int priority)
    {
        e.tasks.addTask(priority, ai);
    }
    public static boolean hasAITask(EntityLiving e, EntityAIBase ai)
    {
        try
        {
            ArrayList list = (ArrayList)ModLoader.getPrivateValue(EntityAITasks.class, e.tasks, 0);

            for (Object o: list)
            {
                EntityAITaskEntry entry = (EntityAITaskEntry)o;
                EntityAIBase i = entry.action;

                if (i.getClass() == ai.getClass())
                {
//                    System.out.println("Has task " + i + " " + ai);
                    return true;
                }
            }
        }
        catch (Exception ex)
        {
            FMLLog.log(Level.SEVERE, null, ex);
        }

        return false;
    }
    public static int getExperiencePoints(EntityLiving e, EntityPlayer p)
    {
        return e.getExperiencePoints(p);
    }
    public static boolean getHasAttacked(EntityCreature e)
    {
        return e.hasAttacked;
    }
    public static void updateState(EntityCreature e)
    {
        e.updateEntityActionState();
    }
    public static void notifyBlockChange(World w, int i, int j, int k, int l)
    {
        w.notifyBlockChange(i, j, k, l);
        w.markBlockForUpdate(i, j, k);
    }
    public static int damageDropped(Block b, int i)
    {
        return b.damageDropped(i);
    }
    public static float getMoveForward(EntityLiving ent)
    {
        return ent.moveForward;
    }
    
    public static void updateSkylightNeighborHeight(Chunk chunk, int par1, int par2, int par3, int par4)
    {
        if (par4 > par3 && chunk.worldObj.doChunksNearChunkExist(par1, 0, par2, 16))
        {
            for (int i = par3; i < par4; i++)
            {
                chunk.worldObj.updateLightByType(EnumSkyBlock.Sky, par1, i, par2);
            }

            chunk.isModified = true;
        }
    }
    public static boolean isAIEnabled(EntityCreature e)
    {
        return e.isAIEnabled();
    }
    public static void updateActionState(EntityLiving e)
    {
        e.updateEntityActionState();
    }
    public static void setHasAttacked(EntityCreature e, boolean val)
    {
        e.hasAttacked = val;
    }
    
    public static boolean isJumping(EntityLiving e){
    	return e.isJumping;
    }
    
    public static Item newItem(int id){
    	return new Item(id);
    }
    
}
