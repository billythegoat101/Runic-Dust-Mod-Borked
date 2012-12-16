package dustmod;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.src.Block;
import net.minecraft.src.DamageSource;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class InscriptionManager {
	//
	// public void regsterInscriptionEvent(InscriptionShape shape,
	// InscriptionEvent event){
	//
	// }

	public static ArrayList<InscriptionEvent> events = new ArrayList<InscriptionEvent>();
	public static ArrayList<InscriptionEvent> eventsRemote = new ArrayList<InscriptionEvent>();

	public static HashMap<String, ItemStack> lastArmor = new HashMap<String, ItemStack>();

    public static Configuration config;
	public static void registerInscriptionEvent(InscriptionEvent evt) {
		if (getEvent(evt.id) != null) {
			throw new IllegalArgumentException("Inscription ID already taken! "
					+ evt.idName);
		}
		events.add(evt);
		
		System.out.println("Registering inscription " + evt.idName);
		if(config == null){
            config = new Configuration(DustMod.suggestedConfig);
            config.load();
            config.addCustomCategoryComment("INSCRIPTIONS", "Allow specific inscriptions to be used. Options: ALL, NONE, OPS");
            config.addCustomCategoryComment("RUNES", "Allow specific runes to be used. Options: ALL, NONE, OPS");
        }
            if (!evt.secret)
            {
            	String permission = config.get( "INSCRIPTIONS", "Allow-" + evt.getInscriptionName().replace(' ', '_'), evt.permission).value.toUpperCase();
            	
            	if(permission.equals("ALL") || permission.equals("NONE") || permission.equals("OPS")){
            		evt.permission = permission;
            	}else
            		evt.permission = "NONE";
        		if(!evt.permission.equals("ALL")){
        			System.out.println("Inscription permission for " + evt.idName + " set to " + evt.permission);
        		}
            }

        config.save();
	}

	public static void registerRemoteInscriptionEvent(InscriptionEvent evt) {
		eventsRemote.add(evt);
		LanguageRegistry.instance().addStringLocalization(
				"insc." + evt.idName + ".name", "en_US",
				evt.properName + " Inscription");
		DustItemManager.reloadLanguage();
		DustMod.proxy.checkInscriptionPage(evt);
		evt.isRemote = true;
	}

	public static void resetRemoteInscriptions() {
		eventsRemote = new ArrayList<InscriptionEvent>();
	}

	public static void tick(Player p, boolean[] buttons, ItemStack item) {
		InscriptionEvent event = getEvent(p);
		InscriptionEvent last = getEvent(lastArmor.get(DustMod.getUsername(p)));
//		ItemStack item = ((EntityPlayer) p).inventory.getStackInSlot(38);
//		System.out.println("yo wtf " + DustMod.getUsername(p) + " " + event + " " + last);
		if (event != null) {
			if (last != event) {
				event.onEquip((EntityPlayer) p, item);
			} else {
				event.onUpdate((EntityPlayer) p, item, buttons);
			}
		}

		if (item == null && last != null) {
			last.onRemoval((EntityPlayer) p, lastArmor.get(p));
			lastArmor.put(DustMod.getUsername(p), null);
		}

		lastArmor.put(DustMod.getUsername(p), item);
	}

	public static InscriptionEvent getEvent(Player p) {
		EntityPlayer ep = (EntityPlayer) p;
		ItemStack item = ep.inventory.getStackInSlot(38);
		if (item == null || item.itemID != DustMod.getWornInscription().shiftedIndex)
			return null;
		if (item.getItemDamage() >= item.getMaxDamage() - 1)
			return null;
		return getEvent(item);
	}

	public static InscriptionEvent getEvent(ItemStack item) {
		if (item != null && item.hasTagCompound()) {
			NBTTagCompound tag = item.getTagCompound();
			if (tag.hasKey("eventID")) {
				return getEvent(tag.getInteger("eventID"));
			} else {
				int[][] ink;
				ink = ItemInscription.getDesign(item);
				if (ink == null) {
					return null;
				}
				InscriptionEvent event = null;
				for (InscriptionEvent ievt : events) {
					if (event != null)
						break;
					int[][] design = ievt.referenceDesign;
					for (int i = 0; i <= ink.length - design.length
							&& event == null; i++) {
						for (int j = 0; j <= ink[0].length - design[0].length; j++) {
							boolean found = true;
							for (int x = 0; x < design.length && found; x++) {
								for (int y = 0; y < design[0].length; y++) {
									if (design[x][y] != ink[i + x][j + y]) {
										found = false;
										break;
									}
								}
							}
							if (found) {
								event = ievt;
								break;
							}
						}
					}
				}

				if (event != null) {
					System.out.println("Inscription Identified: "
							+ event.idName);
					tag.setInteger("eventID", event.id);
					return event;
				}
			}
		}
		return null;
	}

	public static InscriptionEvent getEventInOrder(int ind) {
		return getEvents().get(ind);
	}

	public static InscriptionEvent getEvent(int id) {
		for (InscriptionEvent evt : events) {
			if (evt.id == id)
				return evt;
		}
		return null;
	}

	public static ArrayList<InscriptionEvent> getEvents() {
		if (DustMod.proxy.isClient())
			return eventsRemote;
		return events;
	}

	public static int getArmor(EntityPlayer player, ItemStack item) {
		InscriptionEvent event = getEvent(item);
		if (event == null)
			return 0;
		return event.getArmorPoints(player, item);
	}

	public static void onDamage(EntityLiving entity, ItemStack item,
			DamageSource source, int damage) {

		InscriptionEvent event = getEvent(item);
		if (event == null)
			return;
		event.onDamage(entity, item, source, damage);
	}

	public static int getPreventedDamage(EntityLiving entity, ItemStack item,
			DamageSource source, int damage) {

		InscriptionEvent event = getEvent(item);
//		System.out.println("Hey wtf " + event);
		if (event == null)
			return damage;
		return event.getPreventedDamage(entity, item, source, damage);
	}

	public static void onEquip(EntityPlayer player, ItemStack item) {
		InscriptionEvent event = getEvent(item);
		if (event == null)
			return;
		event.onEquip(player, item);
	}

	public static void onRemoval(EntityPlayer player, ItemStack item) {
		InscriptionEvent event = getEvent(item);
		if (event == null)
			return;
		event.onRemoval(player, item);
	}

	public static void onCreate(EntityPlayer player, ItemStack item) {
		InscriptionEvent event = getEvent(item);
		if (event == null)
			return;
		event.onCreate(player, item);
	}

	public static ItemStack onItemPickup(EntityPlayer player, ItemStack item) {
		Player p = (Player) player;
		InscriptionEvent event = getEvent(p);
		InscriptionEvent last = getEvent(lastArmor.get(p));
		ItemStack insc = ((EntityPlayer) p).inventory.getStackInSlot(38);
		if (event == null)
			return item;
		return event.onItemPickup(player, insc, item);
	}

	public static void shield(EntityPlayer ep) {
		if (ep.getCurrentEquippedItem() == null && ep.isSneaking()) {
			float ticks = 1F;
			double distance = 64D;
			Vec3 pos = ep.worldObj.getWorldVec3Pool().getVecFromPool(ep.posX, ep.posY,
					ep.posZ);

			pos.yCoord += ep.getEyeHeight();
			Vec3 look = ep.getLook(ticks);
			Vec3 result = pos.addVector(look.xCoord * distance, look.yCoord
					* distance, look.zCoord * distance);

			if (look.yCoord < -0.95) {
//				System.out.println("Shield");
				int x = (int) Math.round(ep.posX);
				int y = (int) Math.round(ep.posY);
				int z = (int) Math.round(ep.posZ);

				World world = ep.worldObj;

				int r = 2;
				for (int i = -r; i <= r; i++) {
					for (int k = -r; k <= r; k++) {
						if (i == -r || k == -r || i == r || k == r) {
							world.setBlockWithNotify(x + i, y, z + k,
									Block.dirt.blockID);
						}
					}
				}
				r++;
				for (int i = -r; i <= r; i++) {
					for (int k = -r; k <= r; k++) {
						if (i == -r || k == -r || i == r || k == r) {
							world.setBlockWithNotify(x + i, y, z + k,
									Block.dirt.blockID);
							world.setBlockWithNotify(x + i, y + 1, z + k,
									Block.dirt.blockID);
						}
					}
				}
				r++;
				for (int i = -r; i <= r; i++) {
					for (int k = -r; k <= r; k++) {
						if (i == -r || k == -r || i == r || k == r) {
							world.setBlockWithNotify(x + i, y - 1, z + k, 0);
						}
					}
				}
			}

			// System.out.println(look.xCoord + " " + look.yCoord + " "
			// + look.zCoord);
		}
	}
	

	public static void registerDefaultInscriptions() {
		// None yet! See the test pack.

	}

	public static boolean isEmpty() {
		return eventsRemote.isEmpty();
	}

}
