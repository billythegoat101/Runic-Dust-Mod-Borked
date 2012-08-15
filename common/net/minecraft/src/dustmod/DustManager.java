/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minecraft.src.dustmod;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.registry.LanguageRegistry;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ModLoader;
import net.minecraft.src.World;
import net.minecraftforge.common.Configuration;

/**
 *
 * @author billythegoat101
 */
public class DustManager
{
	
    public static HashMap<String, DustEvent> events = new HashMap<String, DustEvent>();
    public static List<String> names = new ArrayList<String>();

    public static ArrayList<DustShape> shapes = new ArrayList<DustShape>();
    public static Configuration config;
    public DustManager()
    {
    }



    public static List<String> getNames(){
        return names;
    }
    
    public static ArrayList<DustShape> getShapes(){
        return shapes;
    }
    public static HashMap<String, DustEvent> getEvents(){
        return events;
    }
    
    public static EntityDust initiate(DustShape shape, String name, double x, double y, double z, World world, List<Integer[]> points, int[][] map, String username,int rot)
    {
        DustEvent evt = events.get(name);

        if (evt == null)
        {
            return null;
        }

        EntityDust result = new EntityDust(world);
        result.entityDustID = DustMod.proxy.getNextDustEntityID();
        DustMod.proxy.registerEntityDust(result, result.entityDustID);
        result.setPosition(x, y - 0.8, z);
//        result.posX = x;
//        result.posY = y-0.8;//EntityDust.yOffset;
//        result.posZ = z;
        result.dustPoints = points;
        result.dusts = map;
        result.rot = rot;
        result.summonerUN = (username == null) ? "" : username;

        for (Integer[] pos: points)
        {
            TileEntityDust ted = (TileEntityDust)world.getBlockTileEntity(pos[0], pos[1], pos[2]);
            ted.setEntityDust(result);
        }

        if (shape.solid)
        {
            boolean found = false;
            result.dustID = -1;

            for (int i = 0; i < map.length && !found; i++)
            {
                for (int j = 0; j < map[0].length && !found; j++)
                {
                    int iter = map[i][j];

                    if (iter != 0)
                    {
                        result.dustID = iter;
                        found = true;
                    }
                }
            }
        }

        result.setEvent(evt, name);
        world.spawnEntityInWorld(result);
        
        
        if ((evt.allowed || ModLoader.getMinecraftServerInstance().getConfigurationManager().areCommandsAllowed(name)) && evt.permaAllowed)
        {
            evt.init(result);
            result.updateDataWatcher();
        }
        else
        {
            EntityPlayer player = world.getPlayerEntityByName(username);
            result.reanimate = true;

            if (player != null)
            {
                player.addChatMessage("This rune is disabled on this server.");
            }
        }

        result.dusts = null; //clearing so that i dont forget that it wont be saved and then try to access it in onTick()
        return result;
    }

    public static DustEvent get(String name)
    {
        return events.get(name);
    }

    public static void add(String name, DustEvent evt)
    {
        events.put(name, evt);
        names.add(name);
        evt.name = name;
//        System.out.println("Added event " + name +". Total:" + names.size() );
    }

    public static DustShape getShape(int ind)
    {
        return shapes.get(ind);
    }
    public static DustShape getShapeFromID(int id)
    {
        for (DustShape i: shapes)
        {
            if (i.id == id)
            {
                return i;
            }
        }

        return null;
    }


    /**
     * Called upon joining a server to clear any events that were synced and 
     * registered from the last server.
     * 
     */
    public static void resetMultiplayerRunes(){
    	System.out.println("Reset multiplayer runes");
        events = new HashMap<String, DustEvent>();
        names = new ArrayList<String>();
        shapes = new ArrayList<DustShape>();
        DustMod.proxy.resetPlayerTomePage();
    }
    
    
    /**
     * Register a new DustShape into the local system. The lexicon image will be 
     * generated automatically if missing.
     * 
     * @param shape The DustShape object that stores all the shape information
     * @param eventInstance An instance of the event to call when the rune shape is made.
     */
    public static void registerLocalDustShape(DustShape shape, DustEvent eventInstance){
//    	System.out.println("Registering local rune");
        add(shape.name, eventInstance);
        shapes.add(shape);
        DustMod.proxy.checkPage(shape);
//        PageHelper.instance.checkImage(shape);
        if(eventInstance != null && eventInstance instanceof PoweredEvent){
            shape.isPower = true;
        }
//        ModLoader.addLocalization("tile.scroll" + shape.name + ".name", shape.getRuneName() + " Placing Scroll");
//        System.out.println("Registering new DustShape " + shape.name + " " + mod_DustMod.isSessionMultiplayer());
        
        
        if(config == null){
            config = new Configuration(new File("./DustModConfig.cfg"));
            config.load();
        }
            if (!eventInstance.secret)
            {
                eventInstance.allowed = Boolean.parseBoolean(config.getOrCreateBooleanProperty("Allow_" + shape.getRuneName().replace(' ', '_'), Configuration.CATEGORY_GENERAL, eventInstance.allowed).value);
            }

        config.save();
        
        LanguageRegistry.instance().addStringLocalization("tile.scroll" + shape.name + ".name", "en_US", shape.getRuneName() + " Placing Scroll");
    }
    

    
    /**
     * Registers a new DustShape into the local system reserved for SMP. This is 
     * called by the packet from the server so that the available events are synced.
     * 
     * @param shape the DustShape to register.
     */
    public static void registerRemoteDustShape(DustShape shape){
        String name = shape.name;
        shapes.add(shape);
        names.add(shape.name);
        DustMod.proxy.checkPage(shape);
//        System.out.println("Registering temporary remote DustShape " + shape.name);
        LanguageRegistry.instance().addStringLocalization("tile.scroll" + shape.name + ".name", "en_US", shape.getRuneName() + " Placing Scroll");
    }
    
    
    
}