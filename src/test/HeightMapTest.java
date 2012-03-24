import java.io.File;
import java.util.Random;

import org.spoutcraft.client.chunkcache.HeightMap;


public class HeightMapTest {
	
	private static final int RECORDS = 25000;
	
	public static void main(String args[]) {
		Random rand = new Random();
		Data data[] = new Data[RECORDS];
		int j = 0;
		for(int x = 0; x < 16; x++) {
			for(int z = 0; z < 16; z++) {
				Data d = new Data();
				do {
					d.x = rand.nextInt(512);
					d.z = rand.nextInt(512);
				} while (contains(data, d));
				d.h = (short) rand.nextInt(256);
				d.id = (byte) rand.nextInt(128);
				data[j] = d;
				j++;
			}
		}
		for(int i = 256; i < RECORDS; i++) {
			Data d = new Data();
			do {
				d.x = rand.nextInt(512);
				d.z = rand.nextInt(512);
			} while (contains(data, d));
			d.h = (short) rand.nextInt(256);
			d.id = (byte) rand.nextInt(128);
			data[i] = d;
		}
		HeightMap map = HeightMap.getHeightMap("test", new File("test.hma"));
		map.clear();
		
		//SETTING VALUES
		
		long nanos = System.nanoTime();
		for(int i = 0; i < RECORDS; i++) {
			Data d = data[i];
			map.setHighestBlock(d.x, d.z, d.h, d.id);
		}
		System.out.println("Setting values took " + (System.nanoTime() - nanos) + " ns");
		
		//SETTING VALUES AGAIN
		
		nanos = System.nanoTime();
		for(int i = 0; i < RECORDS; i++) {
			Data d = data[i];
			map.setHighestBlock(d.x, d.z, d.h, d.id);
		}
		System.out.println("Setting values again took " + (System.nanoTime() - nanos) + " ns");
		
		//GETTING VALUES
		
		nanos = System.nanoTime();
		for(int i = 0; i < RECORDS; i++) {
			Data d = data[i];
			short h = map.getHeight(d.x, d.z);
			byte id = map.getBlockId(d.x, d.z);
			if(h != d.h || id != d.id) {
				System.out.println("Failed test: height store: x = " + d.x + " z = " + d.z + " d.h = " + d.h + " h = " + h);
			}
		}
		System.out.println("Checking values took " + (System.nanoTime() - nanos) + " ns");
		
		//SAVING
		
		nanos = System.nanoTime();
		map.save();
		System.out.println("Saving took " + (System.nanoTime() - nanos) + " ns");
		
		//LOADING
		
		nanos = System.nanoTime();
		map.load();
		System.out.println("Loading took " + (System.nanoTime() - nanos) + " ns");
		
		//CHECKING PERSISTENCE
		
		nanos = System.nanoTime();
		for(int i = 0; i < RECORDS; i++) {
			Data d = data[i];
			short h = map.getHeight(d.x, d.z);
			byte id = map.getBlockId(d.x, d.z);
			if(h != d.h || id != d.id) {
				System.out.println("Failed test: persistent height store: x = " + d.x + " z = " + d.z + " d.h = " + d.h + " h = " + h);
			}
		}
		System.out.println("Checking persistence took " + (System.nanoTime() - nanos) + " ns");
	}

	private static boolean contains(Data[] data, Data d) {
		for(Data o:data) {
			if(o == null) {
				continue;
			}
			if(o.x == d.x && o.z == d.z) {
				return true;
			}
		}
		return false;
	}
}

