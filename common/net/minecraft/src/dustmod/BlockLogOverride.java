/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minecraft.src.dustmod;

import net.minecraft.src.BlockLog;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.World;

/**
 *
 * @author billythegoat101
 */
public class BlockLogOverride extends BlockLog
{
    
    public BlockLogOverride(int par1)
    {
        super(par1);
    }

    @Override
    public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving player)
    {
        super.onBlockPlacedBy(world, i, j, k, player);
        world.setBlockMetadataWithNotify(i, j, k, world.getBlockMetadata(i, j, k)+4);
    }
    @Override
    public int getBlockTextureFromSideAndMetadata(int side, int meta)
    {
        if(meta >= 4) meta -= 4;
        return side == 1 ? 21 : (side == 0 ? 21 : (meta == 1 ? 116 : (meta == 2 ? 117 : (meta == 3 ? 153 : 20))));
    }
    
    
    /**
     * Determines the damage on the item the block drops. Used in cloth and wood.
     */
    @Override
    protected int damageDropped(int par1)
    {
        if(par1 >= 4) par1 -= 4;
        return par1;
    }
}
