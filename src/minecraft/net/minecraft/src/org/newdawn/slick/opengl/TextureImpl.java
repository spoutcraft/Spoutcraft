package org.newdawn.slick.opengl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.newdawn.slick.opengl.renderer.SGL;
import org.newdawn.slick.opengl.renderer.Renderer;

/**
 * A texture to be bound within JOGL. This object is responsible for 
 * keeping track of a given OpenGL texture and for calculating the
 * texturing mapping coordinates of the full image.
 * 
 * Since textures need to be powers of 2 the actual texture may be
 * considerably bigged that the source image and hence the texture
 * mapping coordinates need to be adjusted to matchup drawing the
 * sprite against the texture.
 *
 * @author Kevin Glass
 * @author Brian Matzon
 */
public class TextureImpl implements Texture {
	/** The renderer to use for all GL operations */
	protected static SGL GL = Renderer.get();
	
	/** The last texture that was bound to */
	static Texture lastBind;
	
	/**
	 * Retrieve the last texture bound through the texture interface
	 * 
	 * @return The last texture bound
	 */
	public static Texture getLastBind() {
		return lastBind;
	}
	
    /** The GL target type */
    private int target; 
    /** The GL texture ID */
    private int textureID;
    /** The height of the image */
    private int height;
    /** The width of the image */
    private int width;
    /** The width of the texture */
    private int texWidth;
    /** The height of the texture */
    private int texHeight;
    /** The ratio of the width of the image to the texture */
    private float widthRatio;
    /** The ratio of the height of the image to the texture */
    private float heightRatio;
    /** If this texture has alpha */
    private boolean alpha;
    /** The reference this texture was loaded from */
    private String ref;
    /** The name the texture has in the cache */
    private String cacheName;
    
    /**
     * For subclasses to utilise
     */
    protected TextureImpl() {	
    }
    
    /**
     * Create a new texture
     *
     * @param ref The reference this texture was loaded from
     * @param target The GL target 
     * @param textureID The GL texture ID
     */
    public TextureImpl(String ref, int target,int textureID) {
        this.target = target;
        this.ref = ref;
        this.textureID = textureID;
        lastBind = this;
    }
    
    /**
     * Set the name this texture is stored against in the cache
     * 
     * @param cacheName The name the texture is stored against in the cache
     */
    public void setCacheName(String cacheName) {
    	this.cacheName = cacheName;
    }
    
    /**
	 * @see org.newdawn.slick.opengl.Texture#hasAlpha()
	 */
    public boolean hasAlpha() {
    	return alpha;
    }
    
    /**
	 * @see org.newdawn.slick.opengl.Texture#getTextureRef()
	 */
    public String getTextureRef() {
    	return ref;
    }
    
    /** 
     * If this texture has alpha
     * 
     * @param alpha True, If this texture has alpha
     */
    public void setAlpha(boolean alpha) {
    	this.alpha = alpha;
    }
    
    /**
     * Clear the binding of the texture
     */
    public static void bindNone() {
    	lastBind = null;
    	GL.glDisable(SGL.GL_TEXTURE_2D);
    }
    
    /**
     * Clear slick caching of the last bound texture so that an 
     * external texture binder can play with the context before returning 
     * control to slick.
     */
    public static void unbind() {
    	lastBind = null;
    }
    
    /**
	 * @see org.newdawn.slick.opengl.Texture#bind()
	 */
    public void bind() {
    	if (lastBind != this) {
    		lastBind = this;
    		GL.glEnable(SGL.GL_TEXTURE_2D);
    	    GL.glBindTexture(target, textureID);
    	}
    }
    
    /**
     * Set the height of the image
     *
     * @param height The height of the image
     */
    public void setHeight(int height) {
        this.height = height;
        setHeight();
    }
    
    /**
     * Set the width of the image
     *
     * @param width The width of the image
     */
    public void setWidth(int width) {
        this.width = width;
        setWidth();
    }
    
    /**
	 * @see org.newdawn.slick.opengl.Texture#getImageHeight()
	 */
    public int getImageHeight() {
        return height;
    }
    
    /**
	 * @see org.newdawn.slick.opengl.Texture#getImageWidth()
	 */
    public int getImageWidth() {
        return width;
    }
    
    /**
	 * @see org.newdawn.slick.opengl.Texture#getHeight()
	 */
    public float getHeight() {
        return heightRatio;
    }
    
    /**
	 * @see org.newdawn.slick.opengl.Texture#getWidth()
	 */
    public float getWidth() {
        return widthRatio;
    }
    
    /**
	 * @see org.newdawn.slick.opengl.Texture#getTextureHeight()
	 */
    public int getTextureHeight() {
    	return texHeight;
    }

    /**
	 * @see org.newdawn.slick.opengl.Texture#getTextureWidth()
	 */
    public int getTextureWidth() {
    	return texWidth;
    }
    
    /**
     * Set the height of this texture 
     *
     * @param texHeight The height of the texture
     */
    public void setTextureHeight(int texHeight) {
        this.texHeight = texHeight;
        setHeight();
    }
    
    /**
     * Set the width of this texture 
     *
     * @param texWidth The width of the texture
     */
    public void setTextureWidth(int texWidth) {
        this.texWidth = texWidth;
        setWidth();
    }
    
    /**
     * Set the height of the texture. This will update the
     * ratio also.
     */
    private void setHeight() {
        if (texHeight != 0) {
            heightRatio = ((float) height)/texHeight;
        }
    }
    
    /**
     * Set the width of the texture. This will update the
     * ratio also.
     */
    private void setWidth() {
        if (texWidth != 0) {
            widthRatio = ((float) width)/texWidth;
        }
    }
    
    /**
	 * @see org.newdawn.slick.opengl.Texture#release()
	 */
    public void release() {
        IntBuffer texBuf = createIntBuffer(1); 
        texBuf.put(textureID);
        texBuf.flip();
        
    	GL.glDeleteTextures(texBuf);
    	
        if (lastBind == this) {
        	bindNone();
        }
        
        if (cacheName != null) {
        	InternalTextureLoader.get().clear(cacheName);
        } else {
        	InternalTextureLoader.get().clear(ref);
        }
    }
    
    /**
	 * @see org.newdawn.slick.opengl.Texture#getTextureID()
	 */
    public int getTextureID() {
    	return textureID;
    }
    
    /**
     * Set the OpenGL texture ID for this texture
     * 
     * @param textureID The OpenGL texture ID
     */
    public void setTextureID(int textureID) {
    	this.textureID = textureID;
    }
    
    /**
     * Creates an integer buffer to hold specified ints
     * - strictly a utility method
     *
     * @param size how many int to contain
     * @return created IntBuffer
     */
    protected IntBuffer createIntBuffer(int size) {
      ByteBuffer temp = ByteBuffer.allocateDirect(4 * size);
      temp.order(ByteOrder.nativeOrder());

      return temp.asIntBuffer();
    }    
    
    /**
	 * @see org.newdawn.slick.opengl.Texture#getTextureData()
	 */
    public byte[] getTextureData() {
    	ByteBuffer buffer = BufferUtils.createByteBuffer((hasAlpha() ? 4 : 3) * texWidth * texHeight);
    	bind();
    	GL.glGetTexImage(SGL.GL_TEXTURE_2D, 0, hasAlpha() ? SGL.GL_RGBA : SGL.GL_RGB, SGL.GL_UNSIGNED_BYTE, 
    					   buffer);
    	byte[] data = new byte[buffer.limit()];
    	buffer.get(data);
    	buffer.clear();
    	
    	return data;
    }
}
