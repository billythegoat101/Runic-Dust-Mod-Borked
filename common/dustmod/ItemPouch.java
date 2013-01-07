package dustmod;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemReed;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemPouch extends ItemReed {

	public static final int max = 6400;

    private int blockID;
    private ItemStack container = null;
	public ItemPouch(int par1, Block block) {
		super(par1, block);
        blockID = block.blockID;
		this.setTextureFile(DustMod.path + "/dustItems.png");
		this.hasSubtypes = true;
		this.setMaxStackSize(1);
	}

	@Override
    public boolean onItemUse(ItemStack item, EntityPlayer p, World world, int i, int j, int k, int face, float x, float y, float z)
    {
        int var11 = world.getBlockId(i, j, k);

        if (var11 == Block.snow.blockID)
        {
            face = 1;
        }
        else if (var11 != Block.vine.blockID && var11 != Block.tallGrass.blockID && var11 != Block.deadBush.blockID)
        {
            if (face == 0)
            {
                --j;
            }

            if (face == 1)
            {
                ++j;
            }

            if (face == 2)
            {
                --k;
            }

            if (face == 3)
            {
                ++k;
            }

            if (face == 4)
            {
                --i;
            }

            if (face == 5)
            {
                ++i;
            }
        }

        if (!p.canPlayerEdit(i, j, k, 7, item))
        {
            return false;
        }
        else if (getDustAmount(item) <= 0)
        {
            return false;
        }
        else
        {
            if (world.canPlaceEntityOnSide(this.blockID, i, j, k, false, face, (Entity)null))
            {
                Block var12 = Block.blocksList[this.blockID];
                int var13 = var12.func_85104_a(world, i, j, k, face, x, y, z, 0);

                if (world.setBlockWithNotify(i, j, k, this.blockID))
                {
                    if (world.getBlockId(i, j, k) == this.blockID)
                    {
                        Block.blocksList[this.blockID].onBlockPlacedBy(world, i, j, k, p);
                        Block.blocksList[this.blockID].func_85105_g(world, i, j, k, var13);
                    }
                    DustMod.dust.onBlockActivated(world, i, j, k, p, face, x, y, z);

                    world.playSoundEffect((double)((float)i + 0.5F), (double)((float)j + 0.5F), (double)((float)k + 0.5F), var12.stepSound.getStepSound(), (var12.stepSound.getVolume() + 1.0F) / 6.0F, var12.stepSound.getPitch() * 0.99F);
//                    if(!p.capabilities.isCreativeMode)subtractDust(item,1);
                }
            }

            return true;
        }
    }
    


    @Override
    public String getItemNameIS(ItemStack itemstack)
    {
		int dust = getValue(itemstack);
    	String id = DustItemManager.getIDS()[dust];
    	if(id != null) return "pouch." + DustItemManager.idsRemote[dust];
//    	System.out.println("Dammit " + dust + " " + itemstack.getItemDamage());
        return "pouchblank";
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack item,
    		EntityPlayer player, List list, boolean flag) {
    	super.addInformation(item, player, list, flag);
    	int amt = ItemPouch.getDustAmount(item);
    	if(amt != 0) list.add("Contains " + amt + " dust");
    }

    @Override
    public String getLocalItemName(ItemStack itemstack)
    {
    	return getItemNameIS(itemstack);
    }
    
    @SideOnly(Side.CLIENT)

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    public void getSubItems(int par1, CreativeTabs par2CreativeTabs, List par3List)
    {
        for (int i = 5; i < 1000; ++i) //i > 4 for migration from old system
        {
        	if(DustItemManager.getColors()[i] != null){
                par3List.add(new ItemStack(par1, 1, i*2));
        	}
        }
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int pass) {
    	int meta = stack.getItemDamage();
    	meta = meta >> 1;
    	if(pass == 0) return super.getColorFromItemStack(stack, pass);
    	return pass == 1 ? DustItemManager.getPrimaryColor(meta) : DustItemManager.getSecondaryColor(meta);
    }


    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }

    @SideOnly(Side.CLIENT)

    /**
     * Gets an icon index based on an item's damage value and the given render pass
     */
    public int getIconFromDamageForRenderPass(int meta, int rend)
    {
    	if(rend < 3){
    		return 51 + rend*16;
    	}
        return 0;
    }
    

    public void setContainerItemstack(ItemStack item){
    	container = item;
    }

    public boolean doesContainerItemLeaveCraftingGrid(ItemStack par1ItemStack)
    {
        return false;
    }
    
    @Override
    public Item getContainerItem() {
    	return DustMod.pouch;
    }
    @Override
    public boolean hasContainerItem() {
    	return true;
    }
    
    @Override
    public ItemStack getContainerItemStack(ItemStack itemStack) {
    	// TODO Auto-generated method stub
    	return container;
    }
    
    @SideOnly(Side.CLIENT)

    /**
     * allows items to add custom lines of information to the mouseover description
     */
    public void addInformation(ItemStack item, List info)
    {
        if (item.hasTagCompound())
        {
//            NBTTagCompound tag = item.getTagCompound();
//            NBTTagString author = (NBTTagString)tag.getTag("author");
            int amt = getDustAmount(item);
//            if (author != null)
//            {
                info.add("\u00a77 Holding " + amt + " piles.");//String.format(StatCollector.translateToLocalFormatted("book.byAuthor", new Object[] {author.data}), new Object[0]));
//            }
        }
    }
    
    
    public static int getDustAmount(ItemStack pouch){
    	if(pouch.itemID == DustMod.idust.shiftedIndex){
    		return pouch.stackSize;
    	}
    	if(pouch.itemID != DustMod.pouch.shiftedIndex) return -1;
    	
    	if(pouch.getItemDamage() %2 == 0) return 0;
    	
    	if(!pouch.hasTagCompound()){
    		pouch.setTagCompound(new NBTTagCompound());
    	}
    	NBTTagCompound tag = pouch.getTagCompound();
    	return tag.getInteger("dustamount");
    }
    
    public static boolean subtractDust(ItemStack pouch, int sub){
    	if(pouch.itemID == DustMod.idust.shiftedIndex){
    		if(pouch.stackSize >= sub){
    			pouch.stackSize -= sub;
    			return true;
    		} else
    			return false;
    	}
    	if(pouch.itemID != DustMod.pouch.shiftedIndex) return false;
    	
    	if(!pouch.hasTagCompound()){
    		pouch.setTagCompound(new NBTTagCompound());
    	}
    	NBTTagCompound tag = pouch.getTagCompound();

    	int amt = tag.getInteger("dustamount");
    	if(amt >= sub){
    		tag.setInteger("dustamount", amt-sub);
    		int dust = getValue(pouch);
    		if(amt-sub == 0) {
    			pouch.setItemDamage(dust*2);
    		}else{
    			pouch.setItemDamage(dust*2+1);
    		}
    		return true;
    	} else 
    		return false;
    }

    /*
     * @return amount remaining if attempting to add more than pouch can contain
     */
    public static int addDust(ItemStack pouch, int add){
    	if(pouch.itemID == DustMod.idust.shiftedIndex){
			pouch.stackSize += add;
			return 0;
    	}
    	if(pouch.itemID != DustMod.pouch.shiftedIndex) return -1;
    	
    	int rtn = 0;
    	
    	if(!pouch.hasTagCompound()){
    		pouch.setTagCompound(new NBTTagCompound());
    	}
    	NBTTagCompound tag = pouch.getTagCompound();

    	int amt = tag.getInteger("dustamount");
    	if(amt + add > max){
    		rtn = add - (max - amt);
    		add -= rtn;
    	}
		tag.setInteger("dustamount", amt+add);
		int dust = getValue(pouch);
		if(amt + add == 0) {
			pouch.setItemDamage(dust*2);
		}else{
			pouch.setItemDamage(dust*2+1);
		}
		return rtn;
    }
    
    public static void setAmount(ItemStack pouch, int amt){
    	if(pouch.itemID == DustMod.idust.shiftedIndex){
			pouch.stackSize -= amt;
    	}
    	if(pouch.itemID != DustMod.pouch.shiftedIndex) return;
    	
    	if(!pouch.hasTagCompound()){
    		pouch.setTagCompound(new NBTTagCompound());
    	}
    	NBTTagCompound tag = pouch.getTagCompound();
		tag.setInteger("dustamount", amt);
		int dust = getValue(pouch);
		if(amt > 0){
			pouch.setItemDamage(dust*2+1);
		}else{
			pouch.setItemDamage(dust*2);
		}
    }
    
    public static int getValue(ItemStack pouch){
    	int damage = pouch.getItemDamage();
    	return damage >> 1;
    }
    
}
