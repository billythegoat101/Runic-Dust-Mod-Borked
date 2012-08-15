/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minecraft.src.dustmod.events;

import java.util.Random;

import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;
import net.minecraft.src.dustmod.DustEvent;
import net.minecraft.src.dustmod.DustMod;
import net.minecraft.src.dustmod.EntityDust;

/**
 *
 * @author billythegoat101
 */
public class DEFarm extends DustEvent
{
    public DEFarm()
    {
        super();
    }

    public void onInit(EntityDust e)
    {
        ItemStack[] req = new ItemStack[]
        {
            new ItemStack(Item.ingotIron, 8, -1)
        };
        sacrifice(e, req);

        if (req[0].stackSize > 0 || !this.takeXP(e, 4))
        {
            e.fizzle();
            return;
        }

//        e.ignoreRune = true;
        int dustID = e.dusts[e.dusts.length / 2][e.dusts[0].length / 2];
//        System.out.println("DATA: \n" + Arrays.deepToString(e.dusts));
        int r = 1;
        int cBase = 0;
        int cRand = 1;

        switch (dustID)
        {
            case 1:
                r = 1;
                cBase = 0;
                cRand = 2;
                break;

            case 2:
                r = 2;
                cBase = 1;
                cRand = 3;
                break;

            case 3:
                r = 3;
                cBase = 3;
                cRand = 3;
                break;

            case 4:
                r = 4;
                cBase = 4;
                cRand = 5;
                break;
        }

        int i = e.getX();
        int j = e.getY();
        int k = e.getZ();
//        System.out.println("R = " + r + " " + dustID);
        World world = e.worldObj;
        world.setBlockWithNotify(i, j - 1, k, Block.waterStill.blockID);
        Random rand = new Random();

        for (int di = -r; di <= r; di++)
        {
            for (int dk = -r; dk <= r; dk++)
            {
                layer:

                for (int dj = r; dj >= -r; dj--)
                {
                    int bidt = world.getBlockId(di + i, dj + j, dk + k);
                    int bidb = world.getBlockId(di + i, dj + j - 1, dk + k);

                    if ((bidb == Block.dirt.blockID || bidb == Block.grass.blockID || bidb == Block.tilledField.blockID || bidb == Block.sand.blockID) && (bidt == 0 || DustMod.isDust(bidt) || bidt == Block.tallGrass.blockID))
                    {
                        world.setBlockWithNotify(i + di, j + dj - 1, k + dk, Block.tilledField.blockID);
                        int meta = cBase + rand.nextInt(cRand);

                        if (meta > 7)
                        {
                            meta = 7;
                        }

                        world.setBlockAndMetadataWithNotify(i + di, j + dj, k + dk, 0, 0);
                        world.setBlockAndMetadataWithNotify(i + di, j + dj, k + dk, Block.crops.blockID, meta);
//                        System.out.println("setting");
                        break layer;
                    }//else

//                        System.out.println("wat " + bidb + " " + bidt + " " + dj);
//                    System.out.println("DURR " + bidu + "," + bid + " " + Block.grass.blockID + " " + Block.dirt.blockID );
                }
            }
        }

        world.setBlockWithNotify(i, j - 1, k, Block.waterStill.blockID);
    }

    public void onTick(EntityDust e)
    {
        e.fade();
    }
}
