package net.minecraft.src.dustmod;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class DustItemManager {
	
	public static DustColor[] colors = new DustColor[1000];
	public static String[] names = new String[1000];
	public static String[] ids = new String[1000];
	
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
		
		colors[value] = new DustColor(primaryColor, secondaryColor, floorColor);
		names[value] = name;
		ids[value] = idName;

		LanguageRegistry.instance().addStringLocalization("tile." + idName + ".name", "en_US", name);
	}
	
	public static int getPrimaryColor(int value){
		if(value <= 0) return 0x8F25A2;
		if(colors[value] == null) return 0;
		return colors[value].primaryColor;
	}
	
	public static int getSecondaryColor(int value){
		if(value <= 0) return 0xDB73ED1;
		if(colors[value] == null) return 0;
		return colors[value].secondaryColor;
	}
	
	public static int getFloorColor(int value){
		if(value <= 0) return 0xCE00E0;
		if(colors[value] == null) return 0;
		return colors[value].floorColor;
	}
	
	public static int[] getFloorColorRGB(int value){
		if(value <= 0) return new int[] {206, 0, 224}; //00CE00E0 variable

		if(colors[value] == null) return new int[]{0,0,0};
		
		int[] rtn = new int[3];
		
		int col = colors[value].floorColor;
		
		rtn[0] = (col & 0xFF0000) >> 16;
		rtn[1] = (col & 0xFF00) >> 8;
		rtn[2] = (col & 0xFF);
		
		return rtn;
	}
	
	public static void reset(){
		colors = new DustColor[1000];
		names = new String[1000];
		ids = new String[1000];
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
	private static class DustColor{
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
