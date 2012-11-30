package dustmod;

import net.minecraft.src.EntityClientPlayerMP;
import net.minecraft.src.GuiScreen;

import org.lwjgl.input.Mouse;

import cpw.mods.fml.client.FMLClientHandler;

public class MouseManager {
	
	public boolean[] state;
	
	private MouseManager(){
		state = new boolean[3];
	}
	
	public static MouseManager instance = new MouseManager();
	
	public void onTick(){
		EntityClientPlayerMP player = FMLClientHandler.instance().getClient().thePlayer;
		GuiScreen screen = FMLClientHandler.instance().getClient().currentScreen;
		if(player == null || screen != null) return;
		for(int i = 0; i < state.length; i++){
			boolean s = Mouse.isButtonDown(i);
			
			if(state[i] != s){
				FMLClientHandler.instance().sendPacket(PacketHandler.getMousePacket(i, s));
			}
			
			state[i] = s;
		}
	}
	
}
