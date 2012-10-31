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
package org.spoutcraft.client.gui.error;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

class PasteBinAPI {
	private final static String pasteURL = "http://www.pastebin.com/api/api_post.php";
	private String token;
	private String devkey;

	public PasteBinAPI(String devkey) {
		this.devkey = devkey;
	}

	public String checkResponse(String response) {
		if (response.substring(0, 15) == "Bad API request")
				return response.substring(17);
		return "";
	}

	public String makePaste(String message, String name, String format) throws UnsupportedEncodingException {
		String content = URLEncoder.encode(message, "UTF-8");
		String title = URLEncoder.encode(name, "UTF-8");
		String data = "api_option=paste&api_user_key=" + this.token
						+ "&api_paste_private=0&api_paste_name=" + title
						+ "&api_paste_expire_date=N&api_paste_format=" + format
						+ "&api_dev_key=" + this.devkey + "&api_paste_code=" + content;
		String response = this.page(pasteURL, data);
		String check = this.checkResponse(response);
		if (!check.equals(""))
				return check;
		return response;
	}

	public String page(String uri, String urlParameters) {
		URL url;
		HttpURLConnection connection = null;
		try {
				// Create connection
				url = new URL(uri);
				connection = (HttpURLConnection) url.openConnection();
				connection.setRequestMethod("POST");
				connection.setRequestProperty("Content-Type",
								"application/x-www-form-urlencoded");

				connection.setRequestProperty("Content-Length",
								"" + Integer.toString(urlParameters.getBytes().length));
				connection.setRequestProperty("Content-Language", "en-US");

				connection.setUseCaches(false);
				connection.setDoInput(true);
				connection.setDoOutput(true);

				// Send request
				DataOutputStream wr = new DataOutputStream(
								connection.getOutputStream());
				wr.writeBytes(urlParameters);
				wr.flush();
				wr.close();

				// Get Response
				InputStream is = connection.getInputStream();
				BufferedReader rd = new BufferedReader(new InputStreamReader(is));
				String line;
				StringBuffer response = new StringBuffer();
				while ((line = rd.readLine()) != null) {
						response.append(line);
				}
				rd.close();
				return response.toString();

		} catch (Exception e) {

				e.printStackTrace();
				return null;

		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	public void setToken(String token) {
		this.token = token;
	}
}