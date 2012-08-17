package dustmod;

import net.minecraft.src.StringTranslate;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class DustItemManager {
	
	public static DustColor[] colors = new DustColor[1000];
	public static String[] names = new String[1000];
	public static String[] ids = new String[1000];
	public static DustColor[] colorsRemote = new DustColor[1000];
	public static String[] namesRemote = new String[1000];
	public static String[] idsRemote = new String[1000];
	
	/**
	 * Register a new dust type to the system.
	 * You'll have to manually set the recipe/method of getting the dust.
	 * The item will just be DustMod.idust with the damage value equal to this passed value parameter. 
	 * 
	 * @param value	Worth of the dust. Bigger number means more worth (999 max)
	 * @param primaryColor	Color of the base of the item dust
	 * @param secondaryColor	Color of the sparkles on the item dust 
	 * @param floorColor	Color of the dust when placed on the ground.
	 */
	public static void registerDust(int value, String name, String idName,  int primaryColor, int secondaryColor, int floorColor){
		if(colors[value] != null){
			throw new IllegalArgumentException("[DustMod] Dust value already taken! " + value);
		}
		
		colors[value] = colorsRemote[value] = new DustColor(primaryColor, secondaryColor, floorColor);
		names[value] = namesRemote[value] = name;
		ids[value] = idsRemote[value] = idName;

		LanguageRegistry.instance().addStringLocalization("tile." + idName + ".name", "en_US", name);
		reloadLanguage();
	}
	protected static void registerRemoteDust(int value, String name, String idName,  int primaryColor, int secondaryColor, int floorColor){
		if(colorsRemote[value] != null){
			throw new IllegalArgumentException("[DustMod] Remote error! Dust value already taken! " + value);
		}
		
//		System.out.println("Register new remote dust " + primaryColor + " " + secondaryColor + " " + floorColor);
		
		colorsRemote[value] = new DustColor(primaryColor, secondaryColor, floorColor);
		namesRemote[value] = name;
		idsRemote[value] = idName;
		
		LanguageRegistry.instance().addStringLocalization("tile." + idName + ".name", "en_US", name);
		reloadLanguage();
	}
	
	public static void reloadLanguage(){
		StringTranslate st = StringTranslate.getInstance();
		String curLan = st.currentLanguage;
		String trick = "ar_SA"; //I pick this one because its the second one I see a hard-coded reference to in StringTranslate >_>
		if(curLan.equals(trick)){
			trick = "en_us"; //in case someone is using ar_SA. Not even 100% sure what that is.
		}
		st.setLanguage(trick);
		st.setLanguage(curLan);
	}
	
	public static String[] getNames(){
		return (DustMod.proxy.isClient() ? namesRemote:names);
	}
	public static String[] getIDS(){
		return (DustMod.proxy.isClient() ? idsRemote:ids);
	}
	public static DustColor[] getColors(){
		return (DustMod.proxy.isClient() ? colorsRemote:colors);
	}
	
	public static int getPrimaryColor(int value){
		if(value <= 0) return 0x8F25A2;
		if(colorsRemote[value] == null) return 0;
		return colorsRemote[value].primaryColor;
	}
	
	public static int getSecondaryColor(int value){
		if(value <= 0) return 0xDB73ED1;
		if(colorsRemote[value] == null) return 0;
		return colorsRemote[value].secondaryColor;
	}
	
	public static int getFloorColor(int value){
		if(value <= 0) return 0xCE00E0;
		if(colorsRemote[value] == null) return 0;
		return colorsRemote[value].floorColor;
	}
	
	public static int[] getFloorColorRGB(int value){
		if(value <= 0) return new int[] {206, 0, 224}; //00CE00E0 variable
		
		if(colorsRemote[value] == null) return new int[]{0,0,0};
		
		int[] rtn = new int[3];
		
		int col = colorsRemote[value].floorColor;
		
		rtn[0] = (col & 0xFF0000) >> 16;
		rtn[1] = (col & 0xFF00) >> 8;
		rtn[2] = (col & 0xFF);
		
		return rtn;
	}
	
	public static void reset(){
//		System.out.println("Reset local dusts");
		colorsRemote = new DustColor[1000];
		namesRemote = new String[1000];
		idsRemote = new String[1000];
	}
	
	public static void registerDefaultDusts(){
		registerDust(1,"PlantDust (old, place or craft to update)", "plantdustold", 0x629B26, 0x8AD041, 0xC2E300);
		registerDust(100,"Plant Runic Dust", "plantdust", 0x629B26, 0x8AD041, 0xC2E300); //Migrating to space out
		
		registerDust(2,"GunDust (old, place or craft to update)", "gundustold",0x696969, 0x979797, 0x666464);
		registerDust(200,"Gunpowder Runic Dust", "gundust",0x696969, 0x979797, 0x666464); //Migrating to space out
		
		registerDust(3,"LapisDust (old, place or craft to update)", "lapisdustold",0x345EC3, 0x5A82E2, 0x0087FF);
		registerDust(300,"Lapis Runic Dust", "lapisdust",0x345EC3, 0x5A82E2, 0x0087FF); //Migrating to space out
		
		registerDust(4,"BlazeDust (old, place or craft to update)", "blazedustold",0xEA8A00, 0xFFFE31, 0xFF6E1E);
		registerDust(400,"Blaze Runic Dust", "blazedust",0xEA8A00, 0xFFFE31, 0xFF6E1E); //Migrating to space out
	}

	@SideOnly(Side.SERVER)
	public static class DustColor{
		public int primaryColor;
		public int secondaryColor;
		public int floorColor;
		public DustColor(int primaryColor, int secondaryColor, int floorColor){
			this.primaryColor = primaryColor;
			this.secondaryColor = secondaryColor;
			this.floorColor = floorColor;
		}
	}
}
