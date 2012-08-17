/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dustmod.runes;

import java.util.List;

import dustmod.*;
import net.minecraft.src.*;

/**
 *
 * @author billythegoat101
 */
public class DEFortuneEnch extends DustEvent
{
    public DEFortuneEnch()
    {
        super();
    }

    public void onInit(EntityDust e)
    {
        List<EntityItem> sacrifice = getItems(e);
        int item = Item.pickaxeDiamond.shiftedIndex;

        for (EntityItem i: sacrifice)
        {
            ItemStack is = i.item;

            if (is.itemID == Item.pickaxeDiamond.shiftedIndex || is.itemID == Item.swordDiamond.shiftedIndex)
            {
                item = is.itemID;
                break;
            }
        }

//        int gold = ((item == Item.pickaxeDiamond.shiftedIndex) ? Item.pickaxeGold.shiftedIndex:Item.swordGold.shiftedIndex);
        ItemStack[] req = this.sacrifice(e, new ItemStack[] {new ItemStack(item, 1, 0),
                      new ItemStack(Block.blockGold.blockID, 1, 0),
                      new ItemStack(Block.oreDiamond.blockID, 1, 0),
                      new ItemStack(Block.oreRedstone.blockID, 1, 0),
                      new ItemStack(Block.oreLapis.blockID, 1, 0)
        });

        if (!checkSacrifice(req) || !takeXP(e, 40))
        {
            e.fizzle();
            return;
        }

        e.renderBeam = true;
        e.renderStar = true;
        e.setColorStarOuter(0, 0, 255);
        e.setColorBeam(0,0,255);
        e.data[0] = item;
    }

    public void onTick(EntityDust e)
    {
        e.starScale += 0.001;

        if (e.ticksExisted > 20)
        {
            Entity en = null;
            ItemStack create =  new ItemStack((int)e.data[0], 1, 0);

            if (e.data[0] == Item.swordDiamond.shiftedIndex)
            {
                create.addEnchantment(Enchantment.looting, 4);
            }

            if (e.data[0] == Item.pickaxeDiamond.shiftedIndex)
            {
                create.addEnchantment(Enchantment.fortune, 4);
            }

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
