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
public class DEBomb extends DustEvent
{
    public DEBomb()
    {
        super();
    }

    @Override
    public void onInit(EntityDust e)
    {
        ItemStack[] sac = new ItemStack[] {new ItemStack(Item.gunpowder, 5)};
        sac = this.sacrifice(e, sac);

        if (sac[0].stackSize > 0)
        {
            sac = new ItemStack[] {new ItemStack(DustMod.idust, 15, 2)};
            sac = this.sacrifice(e, sac);

            if (sac[0].stackSize > 0)
            {
                e.fizzle();
                return;
            }
        }

        int[] center = new int[4];
        int[] fuse = new int[4];
        int[][] dusts = e.dusts;
        center[0] = dusts[3][1];
        center[1] = dusts[4][1];
        center[2] = dusts[3][2];
        center[3] = dusts[4][2];
        fuse[0] = dusts[0][4];
        fuse[1] = dusts[1][4];
        fuse[2] = dusts[1][3];
        fuse[3] = dusts[2][3];

        for (int i = 0; i < 4; i++)
        {
            if (center[0] != center[i])
            {
                e.fizzle();
                return;
            }

            if (fuse[0] != fuse[i])
            {
                e.fizzle();
                return;
            }
        }

        int c = center[0];
        int f = fuse[0];
        c = c << 3;
        e.data[0] = c | f;
        e.renderStar = true;
    }
    public void onTick(EntityDust e)
    {
        int f = e.data[0] & 7;
        int c = (e.data[0] >> 3) & 7;
        e.renderStar = true;

        if (f != 1 && e.ticksExisted < f * 30)
        {
            e.setColorStarInner(140, 140, 140);
            e.setColorStarOuter(140, 140, 140);
            return;
        }

        e.setColorStarInner(0, 0, 255);
        e.setColorStarOuter(0, 0, 255);
        List<Entity> entities = getEntities(e);

        if (entities.size() > 0 || f > 1)
        {
            trigger(e, c);
            e.fade();
        }
    }

    public void trigger(EntityDust e, int level)
    {
        e.worldObj.createExplosion(e, e.posX, e.posY - EntityDust.yOffset, e.posZ, (float)(level * level)/10000 + 2F);
    }
}
