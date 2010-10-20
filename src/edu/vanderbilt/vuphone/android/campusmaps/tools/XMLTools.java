/**
 * Android Campus Maps
 *  http://code.google.com/p/vuphone/
 * 
 * @author Adam Albright
 * @date Dec 24, 2009
 * 
 * Copyright 2009 VUPhone Team
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License. 
 *  You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *  Unless required by applicable law or agreed to in writing, software 
 *  distributed under the License is distributed on an "AS IS" BASIS, 
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 *  implied. See the License for the specific language governing 
 *  permissions and limitations under the License. 
 */

package edu.vanderbilt.vuphone.android.campusmaps.tools;

import java.io.InputStream;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.vanderbilt.vuphone.android.campusmaps.Main;

public class XMLTools {

	public static Document parseXML(InputStream in) {
		try {
			// TODO Allow local Android file to be loaded vs getting from URL
			Main.trace("Loading building list");

			DocumentBuilder db = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			Main.trace("Parsing XML");
			Document d = db.parse(in);
			in.close();
			return d;
		} catch (Exception e) {
			Main.trace("Could not parse XML!" + e.getMessage());
			return null;
		}
	}

	/**
	 * Converts a list of nodes into a (NodeName -> NodeValue) hashmap
	 * 
	 * @param list
	 *            - the NodeList to be converted
	 * @return
	 */
	public static Properties NodeList2Array(NodeList list) {
		if (list == null)
			return null;

		Properties p = new Properties();

		try {
			for (int i = 0; i < list.getLength(); i++) {
				Node n = list.item(i);
				if (n == null || n.getNodeType() != 1)
					continue;

				Node x = n.getFirstChild();
				if (x == null)
					continue;

				p.setProperty(n.getNodeName(), x.getNodeValue());

			}
		} catch (Exception e) {
			Main.trace("Couldn't parse XML! " + e.getMessage());
			return null;
		}

		return p;
	}
}
