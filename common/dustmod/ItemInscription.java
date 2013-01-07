package dustmod;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemInscription extends Item {

	protected ItemInscription(int par1) {
		super(par1);
		this.setMaxStackSize(1);
		this.setIconIndex(37);
		this.setMaxDamage(181);
	}

	
	public InventoryInscription getInventory(ItemStack item){
		return new InventoryInscription(item);
	}
	@Override
	@SideOnly(Side.CLIENT)
	public int getIconFromDamage(int meta) {
		// TODO Auto-generated method stub
		return meta == 0 ? 37 : 38;
	}
	@Override
	public int getIconIndex(ItemStack stack, int renderPass,
			EntityPlayer player, ItemStack usingItem, int useRemaining) {

		boolean isDried = isDried(stack);
		int damage = stack.getItemDamage();
		if(damage != 0 || isDried) {
			return 38;
		}
		else {
			return 37;
		}

	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack item, World world,
			EntityPlayer player) {
		// TODO Auto-generated method stub
		player.openGui(DustMod.instance, 0, world, 0,0,0);

		return super.onItemRightClick(item, world, player);
	}
	
	public static int[][] getDesign(ItemStack item){
		if(item == null || !item.hasTagCompound()) return null;
		
		int[][] rtn = new int[16][16];

		NBTTagCompound tag = item.getTagCompound();
		for(int i = 0; i < 16; i++){
			for(int j = 0; j < 16; j++){
				rtn[i][j] = tag.getInteger(i + "," + j);
			}
		}
		
		return rtn;
	}
	public static boolean isDesignEmpty(ItemStack item){
		if(item == null || !item.hasTagCompound()) return false;
		NBTTagCompound tag = item.getTagCompound();
		for(int i = 0; i < 16; i++){
			for(int j = 0; j < 16; j++){
				int check = tag.getInteger(i + "," + j);
				if(check != 0) return false;
			}
		}
		
		return true;
	}
	
	@Override
	public void onUpdate(ItemStack item, World world,
			Entity ent, int par4, boolean par5) {
		int meta = item.getItemDamage();
		if(isDried(item)) return;
		if(meta > 0 && world.getWorldTime()%20 == 0){
			if(isDesignEmpty(item)){
				item.setItemDamage(0);
			}else {
				if(!world.isRaining()){
					int amt = 1;
					int x = (int)ent.posX;
					int z = (int)ent.posZ;
			        if (world.getBiomeGenForCoords(x, z).getFloatTemperature() > 1.0F)
			        {
			            amt = 2;
			        }
					if(!DustMod.debug) item.setItemDamage(meta+amt);
				}
			}
		}
		if(meta >= 181){
			InscriptionEvent event = InscriptionManager.getEvent(item);
			int id = -1;
			if(event != null){
				id = event.id;
				InscriptionManager.onCreate((EntityPlayer)ent, item);
				item.itemID = DustMod.getWornInscription().shiftedIndex;
				item.setItemDamage(999);
			}else{
				setDried(item);
			}
		}
		super.onUpdate(item, world, ent, par4, par5);
	}
	
	@Override
	public boolean getShareTag() {
		return true;
	}
	
	public boolean isDried(ItemStack item){
		if(item.hasTagCompound()){
			NBTTagCompound tag = item.getTagCompound();
			if(tag.hasKey("dried") && tag.getBoolean("dried")) return true;
			else {
				tag.setBoolean("dried",false);
				return false;
			}
		}return false;
	}
	public void setDried(ItemStack item){
		item.getTagCompound().setBoolean("dried", true);
		item.setItemDamage(-1);
	}


    public String getItemNameIS(ItemStack itemstack)
    {
    	boolean isDried = isDried(itemstack);
    	int damage = itemstack.getItemDamage();
    	if(damage == 0) return "emptyinsc";
    	else if(isDried) return "driedinsc";
    	else return "dryinginsc";
    }

    public String getLocalItemName(ItemStack itemstack)
    {
        return getItemNameIS(itemstack);
    }
}
