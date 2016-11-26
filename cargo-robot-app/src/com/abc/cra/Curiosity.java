/**
 * 
 */
package com.abc.cra;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Nikola Georgiev
 * 
 * @email nikigeorgiev2000@gmail.com
 *
 */
class Curiosity implements Runnable {

	private final String TAG = "Curiosity";

	private PathFinder context;
	private Map storage;
	private Point startPoint;
	private List<Point> adp;
	private int lastDirection;

	/**
	 * @param PathFinder
	 *            context
	 * @param Point
	 *            startPoint
	 * @param List
	 *            <Point> adp
	 * @param int lastDirection
	 * @param Map
	 *            mapArray
	 */
	Curiosity(PathFinder context, Point startPoint, List<Point> adp,
			int lastDirection, Map mapArray) {

		this.context = context;
		this.startPoint = startPoint;
		this.adp = adp;
		this.lastDirection = lastDirection;
		this.storage = mapArray;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		if (this.startPoint != null) {
			moveOnMap();
		}
	}

	/**
	 * 
	 */
	private void moveOnMap() {

		int exitRow = (this.storage.getStorageMap()[0].length - 1);
		int dir = this.lastDirection;
		boolean hasOptions = true;
		boolean isGone = false;
		String key = "";

		// We need to use temp variable.
		Point currentPoint = new Point(this.startPoint);

		// Print some debug info.
		System.out.println(); // Just separate from the last info
		DebugInfo.p("Curiousity" + Thread.currentThread().getId() + ": ");
		DebugInfo.p((this.startPoint.x + 1) + "X" + (this.startPoint.y + 1)
				+ ",");

		// Move through the map till there is a way to make step forward or
		// found the exit.
		while (hasOptions && !isGone) {

			// Make step forward and get the new possible point to move on.
			Point newPoint = this.makeStep(currentPoint, dir); // TODO NPE check

			if (newPoint == null) {

				hasOptions = false;

			} else {

				if (newPoint.x == (this.startPoint.x - 1)
						&& newPoint.y == this.startPoint.y) {
					dir = Map.GO_LEFT;
				} else if (newPoint.x == (this.startPoint.x + 1)
						&& newPoint.y == this.startPoint.y) {
					dir = Map.GO_RIGHT;
				} else if (newPoint.y == (this.startPoint.y - 1)
						&& newPoint.x == this.startPoint.x) {
					dir = Map.GO_UP;
				} else if (newPoint.y == (this.startPoint.y + 1)
						&& newPoint.x == this.startPoint.x) {
					dir = Map.GO_DOWN;
				}

				if (newPoint.y == exitRow) {
					isGone = true;
				}

				this.adp.add(newPoint);

				currentPoint = new Point(newPoint);

				// Print some debug info.
				DebugInfo.p((currentPoint.x + 1) + "X" + (currentPoint.y + 1)
						+ ",");
			}
		}

		// Create list with 2 elements: String whether it's a successful or
		// unsuccessful path to the exit, and List of all points of this path.
		if (isGone) {
			key = "successful";
		} else {
			key = "unsuccessful";
		}

		if (this.context != null) {
			if (this.context.allPosiblePathsList != null) {

				// In case there's no List in the map under this key.
				this.context.allPosiblePathsList.putIfAbsent(key,
						new ArrayList<List<Point>>());

				// Get the list with paths under this key and add current
				// path.
				List<List<Point>> l = this.context.allPosiblePathsList.get(key);
				l.add((List<Point>) this.adp);

				// Put the list with paths back to the map.
				this.context.allPosiblePathsList.put(key, l);
			}
		} else {
			// Print some debug info.
			DebugInfo.pln(TAG, "Context is null.");
		}
	}

	/**
	 * @param Point
	 *            element
	 * @param int lastDirection
	 * @return Point
	 */
	private Point makeStep(Point element, int lastDirection) {

		Point currentPoint = null;
		Point newPoint = null;
		boolean hasWay = false;
		boolean hasAnotherOption = false;

		int[] posibleDirArray = new int[] { Map.GO_UP, Map.GO_RIGHT,
				Map.GO_DOWN, Map.GO_LEFT };

		// Check if can make a step in the all possible directions.
		for (int i = 0; i < posibleDirArray.length; i++) {

			boolean skippedTurn = false;
			int newDir = posibleDirArray[i];

			switch (newDir) {
			// Check if there is a way to move LEFT.
			case Map.GO_LEFT:
				if (lastDirection != Map.GO_RIGHT) {
					// Get the next possible point in left of the current one.
					newPoint = new Point((int) (element.getX() - 1),
							(int) element.getY());
				} else {
					skippedTurn = true;
				}
				break;

			// Check if there is a way to move DOWN.
			case Map.GO_DOWN:
				if (lastDirection != Map.GO_UP) {
					// Get the next possible point under the current one.
					newPoint = new Point((int) element.getX(),
							(int) (element.getY() + 1));
				} else {
					skippedTurn = true;
				}
				break;

			// Check if there is a way to move RIGHT.
			case Map.GO_RIGHT:
				if (lastDirection != Map.GO_LEFT) {
					// Get the next possible point in right of the current one.
					newPoint = new Point((int) (element.getX() + 1),
							(int) element.getY());
				} else {
					skippedTurn = true;
				}
				break;

			// Check if there is a way to move DOWN.
			case Map.GO_UP:
				if (lastDirection != Map.GO_DOWN) {
					// Get the next possible point above the current one.
					newPoint = new Point((int) element.getX(),
							(int) (element.getY() - 1));
				} else {
					skippedTurn = true;
				}
				break;

			default:
				// Print some debug info.
				DebugInfo.pln(TAG, "Error occured while looking around.");
				break;
			}

			if (lookBack(newPoint, this.adp)) {
				skippedTurn = true;
			}

			if (!skippedTurn) { // This skips useless checking for an option.

				hasWay = lookAround(newPoint);

				if (hasWay) {

					if (hasAnotherOption) {

						// If already has another possible way, then get the
						// current path and create another thread that will look
						// for the right way.
						createCloning(this.adp, newPoint, newDir);

					} else {
						// Trigger flag that there is a possible point to move.
						hasAnotherOption = true;
					}

					currentPoint = newPoint;
				}
			}
		}

		// If there is ONLY one option, return the point as a result.
		// If no options, return the current point to notify for the end of the
		// current path.
		return currentPoint;
	}

	/**
	 * @param Point
	 *            newPoint
	 * @param List
	 *            <Point> adp
	 * @return boolean
	 */
	private boolean lookBack(Point newPoint, List<Point> adp) {

		for (Iterator<Point> it = adp.iterator(); it.hasNext();) {

			Point point = (Point) it.next();

			if (newPoint != null && point != null) {
				if (newPoint.equals(point)) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * @param Point
	 *            point
	 * @return boolean
	 */
	private boolean lookAround(Point point) {

		char[][] map = this.storage.getStorageMap();

		boolean hasFreeWay = engageCuriousity(map, point);

		return hasFreeWay;
	}

	/**
	 * @param char[][] map
	 * @param Point
	 *            point
	 * @return boolean
	 */
	private boolean engageCuriousity(char[][] map, Point point) {

		char fieldContent = Map.WALL;

		try {

			fieldContent = map[point.x][point.y];

		} catch (IndexOutOfBoundsException ioobe) {
			// DO NOTHING.
		} catch (Exception e) {
			e.printStackTrace();
		}

		// If so, no need to check it
		if (fieldContent == Map.EMPTY_SPACE) { // If empty space, then go.

			// Trigger flag that there is a possible point to move on.
			return true;
		}

		return false;
	}

	/**
	 * @param List
	 *            <Point> adp
	 * @param Point
	 *            p
	 * @param int dir
	 */
	private void createCloning(List<Point> adp, Point p, int dir) {

		try {

			List<Point> l = new LinkedList<Point>(adp);

			// Send command to the parent PathFinder object that you want it to
			// clone this curiosity with new parameters.
			// new Thread(new Runnable() {
			// @Override
			// public void run() {
			context.travelThroughMap(p, l, dir); // TODO Check the instance
			// }
			// }).start();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return TAG;
	}

	/**
	 * @return Map storage
	 */
	protected Map getStorage() {
		return this.storage;
	}

	/**
	 * @param Map
	 *            storage - the storage to set
	 */
	protected void setStorage(Map storage) {
		this.storage = storage;
	}
}
