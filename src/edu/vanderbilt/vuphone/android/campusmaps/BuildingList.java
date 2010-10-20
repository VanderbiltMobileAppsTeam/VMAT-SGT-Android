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

package edu.vanderbilt.vuphone.android.campusmaps;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import android.app.ListActivity;
import android.database.sqlite.SQLiteCursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;

import edu.vanderbilt.vuphone.android.campusmaps.storage.Building;
import edu.vanderbilt.vuphone.android.campusmaps.storage.DBAdapter;
import edu.vanderbilt.vuphone.android.campusmaps.tools.Tools;
import edu.vanderbilt.vuphone.android.campusmaps.tools.XMLTools;

public class BuildingList extends ListActivity {

	private EditText filterText = null;
	SimpleCursorAdapter simpleCursorAdapter = null;
	ArrayAdapter<Building> dataAdapter = null;
	private static Map<Long, Building> buildings_ = null;
	private DBAdapter dbAdapter_;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.buildinglist);

		filterText = (EditText) findViewById(R.building_list.search_box);
		filterText.addTextChangedListener(filterTextWatcher);

		if (dbAdapter_ == null)
			dbAdapter_ = new DBAdapter(this);

		populateBuildings();

		// Variables to map from db column names to cell names in display
		String[] from = new String[] { DBAdapter.COLUMN_NAME,
				DBAdapter.COLUMN_ID };
		int[] to = new int[] { R.list_view.buildingName, R.list_view.buildingID };

		SimpleCursorAdapter sca = new SimpleCursorAdapter(
				getApplicationContext(), R.layout.building_list_item,
				dbAdapter_.fetchAllBuildingsSortedCursor(), from, to);

		setListAdapter(sca);

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		// mod this
		SQLiteCursor sqlc = (SQLiteCursor) getListView().getItemAtPosition(
				position);
		long buildingID = sqlc
				.getLong(sqlc.getColumnIndex(DBAdapter.COLUMN_ID));
		Building bc = dbAdapter_.fetchBuilding(buildingID);
		Main.trace(bc.getName() + " selected");

		// TODO open a menu that asks what they want to do

		// Drop a pin
		Main.getInstance().drop_pin(bc);

		super.finish();
	}

	private TextWatcher filterTextWatcher = new TextWatcher() {

		public void afterTextChanged(Editable s) {
		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// Variables to map from db column names to cell names in display
			String[] from = new String[] { DBAdapter.COLUMN_NAME,
					DBAdapter.COLUMN_ID };
			int[] to = new int[] { R.list_view.buildingName, R.list_view.buildingID };

			SimpleCursorAdapter sca = new SimpleCursorAdapter(
					getApplicationContext(), R.layout.building_list_item,
					dbAdapter_.fetchSomeBuildingsSortedCursor(s.toString()), from, to);

			setListAdapter(sca);
		}

	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		filterText.removeTextChangedListener(filterTextWatcher);
	}

	/**
	 * Returns a list of building names
	 * 
	 * @return
	 */
	public List<String> getBuildingNames() {
		List<String> list = new ArrayList<String>();

		Iterator<Building> iter = buildings_.values().iterator();
		while (iter.hasNext()) {
			list.add(iter.next().getName());
		}

		return list;
	}

	/**
	 * Provides access to the database of buildings
	 * 
	 * @return
	 */
	public static Map<Long, Building> getBuildingList() {
		if (buildings_ == null)
			buildings_ = new HashMap<Long, Building>();

		return buildings_;
	}

	/**
	 * Parses in the building data to populate BuildingList
	 */
	public void populateBuildings() {

		if (isNewListAvailable()
				|| dbAdapter_.fetchAllBuildingIDs().size() == 0) {

			Main.trace("Parsing building list from XML");

			InputStream xmlData = null;
			try {
				xmlData = getResources().getAssets().open("buildings.xml");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			loadFromXML(xmlData);

			// Cache the building list
			updateDataBase();
		}

	}

	private static boolean isNewListAvailable() {
		// TODO check server for updated building list

		return false;
	}

	private void updateDataBase() {
		// TODO: Check if building exists in db before committing.
		Map<Long, Building> buildings = getBuildingList();

		for (Long l : buildings.keySet()) {
			dbAdapter_.createBuilding(buildings.get(l).getName(), buildings
					.get(l).getLat_(), buildings.get(l).getLong_(), buildings
					.get(l).getDescription(), buildings.get(l).getImageURL());
		}

		Main.trace("Building list database has been updated");
	}

	/**
	 * Prints a message to the screen for a few seconds
	 */
	public void echo(String s) {
		Toast.makeText(getBaseContext(), s, Toast.LENGTH_SHORT).show();
	}

	private static void loadFromXML(InputStream xmlData) {
		Map<Long, Building> bList = BuildingList.getBuildingList();

		Document doc = XMLTools.parseXML(xmlData);

		if (doc == null)
			return;

		int i;
		NodeList list_ = doc.getElementsByTagName("feature");
		for (i = 0; i < list_.getLength(); i++) {
			Properties attrib = XMLTools.NodeList2Array(list_.item(i)
					.getChildNodes());

			if (attrib == null)
				continue;

			String name = Tools.titleCase(attrib.getProperty("FACILITY_NAME"));

			if (!attrib.containsKey("coordinates"))
				continue;

			String loc[] = attrib.getProperty("coordinates").split(" ");
			String latlong[] = loc[0].split(",");
			GeoPoint gp = Tools.EPSG900913ToGeoPoint(Double
					.parseDouble(latlong[0]), Double.parseDouble(latlong[1]));
			String url = "http://www.vanderbilt.edu/map/"
					+ attrib.getProperty("FACILITY_URL").toLowerCase();

			Building b = new Building(i, gp.getLatitudeE6(), gp
					.getLongitudeE6(), name, attrib
					.getProperty("FACILITY_REMARKS"), url);

			bList.put(new Long(i), b);
		}
	}
}
