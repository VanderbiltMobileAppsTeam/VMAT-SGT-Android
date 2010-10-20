/**
 * Android Campus Maps
 *  http://code.google.com/p/vuphone/
 * 
 * @author Adam Albright
 * @date Oct 30, 2009
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

import java.util.Calendar;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

/**
 * This class implements a marker/pin that can be placed and drug around on the
 * map
 */
public class GPSMarker extends com.google.android.maps.Overlay {
	GeoPoint location_;
	int marker_image_;
	boolean dragging_ = false;
	boolean showing_ = false;
	long lastTap_ = 0;

	public GPSMarker() {
	}

	/**
	 * Used to place the marker on the map
	 */
	public void showMarker() {
		if (showing_)
			return;

		List<Overlay> listOfOverlays = Main.mapView_.getOverlays();
		listOfOverlays.add(this);

		Main.mapView_.invalidate();
		showing_ = true;
	}

	/**
	 * Used to remove the marker from the map
	 */
	public void hideMarker() {
		if (!showing_)
			return;

		List<Overlay> listOfOverlays = Main.mapView_.getOverlays();
		listOfOverlays.remove(this);

		Main.mapView_.invalidate();
		showing_ = false;
	}

	public void setLocation(GeoPoint p) {
		location_ = p;
	}

	/**
	 * Called several times per second when the screen refreshes
	 */
	@Override
	public boolean draw(Canvas canvas, MapView mapView, boolean shadow,
			long when) {
		if (location_ == null || showing_ == false)
			return false;

		super.draw(canvas, mapView, shadow);

		// convert GeoPoint to screen pixels
		Point screenPts = new Point();
		mapView.getProjection().toPixels(location_, screenPts);

		// drop a random colored pin
		Bitmap bmp = BitmapFactory.decodeResource(Main.resources_,
				R.drawable.blue_dot);
		canvas.drawBitmap(bmp, screenPts.x, screenPts.y - 10, null);
		return true;
	}

	/**
	 * Called when this marker is double-clicked
	 */
	public void onDoubleTap() {
		echo("You double tapped your current location!");
	}

	/**
	 * Called when the user taps anywhere on the screen
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event, MapView mapView) {
		if (location_ == null || showing_ == false)
			return false;

		super.onTouchEvent(event, mapView);

		GeoPoint p = mapView.getProjection().fromPixels((int) event.getX(),
				(int) event.getY());

		// are they are starting or stopping a drag?
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			long curTime = Calendar.getInstance().getTimeInMillis();

			int diff_lat = p.getLatitudeE6() - location_.getLatitudeE6();
			int diff_long = p.getLongitudeE6() - location_.getLongitudeE6();

			// Hit test
			// TODO update the hit-test ranges
			if (diff_lat < 800 && diff_lat > -50 && diff_long < 700
					&& diff_long > -150) {
				if ((curTime - lastTap_) < 1500) {
					onDoubleTap();
					lastTap_ = 0;
					return true;
				} else {
					lastTap_ = curTime;
				}
			}
		}
		return false;
	}

	/**
	 * Prints a message to the screen for a few seconds
	 * 
	 * @param s
	 *            String to print
	 */
	public void echo(String s) {
		Main.echo(s);
	}

	/**
	 * Prints a message to LogCat with tag='mad'
	 * 
	 * @param s
	 *            String to print
	 */
	public void trace(String s) {
		Log.d("mad", s);
	}
}
