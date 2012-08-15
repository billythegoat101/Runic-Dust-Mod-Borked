/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minecraft.src.dustmod.events;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityList;
import net.minecraft.src.EntitySkeleton;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.dustmod.DustEvent;
import net.minecraft.src.dustmod.DustMod;
import net.minecraft.src.dustmod.EntityDust;

/**
 *
 * @author billythegoat101
 */
public class DEResurrection extends DustEvent
{
    public void onInit(EntityDust e)
    {
        e.renderBeam = true;
        e.renderStar = true;
        ItemStack[] sac = new ItemStack[] {new ItemStack(Item.ghastTear, 1), new ItemStack(Block.slowSand, 4)};
        sac = this.sacrifice(e, sac);

        if (!checkSacrifice(sac))
        {
            System.out.println("k1");
            e.fizzle();
            return;
        }

        //get sacrifice
        ArrayList<EntityItem> itemstacks = new ArrayList<EntityItem>();
        List l = getEntities(e);

        for (Object o: l)
        {
            if (o instanceof EntityItem)
            {
                EntityItem ei = (EntityItem)o;
                itemstacks.add(ei);
            }
        }

        if (itemstacks.size() == 0)
        {
            System.out.println("k2");
            e.kill();
            return;
        }

        int entClass = -1;

        for (EntityItem ei: itemstacks)
        {
            if (entClass != -1)
            {
                break;
            }

            int id = ei.item.itemID;
            int m = ei.item.getItemDamage();
            int amount;
            int amt = amount = 2;

            for (EntityItem ent: itemstacks)
            {
                if (ent.item.itemID == id && ent.item.getItemDamage() == m)
                {
                    amount -= ent.item.stackSize;
                }
            }

            if (amount <= 0 && DustMod.getEntityIDFromDrop(new ItemStack(id, 0, m), 0) != -1)
            {
                for (EntityItem ent: itemstacks)
                {
                    if (ent.item.itemID == id && ent.item.getItemDamage() == m)
                    {
                        while (amt > 0 && ent.item.stackSize > 0)
                        {
                            amt--;
                            ent.item.stackSize--;

                            if (ent.item.stackSize <= 0)
                            {
                                ent.setDead();
                            }
                        }
                    }
                }

                entClass = DustMod.getEntityIDFromDrop(new ItemStack(id, 0, m), 0);

//                System.out.println("success " + " " + itemID + " " + entClass);
                if (entClass != -1)
                {
                    break;
                }
            }
        }

//        int itemID = itemstacks.get(0).item.itemID;
//        int meta = itemstacks.get(0).item.getItemDamage();
//        for(EntityItem ei:itemstacks){
//            ItemStack is = ei.item;
//            if(is.itemID != itemID || is.getItemDamage() != meta){
//            System.out.println("k3 " + itemID + " " + is.itemID);
//                e.kill();
//                return;
//            }
//        }
//        int amt = 2;
//        for(EntityItem ei:itemstacks){
//            ItemStack is = ei.item;
//            while(amt > 0 && is.stackSize > 0){
//                amt--;
//                is.stackSize--;
//            }
//            if(is.stackSize <= 0)  mod_DustMod.killEntity(ei);
//        }

        if (entClass == -1)
        {
            System.out.println("k4");
            e.fizzle();
            return;
        }

        e.data[0] = (byte)entClass;
        EntitySkeleton test;
    }

    public void onTick(EntityDust e)
    {
        e.starScale += 0.001;

        if (e.ticksExisted > 120)
        {
            Entity en = null;
            en = EntityList.createEntityByID((int)e.data[0], e.worldObj);

            if (en != null)
            {
                en.setPosition(e.posX, e.posY - EntityDust.yOffset, e.posZ);
                boolean blah = e.worldObj.spawnEntityInWorld(en);
            }

            e.fade();
        }
    }
}