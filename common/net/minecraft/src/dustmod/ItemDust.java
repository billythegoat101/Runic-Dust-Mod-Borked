/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minecraft.src.dustmod;

import java.util.List;

import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemReed;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

/**
 *
 * @author billythegoat101
 */
public class ItemDust extends ItemReed 
{
    private int blockID;

    private int plantTex;
    private int gunTex;
    private int lapisTex;
    private int blazeTex;

    public ItemDust(int i, Block block)
    {
        super(i, block);
        blockID = block.blockID;
        setMaxDamage(0);
        setHasSubtypes(true);
        
        //[non-forge]
//        plantTex = ModLoader.addOverride("/gui/items.png", mod_DustMod.path + "/plantdust.png");
//        gunTex = ModLoader.addOverride("/gui/items.png", mod_DustMod.path + "/gundust.png");
//        lapisTex = ModLoader.addOverride("/gui/items.png", mod_DustMod.path + "/lapisdust.png");
//        blazeTex = ModLoader.addOverride("/gui/items.png", mod_DustMod.path + "/blazedust.png");
        
        //[forge]
        this.setTextureFile(DustMod.path + "/dustItems.png");
    }

//    @Override
//    public boolean onItemUseFirst(ItemStack itemstack, EntityPlayer p, World world, int i, int j, int k, int l)
//    {
//        int i1 = world.getBlockId(i, j, k);
//
//        if (i1 == Block.snow.blockID)
//        {
//            l = 0;
//        }
//        else if (i1 != Block.vine.blockID)
//        {
//            if (l == 0)
//            {
//                j--;
//            }
//
//            if (l == 1)
//            {
//                j++;
//            }
//
//            if (l == 2)
//            {
//                k--;
//            }
//
//            if (l == 3)
//            {
//                k++;
//            }
//
//            if (l == 4)
//            {
//                i--;
//            }
//
//            if (l == 5)
//            {
//                i++;
//            }
//        }
//
//        if (itemstack.stackSize == 0)
//        {
//            return false;
//        }
//
//        if (!p.canPlayerEdit(i, j, k))
//        {
//            return false;
//        }
//
//        if (j == world.getHeight() - 1 && Block.blocksList[blockID].blockMaterial.isSolid())
//        {
//            return false;
//        }
//
//        if (world.canPlaceEntityOnSide(blockID, i, j, k, true, 0, null))
//        {
//            System.out.println("woo? " + i + " " + j + " " + k + " " + l + " " + world.isRemote);
//            Block block = Block.blocksList[blockID];
//
//            if (!world.isRemote && world.setBlockAndMetadataWithNotify(i, j, k, blockID, getMetadata(itemstack.getItemDamage())))
//            {
//                if (world.getBlockId(i, j, k) == blockID)
//                {
////                    Block.blocksList[blockID].onBlockPlaced(world, i, j, k, l);
//                    Block.blocksList[this.spawnID].updateBlockMetadata(par3World, par4, par5, par6, par7, par8, par9, par10);
//                    Block.blocksList[blockID].onBlockPlacedBy(world, i, j, k, p);
//                }
//
//                world.playSoundEffect((float)i + 0.5F, (float)j + 0.5F, (float)k + 0.5F, block.stepSound.getStepSound(), (block.stepSound.getVolume() + 1.0F) / 6.0F, block.stepSound.getPitch() * 0.99F);
//                itemstack.stackSize--;
//                block.onBlockActivated(world, i, j, k, p, 0, 0, 0, 0);
////                TileEntityRetChest te = ((TileEntityRetChest)world.getBlockTileEntity(i, j, k));
//////                System.out.println("DAM " + itemstack.getItemDamage());
////                te.id = itemstack.getItemDamage();
////                GroupInventory gis = mod_RetainerChest.chests.get(itemstack.getItemDamage());
////                if(gis != null){
////                    gis.fillChest(te);
////                }
//            }
//
//            return true;
//        }
//        else
//        {
////            System.out.println("dammit");
//            return false;
//        }
//    }
    
    
    public boolean tryPlaceIntoWorld(ItemStack item, EntityPlayer p, World world, int i, int j, int k, int dir, float x, float y, float z)
    {
        int var11 = world.getBlockId(i, j, k);

        if (var11 == Block.snow.blockID)
        {
            dir = 1;
        }
        else if (var11 != Block.vine.blockID && var11 != Block.tallGrass.blockID && var11 != Block.deadBush.blockID)
        {
            if (dir == 0)
            {
                --j;
            }

            if (dir == 1)
            {
                ++j;
            }

            if (dir == 2)
            {
                --k;
            }

            if (dir == 3)
            {
                ++k;
            }

            if (dir == 4)
            {
                --i;
            }

            if (dir == 5)
            {
                ++i;
            }
        }

        if (!p.canPlayerEdit(i, j, k))
        {
            return false;
        }
        else if (item.stackSize == 0)
        {
            return false;
        }
        else
        {
            if (world.canPlaceEntityOnSide(this.blockID, i, j, k, false, dir, (Entity)null))
            {
                Block var12 = Block.blocksList[this.blockID];

                if (world.setBlockWithNotify(i, j, k, this.blockID))
                {
                    if (world.getBlockId(i, j, k) == this.blockID)
                    {
                        Block.blocksList[this.blockID].updateBlockMetadata(world, i, j, k, dir, x, y, z);
                        Block.blocksList[this.blockID].onBlockPlacedBy(world, i, j, k, p);
                    }

                    world.playSoundEffect((double)((float)i + 0.5F), (double)((float)j + 0.5F), (double)((float)k + 0.5F), var12.stepSound.getStepSound(), (var12.stepSound.getVolume() + 1.0F) / 6.0F, var12.stepSound.getPitch() * 0.99F);
                    --item.stackSize;
                }
            }

            return true;
        }
    }
    

    @Override
    public String getItemNameIS(ItemStack itemstack)
    {
        switch (itemstack.getItemDamage())
        {
            case 1:
                return "tile.plantdust";

            case 2:
                return "tile.gundust";

            case 3:
                return "tile.lapisdust";

            case 4:
                return "tile.blazedust";
        }

        return "tile.dust";
    }

    @Override
    public String getLocalItemName(ItemStack itemstack)
    {
        switch (itemstack.getItemDamage())
        {
            case 1:
                return "tile.plantdust";

            case 2:
                return "tile.gundust";

            case 3:
                return "tile.lapisdust";

            case 4:
                return "tile.blazedust";
        }

        return "tile.dust";
    }

    @Override
    public int getIconFromDamage(int i)
    {
        return i-1;
//        switch (i)
//        {
//            case 1:
//                return plantTex;
//
//            case 2:
//                return gunTex;
//
//            case 3:
//                return lapisTex;
//
//            case 4:
//                return blazeTex;
//
//            default:
//                return Item.brick.getIconFromDamage(0);
//        }
    }
    

    @SideOnly(Side.CLIENT)

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int var4 = 1; var4 < 5; ++var4)
        {
            par3List.add(new ItemStack(par1, 1, var4));
        }
    }
}