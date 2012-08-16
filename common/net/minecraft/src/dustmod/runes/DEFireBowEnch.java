/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minecraft.src.dustmod.runes;

import net.minecraft.src.Block;
import net.minecraft.src.Enchantment;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityItem;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.dustmod.DustEvent;
import net.minecraft.src.dustmod.EntityDust;

/**
 *
 * @author billythegoat101
 */
public class DEFireBowEnch extends DustEvent
{
    public DEFireBowEnch()
    {
        super();
    }

    public void onInit(EntityDust e)
    {
//        List<EntityItem> sacrifice = getSacrifice(e);
//        int item = Item.bow.shiftedIndex;
//        for(EntityItem i:sacrifice){
//            ItemStack is = i.item;
//
//            if(is.itemID == Item.pickaxeDiamond.shiftedIndex || is.itemID == Item.shovelDiamond.shiftedIndex) {
//                item = is.itemID;
//                break;
//            }
//        }
//        int gold = ((item == Item.pickaxeDiamond.shiftedIndex) ? Item.pickaxeGold.shiftedIndex:Item.shovelGold.shiftedIndex);
        ItemStack[] req = this.sacrifice(e, new ItemStack[] {new ItemStack(Item.bow, 1, 0),
                      new ItemStack(Block.blockGold.blockID, 1, 0), new ItemStack(Item.fireballCharge, 9)
        });

        if (!checkSacrifice(req) || !takeXP(e, 30))
        {
            e.fizzle();
            return;
        }

        e.renderBeam = true;
        e.renderStar = true;
        e.setColorOuter(0, 0, 255);
        e.setColorBeam(0, 0, 255);
    }

    public void onTick(EntityDust e)
    {
        e.starScale += 0.001;

        if (e.ticksExisted > 20)
        {
            Entity en = null;
            ItemStack create =  new ItemStack((int)Item.bow.shiftedIndex, 1, 0);
//            if(e.data == mod_DustMod.spiritSword.shiftedIndex){
            create.addEnchantment(Enchantment.flame, 1);
//            }
//            System.out.println("derp " + create.itemID);
            en = new EntityItem(e.worldObj, e.posX, e.posY - EntityDust.yOffset, e.posZ, create);

            if (en != null)
            {
                en.setPosition(e.posX, e.posY, e.posZ);
                e.worldObj.spawnEntityInWorld(en);
            }

            e.fade();
        }
    }
}
