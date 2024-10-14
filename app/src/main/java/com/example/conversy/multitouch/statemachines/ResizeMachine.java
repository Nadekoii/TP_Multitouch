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

    public class ResizeMachine extends StateMachine {
        GraphicItem graphicItem = null;
        Map<Integer,Point> cursorsStartPoints = new LinkedHashMap<>();


        public ResizeMachine(GraphicItem graphicItem){
            this.graphicItem = graphicItem;
        };

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

                public State goTo() {
                    return resizing;
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
                    if (cursorsStartPoints.isEmpty()){
                        graphicItem.setStyle(0, 0, 0);
                        return start;
                    }
                    else
                        return touched;
                }
            };

            Transition move = new Transition<Move>(){
                public boolean guard(){
                    if (!cursorsStartPoints.containsKey(evt.cursorID)) return false;
                    Point startPoint = cursorsStartPoints.get(evt.cursorID);
                    stopPoint=evt.p;
                    moveVector = new Vector(startPoint,stopPoint);
                    cursorsStartPoints.put(evt.cursorID,stopPoint);
                    return (cursorsStartPoints.entrySet().iterator().next().getKey() ==evt.cursorID)&&(Point.distanceSq(startPoint,stopPoint)>500);
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

                public State goTo() {
                    return resizing;
                }
            };

            Transition release = new Transition<Release>() {
                public boolean guard() { return cursorsStartPoints.containsKey(evt.cursorID); }

                public void action() {
                    cursorsStartPoints.remove(Integer.valueOf(evt.cursorID));
                }

                public State goTo() {
                    if (cursorsStartPoints.isEmpty()){
                        graphicItem.setStyle(0, 0, 0);
                        return start;
                    }
                    else
                        return moving;
                }
            };

            Transition move = new Transition<Move>(){
                public boolean guard(){
                    if (!cursorsStartPoints.containsKey(evt.cursorID)) return false;
                    Point startPoint = cursorsStartPoints.get(evt.cursorID);
                    stopPoint=evt.p;
                    moveVector = new Vector(startPoint,stopPoint);
                    cursorsStartPoints.put(evt.cursorID,stopPoint);
                    return (cursorsStartPoints.entrySet().iterator().next().getKey() ==evt.cursorID)&&(Point.distanceSq(startPoint,stopPoint)>5);
                }

                public void action() {
                    graphicItem.translateBy(moveVector);
                }
            };
        };

        public State resizing = new State() {
            Point stopPoint;
            Point centroidCenter;
            float scaleFactor;

            Transition press = new Transition<Press>() {
                public boolean guard() {
                    return evt.graphicItem == graphicItem;
                }

                public void action() {
                    cursorsStartPoints.put(evt.cursorID, evt.p);
                }
            };

            Transition release = new Transition<Release>() {
                public boolean guard() { return cursorsStartPoints.containsKey(evt.cursorID); }

                public void action() {
                    cursorsStartPoints.remove(Integer.valueOf(evt.cursorID));
                }

                public State goTo() {
                    if (cursorsStartPoints.size()==1){
                        return touched;
                    }
                    else
                        return resizing;
                }
            };

            Transition move = new Transition<Move>() {
                public boolean guard() {
                    return (cursorsStartPoints.containsKey(evt.cursorID));
                }

                public void action() {
                    //if (!cursorsStartPoints.containsKey(evt.cursorID)) return false;

                    float centroidX = 0.0F;
                    float centroidY = 0.0F;
                    for (Map.Entry<Integer, Point> entry : cursorsStartPoints.entrySet()) {
                        if (entry.getKey().equals(evt.cursorID)) {
                            continue;
                        }
                        centroidX += entry.getValue().x;
                        centroidY += entry.getValue().y;
                    }
                    centroidX = centroidX / (cursorsStartPoints.size() - 1);
                    centroidY = centroidY / (cursorsStartPoints.size() - 1);
                    centroidCenter = new Point(centroidX, centroidY);

                    Point startPoint = cursorsStartPoints.get(evt.cursorID);
                    stopPoint = evt.p;
                    float resizeBefore = Point.distance(centroidCenter, startPoint);
                    float resizeAfter = Point.distance(centroidCenter, stopPoint);
                    scaleFactor = resizeAfter / resizeBefore;
                    cursorsStartPoints.put(evt.cursorID, stopPoint);

                    graphicItem.scaleBy(scaleFactor, centroidCenter);
                }
            };
        };


    }