package net.minecraft.src;

import java.util.ArrayList;
import java.util.Random;

public class ComponentVillageStartPiece extends ComponentVillageWell {
	public WorldChunkManager worldChunkMngr;
	public int field_35109_b;
	public StructureVillagePieceWeight structVillagePieceWeight;
	public ArrayList structureVillageWeightedPieceList;
	public ArrayList field_35108_e;
	public ArrayList field_35106_f;

	public ComponentVillageStartPiece(WorldChunkManager worldchunkmanager, int i, Random random, int j, int k, ArrayList arraylist, int l) {
		super(0, random, j, k);
		field_35108_e = new ArrayList();
		field_35106_f = new ArrayList();
		worldChunkMngr = worldchunkmanager;
		structureVillageWeightedPieceList = arraylist;
		field_35109_b = l;
	}

	public WorldChunkManager getWorldChunkMngr() {
		return worldChunkMngr;
	}
}
