/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dustmod.runes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dustmod.EntityDust;
import dustmod.PoweredEvent;
import dustmod.TileEntityDust;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;

/**
 *
 * @author billythegoat101
 */
public class DEPowerRelay extends PoweredEvent
{
    public static int distance = 32;

    public DEPowerRelay()
    {
        super();
        networks = new HashMap<EntityDust, List<EntityDust>>();
    }

    @Override
    public void onInit(EntityDust e)
    {
        super.onInit(e);
        ItemStack[] req = new ItemStack[] {new ItemStack(Item.monsterPlacer, 1, 120 /*testificate*/)};
        req = this.sacrifice(e, req);

        if (!checkSacrifice(req) || !takeXP(e, 15))
        {
            e.fizzle();
            return;
        }

        networks.put(e, new ArrayList<EntityDust>());
    }

    @Override
    public void onTick(EntityDust e)
    {
        super.onTick(e);
//        System.out.println("POWAH RELAY: " + e.getFuel());
        double powerPercent = (double)e.getFuel() / (double)this.getStableFuelAmount(e);
        int c = (int)(255D * powerPercent);

        if (c > 255)
        {
            c = 255;
        }

        e.setColorStar(255, c, c);

        if (e.ticksExisted % 10 == 0)
        {
            disperseFuel(e);
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
        networks.remove(e);
        List<EntityDust> ents = this.findDustEntities(e);

        if (ents == null)
        {
            return;
        }

        List<EntityDust> powEnts = new ArrayList<EntityDust>();
        int needPower = 0;
        int totalPowerRequest = 0;

        for (EntityDust i: ents)
        {
//            if(i.fueledExternally){
            i.fueledExternally = false;
//            }
        }
    }

    @Override
    public void subtractFuel(EntityDust e)
    {
//        super.subtractFuel(e);
    }

    @Override
    public void addFuel(EntityDust e, int amt)
    {
        super.addFuel(e, amt);
        disperseFuel(e);
    }

    public void disperseFuel(EntityDust e)
    {
        List<EntityDust> ents = this.findDustEntities(e);
        int needPower = 0;
        int totalPowerRequest = 0;
        e.data[0] = 0;

        for (EntityDust i: ents)
        {
            if (i.requiresFuel && i.event != this)
            {
                needPower++;
                int want = ((PoweredEvent)i.event).powerWanted(i);
                totalPowerRequest += want;
//                e.data[0] += ((PoweredEvent)i.event).getStableFuelAmount(i);
                registerSelfTo(e, i);
            }
            else if (i.event == this && i != e)
            {
//                if(i.getFuel() > e.getFuel()){
                int split = (i.getFuel() + e.getFuel()) / 2;
                i.setFuel(split);
                e.setFuel(split);
//                }
            }
        }

        checkNetwork(e);
        List<EntityDust> network = networks.get(e);

        if (network == null)
        {
            network = new ArrayList<EntityDust>();
            networks.put(e, network);
        }

        EntityDust[] arr = new EntityDust[network.size()];
        network.toArray(arr);
//        System.out.println("Amount need power: " + needPower + " " + ents.size());
        boolean hasEnough = e.getFuel() >= totalPowerRequest;
        int fuel = (hasEnough) ? totalPowerRequest : e.getFuel();

        for (EntityDust i: arr)
        {
            if (i.isDead || e.getDistanceSqToEntity(i) > distance * distance)
            {
                removeSelfFrom(e, i);
                i.fueledExternally = false;
                continue;
            }

//            System.out.println("DERP " + i.event.getClass());
            i.fueledExternally = true;
            PoweredEvent event = (PoweredEvent)i.event;
            e.data[0] += event.getStableFuelAmount(i);
            int want = event.powerWanted(i);

            if (want > 50)
            {
                want = 50;
            }

            if (hasEnough)
            {
                int prev = i.getFuel();
                i.setFuel(i.getFuel() + want);
                e.setFuel(e.getFuel() - want);
//                System.out.println("Giving fuel " + i.getFuel() + " " + prev + " " + want + " " + e.getFuel());
            }
            else
            {
//                System.out.println("Giving fuel");
                double percent = (double)want / (double)totalPowerRequest;
                int amt = (int)(percent * (double)fuel);
                i.setFuel(i.getFuel() + amt);
                e.setFuel(e.getFuel() - amt);
            }
        }
    }

    public static List<EntityDust> findDustEntities(EntityDust e)
    {
        int x = e.getX();
        int y = e.getY();
        int z = e.getZ();
        int radius = 32;
        List<EntityDust> rtn =
                e.worldObj.getEntitiesWithinAABB(e.getClass(),
                        AxisAlignedBB.getBoundingBox(
                                x, y, z, x + 1.0D, y + 1.0D, z + 1.0D)
                        .expand(radius, radius, radius));
        return rtn;
    }

    @Override
    public int getStartFuel()
    {
        return 0;
    }

    @Override
    public int getMaxFuel()
    {
        return Integer.MAX_VALUE;
    }

    @Override
    public int getStableFuelAmount(EntityDust e)
    {
        return e.data[0];
    }

    @Override
    public boolean isPaused(EntityDust e)
    {
        return true;
    }

    public void registerSelfTo(EntityDust parent, EntityDust node)
    {
        checkNetwork(parent);
        List<EntityDust> network = networks.get(parent);

        if (!network.contains(node))
        {
            network.add(node);
        }
    }
    public void removeSelfFrom(EntityDust parent, EntityDust node)
    {
        checkNetwork(parent);
        List<EntityDust> network = networks.get(parent);

        if (network.contains(node))
        {
            network.remove(node);
        }
    }
    private void checkNetwork(EntityDust e)
    {
        if (!networks.containsKey(e))
        {
            networks.put(e, new ArrayList<EntityDust>());
        }
    }

    public HashMap<EntityDust, List<EntityDust>> networks;
}
