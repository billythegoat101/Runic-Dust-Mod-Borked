package net.minecraft.src.dustmod;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
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
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.network.IGuiHandler;

public class CommonProxy implements IGuiHandler{

	public int getBlockModel(Block b){
		return -1;
	}
	
	public void registerTileEntityRenderers() {
	}

	public void registerRenderInformation() {
	}

	public void resetPlayerTomePage() {
	}

	public World getClientWorld() {
		return null;
	}


	/**
	 * Check local directory for the tome page for the given shape.
	 * @param shape	Shape to check.
	 */
	public void checkPage(DustShape shape) {

	}

	public boolean placeDustWithTome(ItemStack itemstack, EntityPlayer p,
			World world, int i, int j, int k, int l) {
		return false;
	}

	public void openTomeGUI(ItemStack itemstack, EntityPlayer p) {
	}

	public void registerEventHandlers() {
	}


	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
//		Object rtn = new GuiTome(player.getCurrentEquippedItem());
		return  null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
//		Object rtn = new GuiTome(player.getCurrentEquippedItem());
		return  null;
	}
}
