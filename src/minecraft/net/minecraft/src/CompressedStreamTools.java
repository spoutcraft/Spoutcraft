package net.minecraft.src;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class CompressedStreamTools {
	public CompressedStreamTools() {
	}

	public static NBTTagCompound loadGzippedCompoundFromOutputStream(InputStream inputstream)
	throws IOException {
		DataInputStream datainputstream = new DataInputStream(new BufferedInputStream(new GZIPInputStream(inputstream)));
		try {
			NBTTagCompound nbttagcompound = read(datainputstream);
			return nbttagcompound;
		}
		finally {
			datainputstream.close();
		}
	}

	public static void writeGzippedCompoundToOutputStream(NBTTagCompound nbttagcompound, OutputStream outputstream)
	throws IOException {
		DataOutputStream dataoutputstream = new DataOutputStream(new GZIPOutputStream(outputstream));
		try {
			writeTo(nbttagcompound, dataoutputstream);
		}
		finally {
			dataoutputstream.close();
		}
	}

	public static NBTTagCompound loadMapFromByteArray(byte abyte0[])
	throws IOException {
		DataInputStream datainputstream = new DataInputStream(new BufferedInputStream(new GZIPInputStream(new ByteArrayInputStream(abyte0))));
		try {
			NBTTagCompound nbttagcompound = read(datainputstream);
			return nbttagcompound;
		}
		finally {
			datainputstream.close();
		}
	}

	public static byte[] writeMapToByteArray(NBTTagCompound nbttagcompound)
	throws IOException {
		ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
		DataOutputStream dataoutputstream = new DataOutputStream(new GZIPOutputStream(bytearrayoutputstream));
		try {
			writeTo(nbttagcompound, dataoutputstream);
		}
		finally {
			dataoutputstream.close();
		}
		return bytearrayoutputstream.toByteArray();
	}

	public static void saveMapToFileWithBackup(NBTTagCompound nbttagcompound, File file)
	throws IOException {
		File file1 = new File((new StringBuilder()).append(file.getAbsolutePath()).append("_tmp").toString());
		if (file1.exists()) {
			file1.delete();
		}
		saveMapToFile(nbttagcompound, file1);
		if (file.exists()) {
			file.delete();
		}
		if (file.exists()) {
			throw new IOException((new StringBuilder()).append("Failed to delete ").append(file).toString());
		}
		else {
			file1.renameTo(file);
			return;
		}
	}

	public static void saveMapToFile(NBTTagCompound nbttagcompound, File file)
	throws IOException {
		DataOutputStream dataoutputstream = new DataOutputStream(new FileOutputStream(file));
		try {
			writeTo(nbttagcompound, dataoutputstream);
		}
		finally {
			dataoutputstream.close();
		}
	}

	public static NBTTagCompound writeMapToFileUncompressed(File file)
	throws IOException {
		if (!file.exists()) {
			return null;
		}
		DataInputStream datainputstream = new DataInputStream(new FileInputStream(file));
		try {
			NBTTagCompound nbttagcompound = read(datainputstream);
			return nbttagcompound;
		}
		finally {
			datainputstream.close();
		}
	}

	public static NBTTagCompound read(DataInput datainput)
	throws IOException {
		NBTBase nbtbase = NBTBase.readTag(datainput);
		if (nbtbase instanceof NBTTagCompound) {
			return (NBTTagCompound)nbtbase;
		}
		else {
			throw new IOException("Root tag must be a named compound tag");
		}
	}

	public static void writeTo(NBTTagCompound nbttagcompound, DataOutput dataoutput)
	throws IOException {
		NBTBase.writeTag(nbttagcompound, dataoutput);
	}
}
