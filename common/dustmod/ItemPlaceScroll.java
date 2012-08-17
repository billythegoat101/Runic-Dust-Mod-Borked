/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dustmod;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.World;

/**
 *
 * @author billythegoat101
 */
public class ItemPlaceScroll extends Item
{
    private int blockID;

    public ItemPlaceScroll(int i)
    {
        super(i);
        blockID = DustMod.dust.blockID;
        setMaxDamage(0);
        setHasSubtypes(true);
        this.setMaxStackSize(4);
        
        //[non-forge]
        //this.setIconIndex(ModLoader.addOverride("/gui/items.png", mod_DustMod.path + "/scroll.png"));
        //[forge]
        this.setIconCoord(0,2);
        this.setTextureFile(DustMod.path + "/dustItems.png");
    }

    @Override
    public boolean tryPlaceIntoWorld(ItemStack itemstack, EntityPlayer p, World world, int i, int j, int k, int l, float x, float y, float z)
    {
    	if(world.isRemote) return false;
    
        DustShape ds = DustManager.getShapeFromID(itemstack.getItemDamage());
        int r = (int)MathHelper.floor_double((double)((p.rotationYaw * 4F) / 360F) + 0.5D) & 3;
        
//        if (world.canBlockBePlacedAt(blockID, i, j, k, false, l))
//        {
        if (DustMod.isDust(world.getBlockId(i, j, k)))
        {
            j--;
        }

        if (ds.drawOnWorld(world, i, j, k, p, r))
        {
            itemstack.stackSize--;
//                System.out.println("Drawing success!");
        }

        return true;
    }

    public String getItemNameIS(ItemStack itemstack)
    {
        return "tile.scroll" + DustManager.getShapeFromID(itemstack.getItemDamage()).name;
    }

    public String getLocalItemName(ItemStack itemstack)
    {
        return getItemNameIS(itemstack);
    }
}