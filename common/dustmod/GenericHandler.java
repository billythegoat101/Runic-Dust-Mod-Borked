package dustmod;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import cpw.mods.fml.common.ICraftingHandler;
import cpw.mods.fml.common.network.Player;

public class GenericHandler implements ICraftingHandler{

	public static final GenericHandler instance = new GenericHandler(); 
	
	@Override
	public void onCrafting(EntityPlayer player, ItemStack item,
			IInventory craftMatrix) {
		ItemStack pouch = null;
		boolean hasPouch = false;
		for(int i = 0; i < craftMatrix.getSizeInventory(); i++){
			ItemStack inv = craftMatrix.getStackInSlot(i);
			if(inv != null && inv.itemID == DustMod.pouch.shiftedIndex){
				pouch = inv;
				hasPouch = true;
			}
		}
		
		if(hasPouch && (item.itemID == DustMod.idust.shiftedIndex || item.itemID == DustMod.ink.shiftedIndex)){
			ItemPouch.subtractDust(pouch, 1);
			DustMod.pouch.setContainerItemstack(pouch);
		}
		
//		else if(hasPouch && item.itemID == DustMod.pouch.shiftedIndex){
//			DustMod.pouch.setContainerItem(null);
//			DustMod.pouch.setContainerItemstack(null);
//			ItemPouch.setAmount(item, ItemPouch.getDustAmount(pouch) + 1);
//		}

		if(hasPouch){
			int dust = ItemPouch.getValue(pouch);
			if(ItemPouch.getDustAmount(pouch) == 0){
				pouch.setItemDamage(dust*2);
			}else{
				pouch.setItemDamage(dust*2+1);
			}
		}
	}

	@Override
	public void onSmelting(EntityPlayer player, ItemStack item) {
	}

	
	@ForgeSubscribe
	public void onItemPickup(EntityItemPickupEvent evt){
		EntityPlayer player = evt.entityPlayer;
		ItemStack item = evt.item./*item*/func_92014_d();
		
		int dust = item.getItemDamage();
		if(item.itemID == DustMod.idust.shiftedIndex && 
				(player.inventory.hasItemStack(new ItemStack(DustMod.pouch.shiftedIndex, 1, dust * 2)) 
				|| player.inventory.hasItemStack(new ItemStack(DustMod.pouch.shiftedIndex, 1, dust * 2 +1)))){
			InventoryPlayer inv = player.inventory;
			for(int i = 0; i < inv.getSizeInventory(); i++){
				ItemStack check = inv.getStackInSlot(i);
				if(check != null && check.itemID == DustMod.pouch.shiftedIndex && (check.getItemDamage() == dust*2 || check.getItemDamage() == dust*2+1)){
					int left = ItemPouch.addDust(check, item.stackSize);
					item.stackSize = left;
				}
			}
		}
		
		evt.item./*item*/func_92013_a(InscriptionManager.onItemPickup(player, item));
	}
	
	
	@ForgeSubscribe
	public void onLivingUpdate(LivingUpdateEvent evt){

		Entity ent = evt.entityLiving;
    	if(ent instanceof EntityPlayer){
    		EntityPlayer p = (EntityPlayer) ent;
    		ItemStack item = p.inventory.getStackInSlot(38);

    		if(item != null && item.itemID == DustMod.wornInscription.shiftedIndex){	
    			boolean[] buttons = DustMod.keyHandler.getButtons(p.username); 
    			InscriptionManager.tick((Player)p, buttons, p.inventory.getStackInSlot(38));
    		}
    	}
	}
}
