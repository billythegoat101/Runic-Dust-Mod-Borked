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
import net.minecraft.src.dustmod.runes.DEBait;
import net.minecraft.src.dustmod.runes.DEBomb;
import net.minecraft.src.dustmod.runes.DEBounce;
import net.minecraft.src.dustmod.runes.DECage;
import net.minecraft.src.dustmod.runes.DECampFire;
import net.minecraft.src.dustmod.runes.DECompression;
import net.minecraft.src.dustmod.runes.DEDawn;
import net.minecraft.src.dustmod.runes.DEEarthSprite;
import net.minecraft.src.dustmod.runes.DEEggifier;
import net.minecraft.src.dustmod.runes.DEFarm;
import net.minecraft.src.dustmod.runes.DEFireBowEnch;
import net.minecraft.src.dustmod.runes.DEFireRain;
import net.minecraft.src.dustmod.runes.DEFireSprite;
import net.minecraft.src.dustmod.runes.DEFireTrap;
import net.minecraft.src.dustmod.runes.DEFlatten;
import net.minecraft.src.dustmod.runes.DEFog;
import net.minecraft.src.dustmod.runes.DEForcefield;
import net.minecraft.src.dustmod.runes.DEFortuneEnch;
import net.minecraft.src.dustmod.runes.DEHeal;
import net.minecraft.src.dustmod.runes.DEHideout;
import net.minecraft.src.dustmod.runes.DEHunterVision;
import net.minecraft.src.dustmod.runes.DELiftTerrain;
import net.minecraft.src.dustmod.runes.DELightning;
import net.minecraft.src.dustmod.runes.DELillyBridge;
import net.minecraft.src.dustmod.runes.DELoyaltySprite;
import net.minecraft.src.dustmod.runes.DELumberjack;
import net.minecraft.src.dustmod.runes.DELunar;
import net.minecraft.src.dustmod.runes.DEMiniTele;
import net.minecraft.src.dustmod.runes.DEObelisk;
import net.minecraft.src.dustmod.runes.DEPit;
import net.minecraft.src.dustmod.runes.DEPoisonTrap;
import net.minecraft.src.dustmod.runes.DEPowerRelay;
import net.minecraft.src.dustmod.runes.DEResurrection;
import net.minecraft.src.dustmod.runes.DESilkTouchEnch;
import net.minecraft.src.dustmod.runes.DESpawnRecord;
import net.minecraft.src.dustmod.runes.DESpawnTorch;
import net.minecraft.src.dustmod.runes.DESpawnerCollector;
import net.minecraft.src.dustmod.runes.DESpawnerReprog;
import net.minecraft.src.dustmod.runes.DESpeed;
import net.minecraft.src.dustmod.runes.DESpiritTool;
import net.minecraft.src.dustmod.runes.DETeleportation;
import net.minecraft.src.dustmod.runes.DETimeLock;
import net.minecraft.src.dustmod.runes.DEVoid;
import net.minecraft.src.dustmod.runes.DEWall;
import net.minecraft.src.dustmod.runes.DEXP;
import net.minecraft.src.dustmod.runes.DEXPStore;
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
        result.entityDustID = EntityDustManager.getNextDustEntityID();
        EntityDustManager.registerEntityDust(result, result.entityDustID);
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
    	for(DustShape i:shapes){
    		if(i.id == shape.id){
    			throw new IllegalArgumentException("[DustMod] Rune ID [" + shape.id + "] already occupied. " + i + " and " + shape);
    		}
    	}
    	
        add(shape.name, eventInstance);
        shapes.add(shape);
        DustMod.proxy.checkPage(shape);
        if(eventInstance != null && eventInstance instanceof PoweredEvent){
            shape.isPower = true;
        }
        
        
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
	 * Called when a rune is activated. This checks to see if the rune shape is
	 * valid and then calls DustManager.initiate
	 * 
	 * @param i
	 *            The center x position of the rune
	 * @param j
	 *            The center y position of the rune
	 * @param k
	 *            The center Z position of the rune
	 * @param map
	 *            The untrimmed(leading and trailing empty space included) map
	 *            containing the full rune shape created when combining all the
	 *            shapes of adjacent TileEntityDusts
	 * @param points
	 *            The list of all BlockDust blocks that contributed to the rune.
	 * @param username
	 *            The username of the player who called the rune. Null if called
	 *            by redstone.
	 */
	public static void callShape(World world, double i, double j, double k,
			int[][] map, List<Integer[]> points, String username) {
		DustShape found = null;
		// trim shape
		ArrayList<ArrayList<Integer>> temp = new ArrayList<ArrayList<Integer>>();
		int sx = map.length;
		int sz = map[0].length;
		int mx = 0;
		int mz = 0;
		boolean fnd = false;

		for (int x = 0; x < map.length; x++) {
			for (int z = 0; z < map[0].length; z++) {
				if ((x < sx || z < sz) && map[x][z] != 0) {
					if (x < sx) {
						sx = x;
					}

					if (z < sz) {
						sz = z;
					}

					fnd = true;
				}

				if (map[x][z] != 0) {
					if (x > mx) {
						mx = x;
					}

					if (z > mz) {
						mz = z;
					}
				}
			}
		}

		int dx = Math.abs(mx - sx) + 1;
		int dz = Math.abs(mz - sz) + 1;

		if (dx < 4) {
			sx = 0;
			mx = 3;
			dx = 4;
		}

		if (dz < 4) {
			sz = 0;
			mz = 3;
			dz = 4;
		}

		int[][] trim = new int[dx][dz];

		for (int x = sx; x <= mx; x++) {
			for (int z = sz; z <= mz; z++) {
				trim[x - sx][z - sz] = 0;
				trim[x - sx][z - sz] = map[x][z];
			}
		}

		for (int[] a : trim) {
			for (int b : a) {
				if (b == -2) {

					for (Integer[] p : points) {
						int id = world.getBlockId(p[0], p[1], p[2]);

						if (id == DustMod.dust.blockID) {
							world.setBlockMetadataWithNotify(p[0], p[1], p[2],
									2);
						}
					}

					System.out.println("derp left variable dust.");
					return;
				}
			}
		}
		int rot = 0;

		for (int iter = 0; iter < DustManager.getShapes().size(); iter++) {
			DustShape s = DustManager.getShapes().get(iter);
			// System.out.println("Dicks");
			int[][] temptrim = trim;

			if ((rot = s.compareData(trim)) != -1) {
				// trim = temptrim;
				found = s;
				break;
			}
		}

		if (found != null) {
			System.out.println("Found: " + found.name);
			DustManager.initiate(found, found.name, i, j, k, world, points,
					trim, username, rot);
		} else {

			for (Integer[] p : points) {
				int id = world.getBlockId(p[0], p[1], p[2]);

				if (id == DustMod.dust.blockID) {
					world.setBlockMetadataWithNotify(p[0], p[1], p[2], 2);
				}
			}

			System.out.println("nothing found.");
		}
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
    
    
    

	public static void registerDefaultShapes() {

//		System.out.println("Registering Shapes");
        int N = -1;
        int P = 1;
        int G = 2;
        int L = 3;
        int B = 4;
        DustShape s;
        int[][][] values;
        //test shape
//        s = new DustShape(2,2,1, "testshape");
        //DustManager.registerLocalDustShape(s, new DETestEvent());
//        s.setDataAt(0, 0, 0, 1);
//        s.setDataAt(0, 0, 1, 1);
//        s.setDataAt(1, 0, 1, 1);
//        s.setDataAt(1, 0, 0, 1);
        //DustManager.shapes.add(s);
        //<editor-fold defaultstate="collapsed" desc="torch">
        s = new DustShape(4, 4, "torch2", false, 0, 0, 0, 0, 0);
        values = new int[][][]
        {
            {
                {0, 0, 0, 0},
                {0, N, N, 0},
                {0, N, N, 0},
                {0, 0, 0, 0}
            }
        };
        s.setData(values);
        s.setRuneName("Torch Rune");
        s.setNotes("Sacrifice:\n\n"
                + "-None: Normal torch spawn.\n"
                + "-1xFlint: Beacon rune.\n"
                + "\nNotes:\n\n"
                + "=Sacrificing a dye to an existing beacon will change its color.");
        s.setDesc("Description:\n\n"
                + "Spawns a torch or, if a piece of flint is sacrficed, a beacon.");
        s.setAuthor("billythegoat101");
        s.setManualRotationDerp(new int[] {00, 00, 0, 0, 0, 0, 0, 0});
        DustManager.registerLocalDustShape(s, new DESpawnTorch());
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="rabbit">
        s = new DustShape(4, 4, "rabbit", true, 0, 0, 0, 0, 44);
        values = new int[][][]
        {
            {
                {0, 0, N, N},
                {0, 0, 0, N},
                {N, 0, 0, 0},
                {N, N, 0, 0}
            }
        };
        s.setData(values);
        s.setRuneName("Rune of the Rabbit Hole");
        s.setNotes("Sacrifice:\n\n"
                + "-2xHunger.\n\n"
                + "Notes:\n\n"
                + "-Variable determines the volume of the den.\n"
                + "-Stepping on the rune will send you down.\n"
                + "-Standing directly below and crouching will send you back up.");
        s.setDesc("Description:\n\n"
                + "Creates a small hole beneath the rune and allows you to jump "
                + "inside for safety. Walking over the top will send you down to "
                + "the next solid block below the rune. Pressing [crouch] while "
                + "directly beneath the rune will bring you back up.");
        s.setAuthor("billythegoat101");
        s.setManualRotationDerp(new int[] {00, 00, 0, 0, 0, 0, 0, 0});
        DustManager.registerLocalDustShape(s, new DEHideout());
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="heal">
        s = new DustShape(4, 4, "heal", true, 0, 0, 0, 0, 1);
        values = new int[][][]
        {
            {
                {0, N, N, 0},
                {N, N, N, N},
                {N, N, N, N},
                {0, N, N, 0},
            }
        };
        s.setData(values);
        s.setRuneName("Rune of Healing");
        s.setNotes("Sacrifice:\n\n"
                + "-2xCoal + 2XP");
        s.setDesc("Description:\n\n"
                + "Heals any nearby entities' hearts with regeneration.");
        s.setAuthor("billythegoat101");
        s.setManualRotationDerp(new int[] {0, 0, 0, 0, 0, 0, 0, 0});
        DustManager.registerLocalDustShape(s, new DEHeal());
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="lumberjack">
        s = new DustShape(8, 8, "lumberjack", true, 2, 2, 0, 0, 4);
        values = new int[][][]
        {
            {
                {0, 0, N, 0, 0, N, 0, 0},
                {0, 0, N, N, N, N, 0, 0},

                {N, N, 0, 0, 0, 0, N, N},
                {0, N, 0, 0, 0, 0, N, 0},
                {0, N, 0, 0, 0, 0, N, 0},
                {N, N, 0, 0, 0, 0, N, N},

                {0, 0, N, N, N, N, 0, 0},
                {0, 0, N, 0, 0, N, 0, 0}
            }
        };
        s.setData(values);
        s.setRuneName("Rune of Lumber");
        s.setNotes("Sacrifice:\n\n"
                + "-3xLog + 2xStick");
        s.setDesc("Description:\n\n"
                + "Chops down all trees within an area. Has a chance to drop more than 1 log and some sticks.\n"
                + "The area of effect and the chances of doubling increase with dust value. "
                + "Also destroys leaves with a small chance of dropping plant runic dust.");
        s.setAuthor("billythegoat101");
        s.setManualRotationDerp(new int[] { -2, -1, -1, 0, 0, -1, -1, -2});
        DustManager.registerLocalDustShape(s, new DELumberjack());
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="campfire">
        s = new DustShape(8, 8, "campfire6", true, 2, 2, 2, 2,  40);
        values = new int[][][]
        {
            {
                {0, P, 0, P, P, 0, P, 0},
                {P, 0, 0, P, P, 0, 0, P},

                {0, 0, 0, 0, 0, 0, 0, 0},
                {P, P, 0, 0, 0, 0, P, P},
                {P, P, 0, 0, 0, 0, P, P},
                {0, 0, 0, 0, 0, 0, 0, 0},

                {P, 0, 0, P, P, 0, 0, P},
                {0, P, 0, P, P, 0, P, 0}
            }
        };
        s.setData(values);
        s.setRuneName("Rune of Fire");
        s.setManualRotationDerp(new int[] {0, 0, 0, 0, 0, 0, 0, 0});
        s.setAuthor("billythegoat101");
        s.setNotes("Sacrifice:\n\n"
                + "-8xLog + 1xZombieFlesh\n\n"
                + "Notes:\n\n"
                + "-Lasts a fourth of a day unless fueled.\n");
        s.setDesc("Description:\n\n"
                + "Creates a flame that allows you to smelt items instantly. There is a small chance of getting double of what you throw in. Items must be thrown in 1 at a time or else they will burn.");
        DustManager.registerLocalDustShape(s, new DECampFire());
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="pit">
        s = new DustShape(8, 8, "pit", true, 2, 2, 2, 2, 2);
        values = new int[][][]
        {
            {
                {0, 0, N, 0, 0, N, 0, 0},
                {0, N, N, 0, 0, N, N, 0},

                {N, N, 0, 0, 0, 0, N, N},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {N, N, 0, 0, 0, 0, N, N},

                {0, N, N, 0, 0, N, N, 0},
                {0, 0, N, 0, 0, N, 0, 0}
            }
        };
        s.setData(values);
        s.setRuneName("Rune of Depths");
        s.setAuthor("billythegoat101");
        s.setNotes("Sacrifice:\n\n"
                + "-2xLog at Plant & Gunpowder levels\n"
                + "-2xIronIngot at Lapis & Blaze levels.\n\n"
                + "Notes:\n\n"
                + "-Requires hole in the center.");
        s.setDesc("Description:\n\n"
                + "Digs a pit down into the earth. Requires a hole at the center of the rune (1 block down).");
        DustManager.registerLocalDustShape(s, new DEPit());
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="obelisk">
        s = new DustShape(8, 8, "obelisk", true, 2, 2, 2, 2, 15);
        values = new int[][][]
        {
            {
                {0, 0, 0, P, P, 0, 0, 0},
                {0, P, P, P, P, P, P, 0},

                {0, P, 0, 0, 0, 0, P, 0},
                {P, P, 0, 0, 0, 0, P, P},
                {P, P, 0, 0, 0, 0, P, P},
                {0, P, 0, 0, 0, 0, P, 0},

                {0, P, P, P, P, P, P, 0},
                {0, 0, 0, P, P, 0, 0, 0}
            }
        };
        s.setData(values);
        s.setRuneName("Rune of Heights");
        s.setNotes("Sacrifice:\n\n"
                + "-2xIronOre\n\n"
                + "Notes:\n\n"
                + "-The obelisk will stay standing for one day waiting for you.\n"
                + "-Destroying the top block will cause it to go back down.\n"
                + "-If you do not destroy the top block within a day, it will simply remain standing.");
        s.setDesc("Description:\n\n"
                + "Creates a ride-able pillar to the sky. When it reaches the top, you can destroy the top block to send it back down.");
        s.setAuthor("billythegoat101");
        s.setManualRotationDerp(new int[] {0, 0, 0, 0, 0, 0, 0, 0});
        DustManager.registerLocalDustShape(s, new DEObelisk());
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="farm">
        s = new DustShape(8, 6, "farm", false, 2, 3, 2, 1, 3);
        values = new int[][][]
        {
            {
                {0,P, P,0,0,P, P,0},

                {P,0, P,N,N,P, 0,P},
                {P,0, P,N,N,P, 0,P},
                {P,0, P,N,N,P, 0,P},
                {P,0, P,N,N,P, 0,P},

                {0,P, P,0,0,P, P,0}
            }
        };
        s.setData(values);
        s.setRuneName("Rune of the Farm");
        s.setAuthor("billythegoat101");
        s.setManualRotationDerp(new int[] {0, 0, -1, 0, 0, 0, -1, 0});
        s.setNotes("Sacrifice:\n\n"
                + "-8xIronIngot + 4XP");
        s.setDesc("Description:\n\n"
                + "Instantly spawns a farm.");
        DustManager.registerLocalDustShape(s, new DEFarm());
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="lilypad">
        s = new DustShape(6, 9, "lilypad", false, 3, 0, 1, 4, 7);
        values = new int[][][]
        {
            {
                {0, 0,P,G,0, 0},
                {0, 0,P,G,0, 0},
                {0, P,P,G,G, 0},
                {0, P,P,G,G, 0},

                {P, P,0,0,G, G},
                {P, P,P,G,G, G},
                {P, P,G,P,G, G},
                {P, P,P,G,G, G},

                {0, 0,P,G,0, 0}
            }
        };
        s.setData(values);
        s.setRuneName("Rune of the Leaping Frog");
        s.setNotes("Sacrifice:\n\n"
                + "-4xLeaves");
        s.setDesc("Description:\n\n"
                + "Spawns a bridge of lily pads over a body of water in front of it.");
        s.setAuthor("billythegoat101");
        s.setManualRotationDerp(new int[] {0, 0, 0, 0, 0, 0, 0, -1});
        DustManager.registerLocalDustShape(s, new DELillyBridge());
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="dawn">
        s = new DustShape(8, 8, "dawn", true, 2, 2, 2, 2, 8);
        values = new int[][][]
        {
            {
                {0, 0, G, 0, 0, G, 0, 0},
                {0, 0, 0, G, G, 0, 0, 0},

                {G, 0, G, 0, 0, G, 0, G},
                {0, G, 0, G, G, 0, G, 0},
                {0, G, 0, G, G, 0, G, 0},
                {G, 0, G, 0, 0, G, 0, G},

                {0, 0, 0, G, G, 0, 0, 0},
                {0, 0, G, 0, 0, G, 0, 0}
            }
        };
        s.setRuneName("Rune of Dawn");
        s.setData(values);
        s.setNotes("Sacrifice:\n\n"
                + "-4xRedstoneDust + 1LapisLazuli\n\n"
                + "Notes:\n"
                + "-If it is already day, it will last through to the next night night to do anything.");
        s.setDesc("Description:\n\n"
                + "Turns night into day. If it is already day, it will wait until the next night to activate.");
        s.setAuthor("billythegoat101");
        s.setManualRotationDerp(new int[] {0, 0, 0, 0, 0, 0, 0, 0});
        DustManager.registerLocalDustShape(s, new DEDawn());
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="lunar">
        s = new DustShape(4, 4, "lunar", true, 0, 0, 0, 0, 9);
        values = new int[][][]
        {
            {
                {G, G, G, G},
                {G, 0, 0, G},
                {G, 0, 0, 0},
                {G, G, 0, G}
            }
        };
        s.setRuneName("Rune of Dusk");
        s.setData(values);
        s.setNotes("Sacrifice:\n\n"
                + "-4xNetherwart + 1xLapisLazuli");
        s.setDesc("Description:\n\n"
                + "Turns day into night. If it is already night, it will wait until the next day to activate.");
        s.setAuthor("billythegoat101");
        s.setManualRotationDerp(new int[] {0, 0, 0, 0, 0, 0, 0, 0});
        DustManager.registerLocalDustShape(s, new DELunar());
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="firetrap">
        s = new DustShape(4, 4, "firetrap", true, 0, 0, 0, 0, 10);
        values = new int[][][]
        {
            {
                {0, 0, 0, N},
                {N, 0, N, N},
                {N, N, 0, 0},
                {N, N, N, 0}
            }
        };
        s.setData(values);
        s.setRuneName("Fire Trap Rune");
        s.setNotes("Sacrifice:\n\n"
                + "-3xFlint\n\n"
                + "Notes:\n\n"
                + "-Dust must be gunpowder or better.");
        s.setDesc("Description:\n\n"
                + "Sets entities and landscape on fire when an entity comes near.");
        s.setAuthor("billythegoat101");
        s.setManualRotationDerp(new int[] {0, 0, 0, 0, 0, 0, 0, 0});
        DustManager.registerLocalDustShape(s, new DEFireTrap());
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="lightning">
        s = new DustShape(4, 4, "lightning", true, 0, 0, 0, 0, 12);
        values = new int[][][]
        {
            {
                {0, N, N, N},
                {0, N, 0, 0},
                {0, 0, N, 0},
                {N, N, N, 0}
            }
        };
        s.setData(values);
        s.setRuneName("Lightning Trap Rune");
        s.setNotes("Sacrifice:\n\n"
                + "-3xIronIngot\n\n"
                + "Notes:\n\n"
                + "-Dust must be gunpowder or better.");
        s.setDesc("Description:\n\n"
                + "Zaps entities with lightning when an entity comes near.");
        s.setAuthor("billythegoat101");
        s.setManualRotationDerp(new int[] {0, 0, 0, 0, 0, 0, 0, 0});
        DustManager.registerLocalDustShape(s, new DELightning());
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="poison">
        s = new DustShape(6, 6, "poison", true, 3, 3, 1, 1, 11);
        values = new int[][][]
        {
            {
                {0, 0, N, 0, N, 0},

                {0, N, N, N, N, N},
                {N, N, N, 0, N, 0},
                {N, 0, N, N, N, N},
                {0, N, 0, N, N, 0},

                {0, 0, N, N, 0, 0}
            }
        };
        s.setData(values);
        s.setRuneName("Poison Trap Rune");
        s.setNotes("Sacrifice:\n\n"
                + "-3xSpiderEye\n\n"
                + "Notes:\n\n"
                + "-Dust must be gunpowder or better.");
        s.setDesc("Description:\n\n"
                + "Poisons entities when an entity comes near.");
        s.setAuthor("billythegoat101");
        s.setManualRotationDerp(new int[] {0, 0, 0, 0, 0, 0, 0, 0});
        DustManager.registerLocalDustShape(s, new DEPoisonTrap());
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="bomb">
        s = new DustShape(5, 6, "bomb", false, 0, 2, 0, 2, 13);
        values = new int[][][]
        {
            {
                {0, 0, 0, 0, N},
                {0, 0, 0, N, N},

                {G, G, G, N, 0},
                {G, N, N, G, 0},
                {G, N, N, G, 0},
                {G, G, G, G, 0}
            }
        };
        s.setData(values);
        s.setRuneName("Rune of Detonation");
        s.setNotes("Sacrifice:\n\n"
                + "-5xGunpowder\n"
                + "OR\n"
                + "-15xGunpowder Runic Dust\n\n"
                + "Notes:\n\n"
                + "-Center determines strength.\n"
                + "-Fuse determines triggering.\n"
                + "-If fuse is made out of plant runic dust, it will wait for a mob to trigger.\n"
                + "-Otherwise, the time until deonation will depend on the dust strength.");
        s.setDesc("Description:\n\n"
                + "Creates a variable-sized explosion when triggered by an entity or by time. "
                + "Center determines the strength, fuse deteremines the triggering. "
                + "If fuse is made out of plant runic dust, it will wait for a mob to trigger. "
                + "Otherwise, the time until deonation will depend on the dust strength.");
        s.setAuthor("billythegoat101");
        s.setManualRotationDerp(new int[] {0, 0, 0, 0, 0, -1, 0, -1});
        DustManager.registerLocalDustShape(s, new DEBomb());
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="cage">
        s = new DustShape(6, 6, "cage", false, 3, 3, 1, 1, 18);
        values = new int[][][]
        {
            {
                {0, G, G, G, G, 0},

                {G, G, 0, 0, G, G},
                {G, L, L, L, L, G},
                {G, L, L, L, L, G},
                {G, G, 0, 0, G, G},

                {0, G, G, G, G, 0}
            }
        };
        s.setData(values);
        s.setRuneName("Rune of Entrapment");
        s.setNotes("Sacrifice:\n\n"
                + "-6xIron + 8xLapisLazuli");
        s.setDesc("Description:\n\n"
                + "Entraps a single mob that walks nearby. Will not entrap the summoner.");
        s.setAuthor("billythegoat101");
        s.setManualRotationDerp(new int[] {0, 0, 0, 0, 0, 0, 0, 0});
        DustManager.registerLocalDustShape(s, new DECage());
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="timelock">
        s = new DustShape(6, 6, "timelock6", false, 3, 2, 1, 2, 19);
        values = new int[][][]
        {
            {
                {0, G, G, G, G, 0},
                {0, G, 0, 0, G, 0},

                {G, G, G, G, G, G},
                {G, G, L, L, G, G},
                {G, G, L, L, G, G},
                {G, G, G, G, G, G}
            }
        };
        s.setData(values);
        s.setRuneName("Rune of Locked Time");
        s.setNotes("Sacrifice:\n\n"
                + "-8xObsidian + 4xSlime + 1xLapisLaz\n\n"
                + "Notes:\n\n"
                + "-Expect bugs.\n"
                + "-Lasts for a day's time unless fueled.");
        s.setDesc("Description:\n\n"
                + "Locks day/night time, sand falling, and water flowing for as long as it is fueled. BEWARE: Very high chance of bugs especially with other mods.");
        s.setAuthor("billythegoat101");
        s.setManualRotationDerp(new int[] {0, 0, 0, 0, 0, 0, 0, -1});
        DustManager.registerLocalDustShape(s, new DETimeLock());
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="void">
        s = new DustShape(8, 6, "void", true, 2, 3, 2, 1, 29);
        values = new int[][][]
        {
            {
                {0, L, 0, L, L, 0, L, 0},

                {L, L, L, 0, 0, L, L, L},
                {0, L, 0, L, L, 0, L, 0},
                {0, L, 0, L, L, 0, L, 0},
                {L, L, L, 0, 0, L, L, L},

                {0, L, 0, L, L, 0, L, 0}
            }
        };
        s.setData(values);
        s.setRuneName("Rune of the Void");
        s.setNotes("Sacrifice:\n\n"
                + "-3XP + Items to store\n\n"
                + "Notes:\n\n"
                + "-If there is a sacrifice, it will be stored.\n"
                + "-If there is not, all items in storage will be dropped.\n"
                + "-3XP will be taken either way.\n"
                + "-Inventories are separated by user. So you will not get yours mixed with someone elses.");
        s.setDesc("Description:\n\n"
                + "Stores sacrificed items in a void. When activated without a sacrifice, the items are returned.");
        s.setAuthor("billythegoat101");
        s.setManualRotationDerp(new int[] {0, 0, -1, 0, 0, 0, -1, 0});
        DustManager.registerLocalDustShape(s, new DEVoid());
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="wall">
        s = new DustShape(14, 6, "wall", false, 3, 3, 5, 1, 16);
        values = new int[][][]
        {
            {
                {0, 0, 0, 0, 0, 0, G, G, G, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, G, P, G, G, G, 0, 0, 0},
                {0, G, G, G, G, G, G, G, P, P, G, G, G, G},
                {P, P, P, P, G, G, P, P, P, P, P, P, P, 0},
                {0, 0, 0, P, P, P, G, P, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, P, P, P, 0, 0, 0, 0, 0, 0}
            }
        };
        s.setData(values);
        s.setRuneName("Rune of the Barrier");
        s.setNotes("Sacrifice:\n\n"
                + "-5xIronOre + 3XP");
        s.setDesc("Description:\n\n"
                + "Lifts a wall out of the earth.");
        s.setAuthor("billythegoat101");
        s.setManualRotationDerp(new int[] {0, 0, -1, 1, 0, 0, -1, 1 , 18});
        DustManager.registerLocalDustShape(s, new DEWall());
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="xpstore">
        s = new DustShape(6, 6, "xpstore", false, 3, 3, 1, 1, 37);
        values = new int[][][]
        {
            {
                {0, 0, 0, P, P, 0},

                {0, G, G, 0, P, P},
                {0, G, L, L, 0, P},
                {P, 0, L, L, G, 0},
                {P, P, 0, G, G, 0},

                {0, P, P, 0, 0, 0}
            }
        };
        s.setData(values);
        s.setRuneName("Rune of Wisdom");
        s.setNotes("Sacrifice:\n\n"
                + "-16xIronIngot + 6XP\n\n"
                + "Notes:\n\n"
                + "-Right-clicking will give you back your XP and pause the rune for a short time.\n"
                + "-While the rune is paused, it will glow yellow and not absorb any XP.\n"
                + "-Not accessible by other players.");
        s.setDesc("Description:\n\n"
                + "Stores all XP. When you walk over it it will take all your levels. It will also store any XP orbs dropped onto it. Not useable by other players.");
        s.setAuthor("billythegoat101");
        s.setManualRotationDerp(new int[] {0, 0, 0, 0, 0, 0, 0, 0});
        DustManager.registerLocalDustShape(s, new DEXPStore());
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="speed">
        s = new DustShape(5, 4, "speed", false, 0, 0, 0, 0, 6);
        values = new int[][][]
        {
            {
                {P, P, P, P, N},
                {0, L, L, L, N},
                {0, 0, P, P, N},
                {0, 0, 0, L, N}
            }
        };
        s.setData(values);
        s.setRuneName("Rune of Speed");
        s.setNotes("Sacrifice:\n\n"
                + "-4xSugar + 2xBlazePowder");
        s.setDesc("Description:\n\n"
                + "Gives you a variable speed boost.");
        s.setAuthor("billythegoat101");
        s.setManualRotationDerp(new int[] {0, 0, 0, 0, 0, -1, -1, 0});
        DustManager.registerLocalDustShape(s, new DESpeed());
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="compression">
        s = new DustShape(6, 6, "compression", false, 3, 3, 1, 1, 24);
        values = new int[][][]
        {
            {
                {0, G, B, G, B, 0},

                {G, G, G, B, B, B},
                {B, B, G, B, G, G},
                {G, G, B, G, B, B},
                {B, B, B, G, G, G},

                {0, B, G, B, G, 0}
            }
        };
        s.setData(values);
        s.setRuneName("Rune of Compression");
        s.setNotes("Sacrifice:\n\n"
                + "-NxCoal + 1xIronBlock\n\n"
                + "Notes:\n\n"
                + "-Every 32Coal will yield a diamond.");
        s.setDesc("Description:\n\n"
                + "Turns coal into diamond at a ratio of 32Coal=1Diamond.");
        s.setAuthor("billythegoat101");
        s.setManualRotationDerp(new int[] {0, 0, 0, 0, 0, 0, 0, 0});
        DustManager.registerLocalDustShape(s, new DECompression());
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="fog">
        s = new DustShape(8, 6, "fog", false, 2, 3, 2, 1, 41);
        values = new int[][][]
        {
            {
                {0, 0, G, G, G, G, G, 0},
                {0, L, G, L, L, L, G, G},
                {L, 0, G, G, L, L, G, G},
                {L, L, G, G, L, L, 0, G},
                {L, L, G, G, G, L, G, 0},
                {0, L, L, L, L, L, 0, 0}
            }
        };
        s.setData(values);
        s.setRuneName("Rune of the Blinding Fog");
        s.setNotes("Sacrifice:\n\n"
                + "-1xWaterBucket + 1xRedshroom + 6XP\n\n"
                + "Notes:\n\n"
                + "-Will last a day unless fueled.");
        s.setDesc("Description:\n\n"
                + "Creates a fog that blinds and confuses anyone inside.");
        s.setAuthor("billythegoat101");
        s.setManualRotationDerp(new int[] {0, 0, -1, 0, 0, 0, -1, 0});
        DustManager.registerLocalDustShape(s, new DEFog());
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="firerain">
        s = new DustShape(6, 7, "firerain6", false, 3, 3, 1, 1, 25);
        values = new int[][][]
        {
            {
                {B, G, G, G, G, B},
                {B, B, G, G, B, B},
                {0, G, B, B, G, 0},
                {B, G, B, B, G, B},
                {B, B, G, G, B, B},
                {0, B, G, G, B, 0},
                {0, G, G, G, G, 0}
            }
        };
        s.setData(values);
        s.setRuneName("Rune of the Hellstorm");
        s.setNotes("Sacrifice:\n\n"
                + "-4xBlazeRod\n\n"
                + "Notes:\n\n"
                + "-Will last half a day unless fueled.\n"
                + "-Will cause major lag.");
        s.setDesc("Description:\n\n"
                + "Summons a storm of ignited arrows for a duration of time. WARNING:Large chance of lag if left running. Break rune to stop.");
        s.setAuthor("billythegoat101");
        s.setManualRotationDerp(new int[] {0, 0, 0, -1, -1, 0, 0, 0});
        DustManager.registerLocalDustShape(s, new DEFireRain());
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="egg">
        s = new DustShape(6, 7, "egg", false, 3, 2, 5, 5,  45);
        values = new int[][][]
        {
            {
                {0, 0, P, P, 0, 0},
                {0, P, P, P, P, 0},

                {P, P, P, P, P, P},
                {P, P, G, G, P, P},
                {P, P, G, G, P, P},
                {G, G, G, G, G, G},

                {0, P, P, P, P, 0}
            }
        };
        s.setData(values);
        s.setRuneName("Rune Rebirth");
        s.setAuthor("billythegoat101");
        s.setManualRotationDerp(new int[] {0, 1, 1, 1, 1, 1, 1, 0});
        s.setNotes("Sacrifice:\n\n"
                + "-1xLiveEntity + 1xEgg + 1xDiamond + 10XP\n\n");
        s.setDesc("Description:\n\n"
                + "Drops an egg containing the mob sacrificed.");
        DustManager.registerLocalDustShape(s, new DEEggifier());
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="bait">
        s = new DustShape(6, 6, "bait6", false, 3, 3, 1, 1, 5);
        values = new int[][][]
        {
            {
                {P, P, P, 0, 0, 0},

                {P, P, P, P, P, 0},
                {P, P, L, L, P, 0},
                {0, P, L, L, P, P},
                {0, P, P, P, P, P},

                {0, 0, 0, P, P, P}
            }
        };
        s.setData(values);
        s.setRuneName("Rune of Baiting");
        s.setNotes("Sacrifice:\n\n"
                + "-1xMobEgg + 1xGoldBlock + 5XP\n\n"
                + "Notes:\n\n"
                + "-Will last seven days unless fueled.\n\n"
                + "Current bug: Does not detect entities who do not utilize the new AI system.");
        s.setDesc("Description:\n\n"
                + "Attracts any mobs with the specified drop type.");
        s.setAuthor("billythegoat101");
        s.setManualRotationDerp(new int[] {0, 0, 0, 0, 0, 0, 0, 0});
        DustManager.registerLocalDustShape(s, new DEBait());
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="resurrect">
        s = new DustShape(6, 6, "resurrect", true, 3, 3, 1, 1, 17);
        values = new int[][][]
        {
            {
                {L, 0, 0, 0, L, L},

                {L, 0, L, 0, 0, L},
                {L, L, L, L, 0, L},
                {L, 0, L, L, L, L},
                {L, 0, 0, L, 0, L},

                {L, L, 0, 0, 0, L}
            }
        };
        s.setData(values);
        s.setRuneName("Rune of Resurrection");
        s.setNotes("Sacrifice:\n\n"
                + "-1xGhastTear + 4xSoulSand +2xMobDrop");
        s.setDesc("Description:\n\n"
                + "Spawns a mob of the specified drop type.");
        s.setAuthor("billythegoat101");
        s.setManualRotationDerp(new int[] {0, 0, 0, 0, 0, 0, 0, 0});
        DustManager.registerLocalDustShape(s, new DEResurrection());
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="powerRelay">
        s = new DustShape(10, 10, "powerRelay", false, 1, 1, 5, 5,  42);
        values = new int[][][]
        {
            {
                {0, L, L, L, L, 0, 0, 0, 0, 0},
                {0, L, G, G, L, 0, G, G, 0, 0},
                {0, L, G, G, L, L, G, G, 0, 0},

                {0, L, L, 0, L, L, L, 0, 0, 0},
                {G, G, 0, 0, B, B, L, L, G, G},
                {G, G, L, L, B, B, 0, 0, G, G},
                {0, 0, 0, L, L, L, 0, L, L, 0},

                {0, 0, G, G, L, L, G, G, L, 0},
                {0, 0, G, G, 0, L, G, G, L, 0},
                {0, 0, 0, 0, 0, L, L, L, L, 0}
            }
        };
        s.setData(values);
        s.setRuneName("Rune of Power Distribution");
        s.setNotes("Sacrifice:\n\n"
                + "-1xTestificateEgg + 15XP\n\n"
                + "Notes:\n\n"
                + "-Does not exhaust any fuel itself. All fuel is redirected to runes within a " + DEPowerRelay.distance + " block radius.\n"
                + "-Fueled runes will have a bigger spark than normal.\n"
                + "-Sprites cannot be powered by powering the location at which their runes are summoned, but by the sprites themselves.");
        s.setDesc("Description:\n\n"
                + "Acts like a battery storing an infinite amount of fuel and distributing it to nearby runes who need it as they need it. Takes no fuel to sustain itself. Runes being powered will display a spark twice as big as normal.");
        s.setAuthor("billythegoat101");
        s.setManualRotationDerp(new int[] {0, 0, 0, 0, 0, 0, 0, 0});
        DustManager.registerLocalDustShape(s, new DEPowerRelay());
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="scollect">
        s = new DustShape(9, 6, "scollect", false, 2, 3, 0, 0, 35);
        values = new int[][][]
        {
            {
                {L, G, G, G, G, G, G, L},
                {L, G, 0, 0, 0, 0, G, L},
                {G, G, 0, 0, 0, 0, G, G},
                {L, L, 0, 0, 0, 0, L, L},
                {G, L, 0, 0, 0, 0, L, G},
                {G, L, L, L, L, L, L, G},
                {0, 0, L, L, L, L, 0, 0}
            }
        };
        s.setData(values);
        s.setRuneName("Rune of Spawner Collection");
        s.setNotes("Sacrifice:\n\n"
                + "-6xGoldOre + 13XP");
        s.setDesc("Description:\n\n"
                + "Collects a spawner that it is placed around.");
        s.setAuthor("billythegoat101");
        s.setManualRotationDerp(new int[] { -2, -1, -1, 0, 0, -1, -1, -2});
        DustManager.registerLocalDustShape(s, new DESpawnerCollector());
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="reprog">
        s = new DustShape(8, 10, "reprog", true, 2, 2, 2, 2, 33);
        values = new int[][][]
        {
            {
                {0, 0, L, 0, 0, L, 0, 0},
                {0, L, L, 0, 0, L, L, 0},

                {0, L, 0, 0, 0, 0, L, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {L, L, 0, 0, 0, 0, L, L},
                {L, L, 0, 0, 0, 0, L, L},

                {0, L, L, L, L, L, L, 0},
                {L, L, L, L, L, L, L, L},
                {L, L, 0, L, L, 0, L, L},
                {0, L, 0, L, L, 0, L, 0}
            }
        };
        s.setData(values);
        s.setRuneName("Rune of Spawner Reassignment");
        s.setNotes("Sacrifice:\n\n"
                + "-1xMobEgg + 4xEnderPearl + 25XP");
        s.setDesc("Description:\n\n"
                + "Reassigns a placed spawner to spawn mobs of the specified type.");
        s.setAuthor("billythegoat101");
        s.setManualRotationDerp(new int[] { -1, 0, 0, 0, 0, 0, 0, -1});
        DustManager.registerLocalDustShape(s, new DESpawnerReprog());
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="tele">
        s = new DustShape(12, 10, "tele6", false, 1, 0, 3, 8, 30);
        values = new int[][][]
        {
            {
                {0, 0, 0, 0, 0, B, 0, 0, 0, 0},
                {0, 0, 0, 0, B, B, 0, 0, 0, 0},
                {0, 0, 0, 0, B, G, 0, 0, 0, 0},
                {0, 0, 0, 0, G, G, 0, 0, 0, 0},

                {0, 0, 0, 0, G, B, 0, 0, 0, 0},
                {0, 0, 0, 0, B, B, 0, 0, 0, 0},
                {0, 0, 0, 0, B, G, 0, 0, 0, 0},
                {0, 0, 0, 0, G, G, 0, 0, 0, 0},

                {G, G, G, G, G, G, G, G, G, G},
                {0, B, B, G, B, B, G, B, B, 0},
                {0, 0, G, G, B, B, G, G, 0, 0},
                {0, 0, 0, G, 0, 0, G, 0, 0, 0}
            }
        };
        s.setData(values);
        s.setRuneName("Rune of Teleportation");
        s.setNotes("Sacrifice:\n\n"
                + "-1xEnderEye + 20XP\n\n"
                + "Notes:\n\n"
                + "-Takes away 3 hearts upon every teleportation.");
        s.setDesc("Description:\n\n"
                + "Creates a teleporation network location for other teleportation runes to teleport to. Will cost 4 hearts every teleportation. The teleportation network frequency on which to send you depends on the block beneath the blaze square in the rune design.");
        s.setAuthor("billythegoat101");
        s.setManualRotationDerp(new int[] {0, 0, 1, 0, 2, 0, 1, -2});
        DustManager.registerLocalDustShape(s, new DETeleportation());
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="minitele">
        s = new DustShape(10, 4, "minitele", false, 1, 0, 3, 0, 34);
        values = new int[][][]
        {
            {
                {B, B, B, B, G, G, B, B, B, B},
                {0, G, G, G, B, B, G, G, G, 0},
                {0, B, B, G, B, B, G, B, B, 0},
                {0, 0, G, G, 0, 0, G, G, 0, 0}
            }
        };
        s.setData(values);
        s.setRuneName("Rune of Transport");
        s.setNotes("Sacrifice:\n\n"
                + "-5XP");
        s.setDesc("Description:\n\n"
                + "Teleports you to a teleporation network rune location. The teleportation network frequency on which to send you depends on the block beneath the blaze square in the rune design.");
        s.setAuthor("billythegoat101");
        s.setManualRotationDerp(new int[] {0, 0, -1, 0, 0, 0, -1, 0});
        DustManager.registerLocalDustShape(s, new DEMiniTele());
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Fire Sprite">
        s = new DustShape(12, 12, "Fire Sprite6", false, 0, 0, 4, 4, 20);
        values = new int[][][]
        {
            {
                {0, 0, 0, G, G, 0, 0, G, G, 0, 0, 0},
                {0, 0, 0, 0, G, 0, 0, G, 0, 0, 0, 0},
                {0, 0, 0, G, B, G, G, B, G, 0, 0, 0},
                {G, 0, G, B, B, G, G, B, B, G, 0, G},

                {G, G, B, B, 0, 0, 0, 0, B, B, G, G},
                {0, 0, G, G, 0, B, B, 0, G, G, 0, 0},
                {0, 0, G, G, 0, B, B, 0, G, G, 0, 0},
                {G, G, B, B, 0, 0, 0, 0, B, B, G, G},

                {G, 0, G, B, B, G, G, B, B, G, 0, G},
                {0, 0, 0, G, B, G, G, B, G, 0, 0, 0},
                {0, 0, 0, 0, G, 0, 0, G, 0, 0, 0, 0},
                {0, 0, 0, G, G, 0, 0, G, G, 0, 0, 0}
            }
        };
        s.setData(values);
        s.setRuneName("Rune of the Fire Sprite");
        s.setNotes("Sacrifice:\n\n"
                + "-1xGhastTear + 2xFireCharge + 22XP\n\n"
                + "-Notes:\n\n"
                + "-Will last for three days unless fueled.");
        s.setDesc("Description:\n\n"
                + "Creates a sprite that will follow you and set the world and mobs on fire. ");
        s.setAuthor("billythegoat101");
        s.setManualRotationDerp(new int[] {0, 0, 0, 0, 0, 0, 0, 0});
        DustManager.registerLocalDustShape(s, new DEFireSprite());
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Earth Sprite">
        s = new DustShape(10, 8, "earthsprite6", false, 1, 2, 3, 2, 39);
        values = new int[][][]
        {
            {
                {0, 0, 0, 0, G, G, 0, 0, 0, 0},
                {0, 0, G, P, B, G, P, G, 0, 0},

                {0, B, B, P, B, B, P, B, B, 0},
                {G, G, G, P, G, B, P, G, G, G},
                {G, G, G, P, B, G, P, G, G, G},
                {0, B, B, P, B, B, P, B, B, 0},

                {0, 0, G, P, G, B, P, G, 0, 0},
                {0, 0, 0, 0, G, G, 0, 0, 0, 0}
            }
        };
        s.setData(values);
        s.setRuneName("Rune of the Earth Sprite");
        s.setNotes("Sacrifice:\n\n"
                + "-1xGhastTear + 20xIronOre + 16xGlass + 25XP\n\n"
                + "Notes:\n\n"
                + "-Stop and crouch to call the sprite to protect you.\n"
                + "-Will last for three days unless fueled.\n");
        s.setDesc("Description:\n\n"
                + "Summons a sprite that will encircle you with earth. Pressing [crouch] while standing still will call the sprite to protect you.");
        s.setAuthor("billythegoat101");
        s.setManualRotationDerp(new int[] {0, 0, -1, 0, 0, 0, -1, 0});
        DustManager.registerLocalDustShape(s, new DEEarthSprite());
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="loyaltysprite">
        s = new DustShape(6, 6, "loyaltysprite6", false, 3, 3, 1, 1, 21);
        values = new int[][][]
        {
            {
                {L, L, G, L, G, G},

                {G, G, G, L, L, L},
                {L, G, L, G, L, G},
                {L, G, L, G, L, G},
                {G, G, G, L, L, L},

                {L, L, G, L, G, G}
            }
        };
        s.setData(values);
        s.setRuneName("Rune of the Loyalty Sprite");
        s.setNotes("Sacrifice:\n\n"
                + "-4xGhastTear + 10XP\n\n"
                + "Notes:\n\n"
                + "-Will last for three days unless fueled.\n"
                + "-CURRENTLY BROKEN.");
        s.setDesc("Description:\n\n"
                + "Takes over the mind of a mob to have them fight for you. CURRENTLY BROKEN: Any attempt to summon this rune will automatically fail.");
        s.setAuthor("billythegoat101");
        s.setManualRotationDerp(new int[] {0, 0, 0, 0, 0, 0, 0, 0});
        DustManager.registerLocalDustShape(s, new DELoyaltySprite().setAllowed(false));
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="hunter">
        s = new DustShape(12, 8, "hunter6", false, 0, 2, 0, 0,  43);
        values = new int[][][]
        {
            {
                {0, 0, G, G, G, 0, 0, G, G, G, 0, 0},
                {G, G, G, B, G, G, G, G, B, G, G, G},

                {0, 0, B, G, G, G, G, G, G, B, 0, 0},
                {0, 0, G, G, B, B, B, B, G, G, 0, 0},
                {0, 0, G, G, B, B, B, B, G, G, 0, 0},
                {0, 0, B, G, G, G, G, G, G, B, 0, 0},

                {G, G, G, B, G, G, G, G, B, G, G, G},
                {0, 0, G, G, G, 0, 0, G, G, G, 0, 0},
            }
        };
        s.setData(values);
        s.setRuneName("Rune of the Hunter");
        s.setAuthor("billythegoat101");
        s.setManualRotationDerp(new int[] { -1, -1, -1, -1, -1, -1, -1, -1});
        s.setNotes("Sacrifice:\n\n"
                + "-3xBlazePowder + 1xEnderEye + 12XP\n\n"
                + "Notes:\n\n"
                + "-Will last a day unless fueled.\n"
                + "-Will cause lag: Right clicking will pause the rune (and its fuel consumption.)\n\n"
                + "Current bug: Does not detect entities who do not utilize the new AI system.");
        s.setDesc("Description:\n\n"
                + "Allows you to see the location and health of any mob nearby. WARNING: Possiblitity for lag on lower-quality machines. Right-click the rune to disable. ");
        
        DustManager.registerLocalDustShape(s, new DEHunterVision().setAllowed(false));
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="bounce">
        s = new DustShape(4, 4, "bounce", false, 0, 0, 0, 0, 22);
        values = new int[][][]
        {
            {
                {G, G, P, P},
                {G, P, G, P},
                {P, G, P, G},
                {P, P, G, G}
            }
        };
        s.setData(values);
        s.setRuneName("Rune of Bouncing");
        s.setNotes("Sacrifice:\n\n"
                + "-4xSlimeBall");
        s.setDesc("Description:\n\n"
                + "Creates a rune that will help you jump much higher.");
        s.setAuthor("billythegoat101");
        s.setManualRotationDerp(new int[] {0, 0, 0, 0, 0, 0, 0, 0});
        DustManager.registerLocalDustShape(s, new DEBounce());
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="record">
        s = new DustShape(4, 4, "record", false, 0, 0, 0, 0, 23);
        values = new int[][][]
        {
            {
                {2, 2, 2, 2},
                {2, 3, 2, 2},
                {2, 2, 3, 2},
                {2, 2, 2, 2}
            }
        };
        s.setData(values);
        s.setRuneName("Rune of Music");
        s.setNotes("Sacrifice:\n\n"
                + "-1xDiamond");
        s.setDesc("Description:\n\n"
                + "Spawns a random record.");
        s.setAuthor("billythegoat101");
        s.setManualRotationDerp(new int[] {0, 0, 0, 0, 0, 0, 0, 0});
        DustManager.registerLocalDustShape(s, new DESpawnRecord());
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="spirittools">
        s = new DustShape(4, 4, "spirittools", true, 0, 0, 0, 0, 14);
        values = new int[][][]
        {
            {
                {L, L, L, L},
                {L, L, L, L},
                {L, L, L, L},
                {L, L, L, L}
            }
        };
        s.setRuneName("Rune of the Spirit Tools");
        s.setNotes("Sacrifice:\n\n"
                + "-Spirit Pickaxe: 1xGoldPickaxe + 4xTNT + 18XP\n"
                + "-Spirit Sword: 1xGoldSword + 1xGlowstoneBlock + 18XP");
        s.setDesc("Description:\n\n"
                + "Spawns either a spirit sword or spirit pickaxe. Each with special abilities.");
        s.setData(values);
        s.setAuthor("billythegoat101");
        s.setManualRotationDerp(new int[] {0, 0, 0, 0, 0, 0, 0, 0});
        DustManager.registerLocalDustShape(s, new DESpiritTool());
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="ench.firebow">
        s = new DustShape(6, 6, "ench.firebow", false, 3, 3, 1, 1, 28);
        values = new int[][][]
        {
            {
                {0, B, G, G, 0, 0},
                {B, B, G, B, B, 0},
                {G, G, G, G, B, G},
                {G, B, G, G, G, G},
                {0, B, B, G, B, B},
                {0, 0, G, G, B, 0}
            }
        };
        s.setData(values);
        s.setRuneName("Enchanting Rune of the Fire Bow");
        s.setNotes("Sacrifice:\n\n"
                + "-9xFireCharge + 1xBow + 1xGoldBlock + 30XP");
        s.setDesc("Description:\n\n"
                + "Enchants and repairs your bow with Fire Aspect I");
        s.setAuthor("billythegoat101");
        s.setManualRotationDerp(new int[] {0, 0, 0, 0, 0, 0, 0, 0});
        DustManager.registerLocalDustShape(s, new DEFireBowEnch());
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="ench.silktouch">
        s = new DustShape(8, 8, "ench.silktouch", false, 2, 2, 2, 2, 26);
        values = new int[][][]
        {
            {
                {0, 0, 0, G, 0, 0, 0, 0},
                {0, G, G, G, P, P, P, 0},

                {0, G, G, B, G, P, P, 0},
                {G, G, B, G, B, G, P, 0},
                {0, P, G, B, G, B, G, G},
                {0, P, P, G, B, G, G, 0},

                {0, P, P, P, G, G, G, 0},
                {0, 0, 0, 0, G, 0, 0, 0}
            }
        };
        s.setData(values);
        s.setRuneName("Enchanting Rune of Silk Touch");
        s.setNotes("Sacrifice:\n\n"
                + "-1xLiveTestificate + 1xDiamondPickaxe + 1xGoldBlock + 20XP\n"
                + "OR\n"
                + "-1xLiveTestificate + 1xDiamondSword + 1xGoldBlock + 20XP");
        s.setDesc("Description:\n\n"
                + "Enchants and repairs your pick or shovel with Silk Touch I.");
        s.setAuthor("billythegoat101");
        s.setManualRotationDerp(new int[] {0, 0, 0, 0, 0, 0, 0, 0});
        DustManager.registerLocalDustShape(s, new DESilkTouchEnch());
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="ench.fortune">
        s = new DustShape(10, 10, "ench.fortune", false, 1, 1, 5, 5, 27);
        values = new int[][][]
        {
            {
                {0, 0, G, G, G, G, G, G, 0, 0},
                {0, 0, G, L, G, G, L, G, 0, 0},
                {G, G, L, L, P, P, L, L, G, G},
                {G, L, L, P, 0, 0, P, L, L, G},
                {G, G, P, 0, 0, 0, 0, P, G, G},
                {G, G, P, 0, 0, 0, 0, P, G, G},
                {G, L, L, P, 0, 0, P, L, L, G},
                {G, G, L, L, P, P, L, L, G, G},
                {0, 0, G, L, G, G, L, G, 0, 0},
                {0, 0, G, G, G, G, G, G, 0, 0}
            }
        };
        s.setData(values);
        s.setRuneName("Enchanting Rune of Fortune");
        s.setNotes("Sacrifice:\n\n"
                + "-1xDiamondOre + 1xLapisOre + 1xRedstoneOre + 1xGoldBlock + 1xDiamondPickaxe + 40XP\n"
                + "OR\n"
                + "-1xDiamondOre + 1xLapisOre + 1xRedstoneOre + 1xGoldBlock + 1xDiamondSword + 40XP\n");
        s.setDesc("Description:\n\n"
                + "Enchants and repairs your pick or sword with Fortune IV or Looting IV respectively.");
        s.setAuthor("billythegoat101");
        s.setManualRotationDerp(new int[] {0, 0, 0, 0, 0, 0, 0, 0});
        DustManager.registerLocalDustShape(s, new DEFortuneEnch());
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="forcefield">
        s = new DustShape(12, 12, "forcefield6", false, 0, 0, 4, 4, 36);
        values = new int[][][]
        {
            {
                {0, 0, 0, 0, L, 0, 0, L, 0, 0, 0, 0},
                {0, 0, L, L, 0, L, L, 0, L, L, 0, 0},
                {0, L, L, G, L, B, B, L, G, L, L, 0},
                {0, L, G, G, B, G, G, B, G, G, L, 0},

                {L, 0, L, B, B, 0, 0, B, B, L, 0, L},
                {0, L, B, G, 0, N, N, 0, G, B, L, 0},
                {0, L, B, G, 0, N, N, 0, G, B, L, 0},
                {L, 0, L, B, B, 0, 0, B, B, L, 0, L},

                {0, L, G, G, B, G, G, B, G, G, L, 0},
                {0, L, L, G, L, B, B, L, G, L, L, 0},
                {0, 0, L, L, 0, L, L, 0, L, L, 0, 0},
                {0, 0, 0, 0, L, 0, 0, L, 0, 0, 0, 0}
            }
        };
        s.setData(values);
        s.setRuneName("Rune of Protection");
        s.setNotes("Sacrifice:\n\n"
                + "-2xLiveIronGolem + 50XP\n\n"
                + "Notes:\n\n"
                + "-Lasts for a day unless fueled.");
        s.setDesc("Description:\n\n"
                + "Creates a forcefield that will push away all hostile mobs.");
        s.setAuthor("billythegoat101");
        s.setManualRotationDerp(new int[] {0, 0, 0, 0, 0, 0, 0, 0});
        DustManager.registerLocalDustShape(s, new DEForcefield());
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="flatten">
        s = new DustShape(20, 20, "flatten", false, 0, 0, 9, 9, 32);
        values = new int[][][]
        {
            {
                {0, 0, 0, 0,   0, 0, 0, 0,    G, 0, 0, G,    0, 0, 0, 0,    0, 0, 0, 0},
                {0, 0, 0, 0,   0, 0, 0, 0,    G, G, G, G,    0, 0, 0, 0,    0, 0, 0, 0},
                {0, 0, 0, 0,   0, 0, 0, 0,    G, 0, 0, G,    0, 0, 0, 0,    0, 0, 0, 0},
                {0, 0, 0, 0,   0, 0, 0, 0,    G, G, G, G,    0, 0, 0, 0,    0, 0, 0, 0},

                {0, 0, 0, 0,   0, 0, 0, 0,    G, 0, 0, G,    0, 0, 0, 0,    0, 0, 0, 0},
                {0, 0, 0, 0,   0, 0, P, P,    G, G, G, G,    P, P, 0, 0,    0, 0, 0, 0},
                {0, 0, 0, 0,   0, P, P, L,    G, 0, 0, G,    L, P, P, 0,    0, 0, 0, 0},
                {0, G, G, G,   0, P, L, G,    G, L, L, G,    G, L, P, 0,    G, G, G, 0},

                {G, G, G, G,   G, G, G, G,    0, 0, 0, 0,    G, G, G, G,    G, G, G, G},
                {0, G, 0, G,   0, G, 0, L,    0, N, N, 0,    L, 0, G, 0,    G, 0, G, 0},
                {0, G, 0, G,   0, G, 0, L,    0, N, N, 0,    L, 0, G, 0,    G, 0, G, 0},
                {G, G, G, G,   G, G, G, G,    0, 0, 0, 0,    G, G, G, G,    G, G, G, G},

                {0, G, G, G,   0, P, L, G,    G, L, L, G,    G, L, P, 0,    G, G, G, 0},
                {0, 0, 0, 0,   0, P, P, L,    G, 0, 0, G,    L, P, P, 0,    0, 0, 0, 0},
                {0, 0, 0, 0,   0, 0, P, P,    G, G, G, G,    P, P, 0, 0,    0, 0, 0, 0},
                {0, 0, 0, 0,   0, 0, 0, 0,    G, 0, 0, G,    0, 0, 0, 0,    0, 0, 0, 0},

                {0, 0, 0, 0,   0, 0, 0, 0,    G, G, G, G,    0, 0, 0, 0,    0, 0, 0, 0},
                {0, 0, 0, 0,   0, 0, 0, 0,    G, 0, 0, G,    0, 0, 0, 0,    0, 0, 0, 0},
                {0, 0, 0, 0,   0, 0, 0, 0,    G, G, G, G,    0, 0, 0, 0,    0, 0, 0, 0},
                {0, 0, 0, 0,   0, 0, 0, 0,    G, 0, 0, G,    0, 0, 0, 0,    0, 0, 0, 0},
            }
        };
        s.setData(values);
        s.setRuneName("Rune of Level Earth");
        s.setNotes("Sacrifice:\n\n"
                + "-Plant Dust: 10XP + 20xIronOre\n"
                + "-Gunpowder Dust: 12XP + 20xIronOre\n"
                + "-Lapis Dust: 15XP + 20xIronOre\n"
                + "-Blaze Dust: 20XP + 20xIronOre");
        s.setDesc("Description:\n\n"
                + "Flattens the terrain around it.");
        s.setAuthor("billythegoat101");
        s.setManualRotationDerp(new int[] {0, 0, 0, 0, 0, 0, 0, 0});
        DustManager.registerLocalDustShape(s, new DEFlatten());
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="liftterrain">
        s = new DustShape(16, 8, "liftterrain", false, 2, 2, 6, 2,  38);
        values = new int[][][]
        {
            {
                {0, 0, 0, 0, 0, L, G, G, G, G, L, 0, 0, 0, 0, 0},
                {L, L, L, L, L, L, G, P, P, G, L, L, L, L, L, L},

                {0, 0, G, G, G, G, P, P, P, P, G, G, G, G, 0, 0},
                {0, 0, 0, 0, G, P, P, N, N, P, P, G, 0, 0, 0, 0},
                {0, 0, 0, 0, G, P, P, N, N, P, P, G, 0, 0, 0, 0},
                {0, 0, G, G, G, G, P, P, P, P, G, G, G, G, 0, 0},

                {L, L, L, L, L, L, G, P, P, G, L, L, L, L, L, L},
                {0, 0, 0, 0, 0, L, G, G, G, G, L, 0, 0, 0, 0, 0}
            }
        };
        s.setData(values);
        s.setRuneName("Rune of the Mountain");
        s.setNotes("Sacrifice:\n\n"
                + "-1xLiveIronGolem + 1xRose + 60XP\n\n"
                + "Notes:\n\n"
                + "-The area you want to lift should be outline with etchings filled with clay blocks.");
        s.setDesc("Description:\n\n"
                + "Lifts the earth specified by the clay-filled etchings up high into the sky.");
        s.setAuthor("billythegoat101");
        s.setManualRotationDerp(new int[] {0, 0, -1, 1, 0, 0, -1, 1});
        
        DustManager.registerLocalDustShape(s, new DELiftTerrain());
        //</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="sarlacc">
        s = new DustShape(22, 32, "xp6", false, 2, 3, 15, 10, 31);
        values = new int[][][]
        {
            {
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, G, 0, 0, G, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},

                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, G, 0, 0, 0, G, G, G, G, 0, 0, 0, G, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, G, G, G, G, 0, 0, 0, 0, G, G, 0, 0, 0, 0, G, G, G, G, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, L, L, 0, 0, 0, 0, 0, 0, L, 0, L, 0, 0, L, 0, L, 0, 0, 0, 0, 0, 0, L, L, 0, 0, 0, 0},
                {0, 0, 0, 0, L, L, L, L, L, L, L, L, L, L, L, L, L, L, L, L, L, L, L, L, L, L, L, L, 0, 0, 0, 0},

                {0, 0, G, G, 0, L, 0, 0, 0, 0, 0, 0, 0, 0, 0, L, L, 0, 0, 0, 0, 0, 0, 0, 0, 0, L, 0, G, G, 0, 0},
                {0, 0, G, 0, 0, L, 0, 0, 0, 0, 0, 0, 0, 0, 0, L, L, 0, 0, 0, 0, 0, 0, 0, 0, 0, L, 0, 0, G, 0, 0},
                {0, 0, 0, 0, 0, L, 0, 0, 0, 0, 0, 0, 0, 0, 0, L, L, 0, 0, 0, 0, 0, 0, 0, 0, 0, L, 0, 0, 0, 0, 0},
                {0, 0, 0, L, L, L, 0, 0, 0, 0, 0, 0, 0, 0, L, L, L, L, 0, 0, 0, 0, 0, 0, 0, 0, L, L, L, 0, 0, 0},

                {0, L, L, L, 0, L, 0, 0, 0, 0, G, G, G, G, L, G, G, L, G, G, G, G, 0, 0, 0, 0, L, 0, L, L, L, 0},
                {L, L, L, 0, L, L, 0, 0, 0, 0, 0, L, L, G, G, L, L, G, G, L, L, 0, 0, 0, 0, 0, L, L, 0, L, L, L},
                {L, L, L, 0, L, L, 0, 0, 0, 0, 0, L, L, G, G, L, L, G, G, L, L, 0, 0, 0, 0, 0, L, L, 0, L, L, L},
                {0, L, L, L, 0, L, 0, 0, 0, 0, G, G, G, G, L, G, G, L, G, G, G, G, 0, 0, 0, 0, L, 0, L, L, L, 0},

                {0, 0, 0, L, L, L, 0, 0, 0, 0, 0, 0, 0, 0, L, L, L, L, 0, 0, 0, 0, 0, 0, 0, 0, L, L, L, 0, 0, 0},
                {0, 0, 0, 0, 0, L, 0, 0, 0, 0, 0, 0, 0, 0, 0, L, L, 0, 0, 0, 0, 0, 0, 0, 0, 0, L, 0, 0, 0, 0, 0},
                {0, 0, G, 0, 0, L, 0, 0, 0, 0, 0, 0, 0, 0, 0, L, L, 0, 0, 0, 0, 0, 0, 0, 0, 0, L, 0, 0, G, 0, 0},
                {0, 0, G, G, 0, L, 0, 0, 0, 0, 0, 0, 0, 0, 0, L, L, 0, 0, 0, 0, 0, 0, 0, 0, 0, L, 0, G, G, 0, 0},

                {0, 0, 0, 0, L, L, L, L, L, L, L, L, L, L, L, L, L, L, L, L, L, L, L, L, L, L, L, L, 0, 0, 0, 0},
                {0, 0, 0, 0, L, L, 0, 0, 0, 0, 0, 0, L, 0, L, 0, 0, L, 0, L, 0, 0, 0, 0, 0, 0, L, L, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, G, G, G, G, 0, 0, 0, 0, G, G, 0, 0, 0, 0, G, G, G, G, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, G, 0, 0, 0, G, G, G, G, 0, 0, 0, G, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},

                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, G, 0, 0, G, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            }
        };
        s.setData(values);
        s.setRuneName("Rune of Sarlacc");
        s.setNotes("Sacrifice:\n\n"
                + "-1xPassiveMobEgg + 1xHostileMobEgg + 80XP\n\n"
                + "Notes:\n\n"
                + "-Lasts for a day unless fueled. \n"
                + "-Every mob sacrificed will prolonge it\' life for an eighth of a day.\n"
                + "-Every item (smeltable or otherwise) will prolonge it\'s life for (1/2 a second)*stackSize.");
        s.setDesc("Description:\n\n"
                + "Kills any mobs dropped onto it and destroys their drops. However, will drop 2 times as much XP into the holes around it. Will not damage anyone underneath.");
        s.setAuthor("billythegoat101");
        s.setManualRotationDerp(new int[] {0, 0, -1, 1, 0, 0, -1, 1});
        DustManager.registerLocalDustShape(s, new DEXP());
        //</editor-fold>
//        System.out.println("Loaded " + DustManager.shapes.size() + " runes.");
        //last id used: 45
        //notes for reanimation:
        //all numbers are cut off at the end of the name to preserve lexicon page picture names
    
	}

    
    
}