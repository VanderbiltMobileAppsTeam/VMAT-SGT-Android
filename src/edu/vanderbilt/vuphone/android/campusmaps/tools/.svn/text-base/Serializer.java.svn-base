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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * Class is used to serialize objects
 *
 */
public class Serializer {
	
	private XStream xstream_;

	public Serializer() {
		xstream_ = new XStream(new DomDriver());
	}

	/**
	 * Loads an object from a file
	 * @param path
	 * @return
	 */
	public Object loadObject(String path) {
		File f = new File(path);

		if (!f.exists())
			return null;

		try {
			// Read file into a variable
			BufferedReader reader = new BufferedReader(new FileReader(path));
			StringBuilder xml = new StringBuilder();
			String line;

			while ((line = reader.readLine()) != null)
				xml.append(line + "\n");

			return xstream_.fromXML(xml.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Saves an object to disk
	 * @param obj - Object to be saved to disk
	 * @param path
	 * @throws IOException
	 */
	public void saveObject(Object obj, String path) throws IOException {
		try {
			// delete the existing file, if there is one
			File existingFile = new File(path);
			if (existingFile.exists())
				existingFile.delete();

			// create the new file
			BufferedWriter out = new BufferedWriter(new FileWriter(path, true));
			saveObject(obj, out);

		} catch (Exception e) {
			throw new IOException();
		}
	}

	/**
	 * Saves an object to an output stream
	 * @param obj
	 * @param out
	 * @throws IOException
	 */
	public void saveObject(Object obj, Writer out) throws IOException {
		try {
			out.write(xstream_.toXML(obj));
			out.close();

		} catch (Exception e) {
			System.out.println("Error saving object: " + e);
			throw new IOException();
		}
	}

	/**
	 * Instantiate an object from its serialized form
	 * @param s
	 * @return
	 */
	public Object fromXML(String s) {
		try {
			return xstream_.fromXML(s);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Instantiate an object from its serialized form
	 * @param s
	 * @return
	 */
	public Object fromXML(Reader r) {
		try {
			return xstream_.fromXML(r);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Serializes the object to a string
	 * @param obj
	 * @return
	 * @throws IOException
	 */
	public String toXML(Object obj) throws IOException {
		try {
			return xstream_.toXML(obj);
		} catch (Exception e) {
			throw new IOException();
		}
	}
}
