package org.getspout.spout.gui;

import net.minecraft.src.Vec3D;

public class Color {
    private short red;
    private short green;
    private short blue;
    private short alpha = 1;
    
    public Color(float r, float g, float b){
        setRed(r);
        setGreen(g);
        setBlue(b);
    }
    
    public Color(float r, float g, float b, float a){
        setRed(r);
        setGreen(g);
        setBlue(b);
        setAlpha(a);
    }
    
    public Color(short r, short g, short b) {
        red = r;
        green = g;
        blue = b;
    }
    
    public Color(short r, short g, short b, short a) {
        red = r;
        green = g;
        blue = b;
        alpha = a;
    }
    
    public float getRedF(){
        return red/255F;
    }
    
    public float getGreenF(){
        return green/255F;
    }
    
    public float getBlueF(){
        return blue/255F;
    }
    
    public float getAlphaF(){
        return alpha/255F;
    }
    
    public short getRedB(){
        return red;
    }
    
    public short getGreenB(){
        return green;
    }
    
    public short getBlueB(){
        return blue;
    }
    
    public short getAlphaB(){
        return alpha;
    }
    
    public Color setRed(float r){
        red = (short)(r*255);
        return this;
    }
    
    public Color setGreen(float g){
        green = (short)(g*255);
        return this;
    }
    
    public Color setBlue(float b){
        blue = (short)(b*255);
        return this;
    }
    
    public Color setAlpha(float a){
        alpha = (short)(a*255);
        return this;
    }
    
    public Color clone() {
        return new Color(red, green, blue, alpha);
    }
    
    public static Color fromVector(Vec3D vec) {
        return new Color((float)vec.xCoord, (float)vec.yCoord, (float)vec.zCoord);
    }
    
    public boolean isInvalid() {
        return red == -1 || red/255 == -1;
    }
    
    public boolean isOverride() {
        return red == -2 || red/255 == -2;
    }
    
    public static Color invalid() {
        return new Color(-1,-1,-1);
    }
    
    public static Color override() {
        return new Color(-2,-2,-2);
    }
}