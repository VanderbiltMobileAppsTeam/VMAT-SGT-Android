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
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

public class DBAdapter {

	/** Used for logging */
	private static final String pre = "DBAdapter";

	/** Used for database updates */
	private static final int DB_VERSION = 1;

	/** The filename where the database is stored */
	private static final String DB_NAME = "campusmaps";

	/** The main table name */
	protected static final String BUILDING_TABLE = "buildings";

	/** The index column */
	public static final String COLUMN_ID = "_id";

	/** The other column names */
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_LATITUDE = "latitude";
	public static final String COLUMN_LONGITUDE = "longitude";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_URL = "url";

	/** Handle to the database instance */
	private SQLiteDatabase db_;

	/** Used to help open and update the database */
	private DatabaseHelper DBHelper;

	private Context context;

	/** Used to create database */
	private static final String BUILDING_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ BUILDING_TABLE + " (                                       "
			+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,       "
			+ COLUMN_NAME + " TEXT NOT NULL,                         "
			+ COLUMN_LATITUDE + " REAL NOT NULL,                "
			+ COLUMN_LONGITUDE + " REAL NOT NULL,                  "
			+ COLUMN_DESCRIPTION + " TEXT,              " + COLUMN_URL
			+ " TEXT)";

	public DBAdapter(Context ctx) {
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
		open();
		DBHelper.onCreate(db_);
	}

	public class DatabaseHelper extends SQLiteOpenHelper {

		/** Used for logging */
		private static final String pre = "DBOpenHelper: ";

		/**
		 * @see android.database.sqlite.SQLiteOpenHelper#SQLiteOpenHelper(Context,
		 *      String, CursorFactory, int)
		 */
		public DatabaseHelper(Context context) {
			super(context, DB_NAME, null, DB_VERSION);
		}

		/**
		 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
		 */
		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.i("i", pre + "Creating a new DB");
			db.execSQL(BUILDING_CREATE);
		}

		/**
		 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase,
		 *      int, int)
		 */
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// Log the version upgrade.
			Log
					.w("pre", "Upgrading database from version " + oldVersion
							+ " to " + newVersion
							+ ", which will destroy all old data");

			db.execSQL("DROP TABLE IF EXISTS " + BUILDING_TABLE);
			onCreate(db);
		}
	}

	/**
	 * Opens the database
	 * 
	 * @return this DBAdater
	 * @throws SQLException
	 */
	public DBAdapter open() throws SQLException {
		db_ = DBHelper.getWritableDatabase();
		return this;
	}

	/** Used to close the database when done */
	public void close() {
		DBHelper.close();
	}

	/**
	 * Create a new building using the name, latitude, longitude, description,
	 * and url provided. If the building is successfully created return the new
	 * rowId for that restaurant, otherwise return a -1 to indicate failure.
	 * 
	 * @param name
	 *            the name of the building
	 * @param latitude
	 *            the latitude of the building location
	 * @param longitude
	 *            the longitude of the building location
	 * @param description
	 *            the building description
	 * @param url
	 *            url of an image of the building
	 * @return rowId or -1 if failed
	 */
	public long createBuilding(String name, int latitude, int longitude,
			String description, String url) {

		ContentValues initialValues = new ContentValues(5);
		initialValues.put(COLUMN_NAME, name);
		initialValues.put(COLUMN_LATITUDE, latitude);
		initialValues.put(COLUMN_LONGITUDE, longitude);
		initialValues.put(COLUMN_DESCRIPTION, description);
		initialValues.put(COLUMN_URL, url);

		return db_.insert(BUILDING_TABLE, null, initialValues);
	}

	/**
	 * Delete the building with the given rowId
	 * 
	 * @param rowId
	 *            id of building to delete
	 * @return true if deleted, false otherwise
	 */
	public boolean deleteBuilding(long rowId) {

		return db_.delete(BUILDING_TABLE, COLUMN_ID + "=" + rowId, null) > 0;
	}

	/**
	 * not needed by DBWrapper Get a list of all building IDs in the database
	 * 
	 * @return A List of Longs, where each Long is the ID of one of the
	 *         buildings in the database. Use the fetchBuilding() call to get
	 *         the building object
	 * 
	 * @see DBAdapter.fetchBuilding(long rowId)
	 */
	public List<Long> fetchAllBuildingIDs() {
		Cursor c = db_.query(BUILDING_TABLE, new String[] { COLUMN_ID }, null,
				null, null, null, null, null);

		ArrayList<Long> buildingIds = new ArrayList<Long>();

		// If there are no buildings, return empty list
		if (c.moveToFirst() == false)
			return buildingIds;

		do {
			buildingIds.add(c.getLong(c.getColumnIndex(COLUMN_ID)));
		} while (c.moveToNext());

		c.close();

		return buildingIds;
	}

	/**
	 * not needed by DBWrapper Return a building object for the given building
	 * id.
	 * 
	 * @param rowId
	 *            id of building to retrieve
	 * @return Building that that ID represents in the database
	 * 
	 * @throws SQLException
	 *             if building could not be found/retrieved
	 * 
	 * @TODO - Create a buildingNotFound exception, and throw that instead
	 */
	public Building fetchBuilding(long rowId) throws SQLException {
		Cursor c = db_.query(true, BUILDING_TABLE, new String[] { COLUMN_ID,
				COLUMN_NAME, COLUMN_LATITUDE, COLUMN_LONGITUDE,
				COLUMN_DESCRIPTION, COLUMN_URL }, COLUMN_ID + "=" + rowId,
				null, null, null, null, null);

		if (c.moveToFirst() == false)
			throw new SQLException("building was not found");

		String name = c.getString(c.getColumnIndex(COLUMN_NAME));
		int latitude = c.getInt(c.getColumnIndex(COLUMN_LATITUDE));
		int longitude = c.getInt(c.getColumnIndex(COLUMN_LONGITUDE));
		String desc = c.getString(c.getColumnIndex(COLUMN_DESCRIPTION));
		String url = c.getString(c.getColumnIndex(COLUMN_URL));

		c.close();

		return new Building(rowId, latitude, longitude, name, desc, url);
	}

	/**
	 * Return a Cursor over the list of all buildings in the database
	 * 
	 * @param columns
	 *            An array of column names required to be traversable by the
	 *            returned Cursor
	 * @return This cursor allows you to reference these columns COLUMN_ID,
	 *         COLUMN_NAME, COLUMN_LATITUDE, COLUMN_LONGITUDE,
	 *         COLUMN_DESCRIPTION, COLUMN_URL.
	 */
	public Cursor getCursor(String[] columns) {
		return db_.query(BUILDING_TABLE, columns, null, null, null, null, null);
	}

	/**
	 * @param columns
	 *            An array of column names required to be traversable by the
	 *            returned Cursor
	 * @param rowId
	 *            the rowID of the building to be traversed
	 * @return A cursor to traverse over building with rowID.
	 */
	public Cursor getCursor(String[] columns, long rowId) {
		return db_.query(true, BUILDING_TABLE, columns,
				COLUMN_ID + "=" + rowId, null, null, null, null, null);
	}

	/**
	 * Update the building using the details provided. The building to be
	 * updated is specified using the rowId, and it is altered to use the name,
	 * latitude, longitude, description, favorite values passed in
	 * 
	 * @param rowId
	 *            id of note to update
	 * @param name
	 *            value to set building name to
	 * @param latitude
	 *            value to set building latitude to
	 * @param latitude
	 *            value to set building longitude to
	 * @param description
	 *            value to set building description to
	 * @param url
	 *            value to set building image url to
	 * @return true if the building was successfully updated, false otherwise
	 */
	public boolean updateBuilding(long rowId, String name, int latitude,
			int longitude, String description, String url) {

		ContentValues args = new ContentValues(5);
		args.put(COLUMN_NAME, name);
		args.put(COLUMN_LATITUDE, latitude);
		args.put(COLUMN_LONGITUDE, longitude);
		args.put(COLUMN_DESCRIPTION, description);
		args.put(COLUMN_URL, url);

		return db_.update(BUILDING_TABLE, args, COLUMN_ID + "=" + rowId, null) > 0;
	}

	// these methods allow individual columns to be updated, without
	// having to pull the rest of the building from storage
	public boolean updateColumn(long rowId, String column, int value) {
		ContentValues args = new ContentValues(1);
		args.put(column, value);
		return db_.update(BUILDING_TABLE, args, COLUMN_ID + "=" + rowId, null) > 0;
	}

	public boolean updateColumn(long rowId, String column, long value) {
		ContentValues args = new ContentValues(1);
		args.put(column, value);
		return db_.update(BUILDING_TABLE, args, COLUMN_ID + "=" + rowId, null) > 0;
	}

	public boolean updateColumn(long rowId, String column, String value) {
		ContentValues args = new ContentValues(1);
		args.put(column, value);
		return db_.update(BUILDING_TABLE, args, COLUMN_ID + "=" + rowId, null) > 0;
	}

	/** Used to open a readable database */
	public DBAdapter openReadable() throws SQLException {
		db_ = DBHelper.getReadableDatabase();
		return this;
	}

	/** Used to open a writable database */
	public DBAdapter openWritable() throws SQLException {
		db_ = DBHelper.getWritableDatabase();
		return this;
	}

	/**
	 * Return a Cursor over the list of all buildings in the database sorted by
	 * COLUMN_NAME
	 * 
	 * @return This cursor allows you to reference these columns COLUMN_ID,
	 *         COLUMN_NAME, COLUMN_LATITUDE, COLUMN_LONGITUDE,
	 *         COLUMN_DESCRIPTION, COLUMN_URL.
	 */
	public Cursor fetchAllBuildingsSortedCursor() {
		return db_.query(BUILDING_TABLE, new String[] { COLUMN_ID, COLUMN_NAME,
				COLUMN_LATITUDE, COLUMN_LONGITUDE, COLUMN_DESCRIPTION,
				COLUMN_URL }, null, null, null, null, COLUMN_NAME);
	}

	/**
	 * Return a Cursor over the list of some buildings in the database Where
	 * COLUMN_NAME like %compareText% sorted by COLUMN_NAME
	 * 
	 * @return This cursor allows you to reference these columns COLUMN_ID,
	 *         COLUMN_NAME, COLUMN_LATITUDE, COLUMN_LONGITUDE,
	 *         COLUMN_DESCRIPTION, COLUMN_URL.
	 */
	public Cursor fetchSomeBuildingsSortedCursor(String compareText) {
		String whereClause = COLUMN_NAME + " like '%" + compareText + "%'";
		return db_.query(BUILDING_TABLE, new String[] { COLUMN_ID, COLUMN_NAME,
				COLUMN_LATITUDE, COLUMN_LONGITUDE, COLUMN_DESCRIPTION,
				COLUMN_URL }, whereClause, null, null, null, COLUMN_NAME);
	}

}