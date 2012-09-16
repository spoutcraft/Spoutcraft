/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011-2012, SpoutDev <http://www.spout.org/>
 * Spoutcraft is licensed under the GNU Lesser General Public License.
 *
 * Spoutcraft is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spoutcraft is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.spoutcraft.client.chunkcache;


@SuppressWarnings("unchecked")
public class PartitionChunk {
	
	static public void copyToChunkData(byte[] chunkData, int blockNum, byte[] partition) {
		
		int j = blockNum << 11;

		boolean clear = partition == null;
		
		if (clear) {
			for (int i = 0; i < 2048 && j < chunkData.length; i++) {
				chunkData[j++] = 0;
			}
		} else {
			for (int i = 0; i < 2048 && j < chunkData.length; i++) {
				chunkData[j++] = partition[i];
			}
		}
	}

	static public void copyFromChunkData(byte[] chunkData, int blockNum, byte[] partition) {

		int j = blockNum << 11;

		for (int i = 0; i < 2048 && j < chunkData.length; i++) {
			partition[i] = chunkData[j++];
		}
	}
	
	static public long getHash(byte[] chunkData, int blockNum, int base) {
		int p = blockNum * 8 + base;
		long hash = 0;
		hash = hash << 8 | (((long) chunkData[p++]) & 0xFFL);
		hash = hash << 8 | (((long) chunkData[p++]) & 0xFFL);
		hash = hash << 8 | (((long) chunkData[p++]) & 0xFFL);
		hash = hash << 8 | (((long) chunkData[p++]) & 0xFFL);
		hash = hash << 8 | (((long) chunkData[p++]) & 0xFFL);
		hash = hash << 8 | (((long) chunkData[p++]) & 0xFFL);
		hash = hash << 8 | (((long) chunkData[p++]) & 0xFFL);
		hash = hash << 8 | (((long) chunkData[p++]) & 0xFFL);
		return hash;
	}

	static public void setHash(byte[] chunkData, int blockNum, long hash, int base) {
		int p = blockNum * 8 + base;
		chunkData[p++] = (byte) (hash >> 56);
		chunkData[p++] = (byte) (hash >> 48);
		chunkData[p++] = (byte) (hash >> 40);
		chunkData[p++] = (byte) (hash >> 32);
		chunkData[p++] = (byte) (hash >> 24);
		chunkData[p++] = (byte) (hash >> 16);
		chunkData[p++] = (byte) (hash >> 8);
		chunkData[p++] = (byte) (hash >> 0);
	}
	
	static public int getInt(byte[] chunkData, int blockNum, int base) {
		int p = blockNum * 4 + base;
		int hash = 0;
		hash = hash << 8 | (((int) chunkData[p++]) & 0xFF);
		hash = hash << 8 | (((int) chunkData[p++]) & 0xFF);
		hash = hash << 8 | (((int) chunkData[p++]) & 0xFF);
		hash = hash << 8 | (((int) chunkData[p++]) & 0xFF);
		return hash;
	}

	static public void setInt(byte[] chunkData, int blockNum, int hash, int base) {
		int p = blockNum * 4 + base;
		chunkData[p++] = (byte) (hash >> 24);
		chunkData[p++] = (byte) (hash >> 16);
		chunkData[p++] = (byte) (hash >> 8);
		chunkData[p++] = (byte) (hash >> 0);
	}
}
