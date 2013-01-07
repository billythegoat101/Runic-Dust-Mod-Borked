package dustmod;

import java.util.HashMap;

import net.minecraft.server.MinecraftServer;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.Player;

public class CommonMouseHandler {

	public HashMap<String,boolean[]> buttonsPressed;
	
	public CommonMouseHandler(){
		buttonsPressed = new HashMap<String, boolean[]>();
	}
	
	public void checkPlayer(Player p){
		buttonsPressed.put(DustMod.getUsername(p), new boolean[3]);
	}
	
	public boolean[] getButtons(String player){
		boolean[] rtn = buttonsPressed.get(player);
		if(rtn == null) rtn = new boolean[3];
		return rtn;
	}
	public void setKey(Player p, int key, boolean pressed){
		String un = DustMod.getUsername(p);
		if(!buttonsPressed.containsKey(un)) checkPlayer(p);
		buttonsPressed.get(un)[key] = pressed;
	}
}
