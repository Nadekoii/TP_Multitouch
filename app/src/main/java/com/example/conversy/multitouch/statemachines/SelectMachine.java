package com.example.conversy.multitouch.statemachines;

import android.util.Log;

import java.util.LinkedHashMap;
import java.util.Map;

import fr.liienac.statemachine.StateMachine;
import fr.liienac.statemachine.event.Move;
import fr.liienac.statemachine.event.Press;
import fr.liienac.statemachine.event.Release;
import fr.liienac.statemachine.geometry.Point;
import fr.liienac.statemachine.geometry.Vector;
import fr.liienac.statemachine.graphic.GraphicItem;

public class SelectMachine extends StateMachine {
    GraphicItem graphicItem = null;
    Map<Integer, Point> cursorsStartPoints = new LinkedHashMap<>();


    public SelectMachine(GraphicItem graphicItem) {
        this.graphicItem = graphicItem;
    }

    ;

    public State start = new State() {
        Transition press = new Transition<Press>() {
            public boolean guard() {
                return evt.graphicItem == graphicItem;
            }

            public void action() {
                cursorsStartPoints.put(evt.cursorID, evt.p);
                graphicItem.setStyle(255, 0, 0);
            }

            public State goTo() {
                return touched;
            }
        };
    };

    public State touched = new State() {
        Vector moveVector;
        Point stopPoint;
        Transition press = new Transition<Press>() {
            public boolean guard() {
                return evt.graphicItem == graphicItem;
            }

            public void action() {
                cursorsStartPoints.put(evt.cursorID, evt.p);
            }

        };

        Transition release = new Transition<Release>() {
            public boolean guard() {
                return cursorsStartPoints.containsKey(evt.cursorID);
            }

            public void action() {
                cursorsStartPoints.remove(Integer.valueOf(evt.cursorID));
            }

            public State goTo() {
                if (cursorsStartPoints.isEmpty()) {
                    graphicItem.setStyle(0, 0, 0);
                    return start;
                } else
                    return touched;
            }
        };

        Transition move = new Transition<Move>() {
            public boolean guard() {
                if (!cursorsStartPoints.containsKey(evt.cursorID)) return false;
                Point startPoint = cursorsStartPoints.get(evt.cursorID);
                stopPoint = evt.p;
                moveVector = new Vector(startPoint, stopPoint);
                cursorsStartPoints.put(evt.cursorID, stopPoint);
                return (cursorsStartPoints.entrySet().iterator().next().getKey() == evt.cursorID) && (Point.distanceSq(startPoint, stopPoint) > 500);
            }

            public void action() {
                graphicItem.translateBy(moveVector);
            }

            public State goTo() {
                return moving;
            }
        };
    };

    public State moving = new State() {
        Vector moveVector;
        Point stopPoint;
        Transition press = new Transition<Press>() {
            public boolean guard() {
                return evt.graphicItem == graphicItem;
            }

            public void action() {
                cursorsStartPoints.put(evt.cursorID, evt.p);
            }
        };

        Transition release = new Transition<Release>() {
            public boolean guard() {
                return cursorsStartPoints.containsKey(evt.cursorID);
            }

            public void action() {
                cursorsStartPoints.remove(Integer.valueOf(evt.cursorID));
            }

            public State goTo() {
                if (cursorsStartPoints.isEmpty()) {
                    graphicItem.setStyle(0, 0, 0);
                    return start;
                } else
                    return moving;
            }
        };

        Transition move = new Transition<Move>() {
            public boolean guard() {
                if (!cursorsStartPoints.containsKey(evt.cursorID)) return false;
                Point startPoint = cursorsStartPoints.get(evt.cursorID);
                stopPoint = evt.p;
                moveVector = new Vector(startPoint, stopPoint);
                cursorsStartPoints.put(evt.cursorID, stopPoint);
                return (cursorsStartPoints.entrySet().iterator().next().getKey() == evt.cursorID);
            }

            public void action() {
                graphicItem.translateBy(moveVector);
            }
        };
    };
}