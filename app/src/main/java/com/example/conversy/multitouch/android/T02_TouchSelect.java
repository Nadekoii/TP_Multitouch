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

import com.example.conversy.multitouch.statemachines.RRRMachine;
import com.example.conversy.multitouch.statemachines.ResizeMachine;
import com.example.conversy.multitouch.statemachines.RotateMachine;
import com.example.conversy.multitouch.statemachines.SelectMachine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import fr.liienac.statemachine.StateMachine;
import fr.liienac.statemachine.event.Move;
import fr.liienac.statemachine.event.PositionalEvent;
import fr.liienac.statemachine.event.Press;
import fr.liienac.statemachine.event.Release;
import fr.liienac.statemachine.geometry.Point;

public class T02_TouchSelect extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(new MyView(this));
	}


	public class MyView extends View {
		Map<Integer, Cursor> cursors = new HashMap<>();

		// Scenegraph
		Collection<GraphicItemAndroid> sceneGraph = new ArrayList<>();

		// State machines
		Collection<StateMachine> machines = new ArrayList<>();

		public MyView(final Context c) {
			super(c);

			// Cache paints to avoid recreating them at each draw
			paint = new Paint();
			pickingPaint = new Paint();
			pickingPaint.setAntiAlias(false);

			// Graphic item and state machine
			GraphicItemAndroid graphicItem1 = new GraphicItemAndroid(0, 0, 600, 600);
			sceneGraph.add(graphicItem1);
			GraphicItemAndroid graphicItem2 = new GraphicItemAndroid(600, 600, 600, 600);
			sceneGraph.add(graphicItem2);
			StateMachine machine1 = new ResizeMachine(graphicItem1);
			machines.add(machine1);
			StateMachine machine2 = new RRRMachine(graphicItem2);
			machines.add(machine2);
			machine2.setDebug(true);
		}


		//---------- Drawing & picking

		ColorPicking colorPicking = new ColorPicking();

		@Override
		protected void onSizeChanged(int w, int h, int oldw, int oldh) {
			super.onSizeChanged(w, h, oldw, oldh);
			colorPicking.onSizeChanged(w, h);
		}

		// Cache paints to avoid recreating them at each draw
		Paint paint;
		Paint pickingPaint;

		@Override
		protected void onDraw(Canvas canvas) {
			// "Erase" canvas (fill it with white)
			canvas.drawColor(0xFFAAAAAA);
			colorPicking.reset();

			// Draw scene graph
			for (GraphicItemAndroid graphicItem : sceneGraph) {
				// Display view
				graphicItem.draw(canvas, paint);

				// Picking view
				graphicItem.drawPickingView(colorPicking, pickingPaint);
			}

			if (false) { // debug: show picking view
				canvas.drawBitmap(colorPicking.bitmap, 0, 0, paint);
			}

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
			GraphicItemAndroid s = (GraphicItemAndroid) colorPicking.pick(p);
			PositionalEvent evt = new Press(cursorid, p, s, 0);
			for (StateMachine m : machines) {
				m.handleEvent(evt);
			}

			Cursor c = new Cursor(cursorid, p);
			c.r = (int) Math.floor(Math.random() * 100);
			c.g = (int) Math.floor(Math.random() * 100);
			c.b = (int) Math.floor(Math.random() * 100);
			cursors.put(Integer.valueOf(c.id), c);
		}

		private void onTouchMove(Point p, int cursorid) {
			Cursor c = cursors.get(Integer.valueOf(cursorid));
			if (Point.distance(c.p, p) > 0) {
				GraphicItemAndroid s = (GraphicItemAndroid) colorPicking.pick(p);
				PositionalEvent evt = new Move(cursorid, p, s, 0);
				for (StateMachine m : machines) {
					m.handleEvent(evt);
				}
				c.p = p;
			}
		}

		private void onTouchUp(Point p, int cursorid) {
			GraphicItemAndroid s = (GraphicItemAndroid) colorPicking.pick(p);
			PositionalEvent evt = new Release(cursorid, p, s, 0);
			for (StateMachine m : machines) {
				m.handleEvent(evt);
			}
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