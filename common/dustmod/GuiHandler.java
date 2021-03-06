package dustmod;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		ItemStack item = player.getCurrentEquippedItem();
		if(item != null && item.itemID == DustMod.inscription.shiftedIndex){
			return new InscriptionGuiContainer(player.inventory, DustMod.inscription.getInventory(item));
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		ItemStack item = player.getCurrentEquippedItem();
		if(item != null && item.itemID == DustMod.inscription.shiftedIndex){
			return new GuiInscription(player, DustMod.inscription.getInventory(item));
		}
		return null;
	}

}
