/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dustmod;

import java.util.Iterator;
import java.util.List;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.BlockSand;
import net.minecraft.src.DamageSource;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityFallingSand;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.MovingObjectPosition;
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

    public boolean throwing = false;
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
        dataWatcher.addObject(12, new Integer((lingering ? 1:0)));
    }

    public void updateDataWatcher()
    {
        dataWatcher.updateObject(10, new Integer(blockID));
        dataWatcher.updateObject(11, new Integer(meta));
        dataWatcher.updateObject(12, new Integer((lingering ? 1:0)));
    }
    public void updateEntityFromDataWatcher()
    {
        blockID = dataWatcher.getWatchableObjectInt(10);
        meta = dataWatcher.getWatchableObjectInt(11);
        lingering = dataWatcher.getWatchableObjectInt(12) == 1;
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

//        if (worldObj.isRemote)
//        {
//            super.onEntityUpdate();

            if ((this.ticksExisted % 10 == 0 && this.ticksExisted < 100) || this.ticksExisted % 60 == 0)
            {
                updateEntityFromDataWatcher();
            }

//            return;
//        }

//        if ((this.ticksExisted % 10 == 0 && this.ticksExisted < 100) || this.ticksExisted % 60 == 0)
//        {
//            updateDataWatcher();
//        }

        if(!throwing) motionX = motionY = motionZ = 0;
        else {
//        	System.out.println("wat " + motionX + " " + motionY + " " + motionZ);
        	updateSand();
//        	this.moveEntity(motionX, motionY, motionZ);
//        	this.
        }
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
            if (lingering && getDistance(lx, ly, lz) > 1.5D)
            {
                worldObj.setBlockWithNotify(lx, ly, lz, 0);
                lingering = false;
                this.updateDataWatcher();
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

            if (placeWhenArrived && (dist < 1.4D ||
                    (Math.abs(motionX) < velTol && Math.abs(motionY) < velTol && Math.abs(motionY) < velTol)))
            {
                if (lingering)
                {
                    worldObj.setBlockWithNotify(lx, ly, lz, 0);
                    lingering = false;
                    this.updateDataWatcher();
                }
//                System.out.println("PLACE");
                this.setPosition(gx,gy,gz);
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
        
        this.updateDataWatcher();
    }
    
    public void updateSand(){
//    	if (this.blockID == 0)
//        {
////            this.setDead();
//        }
//        else
//        {
            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;
            ++this.fallTime;
            this.motionY -= 0.03999999910593033D;
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.9800000190734863D;
            this.motionY *= 0.9800000190734863D;
            this.motionZ *= 0.9800000190734863D;

            if (!this.worldObj.isRemote)
            {
                int var1 = MathHelper.floor_double(this.posX);
                int var2 = MathHelper.floor_double(this.posY);
                int var3 = MathHelper.floor_double(this.posZ);

//                if (this.fallTime == 1)
//                {
//                    if (this.fallTime == 1 && this.worldObj.getBlockId(var1, var2, var3) == this.blockID)
//                    {
//                        this.worldObj.setBlockWithNotify(var1, var2, var3, 0);
//                    }
//                    else
//                    {
//                        this.setDead();
//                    }
//                }

                if (this.onGround)
                {
                    this.motionX *= 0.699999988079071D;
                    this.motionZ *= 0.699999988079071D;
                    this.motionY *= -0.5D;

                    if (this.worldObj.getBlockId(var1, var2, var3) != Block.pistonMoving.blockID)
                    {
                        this.setDead();

                        if ((!this.worldObj.canPlaceEntityOnSide(this.blockID, var1, var2, var3, true, 1, (Entity)null) || BlockSand.canFallBelow(this.worldObj, var1, var2 - 1, var3) || !this.worldObj.setBlockAndMetadataWithNotify(var1, var2, var3, this.blockID, this.field_70285_b)) && !this.worldObj.isRemote && this.field_70284_d)
                        {
//                            this.entityDropItem(new ItemStack(this.blockID, 1, this.field_70285_b), 0.0F);
                        }
                    }
                }
                else if (this.fallTime > 100 && !this.worldObj.isRemote && (var2 < 1 || var2 > 256) || this.fallTime > 600)
                {
                    if (this.field_70284_d)
                    {
                        this.dropItem(this.blockID, 1);
                    }

                    this.setDead();
                }
            }
//        }
            
            
            //Collision with entity
            double knockback = 2D;
            Vec3 var17 = this.worldObj.func_82732_R().getVecFromPool(this.posX, this.posY, this.posZ);
            Vec3 var3 = this.worldObj.func_82732_R().getVecFromPool(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            Entity var5 = null;
            List var6 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
            double var7 = 0.0D;
            Iterator var9 = var6.iterator();
            float var11;

            while (var9.hasNext())
            {
                Entity var10 = (Entity)var9.next();

                if (var10.getClass() != this.getClass() && var10.canBeCollidedWith()/* && (var10 != this.shootingEntity || this.ticksInAir >= 5)*/)
                {
                    var11 = 0.3F;
                    AxisAlignedBB var12 = var10.boundingBox.expand((double)var11, (double)var11, (double)var11);
                    MovingObjectPosition var13 = var12.calculateIntercept(var17, var3);

                    if (var13 != null)
                    {
                        double var14 = var17.distanceTo(var13.hitVec);

                        if (var14 < var7 || var7 == 0.0D)
                        {
                            var5 = var10;
                            var7 = var14;
                        }
                    }
                }
            }
            

            if(var5 != null){
	            if (var5.attackEntityFrom(DamageSource.inWall, 104))
	            {
	                if (var5 instanceof EntityLiving)
	                {
	                    ++((EntityLiving)var5).arrowHitTempCounter;
	
//	                    if (this.knockbackStrength > 0)
//	                    {
	                        float var25 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
	
	                        if (var25 > 0.0F)
	                        {
	                        	var5.addVelocity(this.motionX * (double)knockback * 0.6000000238418579D / (double)var25, 0.1D, this.motionZ * (double)knockback * 0.6000000238418579D / (double)var25);
	                        }
//	                    }
	                }
	
//	                this.worldObj.playSoundAtEntity(this, "random.bowhit", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
	                this.setDead();
	            }
	            else
	            {
	                this.motionX *= -0.10000000149011612D;
	                this.motionY *= -0.10000000149011612D;
	                this.motionZ *= -0.10000000149011612D;
	                this.rotationYaw += 180.0F;
	                this.prevRotationYaw += 180.0F;
	            }
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
        this.updateDataWatcher();
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

    @Override
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
    
    public boolean doesCollideWithWorld(){
    	int ix = (int)(gx -0.5);
    	int iy = (int)(gy -0.5);
    	int iz = (int)(gz -0.5);
    	for(int i = -1; i <= 1; i++){
    		for(int j = -1; j <= 1; j++){
    			for(int k = -1; k <= 1; k++){
    				if(Math.abs(i+j)!=1 && Math.abs(i+k)!=0) continue;
    				int bid = worldObj.getBlockId(ix+i, iy+j, iz+k);
    				if(bid != 0) return true;
    			}
    		}
    	}
    	return false;
    }
}
