/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minecraft.src.dustmod;

import net.minecraft.src.EntityFallingSand;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;

/**
 *
 * @author billythegoat101
 */
public class EntityBlock extends EntityFallingSand
{
//    public int blockID;
    public int meta;

    /** How long the block has been falling for. */
    public int fallTime;
    public boolean save = true;
    public boolean hasParentDust = false;
    public long parentDustID = -1L;
    public EntityDust parentDust = null;

    public boolean going = false;
    public boolean placeWhenArrived = false;
    public boolean lingerWhenArrived = false;
    public boolean lingering = false;
    public double gx, gy, gz;
    public int lx, ly, lz;
    public double gv;

    public int origX, origY, origZ;

    boolean justBorn = true;

    public EntityBlock(World par1World)
    {
        super(par1World);
    }

    public EntityBlock(World par1World, double par2, double par4, double par6, int par8)
    {
        super(par1World, par2, par4, par6, par8);
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
        dataWatcher.addObject(10, new Integer(blockID));
        dataWatcher.addObject(11, new Integer(meta));
    }

    public void updateDataWatcher()
    {
        dataWatcher.updateObject(10, new Integer(blockID));
        dataWatcher.updateObject(11, new Integer(meta));
    }
    public void updateEntityFromDataWatcher()
    {
        blockID = dataWatcher.getWatchableObjectInt(10);
        meta = dataWatcher.getWatchableObjectInt(11);
    }

    @Override
    public boolean canBePushed()
    {
        return false;
    }
    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean canBeCollidedWith()
    {
        return !isDead;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        if (this.isBurning())
        {
            this.extinguish();
        }

        if (worldObj.isRemote)
        {
            super.onEntityUpdate();

            if ((this.ticksExisted % 10 == 0 && this.ticksExisted < 100) || this.ticksExisted % 60 == 0)
            {
                updateEntityFromDataWatcher();
            }

            return;
        }

        if ((this.ticksExisted % 10 == 0 && this.ticksExisted < 100) || this.ticksExisted % 60 == 0)
        {
            updateDataWatcher();
        }

        motionX = motionY = motionZ = 0;

        if (justBorn && hasParentDust && parentDust == null)
        {
            parentDust = EntityDustManager.getDustAtID(parentDustID);

            if (parentDust != null)
            {
                DustEvent evt = parentDust.event;

                if (evt != null)
                {
                    evt.registerFollower(parentDust, this);
                }
            }
            else
            {
                setDead();
                return;
            }

            justBorn = false;
        }

        if (lingering && worldObj.getBlockId(lx, ly, lz) != blockID)
        {
            setDead();
            return;
        }

        if (going)
        {
            if (lingering && getDistance(lx, ly, lz) > 0.5D)
            {
                worldObj.setBlockWithNotify(lx, ly, lz, 0);
                lingering = false;
            }

            double dist = goTo(gv, gx, gy, gz);

            if (lingerWhenArrived || lingering)
            {
                noClip = true;
            }

            moveEntity(motionX, motionY, motionZ);

            if (lingerWhenArrived || lingering)
            {
                noClip = false;
            }

            double velTol = 0.02D;

            if (placeWhenArrived && (dist < 0.4D ||
                    (Math.abs(motionX) < velTol && Math.abs(motionY) < velTol && Math.abs(motionY) < velTol)))
            {
                if (lingering)
                {
                    worldObj.setBlockWithNotify(lx, ly, lz, 0);
                    lingering = false;
                }

                place();
            }
        }
        else if (hasParentDust && parentDust == null)
        {
            parentDust = EntityDustManager.getDustAtID(parentDustID);

            if (parentDust != null)
            {
                DustEvent evt = parentDust.event;

                if (evt != null)
                {
                    evt.registerFollower(parentDust, this);
                }
            }
            else
            {
                setDead();
            }
        }
        else if ((parentDust != null && parentDust.isDead) || blockID == 0)
        {
            setDead();
            return;
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    protected void writeEntityToNBT(NBTTagCompound tag)
    {
        tag.setInteger("tile", blockID);
        tag.setInteger("meta", meta);
        tag.setBoolean("save", save);
        tag.setBoolean("hasparentdust", hasParentDust);
        tag.setLong("parentDustID", parentDustID);
        tag.setBoolean("going", going);
        tag.setBoolean("pwa", placeWhenArrived);
        tag.setDouble("gx", gx);
        tag.setDouble("gy", gy);
        tag.setDouble("gz", gz);
        tag.setDouble("gv", gv);
        tag.setBoolean("lingering", lingering);
        tag.setBoolean("lingerWA", lingerWhenArrived);
        tag.setInteger("lx", lx);
        tag.setInteger("ly", ly);
        tag.setInteger("lz", lz);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readEntityFromNBT(NBTTagCompound tag)
    {
        if (!tag.getBoolean("save"))
        {
            this.setDead();
            return;
        }

        blockID = tag.getInteger("tile");
        hasParentDust = tag.getBoolean("hasparentdust");
        parentDustID = tag.getLong("parentDustID");
        going = tag.getBoolean("going");
        placeWhenArrived = tag.getBoolean("pwa");
        gx = tag.getDouble("gx");
        gy = tag.getDouble("gy");
        gz = tag.getDouble("gz");
        gv = tag.getDouble("gv");
        lingering = tag.getBoolean("lingering");
        lingerWhenArrived = tag.getBoolean("lingerWA");
        lx = tag.getInteger("lx");
        ly = tag.getInteger("ly");
        lz = tag.getInteger("lz");
    }

    public float getShadowSize()
    {
        return 0.0F;
    }

    public World getWorld()
    {
        return worldObj;
    }

    public void setSave(boolean save)
    {
        this.save = save;
    }

    public void placeAndLinger(double vel, double x, double y, double z)
    {
        lingerWhenArrived = placeWhenArrived = true;
        goToAndPlace(vel, x, y, z);
    }

    public void setOriginal(int x, int y, int z)
    {
        origX = x;
        origY = y;
        origZ = z;
    }

    public void returnToOrigin(double vel)
    {
        goTo(vel, origX, origY, origZ);
        placeWhenArrived = true;
    }

    public void place()
    {
        lx = MathHelper.floor_double(posX);
        ly = MathHelper.floor_double(posY - 0.5D);
        lz = MathHelper.floor_double(posZ);
        going = false;
        placeWhenArrived = false;
        gv = 0;

//        System.out.println("placing " + ly);
        if (worldObj.getBlockId(lx, ly, lz) == 0)
        {
            worldObj.setBlockAndMetadataWithNotify(lx, ly, lz, blockID, meta);

            if (lingerWhenArrived)
            {
                lingering = true;
                lingerWhenArrived = false;
            }
            else
            {
                setDead();
            }
        }
    }

    public double goToAndPlace(double vel, double x, double y, double z)
    {
        placeWhenArrived = true;
        return goTo(vel, x, y, z);
    }

    public double goTo(double vel, double x, double y, double z)
    {
        gx = x;
        gy = y;
        gz = z;
        double dx = x - posX;
        double dy = y - posY;
        double dz = z - posZ;
        going = true;
        gv = vel;
        Vec3 vec = Vec3.createVectorHelper(dx, dy, dz);
        double dist = this.getDistance(x, y, z);

        if (dist < 0.4D)
        {
            motionX = motionY = motionZ = 0;
            return dist;
        }

        if (dist < gv)
        {
            gv = dist;
        }

        vec = vec.normalize();
        double vx = vec.xCoord * vel;
        double vy = vec.yCoord * vel;
        double vz = vec.zCoord * vel;
        motionX = vx;
        motionY = vy;
        motionZ = vz;
        return dist;
    }
}
