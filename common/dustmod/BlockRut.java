/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dustmod;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.DustModBouncer;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemSpade;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.TileEntity;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;

/**
 *
 * @author billythegoat101
 */
public class BlockRut extends BlockContainer
{
    public BlockRut(int i)
    {
        super(i, 7, Material.wood);
//        setLightOpacity(128);
//        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.9375F, 1.0F);
//        standTex = ModLoader.addOverride("/terrain.png", mod_DustMod.path + "/standTop.png");
    }

    @Override
    public int getRenderType()
    {
        return DustMod.proxy.getBlockModel(this);
    }
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public void onNeighborBlockChange(World world, int i, int j, int k, int l)
    {
        boolean notified = false;
        //DustModBouncer.notifyBlockChange(world, i, j, k, 0);
        for (int ix = -1; ix <= 1; ix++)
        {
            for (int iy = -1; iy <= 1; iy++)
            {
                for (int iz = -1; iz <= 1; iz++)
                {
                    if (ix == iy || ix == iz || iy == iz)
                    {
                        TileEntityRut ter = (TileEntityRut)world.getBlockTileEntity(i, j, k);
                        int check = world.getBlockId(i + ix, j + iy, k + iz);

                        if (ter.fluid == 0)
                        {
                            if (check == Block.lavaStill.blockID || check == Block.lavaMoving.blockID)
                            {
                                ter.setFluid(Block.lavaStill.blockID);
                                notified = true;
//                                mod_DustMod.notifyBlockChange(world, i, j, k, 0);
                            }
                            else if (check == Block.waterStill.blockID || check == Block.waterMoving.blockID)
                            {
                                ter.setFluid(Block.waterStill.blockID);
                                notified = true;
//                                mod_DustMod.notifyBlockChange(world, i, j, k, 0);
                            }
                        }

                        if (ter.fluid == Block.waterStill.blockID)
                        {
                            if (check == Block.lavaStill.blockID || check == Block.lavaMoving.blockID)
                            {
                                ter.setFluid(Block.cobblestone.blockID);
                                notified = true;
//                                mod_DustMod.notifyBlockChange(world, i, j, k, 0);
                            }
                        }

                        if (ter.fluid == Block.lavaStill.blockID)
                        {
                            if (check == Block.waterStill.blockID || check == Block.waterMoving.blockID)
                            {
                                ter.setFluid(Block.obsidian.blockID);
                                notified = true;
//                                mod_DustMod.notifyBlockChange(world, i, j, k, 0);
                            }
                        }
                    }
                }
            }
        }

//        if(((TileEntityRut)world.getBlockTileEntity(i, j, k)).updateNeighbors() && !notified){
//        	DustModBouncer.notifyBlockChange(world, i, j, k, 0);
//        }
    }
    @Override
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer p,int rot, float x, float y, float z)
    {
        ItemStack pItem = p.getCurrentEquippedItem();
        boolean isNull = (pItem == null);
//        if (world.getBlockMetadata(i, j, k) > 0) {
//            return false;
//        }
//
//        if (pItem != null && pItem.itemID == mod_DustMod.tome.shiftedIndex) {
//            updatePattern(world, i, j, k, p);
//            mod_DustMod.notifyBlockChange(world, i, j, k, 0);
//            return true;
//        }
//        if (pItem != null && pItem.itemID == mod_DustMod.spiritSword.shiftedIndex) {
//            this.onBlockClicked(world, i, j, k, p);
//        }

//        if (DustMod.debug && p != null && p.isSneaking())
//        {
//            Minecraft mc = ModLoader.getMinecraftInstance();
//
//            if (mc.playerController.isInCreativeMode())
//            {
//                mc.playerController = new PlayerControllerSP(mc);
//                p.capabilities.isCreativeMode = false;
//            }
//            else
//            {
//                mc.playerController = new PlayerControllerCreative(mc);
//                p.capabilities.isCreativeMode = true;
//            }
//
////                mc.thePlayer = (EntityPlayerSP)mc.playerController.createPlayer(world);
////                mc.thePlayer.preparePlayerToSpawn();
////                mc.playerController.flipPlayer(mc.thePlayer);
//        }

        TileEntityRut ter = (TileEntityRut)world.getBlockTileEntity(i, j, k);

        if (ter.isBeingUsed)
        {
            return false;
        }

//        ter.fluid = Block.obsidian.blockID;
        if (ter.fluid == 0 && !isNull && pItem.itemID == Item.bucketWater.shiftedIndex)
        {
            if (!p.capabilities.isCreativeMode)
            {
                pItem.itemID = Item.bucketEmpty.shiftedIndex;
            }

            ter.setFluid(Block.waterStill.blockID);
            return true;
        }

        if (ter.fluid == 0 && !isNull && pItem.itemID == Item.bucketLava.shiftedIndex)
        {
            if (!p.capabilities.isCreativeMode)
            {
                pItem.itemID = Item.bucketEmpty.shiftedIndex;
            }

            ter.setFluid(Block.lavaStill.blockID);
            return true;
        }

        if (!isNull && (ter.fluid == 0 || ter.fluidIsFluid()))
        {
            int bid = pItem.itemID;

            if (bid < Block.blocksList.length && Block.blocksList[bid] != null)
            {
                Block b = Block.blocksList[bid];

                if (b.renderAsNormalBlock() && b.isOpaqueCube() && b.getBlockHardness(world, i,j,k) <= TileEntityRut.hardnessStandard)
                {
                    if (!p.capabilities.isCreativeMode)
                    {
                        pItem.stackSize--;
                    }

                    ter.setFluid(bid);
                    return true;
                }
            }
        }

        if (!isNull && ter.fluid != 0 && !ter.fluidIsFluid() && Block.blocksList[ter.fluid].getBlockHardness(world, i,j,k) <= TileEntityRut.hardnessStandard)
        {
            if (Item.itemsList[pItem.itemID] instanceof ItemSpade)
            {
                this.dropBlockAsItem_do(world, i, j + 1, k, new ItemStack(ter.fluid, 1, 0));
                ter.setFluid(0);
                return true;
            }
        }

        if (isNull || pItem.itemID != DustMod.chisel.shiftedIndex)
        {
            return false;
        }

//        System.out.println("ACTIVATED " + pItem.itemID  + " " +dust+ " derp " + mod_DustMod.ITEM_DustID+256);
        Vec3 look = p.getLookVec();
        double a = look.xCoord;//Math.cos((p.rotationYaw+90)*Math.PI/180);
        double b = look.yCoord;//Math.sin(-p.rotationPitch*Math.PI/180);
        double c = look.zCoord;//Math.sin((p.rotationYaw+90)*Math.PI/180);
        double xi = p.posX - i;
        double yi = p.posY + p.getEyeHeight() - j;
        double zi = p.posZ - k;

        if (a == 0)
        {
            a = 0.001D;
        }

        if (b == 0)
        {
            b = 0.001D;
        }

        if (c == 0)
        {
            c = 0.001D;
        }

        int dir = determineOrientation(world, i, j, k, p);
        Block block = Block.blocksList[ter.maskBlock];
        world.playSoundEffect((float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F, block.stepSound.getStepSound(), (block.stepSound.getVolume() + 1.0F) / 6.0F, block.stepSound.getPitch() * 0.99F);

        //<editor-fold defaultstate="collapsed" desc="Face0">
        for (double test = 0; test < 4 && dir == 0; test += 0.01)
        {
            double tx = p.posX + a * test;
            double ty = p.posY + p.getEyeHeight() + b * test;
            double tz = p.posZ + c * test;
            double testy = ty - (double) j;

            if (testy >= 0.0 && testy <= 0.02)
            {
                double dx = Math.abs(tx - (double) i) - 0.02;
                double dz = Math.abs(tz - (double) k) - 0.02;
                int rx = (int) Math.floor(dx * 3);
                int rz = (int) Math.floor(dz * 3);

                if (rx >= 3)
                {
                    rx = 2;
                }

                if (rz >= 3)
                {
                    rz = 2;
                }

                if (rx < 0)
                {
                    rx = 0;
                }

                if (rz < 0)
                {
                    rz = 0;
                }

                toggleRut(ter, rx, 0, rz);
                break;
            }
        }

        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Face1">
        for (double test = 0; test < 4 && dir == 1; test += 0.01)
        {
            double tx = p.posX + a * test;
            double ty = p.posY + p.getEyeHeight() + b * test;
            double tz = p.posZ + c * test;
            double testy = ty - (double) j;

            if (testy >= 0.98 && testy <= 1.0)
            {
                double dx = Math.abs(tx - (double) i) - 0.02;
                double dz = Math.abs(tz - (double) k) - 0.02;
                int rx = (int) Math.floor(dx * 3);
                int rz = (int) Math.floor(dz * 3);

                if (rx >= 3)
                {
                    rx = 2;
                }

                if (rz >= 3)
                {
                    rz = 2;
                }

                if (rx < 0)
                {
                    rx = 0;
                }

                if (rz < 0)
                {
                    rz = 0;
                }

                toggleRut(ter, rx, 2, rz);
                break;
            }
        }

        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Face2">
        for (double test = 0; test < 4 && dir == 2; test += 0.01)
        {
            double tx = p.posX + a * test;
            double ty = p.posY + p.getEyeHeight() + b * test;
            double tz = p.posZ + c * test;
            double testz = tz - (double) k;

            if (testz >= 0.0 && testz <= 0.02)
            {
                double dy = Math.abs(ty - (double) j) - 0.02;
                double dx = Math.abs(tx - (double) i) - 0.02;
                int ry = (int) Math.floor(dy * 3);
                int rx = (int) Math.floor(dx * 3);

                if (ry >= 3)
                {
                    ry = 2;
                }

                if (rx >= 3)
                {
                    rx = 2;
                }

                if (ry < 0)
                {
                    ry = 0;
                }

                if (rx < 0)
                {
                    rx = 0;
                }

                toggleRut(ter, rx, ry, 0);
                break;
            }
        }

        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Face3">
        for (double test = 0; test < 4 && dir == 3; test += 0.01)
        {
            double tx = p.posX + a * test;
            double ty = p.posY + p.getEyeHeight() + b * test;
            double tz = p.posZ + c * test;
            double testz = tz - (double) k;

            if (testz >= 0.98 && testz <= 1.0)
            {
                double dy = Math.abs(ty - (double) j) - 0.02;
                double dx = Math.abs(tx - (double) i) - 0.02;
                int ry = (int) Math.floor(dy * 3);
                int rx = (int) Math.floor(dx * 3);

                if (ry >= 3)
                {
                    ry = 2;
                }

                if (rx >= 3)
                {
                    rx = 2;
                }

                if (ry < 0)
                {
                    ry = 0;
                }

                if (rx < 0)
                {
                    rx = 0;
                }

                toggleRut(ter, rx, ry, 2);
                break;
            }
        }

        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Face4">
        for (double test = 0; test < 4 && dir == 4; test += 0.01)
        {
            double tx = p.posX + a * test;
            double ty = p.posY + p.getEyeHeight() + b * test;
            double tz = p.posZ + c * test;
            double testx = tx - (double) i;

            if (testx >= 0.00 && testx <= 0.02)
            {
                double dy = Math.abs(ty - (double) j) - 0.02;
                double dz = Math.abs(tz - (double) k) - 0.02;
                int ry = (int) Math.floor(dy * 3);
                int rz = (int) Math.floor(dz * 3);

                if (ry >= 3)
                {
                    ry = 2;
                }

                if (rz >= 3)
                {
                    rz = 2;
                }

                if (ry < 0)
                {
                    ry = 0;
                }

                if (rz < 0)
                {
                    rz = 0;
                }

                toggleRut(ter, 0, ry, rz);
                break;
            }
        }

        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Face5">
        for (double test = 0; test < 4 && dir == 5; test += 0.01)
        {
            double tx = p.posX + a * test;
            double ty = p.posY + p.getEyeHeight() + b * test;
            double tz = p.posZ + c * test;
            double testx = tx - (double) i;

            if (testx >= 0.98 && testx <= 1.0)
            {
                double dy = Math.abs(ty - (double) j) - 0.02;
                double dz = Math.abs(tz - (double) k) - 0.02;
                int ry = (int) Math.floor(dy * 3);
                int rz = (int) Math.floor(dz * 3);

                if (ry >= 3)
                {
                    ry = 2;
                }

                if (rz >= 3)
                {
                    rz = 2;
                }

                if (ry < 0)
                {
                    ry = 0;
                }

                if (rz < 0)
                {
                    rz = 0;
                }

                toggleRut(ter, 2, ry, rz);
                break;
            }
        }

        //</editor-fold>
        return true;
    }

    public void toggleRut(TileEntityRut rut, int x, int y, int z)
    {
        rut.setRut(x, y, z, rut.getRut(x, y, z) == 0 ? 1 : 0);

        if (rut.isEmpty())
        {
            rut.resetBlock();
        }
    }

    private static int determineOrientation(World world, int i, int j, int k, EntityPlayer entityplayer)
    {
        if (MathHelper.abs((float)entityplayer.posX - (float)i) < 2.0F && MathHelper.abs((float)entityplayer.posZ - (float)k) < 2.0F)
        {
            double d = (entityplayer.posY + 1.8200000000000001D) - (double)entityplayer.yOffset;

            if (d - (double)j > 2D)
            {
                return 1;
            }

            if ((double)j - d > 0.0D)
            {
                return 0;
            }
        }

        int l = MathHelper.floor_double((double)((entityplayer.rotationYaw * 4F) / 360F) + 0.5D) & 3;

        if (l == 0)
        {
            return 2;
        }

        if (l == 1)
        {
            return 5;
        }

        if (l == 2)
        {
            return 3;
        }

        return l != 3 ? 0 : 4;
    }

    @Override
    public void breakBlock(World world, int i, int j, int k, int b, int m) {
		if (world.isRemote) {
			return;
		}
        TileEntityRut ter = (TileEntityRut)world.getBlockTileEntity(i, j, k);

        if (ter.isDead)
        {
            return;
        }

        super.onBlockDestroyedByPlayer(world, i, j, k, m);
        int bid = ter.maskBlock;
        int meta = ter.maskMeta;
        int drop = Block.blocksList[bid].idDropped(meta, new Random(), 0);
        int mdrop = DustModBouncer.damageDropped(Block.blocksList[bid], meta);
        int qdrop = Block.blocksList[bid].quantityDropped(new Random());
        this.dropBlockAsItem_do(world, i, j, k, new ItemStack(drop, qdrop, mdrop));

        if (ter.fluid != 0 && !ter.fluidIsFluid() && ter.canEdit())
        {
            this.dropBlockAsItem_do(world, i, j, k, new ItemStack(ter.fluid, 1, 0));
        }
    }

    @Override
    public int idDropped(int i, Random random, int j)
    {
        return 0;
    }

    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return new TileEntityRut();
    }
}
