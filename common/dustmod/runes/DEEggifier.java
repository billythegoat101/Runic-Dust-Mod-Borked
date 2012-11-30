/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dustmod.runes;

import java.util.ArrayList;

import dustmod.*;
import net.minecraft.src.*;

/**
 *
 * @author billythegoat101
 */
public class DEEggifier extends DustEvent
{
    public DEEggifier()
    {
        super();
    }
	
	@Override
    public void initGraphics(EntityDust e){
    	super.initGraphics(e);

        e.setRenderStar(true);
    	
    }

    @Override
    public void onInit(EntityDust e)
    {
        super.onInit(e);
        ItemStack[] req = new ItemStack[] {new ItemStack(Item.egg, 1), new ItemStack(Item.diamond, 1)};
        req = this.sacrifice(e, req);

        if (!checkSacrifice(req) || !takeXP(e, 10))
        {
            e.fizzle();
            return;
        }

        e.setRenderStar(true);
        e.setColorStar(255, 2555, 255);
        e.sacrificeWaiting = 600;

        for (Integer i: DustMod.entdrops.values())
        {
            this.addSacrificeList(new Sacrifice(i));
            System.out.println("Adding sacrifice " + i);
        }
    }

    @Override
    public void onTick(EntityDust e)
    {
        super.onTick(e);
//        System.out.println("IM IN " + e.data[0]);
//        if(e.data[0] == 0){
//            for(ArrayList<Sacrifice> arr: this.waitingSacrifices){
//                for(Sacrifice s:arr){
//                    System.out.println("Rawr " + s.entityType + " " + s.isComplete);
//                    if(s.isComplete){
//                        e.data[0] = s.entityType;
//                    }
//                }
//            }
//            if(e.data[0] == 0) return;
//            System.out.println("EntityType " + e.data[0]);
//        }
        e.setStarScale(e.getStarScale() + 0.001F);

        if (e.ticksExisted > 120)
        {
            EntityItem en = null;
            en = new EntityItem(e.worldObj, e.posX, e.posY, e.posZ, new ItemStack(Item.monsterPlacer, 1, e.data[15]));

            if (en != null)
            {
                en.setPosition(e.posX, e.posY - EntityDust.yOffset, e.posZ);
                boolean blah = e.worldObj.spawnEntityInWorld(en);
            }

            e.fade();
        }
    }

    @Override
    public void onRightClick(EntityDust e, TileEntityDust ted, EntityPlayer p)
    {
        super.onRightClick(e, ted, p);
    }

    @Override
    public void onUnload(EntityDust e)
    {
        super.onUnload(e);
    }
}
