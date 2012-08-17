/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dustmod;

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
    	String id = DustItemManager.getIDS()[itemstack.getItemDamage()];
    	if(id != null) return "tile." + DustItemManager.idsRemote[itemstack.getItemDamage()];

        return "tile.dust";
    }

    @Override
    public String getLocalItemName(ItemStack itemstack)
    {
    	String id = DustItemManager.getIDS()[itemstack.getItemDamage()];
    	if(id != null) return "tile." + DustItemManager.idsRemote[itemstack.getItemDamage()];

        return "tile.dust";
    }

    @Override
    public int getIconFromDamage(int i)
    {
        return i-1;
    }
    

    @SideOnly(Side.CLIENT)

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int i = 5; i < 1000; ++i) //i > 4 for migration from old system
        {
        	if(DustItemManager.getColors()[i] != null){
                par3List.add(new ItemStack(par1, 1, i));
        	}
        }
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromDamage(int meta, int pass) {
    	return pass == 0 ? DustItemManager.getPrimaryColor(meta) : DustItemManager.getSecondaryColor(meta);
    }


    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }

    @SideOnly(Side.CLIENT)

    /**
     * Gets an icon index based on an item's damage value and the given render pass
     */
    public int getIconFromDamageForRenderPass(int meta, int rend)
    {
        return rend > 0 ? 5 : 4;
    }
}