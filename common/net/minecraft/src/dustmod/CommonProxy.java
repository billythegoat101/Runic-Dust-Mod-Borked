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
import net.minecraft.src.dustmod.events.DEBait;
import net.minecraft.src.dustmod.events.DEBomb;
import net.minecraft.src.dustmod.events.DEBounce;
import net.minecraft.src.dustmod.events.DECage;
import net.minecraft.src.dustmod.events.DECampFire;
import net.minecraft.src.dustmod.events.DECompression;
import net.minecraft.src.dustmod.events.DEDawn;
import net.minecraft.src.dustmod.events.DEEarthSprite;
import net.minecraft.src.dustmod.events.DEEggifier;
import net.minecraft.src.dustmod.events.DEFarm;
import net.minecraft.src.dustmod.events.DEFireBowEnch;
import net.minecraft.src.dustmod.events.DEFireRain;
import net.minecraft.src.dustmod.events.DEFireSprite;
import net.minecraft.src.dustmod.events.DEFireTrap;
import net.minecraft.src.dustmod.events.DEFlatten;
import net.minecraft.src.dustmod.events.DEFog;
import net.minecraft.src.dustmod.events.DEForcefield;
import net.minecraft.src.dustmod.events.DEFortuneEnch;
import net.minecraft.src.dustmod.events.DEHeal;
import net.minecraft.src.dustmod.events.DEHideout;
import net.minecraft.src.dustmod.events.DEHunterVision;
import net.minecraft.src.dustmod.events.DELiftTerrain;
import net.minecraft.src.dustmod.events.DELightning;
import net.minecraft.src.dustmod.events.DELillyBridge;
import net.minecraft.src.dustmod.events.DELoyaltySprite;
import net.minecraft.src.dustmod.events.DELumberjack;
import net.minecraft.src.dustmod.events.DELunar;
import net.minecraft.src.dustmod.events.DEMiniTele;
import net.minecraft.src.dustmod.events.DEObelisk;
import net.minecraft.src.dustmod.events.DEPit;
import net.minecraft.src.dustmod.events.DEPoisonTrap;
import net.minecraft.src.dustmod.events.DEPowerRelay;
import net.minecraft.src.dustmod.events.DEResurrection;
import net.minecraft.src.dustmod.events.DESilkTouchEnch;
import net.minecraft.src.dustmod.events.DESpawnRecord;
import net.minecraft.src.dustmod.events.DESpawnTorch;
import net.minecraft.src.dustmod.events.DESpawnerCollector;
import net.minecraft.src.dustmod.events.DESpawnerReprog;
import net.minecraft.src.dustmod.events.DESpeed;
import net.minecraft.src.dustmod.events.DESpiritTool;
import net.minecraft.src.dustmod.events.DETeleportation;
import net.minecraft.src.dustmod.events.DETimeLock;
import net.minecraft.src.dustmod.events.DEVoid;
import net.minecraft.src.dustmod.events.DEWall;
import net.minecraft.src.dustmod.events.DEXP;
import net.minecraft.src.dustmod.events.DEXPStore;
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
