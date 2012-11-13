package dustmodtestpack.inscriptions;

import net.minecraft.src.DamageSource;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumMovingObjectType;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.World;
import dustmod.DustEvent;
import dustmod.DustMod;
import dustmod.EntityDust;
import dustmod.InscriptionEvent;

public class BlinkerInscription extends InscriptionEvent {

	public BlinkerInscription(int[][] design, String idName, String properName,
			int id) {
		super(design, idName, properName, id);
		this.setAuthor("billythegoat101");
		this.setDescription("Description:\n" +
				"Blink like an enderman to whever you click. Shift+RightClick with a bare hand to activate.");
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
		
		if (((EntityPlayer)wearer).getCurrentEquippedItem() == null && wearer.isSneaking() && buttons[1]) {
			int[] target = getClickedBlock(wearer,item);
			if(target != null){
				wearer.setPositionAndUpdate(target[0], target[1]+1, target[2]);
				wearer.fallDistance = 0.0F;
				wearer.attackEntityFrom(DamageSource.fall, 5);
			}
		}
	}
	
	public int[] getClickedBlock(Entity wearer, ItemStack item){
		MovingObjectPosition click = DustMod.wornInscription.getMovingObjectPositionFromPlayer(wearer.worldObj, (EntityPlayer)wearer, false);
		if(click != null && click.typeOfHit == EnumMovingObjectType.TILE){
			int tx = click.blockX;
			int ty = click.blockY;
			int tz = click.blockZ;
			return new int[]{tx,ty,tz};
		}
		return null;
	}

}
