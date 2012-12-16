package dustmoddeco;

import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import dustexample.DustExample;
import dustmod.DustItemManager;
import dustmod.DustMod;

//import cpw.mods.fml.common.Mod.Instance;

@Mod(modid = "DustDeco", name = "Dust mod Decorative version", version = "2.1", dependencies="after:DustMod")
@NetworkMod(clientSideRequired=false, serverSideRequired=false)
public class DustDeco {

	public static final int[] colors = new int[]{
		0x000000,//0x191919, 	//0
		0xCC4C4C,				//1
		0x667F33,				//2
		0x7F664C,				//3
		0x3366CC,				//4
		0xB266E5,				//5
		0x4C99B2,				//6
		0x999999,				//7
		0x4C4C4C,				//8
		0xF2B2CC,				//9
		0x7FCC19,				//10
		0xE5E533,				//11
		0x99B2F2,				//12
		0xE57FD8,				//13
		0xF2B233,				//14
		0xFFFFFF				//15
	};
	public static final int[] floorcolors = new int[]{
		0x000000,//0x191919, 	//0
		0xFF0000,//0xCC4C4C,	//1
		0x667F33,				//2
		0x826A4C,				//3
		0x00B7FF,//0x3366CC,	//4
		0xB266E5,				//5
		0x4C99B2,				//6
		0xb9b9b9,				//7
		0x666464,//0x4C4C4C,	//8
		0xF9B2CC,				//9
		0x7FCC19,				//10
		0xE5E533,				//11
		0x99B2F2,				//12
		0xE57FD8,				//13
		0xF7B233,				//14
		0xFFFFFF				//15
	};
	
    @Instance("DustDeco")
    public static DustDeco instance;
    
	@PostInit
	public void postInit(FMLPostInitializationEvent evt){
		registerDusts();
		registerRunes();
	}
	
    
	public void registerDusts() {
		
		DustItemManager.registerDust(10,"Black", 		"blkdust", 	colors[0], 	colors[0],  floorcolors[0]);
		DustItemManager.registerDust(11,"Red", 			"reddust", 	colors[1], 	colors[1],  floorcolors[1]);
		DustItemManager.registerDust(12,"Green", 		"grndust", 	colors[2], 	colors[2],  floorcolors[2]);
		DustItemManager.registerDust(13,"Brown", 		"bwndust", 	colors[3], 	colors[3],  floorcolors[3]);
		DustItemManager.registerDust(14,"Blue", 		"bludust", 	colors[4], 	colors[4],  floorcolors[4]);
		DustItemManager.registerDust(15,"Purple",	 	"prldust", 	colors[5], 	colors[5],  floorcolors[5]);
		DustItemManager.registerDust(16,"Cyan", 		"cyndust", 	colors[6], 	colors[6],  floorcolors[6]);
		DustItemManager.registerDust(17,"Light Gray",	"lgrdust", 	colors[7], 	colors[7],  floorcolors[7]);
		DustItemManager.registerDust(18,"Gray", 		"grydust", 	colors[8], 	colors[8],  floorcolors[8]);
		DustItemManager.registerDust(19,"Pink", 		"pnkdust",	colors[9], 	colors[9],  floorcolors[9]);
		DustItemManager.registerDust(20,"Lime", 		"lmedust", 	colors[10], colors[10], floorcolors[10]);
		DustItemManager.registerDust(21,"Yellow", 		"ylwdust", 	colors[11], colors[11], floorcolors[11]);
		DustItemManager.registerDust(22,"Light Blue", 	"lbldust", 	colors[12], colors[12], floorcolors[12]);
		DustItemManager.registerDust(23,"Magenta", 		"magdust", 	colors[13], colors[13], floorcolors[13]);
		DustItemManager.registerDust(24,"Orange", 		"orndust", 	colors[14], colors[14], floorcolors[14]);
		DustItemManager.registerDust(25,"White", 		"whtdust", 	colors[15], colors[15], floorcolors[15]);
    	
		for(int i = 0; i < 16; i++){
			GameRegistry.addShapelessRecipe(new ItemStack(DustMod.getItemDust(),4,i+10), new ItemStack(Item.dyePowder,1,i), new ItemStack(Block.sand,1));
		}
		
	}
	
	public void registerRunes() {
		//No runes here ;)
	}
	
}
