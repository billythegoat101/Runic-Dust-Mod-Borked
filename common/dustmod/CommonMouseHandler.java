package dustmod;

import java.util.HashMap;

import net.minecraft.server.MinecraftServer;
import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;
import cpw.mods.fml.common.network.Player;

public class CommonMouseHandler {

	public HashMap<String,boolean[]> buttonsPressed;
	
	public CommonMouseHandler(){
		buttonsPressed = new HashMap<String, boolean[]>();
	}
	
	public void checkPlayer(Player p){
		buttonsPressed.put(DustMod.getUsername(p), new boolean[3]);
	}
	
	public void tick(){
//		System.out.println("first tick?");
		for(String username: buttonsPressed.keySet()){
//			System.out.println("rawr");
			Player p = (Player)MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(username);
			boolean[] buttons = buttonsPressed.get(username);
			DustMod.inscriptionManager.tick(p, buttons);
		}
	}
	public void setKey(Player p, int key, boolean pressed){
		String un = DustMod.getUsername(p);
		if(!buttonsPressed.containsKey(un)) checkPlayer(p);
		buttonsPressed.get(un)[key] = pressed;
	}
}
