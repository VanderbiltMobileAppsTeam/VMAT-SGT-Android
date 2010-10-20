/**
 * Android Campus Maps
 *  http://code.google.com/p/vuphone/
 * 
 * @author Adam Albright
 * @date Oct 16, 2009
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

package edu.vanderbilt.vuphone.android.campusmaps.storage;

import edu.vanderbilt.vuphone.android.campusmaps.R;

public class Building implements Comparable<Building> {
	// Stored in microdegrees.
	private int lat_ = 0;
	private int long_ = 0;
	private String name_ = null;
	private String desc_ = null;
	private String url_ = null;
	private long id_ = 0;

	@SuppressWarnings("unused")
	private Building() {
		// This ctor is needed for XStream
	}

	public Building(long id, int lat, int lon, String name, String desc,
			String url) {
		id_ = id;
		setLat_(lat);
		setLong_(lon);
		name_ = name;
		desc_ = desc;
		setImageURL(url);
	}

	// Don't know if we need these setters so I made them private.
	private void setLat_(int lat_) {
		this.lat_ = lat_;
	}

	private void setLong_(int long_) {
		this.long_ = long_;
	}

	public int getLat_() {
		return lat_;
	}

	public int getLong_() {
		return long_;
	}

	public String getName() {
		return name_;
	}

	public String getDescription() {
		return desc_;
	}

	public String getImageURL() {
		return url_;
	}

	public long getID() {
		return id_;
	}

	public void setDescription(String desc) {
		desc_ = desc;
	}

	public void setImageURL(String url) {
		url_ = url;
	}

	public String toString() {
		return getName();
	}

	// TODO(corespace): Are building names unique?
	public int compareTo(Building another) {
		return getName().compareTo(another.getName());
	}

}
