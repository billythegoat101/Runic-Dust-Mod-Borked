/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minecraft.src.dustmod;

import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumRarity;
import net.minecraft.src.EnumToolMaterial;
import net.minecraft.src.ItemPickaxe;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.World;

/**
 *
 * @author billythegoat101
 */
public class ItemSpiritPickaxe extends ItemPickaxe
{
    private static Block blocksEffectiveAgainst[];

    public ItemSpiritPickaxe(int i, EnumToolMaterial enumtoolmaterial)
    {
        super(i, enumtoolmaterial);
        setMaxDamage(131);
        efficiencyOnProperMaterial = 16F;
        
        //[non-forge]
//        this.setIconIndex(ModLoader.addOverride("/gui/items.png", mod_DustMod.path + "/spiritpick.png"));
        //[forge]
        this.setIconCoord(0,1);
        this.setTextureFile(DustMod.path + "/dustItems.png");
    }

    public EnumRarity func_40398_f(ItemStack itemstack)
    {
        return EnumRarity.epic;
    }

    /**
     * On block destroyed by pick, called by Minecraft basics
     *
     * @param itemstack  itemstack in hand
     * @param l     block id destroyed
     * @param i     x location of block destroyed
     * @param j     y location of block destroyed
     * @param k     z location of block destroyed
     * @param e     entity (player) who destoyed the block
     * @return dunno dun care, i think it returns true if the block should get destroyed
     */
    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, int i, int j, int k, EntityPlayer e)
    {
    	System.out.println("StartBreak " + e.worldObj.isRemote);
//        if (e.worldObj.isRemote)
//        {
//            return false;
//        }
        System.out.println("Check1");

        int r = 1; //radius of destruction
        World world = e.worldObj;

        for (int di = -r; di <= r; di++)         //*for every block within radius
        {
            for (int dj = -r; dj <= r; dj++)     //*
            {
                for (int dk = -r; dk <= r; dk++) //*
                {
                    int bid = world.getBlockId(di + i, dj + j, dk + k);
                    Block block = Block.blocksList[bid];

                    if (block != null) //block is not null (air)
                    {
                        if (block.blockMaterial == Material.rock && block != Block.bedrock) //if block is made of rock
                        {
                            world.setBlockWithNotify(i + di, j + dj, k + dk, 0); //update world
//                            block.onBlockDestroyedByPlayer(world, i + di, j + dj, k + dk, world.getBlockMetadata(i + di, j + dj, k + dk)); //destroy block
//                            block.dropBlockAsItem(world, i + di, j + dj, k + dk, bid, 0); //drop block
                        }
                    }
                }
            }
        }

        return super.onBlockStartBreak(itemstack, i, j, k, e);
    }
}
