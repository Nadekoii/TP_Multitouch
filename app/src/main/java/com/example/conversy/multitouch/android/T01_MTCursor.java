/*
 * Copyright (c) 2016 Stéphane Conversy - ENAC - All rights Reserved
 */
package com.example.conversy.multitouch.android;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

import fr.liienac.statemachine.geometry.Point;

public class T01_MTCursor extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(new MyView(this));
	}


	public class MyView extends View {
		Map<Integer, Cursor> cursors = new HashMap<>();

		// Cache paints to avoid recreating them at each draw
		Paint paint;

		public MyView(Context c) {
			super(c);
			paint = new Paint();
		}

		@Override
		protected void onSizeChanged(int w, int h, int oldw, int oldh) {
			super.onSizeChanged(w, h, oldw, oldh);
		}

		@Override
		protected void onDraw(Canvas canvas) {
			// "erase" canvas (fill it with white)
			canvas.drawColor(0xFFAAAAAA);

			// Draw cursors
			for (Map.Entry<Integer, Cursor> entry : cursors.entrySet()) {
				Cursor c = entry.getValue();
				paint.setARGB(100, c.r, c.g, c.b);
				canvas.drawCircle(c.p.x, c.p.y, 70, paint);
				paint.setARGB(100, 0, 0, 0);
				canvas.drawText(""+c.id, c.p.x+50, c.p.y-50, paint);
			}
		}


		//---------- Low level touch management

		private void onTouchDown(Point p, int cursorid) {
			Cursor c = new Cursor(cursorid, p);
			c.r = (int) Math.floor(Math.random() * 100);
			c.g = (int) Math.floor(Math.random() * 100);
			c.b = (int) Math.floor(Math.random() * 100);
			cursors.put(Integer.valueOf(cursorid), c);
		}

		private void onTouchMove(Point p, int cursorid) {
			Cursor c = cursors.get(Integer.valueOf(cursorid));
			c.p = p;
		}

		private void onTouchUp(Point p, int cursorid) {
			cursors.remove(Integer.valueOf(cursorid));
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			int action = event.getActionMasked();
			int index = event.getActionIndex();
			int id = event.getPointerId(index);
			float x, y;

			switch (action) {
				case MotionEvent.ACTION_DOWN:
				case MotionEvent.ACTION_POINTER_DOWN:
					x = event.getX(index);
					y = event.getY(index);
					onTouchDown(new Point(x, y), id);
					break;
				case MotionEvent.ACTION_MOVE:
					for (int i=0; i<event.getPointerCount(); ++i) {
						x = event.getX(i);
						y = event.getY(i);
						id = event.getPointerId(i);
						onTouchMove(new Point(x, y), id);
					}
					break;
				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_POINTER_UP:
					x = event.getX(index);
					y = event.getY(index);
					onTouchUp(new Point(x, y), id);
					break;
			}

			invalidate();

			return true;
		}
	}
}