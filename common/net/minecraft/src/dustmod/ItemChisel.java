/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minecraft.src.dustmod;

import net.minecraft.src.*;

/**
 *
 * @author billythegoat101
 */
public class ItemChisel extends Item
{
    private int tex;

    public ItemChisel(int i)
    {
        super(i);
//        blockID = block.blockID;
//        setMaxDamage(0);
//        setHasSubtypes(true);
        
        //[non-forge]
//        tex = ModLoader.addOverride("/gui/items.png", mod_DustMod.path + "/hammerandchisel.png");
//        this.setIconIndex(tex);
        
        //[forge]
        this.setIconCoord(4,2);
        this.setTextureFile(DustMod.path + "/dustItems.png");
        
        setMaxStackSize(1);
        setMaxDamage(238);
    }

    @Override
    public boolean tryPlaceIntoWorld(ItemStack itemstack, EntityPlayer p, World world, int i, int j, int k, int dir, float x, float y, float z)
    {
        int blockId = world.getBlockId(i, j, k);
//        if(blockId == Block.grass.blockID) blockId = Block.dirt.blockID;
        int meta = world.getBlockMetadata(i, j, k);
        Block b = Block.blocksList[blockId];

        if (b == DustMod.rutBlock)
        {
            itemstack.damageItem(1, p);
        }

        if (b == null)
        {
            return false;
        }

        if (b.getBlockHardness(world,i,j,k) > Block.wood.getBlockHardness(world,i,j,k))
        {
            return false;
        }
        else if (!b.isOpaqueCube() || b.getRenderType() != 0 || !b.renderAsNormalBlock())
        {
            return false;
        }

        itemstack.damageItem(1, p);

//        if (!world.isRemote)
//        {
            world.setBlockAndMetadataWithNotify(i, j, k, DustMod.rutBlock.blockID, meta);
            TileEntityRut ter = (TileEntityRut)world.getBlockTileEntity(i, j, k);
            ter.maskBlock = blockId;
            ter.maskMeta = meta;
            DustMod.rutBlock.onBlockActivated(world, i, j, k, p,0,0,0,0);
            System.out.println("Set");
//        }

//        System.out.println("Setting to " + blockID + " " + meta);
        return true;
    }
//    @Override
//    public String getItemName() {
//        return "dustchisel";
//    }
}