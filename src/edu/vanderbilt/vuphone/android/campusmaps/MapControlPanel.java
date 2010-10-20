/**
 * Android Campus Maps
 *  http://code.google.com/p/vuphone/
 * 
 * @author Adam Albright
 * @date Nov 07, 2009
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

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

/*
 * This class provides an overlay panel to control the MapView
 */
public class MapControlPanel extends RelativeLayout {
	private Paint innerPaint, borderPaint;

	/*
	 * init: used to indicate if the panel buttons have been configured
	 */
	private boolean buttonsInitialized = false;

	public MapControlPanel(Context context, AttributeSet attrs) {
		super(context, attrs);
		initializeCanvas();
	}

	public MapControlPanel(Context context) {
		super(context);
		initializeCanvas();
	}

	/*
	 * Initializes the overlay panel for the map
	 */
	private void initializeCanvas() {
		innerPaint = new Paint();
		innerPaint.setARGB(225, 75, 75, 75); // gray
		innerPaint.setAntiAlias(true);

		borderPaint = new Paint();
		borderPaint.setARGB(255, 255, 255, 255);
		borderPaint.setAntiAlias(true);
		borderPaint.setStyle(Style.STROKE);
		borderPaint.setStrokeWidth(2);
	}

	/*
	 * Configures the panel buttons once the canvas has been initialized
	 */
	public void initializeButtons() {
		buttonsInitialized = true;

		ImageButton zoomIn = (ImageButton) findViewById(R.id.button_zoom_in);
		ImageButton zoomOut = (ImageButton) findViewById(R.id.button_zoom_out);
		ImageButton centergps = (ImageButton) findViewById(R.id.button_center_gps);

		zoomIn.setImageResource(android.R.drawable.btn_plus);
		zoomIn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Main.mapView_.getController().zoomIn();
			}
		});

		zoomOut.setImageResource(android.R.drawable.btn_minus);
		zoomOut.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Main.mapView_.getController().zoomOut();
			}
		});

		centergps.setImageResource(R.drawable.centergps);
		centergps.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Main.gps_.centerOnGPS(!Main.gps_.centerOnGPS_);
			}
		});
	}

	/*
	 * Called whenever the panel is to be redrawn
	 */
	@Override
	protected void dispatchDraw(Canvas canvas) {
		if (!buttonsInitialized)
			initializeButtons();

		RectF drawRect = new RectF();
		drawRect.set(0, 0, getMeasuredWidth(), getMeasuredHeight());

		canvas.drawRoundRect(drawRect, 5, 5, innerPaint);
		canvas.drawRoundRect(drawRect, 5, 5, borderPaint);

		super.dispatchDraw(canvas);
	}
}
