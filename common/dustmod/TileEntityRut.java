/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dustmod;

import java.util.Arrays;

import net.minecraft.src.Block;
import net.minecraft.src.BlockSand;
import net.minecraft.src.DustModBouncer;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.Packet;
import net.minecraft.src.TileEntity;

/**
 *
 * @author billythegoat101
 */
public class TileEntityRut extends TileEntity
{
    public static float hardnessStandard = -1;
    public int maskBlock;
    public int maskMeta;
    public int prevFluid;
    public int fluid;
    public int[][][] ruts;
    public boolean isBeingUsed = false;
    public boolean isDead = false;
    public int ticksExisted = 0;

    public boolean[][][] neighborSolid = null;

    public boolean changed = true;

    public TileEntityRut()
    {
    	if(hardnessStandard == -1){
    		 hardnessStandard = Block.gravel.getBlockHardness(worldObj,xCoord,yCoord,zCoord);
    	}
        ruts = new int[3][3][3];

        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                for (int k = 0; k < 3; k++)
                {
                    ruts[i][j][k] = 0;
                }
            }
        }
    }

    public boolean hasChanged()
    {
        boolean rtn = changed;
        changed = false;
        return rtn;
    }

    @Override
    public void updateEntity()
    {
        super.updateEntity();

        if (neighborSolid == null)
        {
            neighborSolid = new boolean[3][3][3];
            updateNeighbors();
        }

        if (isEmpty() || (Block.blocksList[maskBlock] instanceof BlockSand && BlockSand.canFallBelow(worldObj, xCoord, yCoord - 1, zCoord)))
        {
            isDead = true;
            worldObj.setBlockAndMetadataWithNotify(xCoord, yCoord, zCoord, maskBlock, maskMeta);
            this.invalidate();
            return;
        }

        if (worldObj.getWorldTime() % 14 == 0 && prevFluid == fluid && fluidIsFluid())
        {
            worldObj.setBlockMetadata(xCoord, yCoord, zCoord, maskMeta);
            int i = xCoord, j = yCoord, k = zCoord;
            super.updateEntity();

            for (int ix = -1; ix <= 1; ix++)
            {
                for (int iy = -1; iy <= 0; iy++)
                {
                    for (int iz = -1; iz <= 1; iz++)
                    {
                        if ((ix == -1 || ix == 1) && ix == iy && (iz == -1 || iz == 1))
                        {
                            continue;
                        }

                        if ((ix == -1 || ix == 1) && (iy == -1 || iy == 1) && ix != iy && (iz == -1 || iz == 1))
                        {
                            continue;
                        }

                        if (iy == 0 && (ix == -1 || ix == 1) && (iz == -1 || iz == 1))
                        {
                            continue;
                        }

                        if (worldObj.getBlockId(i + ix, j + iy, k + iz) == DustMod.rutBlock.blockID)
                        {
                            TileEntityRut ter = (TileEntityRut)worldObj.getBlockTileEntity(i + ix, j + iy, k + iz);

                            if (ter.fluid == 0)
                            {
                                ter.setFluid(this.fluid);
                            }
                            else if (ter.fluid == Block.waterStill.blockID && this.fluid == Block.lavaStill.blockID)
                            {
                                ter.setFluid(Block.cobblestone.blockID);
                                this.setFluid(Block.cobblestone.blockID);
                            }
                            else if (this.fluid == Block.waterStill.blockID && ter.fluid == Block.lavaStill.blockID)
                            {
                                ter.setFluid(Block.cobblestone.blockID);
                                this.setFluid(Block.cobblestone.blockID);
                            }
                        }
                    }
                }
            }
        }

        if (worldObj.getWorldTime() % 60 == 0 && fluid == 0)
        {
            for (int ix = -1; ix <= 1; ix++)
            {
                for (int iy = -1; iy <= 1; iy++)
                {
                    for (int iz = -1; iz <= 1; iz++)
                    {
                        if (ix == iy || ix == iz || iy == iz)
                        {
                            int check = worldObj.getBlockId(xCoord + ix, yCoord + iy, zCoord + iz);

                            if (fluid == 0)
                            {
                                if (check == Block.lavaStill.blockID || check == Block.lavaMoving.blockID)
                                {
                                    setFluid(Block.lavaStill.blockID);
//                                    mod_DustMod.notifyBlockChange(worldObj, xCoord, yCoord, zCoord, 0);
                                }
                                else if (check == Block.waterStill.blockID || check == Block.waterMoving.blockID)
                                {
                                    setFluid(Block.waterStill.blockID);
//                                    mod_DustMod.notifyBlockChange(worldObj, xCoord, yCoord, zCoord, 0);
                                }
                            }
                        }
                    }
                }
            }
        }

        prevFluid = fluid;
    }

    public boolean updateNeighbors()
    {
    	boolean rtn = false;
        if (neighborSolid == null)
        {
        	rtn = true;
            neighborSolid = new boolean[3][3][3];
        }

        changed = true;

        for (int i = -1; i <= 1; i++)
            for (int j = -1; j <= 1; j++)
                for (int k = -1; k <= 1; k++)
                {
                	boolean prev = neighborSolid[i + 1][j + 1][k + 1]; 
                    int bid = worldObj.getBlockId(xCoord + i, yCoord + j, zCoord + k);
                    boolean next = (bid != 0 && (Block.blocksList[bid].isOpaqueCube() || Block.blocksList[bid] == DustMod.rutBlock));
                    if(prev != next) rtn = true;
                    neighborSolid[i + 1][j + 1][k + 1] = next;
                }
        return rtn;
    }

    public boolean isNeighborSolid(int ix, int iy, int iz)
    {
        if (neighborSolid == null)
        {
            updateNeighbors();
        }

        return neighborSolid[ix + 1][iy + 1][iz + 1];
    }

    @Override
    public void writeToNBT(NBTTagCompound tag)
    {
        super.writeToNBT(tag);
        tag.setInteger("maskBlock", maskBlock);
        tag.setInteger("maskMeta", maskMeta);
        tag.setInteger("fluid", fluid);
        tag.setBoolean("isBeingUsed", isBeingUsed);

        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                for (int k = 0; k < 3; k++)
                {
                    tag.setInteger("rut[" + i + "," + j + "," + k + "]", ruts[i][j][k]);
                }
            }
        }
    }
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);

        if (tag.hasKey("maskBlock"))
        {
            maskBlock = tag.getInteger("maskBlock");
        }
        else
        {
            maskBlock = Block.workbench.blockID;
            System.out.println("damn block");
        }

        if (tag.hasKey("maskMeta"))
        {
            maskMeta = tag.getInteger("maskMeta");
        }
        else
        {
            maskMeta = 2;
        }

        if (tag.hasKey("fluid"))
        {
            fluid = tag.getInteger("fluid");
        }
        else
        {
            fluid = 2;
        }

        if (tag.hasKey("isBeingUsed"))
        {
            isBeingUsed = tag.getBoolean("isBeingUsed");
        }

        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                for (int k = 0; k < 3; k++)
                {
                    String stag = "rut[" + i + "," + j + "," + k + "]";

                    if (tag.hasKey(stag))
                    {
                        ruts[i][j][k] = tag.getInteger(stag);
                    }
                }
            }
        }
    }

//    public void onNeighborChange(){
//
//    }
//    public boolean isValidNeighbor(Block b){
//        return b == null || (!b.isOpaqueCube() && b != mod_DustMod.rutBlock);
//    }

    public void setRut(int i, int j, int k, int l)
    {
        if (isBeingUsed)
        {
            return;
        }

        changed = true;

        if (canEdit())
        {
            if ((i == 0 || i == 2) && i == j && (k == 0 || k == 2))
            {
                return;
            }

            if ((i == 0 || i == 2) && (j == 0 || j == 2) && i != j && (k == 0 || k == 2))
            {
                return;
            }

            ruts[i][j][k] = l;
        }

        System.out.println("Setting [" + i + "," + j + "," + k + "]");
        DustModBouncer.notifyBlockChange(worldObj, xCoord, yCoord, zCoord, 0);
    }
    public int getRut(int i, int j, int k)
    {
        return ruts[i][j][k];
    }

    public void resetBlock()
    {
        isDead = true;
        worldObj.setBlockAndMetadataWithNotify(xCoord, yCoord, zCoord, maskBlock, maskMeta);
    }

    public boolean fluidIsFluid()
    {
        Block f = Block.blocksList[fluid];
        return (f == null || f == Block.waterStill || f == Block.lavaStill);
    }

    public void setFluid(int fluid)
    {
        if (this.fluid != fluid)
        {
            this.fluid = fluid;
            DustModBouncer.notifyBlockChange(worldObj, xCoord, yCoord, zCoord, 0);
            changed = true;
        }
    }
    public boolean canEdit()
    {
        Block f = Block.blocksList[fluid];
        return (fluidIsFluid() || f.getBlockHardness(worldObj,xCoord,yCoord,zCoord) <= hardnessStandard) && !isBeingUsed;
    }

    public boolean isEmpty()
    {
        for (int i = 0; i < 3; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                for (int k = 0; k < 3; k++)
                {
                    if (getRut(i, j, k) != 0)
                    {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    @Override
    public Packet getDescriptionPacket()
    {
        return PacketHandler.getTERPacket(this);
    }
}
