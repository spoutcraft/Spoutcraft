package net.minecraft.src;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

class ThreadPollServers extends Thread {
	final ServerData field_78318_a;

	/** Slot container for the server list */
	final GuiSlotServer serverSlotContainer;

	ThreadPollServers(GuiSlotServer par1GuiSlotServer, ServerData par2ServerData) {
		this.serverSlotContainer = par1GuiSlotServer;
		this.field_78318_a = par2ServerData;
	}

	public void run() {
		boolean var27 = false;
		label183: {
			label184: {
				label185: {
					label186: {
						label187: {
							try {
								var27 = true;
								this.field_78318_a.serverMOTD = "\u00a78Polling..";
								long var1 = System.nanoTime();
								GuiMultiplayer.pollServer(this.serverSlotContainer.parentGui, this.field_78318_a); // Spout
								long var3 = System.nanoTime();
								this.field_78318_a.field_78844_e = (var3 - var1) / 1000000L;
								var27 = false;
								break label183;
							} catch (UnknownHostException var35) {
								this.field_78318_a.field_78844_e = -1L;
								this.field_78318_a.serverMOTD = "\u00a74Can\'t resolve hostname";
								var27 = false;
							} catch (SocketTimeoutException var36) {
								this.field_78318_a.field_78844_e = -1L;
								this.field_78318_a.serverMOTD = "\u00a74Can\'t reach server";
								var27 = false;
								break label187;
							} catch (ConnectException var37) {
								this.field_78318_a.field_78844_e = -1L;
								this.field_78318_a.serverMOTD = "\u00a74Can\'t reach server";
								var27 = false;
								break label186;
							} catch (IOException var38) {
								this.field_78318_a.field_78844_e = -1L;
								this.field_78318_a.serverMOTD = "\u00a74Communication error";
								var27 = false;
								break label185;
							} catch (Exception var39) {
								this.field_78318_a.field_78844_e = -1L;
								this.field_78318_a.serverMOTD = "ERROR: " + var39.getClass();
								var27 = false;
								break label184;
							} finally {
								if (var27) {
									// Spout Start
									synchronized (GuiMultiplayer.getLock()) {
										GuiMultiplayer.decrementThreadsPending();
									}
								}
							}

							synchronized (GuiMultiplayer.getLock()) {
								GuiMultiplayer.decrementThreadsPending();
								return;
							}
						}

						synchronized (GuiMultiplayer.getLock()) {
							GuiMultiplayer.decrementThreadsPending();
							return;
						}
					}

					synchronized (GuiMultiplayer.getLock()) {
						GuiMultiplayer.decrementThreadsPending();
						return;
					}
				}

				synchronized (GuiMultiplayer.getLock()) {
					GuiMultiplayer.decrementThreadsPending();
					return;
				}
			}

			synchronized (GuiMultiplayer.getLock()) {
				GuiMultiplayer.decrementThreadsPending();
				return;
			}
		}

		synchronized (GuiMultiplayer.getLock()) {
			GuiMultiplayer.decrementThreadsPending();
		}
		// Spout End
	}
}
