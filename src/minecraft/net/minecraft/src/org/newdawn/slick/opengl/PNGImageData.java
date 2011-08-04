package org.newdawn.slick.opengl;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.zip.CRC32;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

import org.lwjgl.BufferUtils;

/**
 * The PNG imge data source that is pure java reading PNGs
 * 
 * @author Matthias Mann (original code)
 */
public class PNGImageData implements LoadableImageData {
    /** The valid signature of a PNG */
    private static final byte[] SIGNATURE = {(byte)137, 80, 78, 71, 13, 10, 26, 10};

    /** The header chunk identifer */
    private static final int IHDR = 0x49484452;
    /** The palette chunk identifer */
    private static final int PLTE = 0x504C5445;
    /** The transparency chunk identifier */
    private static final int tRNS = 0x74524E53;
    /** The data chunk identifier */
    private static final int IDAT = 0x49444154;
    /** The end chunk identifier */
    private static final int IEND = 0x49454E44;
    
    /** Color type for greyscale images */
    private static final byte COLOR_GREYSCALE = 0;
    /** Color type for true colour images */
    private static final byte COLOR_TRUECOLOR = 2;
    /** Color type for indexed palette images */
    private static final byte COLOR_INDEXED = 3;
    /** Color type for greyscale images with alpha */
    private static final byte COLOR_GREYALPHA = 4;
    /** Color type for true colour images with alpha */
    private static final byte COLOR_TRUEALPHA = 6;  
    
    /** The stream we're going to read from */
    private InputStream input;
    /** The CRC for the current chunk */
    private final CRC32 crc;
    /** The buffer we'll use as temporary storage */
    private final byte[] buffer;
    
    /** The length of the current chunk in bytes */
    private int chunkLength;
    /** The ID of the current chunk */
    private int chunkType;
    /** The number of bytes remaining in the current chunk */
    private int chunkRemaining;
    
    /** The width of the image read */
    private int width;
    /** The height of the image read */
    private int height;
    /** The type of colours in the PNG data */
    private int colorType;
    /** The number of bytes per pixel */
    private int bytesPerPixel;
    /** The palette data that has been read - RGB only */
    private byte[] palette;
    /** The palette data thats be read from alpha channel */
    private byte[] paletteA;
    /** The transparent pixel description */
    private byte[] transPixel;
    
    /** The bit depth of the image */
    private int bitDepth;
    /** The width of the texture to be generated */
    private int texWidth;
    /** The height of the texture to be generated */
    private int texHeight;
    
    /** The scratch buffer used to store the image data */
    private ByteBuffer scratch;
    
    /**
     * Create a new PNG image data that can read image data from PNG formated files
     */
    public PNGImageData() {
        this.crc = new CRC32();
        this.buffer = new byte[4096];
    }
    
    /**
     * Initialise the PNG data header fields from the input stream
     * 
     * @param input The input stream to read from
     * @throws IOException Indicates a failure to read appropriate data from the stream
     */
    private void init(InputStream input) throws IOException {
        this.input = input;
        
        int read = input.read(buffer, 0, SIGNATURE.length);
        if(read != SIGNATURE.length || !checkSignatur(buffer)) {
            throw new IOException("Not a valid PNG file");
        }
        
        openChunk(IHDR);
        readIHDR();
        closeChunk();
        
        searchIDAT: for(;;) {
            openChunk();
            switch (chunkType) {
            case IDAT:
                break searchIDAT;
            case PLTE:
                readPLTE();
                break;
            case tRNS:
                readtRNS();
                break;
            }
            closeChunk();
        }
    }

    /**
     * @see org.newdawn.slick.opengl.ImageData#getHeight()
     */
    public int getHeight() {
        return height;
    }

    /**
     * @see org.newdawn.slick.opengl.ImageData#getWidth()
     */
    public int getWidth() {
        return width;
    }
    
    /**
     * Check if this PNG has a an alpha channel
     * 
     * @return True if the PNG has an alpha channel
     */
    public boolean hasAlpha() {
        return colorType == COLOR_TRUEALPHA ||
                paletteA != null || transPixel != null;
    }
    
    /**
     * Check if the PNG is RGB formatted
     * 
     * @return True if the PNG is RGB formatted
     */
    public boolean isRGB() {
        return colorType == COLOR_TRUEALPHA ||
                colorType == COLOR_TRUECOLOR ||
                colorType == COLOR_INDEXED;
    }
    
    /**
     * Decode a PNG into a data buffer
     * 
     * @param buffer The buffer to read the data into 
     * @param stride The image stride to read (i.e. the number of bytes to skip each line)
     * @param flip True if the PNG should be flipped
     * @throws IOException Indicates a failure to read the PNG either invalid data or 
     * not enough room in the buffer
     */
    private void decode(ByteBuffer buffer, int stride, boolean flip) throws IOException {
        final int offset = buffer.position();
        byte[] curLine = new byte[width*bytesPerPixel+1];
        byte[] prevLine = new byte[width*bytesPerPixel+1];
        
        final Inflater inflater = new Inflater();
        try {
            for(int yIndex=0 ; yIndex<height ; yIndex++) {
            	int y = yIndex;
            	if (flip) {
            		y = height - 1 - yIndex;
            	}
            	
                readChunkUnzip(inflater, curLine, 0, curLine.length);
                unfilter(curLine, prevLine);

                buffer.position(offset + y*stride);

                switch (colorType) {
                case COLOR_TRUECOLOR:
                case COLOR_TRUEALPHA:
                	copy(buffer, curLine); 
                	break;
                case COLOR_INDEXED:
                	copyExpand(buffer, curLine);
                	break;
                default:
                    throw new UnsupportedOperationException("Not yet implemented");
                }

                byte[] tmp = curLine;
                curLine = prevLine;
                prevLine = tmp;
            }
        } finally {
            inflater.end();
        }
        
        bitDepth = hasAlpha() ? 32 : 24;
    }
    
    /**
     * Copy some data into the given byte buffer expanding the
     * data based on indexing the palette
     * 
     * @param buffer The buffer to write into
     * @param curLine The current line of data to copy
     */
    private void copyExpand(ByteBuffer buffer, byte[] curLine) {
    	for (int i=1;i<curLine.length;i++) {
    		int v = curLine[i] & 255;
    
    		int index = v * 3;
    		for (int j=0;j<3;j++) {
    			buffer.put(palette[index+j]);
    		}
    		
    		if (hasAlpha()) {
	    		if (paletteA != null) {
	    			buffer.put(paletteA[v]);
	    		} else {
	    			buffer.put((byte) 255);
	    		}
    		}
    	}
    }
    
    /**
     * Copy the data given directly into the byte buffer (skipping 
     * the filter byte);
     * 
     * @param buffer The buffer to write into 
     * @param curLine The current line to copy into the buffer
     */
    private void copy(ByteBuffer buffer, byte[] curLine) {
        buffer.put(curLine, 1, curLine.length-1);
    }

    /**
     * Unfilter the data, i.e. convert it back to it's original form
     * 
     * @param curLine The line of data just read
     * @param prevLine The line before 
     * @throws IOException Indicates a failure to unfilter the data due to an unknown
     * filter type
     */
    private void unfilter(byte[] curLine, byte[] prevLine) throws IOException {
        switch (curLine[0]) {
            case 0: // none
                break;
            case 1:
                unfilterSub(curLine);
                break;
            case 2:
                unfilterUp(curLine, prevLine);
                break;
            case 3:
                unfilterAverage(curLine, prevLine);
                break;
            case 4:
                unfilterPaeth(curLine, prevLine);
                break;
            default:
                throw new IOException("invalide filter type in scanline: " + curLine[0]);
        }
    }
    
    /**
     * Sub unfilter
     * {@url http://libpng.nigilist.ru/pub/png/spec/1.2/PNG-Filters.html}
     * 
     * @param curLine The line of data to be unfiltered
     */
    private void unfilterSub(byte[] curLine) {
        final int bpp = this.bytesPerPixel;
        final int lineSize = width*bpp;
        
        for(int i=bpp+1 ; i<=lineSize ; ++i) {
            curLine[i] += curLine[i-bpp];
        }
    }

    /**
     * Up unfilter
     * {@url http://libpng.nigilist.ru/pub/png/spec/1.2/PNG-Filters.html}
     * 
     * @param prevLine The line of data read before the current
     * @param curLine The line of data to be unfiltered
     */
    private void unfilterUp(byte[] curLine, byte[] prevLine) {
        final int bpp = this.bytesPerPixel;
        final int lineSize = width*bpp;
        
        for(int i=1 ; i<=lineSize ; ++i) {
            curLine[i] += prevLine[i];
        }
    }

    /**
     * Average unfilter
     * {@url http://libpng.nigilist.ru/pub/png/spec/1.2/PNG-Filters.html}
     * 
     * @param prevLine The line of data read before the current
     * @param curLine The line of data to be unfiltered
     */
    private void unfilterAverage(byte[] curLine, byte[] prevLine) {
        final int bpp = this.bytesPerPixel;
        final int lineSize = width*bpp;
        
        int i;
        for(i=1 ; i<=bpp ; ++i) {
            curLine[i] += (byte)((prevLine[i] & 0xFF) >>> 1);
        }
        for(; i<=lineSize ; ++i) {
            curLine[i] += (byte)(((prevLine[i] & 0xFF) + (curLine[i - bpp] & 0xFF)) >>> 1);
        }
    }

    /**
     * Paeth unfilter
     * {@url http://libpng.nigilist.ru/pub/png/spec/1.2/PNG-Filters.html}
     * 
     * @param prevLine The line of data read before the current
     * @param curLine The line of data to be unfiltered
     */
    private void unfilterPaeth(byte[] curLine, byte[] prevLine) {
        final int bpp = this.bytesPerPixel;
        final int lineSize = width*bpp;
        
        int i;
        for(i=1 ; i<=bpp ; ++i) {
            curLine[i] += prevLine[i];
        }
        for(; i<=lineSize ; ++i) {
            int a = curLine[i - bpp] & 255;
            int b = prevLine[i] & 255;
            int c = prevLine[i - bpp] & 255;
            int p = a + b - c;
            int pa = p - a; if(pa < 0) pa = -pa;
            int pb = p - b; if(pb < 0) pb = -pb;
            int pc = p - c; if(pc < 0) pc = -pc;
            if(pa<=pb && pa<=pc)
                c = a;
            else if(pb<=pc)
                c = b;
            curLine[i] += (byte)c;
        }
    }
      
    /**
     * Read the header of the PNG
     * 
     * @throws IOException Indicates a failure to read the header
     */
    private void readIHDR() throws IOException {
        checkChunkLength(13);
        readChunk(buffer, 0, 13);
        width = readInt(buffer, 0);
        height = readInt(buffer, 4);
        
        if(buffer[8] != 8) {
            throw new IOException("Unsupported bit depth");
        }
        
        colorType = buffer[9] & 255;
        switch (colorType) {
        case COLOR_GREYSCALE:
            bytesPerPixel = 1;
            break;
        case COLOR_TRUECOLOR:
            bytesPerPixel = 3;
            break;
        case COLOR_TRUEALPHA:
            bytesPerPixel = 4;
            break;
        case COLOR_INDEXED:
            bytesPerPixel = 1;
            break;
        default:
            throw new IOException("unsupported color format");  
        }
        
        if(buffer[10] != 0) {
            throw new IOException("unsupported compression method");
        }
        if(buffer[11] != 0) {
            throw new IOException("unsupported filtering method");
        }
        if(buffer[12] != 0) {
            throw new IOException("unsupported interlace method");
        }
    }

    /**
     * Read the palette chunk
     * 
     * @throws IOException Indicates a failure to fully read the chunk
     */
    private void readPLTE() throws IOException {
        int paletteEntries = chunkLength / 3;
        if(paletteEntries < 1 || paletteEntries > 256 || (chunkLength % 3) != 0) {
            throw new IOException("PLTE chunk has wrong length");
        }
        
        palette = new byte[paletteEntries*3];
        readChunk(palette, 0, palette.length);
    }

    /**
     * Read the transparency chunk
     * 
     * @throws IOException Indicates a failure to fully read the chunk
     */
    private void readtRNS() throws IOException {
        switch (colorType) {
        case COLOR_GREYSCALE:
            checkChunkLength(2);
            transPixel = new byte[2];
            readChunk(transPixel, 0, 2);
            break;
        case COLOR_TRUECOLOR:
            checkChunkLength(6);
            transPixel = new byte[6];
            readChunk(transPixel, 0, 6);
            break;
        case COLOR_INDEXED:
            if(palette == null) {
                throw new IOException("tRNS chunk without PLTE chunk");
            }
            paletteA = new byte[palette.length/3];
            // initialise default palette values
            for (int i=0;i<paletteA.length;i++) {
            	paletteA[i] = (byte) 255;
            }
            readChunk(paletteA, 0, paletteA.length);
            break;
        default:
            // just ignore it
        }
    }
    
    /**
     * Close the current chunk, skip the remaining data
     * 
     * @throws IOException Indicates a failure to read off redundant data
     */
    private void closeChunk() throws IOException {
        if(chunkRemaining > 0) {
            // just skip the rest and the CRC
            input.skip(chunkRemaining+4);
        } else {
            readFully(buffer, 0, 4);
            int expectedCrc = readInt(buffer, 0);
            int computedCrc = (int)crc.getValue();
            if(computedCrc != expectedCrc) {
                throw new IOException("Invalid CRC");
            }
        }
        chunkRemaining = 0;
        chunkLength = 0;
        chunkType = 0;
    }
    
    /**
     * Open the next chunk, determine the type and setup the internal state
     * 
     * @throws IOException Indicates a failure to determine chunk information from the stream
     */
    private void openChunk() throws IOException {
        readFully(buffer, 0, 8);
        chunkLength = readInt(buffer, 0);
        chunkType = readInt(buffer, 4);
        chunkRemaining = chunkLength;
        crc.reset();
        crc.update(buffer, 4, 4);   // only chunkType
    }
    
    /**
     * Open a chunk of an expected type
     * 
     * @param expected The expected type of the next chunk
     * @throws IOException Indicate a failure to read data or a different chunk on the stream
     */
    private void openChunk(int expected) throws IOException {
        openChunk();
        if(chunkType != expected) {
            throw new IOException("Expected chunk: " + Integer.toHexString(expected));
        }
    }

    /**
     * Check the current chunk has the correct size
     * 
     * @param expected The expected size of the chunk
     * @throws IOException Indicate an invalid size
     */
    private void checkChunkLength(int expected) throws IOException {
        if(chunkLength != expected) {
            throw new IOException("Chunk has wrong size");
        }
    }
    
    /**
     * Read some data from the current chunk
     * 
     * @param buffer The buffer to read into
     * @param offset The offset into the buffer to read into 
     * @param length The amount of data to read
     * @return The number of bytes read from the chunk
     * @throws IOException Indicate a failure to read the appropriate data from the chunk
     */
    private int readChunk(byte[] buffer, int offset, int length) throws IOException {
        if(length > chunkRemaining) {
            length = chunkRemaining;
        }
        readFully(buffer, offset, length);
        crc.update(buffer, offset, length);
        chunkRemaining -= length;
        return length;
    }

    /**
     * Refill the inflating stream with data from the stream
     * 
     * @param inflater The inflater to fill
     * @throws IOException Indicates there is no more data left or invalid data has been found on 
     * the stream.
     */
    private void refillInflater(Inflater inflater) throws IOException {
        while(chunkRemaining == 0) {
            closeChunk();
            openChunk(IDAT);
        }
        int read = readChunk(buffer, 0, buffer.length);
        inflater.setInput(buffer, 0, read);
    }
    
    /**
     * Read a chunk from the inflater
     * 
     * @param inflater The inflater to read the data from
     * @param buffer The buffer to write into
     * @param offset The offset into the buffer at which to start writing
     * @param length The number of bytes to read
     * @throws IOException Indicates a failure to read the complete chunk
     */
    private void readChunkUnzip(Inflater inflater, byte[] buffer, int offset, int length) throws IOException {
        try {
            do {
                int read = inflater.inflate(buffer, offset, length);
                if(read <= 0) {
                    if(inflater.finished()) {
                        throw new EOFException();
                    }
                    if(inflater.needsInput()) {
                        refillInflater(inflater);
                    } else {
                        throw new IOException("Can't inflate " + length + " bytes");
                    }
                } else {
                    offset += read;
                    length -= read;
                }
            } while(length > 0);
        } catch (DataFormatException ex) {
            IOException io = new IOException("inflate error");
            io.initCause(ex);
            
            throw io;
        }
    }

    /**
     * Read a complete buffer of data from the input stream
     * 
     * @param buffer The buffer to read into
     * @param offset The offset to start copying into 
     * @param length The length of bytes to read
     * @throws IOException Indicates a failure to access the data
     */
    private void readFully(byte[] buffer, int offset, int length) throws IOException {
        do {
            int read = input.read(buffer, offset, length);
            if(read < 0) {
                throw new EOFException();
            }
            offset += read;
            length -= read;
        } while(length > 0);
    }
    
    /**
     * Read an int from a buffer
     * 
     * @param buffer The buffer to read from
     * @param offset The offset into the buffer to read from
     * @return The int read interpreted in big endian
     */
    private int readInt(byte[] buffer, int offset) {
        return
                ((buffer[offset  ]      ) << 24) |
                ((buffer[offset+1] & 255) << 16) |
                ((buffer[offset+2] & 255) <<  8) |
                ((buffer[offset+3] & 255)      );
    }
    
    /**
     * Check the signature of the PNG to confirm it's a PNG
     * 
     * @param buffer The buffer to read from
     * @return True if the PNG signature is correct
     */
    private boolean checkSignatur(byte[] buffer) {
        for(int i=0 ; i<SIGNATURE.length ; i++) {
            if(buffer[i] != SIGNATURE[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * @see org.newdawn.slick.opengl.ImageData#getDepth()
     */
	public int getDepth() {
		return bitDepth;
	}

	/**
	 * @see org.newdawn.slick.opengl.ImageData#getImageBufferData()
	 */
	public ByteBuffer getImageBufferData() {
		return scratch;
	}

	/**
	 * @see org.newdawn.slick.opengl.ImageData#getTexHeight()
	 */
	public int getTexHeight() {
		return texHeight;
	}

	/**
	 * @see org.newdawn.slick.opengl.ImageData#getTexWidth()
	 */
	public int getTexWidth() {
		return texWidth;
	}

	/**
	 * @see org.newdawn.slick.opengl.LoadableImageData#loadImage(java.io.InputStream)
	 */
	public ByteBuffer loadImage(InputStream fis) throws IOException {
		return loadImage(fis, false, null);
	}

	/**
	 * @see org.newdawn.slick.opengl.LoadableImageData#loadImage(java.io.InputStream, boolean, int[])
	 */
	public ByteBuffer loadImage(InputStream fis, boolean flipped, int[] transparent) throws IOException {
		return loadImage(fis, flipped, false, transparent);
	}

	/**
	 * @see org.newdawn.slick.opengl.LoadableImageData#loadImage(java.io.InputStream, boolean, boolean, int[])
	 */
	public ByteBuffer loadImage(InputStream fis, boolean flipped, boolean forceAlpha, int[] transparent) throws IOException {
		if (transparent != null) {
			forceAlpha = true;
		}
		
		init(fis);
		
		if (!isRGB()) {
			throw new IOException("Only RGB formatted images are supported by the PNGLoader");
		}
		
		texWidth = get2Fold(width);
		texHeight = get2Fold(height);
		
		int perPixel = hasAlpha() ? 4 : 3;
		
		// Get a pointer to the image memory
		scratch = BufferUtils.createByteBuffer(texWidth * texHeight * perPixel);
		decode(scratch, texWidth * perPixel, flipped);

		if (height < texHeight-1) {
			int topOffset = (texHeight-1) * (texWidth*perPixel);
			int bottomOffset = (height-1) * (texWidth*perPixel);
			for (int x=0;x<texWidth;x++) {
				for (int i=0;i<perPixel;i++) {
					scratch.put(topOffset+x+i, scratch.get(x+i));
					scratch.put(bottomOffset+(texWidth*perPixel)+x+i, scratch.get(bottomOffset+x+i));
				}
			}
		}
		if (width < texWidth-1) {
			for (int y=0;y<texHeight;y++) {
				for (int i=0;i<perPixel;i++) {
					scratch.put(((y+1)*(texWidth*perPixel))-perPixel+i, scratch.get(y*(texWidth*perPixel)+i));
					scratch.put((y*(texWidth*perPixel))+(width*perPixel)+i, scratch.get((y*(texWidth*perPixel))+((width-1)*perPixel)+i));
				}
			}
		}
		
		if (!hasAlpha() && forceAlpha) {
			ByteBuffer temp = BufferUtils.createByteBuffer(texWidth * texHeight * 4);
			for (int x=0;x<texWidth;x++) {
				for (int y=0;y<texHeight;y++) {
					int srcOffset = (y*3)+(x*texHeight*3);
					int dstOffset = (y*4)+(x*texHeight*4);
					
					temp.put(dstOffset, scratch.get(srcOffset));
					temp.put(dstOffset+1, scratch.get(srcOffset+1));
					temp.put(dstOffset+2, scratch.get(srcOffset+2));
					temp.put(dstOffset+3, (byte) 255);
				}
			}
			
			colorType = COLOR_TRUEALPHA;
			bitDepth = 32;
			scratch = temp;
		}
			
		if (transparent != null) {
	        for (int i=0;i<texWidth*texHeight*4;i+=4) {
	        	boolean match = true;
	        	for (int c=0;c<3;c++) {
	        		if (toInt(scratch.get(i+c)) != transparent[c]) {
	        			match = false;
	        		}
	        	}
	  
	        	if (match) {
	        		scratch.put(i+3, (byte) 0);
	           	}
	        }
	    }
		
		scratch.position(0);
		
		return scratch;
	}
	
	/**
	 * Safe convert byte to int
	 *  
	 * @param b The byte to convert
	 * @return The converted byte
	 */
	private int toInt(byte b) {
		if (b < 0) {
			return 256+b;
		}
		
		return b;
	}
	
    /**
     * Get the closest greater power of 2 to the fold number
     * 
     * @param fold The target number
     * @return The power of 2
     */
    private int get2Fold(int fold) {
        int ret = 2;
        while (ret < fold) {
            ret *= 2;
        }
        return ret;
    }
    
	/**
	 * @see org.newdawn.slick.opengl.LoadableImageData#configureEdging(boolean)
	 */
	public void configureEdging(boolean edging) {
	}
}

