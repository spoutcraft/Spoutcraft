/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011-2012, Spout LLC <http://www.spout.org/>
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
package org.spoutcraft.client.util;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.regex.Pattern;

import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.SRVRecord;
import org.xbill.DNS.TextParseException;
import org.xbill.DNS.Type;

import org.spoutcraft.client.gui.server.ServerItem;

public class NetworkUtils {
	/**
	 * A {@link Pattern} matching valid DNS hostnames (as opposed to IP addresses).
	 */
	private static final Pattern HOSTNAME_PATTERN = Pattern.compile("[a-zA-Z-]");

	public static void pingUrl(String Url) {
		try {
			URL url = new URL(Url);
			HttpURLConnection con = (HttpURLConnection)(url.openConnection());
			System.setProperty("http.agent", "");
			con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/534.30 (KHTML, like Gecko) Chrome/12.0.742.100 Safari/534.30");
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String str;
			while ((str = in.readLine()) != null);
			in.close();
		} catch (Exception e) {
		}
	}

	public static void openInBrowser(String url) {
		try {
			java.net.URI uri = new java.net.URI(url);
			Desktop desktop = Desktop.getDesktop();
			desktop.browse(uri);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Resolves a hostname to an {@link InetSocketAddress}, encapsulating an IP address and
	 * port. <code>hostname</code> may represent a valid DNS hostname or an IP address. If
	 * <code>hostname</code> is a valid DNS hostname <em>and</em> <code>port</code> is equal
	 * to {@link ServerItem#DEFAULT_PORT} (implying the user may not have specified a server
	 * port), a <a href="http://en.wikipedia.org/wiki/SRV_record">SRV record</a> lookup may
	 * be attempted&mdash;see {@link #resolve(String)} for implementation details.
	 * <p>
	 * This method is the equivalent of passing <code>true</code> to {@link #resolve(String, int, boolean)}.
	 *
	 * @param hostname the DNS hostname to resolve.
	 * @param port the port number to encapsulate within the <code>InetSocketAddress</code>.
	 * @return an {@link InetSocketAddress}, which may be marked unresolved if hostname lookup failed.
	 */
	public static InetSocketAddress resolve(String hostname, int port) {
		return resolve(hostname, port, true);
	}

	/**
	 * Resolves a hostname to an {@link InetSocketAddress}, encapsulating an IP address and
	 * port. An optional <a href="http://en.wikipedia.org/wiki/SRV_record">SRV record</a>
	 * lookup may be attempted&mdash;see {@link #resolve(String)} for implementation details.
	 * A SRV record lookup will only be attempted if <code>hostname</code> represents a valid
	 * DNS hostname, and <code>port</code> is equal to {@link ServerItem#DEFAULT_PORT} (implying
	 * the user may not have specified a server port).
	 * <p>
	 * If <code>srv</code> is <code>true</code> and SRV record lookup fails or returns no
	 * results, a regular DNS lookup will be attempted.
	 * <p>
	 * If the given <code>hostname</code> represents an IP address, it will not be resolved.
	 *
	 * @param hostname the DNS hostname to resolve.
	 * @param port the port number to encapsulate within the <code>InetSocketAddress</code>.
	 * @param srv whether to attempt a SRV record lookup.
	 * @return an {@link InetSocketAddress}, which may be marked unresolved if hostname lookup failed.
	 */
	public static InetSocketAddress resolve(String hostname, int port, boolean srv) {
		if (srv && (port == ServerItem.DEFAULT_PORT) && (HOSTNAME_PATTERN.matcher(hostname).find())) {
			try {
				InetSocketAddress address = resolve(hostname);
				if (address != null) {
					return address;
				}
			} catch (TextParseException e) {
				// Do nothing: fall back on a regular DNS lookup before failing
			}
		}

		return new InetSocketAddress(hostname, port);
	}

	/**
	 * Resolves a hostname to an {@link InetSocketAddress}, encapsulating an IP address and
	 * port, using a <a href="http://en.wikipedia.org/wiki/SRV_record">SRV record</a> lookup.
	 * This method assumes <code>minecraft</code> as the service name during the DNS query,
	 * such that a matching record looks like:
	 * <p>
	 * <code>_minecraft._tcp.example.com. 86400 IN SRV 0 1 25565 minecraft.example.com.</code>
	 * <p>
	 * If multiple SRV records exist for a given hostname, the record with the highest priority
	 * (that is, the lowest priority value) is selected. This implementation does not take
	 * into account relative weights for records with identical priorities, and behavior in
	 * such cases is undefined.
	 *
	 * @param hostname the DNS hostname to on which to perform a SRV lookup.
	 * @return an {@link InetSocketAddress}, or <code>null</code> if no SRV record was found.
	 * @throws TextParseException if the hostname is malformed.
	 */
	public static InetSocketAddress resolve(String hostname) throws TextParseException {
		String query = "_minecraft._tcp." + hostname.trim();
		Record[] records = new Lookup(query, Type.SRV).run();

		if ((records != null) && (records.length > 0)) {
			SRVRecord srv = (SRVRecord)records[0];

			if (records.length > 1) {
				for (int i = 1; i < records.length; i++) {
					SRVRecord record = (SRVRecord)records[i];
					if (record.getPriority() < srv.getPriority()) {
						srv = record;
					}
				}
			}

			String host = srv.getTarget().toString().replaceAll("\\.+$", "");
			int port = srv.getPort();
			return new InetSocketAddress(host, port);
		}

		return null;
	}
}
