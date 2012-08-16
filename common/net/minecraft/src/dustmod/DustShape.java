/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.minecraft.src.dustmod;

import java.util.ArrayList;

import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

/**
 *
 * @author billythegoat101
 */
public class DustShape
{
    public static final int n = -1;

    public int width;
    public int length;
    public int height;
    public boolean solid = true;
    public boolean isPower = false;

    public String name;
    private String pName = "";
    private String notes = "";
    private String author = "";
    protected String desc = "";
    protected int[][][] data;

    public int[] manRot = new int[8];
    private int[] setPos = new int[] {0, 0, 0};

    public int cy, cx;
    public int oy, ox;

    public ArrayList<ArrayList<int[][]>> blocks;

    public int[] dustAmt;

    public final int id;

    
    public ArrayList<Integer> allowedVariable;
    
    /**
     * 
     * The generic shape class that should be created to assign each rune its design.
     * Note: The 'name' parameter is used to identify the rune in save files and once set should not be changed. If you want to set the name that is displayed, use DustShape.setRuneName()
     * See picture: 
     * 
     * 
     * @param w     Rune size width (x)
     * @param l     Rune size length (z)
     * @param name  Code name for the rune.
     * @param solid     Is this rune a solid color. Mostly useful for runes who are entirely made out of variable and should only be one dust (like fire trap) 
     * @param ox    X offset for the edge of the rune
     * @param oy    Y offset for the edge of the rune
     * @param cx    X offset for the center of the rune
     * @param cy    Y offset for the center of the rune
     * @param id    unique rune id
     */
    public DustShape(int w, int l, String name, boolean solid, int ox, int oy, int cx, int cy, int id)
    {
        
        if(w > 22 || l > 32){
            throw new IllegalArgumentException("Rune dimensions too big! Max:22x32");
        }
        
        this.id = id;
        this.name = name;
        width = w;
        length = l;
        height = 1;
        data = new int[height][width][length];
        dustAmt = new int[1000];
        this.solid = solid;
        this.cy = cy;
        this.cx = cx;
        this.oy = oy;
        this.ox = ox;

        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                for (int z = 0; z < length; z++)
                {
                    data[y][x][z] = 0;
                }
            }
        }

        if (ox < 0 || oy < 0)
        {
            throw new IllegalArgumentException("Dust offset cannot be negative.");
        }

        blocks = new ArrayList();
        allowedVariable = new ArrayList();
        int[] test = getBlockCoord(ox + w, oy + l);
        int bwidth = test[0] + 2; //(int)Math.ceil((((double)w+Math.abs(oy))/4.0));
        int bheight = test[1] + 2; //(int)Math.ceil(((double)(l+Math.abs(ox))/4.0));

//        System.out.println("fuckitalllll " + bwidth + " " + bheight);
        for (int i = 0; i < bwidth; i++)
        {
            blocks.add(new ArrayList());

            for (int j = 0; j < bheight; j++)
            {
                blocks.get(i).add(new int[4][4]);
            }
        }

//        System.out.println("Derpsize " + bwidth + " " + bheight + " " + blocks.size() + " " + blocks.get(0).size() + " " + name);
        updateData();
    }

    /**
     * Set the text for the notes/sacrifices page in the Tome
     * @param n Raw string to display
     * @return  This DustShape
     */
    public DustShape setNotes(String n)
    {
        this.notes = n;
        return this;
    }

    /**
     * Set the text for the description page in the Tome
     * @param n Raw string to display
     * @return  This DustShape
     */
    public DustShape setDesc(String d)
    {
        this.desc = d;
        if(!allowedVariable.isEmpty()){
        	desc += "\n----\nAllowed Variable Dusts:\n";
        	for(int i: allowedVariable){
        		desc += DustItemManager.names[i] + "\n";
        	}
        }
        return this;
    }


    /**
     * Sets the manual rotation offset for when being placed by scrolls.
     * Array format:
     * {x0,y0,x1,y1,x2,y2,x3,y3}
     * x0 and y0 represent the x and y offsets for when the player is rotated in the '0' direction
     * x1 and y1 represent the x and y offsets for when the player is rotated in the '1' direction
     * The offset is in terms of whole blocks.
     * You're just gonna have to guess check and revise on this one unfortunately.
     * 
     * @param n Raw string to display
     * @return  This DustShape
     */
    public DustShape setManualRotationDerp(int[] derp)
    {
        this.manRot = derp;
        return this;
    }

    
    /**
     * Add dust values to the allowed variable dust list. 
     * If this function is never called (and the allowedVariable list is empty) then
     * all dust's are allowed;
     * 
     * @param dustValue	Value to allow	
     * @return			This DustShape for convenience
     */
    public DustShape addAllowedVariable(int... dustValue){
    	for(int i:dustValue)
    		allowedVariable.add(i);
    	
    	if(desc.contains("\n----\nAllowed Variable Dusts:\n")){
    		setDesc(desc.substring(0,desc.indexOf("\n----\nAllowed Variable Dusts:\n")));
    	}
    	
    	return this;
    }
    
    public boolean isDustAllowedAsVariable(int dustValue){
    	return allowedVariable.contains(dustValue) || allowedVariable.isEmpty();
    }
    
    private void updateData()
    {
        blocks = updateData(data, oy, ox);
    }
    private ArrayList<ArrayList<int[][]>> updateData(int[][][] tdata, int tox, int toy)
    {
        int w = tdata[0].length;
        int l = tdata[0][0].length;

        if (tdata == this.data)
        {
            width = w;
            length = l;
        }

        ArrayList<ArrayList<int[][]>>  tblocks = new ArrayList();
        int[] test = getBlockCoord(tox + w, toy + l, tox, toy);
        int bwidth = test[0] + 2; //(int)Math.ceil((((double)w+Math.abs(oy))/4.0));
        int bheight = test[1] + 2; //(int)Math.ceil(((double)(l+Math.abs(ox))/4.0));

//        System.out.println("fuckitalllll " + bwidth + " " + bheight);
        for (int i = 0; i < bwidth; i++)
        {
            tblocks.add(new ArrayList());

            for (int j = 0; j < bheight; j++)
            {
                tblocks.get(i).add(new int[4][4]);
            }
        }

        dustAmt = new int[1000];

        for (int y = 0; y < tdata.length; y++)
            for (int x = 0; x < tdata[0].length; x++)
            {
                for (int z = 0; z < tdata[0][0].length; z++)
                {
                    int[] c = getBlockCoord(x, z, tox, toy);
                    int to = tdata[y][x][z];

                    if (to == -1)
                    {
                        to = -2;
                    }

//                System.out.println("what the dick " + Arrays.toString(c));
                    tblocks.get(c[0]).get(c[1])[c[2]][c[3]] = to;

                    if (to >= 0)
                    {
                        dustAmt[to] ++;
                    }
                }
            }

        return tblocks;
//        System.out.println("Dust amount registered " + name + " " + Arrays.toString(dustAmt));
    }

    public int[] getBlockCoord(int x, int z)
    {
        return getBlockCoord(x, z, oy, ox);
    }
    public int[] getBlockCoord(int x, int z, int tox, int toy)
    {
        int i = (int)Math.floor((x + tox) / 4);
        int j = (int)Math.floor((z + toy) / 4);
        int nx = x + tox - i * 4;
        int nz = z + toy - j * 4;
//        if(i >= blocks.size() || j >= blocks.get(0).size()) System.out.println("Derp bx:" + i + " by:" + j + " nx:" + nx + " nz:" + nz );
//        if(nx < 0) nx = 0;
//        if(nz < 0) nz = 0;
        return new int[] {i, j, nx, nz};
    }
    public DustShape setDataAt(int x, int y, int z, int b)
    {
        data[y][x][z] = b;
        setPos[0] = x;
        setPos[1] = y;
        setPos[2] = z;
        updateData();
        return this;
    }

    public int getDataAt(int x, int y, int z)
    {
        return data[y][x][z];
    }

    public void setData(int[][][] data)
    {
        this.data = data;
        updateData();
    }
    public int[][][] getData()
    {
        return data;
    }

    public void translate(int x, int y, int z, int value)
    {
        data[setPos[1]][setPos[0]][setPos[2]] = value;
        setPos[0] += x;
        setPos[y] += y;
        setPos[z] += z;
    }

    /**
     *
     * @param d
     * @param m
     * @return -1: no match, 0: match but incomplete, 1:complete match
     */
    public int compareData(int[][] d)
    {
        //int h = d.length;
        int w = d.length;
        int l = d[0].length;
//        if(h > height) return -1;
//        System.out.println("WHYYY " + w + " " + l + " " + width + " " + length);
//        if(w > width) return -1;
//        if(l > length) return -1;
//        int dh = height - h;
        int dw = width - w;
        int dl = length - l;

        if (dw < 0 || dl < 0)
        {
            dw = width - l;
            dl = length - w;
        }

        int rot = -1;
        
//        System.out.println("potato " + dw + " " + dl);
        for (int x = 0; x <= dw; x++)
        {
            for (int z = 0; z <= dl; z++)
            {
                if ((rot = compareChunk(d, x, 0, z)) == -1)
                {
//                    System.out.println("dicks");
                    return -1;
                }
            }
        }

        w = d.length;
        l = d[0].length;

        if (solid)
        {
            int compare = 0;

            for (int x = 0; x < w; x++)
            {
                for (int z = 0; z < l; z++)
                {
                    int iter = d[x][z];

                    if (compare == 0 && iter != 0)
                    {
                        compare = iter;
                    }
                    else if (compare != 0)
                    {
                        if (compare != iter && iter != 0)
                        {
//                            System.out.println("Rune mulicolored");
                            return -1;
                        }
                    }
                }
            }
        }

        if ((w == width && l == length) || (w == length && l == width))
        {
            return rot;
        }

        return -1;
    }

    protected int compareChunk(int[][] d, int ox, int oy, int oz)
    {
//        int h = d.length;
        //I shouldn't have to put this here, but it works
        width = data[0].length;
        length = data[0][0].length;
        boolean equal = true;

//        for(int y = 0; y < h; y++){
        for (int rot = 0; rot < 4; rot++)
        {
            int w = d.length;
            int l = d[0].length;

//            if(name.equals("bomb")){
//                System.out.println("WIDTHxLENGTH " + w + "x" + l + " " + width + "x" + length);
//                System.out.println("DURR1 " + (w != width) + " " + (l != length));
//                System.out.println("DURR2 " + (d.length != data[0].length) + " " + (d[0].length != data[0][0].length));
//                System.out.println("blarg " + Arrays.deepToString(d));
//                System.out.println("first " + Arrays.deepToString(data[0]));
//            }
            if (w != width || l != length)
            {
//                System.out.println("firsderp");
                equal = false;
            }

            kill:

            for (int x = 0; x < w && equal; x++)
            {
                for (int z = 0; z < l && equal; z++)
                {
                    try
                    {
                        if (x >= width || 
                        	z >= length ||
                        	(d[x][z] != data[oy][x + ox][z + oz] && !(d[x][z] != 0 && data[oy][x + ox][z + oz] == -1)) ||
                        	(data[oy][x + ox][z + oz] == -1 && !this.isDustAllowedAsVariable(d[x][z])))
                        {
                            //                        System.out.println("Derp0 [" + x + "," + z + "][" + (x+oy) + "," + (z+oy) + "] ");
                            equal = false;
                            break kill;
                        }
                    }
                    catch (Exception e)
                    {
                        ///dont feel like figuring out the outofbounds exception right now >_>
//                            System.out.println("DERP comparechunk");
                        equal = false;
                        break kill;
                    }
                }
            }

            if (equal)
            {
                return rot;
            }

            d = rotateMatrix(d);
            equal = true;
        }

        d = flipMatrix(d);
//        System.out.println("FLIP");

        for (int rot = 0; rot < 4; rot++)
        {
            int w = d.length;
            int l = d[0].length;

//            if(name.equals("bomb")){
//                System.out.println("WIDTHxLENGTH " + w + "x" + l + " " + width + "x" + length);
//                System.out.println("DURR1 " + (w != width) + " " + (l != length));
//                System.out.println("DURR2 " + (d.length != data[0].length) + " " + (d[0].length != data[0][0].length));
//                System.out.println("blarg " + Arrays.deepToString(d));
//                System.out.println("first " + Arrays.deepToString(data[0]));
//            }
            if (w != width || l != length)
            {
//                System.out.println("firsderp");
                equal = false;
            }

            kill:

            for (int x = 0; x < w && equal; x++)
            {
                for (int z = 0; z < l && equal; z++)
                {
                    try
                    {
                        if (x >= width || 
                        	z >= length ||
                        	(d[x][z] != data[oy][x + ox][z + oz] && !(d[x][z] != 0 && data[oy][x + ox][z + oz] == -1)) ||
                        	(data[oy][x + ox][z + oz] == -1 && !this.isDustAllowedAsVariable(d[x][z])))
                        {
                            //                        System.out.println("Derp0 [" + x + "," + z + "][" + (x+oy) + "," + (z+oy) + "] ");
                            equal = false;
                            break kill;
                        }
                    }
                    catch (Exception e)
                    {
                        ///dont feel like figuring out the outofbounds exception right now >_>
//                            System.out.println("DERP comparechunk");
                        equal = false;
                        break kill;
                    }
                }
            }

            if (equal)
            {
                return rot;
            }

            d = rotateMatrix(d);
            equal = true;
        }

//            System.out.println("second " + Arrays.deepToString(data[0]));
//        second:
//        for(int x = 0; x < l; x++){
//            for(int z = 0; z < w; z++){
//                if(x >= width || z >= length || (d[x][z] != -1 && d[x][z] != data[ox][x + oy][z + oy])){
////                        System.out.println("Derp1 [" + x + "," + z + "][" + (x+oy) + "," + (z+oy) + "] ");
//                    blargh = false;
//                    break second;
//                }
//            }
//        }
//        if(blargh) return true;
//        d = rotateMatrix(d);
//        blargh = true;
////            System.out.println("third " + Arrays.deepToString(data[0]));
//        third:
//        for(int x = 0; x < w; x++){
//            for(int z = 0; z < l; z++){
//                if(x >= width || z >= length || (d[x][z] != -1 && d[x][z] != data[ox][x + oy][z + oy])){
////                        System.out.println("Derp2 [" + x + "," + z + "][" + (x+oy) + "," + (z+oy) + "] ");
//                    blargh = false;
//                    break third;
//                }
//            }
//        }
//        if(blargh) return true;
//        d = rotateMatrix(d);
//        blargh = true;
////            System.out.println("fourth " + Arrays.deepToString(data[0]));
//        fourth:
//        for(int x = 0; x < l; x++){
//            for(int z = 0; z < w; z++){
//                if(x >= width || z >= length || (d[x][z] != -1 && d[x][z] != data[ox][x + oy][z + oy])){
////                        System.out.println("Derp3 [" + x + "," + z + "][" + (x+oy) + "," + (z+oy) + "] ");
//                    blargh = false;
//                    break fourth;
//                }
//            }
//        }
//        if(blargh) return true;
//        d = rotateMatrix(d);
//        }
//            System.out.println("returning false");
        return -1;
    }

    public static int[][] rotateMatrix(int[][] mat)
    {
        int[][] rtn = new int[mat[0].length][mat.length];
        int M = mat.length;
        int N = mat[0].length;

        for (int r = 0; r < M; r++)
        {
            for (int c = 0; c < N; c++)
            {
                rtn[c][M - 1 - r] = mat[r][c];
            }
        }

        return rtn;
    }

    public static int[][] flipMatrix(int[][] mat)
    {
        int[][] rtn = new int[mat.length][mat[0].length];
        int w = mat.length;
        int l = mat[0].length;

        for (int x = 0; x < w; x++)
        {
            for (int y = 0; y < l; y++)
            {
                rtn[w - x - 1][y] = mat[x][y];
            }
        }

        return rtn;
    }

    public boolean drawOnWorld(World w, int i, int j, int k, EntityPlayer p, int r)
    {
        j++;
        //yes, I know this rotation code is bull, but i'm getting fed up with it
        r = (r + 3) % 4;

//        System.out.println("r " + r);

        if (r == 3)
        {
            r = 1;
        }
        else if (r == 1)
        {
            r = 3;
        }

//        System.out.println("Drawing " + r);
        int si = i, sk = k;
        int tcx = cy, tcy = cx, tox = oy, toy = ox;
        int[][][] tdata = new int[height][width][length];
        ArrayList<ArrayList<int[][]>> tblocks;

        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                for (int z = 0; z < length; z++)
                {
                    tdata[y][x][z] = data[y][x][z];
                }
            }
        }

        for (int rot = 0; rot < r; rot++)
        {
            tdata[0] = rotateMatrix(tdata[0]);
        }

        int tw = (int)Math.floor(data[0].length / 4);
        tw *= 4;
        int th = (int)Math.floor(data[0][0].length / 4);
        th *= 4;

        switch (r)
        {
            case 0:
//                si--;
                si += manRot[0];
                sk += manRot[1];
                break;

            case 1:
                tox = ox;
                toy = tw - ((data[0].length + oy) % 4);
//                tcx = cx;
//                tcy = tw-((data[0].length+cy)%4);
//                toy+=ox;
//                tcx = cx+th;
//                tcy = cy+tw;
                si += manRot[2];
                sk += manRot[3];
                break;

            case 2:
                tox = tw - ((data[0].length + oy) % 4);
                toy = th - ((data[0][0].length + ox) % 4);
//                tcx = tw-((data[0].length+cy)%4)-tox;
//                tcy = th-((data[0][0].length+cx)%4)-toy;
//                tox+=oy;
//                tcx = width-cy;
//                tcy = length-cx;
                si += manRot[4];
                sk += manRot[5];
                break;

            case 3:
                tox = th - ((data[0][0].length + ox) % 4);
                toy = oy;
//                tcx = th-((data[0][0].length+cx)%4)-tox;
//                tcy = cy;
//                sk--;
//                tcx = cx+th;
//                tcy = cy+tw;
                si += manRot[6];
                sk += manRot[7];
                break;
        }

        tblocks = updateData(tdata, tox, toy);
        int[] temp = this.getBlockCoord(tcx, tcy, tox, toy);
        si -= temp[0];
        sk -= temp[1];
//        System.out.println("DICKS offest:" + temp[0] + " " + temp[1] + " do:" + tox + "," + toy + " dim:" + width + "," + length + " r:" + r) ;
        int[] pDustAmount = new int[1000];

        for (ItemStack is: p.inventory.mainInventory)
        {
            if (is != null && is.itemID == DustMod.idust.shiftedIndex)
            {
                pDustAmount[is.getItemDamage()] += is.stackSize;
            }
        }

        if (!hasEnough(pDustAmount) && !p.capabilities.isCreativeMode)
        {
            p.addChatMessage("Not enough dust!");
//            data = backup;
//            updateData();
            return false;
        }

        pDustAmount = new int[1000];

        for (int x = 0; x < tblocks.size(); x++)
        {
            for (int z = 0; z < tblocks.get(0).size(); z++)
            {
                if (w.getBlockId(si + x, j, sk + z) != 0 && !(DustMod.isDust(w.getBlockId(si + x, j, sk + z)) && w.getBlockMetadata(si + x, j, sk + z) == 2) && w.getBlockId(si + x, j, sk + z) != Block.tallGrass.blockID)
                {
                    continue;
                }//return false;

                if (w.getBlockId(si + x, j - 1, sk + z) == 0)
                {
                    continue;
                }//return false;

                if (!DustMod.dust.canPlaceBlockAt(w, si+x, j, sk+z))
                {
                    continue;
                }//return false;

                w.setBlockWithNotify(si + x, j, sk + z, 0);
                w.setBlockWithNotify(si + x, j, sk + z, DustMod.dust.blockID);
                TileEntityDust ted;
                TileEntity te = w.getBlockTileEntity(si + x, j, sk + z);

                if (te != null && te instanceof TileEntityDust)
                {
                    ted = (TileEntityDust)te;
                    w.setBlockMetadata(si + x, j, sk + z, 0);
                }
                else
                {
                    ted = new TileEntityDust();
                    w.setBlockTileEntity(si + i, j, sk + k, ted);
                }

                ted.empty();
                int[][] block = tblocks.get(x).get(z);

                for (int ii = 0; ii < 4; ii++)
                    for (int ij = 0; ij < 4; ij++)
                    {
//                        System.out.println("blargh [" + ii + "," + ij + "] " + block[ii][ij]);
                        ted.setDust(ii, ij, block[ii][ij]);

                        if (block[ii][ij] > 0)
                        {
                            pDustAmount[block[ii][ij]]++;
                        }
                    }
            }
        }

        for (int x = 0; x < tblocks.size(); x++)
        {
            for (int z = 0; z < tblocks.get(0).size(); z++)
            {
                if (DustMod.isDust(w.getBlockId(si + x, j, sk + z)))
                {
                    TileEntityDust ted = (TileEntityDust)w.getBlockTileEntity(si + x, j, sk + z);

                    if (ted.isEmpty())
                    {
                        w.setBlockWithNotify(si + x, j, sk + z, 0);
                    }
                    else
                    {
                        w.markBlockNeedsUpdate(si + x, j, sk + z);
                    }
                }
            }
        }

//        System.out.println("Dust Used " + Arrays.toString(pDustAmount));
        if (!p.capabilities.isCreativeMode)
        {
            for (int id = 1; id < 1000; id++)
            {
                for (int sind = 0; sind < p.inventory.mainInventory.length; sind++)
                {
                    ItemStack is = p.inventory.mainInventory[sind];

                    if (is != null && is.itemID == DustMod.idust.shiftedIndex && is.getItemDamage() == id && pDustAmount[id] > 0)
                    {
                        while (pDustAmount[id] > 0 && is.stackSize > 0)
                        {
                            is.stackSize--;

                            if (is.stackSize == 0)
                            {
                                p.inventory.mainInventory[sind] = null;
                            }

                            pDustAmount[id] --;
                        }
                    }
                }
            }

            p.inventory.onInventoryChanged();
        }

//        data = backup;
        updateData();
        return true;
    }

    public boolean isEmpty(int[][] block)
    {
        for (int[] i: block)
        {
            for (int j: i)
            {
                if (j != 0)
                {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean hasEnough(int[] dust)
    {
        for (int i = 1; i < 1000; i++)
        {
            if (dust[i] < dustAmt[i])
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Set the proper name for the rune.
     * @param n The name
     * @return  This DustShape
     */
    public DustShape setRuneName(String n)
    {
        pName = n;
        return this;
    }
    
    /**
     * Set the mod-author's name of this rune
     * @param name Your screenname
     * @return this DustShape
     */
    public DustShape setAuthor(String name){
        this.author = name;
        return this;
    }

    public String getRuneName()
    {
        if(pName.isEmpty()){
            return name + ".propername";
        }
        return pName;
    }

    public String getDescription()
    {
        if(desc.isEmpty()){
            return name + ".desc";
        }
        return desc;
    }
    
    public String getAuthor(){
        if(author.isEmpty()){
            return name + ".author";
        }
        return author;
    }

    public String getNotes()
    {
        if(notes.isEmpty()){
            return name + ".notes";
        }
        return notes;
    }
}
