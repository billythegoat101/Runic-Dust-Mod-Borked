package dustmod;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.network.IGuiHandler;
import dustmod.runes.DEBait;
import dustmod.runes.DEBomb;
import dustmod.runes.DEBounce;
import dustmod.runes.DECage;
import dustmod.runes.DECampFire;
import dustmod.runes.DECompression;
import dustmod.runes.DEDawn;
import dustmod.runes.DEEarthSprite;
import dustmod.runes.DEEggifier;
import dustmod.runes.DEFarm;
import dustmod.runes.DEFireBowEnch;
import dustmod.runes.DEFireRain;
import dustmod.runes.DEFireSprite;
import dustmod.runes.DEFireTrap;
import dustmod.runes.DEFlatten;
import dustmod.runes.DEFog;
import dustmod.runes.DEForcefield;
import dustmod.runes.DEFortuneEnch;
import dustmod.runes.DEHeal;
import dustmod.runes.DEHideout;
import dustmod.runes.DEHunterVision;
import dustmod.runes.DELiftTerrain;
import dustmod.runes.DELightning;
import dustmod.runes.DELillyBridge;
import dustmod.runes.DELoyaltySprite;
import dustmod.runes.DELumberjack;
import dustmod.runes.DELunar;
import dustmod.runes.DEMiniTele;
import dustmod.runes.DEObelisk;
import dustmod.runes.DEPit;
import dustmod.runes.DEPoisonTrap;
import dustmod.runes.DEPowerRelay;
import dustmod.runes.DEResurrection;
import dustmod.runes.DESilkTouchEnch;
import dustmod.runes.DESpawnRecord;
import dustmod.runes.DESpawnTorch;
import dustmod.runes.DESpawnerCollector;
import dustmod.runes.DESpawnerReprog;
import dustmod.runes.DESpeed;
import dustmod.runes.DESpiritTool;
import dustmod.runes.DETeleportation;
import dustmod.runes.DETimeLock;
import dustmod.runes.DEVoid;
import dustmod.runes.DEWall;
import dustmod.runes.DEXP;
import dustmod.runes.DEXPStore;

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

	public boolean isClient() {
		return false;
	}
}
