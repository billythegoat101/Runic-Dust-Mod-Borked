package net.minecraft.src.dustmod;

import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.ModLoader;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Tessellator;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

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
//        System.out.println("render");
        int size = TileEntityDust.size;
        float px = 1F / 16F;
        float cellWidth = 1F / size;
        float h = 0.0F;
        TileEntityDust ted = (TileEntityDust)iblock.getBlockTileEntity(i, j, k);
        float t = 0.02F;
//        int light = Block.lightValue[block.blockID];
//        Block.lightValue[block.blockID] = 15;
        Tessellator tes = Tessellator.instance;
        tes.setBrightness(block.getMixedBrightnessForBlock(iblock, i, j, k));
//        renderblocks.overrideBlockTexture = 0;
//        tes.startDrawingQuads();
        int[][][] rendArray = ted.getRendArrays();
        int[][] midArray = rendArray[0];
        int[][] horizArray = rendArray[1];
        int[][] vertArray = rendArray[2];
        float bx, bz, bw, bl;
        int[] col;
        float r, g, b;
        int meta = iblock.getBlockMetadata(i, j, k);

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
                    if (meta > 0)
                    {
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
                        renderblocks.renderStandardBlockWithColorMultiplier(block, i, j, k, r, g, b);
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

                    if (meta > 0)
                    {
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
                        renderblocks.renderStandardBlockWithColorMultiplier(block, i, j, k, r, g, b);
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

                    if (meta > 0)
                    {
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
                        renderblocks.renderStandardBlockWithColorMultiplier(block, i, j, k, r, g, b);
                    }
                }
            }
        }

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
            rb.renderStandardBlock(block, i, j, k);
            rendered++;
        }
        else if (rut[1][0][1] == 0)
        {
            block.setBlockBounds(cw, 0, cw, cw + rw, bi, cw + rw);
            rb.renderStandardBlock(block, i, j, k);
            rendered++;
        }
        else if (rut[1][2][1] == 0)
        {
            block.setBlockBounds(cw, 1F - bi, cw, cw + rw, 1F, cw + rw);
            rb.renderStandardBlock(block, i, j, k);
            rendered++;
        }

        block.setBlockBounds(0, 1F - cw, 0, cw, 1F, cw);
        rb.renderStandardBlock(block, i, j, k);
        rendered++;
        block.setBlockBounds(1F - cw, 1F - cw, 0, 1F, 1F, cw);
        rb.renderStandardBlock(block, i, j, k);
        rendered++;
        block.setBlockBounds(0, 1F - cw, 1F - cw, cw, 1F, 1F);
        rb.renderStandardBlock(block, i, j, k);
        rendered++;
        block.setBlockBounds(1F - cw, 1F - cw, 1F - cw, 1F, 1F, 1F);
        rb.renderStandardBlock(block, i, j, k);
        rendered++;

        //Top
        //n
        if (rut[1][2][2] == 0)
        {
            block.setBlockBounds(cw, 1f - cw, 1f - cw, cw + rw, 1f, 1f);
            rb.renderStandardBlock(block, i, j, k);
            rendered++;
        }
        else if (ter.isNeighborSolid(0, 1, 0))  //above
        {
            block.setBlockBounds(cw, 1f - cw, 1f - cw, cw + rw, 1f, 1f - bi);
            rb.renderStandardBlock(block, i, j, k);
            rendered++;
        }
        else if (ter.isNeighborSolid(0, 0, 1))  //north
        {
            if (isGrass)
            {
                block.setBlockBounds(0, 0, 0, 1, 1, 1);
                block = Block.dirt;
            }

            block.setBlockBounds(cw, 1f - cw, 1f - cw, cw + rw, 1f - bi, 1f);
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
            rb.renderStandardBlock(block, i, j, k);
            rendered++;
        }
        else if (ter.isNeighborSolid(0, 1, 0))  //above
        {
            block.setBlockBounds(cw, 1f - cw, bi, cw + rw, 1f, cw);
            rb.renderStandardBlock(block, i, j, k);
            rendered++;
        }
        else if (ter.isNeighborSolid(0, 0, -1))  //south
        {
            if (isGrass)
            {
                block.setBlockBounds(0, 0, 0, 1, 1, 1);
                block = Block.dirt;
            }

            block.setBlockBounds(cw, 1f - cw, 0F, cw + rw, 1f - bi, cw);
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
            rb.renderStandardBlock(block, i, j, k);
            rendered++;
        }
        else if (ter.isNeighborSolid(0, 1, 0))  //above
        {
            block.setBlockBounds(1f - cw, 1f - cw, cw, 1f - bi, 1f, cw + rw);
            rb.renderStandardBlock(block, i, j, k);
            rendered++;
        }
        else if (ter.isNeighborSolid(1, 0, 0))  //east
        {
            if (isGrass)
            {
                block.setBlockBounds(0, 0, 0, 1, 1, 1);
                block = Block.dirt;
            }

            block.setBlockBounds(1f - cw, 1f - cw, cw, 1f, 1f - bi, cw + rw);
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
            rb.renderStandardBlock(block, i, j, k);
            rendered++;
        }
        else if (ter.isNeighborSolid(0, 1, 0))  //above
        {
            block.setBlockBounds(bi, 1f - cw, cw, cw, 1f, cw + rw);
            rb.renderStandardBlock(block, i, j, k);
            rendered++;
        }
        else if (ter.isNeighborSolid(-1, 0, 0))  //west
        {
            if (isGrass)
            {
                block.setBlockBounds(0, 0, 0, 1, 1, 1);
                block = Block.dirt;
            }

            block.setBlockBounds(0F, 1f - cw, cw, cw, 1f - bi, cw + rw);
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
        rb.renderStandardBlock(block, i, j, k);
        rendered++;
        block.setBlockBounds(1F - cw, 0, 0, 1F, cw, cw);
        rb.renderStandardBlock(block, i, j, k);
        rendered++;
        block.setBlockBounds(0, 0, 1F - cw, cw, cw, 1F);
        rb.renderStandardBlock(block, i, j, k);
        rendered++;
        block.setBlockBounds(1F - cw, 0, 1F - cw, 1F, cw, 1F);
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
            rb.renderStandardBlock(fluid, i, j, k);
            rendered++;
        }
        else
        {
            //Base middle
            block.setBlockBounds(bi, bi, bi, 1F - bi, 1F - bi, 1F - bi);
            rb.renderStandardBlock(block, i, j, k);
            rendered++;
        }

        //Centers

        //x
        if (rut[0][1][1] == 0 && rut[2][1][1] == 0)
        {
            block.setBlockBounds(0, cw, cw, 1F, cw + rw, cw + rw);
            rb.renderStandardBlock(block, i, j, k);
            rendered++;
        }
        else if (rut[0][1][1] == 0)
        {
            block.setBlockBounds(0, cw, cw, bi, cw + rw, cw + rw);
            rb.renderStandardBlock(block, i, j, k);
            rendered++;
        }
        else if (rut[2][1][1] == 0)
        {
            block.setBlockBounds(1F - bi, cw, cw, 1F, cw + rw, cw + rw);
            rb.renderStandardBlock(block, i, j, k);
            rendered++;
        }

        //z
        if (rut[1][1][0] == 0 && rut[1][1][2] == 0)
        {
            block.setBlockBounds(cw, cw, 0F, cw + rw, cw + rw, 1F);
            rb.renderStandardBlock(block, i, j, k);
            rendered++;
        }
        else if (rut[1][1][0] == 0)
        {
            block.setBlockBounds(cw, cw, 0F, cw + rw, cw + rw, bi);
            rb.renderStandardBlock(block, i, j, k);
            rendered++;
        }
        else if (rut[1][1][2] == 0)
        {
            block.setBlockBounds(cw, cw, 1F - bi, cw + rw, cw + rw, 1F);
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
            rb.renderStandardBlock(block, i, j, k);
            rendered++;
        }
        else if (ter.isNeighborSolid(0, -1, 0))  //below
        {
            block.setBlockBounds(cw, 0, 1f - cw, cw + rw, cw, 1f - bi);
            rb.renderStandardBlock(block, i, j, k);
            rendered++;
        }
        else if (ter.isNeighborSolid(0, 0, 1))  //north
        {
            block.setBlockBounds(cw, bi, 1f - cw, cw + rw, cw, 1f);
            rb.renderStandardBlock(block, i, j, k);
            rendered++;
        }

        //s
        if (rut[1][0][0] == 0)
        {
            block.setBlockBounds(cw, 0, 0F, cw + rw, cw, cw);
            rb.renderStandardBlock(block, i, j, k);
            rendered++;
        }
        else if (ter.isNeighborSolid(0, -1, 0))  //below
        {
            block.setBlockBounds(cw, 0, bi, cw + rw, cw, cw);
            rb.renderStandardBlock(block, i, j, k);
            rendered++;
        }
        else if (ter.isNeighborSolid(0, 0, -1))  //south
        {
            block.setBlockBounds(cw, bi, 0F, cw + rw, cw, cw);
            rb.renderStandardBlock(block, i, j, k);
            rendered++;
        }

        //e
        if (rut[2][0][1] == 0)
        {
            block.setBlockBounds(1f - cw, 0, cw, 1f, cw, cw + rw);
            rb.renderStandardBlock(block, i, j, k);
            rendered++;
        }
        else if (ter.isNeighborSolid(0, -1, 0))  //below
        {
            block.setBlockBounds(1f - cw, 0, cw, 1f - bi, cw, cw + rw);
            rb.renderStandardBlock(block, i, j, k);
            rendered++;
        }
        else if (ter.isNeighborSolid(1, 0, 0))  //east
        {
            block.setBlockBounds(1f - cw, bi, cw, 1f, cw, cw + rw);
            rb.renderStandardBlock(block, i, j, k);
            rendered++;
        }

        //w
        if (rut[0][0][1] == 0)
        {
            block.setBlockBounds(0F, 0, cw, cw, cw, cw + rw);
            rb.renderStandardBlock(block, i, j, k);
            rendered++;
        }
        else if (ter.isNeighborSolid(0, -1, 0))  //below
        {
            block.setBlockBounds(bi, 0, cw, cw, cw, cw + rw);
            rb.renderStandardBlock(block, i, j, k);
            rendered++;
        }
        else if (ter.isNeighborSolid(-1, 0, 0))  //west
        {
            block.setBlockBounds(0F, bi, cw, cw, cw, cw + rw);
            rb.renderStandardBlock(block, i, j, k);
            rendered++;
        }

        //Middle
        //nw
        if (rut[0][1][2] == 0)
        {
            block.setBlockBounds(0F, cw, 1f - cw, cw, cw + rw, 1f);
            rb.renderStandardBlock(block, i, j, k);
            rendered++;
        }
        else if (ter.isNeighborSolid(0, 0, 1))  //north
        {
            block.setBlockBounds(bi, cw, 1f - cw, cw, cw + rw, 1f);
            rb.renderStandardBlock(block, i, j, k);
            rendered++;
        }
        else if (ter.isNeighborSolid(-1, 0, 0))  //west
        {
            block.setBlockBounds(0F, cw, 1f - cw, cw, cw + rw, 1f - bi);
            rb.renderStandardBlock(block, i, j, k);
            rendered++;
        }

        //ne
        if (rut[2][1][2] == 0)
        {
            block.setBlockBounds(1f - cw, cw, 1f - cw, 1f, cw + rw, 1f);
            rb.renderStandardBlock(block, i, j, k);
            rendered++;
        }
        else if (ter.isNeighborSolid(0, 0, 1))  //north
        {
            block.setBlockBounds(1f - cw, cw, 1f - cw, 1f - bi, cw + rw, 1f);
            rb.renderStandardBlock(block, i, j, k);
            rendered++;
        }
        else if (ter.isNeighborSolid(1, 0, 0))  //east
        {
            block.setBlockBounds(1f - cw, cw, 1f - cw, 1f, cw + rw, 1f - bi);
            rb.renderStandardBlock(block, i, j, k);
            rendered++;
        }

        //sw
        if (rut[0][1][0] == 0)
        {
            block.setBlockBounds(0, cw, 0f, cw, cw + rw, cw);
            rb.renderStandardBlock(block, i, j, k);
            rendered++;
        }
        else if (ter.isNeighborSolid(0, 0, -1))  //south
        {
            block.setBlockBounds(bi, cw, 0f, cw, cw + rw, cw);
            rb.renderStandardBlock(block, i, j, k);
            rendered++;
        }
        else if (ter.isNeighborSolid(-1, 0, 0))  //west
        {
            block.setBlockBounds(0, cw, bi, cw, cw + rw, cw);
            rb.renderStandardBlock(block, i, j, k);
            rendered++;
        }

        //se
        if (rut[2][1][0] == 0)
        {
            block.setBlockBounds(1f - cw, cw, 0f, 1f, cw + rw, cw);
            rb.renderStandardBlock(block, i, j, k);
            rendered++;
        }
        else if (ter.isNeighborSolid(0, 0, -1))  //south
        {
            block.setBlockBounds(1f - cw, cw, 0f, 1f - bi, cw + rw, cw);
            rb.renderStandardBlock(block, i, j, k);
            rendered++;
        }
        else if (ter.isNeighborSolid(1, 0, 0))  //east
        {
            block.setBlockBounds(1f - cw, cw, bi, 1f, cw + rw, cw);
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



}
