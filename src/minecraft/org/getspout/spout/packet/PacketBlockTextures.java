package org.getspout.spout.packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.src.Block;
import net.minecraft.src.Material;
import net.minecraft.src.StepSound;

public class PacketBlockTextures implements SpoutPacket {
	
	private int id = 97;
	private int[] texture = new int[6];
	private byte byteSS = 127;
	private byte byteMat = 127;
	
	@Override
	public int getNumBytes() {
		// int*6 + int + byte + byte
		return 4*6 + 4 + 1 + 1;
	}

	@Override
	public void readData(DataInputStream input) throws IOException {
		System.out.println("Reading Block Packet");
		// TODO Auto-generated method stub
		texture[0] = input.readInt();
		texture[1] = input.readInt();
		texture[2] = input.readInt();
		texture[3] = input.readInt();
		texture[4] = input.readInt();
		texture[5] = input.readInt();
		id = input.readInt();
		byteSS = input.readByte();
		byteMat = input.readByte();
	}

	@Override
	public void writeData(DataOutputStream output) throws IOException {
		// TODO Auto-generated method stub
		output.writeInt(texture[0]);
		output.writeInt(texture[1]);
		output.writeInt(texture[2]);
		output.writeInt(texture[3]);
		output.writeInt(texture[4]);
		output.writeInt(texture[5]);
		output.writeInt(id);
		output.writeByte(byteSS);
		output.writeByte(byteMat);
	}

	@Override
	public void run(int playerId) {
		// TODO Auto-generated method stub
		StepSound ss = Block.soundStoneFootstep;
        switch (byteSS) { case 0:
          ss = Block.soundClothFootstep; break;
        case 1:
          ss = Block.soundGlassFootstep; break;
        case 2:
          ss = Block.soundGrassFootstep; break;
        case 3:
          ss = Block.soundGravelFootstep; break;
        case 4:
          ss = Block.soundMetalFootstep; break;
        case 5:
          ss = Block.soundPowderFootstep; break;
        case 6:
          ss = Block.soundSandFootstep; break;
        case 7:
          ss = Block.soundStoneFootstep; break;
        case 8:
          ss = Block.soundWoodFootstep; break;
        default:
          ss = Block.soundStoneFootstep;
        }
        
        Material mat = Material.rock;
        switch (byteMat) { 
        case 0: mat = Material.builtSnow; break;
        case 1: mat = Material.cactus; break;
        case 2: mat = Material.cakeMaterial; break;
        case 3: mat = Material.circuits; break;
        case 4: mat = Material.clay; break;
        case 5: mat = Material.cloth; break;
        case 6: mat = Material.fire; break;
        case 7: mat = Material.glass; break;
        case 8: mat = Material.grassMaterial; break;
        case 9: mat = Material.ground; break;
        case 10: mat = Material.ice; break;
        case 11: mat = Material.lava; break;
        case 12: mat = Material.leaves; break;
        case 13: mat = Material.plants; break;
        case 14: mat = Material.portal; break;
        case 15: mat = Material.pumpkin; break;
        case 16: mat = Material.rock; break;
        case 17: mat = Material.sand; break;
        case 18: mat = Material.snow; break;
        case 19: mat = Material.sponge; break;
        case 20: mat = Material.tnt; break;
        case 21: mat = Material.water; break;
        case 22: mat = Material.wood; break;
        }
		System.out.println("Defining Block");
		Block.defineBlockSMP(id, ss, texture, mat);
	}

	@Override
	public void failure(int playerId) {
	}

	@Override
	public PacketType getPacketType() {
		// TODO Auto-generated method stub
		return PacketType.PacketBlockTextures;
	}

	@Override
	public int getVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

}
