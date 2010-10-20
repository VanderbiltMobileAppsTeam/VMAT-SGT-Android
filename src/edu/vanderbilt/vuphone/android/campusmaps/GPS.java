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

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;

public class GPS implements LocationListener {
	private static GPS instance_;
	MapView mapView_;
	private GPSMarker marker_ = null;
	private Location loc_ = null;
	public boolean centerOnGPS_ = false;

	protected GPS() {
	}

	/**
	 * Singleton instantiator
	 */
	public static GPS getInstance() {
		if (instance_ == null)
			instance_ = new GPS();

		return instance_;
	}

	/**
	 * Initializes the GPS component
	 */
	public void initialize(LocationManager lm) {
		marker_ = new GPSMarker();

		// Set the last known location
		onLocationChanged(lm.getLastKnownLocation(LocationManager.GPS_PROVIDER));

		// Request to be notified whenever the user moves
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 2, this);
	}
	
	public void uninitialize(LocationManager lm) {
		lm.removeUpdates(this);
	}

	public void centerOnGPS(boolean t) {
		centerOnGPS_ = t;
		if (t && loc_ != null) {
			Main.centerMapAt(new GeoPoint((int) (loc_.getLatitude() * 1E6),
					(int) (loc_.getLongitude() * 1E6)));
		}

	}

	/**
	 * Called by the GPS service to inform us of the current position
	 */

	public void onLocationChanged(Location l) {
		if (l != null && !l.equals(loc_)) {
			GeoPoint g = new GeoPoint((int) (l.getLatitude() * 1E6), (int) (l
					.getLongitude() * 1E6));
			marker_.setLocation(g);

			if (centerOnGPS_)
				Main.centerMapAt(g);

			if (l.getProvider().equals(LocationManager.GPS_PROVIDER)) {
				// Location data is from the GPS
			}

			loc_ = l;
		} else {
			trace("You haven't moved");
		}
	}

	public void onProviderDisabled(String provider) {
		trace("Provider Disabled" + provider);
	}

	public void onProviderEnabled(String provider) {
		trace("Provider Enabled: " + provider);
		Main.echo(provider + " tracking enabled");
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		if (extras != null) {
			// Satellite Count
			extras.getInt("satellites");
		}
	}

	/**
	 * Shows GPS maker on the map
	 */
	public void showMarker() {
		marker_.showMarker();
	}

	/**
	 * Removes GPS maker from the map
	 */
	public void hideMarker() {
		marker_.hideMarker();
	}

	/**
	 * Prints a message to LogCat with tag='mad'
	 * 
	 * @param s
	 *            String to print
	 */
	public static void trace(String s) {
		Main.trace(s);
	}

}
