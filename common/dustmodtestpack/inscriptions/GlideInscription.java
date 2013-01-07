package dustmodtestpack.inscriptions;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import dustmod.DustEvent;
import dustmod.DustMod;
import dustmod.EntityDust;
import dustmod.InscriptionEvent;
import dustmod.InscriptionManager;

public class GlideInscription extends InscriptionEvent {

	public GlideInscription(int[][] design, String idName, String properName,
			int id) {
		super(design, idName, properName, id);
		this.setAuthor("billythegoat101");
		this.setDescription("Description:\n" +
				"Hold shift while falling to glide and prevent fall damage.");
		this.setNotes("Sacrifice:\n" +
				"TBD");
	}
	
	@Override
	public boolean callSacrifice(DustEvent rune, EntityDust e, ItemStack item) {
		// TODO Auto-generated method stub
		return super.callSacrifice(rune, e, item);
	}
	
	@Override
	public void onUpdate(EntityLiving wearer, ItemStack item, boolean[] buttons) {
		super.onUpdate(wearer, item, buttons);
		EntityPlayer player = (EntityPlayer)wearer;
		player.jumpMovementFactor = 0.02f;
		if(player.motionY < 0 && player.isSneaking() && !player.capabilities.isFlying){
			player.motionY *= 0.7;
			player.fallDistance = 0;
			player.jumpMovementFactor = 0.06F;
		}
	}
}
