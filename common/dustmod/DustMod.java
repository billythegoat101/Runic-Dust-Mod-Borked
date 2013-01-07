package dustmod;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraft.server.MinecraftServer;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.DustModBouncer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.entity.monster.IMob;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.WorldEvent;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(modid = "DustMod", name = "Runic Dust Mod", version = "1.1.6")
@NetworkMod(
	clientSideRequired = true, 
	serverSideRequired = false, 
	packetHandler = PacketHandler.class,
	channels = {PacketHandler.CHANNEL_DMRune, 
		PacketHandler.CHANNEL_TEDust, 
		PacketHandler.CHANNEL_TELexicon, 
		PacketHandler.CHANNEL_TERut, 
		PacketHandler.CHANNEL_DustItem, 
		PacketHandler.CHANNEL_Mouse, 
		PacketHandler.CHANNEL_UseInk, 
		PacketHandler.CHANNEL_SetInscription, 
		PacketHandler.CHANNEL_DeclareInscription,
		PacketHandler.CHANNEL_SpawnParticles})
public class DustMod {

	@Instance("DustMod")
	public static DustMod instance;
	
    public static final int warpVer = 1;
    public static boolean debug = false;
    
    public static int plantDID = 1;
    public static int gunDID = 2;
    public static int lapisDID = 3;
    public static int blazeDID = 4;

    public static String path = "/dust";
    public static File suggestedConfig;
    public static int[] tex;
    public static int groundTex;
    public static boolean allTex = true;

    public static int DustMetaDefault = 0;
    public static int DustMetaUsing = 1;
    public static int DustMetaUsed = 2;

    public static int BLOCK_RutID = 222;
    public static int BLOCK_DustTableID = 221;
    public static int BLOCK_DustID = 220;
    public static int ITEM_DustID = 1240;
    public static int ITEM_RunicTomeID = 1220;
    public static int ITEM_DustScrollID = 1221;
    public static int ITEM_SpiritSwordID = 1230;
    public static int ITEM_SpiritPickID = 1231;
    public static int ITEM_ChiselID = 1232;
    public static int ITEM_SacrificeNegationID = 1233;
    public static int ITEM_RunicPaperID = 1234;
    public static int ITEM_InscriptionID = 1235;
    public static int ITEM_InkID = 1236;
    public static int ITEM_WornInscriptionID = 1237;
    public static int ITEM_PouchID = 1238;
    public static int ENTITY_FireSpriteID = 149;
    public static int ENTITY_BlockEntityID = 150;
    public static boolean Enable_Render_Flames_On_Dust = true;
    public static boolean Enable_Render_Flames_On_Ruts = true;
    public static boolean Enable_Decorative_Ruts = false;
    
    public static boolean verbose = false;
    
    public static Block dust;
    protected static Block dustTable;
    protected static Block rutBlock;
    protected static Item idust;
    protected static Item tome;
    protected static Item dustScroll;
    public static Item spiritPickaxe;
    public static Item spiritSword;
    protected static Item chisel;
    protected static Item negateSacrifice;
    protected static Item runicPaper;
    protected static ItemInscription inscription;
    protected static ItemInk ink;
    protected static ItemWornInscription wornInscription;
    protected static ItemPouch pouch;
    
    public static int prevVoidSize;
    public static HashMap<String, ArrayList<ItemStack>> voidInventory;
    public static ArrayList<int[]> voidNetwork;
    public static int skipWarpTick = 0;
    
    public static int numSec = 0; //number of secret runes
    
    @SidedProxy(clientSide = "dustmod.ClientProxy", serverSide = "dustmod.CommonProxy")
    public static CommonProxy proxy;
    public static CommonMouseHandler keyHandler = new CommonMouseHandler();
	public static InscriptionManager inscriptionManager = new InscriptionManager();
	
	private static boolean hasLoaded = false;
	@PreInit
	public void preInit(FMLPreInitializationEvent evt){
		if(hasLoaded) return;
		hasLoaded = true;
		suggestedConfig = new File(evt.getSuggestedConfigurationFile().getParent() + File.separator + "DustModConfigv2.cfg");
//		suggestedConfig.renameTo(new File("DustModConfigv2.cfg"));
		
//		System.out.println("CONFIG " + suggestedConfig.getParent());
		Configuration config = new Configuration(suggestedConfig);
		try{
//			File f = new File(configPath);
//			f.mkdirs();
			config.load();
			
			BLOCK_RutID = config.getBlock("RutBlock", BLOCK_RutID).getInt(BLOCK_RutID);
			BLOCK_DustTableID = config.getBlock("DustTableBlock", BLOCK_DustTableID).getInt(BLOCK_DustTableID);
			BLOCK_DustID = config.getBlock("DustBlock", BLOCK_DustID).getInt(BLOCK_DustID);
			
			ITEM_DustID = config.getItem("DustItem", ITEM_DustID).getInt(ITEM_DustID);
			ITEM_RunicTomeID = config.get( Configuration.CATEGORY_ITEM, "TomeItem",ITEM_RunicTomeID).getInt(ITEM_RunicTomeID);
			ITEM_DustScrollID = config.getItem("ScrollItem", ITEM_DustScrollID).getInt(ITEM_DustScrollID);
			ITEM_SpiritSwordID = config.getItem("SpirtSwordItem", ITEM_SpiritSwordID).getInt(ITEM_SpiritSwordID);
			ITEM_SpiritPickID = config.getItem("SpiritPickItem", ITEM_SpiritPickID).getInt(ITEM_SpiritPickID);
			ITEM_ChiselID = config.getItem("ChiselItem", ITEM_ChiselID).getInt(ITEM_ChiselID);
			ITEM_SacrificeNegationID = config.getItem("SacrificeNegatorItem", ITEM_SacrificeNegationID).getInt(ITEM_SacrificeNegationID);
			ITEM_RunicPaperID = config.getItem("RunicPaperItem", ITEM_RunicPaperID).getInt(ITEM_RunicPaperID);
			ITEM_InscriptionID = config.getItem("RunicInscriptionTag", ITEM_InscriptionID).getInt(ITEM_InscriptionID);
			ITEM_InkID = config.getItem("RunicInk", ITEM_InkID).getInt(ITEM_InkID);
			ITEM_WornInscriptionID = config.getItem("WearableInscription", ITEM_WornInscriptionID).getInt(ITEM_WornInscriptionID);
			ITEM_PouchID = config.getItem("DustPouch", ITEM_PouchID).getInt(ITEM_PouchID);
			
			ENTITY_FireSpriteID = config.get(Configuration.CATEGORY_GENERAL, "FireSpriteEntityID", ENTITY_FireSpriteID).getInt(ENTITY_FireSpriteID);
			ENTITY_BlockEntityID = config.get(Configuration.CATEGORY_GENERAL, "BlockEntityID", ENTITY_BlockEntityID).getInt(ENTITY_BlockEntityID);
			Enable_Decorative_Ruts = config.get("config", "DecorativeRuts", Enable_Decorative_Ruts).getBoolean(Enable_Decorative_Ruts);
			verbose = config.get("config", "verbose", verbose).getBoolean(verbose);
		} catch (Exception e){
			FMLLog.log(Level.SEVERE, e, "[DustMod] : Error loading config.");
		} finally {
			config.save();
		}
		
		dust = new BlockDust(BLOCK_DustID, 164);
		idust = (new ItemDust(ITEM_DustID, dust)).setItemName("idust").setCreativeTab(CreativeTabs.tabMaterials);
		dustTable = ((Block)new BlockDustTable(BLOCK_DustTableID)).setCreativeTab(CreativeTabs.tabDecorations);
        tome = (new ItemRunicTome(ITEM_RunicTomeID)).setItemName("dustlibrary").setCreativeTab(CreativeTabs.tabDecorations);
        negateSacrifice = DustModBouncer.newItem(ITEM_SacrificeNegationID).setItemName("negateSacrifice").setCreativeTab(CreativeTabs.tabMaterials);//.setIconIndex(ModLoader.addOverride("/gui/items.png", path + "/cancel.png")); //[non-forge]
        negateSacrifice.setIconCoord(3,2).setTextureFile(path + "/dustItems.png");//[forge]
        runicPaper = (DustModBouncer.newItem(ITEM_RunicPaperID)).setItemName("runicPaper").setCreativeTab(CreativeTabs.tabMaterials)/*[forge]*/.setIconCoord(1, 2);//[non-forge].setIconIndex(ModLoader.addOverride("/gui/items.png", path + "/runicPaper.png"));
        runicPaper.setTextureFile(path + "/dustItems.png");//[forge];
        dustScroll = (new ItemPlaceScroll(ITEM_DustScrollID)).setItemName("dustscroll").setCreativeTab(CreativeTabs.tabMisc);
        rutBlock = new BlockRut(BLOCK_RutID).setBlockName("dustrutblock").setHardness(3.0F).setResistance(5.0F);
        chisel = new ItemChisel(ITEM_ChiselID).setItemName("itemdustchisel").setCreativeTab(CreativeTabs.tabTools);
        spiritPickaxe = (Item)(new ItemSpiritPickaxe(ITEM_SpiritPickID, EnumToolMaterial.EMERALD)).setItemName("dustpickaxeSpirit").setCreativeTab(CreativeTabs.tabTools);
        spiritSword = (Item)(new ItemSpiritSword(ITEM_SpiritSwordID)).setItemName("dustswordSpirit").setCreativeTab(CreativeTabs.tabCombat);
		inscription = (ItemInscription)(new ItemInscription(ITEM_InscriptionID)).setItemName("runicinscription").setCreativeTab(CreativeTabs.tabMaterials);
		inscription.setTextureFile(path + "/dustItems.png");
        ink = new ItemInk(ITEM_InkID);
        wornInscription = new ItemWornInscription(ITEM_WornInscriptionID);wornInscription.setCreativeTab(CreativeTabs.tabMisc);
        pouch = new ItemPouch(ITEM_PouchID, dust);pouch.setCreativeTab(CreativeTabs.tabMisc);
		
	}
	
	@Init
	public void load(FMLInitializationEvent evt){
		
		NetworkRegistry.instance().registerConnectionHandler(new PacketHandler());
		NetworkRegistry.instance().registerGuiHandler(this, new GuiHandler());
		
		proxy.registerEventHandlers();
		
		GameRegistry.registerBlock(dust);
		GameRegistry.registerBlock(dustTable);
		GameRegistry.registerBlock(rutBlock);
		
		GameRegistry.registerTileEntity(TileEntityDust.class, "dusttileentity");
		GameRegistry.registerTileEntity(TileEntityDustTable.class, "dusttabletileentity");
		GameRegistry.registerTileEntity(TileEntityRut.class, "dustruttileentity");		
		
		proxy.registerTileEntityRenderers();
		
		LanguageRegistry lang = LanguageRegistry.instance();
		lang.addStringLocalization("tile.dust.name", "en_US", "[DustMod] :Do not use this");

        lang.addStringLocalization(dustTable.getBlockName() + ".name", "en_US", "Runic Lexicon");
        lang.addStringLocalization(tome.getItemName() + ".name", "en_US", "Runic Tome");
        lang.addStringLocalization(negateSacrifice.getItemName() + ".name", "en_US", "Negate Rune Sacrifice");
        lang.addStringLocalization(runicPaper.getItemName() + ".name", "en_US", "Scroll Paper");
        lang.addStringLocalization(spiritPickaxe.getItemName() + ".name", "en_US", "Spirit Pickaxe");
        lang.addStringLocalization(spiritSword.getItemName() + ".name", "en_US", "Spirit Sword");
        lang.addStringLocalization(chisel.getItemName() + ".name", "en_US", "Hammer&Chisel");
        lang.addStringLocalization("pouchblank.name", "en_US", "ERROR Runic Pouch");
//        lang.addStringLocalization(inscription.getItemName() + ".name", "en_US", "Blank Runic Inscription");
		lang.addStringLocalization("emptyinsc.name", "en_US", "Blank Runic Inscription");
		lang.addStringLocalization("driedinsc.name", "en_US", "Dried Drawing");
		lang.addStringLocalization("dryinginsc.name", "en_US", "Drying Inscription");
        
        GameRegistry.addRecipe(new ItemStack(dustTable, 1), new Object[] {"dwd", "wbw", "dwd", 'd', new ItemStack(idust, 1, -1), 'w', new ItemStack(Block.planks, 1, -1), 'b', new ItemStack(tome, -1)});
        GameRegistry.addRecipe(new ItemStack(dustTable, 1), new Object[] {"wdw", "dbd", "wdw", 'd', new ItemStack(idust, 1, -1), 'w', new ItemStack(Block.planks, 1, -1), 'b', new ItemStack(tome, -1)});
        GameRegistry.addRecipe(new ItemStack(chisel, 1), new Object[] {"st", "i ", 's', new ItemStack(Block.cobblestone, 1), 't', new ItemStack(Item.stick, 1), 'i', new ItemStack(Item.ingotIron, 1)});
        GameRegistry.addRecipe(new ItemStack(inscription, 1), new Object[] {"s", "p", "p", 's', new ItemStack(Item.silk, 1), 'p', new ItemStack(runicPaper, 1)});
        
        GameRegistry.addShapelessRecipe(new ItemStack(idust, 4, 100), new Object[] {new ItemStack(Item.coal.shiftedIndex, 1, -1), new ItemStack(Block.tallGrass, 1, -1), new ItemStack(Block.tallGrass, 1, -1)/*, mortar*/});
        GameRegistry.addShapelessRecipe(new ItemStack(idust, 4, 100), new Object[] {new ItemStack(Item.coal.shiftedIndex, 1, -1), new ItemStack(Block.leaves, 1, -1), new ItemStack(Block.leaves, 1, -1)/*, mortar*/});
        GameRegistry.addShapelessRecipe(new ItemStack(idust, 4, 100), new Object[] {new ItemStack(Item.coal.shiftedIndex, 1, -1), new ItemStack(Block.sapling, 1, -1), new ItemStack(Block.sapling, 1, -1)/*, mortar*/});
        GameRegistry.addShapelessRecipe(new ItemStack(idust, 4, 100), new Object[] {new ItemStack(Item.coal.shiftedIndex, 1, -1), Item.seeds, Item.seeds/*, mortar*/});
        GameRegistry.addShapelessRecipe(new ItemStack(idust, 4, 100), new Object[] {new ItemStack(Item.coal.shiftedIndex, 1, -1), Block.cactus, Block.cactus/*, mortar*/});
        GameRegistry.addShapelessRecipe(new ItemStack(idust, 4, 100), new Object[] {new ItemStack(Item.coal.shiftedIndex, 1, -1), Block.cactus, Item.seeds});
        GameRegistry.addShapelessRecipe(new ItemStack(idust, 4, 100), new Object[] {new ItemStack(Item.coal.shiftedIndex, 1, -1), Block.cactus, new ItemStack(Block.sapling, 1, -1)});
        GameRegistry.addShapelessRecipe(new ItemStack(idust, 4, 100), new Object[] {new ItemStack(Item.coal.shiftedIndex, 1, -1), Block.cactus, new ItemStack(Block.leaves, 1, -1)});
        GameRegistry.addShapelessRecipe(new ItemStack(idust, 4, 100), new Object[] {new ItemStack(Item.coal.shiftedIndex, 1, -1), Block.cactus, new ItemStack(Block.tallGrass, 1, -1)});
        GameRegistry.addShapelessRecipe(new ItemStack(idust, 4, 100), new Object[] {new ItemStack(Item.coal.shiftedIndex, 1, -1), Item.seeds, new ItemStack(Block.sapling, 1, -1)});
        GameRegistry.addShapelessRecipe(new ItemStack(idust, 4, 100), new Object[] {new ItemStack(Item.coal.shiftedIndex, 1, -1), Item.seeds, new ItemStack(Block.leaves, 1, -1)});
        GameRegistry.addShapelessRecipe(new ItemStack(idust, 4, 100), new Object[] {new ItemStack(Item.coal.shiftedIndex, 1, -1), Item.seeds, new ItemStack(Block.tallGrass, 1, -1)});
        GameRegistry.addShapelessRecipe(new ItemStack(idust, 4, 100), new Object[] {new ItemStack(Item.coal.shiftedIndex, 1, -1), new ItemStack(Block.sapling, 1, -1), new ItemStack(Block.leaves, 1, -1)/*, mortar*/});
        GameRegistry.addShapelessRecipe(new ItemStack(idust, 4, 100), new Object[] {new ItemStack(Item.coal.shiftedIndex, 1, -1), new ItemStack(Block.sapling, 1, -1), new ItemStack(Block.tallGrass, 1, -1)/*, mortar*/});
        GameRegistry.addShapelessRecipe(new ItemStack(idust, 4, 100), new Object[] {new ItemStack(Item.coal.shiftedIndex, 1, -1), new ItemStack(Block.leaves, 1, -1), new ItemStack(Block.tallGrass, 1, -1)/*, mortar*/});
        GameRegistry.addShapelessRecipe(new ItemStack(idust, 3, 200), new Object[] {Item.gunpowder, new ItemStack(idust, 1, 100), new ItemStack(idust, 1, 100)/*, mortar*/});
        GameRegistry.addShapelessRecipe(new ItemStack(idust, 4, 300), new Object[] {new ItemStack(Item.coal.shiftedIndex, 1, -1), new ItemStack(Item.dyePowder, 2, 4), new ItemStack(Item.dyePowder, 2, 4), new ItemStack(Item.dyePowder, 2, 4)});
        GameRegistry.addShapelessRecipe(new ItemStack(idust, 3, 400), new Object[] {Item.blazePowder, new ItemStack(idust, 1, 300), new ItemStack(idust, 1, 300), new ItemStack(idust, 1, 300)/*, mortar*/});
        GameRegistry.addShapelessRecipe(new ItemStack(tome, 1, 0), new Object[] {new ItemStack(idust, 1, -1), Item.book});
        GameRegistry.addShapelessRecipe(new ItemStack(runicPaper, 1), new Object[] {Item.paper, Item.goldNugget, Item.goldNugget});
        
        for(int i = 1; i < 5; i++){
        	//Migration from old system
        	GameRegistry.addShapelessRecipe(new ItemStack(idust,1,i*100), new ItemStack(idust,1,i));
        }
        
        
        EntityRegistry.registerModEntity(EntityDust.class, "dustentity", ENTITY_FireSpriteID, this, 192, 2, false);
//        EntityRegistry.registerGlobalEntityID(EntityDust.class, "dustentity", ENTITY_FireSpriteID);
        EntityRegistry.registerModEntity(EntityBlock.class, "dustblockentity", ENTITY_BlockEntityID, this, 64, 1, false);
//        EntityRegistry.registerGlobalEntityID(EntityBlock.class, "dustblockentity", ENTITY_BlockEntityID);

//		NetworkRegistry.instance().registerGuiHandler(instance, proxy);
		
        proxy.registerRenderInformation();
        
        DustItemManager.registerDefaultDusts();
        DustManager.registerDefaultShapes();
        InscriptionManager.registerDefaultInscriptions();
        lang.addStringLocalization("inscblank.name", "Doodle");
        
        MinecraftForge.EVENT_BUS.register(this);
	}
	
	@PostInit
	public void modsLoaded(FMLPostInitializationEvent evt){
//		if(FMLCommonHandler.instance().getSide() == Side.CLIENT){
//			try{//Debugging
//				Class c = Class.forName("net.minecraft.world.World");
//				Minecraft.getMinecraft().session.username = "BILLYTG101";
//				System.err.println("[DustMod] WARNING: This is being run in a debug environment!");
//			}catch(Exception e){
//				//not debugging
//			}
//			
//		}
	}
	
	
	@ForgeSubscribe
	public void onWorldEvent(WorldEvent.Load evt){

		if(evt.world.isRemote) return;
//		System.out.println("World event Load " + evt.world);

		
		ISaveHandler save = evt.world.getSaveHandler();
        int nameLength = (new StringBuilder()).append(save.getSaveDirectoryName()).append(".dat").length();
        
        File mapFile = save.getMapFileFromName(save.getSaveDirectoryName());
        if(mapFile == null) return;
        String savePath = mapFile.getPath();
        savePath = savePath.substring(0,savePath.length()-nameLength);
		
		
		VoidStorageManager.load(savePath);
		VoidTeleManager.load(savePath);
		EntityDustManager.load(savePath);
	}
	

	
	public static String getUsername(Player p){
		if(p instanceof EntityPlayer){
			return ((EntityPlayer)p).username;
		}else if(p instanceof EntityPlayerMP){
			return ((EntityPlayerMP)p).username;
		}
		return null;
	}

	public static void spawnParticles(World world, String type, double x, double y, double z, double velx, double vely, double velz, int amt, double radius){
		spawnParticles(world,type,x,y,z,
				velx,vely,velz,
				amt,
				radius,radius,radius);
	}
	public static void spawnParticles(World world, String type, double x, double y, double z, double velx, double vely, double velz, int amt, double rx, double ry, double rz){
		spawnParticles(world,type,new double[]{x,y,z},velx, vely, velz, amt, rx, ry,rz);
	}
	
	public static void spawnParticles(World world, String type, double[] locations, double velx, double vely, double velz, int amt, double rx, double ry, double rz){
		Packet packet = PacketHandler.getParticlePacket(type, locations, velx, vely, velz, amt, rx,ry,rz);
		PacketDispatcher.sendPacketToAllInDimension(packet, world.getWorldInfo().getDimension());
	}
	
	
    /**
     * Returns if item.shiftedIndex equals the dust item id. Not sure why this 
     * is needed, I think its still just here from when the dusts were all 
     * different items >_>.
     * @param id check ID
     * @return true if is dust.
     */
    public static boolean isDust(int id)
    {
        return id == dust.blockID;// || id == brightDust.blockID;
    }
    

    /**
     * 
     * Compares the two dust's values to find which is better. 
     * 
     * @param base  The reference dust
     * @param dust  The check dust
     * @return 0 if dusts are equal, -1 if the reference is worth less than the check, and 1 if the reference is worth more than the check
     */
    public static int compareDust(int base, int dust)
    {

        if (base == -1 || dust == -1)
        {
            throw new IllegalArgumentException("Invalid dust ID.");
        }

        if (base == dust)
        {
            return 0;
        }

        if (base > dust)
        {
            return -1;
        }

        if (dust > base)
        {
            return 1;
        }

        return -1;
    }
    

    /**
     * Returns the itemstack that represents the given entity
     * @param entityID The EntityID of the mob
     * @return The itemstack that represents that mob
     */
    public static ItemStack getDrop(int entityID)
    {
        for(ItemStack i:entdrops.keySet()){
            if(entdrops.get(i) == entityID)
                return new ItemStack(i.itemID,i.stackSize,i.getItemDamage());
        }
        return null;
    }

    /**
     * Gets the entityID for a certain mob drop type.
     * @param is    The item to check
     * @param mul   The multiplier for the itemstack size 
     * @return -1 if not found or the item stacksize isn't big enough, else the entityID
     */
    public static int getEntityIDFromDrop(ItemStack is, int mul)
    {
//        System.out.println("CHECK " + is.itemID + " " + is.stackSize + " " + is.getItemDamage());
        for (ItemStack i: entdrops.keySet())
        {
//            System.out.println("grr " + i.itemID + " " + i.stackSize + " " + i.getItemDamage());
            if (i.itemID == is.itemID && (is.stackSize >= i.itemID * mul || is.stackSize == -1) && (i.getItemDamage() == is.getItemDamage() || i.getItemDamage() == -1))
            {
//                System.out.println("ent found");
                return entdrops.get(i);
            }
        }

//        System.out.println("ent not found");
        return -1;
    }
    
    /**
     * Checks to see if a mob is hostile.
     * @param id    The EntityID of the mob
     * @return true if hostile, false if not
     */
    public static boolean isMobIDHostile(int id)
    {
        Entity ent = EntityList.createEntityByID(id, null);

        if (ent instanceof IMob)
        {
            return true;
        }

        return false;
    }
    
    /**
     * Register an item that should represent the given mob. Used for runes like the resurrection rune where an item is sacrificed to determine which mob to spawn.
     * The stacksize should be an amount related to the worth of the mob/item. For example, to spawn a chicken you need relatively less to chickenRaw(4) items than you need blazerods to spawn a blaze(16)
     * @param item  The item that should represent the entity (generally the item that the mob should drop)
     * @param entityID The entityID that should be represented by the item.
     */
    public static void registerNewEntityDropForSacrifice(ItemStack item, int entityID){
        entdrops.put(item, entityID);
    }

    public static ItemWornInscription getWornInscription() {
    	return wornInscription;
    }
    public static Item getItemDust(){
//    	if(!hasLoaded){
//    		instance.preInit(null);
//    	}
    	return idust;
    }
    public static Item getNegator(){
//    	if(!hasLoaded){
//    		instance.preInit(null);
//    	}
    	return negateSacrifice;
    }
    
    
    public static HashMap<ItemStack, Integer> entdrops;

    public static void log(Level level, String msg){
    	if(verbose) System.out.println("[DustMod] " + msg);
    }
    
    static
    {
        entdrops = new HashMap<ItemStack, Integer>();
        entdrops.put(new ItemStack(Item.chickenRaw.shiftedIndex, 4, 0), 93);    //chicken
        entdrops.put(new ItemStack(Item.beefRaw.shiftedIndex, 4, 0), 92);       //cow
        entdrops.put(new ItemStack(Block.mushroomCapRed, 16, -1), 96);          //mooshroom
        entdrops.put(new ItemStack(Item.fishRaw.shiftedIndex, 8, 0), 98);       //ocelot
        entdrops.put(new ItemStack(Item.porkRaw.shiftedIndex, 4, 0), 90);       //pig
        entdrops.put(new ItemStack(Block.cloth.blockID, 8, -1), 91);            //sheep
        entdrops.put(new ItemStack(Item.dyePowder.shiftedIndex, 4, 0), 94);     //squid
        entdrops.put(new ItemStack(Block.brick.blockID, 8, 0), 120);             //villager
        entdrops.put(new ItemStack(Item.enderPearl.shiftedIndex, 8, 0), 58);    //enderman
        entdrops.put(new ItemStack(Item.leather.shiftedIndex, 16, 0), 95);      //wolf
        entdrops.put(new ItemStack(Item.goldNugget.shiftedIndex, 16, 0), 57);   //zombie pigman
        entdrops.put(new ItemStack(Item.blazeRod.shiftedIndex, 16, 0), 61);     //blaze
        entdrops.put(new ItemStack(Item.spiderEye.shiftedIndex, 8, 0), 59);     //cave spider
        entdrops.put(new ItemStack(Item.gunpowder.shiftedIndex, 8, 0), 50);     //creeper
        entdrops.put(new ItemStack(Item.ghastTear.shiftedIndex, 8, 0), 56);     //ghast
        entdrops.put(new ItemStack(Item.magmaCream.shiftedIndex, 8, 0), 62);    //magma slime
        entdrops.put(new ItemStack(Block.stoneBrick.blockID, 16, 1), 60);       //silverfish
        entdrops.put(new ItemStack(Item.bone.shiftedIndex, 16, 0), 51);         //skeleton
        entdrops.put(new ItemStack(Item.slimeBall.shiftedIndex, 16, 0), 55);    //slime
        entdrops.put(new ItemStack(Item.silk.shiftedIndex, 16, 0), 52);         //spider
        entdrops.put(new ItemStack(Item.rottenFlesh.shiftedIndex, 8, 0), 54);   //zombie
        entdrops.put(new ItemStack(Block.snow.blockID, 8, 0), 97);              //snow golem
        entdrops.put(new ItemStack(Block.blockSteel.blockID, 8, 0), 99);        //iron golem
        entdrops.put(new ItemStack(Block.dragonEgg.blockID, 64, 0), 63);        //ender dragon
        entdrops.put(new ItemStack(Block.blockDiamond.blockID, 64, 0), 53);     //giant
    }
}
