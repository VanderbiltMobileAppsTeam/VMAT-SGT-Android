/**
 * Android Campus Maps
 *  http://code.google.com/p/vuphone/
 * 
 * @author Rob McColl
 * @date Oct 13, 2009
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

/**
 * Add this Overlay to a MapView to have paths you add to this class drawn on
 * the map.
 * 
 * Paths are kept internally in a HashMap as an ArrayList of GeoPoints like
 * this:
 * 
 * HashMap { {Int ID, List ( geopoint, geopoint, geopoint)} {Int ID, List (
 * geopoint, geopoint, geopoint)} {Int ID, List ( geopoint, geopoint, geopoint)}
 * }
 * 
 * Each path in the HashMap has a unique int ID number so you can select a path
 * and add points to it later. You can also remove paths by ID.
 */
public class PathOverlay extends Overlay {

	// Turn LogCat output on or off
	final boolean _DEBUGON_ = false;

	Paint paintPath_ = null;
	MapView mvMap_ = null;
	HashMap<Integer, ArrayList<GeoPoint>> hmPathList_ = null;
	int iCurID_;
	int iNextID_;

	/**
	 * Constructor.
	 * 
	 * @param view
	 *            - Pass it the MapView you want the Overlay added to.
	 */
	public PathOverlay(MapView view) {
		if (_DEBUGON_)
			Log.d("CampusMaps", "PathOverlay.PathOverlay()");
		paintPath_ = new Paint();
		paintPath_.setAntiAlias(true);
		mvMap_ = view;
		iCurID_ = -1;
		iNextID_ = 1;
		hmPathList_ = new HashMap<Integer, ArrayList<GeoPoint>>();
		mvMap_.getOverlays().add(this);
	}

	/**
	 * StartNewPath
	 * 
	 * @param gpNew
	 *            - First geopoint of the new path.
	 * @return int ID - ID number corresponding to the path you created. Use
	 *         this to select the path later to add points to it, or to remove
	 *         the path later on.
	 */
	public int StartNewPath(GeoPoint gpNew) {
		if (_DEBUGON_)
			Log.d("CampusMaps", "PathOverlay.StartNewPath()");
		iCurID_ = iNextID_;
		iNextID_++;
		ArrayList<GeoPoint> lstNewPath = new ArrayList<GeoPoint>();
		lstNewPath.add(gpNew);
		hmPathList_.put(iCurID_, lstNewPath);
		return iCurID_;
	}

	/**
	 * Ends the current path (by ensuring the next point will be added to a
	 * completely new path).
	 * 
	 * @return int ID of the path you just ended
	 */
	public int EndCurrentPath() {
		if (_DEBUGON_)
			Log.d("CampusMaps", "PathOverlay.EndCurrentPath()");
		int rtn = iCurID_;
		iCurID_ = -1;
		return rtn;
	}

	/**
	 * Selects an existing path for extension.
	 * 
	 * @param ID
	 *            - the ID of the path you want to select
	 * @return 0 on success, -1 if the ID does not exist
	 */
	public int SelectPath(int ID) {
		if (_DEBUGON_)
			Log.d("CampusMaps", "PathOverlay.SelectPath()");
		if (hmPathList_.containsKey(ID)) {
			iCurID_ = ID;
			return 0;
		} else {
			return -1;
		}
	}

	/**
	 * Removes a path permanently.
	 * 
	 * @param ID
	 *            - the ID of the path you want to remove
	 * @return 0 on success, -1 if the path does not exist
	 */
	public int RemovePath(int ID) {
		if (_DEBUGON_)
			Log.d("CampusMaps", "PathOverlay.RemoveID()");
		if (hmPathList_.containsKey(ID)) {
			hmPathList_.remove(ID);
			if (iCurID_ == ID) {
				iCurID_ = -1;
			}
			return 0;
		} else {
			return -1;
		}
	}

	/**
	 * Add a point to the currently selected path.
	 * 
	 * @param gpNew
	 *            - the GeoPoint you wish to add to the path.
	 * @return int ID of the current path
	 */
	public int AddPoint(GeoPoint gpNew) {
		if (_DEBUGON_)
			Log.d("CampusMaps", "PathOverlay.AddPoint()");
		if (iCurID_ != -1) {
			hmPathList_.get(iCurID_).add(gpNew);
			return iCurID_;
		} else {
			return StartNewPath(gpNew);
		}
	}

	/**
	 * Overloaded draw() Iterates through the HashMap of GeoPoint paths,
	 * converting each into a drawable Path of screen coordinates and drawing
	 * the path before moving to the next path. This is called by the MapView.
	 */
	@Override
	public boolean draw(Canvas canv, MapView mvMap, boolean b, long when) {
		super.draw(canv, mvMap, b, when);
		if (_DEBUGON_)
			Log.d("CampusMaps", "PathOverlay.draw()");

		// Set up the path painter
		paintPath_.setStrokeWidth(4);
		paintPath_.setARGB(100, 113, 105, 252);
		paintPath_.setStyle(Paint.Style.STROKE);

		// Drawing loop
		Iterator<Entry<Integer, ArrayList<GeoPoint>>> itMap = hmPathList_
				.entrySet().iterator();
		while (itMap.hasNext()) {
			Path curPath = new Path();
			Entry<Integer, ArrayList<GeoPoint>> curPair = itMap.next();
			Iterator<GeoPoint> itCurGeoPoint = curPair.getValue().iterator();
			Point curPoint = new Point();
			if (itCurGeoPoint.hasNext()) {
				mvMap.getProjection().toPixels(itCurGeoPoint.next(), curPoint);
				curPath.moveTo(curPoint.x, curPoint.y);
			}
			while (itCurGeoPoint.hasNext()) {
				mvMap.getProjection().toPixels(itCurGeoPoint.next(), curPoint);
				curPath.lineTo(curPoint.x, curPoint.y);
			}
			canv.drawPath(curPath, paintPath_);
		}
		return true;
	}
}
