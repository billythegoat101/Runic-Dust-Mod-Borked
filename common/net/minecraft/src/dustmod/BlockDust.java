/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minecraft.src.dustmod;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.DustModBouncer;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityXPOrb;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.TileEntity;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;
import net.minecraftforge.common.ForgeDirection;

/**
 * 
 * @author billythegoat101
 */
public class BlockDust extends BlockContainer {
	// private int itemID=0;
	public BlockDust(int i, int j) {
		super(i, j, Material.circuits);
		// this.itemID = item + 256;
		// this.color = color;
		// wiresProvidePower = true;
		// blocksNeedingUpdate = new HashSet();
		setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
		this.setTextureFile(DustMod.path + "/dustBlocks.png");
		this.blockIndexInTexture = 0;
		this.setHardness(0.2F);
		this.setLightValue(0.45F);
		this.setStepSound(Block.soundGrassFootstep);
		this.setBlockName("dustblock");
		this.setRequiresSelfNotify();
	}

	@Override
	public int getBlockTextureFromSideAndMetadata(int i, int j) {
		return blockIndexInTexture;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i,
			int j, int k) {
		return null;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public void onEntityCollidedWithBlock(World world, int i, int j, int k,
			Entity entity) {
		// if(world.isRemote) return;
		if (entity instanceof EntityItem) {
			EntityItem ei = (EntityItem) entity;
			ei.age = 0;
			EntityPlayer p = world.getClosestPlayerToEntity(ei, 0.6);

			if (p == null) {
				ei.delayBeforeCanPickup = 10;
				return;
			}

			double dist = p.getDistanceToEntity(ei);

//			if (dist < 0.2 && ei.delayBeforeCanPickup > 5) {
//				System.out.println("Drop " + dist);
//				ei.delayBeforeCanPickup = 5;
//			} else {
////				System.out.println("Grab " + dist);
//			}
		}

		if (entity instanceof EntityXPOrb) {
			EntityXPOrb orb = (EntityXPOrb) entity;
			orb.xpOrbAge = 0;
			EntityPlayer p = world.getClosestPlayerToEntity(orb, 3.0);

			if (p == null) {
				orb.setPosition(orb.prevPosX, orb.prevPosY, orb.prevPosZ);
				return;
			}
		}
	}

	@Override
	public void onBlockPlacedBy(World world, int i, int j, int k,
			EntityLiving entityliving) {
		super.onBlockPlacedBy(world, i, j, k, entityliving);
		this.onBlockActivated(world, i, j, k, (EntityPlayer) entityliving, 0,
				0, 0, 0);

		System.out.println("Placed");
		if (((EntityPlayer) entityliving).getCurrentEquippedItem() != null) {
			((EntityPlayer) entityliving).getCurrentEquippedItem().stackSize++;
		}
	}

	@Override
	public boolean canPlaceBlockAt(World world, int i, int j, int k) {
		Block block = Block.blocksList[world.getBlockId(i, j - 1, k)];

		if (block == null) {
			return false;
		} else {
			return world.isBlockSolidOnSide(i, j - 1, k, ForgeDirection.UP);
			// return block.renderAsNormalBlock() || block == Block.glass ||
			// world.isBlockSolidOnSide(i,j,k,0);
		}

		// return world.isBlockNormalCube(i, j - 1, k);
	}

	@Override
	public int getRenderType() {
		return DustMod.proxy.getBlockModel(this);
	}

	@Override
	public int colorMultiplier(IBlockAccess iblockaccess, int i, int j, int k) {
		switch (iblockaccess.getBlockMetadata(i, j, k)) {
		case 0:
			TileEntityDust ted = (TileEntityDust) iblockaccess
					.getBlockTileEntity(i, j, k);

			if (ted == null) {
				return 0xEFEFEF;
			}

			return ted.getRandomDustColor();

		case 1:
			return 0xDD0000;

		case 2:
			return 0xEFEFEF;

		default:
			System.out.println("derp? "
					+ iblockaccess.getBlockMetadata(i, j, k));
			return 0;
		}
	}

	@Override
	public void onNeighborBlockChange(World world, int i, int j, int k, int l) {
		if (world.isRemote) {
			return;
		}

		// if (world.multiplayerWorld) {
		// return;
		// }
		int i1 = world.getBlockMetadata(i, j, k);

		// boolean flag = world.getBlockId(i, j, k) ==
		// 0;//world.canBlockBePlacedAt(blockID, i, j, k, true, i1);
		if (world.getBlockId(i, j - 1, k) == 0
				|| !Block.blocksList[world.getBlockId(i, j - 1, k)].blockMaterial
						.isSolid()) {
			// System.out.println("aww " + i + " " + (j-1) + " " + k + " " + i1
			// + " " + world.getBlockId(i, j-1, k));
			// if (world.getBlockMetadata(i, j, k) == 0) {
			// onBlockRemoval(world, i, j, k);
			// }
			world.setBlockWithNotify(i, j, k, 0);
		} else if (world.isBlockIndirectlyGettingPowered(i, j, k) && i1 == 0) {
			updatePattern(world, i, j, k, null);
			DustModBouncer.notifyBlockChange(world, i, j, k, 0);
		}

		super.onNeighborBlockChange(world, i, j, k, l);
	}

	/**
	 * ejects contained items into the world, and notifies neighbours of an
	 * update, as appropriate
	 */
	@Override
	public boolean removeBlockByPlayer(World world, EntityPlayer player, int i, int j, int k) {
//		if (world.isRemote) {
//			return;
//		}

		if (world.getBlockMetadata(i, j, k) > 0) {
			world.setBlockWithNotify(i, j, k, 0);
			// for(int x = -1; x <= 1; x++)
			// for(int z = -1; z <= 1; z++){
			// if(world.getBlockId(i+x, j, k+z) == blockID &&
			// world.getBlockMetadata(i+x, j, k+z) == 1){
			// world.setBlockWithNotify(i+x,j,k+z,0);
			// }
			// }
		} else {
			TileEntityDust ted = (TileEntityDust) world.getBlockTileEntity(i,
					j, k);

//			if (world.isRemote) {
//				super.breakBlock(world, i, j, k, b, m);
//				return;
//			}

			if (ted == null || ted.isEmpty()) {
				// System.out.println("TED Was empty!!");
				return true;
			}

			// int amt = ted.getAmount()-1;
			// System.out.println("REMOVE " + amt+1);
			// int meta = world.getBlockMetadata(i, j, k);

			for (int x = 0; x < ted.size; x++) {
				for (int z = 0; z < ted.size; z++) {
					int dust = ted.getDust(x, z);

					if (dust > 0) {
						// if
						// (!ModLoader.getMinecraftInstance().thePlayer.capabilities.isCreativeMode)
						// {
						if(!player.capabilities.isCreativeMode)
							this.dropBlockAsItem_do(world, i, j, k, new ItemStack(
								DustMod.idust.shiftedIndex, 1, dust));
					}
					// }
				}
			}
		}

		return super.removeBlockByPlayer(world, player, i, j, k);
	}

	// @Override
	// public void onBlockDestroyedByPlayer(World world, int i, int j, int k,
	// int meta)
	// {
	// if (world.isRemote)
	// {
	// return;
	// }
	//
	// if (world.getBlockMetadata(i, j, k) > 0)
	// {
	// world.setBlockWithNotify(i, j, k, 0);
	// // for(int x = -1; x <= 1; x++)
	// // for(int z = -1; z <= 1; z++){
	// // if(world.getBlockId(i+x, j, k+z) == blockID &&
	// world.getBlockMetadata(i+x, j, k+z) == 1){
	// // world.setBlockWithNotify(i+x,j,k+z,0);
	// // }
	// // }
	// }
	// else
	// {
	// TileEntityDust ted = (TileEntityDust) world.getBlockTileEntity(i, j, k);
	//
	// if (world.isRemote)
	// {
	// return;
	// }
	//
	// if(ted == null || ted.isEmpty()){
	// System.out.println("TED Was empty!!");
	// return;
	// }
	//
	// // int amt = ted.getAmount()-1;
	// // System.out.println("REMOVE " + amt+1);
	// // int meta = world.getBlockMetadata(i, j, k);
	//
	// for (int x = 0; x < ted.size; x++)
	// {
	// for (int z = 0; z < ted.size; z++)
	// {
	// int dust = ted.getDust(x, z);
	//
	// if (dust > 0)
	// {
	// // if
	// (!ModLoader.getMinecraftInstance().thePlayer.capabilities.isCreativeMode)
	// // {
	// this.dropBlockAsItem_do(world, i, j, k, new
	// ItemStack(DustMod.idust.shiftedIndex, 1, dust));
	// }
	// // }
	// }
	// }
	// }
	// }

	@Override
	protected int damageDropped(int i) {
		return i;
	}

	@Override
	public boolean onBlockActivated(World world, int i, int j, int k,
			EntityPlayer p, int dir, float x, float y, float z) {
		if (world.isRemote) {
			return false;
		}

		if (world.getBlockMetadata(i, j, k) == 1) {
			TileEntityDust ted = (TileEntityDust) world.getBlockTileEntity(i,
					j, k);
			ted.onRightClick(p);
			return true;
		} else if (world.getBlockMetadata(i, j, k) > 1) {
			return false;
		}

		if (p.isSneaking()) {
			if (p.getCurrentEquippedItem() == null
					|| p.getCurrentEquippedItem().itemID != DustMod.tome.shiftedIndex) {
				onBlockClicked(world, i, j, k, p);
			}

			return false;
		}

		if (!world.isRemote
				&& p.getCurrentEquippedItem() != null
				&& p.getCurrentEquippedItem().itemID == DustMod.tome.shiftedIndex) {
			updatePattern(world, i, j, k, p);
			DustModBouncer.notifyBlockChange(world, i, j, k, 0);
			return true;
		}

		if (p.getCurrentEquippedItem() == null
				|| p.getCurrentEquippedItem().itemID != DustMod.idust.shiftedIndex) {
			return false;
		}

		int dust = p.getCurrentEquippedItem().getItemDamage();// mod_DustMod.dustValue(p.getCurrentEquippedItem().itemID);
		// System.out.println("ACTIVATED " + p.getCurrentEquippedItem().itemID +
		// " " +dust+ " derp " + mod_DustMod.ITEM_DustID+256);
		Vec3 look = p.getLookVec();
		double mx = look.xCoord;// Math.cos((p.rotationYaw+90)*Math.PI/180);
		double my = look.yCoord;// Math.sin(-p.rotationPitch*Math.PI/180);
		double mz = look.zCoord;// Math.sin((p.rotationYaw+90)*Math.PI/180);

		for (double test = 0; test < 4; test += 0.01) {
			double tx = p.posX + mx * test;
			double ty = p.posY + p.getEyeHeight() + my * test;
			double tz = p.posZ + mz * test;

			if (ty - (double) j <= 0.02) {
				double dx = Math.abs(tx - (double) i) - 0.02;
				double dz = Math.abs(tz - (double) k) - 0.02;
				// System.out.println("FOUND " + dx + " " + dz + " " + (ty-j));
				int rx = (int) Math.floor(dx * TileEntityDust.size);
				int rz = (int) Math.floor(dz * TileEntityDust.size);

				if (rx >= TileEntityDust.size) {
					rx = TileEntityDust.size - 1;
				}

				if (rz >= TileEntityDust.size) {
					rz = TileEntityDust.size - 1;
				}

				if (rx < 0) {
					rx = 0;
				}

				if (rz < 0) {
					rz = 0;
				}

				// System.out.println("Result: " + rx + " " + rz);
				TileEntityDust ted = (TileEntityDust) world.getBlockTileEntity(
						i, j, k);

				if (ted.getDust(rx, rz) <= 0) {
					if (ted.getDust(rx, rz) == -2) {
						setVariableDust(ted, rx, rz, p, dust);
					} else {
						ted.setDust(rx, rz, dust);

						if (!p.capabilities.isCreativeMode) {
							p.getCurrentEquippedItem().stackSize--;

							if (p.getCurrentEquippedItem().stackSize == 0) {
								p.destroyCurrentEquippedItem();
							}
						}
					}

					DustModBouncer.notifyBlockChange(world, i, j, k, 0);
					world.playSoundEffect((float) i + 0.5F, (float) j + 0.5F,
							(float) k + 0.5F, stepSound.getStepSound(),
							(stepSound.getVolume() + 1.0F) / 6.0F,
							stepSound.getPitch() * 0.99F);
					// updatePattern(world,i,j,k);
					return true;
				}

				break;
			}

			// world.setBlock((int)tx, (int)ty, (int)tz, Block.brick.blockID);
		}

		return true;
	}

	private void setVariableDust(TileEntityDust ted, int x, int z,
			EntityPlayer p, int dust) {
		if (ted.getDust(x, z) != -2) {
			return;
		}

		boolean found = false;

		if (!p.capabilities.isCreativeMode) {
			for (int sind = 0; sind < p.inventory.mainInventory.length; sind++) {
				ItemStack is = p.inventory.mainInventory[sind];

				if (is != null && is.itemID == DustMod.idust.shiftedIndex
						&& is.getItemDamage() == dust) {
					is.stackSize--;

					if (is.stackSize == 0) {
						p.inventory.mainInventory[sind] = null;
					}

					found = true;
					break;
				}
			}
		} else {
			found = true;
		}

		if (!found) {
			return;
		}

		ted.setDust(x, z, dust);

		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (i == 0 || j == 0) {
					int wx = ted.xCoord;
					int wz = ted.zCoord;
					int ix = x + i;
					int iz = z + j;

					if (ix < 0) {
						ix = ted.size - 1;
						wx--;
					} else if (ix >= ted.size) {
						ix = 0;
						wx++;
					}

					if (iz < 0) {
						iz = ted.size - 1;
						wz--;
					} else if (iz >= ted.size) {
						iz = 0;
						wz++;
					}

					TileEntity te = p.worldObj.getBlockTileEntity(wx,
							ted.yCoord, wz);

					if (!(te instanceof TileEntityDust)) {
						continue;
					}

					TileEntityDust nted = (TileEntityDust) te;
					setVariableDust(nted, ix, iz, p, dust);
				}
			}
		}
	}

	// @Override
	// public void onBlockDestroyedByPlayer(World world, int i, int j, int k,
	// int l) {
	// System.out.println("kjnvhn " + world.getBlockTileEntity(i, j, k));
	// world.setBlock(i, j, k, blockID);
	// onBlockClicked(world,i,j,k,ModLoader.getMinecraftInstance().thePlayer);
	// }
	@Override
	public void onBlockClicked(World world, int i, int j, int k, EntityPlayer p) {
		// System.out.println("Clicked");
		if (world.isRemote) {
			return;
		}

		Vec3 look = p.getLookVec();
		double mx = look.xCoord;// Math.cos((p.rotationYaw+90)*Math.PI/180);
		double my = look.yCoord;// Math.sin(-p.rotationPitch*Math.PI/180);
		double mz = look.zCoord;// Math.sin((p.rotationYaw+90)*Math.PI/180);

		for (double test = 0; test < 4; test += 0.01) {
			double tx = p.posX + mx * test;
			double ty = p.posY + p.getEyeHeight() + my * test;
			double tz = p.posZ + mz * test;

			if (ty - (double) j <= 0.02) {
				double dx = Math.abs(tx - (double) i) - 0.02;
				double dz = Math.abs(tz - (double) k) - 0.02;
				int rx = (int) Math.floor(dx * TileEntityDust.size);
				int rz = (int) Math.floor(dz * TileEntityDust.size);

				if (rx >= TileEntityDust.size) {
					rx = TileEntityDust.size - 1;
				}

				if (rz >= TileEntityDust.size) {
					rz = TileEntityDust.size - 1;
				}

				if (rx < 0) {
					rx = 0;
				}

				if (rz < 0) {
					rz = 0;
				}

				TileEntityDust ted = (TileEntityDust) world.getBlockTileEntity(
						i, j, k);

				if (ted.getDust(rx, rz) != 0
						&& world.getBlockMetadata(i, j, k) == 0) {
					// System.out.println("BEFORE " + ted.getAmount());
					// System.out.println("AFTER " + ted.getAmount());
					if (ted.getDust(rx, rz) > 0
							&& !p.capabilities.isCreativeMode) {
						this.dropBlockAsItem_do(world, i, j, k,
								new ItemStack(DustMod.idust.shiftedIndex, 1,
										ted.getDust(rx, rz)));
					}

					world.playSoundEffect((float) i + 0.5F, (float) j + 0.5F,
							(float) k + 0.5F, stepSound.getStepSound(),
							(stepSound.getVolume() + 1.0F) / 6.0F,
							stepSound.getPitch() * 0.99F);
					DustModBouncer.notifyBlockChange(world, i, j, k, 0);
					ted.setDust(rx, rz, 0);

					// System.out.println("drop click");
					if (ted.isEmpty() && world.getBlockMetadata(i, j, k) != 10) {
						// System.out.println("Destroying");
						world.setBlockWithNotify(i, j, k, 0);
						this.onBlockDestroyedByPlayer(world, i, j, k, 0);
					}

					// else{
					// updatePattern(world,i,j,k);
					// }
				}

				// if(dx > 1 || dz > 1 || dx < 0 || dz < 0){
				// System.out.println("Derp");
				// }
				// else if(dx < 0.5 && dz < 0.5){
				// System.out.println("I");
				// }
				// else if(dx > 0.5 && dz < 0.5){
				// System.out.println("II");
				// }
				// else if(dx > 0.5 && dz > 0.5){
				// System.out.println("III");
				// }
				// else if(dx < 0.5 && dz > 0.5){
				// System.out.println("IV");
				// }
				break;
			}

			// world.setBlock((int)tx, (int)ty, (int)tz, Block.brick.blockID);
		}

		// super.onBlockClicked(world, i, j, k, p);
	}

	// @Override
	// public void onBlockDestroyedByPlayer(World world, int i, int j, int k,
	// int l) {
	// // System.out.println("noooo " + world.getBlockId(i, j, k) + " " +
	// (world.getBlockTileEntity(i, j, k)) );
	// TileEntityDust ted = (TileEntityDust) world.getBlockTileEntity(i, j, k);
	// if (l == 0 &&
	// ModLoader.getMinecraftInstance().thePlayer.capabilities.depleteBuckets &&
	// ted != null && !ted.isEmpty()) {
	// world.setBlock(i, j, k, blockID);
	// world.setBlockTileEntity(i, j, k, ted);
	// System.out.println("Attempting ");
	// this.onBlockClicked(world, i, j, k,
	// ModLoader.getMinecraftInstance().thePlayer);
	// }
	// }

	@Override
	public int idDropped(int i, Random random, int j) {
		return 0;// i == 0 ? mod_DustMod.ITEM_DustID+256:0;
	}

	public void updatePattern(World world, int i, int j, int k, EntityPlayer p) {
		List<Integer[]> n = new ArrayList<Integer[]>();
		addNeighbors(world, i, j, k, n);

		if (n.size() == 0) {
			return; // dudewat
		}

		for (Integer[] iter : n) {
			if (world.getBlockId(iter[0], j, iter[2]) == blockID) {
				world.setBlockMetadataWithNotify(iter[0], j, iter[2], 1);
			}
		}

		int sx = n.get(0)[0];
		int sz = n.get(0)[2];
		int mx = n.get(0)[0];
		int mz = n.get(0)[2];

		for (Integer[] iter : n) {
			if (iter[0] < sx) {
				sx = iter[0];
			}

			if (iter[2] < sz) {
				sz = iter[2];
			}

			if (iter[0] > mx) {
				mx = iter[0];
			}

			if (iter[2] > mz) {
				mz = iter[2];
			}
		}

		int size = TileEntityDust.size;
		int dx = mx - sx;
		int dz = mz - sz;
		int[][] map = new int[(mx - sx + 1) * size][(mz - sz + 1) * size];

		for (int x = 0; x <= dx; x++) {
			for (int z = 0; z <= dz; z++) {
				if (world.getBlockId(x + sx, j, z + sz) == blockID) {
					TileEntityDust ted = (TileEntityDust) world
							.getBlockTileEntity(x + sx, j, z + sz);

					for (int ix = 0; ix < size; ix++) {
						for (int iz = 0; iz < size; iz++) {
							map[ix + x * size][iz + z * size] = ted.getDust(ix,
									iz);
						}
					}
				}
			}
		}

		// System.out.println("ASNASO " + Arrays.deepToString(map));
		DustManager.callShape(p.worldObj, (double) sx + (double) dx / 2
				+ 0.5D, j + 1D, (double) sz + (double) dz / 2 + 0.5D, map, n,
				(p == null) ? null : p.username);
	}

	public void addNeighbors(World world, int i, int j, int k,
			List<Integer[]> list) {
		for (int x = -1; x <= 1; x++) {
			for (int z = -1; z <= 1; z++) {
				if (world.getBlockId(i + x, j, k + z) == blockID
						&& world.getBlockMetadata(i + x, j, k + z) == 0) {
					boolean cont = true;
					stopcheck:

					for (Integer[] iter : list) {
						if (iter[0] == i + x && iter[2] == k + z) {
							cont = false;
							break stopcheck;
						}
					}

					if (cont) {
						list.add(new Integer[] { i + x, j, k + z });
						addNeighbors(world, i + x, j, k + z, list);
					}
				}
			}
		}
	}

	@Override
	public TileEntity createNewTileEntity(World var1) {
		return new TileEntityDust();
	}
}
