/* Spout removed file
package net.minecraft.src;


import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.spoutcraft.client.gui.server.ServerSlot;

import net.minecraft.src.GuiMultiplayer;
import net.minecraft.src.GuiSlotServer;
import net.minecraft.src.ServerNBTStorage;

public class ThreadPollServers extends Thread {
//
//	// $FF: synthetic field
//	final ServerSlot field_35601_a;
//	// $FF: synthetic field
//	final GuiSlot field_35600_b;
//
//
	public ThreadPollServers(GuiSlot var1, ServerSlot var2) {
//		this.field_35600_b = var1;
//		this.field_35601_a = var2;
	}
//
//	public void run() {
//		boolean var27 = false;
//
//		label183: {
//			label184: {
//				label185: {
//					label186: {
//						label187: {
//							try {
//								var27 = true;
//								this.field_35601_a.msg = "\u00a78Polling..";
//								long var1 = System.nanoTime();
//								if (this.field_35600_b instanceof GuiSlotServer) {
//									GuiMultiplayer.updateServerNBT(((GuiSlotServer) this.field_35600_b).field_35410_a, this.field_35601_a);
//								} else if (this.field_35600_b instanceof GuiFavoritesSlot) {
//									GuiFavorites.updateServerNBT(((GuiFavoritesSlot) this.field_35600_b).parentServerGui, this.field_35601_a);
//								}
//								long var3 = System.nanoTime();
//								this.field_35601_a.ping = (var3 - var1) / 1000000L;
//								var27 = false;
//								break label183;
//							} catch (UnknownHostException var35) {
//								this.field_35601_a.ping = -1L;
//								this.field_35601_a.msg = "\u00a74Can\'t resolve hostname";
//								var27 = false;
//							} catch (SocketTimeoutException var36) {
//								this.field_35601_a.ping = -1L;
//								this.field_35601_a.msg = "\u00a74Can\'t reach server";
//								var27 = false;
//								break label187;
//							} catch (ConnectException var37) {
//								this.field_35601_a.ping = -1L;
//								this.field_35601_a.msg = "\u00a74Can\'t reach server";
//								var27 = false;
//								break label186;
//							} catch (IOException var38) {
//								this.field_35601_a.ping = -1L;
//								this.field_35601_a.msg = "\u00a74Communication error";
//								var27 = false;
//								break label185;
//							} catch (Exception var39) {
//								this.field_35601_a.ping = -1L;
//								this.field_35601_a.msg = "ERROR: " + var39.getClass();
//								var27 = false;
//								break label184;
//							} finally {
//								if(var27) {
//									synchronized(GuiMultiplayer.getSyncObject()) {
//										GuiMultiplayer.decrementPingLimit();
//									}
//								}
//							}
//
//							synchronized(GuiMultiplayer.getSyncObject()) {
//								GuiMultiplayer.decrementPingLimit();
//								return;
//							}
//						}
//
//						synchronized(GuiMultiplayer.getSyncObject()) {
//							GuiMultiplayer.decrementPingLimit();
//							return;
//						}
//					}
//
//					synchronized(GuiMultiplayer.getSyncObject()) {
//						GuiMultiplayer.decrementPingLimit();
//						return;
//					}
//				}
//
//				synchronized(GuiMultiplayer.getSyncObject()) {
//					GuiMultiplayer.decrementPingLimit();
//					return;
//				}
//			}
//
//			synchronized(GuiMultiplayer.getSyncObject()) {
//				GuiMultiplayer.decrementPingLimit();
//				return;
//			}
//		}
//
//		synchronized(GuiMultiplayer.getSyncObject()) {
//			GuiMultiplayer.decrementPingLimit();
//		}
//
//	}

}
*/