/*
 * Copyright (c) 2016 Stéphane Conversy, Nicolas Saporito - ENAC - All rights Reserved
 */
package fr.liienac.statemachine.event;

import fr.liienac.statemachine.geometry.Point;

public class Move<Item> extends PositionalEvent<Item> {
	public Move(int cursorid_, Point p_, Item s_, float angRad) { super(cursorid_, p_, s_, angRad); }
}