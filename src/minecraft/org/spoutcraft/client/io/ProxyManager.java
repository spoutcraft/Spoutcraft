/*
 * This file is part of Spoutcraft.
 *
 * Copyright (c) 2011-2012, SpoutDev <http://www.spout.org/>
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
package org.spoutcraft.client.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Properties;


public class ProxyManager {
  private static Properties prop = new Properties();
  private static File proxyConfigFile = new File(FileUtil.getConfigDir(), "proxySettings.properties");
  
  public static boolean isEnabled(){
    try {
      prop.load(new FileInputStream(proxyConfigFile));
    }
    catch(Exception e){
      return false;
    }
    
    return prop.getProperty("enabled").equalsIgnoreCase("true");
  }
  
  public static Socket proxyConnect(String host, int port) throws IOException{
    Socket skt = new Socket(prop.getProperty("host"), Integer.parseInt(prop.getProperty("port")));
    PrintStream out = new PrintStream(skt.getOutputStream());
    BufferedReader in = new BufferedReader(new InputStreamReader(skt.getInputStream()));
    out.println("CONNECT " + host + ":" + port + " HTTP/1.1");
    
    if(!prop.getProperty("user").isEmpty() && ! prop.getProperty("pass").isEmpty()){
      //basic http proxy authentication
      out.println("Proxy-Authorization: basic " + Base64.encode(prop.getProperty("user") + ":" + prop.getProperty("pass")));
    }
    //try block untill connection is ready. proxy should return http 200 code
    System.out.println("Proxy connect to: " + host + ":" + port + in.readLine());
    return skt;
  }
  
}


