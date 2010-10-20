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

import java.net.URL;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import edu.vanderbilt.vuphone.android.campusmaps.storage.Building;
import edu.vanderbilt.vuphone.android.campusmaps.storage.DBAdapter;

public class BuildingInfo extends Activity {

	private DBAdapter dbAdapter_;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (dbAdapter_ == null)
			dbAdapter_ = new DBAdapter(this);

		setContentView(R.layout.buildinginfo);

		Bundle extras = getIntent().getExtras();
		long id = -1;
		if (extras == null || (id = extras.getLong("building_id")) < 0)
			return;

		Building b = dbAdapter_.fetchBuilding(id);
		if (b == null)
			finish();

		TextView tv = (TextView) findViewById(R.id.buildingName);
		tv.setText(b.getName());

		String img = null;
		if ((img = b.getImageURL()) != null) {
			ImageView iv = (ImageView) findViewById(R.id.buildingImage);

			BitmapDrawable bm = null;

			try {

				bm = new BitmapDrawable(new URL(img).openStream());

				if (bm == null)
					throw new Exception("BitmapDrawable sucks...");

				iv.setImageDrawable(bm);
			} catch (Exception e) {
				Main.trace("Couldn't download image: " + e.getMessage());
			}
		}

		TextView tv2 = (TextView) findViewById(R.id.buildingDesc);
		tv2.setText(b.getDescription());

	}

}
