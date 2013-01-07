package dustmodtestpack;

import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import dustmod.DustManager;
import dustmod.DustShape;
import dustmod.InscriptionEvent;
import dustmod.InscriptionManager;
import dustmodtestpack.inscriptions.BlinkerInscription;
import dustmodtestpack.inscriptions.ErfBendInscription;
import dustmodtestpack.inscriptions.FireballInscription;
import dustmodtestpack.inscriptions.GlideInscription;
import dustmodtestpack.inscriptions.MountainCutterInscription;
import dustmodtestpack.inscriptions.RespawnInscription;
import dustmodtestpack.inscriptions.VoidInscription;
//TODO: Missing sources, disabled this
//import dustmodtestpack.inscriptions.WaterAffinity;
//import dustmodtestpack.runes.LaunchTestRune;

/**
 * This pack is meant for testing runes & inscriptions as a separate download 
 * to make sure that the added content is balanced and fair.
 *  
 * @author billythegoat101
 *
 */
@Mod(modid = "DustModTestPack", name = "Dust mod Test pack", version = "0.03", dependencies="after:DustMod")
@NetworkMod(clientSideRequired=false, serverSideRequired=false)
public class DustModTestPack {

	
	@Instance("DustModTestPack")
    public static DustModTestPack instance;
    
	@PostInit
	public void postInit(FMLPostInitializationEvent evt){
		registerDusts();
		registerRunes();
		registerInscriptions();
	}
	
    
	public void registerDusts() {
		
	}
	
	public void registerRunes() {


        int N = -1;
        int P = 100;
        int G = 200;
        int L = 300;
        int B = 400;
        DustShape s;
        int[][][] values;
        
		
        s = new DustShape(4, 4, "launchtest", false, 0, 0, 0, 0, 200);
        values = new int[][][]
        {
            {
                {G, 0, 0, G},
                {0, G, G, 0},
                {0, G, G, 0},
                {G, 0, 0, G}
            }
        };
        s.setData(values);
        s.setRuneName("Torch Rune");
        s.setNotes("Sacrifice:\n\n"
                + "-None: Normal torch spawn.\n"
                + "-1xFlint: Beacon rune.\n"
                + "\nNotes:\n\n"
                + "=Sacrificing a dye to an existing beacon will change its color.");
        s.setDesc("Description:\n\n"
                + "Spawns a torch or, if a piece of flint is sacrficed, a beacon.");
        s.setAuthor("billythegoat101");
        s.setManualRotationDerp(new int[] {00, 00, 0, 0, 0, 0, 0, 0});
        //TODO: Missing source, disabled
        //DustManager.registerLocalDustShape(s, new LaunchTestRune());
	}
	
	public void registerInscriptions(){

        int N = -1;
        int P = 100;
        int G = 200;
        int L = 300;
        int B = 400;
		
		InscriptionEvent evt = null;
		int[][] design;

		design = new int[][]{
				{0,P,P,0},
				{P,G,G,P},
				{P,G,G,P},
				{0,P,P,0}
		};
		evt = new MountainCutterInscription(design, "cut","Moutain Cutter", 0);
		InscriptionManager.registerInscriptionEvent(evt);

		
		
		design = new int[][]{
				{0,P,P,0},
				{P,L,L,P},
				{P,L,L,P},
				{0,P,P,0}
		};
		evt = new ErfBendInscription(design, "erfbendin","ERF BENDIN", 2);
		InscriptionManager.registerInscriptionEvent(evt);
		
		
		
		design = new int[][]{
				
				{0,0,0,0,B,B,B,B,0,0,0,0},
				{0,0,0,0,B,G,G,B,0,0,0,0},
				{0,0,0,0,G,G,G,G,0,0,0,0},
				{0,0,0,G,B,G,G,B,G,0,0,0},
				{B,B,G,B,B,G,G,B,B,G,B,B},
				{B,G,G,G,G,B,B,G,G,G,G,B},
				{B,G,G,G,G,B,B,G,G,G,G,B},
				{B,B,G,B,B,G,G,B,B,G,B,B},
				{0,0,0,G,B,G,G,B,G,0,0,0},
				{0,0,0,0,G,G,G,G,0,0,0,0},
				{0,0,0,0,B,G,G,B,0,0,0,0},
				{0,0,0,0,B,B,B,B,0,0,0,0}
		};
		evt = new RespawnInscription(design, "respawn","Respawn", 1);
		InscriptionManager.registerInscriptionEvent(evt);

		
		

		design = new int[][]{
				{0,G,G,0},
				{G,L,L,G},
				{G,L,L,G},
				{0,G,G,0}
		};
		evt = new BlinkerInscription(design, "blinker","Blinker", 3, 0);
		InscriptionManager.registerInscriptionEvent(evt);

		
		
		design = new int[][]{
				{0,P,P,0},
				{P,B,B,P},
				{P,B,B,P},
				{0,P,P,0}
		};
		evt = new VoidInscription(design, "voidinscription","Auto-Void Storage", 4);
		InscriptionManager.registerInscriptionEvent(evt);

		
		
		design = new int[][]{
				{0,G,G,0},
				{G,B,B,G},
				{G,B,B,G},
				{0,G,G,0}
		};
		evt = new FireballInscription(design, "fireball","Fire Ball", 5);
		InscriptionManager.registerInscriptionEvent(evt);

		
		
		design = new int[][]{
				{0,G,G,0},
				{G,B,B,G},
				{G,B,B,G},
				{0,G,G,0}
		};
		evt = new GlideInscription(design, "glide","Glideing", 6);
		MinecraftForge.EVENT_BUS.register(evt);
		InscriptionManager.registerInscriptionEvent(evt);

		
		
		design = new int[][]{
				{0,G,G,0},
				{G,B,B,G},
				{G,B,B,G},
				{0,G,G,0}
		};
		//TODO: Missing source, disabled
		//evt = new WaterAffinity(design, "watertest","Water Affinity", 7);
		//MinecraftForge.EVENT_BUS.register(evt);
		//InscriptionManager.registerInscriptionEvent(evt);
		
		
		
		//last id used: 7
	}
	
}
