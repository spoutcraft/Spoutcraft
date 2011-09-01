/*
 * This file is part of Spoutcraft API (http://wiki.getspout.org/).
 * 
 * Spoutcraft API is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Spoutcraft API is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.spoutcraft.spoutcraftapi.gui;

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
	
	public int toInt() {
		return Long.valueOf(((long)getAlphaB() << 24L) + ((long)getRedB() << 16L) + ((long)getGreenB() << 8L) + (long)getBlueB()).intValue();
	}
}