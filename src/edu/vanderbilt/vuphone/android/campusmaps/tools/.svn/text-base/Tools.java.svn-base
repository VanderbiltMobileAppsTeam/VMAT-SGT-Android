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

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

import com.google.android.maps.GeoPoint;

public class Tools {

	/**
	 * Format a string to titleCase (FOO BAR -> Foo Bar)
	 * 
	 * @param s
	 * @return
	 */
	public static String titleCase(String s) {
		Pattern p = Pattern.compile("(^|\\W)([a-z])");
		Matcher m = p.matcher(s.toLowerCase());

		StringBuffer sb = new StringBuffer(s.length());

		while (m.find())
			m.appendReplacement(sb, m.group(1) + m.group(2).toUpperCase());

		m.appendTail(sb);
		return sb.toString();
	}

	/**
	 * Method for converting from EPSG900913 format used by vu.gml to latitude
	 * and longitude. Based on reversing a C# function from
	 * http://www.cadmaps.com/gisblog/?cat=10
	 * 
	 * @param x
	 *            - 1st EPSG900913 coordinate
	 * @param y
	 *            - 2nd EPSG900913 coordinate
	 * @return GeoPoint at input location
	 */
	public static GeoPoint EPSG900913ToGeoPoint(double x, double y) {
		double longitude = x / (6378137.0 * Math.PI / 180);
		double latitude = ((Math.atan(Math.pow(Math.E, (y / 6378137.0))))
				/ (Math.PI / 180) - 45) * 2.0;

		return new GeoPoint((int) (latitude * 1E6), (int) (longitude * 1E6));
	}

	/**
	 * Download a webpage to a string
	 * 
	 * @param url
	 * @return
	 */
	public static String downloadWebpage(String url) {

		StringBuilder content = new StringBuilder();
		URLConnection connection = null;
		try {
			connection = new URL(url).openConnection();
			Scanner scanner = new Scanner(connection.getInputStream());
			scanner.useDelimiter("\\Z");
			while (scanner.hasNext()) {
				content.append(scanner.next());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return content.toString();
	}

	public static String getDataFromUrl(String url) throws IOException {

		StringBuffer b = new StringBuffer();
		InputStream is = null;
		URLConnection c = null;

		long len = 0;
		int ch = 0;
		c = new URL(url).openConnection();
		is = c.getInputStream();
		len = c.getContentLength();

		if (len != -1) {
			// Read exactly Content-Length bytes
			for (int i = 0; i < len; i++)
				if ((ch = is.read()) != -1) {
					b.append((char) ch);
				}
		} else {
			// Read until the connection is closed.
			while ((ch = is.read()) != -1) {
				len = is.available();
				b.append((char) ch);
			}
		}

		is.close();
		return b.toString();
	}

}
