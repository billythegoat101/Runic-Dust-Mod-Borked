/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dustmod.runes;

import java.util.List;

import dustmod.DustEvent;
import dustmod.EntityDust;

import net.minecraft.src.Block;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityList;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.TileEntityMobSpawner;

/**
 *
 * @author billythegoat101
 */
public class DESpawnerReprog extends DustEvent
{
    public DESpawnerReprog()
    {
        super();
    }
	
	@Override
    public void initGraphics(EntityDust e){
    	super.initGraphics(e);

		e.setRenderStar(true);
		e.setRenderBeam(true);
        e.setStarScale(1.05F);
		
    }

    public void onInit(EntityDust e)
    {
//        int compare = mod_DustMod.compareDust(mod_DustMod.lapisDID, e.dustID);
//        if(compare < 0){
//            e.fizzle();
//            return;
//        }
		e.setRenderStar(true);
		e.setRenderBeam(true);
        e.setStarScale(1.05F);
        //        //<editor-fold defaultstate="collapsed" desc="old">
        //        //get sacrifice
        //        ArrayList<EntityItem> itemstacks = new ArrayList<EntityItem>();
        //        List l = getEntities(e);
        //        for(Object o:l){
        //            if(o instanceof EntityItem){
        //                EntityItem ei = (EntityItem)o;
        //                if(ei.item.itemID != Block.blockDiamond.blockID) itemstacks.add(ei);
        //            }
        //        }
        //        if(itemstacks.size() == 0){
        //            e.fizzle();
        //            return;
        //        }
        ////        for(EntityItem ei:itemstacks){
        ////            ItemStack is = ei.item;
        ////            if(is.itemID != itemID || is.getItemDamage() != meta){
        ////                e.kill();
        ////                return;
        ////            }
        ////        }
        //
        //        int itemID = -1;
        //        int itemMeta = -1;
        //        outer:
        //        for(EntityItem ei:itemstacks){
        //            ItemStack is = ei.item;
        //            int id = is.itemID;
        //            int meta = is.getItemDamage();
        //            int amt = mod_DustMod.getStackSizeForEntityDrop(id, meta) * 2; //mul
        //            if(amt < 0) continue;
        //            ArrayList<EntityItem> toKill = new ArrayList<EntityItem>();
        //            inner:
        //            for(EntityItem eTemp:itemstacks){
        //                if(eTemp.item.itemID == id && eTemp.item.getItemDamage() == meta){
        //                    while(eTemp.item.stackSize > 0 && amt > 0){
        //                        eTemp.item.stackSize--;
        //                        amt--;
        //                    }
        //                    if(eTemp.item.stackSize == 0) toKill.add(eTemp);
        //                }
        //                if(amt == 0){
        //                    break inner;
        //                }
        //            }
        //            if(amt == 0){
        //                for(EntityItem kill:toKill){
        //                    mod_DustMod.killEntity(kill);
        //                }
        //                itemID = id;
        //                itemMeta = meta;
        //                break;
        //            }
        //        }
        //
        //        int entClass = mod_DustMod.getEntityIDFromDrop(new ItemStack(itemID, -1, itemMeta),0);
        //
        //        if(entClass == -1){
        //            e.fizzle();
        //            return;
        //        }
        //</editor-fold>
//        ItemStack[] req = new ItemStack[]{new ItemStack(Block.blockDiamond.blockID, 1, -1)};
//        sacrifice(e,req);
//        if(req[0].stackSize > 0) {
//            System.out.println("NoDiamonds");
//            e.fizzle();
//            return;
//        }
        int entClass = -1;
        List l = getEntities(e);

        for (Object o: l)
        {
            if (o instanceof EntityItem)
            {
                EntityItem ei = (EntityItem)o;
                ItemStack item = ei.item;

                if (item.itemID == Item.monsterPlacer.shiftedIndex)
                {
                    entClass = item.getItemDamage();
                    item.stackSize--;

                    if (item.stackSize <= 0)
                    {
                        ei.setDead();
                    }
                }
            }
        }

        ItemStack[] req = new ItemStack[] {new ItemStack(Item.enderPearl, 4)};
        req = this.sacrifice(e, req);

        if (!checkSacrifice(req) || entClass == -1 || !takeXP(e, 25))
        {
            e.fizzle();
            return;
        }

        e.data[0] = entClass;
    }

    public void onTick(EntityDust e)
    {
        if (e.ticksExisted > 120)
        {
            String mob = EntityList.getEntityString(EntityList.createEntityByID(e.data[0], e.worldObj));
            int[] fin = new int[3];

            for (Integer[] i: e.dustPoints)
            {
                fin[0] += i[0];
                fin[1] += i[1];
                fin[2] += i[2];
            }

            fin[0] /= 8;
            fin[1] /= 8;
            fin[2] /= 8;

            if (e.worldObj.getBlockId(fin[0], fin[1], fin[2]) == Block.mobSpawner.blockID)
            {
                TileEntityMobSpawner tems = ((TileEntityMobSpawner)e.worldObj.getBlockTileEntity(fin[0], fin[1], fin[2]));
                tems.setMobID(mob);
                tems.validate();
//                if(e.ticksExisted > 100){
//                    e.worldObj.setBlockWithNotify(fin[0],fin[1],fin[2],0);
//                    e.worldObj.markBlockNeedsUpdate(fin[0],fin[1],fin[2]);
//                    EntityItem ei = new EntityItem(e.worldObj);
//                    ei.setPosition(e.posX,e.posY+e.yOffset,e.posZ);
//                    ei.item = new ItemStack(Block.mobSpawner, 1);
//                    e.worldObj.spawnEntityInWorld(ei);
//                }
            }

//            if(en != null){
//                en.setPosition(e.posX, e.posY-EntityDust.yOffset, e.posZ);
//                boolean blah = e.worldObj.spawnEntityInWorld(en);
//            }
            e.fade();
        }
        else   //if(e.ticksExisted < 10){
        {
            int[] fin = new int[3];

            for (Integer[] i: e.dustPoints)
            {
                fin[0] += i[0];
                fin[1] += i[1];
                fin[2] += i[2];
            }

            fin[0] /= 8;
            fin[1] /= 8;
            fin[2] /= 8;
            TileEntityMobSpawner tems = ((TileEntityMobSpawner)e.worldObj.getBlockTileEntity(fin[0], fin[1], fin[2]));
            tems.yaw = 0;
        }
    }
}