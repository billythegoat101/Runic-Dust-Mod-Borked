package net.minecraft.src.dustmod;

import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.ModLoader;
import net.minecraft.src.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {
	@Override
	public int getBlockModel(Block b) {
		if(b == DustMod.dust) return DustBlockRenderers.dustModelID;
		if(b == DustMod.rutBlock) return DustBlockRenderers.rutModelID;
		return -1;
	}
	
	@Override
	public void resetPlayerTomePage() {
		GuiTome.page = 0;
	}
	
	@Override
	public World getClientWorld() {
		return FMLClientHandler.instance().getClient().theWorld;
	}
	
	@Override
	public void checkPage(DustShape shape) {
		PageHelper.instance.checkImage(shape);
	}
	
	@Override
	public void registerEventHandlers() {
	}
	
	@Override
	public void registerRenderInformation() {
        MinecraftForgeClient.preloadTexture(DustMod.path + "/dustrItems.png");
        MinecraftForgeClient.preloadTexture(DustMod.path + "/dustBlocks.png");
        
        DustBlockRenderers.dustModelID = RenderingRegistry.instance().getNextAvailableRenderId();
        DustBlockRenderers.rutModelID = RenderingRegistry.instance().getNextAvailableRenderId();
        DustBlockRenderers.dustModelID = RenderingRegistry.instance().getNextAvailableRenderId();
        DustBlockRenderers.rutModelID = RenderingRegistry.instance().getNextAvailableRenderId();
        
        System.out.println("Dust model ID " + DustBlockRenderers.dustModelID + " Rut: " + DustBlockRenderers.rutModelID);
        
        RenderingRegistry.instance().registerBlockHandler(new DustBlockRenderers(DustBlockRenderers.dustModelID));
        RenderingRegistry.instance().registerBlockHandler(new DustBlockRenderers(DustBlockRenderers.rutModelID));
        
        RenderingRegistry.instance().registerEntityRenderingHandler(EntityDust.class, new RenderEntityDust());
        RenderingRegistry.instance().registerEntityRenderingHandler(EntityBlock.class, new RenderEntityBlock());
        MinecraftForge.EVENT_BUS.register(new RenderLastHandler());
	}
	
	@Override
	public void registerTileEntityRenderers() {
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDustTable.class, new RenderDustTable());
	}

	
	@Override
	public void openTomeGUI(ItemStack itemstack, EntityPlayer p) {
//		ModLoader.openGUI(p, new GuiTome(itemstack));
		FMLClientHandler.instance().displayGuiScreen(p, new GuiTome(itemstack));
//		p.openGui(DustMod.instance, );
	}
	
	@Override
	public boolean placeDustWithTome(ItemStack itemstack, EntityPlayer p,
			World world, int i, int j, int k, int l) {
        return true;
	}
}
