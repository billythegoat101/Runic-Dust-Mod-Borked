package dustmod;

import javax.swing.Renderer;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;
import net.minecraft.src.ModLoader;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.Tessellator;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import dustmod.DustItemManager;
import dustmod.TileEntityDust;
import dustmod.TileEntityRut;


/**
 *  4
 * 3 2
 *  5
 */
public class DustBlockRenderers implements ISimpleBlockRenderingHandler{

	public static int dustModelID;
	public static int rutModelID;
	
	public int currentRenderer;
	public DustBlockRenderers(int currentRenderer){
		this.currentRenderer = currentRenderer;
	}
	
	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID,
			RenderBlocks renderer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess iblockaccess, int i, int j, int k,
			Block block, int modelId, RenderBlocks renderblocks) {

        if (modelId == dustModelID)
        {
            renderDust(renderblocks, iblockaccess, i, j, k, block);
            return true;
        }
        else if (modelId == rutModelID)
        {
            renderRut(renderblocks, iblockaccess, i, j, k, block);
            return true;
        }
        else
        {
            return false;
        }
	}

	@Override
	public boolean shouldRender3DInInventory() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getRenderId() {
		// TODO Auto-generated method stub
		return currentRenderer;
	}
	

    public boolean renderDust(RenderBlocks renderblocks, IBlockAccess iblock, int i, int j, int k, Block block)
    {
    	int meta = iblock.getBlockMetadata(i,j,k);
    	boolean drawHightlight = (meta == 1 || meta == 3);
//    	meta = 3;
//    	if(meta == 3){
//	        renderblocks./*func_83018_a*/updateCustomBlockBounds(Block.woodSingleSlab);
//	        renderGlow(renderblocks,iblock,Block.woodSingleSlab,i,j,k);
//    	}
//        System.out.println("render");
        int size = TileEntityDust.size;
        float px = 1F / 16F;
        float cellWidth = 1F / size;
        float h = 0.025F;
        TileEntityDust ted = (TileEntityDust)iblock.getBlockTileEntity(i, j, k);
        float t = 0.02F;
//        int light = Block.lightValue[block.blockID];
//        Block.lightValue[block.blockID] = 15;
        Tessellator tes = Tessellator.instance;
        tes.setBrightness(block.getMixedBrightnessForBlock(iblock, i, j, k));
//        renderblocks.overrideBlockTexture = 0;
//        tes.startDrawingQuads();
        int[][][] rendArray = ted.getRendArrays();
        int[][] midArray = rendArray[0]; //Actual points 
        int[][] horizArray = rendArray[1]; //horizontal connectors
        int[][] vertArray = rendArray[2]; //vertical connectors
        float bx, bz, bw, bl;
        int[] col;
        float r, g, b;
        
//        if(ted.hasFlame()){
//        	RenderManager.instance.renderEngine.bindTexture(RenderManager.instance.renderEngine.getTexture("./terrain.png"));
//        	System.out.println("waaat");
//        	block.setBlockBounds(0,0,0,1,0.5f,1);
//        	
//            renderblocks./*func_83018_a*/updateCustomBlockBounds(block);
//        	renderblocks.renderBlockFire(block, i, j, k);
//        }
        
        float highlightHeight = 0.125f;

        for (int x = 0; x < size + 1; x++)
        {
            for (int z = 0; z < size + 1; z++)
            {
                float ox = x * cellWidth;
                float oz = z * (cellWidth);

                //if(x < size && z < size){
                if (midArray[x][z] != 0)
                {
                    bx = ox + px;
                    bz = oz + px;
                    bw = 2 * px;
                    bl = 2 * px;
                    block.setBlockBounds(bx, t, bz, bx + bw, t + h, bz + bl);

//                        tes.addVertexWithUV(bx, h, bz, 1, 1);
//                        tes.addVertexWithUV(bx-bw, h, bz, 0, 1);
//                        tes.addVertexWithUV(bx-bw, h, bz-bl, 0, 0);
//                        tes.addVertexWithUV(bx, h, bz-bl, 1, 0);
                    if (meta > 0 && meta != 3)
                    {
                        renderblocks./*func_83018_a*/updateCustomBlockBounds(block);
                        renderblocks.renderStandardBlock(block, i, j, k);
                    }
                    else
                    {
                        col = DustItemManager.getFloorColorRGB(midArray[x][z]);
                        r = (float)col[0];
                        g = (float)col[1];
                        b = (float)col[2];
                        r = r / 255;
                        g = g / 255;
                        b = b / 255;
                        renderblocks./*func_83018_a*/updateCustomBlockBounds(block);
                        renderblocks.renderStandardBlockWithColorMultiplier(block, i, j, k, r, g, b);
                    }
                    if(drawHightlight){
                        if(meta == 3) {
                        	tes.setColorOpaque_F(1, 1, 1);
                            block.setBlockBounds(bx, t, bz, bx + bw, highlightHeight, bz + bl);
                        }
                        else{
                        	tes.setColorOpaque_F(1, 0.68f, 0.68f);
                            block.setBlockBounds(bx, t, bz, bx + bw, t+h, bz + bl);
                        }
                        tes.setBrightness(15728880);
                    	this.renderGlowPoint(renderblocks,block,i,j,k,x,z,midArray[x][z],horizArray,vertArray);
                    }
                }

                if (horizArray[x][z] != 0)
                {
                    bx = ox + px;
                    bz = oz - px;
                    bw = 2 * px;
                    bl = 2 * px;

                    if (z == 0)
                    {
                        bz = 0;
                        bl = px;
                    }

                    if (z == size)
                    {
                        bl = px;
                    }

                    block.setBlockBounds(bx, t, bz, bx + bw, t + h, bz + bl);

                    if (meta > 0 && meta != 3)
                    {
                        renderblocks./*func_83018_a*/updateCustomBlockBounds(block);
                        renderblocks.renderStandardBlock(block, i, j, k);
                    }
                    else
                    {
                        col = DustItemManager.getFloorColorRGB(horizArray[x][z]);
                        r = (float)col[0];
                        g = (float)col[1];
                        b = (float)col[2];
                        r = r / 255;
                        g = g / 255;
                        b = b / 255;
                        renderblocks./*func_83018_a*/updateCustomBlockBounds(block);
                        renderblocks.renderStandardBlockWithColorMultiplier(block, i, j, k, r, g, b);
                    }
                    if(drawHightlight){
                        if(meta == 3) {
                        	tes.setColorOpaque_F(1, 1, 1);
                            block.setBlockBounds(bx, t, bz, bx + bw, highlightHeight, bz + bl);
                        }else{
                        	tes.setColorOpaque_F(1, 0.68f, 0.68f);
                            block.setBlockBounds(bx, t, bz, bx + bw, t+h, bz + bl);
                        }
                        tes.setBrightness(15728880);
                    	this.renderGlowIgnoreSide(renderblocks,block,i,j,k,new boolean[]{true,true,false,false});
                    }
                }

                if (vertArray[x][z] != 0)
                {
                    bx = ox - px;
                    bz = oz + px;
                    bw = 2 * px;
                    bl = 2 * px;

                    if (x == 0)
                    {
                        bx = 0;
                        bw = px;
                    }

                    if (x == size)
                    {
                        bw = px;
                    }

                    block.setBlockBounds(bx, t, bz, bx + bw, t + h, bz + bl);

                    if (meta > 0 && meta != 3)
                    {
                        renderblocks./*func_83018_a*/updateCustomBlockBounds(block);
                        renderblocks.renderStandardBlock(block, i, j, k);
                    }
                    else
                    {
                        col = DustItemManager.getFloorColorRGB(vertArray[x][z]);
                        r = (float)col[0];
                        g = (float)col[1];
                        b = (float)col[2];
                        r = r / 255;
                        g = g / 255;
                        b = b / 255;
                        renderblocks./*func_83018_a*/updateCustomBlockBounds(block);
                        renderblocks.renderStandardBlockWithColorMultiplier(block, i, j, k, r, g, b);
                    }
                    if(drawHightlight){
                        if(meta == 3) {
                        	tes.setColorOpaque_F(1, 1, 1);
                            block.setBlockBounds(bx, t, bz, bx + bw, highlightHeight, bz + bl);
                        }else{
                        	tes.setColorOpaque_F(1, 0.68f, 0.68f);
                            block.setBlockBounds(bx, t, bz, bx + bw, t+h, bz + bl);
                        }
                        tes.setBrightness(15728880);
                    	this.renderGlowIgnoreSide(renderblocks,block,i,j,k,new boolean[]{false,false,true,true});
                    }
                }
            }
        }


        block.setBlockBounds(0, 0, 0, 0, 0, 0);
        renderblocks./*func_83018_a*/updateCustomBlockBounds(block);
        renderblocks.renderStandardBlockWithColorMultiplier(block, i, j, k, 1, 1, 1);
        
//        tes.draw();
//        tes.startDrawingQuads();
//        Block.lightValue[block.blockID] = light;
        renderblocks.overrideBlockTexture = -1;
        block.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, h, 1.0F);
        return true;
    }

    public boolean renderRut(RenderBlocks rb, IBlockAccess iblock, int i, int j, int k, Block block)
    {
        int size = TileEntityDust.size;
        TileEntityRut ter = (TileEntityRut)iblock.getBlockTileEntity(i, j, k);
        int[][][] rut = ter.ruts;
        if(rut == null) return false;
        float t = 0.02F;
//        int light = Block.lightValue[rutBlock.blockID];
//        Block.lightValue[rutBlock.blockID] = 15;
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(block.getMixedBrightnessForBlock(iblock, i, j, k));
        block = Block.blocksList[ter.maskBlock];
        if(block == null) return false;
        Block fluid = Block.blocksList[ter.fluid];
        float bi = 2F / 16F; //baseInset
        float fi = 1F / 16F; //fluidInset
        float cw = 6F / 16F; //cornerWidth
        float rw = 4F / 16F; //rutWidth
        int rendered = 0;
        boolean isGrass = block == Block.grass;

        ///the top of stuff
        //y
        if (rut[1][0][1] == 0 && rut[1][2][1] == 0)
        {
            block.setBlockBounds(cw, 0, cw, cw + rw, 1F, cw + rw);
            rb./*func_83018_a*/updateCustomBlockBounds(block);
            rb.renderStandardBlock(block, i, j, k);
            rendered++;
        }
        else if (rut[1][0][1] == 0)
        {
            block.setBlockBounds(cw, 0, cw, cw + rw, bi, cw + rw);
            rb./*func_83018_a*/updateCustomBlockBounds(block);
            rb.renderStandardBlock(block, i, j, k);
            rendered++;
        }
        else if (rut[1][2][1] == 0)
        {
            block.setBlockBounds(cw, 1F - bi, cw, cw + rw, 1F, cw + rw);
            rb./*func_83018_a*/updateCustomBlockBounds(block);
            rb.renderStandardBlock(block, i, j, k);
            rendered++;
        }

        block.setBlockBounds(0, 1F - cw, 0, cw, 1F, cw);
        rb./*func_83018_a*/updateCustomBlockBounds(block);
        rb.renderStandardBlock(block, i, j, k);
        rendered++;
        block.setBlockBounds(1F - cw, 1F - cw, 0, 1F, 1F, cw);
        rb./*func_83018_a*/updateCustomBlockBounds(block);
        rb.renderStandardBlock(block, i, j, k);
        rendered++;
        block.setBlockBounds(0, 1F - cw, 1F - cw, cw, 1F, 1F);
        rb./*func_83018_a*/updateCustomBlockBounds(block);
        rb.renderStandardBlock(block, i, j, k);
        rendered++;
        block.setBlockBounds(1F - cw, 1F - cw, 1F - cw, 1F, 1F, 1F);
        rb./*func_83018_a*/updateCustomBlockBounds(block);
        rb.renderStandardBlock(block, i, j, k);
        rendered++;

        //Top
        //n
        if (rut[1][2][2] == 0)
        {
            block.setBlockBounds(cw, 1f - cw, 1f - cw, cw + rw, 1f, 1f);
            rb./*func_83018_a*/updateCustomBlockBounds(block);
            rb.renderStandardBlock(block, i, j, k);
            rendered++;
        }

        if (isGrass)
        {
            block.setBlockBounds(0, 0, 0, 1, 1, 1);
            block = Block.grass;
        }

        //s
        if (rut[1][2][0] == 0)
        {
            block.setBlockBounds(cw, 1f - cw, 0F, cw + rw, 1f, cw);
            rb./*func_83018_a*/updateCustomBlockBounds(block);
            rb.renderStandardBlock(block, i, j, k);
            rendered++;
        }

        if (isGrass)
        {
            block.setBlockBounds(0, 0, 0, 1, 1, 1);
            block = Block.grass;
        }

        //e
        if (rut[2][2][1] == 0)
        {
            block.setBlockBounds(1f - cw, 1f - cw, cw, 1f, 1f, cw + rw);
            rb./*func_83018_a*/updateCustomBlockBounds(block);
            rb.renderStandardBlock(block, i, j, k);
            rendered++;
        }

        if (isGrass)
        {
            block.setBlockBounds(0, 0, 0, 1, 1, 1);
            block = Block.grass;
        }

        //w
        if (rut[0][2][1] == 0)
        {
            block.setBlockBounds(0F, 1f - cw, cw, cw, 1f, cw + rw);
            rb./*func_83018_a*/updateCustomBlockBounds(block);
            rb.renderStandardBlock(block, i, j, k);
            rendered++;
        }

        if (isGrass)
        {
            block.setBlockBounds(0, 0, 0, 1, 1, 1);
            block = Block.dirt;
        }

        //corners
        block.setBlockBounds(0, 0, 0, cw, cw, cw);
        rb./*func_83018_a*/updateCustomBlockBounds(block);
        rb.renderStandardBlock(block, i, j, k);
        rendered++;
        block.setBlockBounds(1F - cw, 0, 0, 1F, cw, cw);
        rb./*func_83018_a*/updateCustomBlockBounds(block);
        rb.renderStandardBlock(block, i, j, k);
        rendered++;
        block.setBlockBounds(0, 0, 1F - cw, cw, cw, 1F);
        rb./*func_83018_a*/updateCustomBlockBounds(block);
        rb.renderStandardBlock(block, i, j, k);
        rendered++;
        block.setBlockBounds(1F - cw, 0, 1F - cw, 1F, cw, 1F);
        rb./*func_83018_a*/updateCustomBlockBounds(block);
        rb.renderStandardBlock(block, i, j, k);
        rendered++;

        //Base fluid
        if (fluid != null)
        {
            float ix, iy, iz;
            float iw, ih, il;
            ix = iy = iz = fi;
            iw = ih = il = 1F - fi;
            ter.updateNeighbors();
            if (ter.isNeighborSolid(1, 0, 0))
            {
                iw += fi;
            }

            if (ter.isNeighborSolid(-1, 0, 0))
            {
                ix -= fi;
//                iw+=fi;
            }

            if (ter.isNeighborSolid(0, 1, 0))
            {
                ih += fi;
            }

            if (ter.isNeighborSolid(0, -1, 0))
            {
                iy -= fi;
//                ih+=fi;
            }

            if (ter.isNeighborSolid(0, 0, 1))
            {
                il += fi;
            }

            if (ter.isNeighborSolid(0, 0, -1))
            {
                iz -= fi;
//                il+=fi;
            }

            fluid.setBlockBounds(ix, iy, iz, iw, ih, il);
            rb./*func_83018_a*/updateCustomBlockBounds(fluid);
            rb.renderStandardBlock(fluid, i, j, k);
            rendered++;
        }
        else
        {
            float ix, iy, iz;
            float iw, ih, il;
            ix = iy = iz = bi;
            iw = ih = il = 1F - bi;
            ter.updateNeighbors();
            if (ter.isNeighborSolid(1, 0, 0))
            {
                iw += bi;
            }

            if (ter.isNeighborSolid(-1, 0, 0))
            {
                ix -= bi;
//                iw+=fi;
            }

            if (ter.isNeighborSolid(0, 1, 0))
            {
                ih += bi;
            }

            if (ter.isNeighborSolid(0, -1, 0))
            {
                iy -= bi;
//                ih+=fi;
            }

            if (ter.isNeighborSolid(0, 0, 1))
            {
                il += bi;
            }

            if (ter.isNeighborSolid(0, 0, -1))
            {
                iz -= bi;
//                il+=fi;
            }

            //Base middle
            block.setBlockBounds(ix, iy, iz, iw, ih, il);
            rb./*func_83018_a*/updateCustomBlockBounds(block);
            rb.renderStandardBlock(block, i, j, k);
            rendered++;
        }

        //Centers

        //x
        if (rut[0][1][1] == 0 && rut[2][1][1] == 0)
        {
            block.setBlockBounds(0, cw, cw, 1F, cw + rw, cw + rw);
            rb./*func_83018_a*/updateCustomBlockBounds(block);
            rb.renderStandardBlock(block, i, j, k);
            rendered++;
        }
        else if (rut[0][1][1] == 0)
        {
            block.setBlockBounds(0, cw, cw, bi, cw + rw, cw + rw);
            rb./*func_83018_a*/updateCustomBlockBounds(block);
            rb.renderStandardBlock(block, i, j, k);
            rendered++;
        }
        else if (rut[2][1][1] == 0)
        {
            block.setBlockBounds(1F - bi, cw, cw, 1F, cw + rw, cw + rw);
            rb./*func_83018_a*/updateCustomBlockBounds(block);
            rb.renderStandardBlock(block, i, j, k);
            rendered++;
        }

        //z
        if (rut[1][1][0] == 0 && rut[1][1][2] == 0)
        {
            block.setBlockBounds(cw, cw, 0F, cw + rw, cw + rw, 1F);
            rb./*func_83018_a*/updateCustomBlockBounds(block);
            rb.renderStandardBlock(block, i, j, k);
            rendered++;
        }
        else if (rut[1][1][0] == 0)
        {
            block.setBlockBounds(cw, cw, 0F, cw + rw, cw + rw, bi);
            rb./*func_83018_a*/updateCustomBlockBounds(block);
            rb.renderStandardBlock(block, i, j, k);
            rendered++;
        }
        else if (rut[1][1][2] == 0)
        {
            block.setBlockBounds(cw, cw, 1F - bi, cw + rw, cw + rw, 1F);
            rb./*func_83018_a*/updateCustomBlockBounds(block);
            rb.renderStandardBlock(block, i, j, k);
            rendered++;
        }

//        //d
//        block.setBlockBounds(0F, 0F, 0F, 0f, 0f, 0f);
//        rb.renderStandardBlock(block, i, j, k);

        //Edges

        //Bottom
        //n
        if (rut[1][0][2] == 0)
        {
            block.setBlockBounds(cw, 0, 1f - cw, cw + rw, cw, 1f);
            rb./*func_83018_a*/updateCustomBlockBounds(block);
            rb.renderStandardBlock(block, i, j, k);
            rendered++;
        }

        //s
        if (rut[1][0][0] == 0)
        {
            block.setBlockBounds(cw, 0, 0F, cw + rw, cw, cw);
            rb./*func_83018_a*/updateCustomBlockBounds(block);
            rb.renderStandardBlock(block, i, j, k);
            rendered++;
        }

        //e
        if (rut[2][0][1] == 0)
        {
            block.setBlockBounds(1f - cw, 0, cw, 1f, cw, cw + rw);
            rb./*func_83018_a*/updateCustomBlockBounds(block);
            rb.renderStandardBlock(block, i, j, k);
            rendered++;
        }

        //w
        if (rut[0][0][1] == 0)
        {
            block.setBlockBounds(0F, 0, cw, cw, cw, cw + rw);
            rb./*func_83018_a*/updateCustomBlockBounds(block);
            rb.renderStandardBlock(block, i, j, k);
            rendered++;
        }

        //Middle
        //nw
        if (rut[0][1][2] == 0)
        {
            block.setBlockBounds(0F, cw, 1f - cw, cw, cw + rw, 1f);
            rb./*func_83018_a*/updateCustomBlockBounds(block);
            rb.renderStandardBlock(block, i, j, k);
            rendered++;
        }

        //ne
        if (rut[2][1][2] == 0)
        {
            block.setBlockBounds(1f - cw, cw, 1f - cw, 1f, cw + rw, 1f);
            rb./*func_83018_a*/updateCustomBlockBounds(block);
            rb.renderStandardBlock(block, i, j, k);
            rendered++;
        }

        //sw
        if (rut[0][1][0] == 0)
        {
            block.setBlockBounds(0, cw, 0f, cw, cw + rw, cw);
            rb./*func_83018_a*/updateCustomBlockBounds(block);
            rb.renderStandardBlock(block, i, j, k);
            rendered++;
        }

        //se
        if (rut[2][1][0] == 0)
        {
            block.setBlockBounds(1f - cw, cw, 0f, 1f, cw + rw, cw);
            rb./*func_83018_a*/updateCustomBlockBounds(block);
            rb.renderStandardBlock(block, i, j, k);
            rendered++;
        }

//
//        Block.lightValue[rutBlock.blockID] = light;
//        System.out.println("render " + rendered);
        block.setBlockBounds(0F, 0F, 0F, 1F, 1F, 1F);

        if (fluid != null)
        {
            fluid.setBlockBounds(0F, 0F, 0F, 1F, 1F, 1F);
        }

        return true;
    }
    
    
    private void renderGlow(RenderBlocks rb, Block block, int i, int j, int k){

    	GL11.glPushMatrix();
    	GL11.glScalef(1, 0.2f, 0);
    	
    	GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        Tessellator var8 = Tessellator.instance;
        var8.setColorOpaque_F(1, 1, 1);
        var8.setBrightness(15728880);
//        var8.disableColor();
        double dif = 0.001;
        int tex = 32;
    	renderEastFace(rb, 32, block, i, j, k-dif, tex);
    	renderWestFace(rb, 32, block, i, j, k+dif, tex);
    	renderNorthFace(rb, 32, block, i-dif, j, k, tex);
    	renderSouthFace(rb, 32, block, i+dif, j, k, tex);
    	GL11.glPopAttrib();
    	
    	GL11.glPopMatrix();
    }
    
    private void renderGlowIgnoreSide(RenderBlocks rb, Block b, int i, int j, int k, boolean[] ignore){
    	GL11.glPushMatrix();
    	GL11.glScalef(1, 0.2f, 0);
    	GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        double dif = 0.001;
        int tex = 32;
        rb./*func_83018_a*/updateCustomBlockBounds(b);
        if(!ignore[0])
        	renderEastFace(rb, 128, b, i, j, k-dif, tex);
        if(!ignore[1])
        	renderWestFace(rb, 128, b, i, j, k+dif, tex);
        if(!ignore[2])
        	renderNorthFace(rb, 128, b, i-dif, j, k, tex);
        if(!ignore[3])
        	renderSouthFace(rb, 128, b, i+dif, j, k, tex);
    	GL11.glPopAttrib();
    	
    	GL11.glPopMatrix();
    }

    
    private void renderGlowPoint(RenderBlocks rb, Block b, int i, int j, int k, int x, int y, int dust, int[][] horiz, int[][] vert){

        double dif = 0.001;
        int tex = 32;

        rb./*func_83018_a*/updateCustomBlockBounds(b);
        if(dust != horiz[x][y])
        	renderEastFace(rb, 128, b, i, j, k-dif, tex);
        if(dust != horiz[x][y+1])
        	renderWestFace(rb, 128, b, i, j, k+dif, tex);
        if(dust != vert[x][y])
        	renderNorthFace(rb, 128, b, i-dif, j, k, tex);
        if(dust != vert[x+1][y])
        	renderSouthFace(rb, 128, b, i+dif, j, k, tex);
    }

    
    /*
    * Renders the given texture to the east (z-negative) face of the block.  Args: block, x, y, z, texture
    */
   public void renderEastFace(RenderBlocks rb, int height, Block par1Block, double par2, double par4, double par6, int par8)
   {
       Tessellator var9 = Tessellator.instance;

       if (rb.overrideBlockTexture >= 0)
       {
           par8 = rb.overrideBlockTexture;
       }

       int var10 = (par8 & 15) << 4;
       int var11 = par8 & 240;
       double var12 = ((double)var10 + rb.customMinX * (double)16) / 256.0D;
       double var14 = ((double)var10 + rb.customMaxX * (double)16 - 0.01D) / 256.0D;
       double var16 = ((double)(var11 + 16) - rb.customMaxY * (double)height) / 256.0D;
       double var18 = ((double)(var11 + 16) - rb.customMinY * (double)16 - 0.01D) / 256.0D;
       double var20;

       if (rb.flipTexture)
       {
           var20 = var12;
           var12 = var14;
           var14 = var20;
       }

       if (rb.customMinX < 0.0D || rb.customMaxX > 1.0D)
       {
           var12 = (double)(((float)var10 + 0.0F) / 256.0F);
           var14 = (double)(((float)var10 + 15.99F) / 256.0F);
       }

       if (rb.customMinY < 0.0D || rb.customMaxY > 1.0D)
       {
           var16 = (double)(((float)var11 + 0.0F) / 256.0F);
           var18 = (double)(((float)var11 + 15.99F) / 256.0F);
       }

       var20 = var14;
       double var22 = var12;
       double var24 = var16;
       double var26 = var18;

       if (rb.uvRotateEast == 2)
       {
           var12 = ((double)var10 + rb.customMinY * (double)height) / 256.0D;
           var16 = ((double)(var11 + 16) - rb.customMinX * (double)height) / 256.0D;
           var14 = ((double)var10 + rb.customMaxY * (double)height) / 256.0D;
           var18 = ((double)(var11 + 16) - rb.customMaxX * (double)height) / 256.0D;
           var24 = var16;
           var26 = var18;
           var20 = var12;
           var22 = var14;
           var16 = var18;
           var18 = var24;
       }
       else if (rb.uvRotateEast == 1)
       {
           var12 = ((double)(var10 + 16) - rb.customMaxY * (double)height) / 256.0D;
           var16 = ((double)var11 + rb.customMaxX * (double)height) / 256.0D;
           var14 = ((double)(var10 + 16) - rb.customMinY * (double)height) / 256.0D;
           var18 = ((double)var11 + rb.customMinX * (double)height) / 256.0D;
           var20 = var14;
           var22 = var12;
           var12 = var14;
           var14 = var22;
           var24 = var18;
           var26 = var16;
       }
       else if (rb.uvRotateEast == 3)
       {
           var12 = ((double)(var10 + 16) - rb.customMinX * (double)height) / 256.0D;
           var14 = ((double)(var10 + 16) - rb.customMaxX * (double)height - 0.01D) / 256.0D;
           var16 = ((double)var11 + rb.customMaxY * (double)height) / 256.0D;
           var18 = ((double)var11 + rb.customMinY * (double)height - 0.01D) / 256.0D;
           var20 = var14;
           var22 = var12;
           var24 = var16;
           var26 = var18;
       }

       double var28 = par2 + rb.customMinX;
       double var30 = par2 + rb.customMaxX;
       double var32 = par4 + rb.customMinY;
       double var34 = par4 + rb.customMaxY;
       double var36 = par6 + rb.customMinZ;

       if (rb.enableAO)
       {
           var9.setColorOpaque_F(rb.colorRedTopLeft, rb.colorGreenTopLeft, rb.colorBlueTopLeft);
           var9.setBrightness(rb.brightnessTopLeft);
           var9.addVertexWithUV(var28, var34, var36, var20, var24);
           var9.setColorOpaque_F(rb.colorRedBottomLeft, rb.colorGreenBottomLeft, rb.colorBlueBottomLeft);
           var9.setBrightness(rb.brightnessBottomLeft);
           var9.addVertexWithUV(var30, var34, var36, var12, var16);
           var9.setColorOpaque_F(rb.colorRedBottomRight, rb.colorGreenBottomRight, rb.colorBlueBottomRight);
           var9.setBrightness(rb.brightnessBottomRight);
           var9.addVertexWithUV(var30, var32, var36, var22, var26);
           var9.setColorOpaque_F(rb.colorRedTopRight, rb.colorGreenTopRight, rb.colorBlueTopRight);
           var9.setBrightness(rb.brightnessTopRight);
           var9.addVertexWithUV(var28, var32, var36, var14, var18);
       }
       else
       {
           var9.addVertexWithUV(var28, var34, var36, var20, var24);
           var9.addVertexWithUV(var30, var34, var36, var12, var16);
           var9.addVertexWithUV(var30, var32, var36, var22, var26);
           var9.addVertexWithUV(var28, var32, var36, var14, var18);
       }
   }

   /**
    * Renders the given texture to the west (z-positive) face of the block.  Args: block, x, y, z, texture
    */
   public void renderWestFace(RenderBlocks rb, int height, Block par1Block, double par2, double par4, double par6, int par8)
   {
       Tessellator var9 = Tessellator.instance;

       if (rb.overrideBlockTexture >= 0)
       {
           par8 = rb.overrideBlockTexture;
       }

       int var10 = (par8 & 15) << 4;
       int var11 = par8 & 240;
       double var12 = ((double)var10 + rb.customMinX * (double)16) / 256.0D;
       double var14 = ((double)var10 + rb.customMaxX * (double)16 - 0.01D) / 256.0D;
       double var16 = ((double)(var11 + 16) - rb.customMaxY * (double)height) / 256.0D;
       double var18 = ((double)(var11 + 16) - rb.customMinY * (double)16 - 0.01D) / 256.0D;
       double var20;

       if (rb.flipTexture)
       {
           var20 = var12;
           var12 = var14;
           var14 = var20;
       }

       if (rb.customMinX < 0.0D || rb.customMaxX > 1.0D)
       {
           var12 = (double)(((float)var10 + 0.0F) / 256.0F);
           var14 = (double)(((float)var10 + 15.99F) / 256.0F);
       }

       if (rb.customMinY < 0.0D || rb.customMaxY > 1.0D)
       {
           var16 = (double)(((float)var11 + 0.0F) / 256.0F);
           var18 = (double)(((float)var11 + 15.99F) / 256.0F);
       }

       var20 = var14;
       double var22 = var12;
       double var24 = var16;
       double var26 = var18;

       if (rb.uvRotateWest == 1)
       {
           var12 = ((double)var10 + rb.customMinY * (double)height) / 256.0D;
           var18 = ((double)(var11 + 16) - rb.customMinX * (double)height) / 256.0D;
           var14 = ((double)var10 + rb.customMaxY * (double)height) / 256.0D;
           var16 = ((double)(var11 + 16) - rb.customMaxX * (double)height) / 256.0D;
           var24 = var16;
           var26 = var18;
           var20 = var12;
           var22 = var14;
           var16 = var18;
           var18 = var24;
       }
       else if (rb.uvRotateWest == 2)
       {
           var12 = ((double)(var10 + 16) - rb.customMaxY * (double)height) / 256.0D;
           var16 = ((double)var11 + rb.customMinX * (double)height) / 256.0D;
           var14 = ((double)(var10 + 16) - rb.customMinY * (double)height) / 256.0D;
           var18 = ((double)var11 + rb.customMaxX * (double)height) / 256.0D;
           var20 = var14;
           var22 = var12;
           var12 = var14;
           var14 = var22;
           var24 = var18;
           var26 = var16;
       }
       else if (rb.uvRotateWest == 3)
       {
           var12 = ((double)(var10 + 16) - rb.customMinX * (double)height) / 256.0D;
           var14 = ((double)(var10 + 16) - rb.customMaxX * (double)height - 0.01D) / 256.0D;
           var16 = ((double)var11 + rb.customMaxY * (double)height) / 256.0D;
           var18 = ((double)var11 + rb.customMinY * (double)height - 0.01D) / 256.0D;
           var20 = var14;
           var22 = var12;
           var24 = var16;
           var26 = var18;
       }

       double var28 = par2 + rb.customMinX;
       double var30 = par2 + rb.customMaxX;
       double var32 = par4 + rb.customMinY;
       double var34 = par4 + rb.customMaxY;
       double var36 = par6 + rb.customMaxZ;

       if (rb.enableAO)
       {
           var9.setColorOpaque_F(rb.colorRedTopLeft, rb.colorGreenTopLeft, rb.colorBlueTopLeft);
           var9.setBrightness(rb.brightnessTopLeft);
           var9.addVertexWithUV(var28, var34, var36, var12, var16);
           var9.setColorOpaque_F(rb.colorRedBottomLeft, rb.colorGreenBottomLeft, rb.colorBlueBottomLeft);
           var9.setBrightness(rb.brightnessBottomLeft);
           var9.addVertexWithUV(var28, var32, var36, var22, var26);
           var9.setColorOpaque_F(rb.colorRedBottomRight, rb.colorGreenBottomRight, rb.colorBlueBottomRight);
           var9.setBrightness(rb.brightnessBottomRight);
           var9.addVertexWithUV(var30, var32, var36, var14, var18);
           var9.setColorOpaque_F(rb.colorRedTopRight, rb.colorGreenTopRight, rb.colorBlueTopRight);
           var9.setBrightness(rb.brightnessTopRight);
           var9.addVertexWithUV(var30, var34, var36, var20, var24);
       }
       else
       {
           var9.addVertexWithUV(var28, var34, var36, var12, var16);
           var9.addVertexWithUV(var28, var32, var36, var22, var26);
           var9.addVertexWithUV(var30, var32, var36, var14, var18);
           var9.addVertexWithUV(var30, var34, var36, var20, var24);
       }
   }

   /**
    * Renders the given texture to the north (x-negative) face of the block.  Args: block, x, y, z, texture
    */
   public void renderNorthFace(RenderBlocks rb, int height, Block par1Block, double par2, double par4, double par6, int par8)
   {
       Tessellator var9 = Tessellator.instance;

       if (rb.overrideBlockTexture >= 0)
       {
           par8 = rb.overrideBlockTexture;
       }

       int var10 = (par8 & 15) << 4;
       int var11 = par8 & 240;
       double var12 = ((double)var10 + rb.customMinZ * (double)16) / 256.0D;
       double var14 = ((double)var10 + rb.customMaxZ * (double)16 - 0.01D) / 256.0D;
       double var16 = ((double)(var11 + 16) - rb.customMaxY * (double)height) / 256.0D;
       double var18 = ((double)(var11 + 16) - rb.customMinY * (double)16 - 0.01D) / 256.0D;
       double var20;

       if (rb.flipTexture)
       {
           var20 = var12;
           var12 = var14;
           var14 = var20;
       }

       if (rb.customMinZ < 0.0D || rb.customMaxZ > 1.0D)
       {
           var12 = (double)(((float)var10 + 0.0F) / 256.0F);
           var14 = (double)(((float)var10 + 15.99F) / 256.0F);
       }

       if (rb.customMinY < 0.0D || rb.customMaxY > 1.0D)
       {
           var16 = (double)(((float)var11 + 0.0F) / 256.0F);
           var18 = (double)(((float)var11 + 15.99F) / 256.0F);
       }

       var20 = var14;
       double var22 = var12;
       double var24 = var16;
       double var26 = var18;

       if (rb.uvRotateNorth == 1)
       {
           var12 = ((double)var10 + rb.customMinY * (double)height) / 256.0D;
           var16 = ((double)(var11 + 16) - rb.customMaxZ * (double)height) / 256.0D;
           var14 = ((double)var10 + rb.customMaxY * (double)height) / 256.0D;
           var18 = ((double)(var11 + 16) - rb.customMinZ * (double)height) / 256.0D;
           var24 = var16;
           var26 = var18;
           var20 = var12;
           var22 = var14;
           var16 = var18;
           var18 = var24;
       }
       else if (rb.uvRotateNorth == 2)
       {
           var12 = ((double)(var10 + 16) - rb.customMaxY * (double)height) / 256.0D;
           var16 = ((double)var11 + rb.customMinZ * (double)height) / 256.0D;
           var14 = ((double)(var10 + 16) - rb.customMinY * (double)height) / 256.0D;
           var18 = ((double)var11 + rb.customMaxZ * (double)height) / 256.0D;
           var20 = var14;
           var22 = var12;
           var12 = var14;
           var14 = var22;
           var24 = var18;
           var26 = var16;
       }
       else if (rb.uvRotateNorth == 3)
       {
           var12 = ((double)(var10 + 16) - rb.customMinZ * (double)height) / 256.0D;
           var14 = ((double)(var10 + 16) - rb.customMaxZ * (double)height - 0.01D) / 256.0D;
           var16 = ((double)var11 + rb.customMaxY * (double)height) / 256.0D;
           var18 = ((double)var11 + rb.customMinY * (double)height - 0.01D) / 256.0D;
           var20 = var14;
           var22 = var12;
           var24 = var16;
           var26 = var18;
       }

       double var28 = par2 + rb.customMinX;
       double var30 = par4 + rb.customMinY;
       double var32 = par4 + rb.customMaxY;
       double var34 = par6 + rb.customMinZ;
       double var36 = par6 + rb.customMaxZ;

       if (rb.enableAO)
       {
           var9.setColorOpaque_F(rb.colorRedTopLeft, rb.colorGreenTopLeft, rb.colorBlueTopLeft);
           var9.setBrightness(rb.brightnessTopLeft);
           var9.addVertexWithUV(var28, var32, var36, var20, var24);
           var9.setColorOpaque_F(rb.colorRedBottomLeft, rb.colorGreenBottomLeft, rb.colorBlueBottomLeft);
           var9.setBrightness(rb.brightnessBottomLeft);
           var9.addVertexWithUV(var28, var32, var34, var12, var16);
           var9.setColorOpaque_F(rb.colorRedBottomRight, rb.colorGreenBottomRight, rb.colorBlueBottomRight);
           var9.setBrightness(rb.brightnessBottomRight);
           var9.addVertexWithUV(var28, var30, var34, var22, var26);
           var9.setColorOpaque_F(rb.colorRedTopRight, rb.colorGreenTopRight, rb.colorBlueTopRight);
           var9.setBrightness(rb.brightnessTopRight);
           var9.addVertexWithUV(var28, var30, var36, var14, var18);
       }
       else
       {
           var9.addVertexWithUV(var28, var32, var36, var20, var24);
           var9.addVertexWithUV(var28, var32, var34, var12, var16);
           var9.addVertexWithUV(var28, var30, var34, var22, var26);
           var9.addVertexWithUV(var28, var30, var36, var14, var18);
       }
   }

   /**
    * Renders the given texture to the south (x-positive) face of the block.  Args: block, x, y, z, texture
    */
   public void renderSouthFace(RenderBlocks rb, int height, Block par1Block, double par2, double par4, double par6, int par8)
   {
       Tessellator var9 = Tessellator.instance;

       if (rb.overrideBlockTexture >= 0)
       {
           par8 = rb.overrideBlockTexture;
       }

       int var10 = (par8 & 15) << 4;
       int var11 = par8 & 240;
       double var12 = ((double)var10 + rb.customMinZ * (double)16) / 256.0D;
       double var14 = ((double)var10 + rb.customMaxZ * (double)16 - 0.01D) / 256.0D;
       double var16 = ((double)(var11 + 16) - rb.customMaxY * (double)height) / 256.0D;
       double var18 = ((double)(var11 + 16) - rb.customMinY * (double)16 - 0.01D) / 256.0D;
       double var20;

       if (rb.flipTexture)
       {
           var20 = var12;
           var12 = var14;
           var14 = var20;
       }

       if (rb.customMinZ < 0.0D || rb.customMaxZ > 1.0D)
       {
           var12 = (double)(((float)var10 + 0.0F) / 256.0F);
           var14 = (double)(((float)var10 + 15.99F) / 256.0F);
       }

       if (rb.customMinY < 0.0D || rb.customMaxY > 1.0D)
       {
           var16 = (double)(((float)var11 + 0.0F) / 256.0F);
           var18 = (double)(((float)var11 + 15.99F) / 256.0F);
       }

       var20 = var14;
       double var22 = var12;
       double var24 = var16;
       double var26 = var18;

       if (rb.uvRotateSouth == 2)
       {
           var12 = ((double)var10 + rb.customMinY * (double)height) / 256.0D;
           var16 = ((double)(var11 + 16) - rb.customMinZ * (double)height) / 256.0D;
           var14 = ((double)var10 + rb.customMaxY * (double)height) / 256.0D;
           var18 = ((double)(var11 + 16) - rb.customMaxZ * (double)height) / 256.0D;
           var24 = var16;
           var26 = var18;
           var20 = var12;
           var22 = var14;
           var16 = var18;
           var18 = var24;
       }
       else if (rb.uvRotateSouth == 1)
       {
           var12 = ((double)(var10 + 16) - rb.customMaxY * (double)height) / 256.0D;
           var16 = ((double)var11 + rb.customMaxZ * (double)height) / 256.0D;
           var14 = ((double)(var10 + 16) - rb.customMinY * (double)height) / 256.0D;
           var18 = ((double)var11 + rb.customMinZ * (double)height) / 256.0D;
           var20 = var14;
           var22 = var12;
           var12 = var14;
           var14 = var22;
           var24 = var18;
           var26 = var16;
       }
       else if (rb.uvRotateSouth == 3)
       {
           var12 = ((double)(var10 + 16) - rb.customMinZ * (double)height) / 256.0D;
           var14 = ((double)(var10 + 16) - rb.customMaxZ * (double)height - 0.01D) / 256.0D;
           var16 = ((double)var11 + rb.customMaxY * (double)height) / 256.0D;
           var18 = ((double)var11 + rb.customMinY * (double)height - 0.01D) / 256.0D;
           var20 = var14;
           var22 = var12;
           var24 = var16;
           var26 = var18;
       }

       double var28 = par2 + rb.customMaxX;
       double var30 = par4 + rb.customMinY;
       double var32 = par4 + rb.customMaxY;
       double var34 = par6 + rb.customMinZ;
       double var36 = par6 + rb.customMaxZ;

       if (rb.enableAO)
       {
           var9.setColorOpaque_F(rb.colorRedTopLeft, rb.colorGreenTopLeft, rb.colorBlueTopLeft);
           var9.setBrightness(rb.brightnessTopLeft);
           var9.addVertexWithUV(var28, var30, var36, var22, var26);
           var9.setColorOpaque_F(rb.colorRedBottomLeft, rb.colorGreenBottomLeft, rb.colorBlueBottomLeft);
           var9.setBrightness(rb.brightnessBottomLeft);
           var9.addVertexWithUV(var28, var30, var34, var14, var18);
           var9.setColorOpaque_F(rb.colorRedBottomRight, rb.colorGreenBottomRight, rb.colorBlueBottomRight);
           var9.setBrightness(rb.brightnessBottomRight);
           var9.addVertexWithUV(var28, var32, var34, var20, var24);
           var9.setColorOpaque_F(rb.colorRedTopRight, rb.colorGreenTopRight, rb.colorBlueTopRight);
           var9.setBrightness(rb.brightnessTopRight);
           var9.addVertexWithUV(var28, var32, var36, var12, var16);
       }
       else
       {
           var9.addVertexWithUV(var28, var30, var36, var22, var26);
           var9.addVertexWithUV(var28, var30, var34, var14, var18);
           var9.addVertexWithUV(var28, var32, var34, var20, var24);
           var9.addVertexWithUV(var28, var32, var36, var12, var16);
       }
   }

    


}
