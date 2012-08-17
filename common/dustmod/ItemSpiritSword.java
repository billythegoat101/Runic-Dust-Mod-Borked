/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dustmod;

import net.minecraft.src.Enchantment;
import net.minecraft.src.Entity;
import net.minecraft.src.EnumRarity;
import net.minecraft.src.EnumToolMaterial;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ItemSword;
import net.minecraft.src.World;

/**
 *
 * @author billythegoat101
 */
public class ItemSpiritSword extends ItemSword
{
    public ItemSpiritSword(int i)
    {
        super(i, EnumToolMaterial.EMERALD);
        setMaxDamage(131);
        //[non-forge]
//        this.setIconIndex(ModLoader.addOverride("/gui/items.png", mod_DustMod.path + "/spiritsword.png"));
        //[forge]
        this.setIconCoord(1,1);
        this.setTextureFile(DustMod.path + "/dustItems.png");
    }

    public EnumRarity func_40398_f(ItemStack itemstack)
    {
        return EnumRarity.epic;
    }

    @Override
    public void onUpdate(ItemStack itemstack, World world, Entity entity, int i, boolean flag)
    {
        if (!itemstack.isItemEnchanted())
        {
            itemstack.addEnchantment(Enchantment.knockback, 10);
            itemstack.addEnchantment(Enchantment.smite, 5);
        }

//        ItemStack curr = ((EntityPlayer)entity).getCurrentEquippedItem();
//        if(curr != null && curr.itemID == itemstack.itemID){
//            System.out.println("durr?");
////            RenderEntityDust red = new RenderEntityDust();
//            EntityDust ent = new EntityDust(world);
//            ent.renderStar = true;
//            ent.ticksExisted = entity.ticksExisted;
//            float rot = entity.rotationYaw*(float)Math.PI/180F + 5F*(float)Math.PI/6F;
//            ent.setPosition(entity.posX + MathHelper.cos(rot), entity.posY + ent.yOffset, entity.posZ + MathHelper.sin(rot));
//            ent.setVelocity(entity.motionX, entity.motionY, entity.motionZ);
//            ent.lifetime = 1;
//            world.spawnEntityInWorld(ent);
//        }
        super.onUpdate(itemstack, world, entity, i, flag);
    }

    public int getDamageVsEntity(Entity entity)
    {
        return 12;
    }
}
