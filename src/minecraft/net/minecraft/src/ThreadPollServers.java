package net.minecraft.src;
/* Spout disabled until later
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import net.minecraft.src.GuiMultiplayer;
import net.minecraft.src.GuiSlotServer;
import net.minecraft.src.ServerNBTStorage;

class ThreadPollServers extends Thread {

	// $FF: synthetic field
	final ServerNBTStorage field_35601_a;
	// $FF: synthetic field
	final GuiSlotServer field_35600_b;


	ThreadPollServers(GuiSlotServer var1, ServerNBTStorage var2) {
		this.field_35600_b = var1;
		this.field_35601_a = var2;
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
								this.field_35601_a.field_35791_d = "\u00a78Polling..";
								long var1 = System.nanoTime();
								GuiMultiplayer.func_35336_a(this.field_35600_b.field_35410_a, this.field_35601_a);
								long var3 = System.nanoTime();
								this.field_35601_a.field_35792_e = (var3 - var1) / 1000000L;
								var27 = false;
								break label183;
							} catch (UnknownHostException var35) {
								this.field_35601_a.field_35792_e = -1L;
								this.field_35601_a.field_35791_d = "\u00a74Can\'t resolve hostname";
								var27 = false;
							} catch (SocketTimeoutException var36) {
								this.field_35601_a.field_35792_e = -1L;
								this.field_35601_a.field_35791_d = "\u00a74Can\'t reach server";
								var27 = false;
								break label187;
							} catch (ConnectException var37) {
								this.field_35601_a.field_35792_e = -1L;
								this.field_35601_a.field_35791_d = "\u00a74Can\'t reach server";
								var27 = false;
								break label186;
							} catch (IOException var38) {
								this.field_35601_a.field_35792_e = -1L;
								this.field_35601_a.field_35791_d = "\u00a74Communication error";
								var27 = false;
								break label185;
							} catch (Exception var39) {
								this.field_35601_a.field_35792_e = -1L;
								this.field_35601_a.field_35791_d = "ERROR: " + var39.getClass();
								var27 = false;
								break label184;
							} finally {
								if(var27) {
									synchronized(GuiMultiplayer.func_35321_g()) {
										GuiMultiplayer.func_35335_o();
									}
								}
							}

							synchronized(GuiMultiplayer.func_35321_g()) {
								GuiMultiplayer.func_35335_o();
								return;
							}
						}

						synchronized(GuiMultiplayer.func_35321_g()) {
							GuiMultiplayer.func_35335_o();
							return;
						}
					}

					synchronized(GuiMultiplayer.func_35321_g()) {
						GuiMultiplayer.func_35335_o();
						return;
					}
				}

				synchronized(GuiMultiplayer.func_35321_g()) {
					GuiMultiplayer.func_35335_o();
					return;
				}
			}

			synchronized(GuiMultiplayer.func_35321_g()) {
				GuiMultiplayer.func_35335_o();
				return;
			}
		}

		synchronized(GuiMultiplayer.func_35321_g()) {
			GuiMultiplayer.func_35335_o();
		}

	}

}
*/