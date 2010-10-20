/**
 * Android Campus Maps
 *  http://code.google.com/p/vuphone/
 * 
 * @author Kyle Prete
 * @date Dec 21, 2009
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

import java.util.ArrayList;

import android.database.Cursor;
import edu.vanderbilt.vuphone.android.campusmaps.Main;

/**
 * This class abstracts database access and caches multiple like accesses.
 * Accessed through the Building class
 */
public class DBWrapper {

	private static final int CLOSED = 0;
	private static final int READABLE = 1;
	private static final int WRITABLE = 2;
	private static DBAdapter adapter;
	private static int state;
	private static ArrayList<Long> IDs;
	private static ArrayList<Building> cache;
	private static boolean idsCached = false;
	private static boolean mainDataCached = false;
	private static ArrayList<Boolean> cached;

	@SuppressWarnings("unchecked")
	public static ArrayList<Long> getIDs() {
		cacheIDs();
		return (ArrayList<Long>) IDs.clone();
	}

	public static Building get(long rowID) {
		int i = cacheBuilding(rowID);
		return cache.get(i);
	}

	public static String getName(long rowID) {
		cacheMainData();
		return cache.get(IDs.indexOf(rowID)).getName();
	}

	public static double getLat(long rowID) {
		cacheMainData();
		return (cache.get(IDs.indexOf(rowID)).getLat_() / 1E6);
	}

	public static double getLon(long rowID) {
		cacheMainData();
		return (cache.get(IDs.indexOf(rowID)).getLong_() / 1E6);
	}

	public static String getURL(long rowID) {
		cacheMainData();
		return cache.get(IDs.indexOf(rowID)).getImageURL();
	}

	public static String getDesc(long rowID) {
		cacheMainData();
		return cache.get(IDs.indexOf(rowID)).getDescription();
	}

	public static boolean create(Building b) {
		makeWritable();
		long rID = adapter.createBuilding(b.getName(), b.getLat_(), b
				.getLong_(), b.getDescription(), b.getImageURL());
		if (mainDataCached && rID != -1) {
			IDs.add(rID);
			cache.add(b);
		}
		return (rID != -1);
	}

	// updates the database and cache immediately with new values
	public static boolean update(long rowID, Building updated) {
		cacheIDs();
		makeWritable();
		int i = IDs.indexOf(rowID);
		if (i < 0)
			return false;
		boolean success = adapter.updateBuilding(rowID, updated.getName(),
				updated.getLat_(), updated.getLong_(),
				updated.getDescription(), updated.getImageURL());
		if (mainDataCached && success)
			cache.set(i, updated);
		close();
		return success;
	}

	public static boolean delete(long rowID) {
		makeWritable();
		if (adapter.deleteBuilding(rowID)) {
			int i = getIDs().indexOf(rowID);
			if (i >= 0) {
				if (mainDataCached)
					cache.remove(i);
				IDs.remove(i);
			}
			return true;
		} else
			return false;
	}

	public static void cacheIDs() {
		if (idsCached)
			return;
		IDs = new ArrayList<Long>();
		makeReadable();
		Cursor c = adapter.getCursor(new String[] { DBAdapter.COLUMN_ID });
		if (c.moveToFirst()) {
			do {
				IDs.add(c.getLong(c.getColumnIndex(DBAdapter.COLUMN_ID)));
			} while (c.moveToNext());
		}
		idsCached = true;
		c.close();
	}

	public static void cacheMainData() {
		if (mainDataCached)
			return;
		resetBuildingCache(); // ensures IDs are cached
		makeReadable();
		Cursor c = adapter.getCursor(new String[] { DBAdapter.COLUMN_NAME,
				DBAdapter.COLUMN_LATITUDE, DBAdapter.COLUMN_LONGITUDE });
		if (c.moveToFirst()) {
			do {
				Building current = new Building(c.getLong(c
						.getColumnIndex(DBAdapter.COLUMN_ID)), c.getInt(c
						.getColumnIndex(DBAdapter.COLUMN_LATITUDE)), c.getInt(c
						.getColumnIndex(DBAdapter.COLUMN_LONGITUDE)), c
						.getString(c.getColumnIndex(DBAdapter.COLUMN_NAME)), c
						.getString(c
								.getColumnIndex(DBAdapter.COLUMN_DESCRIPTION)),
						c.getString(c.getColumnIndex(DBAdapter.COLUMN_URL)));
				cache.add(current);
				cached.add(false);
			} while (c.moveToNext());
		}
		mainDataCached = true;
		c.close();
		close();

	}

	public static int cacheBuilding(long rowID) {
		if (!mainDataCached)
			cacheMainData();
		int i = IDs.indexOf(rowID);
		if (cached.get(i))
			return i;
		makeReadable();
		String[] columnsToRead;
		columnsToRead = new String[] { DBAdapter.COLUMN_DESCRIPTION,
				DBAdapter.COLUMN_URL };
		Cursor c = adapter.getCursor(columnsToRead, rowID);
		if (!c.moveToFirst())
			throw new RuntimeException(
					"Cannot cache building which doesnt exist");
		if (i < 0)
			throw new RuntimeException(
					"error, discrepancy between database and memory cache");

		Building b = cache.get(i);

		b.setDescription(c.getString(c
				.getColumnIndex(DBAdapter.COLUMN_DESCRIPTION)));
		b.setImageURL(c.getString(c.getColumnIndex(DBAdapter.COLUMN_URL)));
		c.close();
		close();
		cached.set(i, true);
		return i;
	}

	private static void resetBuildingCache() {
		int restaurants = getIDs().size();
		cache = new ArrayList<Building>();
		cache.ensureCapacity(restaurants);
		cached = new ArrayList<Boolean>();
		cached.ensureCapacity(restaurants);
		mainDataCached = false;
	}

	private static void initialize() {
		if (adapter == null) {
			adapter = new DBAdapter(Main.applicationContext);
			state = CLOSED;
		}
	}

	private static void makeWritable() {
		initialize();
		switch (state) {
		case READABLE:
			adapter.close();
			adapter.openWritable();
			state = WRITABLE;
			return;
		case WRITABLE:
			return;
		case CLOSED:
			adapter.openWritable();
			state = WRITABLE;
			return;
		}
	}

	private static void makeReadable() {
		initialize();
		switch (state) {
		case READABLE:
			return;
		case WRITABLE:
			adapter.close();
			adapter.openReadable();
			state = READABLE;
			return;
		case CLOSED:
			adapter.openReadable();
			state = READABLE;
			return;
		}
	}

	// closes the underlying database adapter
	// no read/writes are to be performed in the
	// near future
	public static void close() {
		initialize();
		switch (state) {
		case READABLE:
			adapter.close();
			state = CLOSED;
			return;
		case WRITABLE:
			adapter.close();
			state = CLOSED;
			return;
		case CLOSED:
			return;
		}
	}

	public static Cursor fetchAllBuildingsCursor() {
		return adapter.fetchAllBuildingsSortedCursor();
	}

}
