package dustmodtestpack.inscriptions;

import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.ItemStack;
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
				"");
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
		EntityPlayerMP player = (EntityPlayerMP)wearer;
//		if(){
			System.out.println("Penis ");
//			wearer.motionY /= 2;
//			System.out.println("rwara w" + wearer.rotationPitch + " " + wearer);
//			player.setPositionAndUpdate(wearer.posX+wearer.motionX, wearer.posY+wearer.motionY, wearer.posZ+wearer.motionZ);
//			System.out.println("rwar2 w" + wearer.rotationPitch + " " + wearer);
//			player.setLocationAndAngles(wearer.posX, wearer.posY + 10, wearer.posZ, wearer.rotationYaw, wearer.rotationPitch);
//			wearer.addVelocity(0, -wearer.motionY/2, 0);
			player.motionY /= 2;
			player.fallDistance = 0;
//			wearer.moveEntity(0, -10, 0);
//			player.capabilities.isFlying = true;
//			player.sendPlayerAbilities();
//			System.out.println("FALL1 " + wearer.motionY + " " + wearer);
//			player.onUpdateEntity();
//			System.out.println("FALL " + wearer.motionY + " " + wearer);
//		}
	}
	
	@ForgeSubscribe
	public void onFall(LivingFallEvent event){
//		System.out.println("Call");
		EntityLiving ent = event.entityLiving;
//		ent.addVelocity(0, 10, 0);
//		ent.setPositionAndUpdate(ent.posX, ent.posY,  ent.posZ);
		ItemStack item = ent.getCurrentArmor(2);
		if(item != null && item.itemID == DustMod.getWornInscription().shiftedIndex
				&& InscriptionManager.getEvent(item) == this){
			if(!ent.onGround && ent.motionY < 0){
				System.out.println("wat");
//				ent.motionY /=2;
			}
		}
	}
}
