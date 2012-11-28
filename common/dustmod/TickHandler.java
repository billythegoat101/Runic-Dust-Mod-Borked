package dustmod;

import java.util.EnumSet;

import org.lwjgl.input.Mouse;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class TickHandler implements ITickHandler {

	private boolean mouseDown = false;
	
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		try{
		DustMod.proxy.tickMouseManager();
		DustMod.keyHandler.tick();
		}catch(Exception e){}
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.CLIENT, TickType.SERVER);
	}

	@Override
	public String getLabel() {
		return "DustMod";
	}

}
