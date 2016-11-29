/**
 * 
 */
package com.abc.cra;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

	private final String[] POSIBLE_DIR_ARRAY = new String[] { Map.GO_UP,
			Map.GO_RIGHT, Map.GO_DOWN, Map.GO_LEFT };

	private PathFinder context;
	private Map warehouse;
	private Point startPoint;
	private List<Point> adp;
	private String lastDirection;

	/**
	 * @param PathFinder
	 *            context
	 * @param Point
	 *            startPoint
	 * @param List
	 *            <Point> adp
	 * @param int lastDirection
	 * @param Map
	 *            map
	 */
	Curiosity(PathFinder context, Point startPoint, List<Point> adp,
			String lastDirection, Map map) {

		this.context = context;
		this.startPoint = startPoint;
		this.adp = adp;
		this.lastDirection = lastDirection;
		this.warehouse = map;
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

		int exitRow = (this.warehouse.getStorageMap()[0].length - 1);
		boolean hasOptions = true;
		boolean isGone = false;
		String key = "";
		String newDir = new String(this.lastDirection);

		// We need to use temp variable.
		Point currentPoint = new Point(this.startPoint);

		// Put the start point into the already drove path list.
		this.adp.add(currentPoint);

		// Print some debug info.
		System.out.println(); // Just separate from the last info
		DebugInfo.p(TAG + Thread.currentThread().getId() + ": ");
		DebugInfo.p((this.startPoint.x + 1) + "X" + (this.startPoint.y + 1)
				+ ",");

		// Move through the map till there is a way to make step forward or
		// found the exit.
		while (hasOptions && !isGone) {

			// Make step forward and get the new possible point to move on.
			Point newPoint = this.makeStep(currentPoint, this.lastDirection); // TODO

			if (newPoint == null) {

				hasOptions = false;

			} else {

				if (newPoint.x == (this.startPoint.x - 1)
						&& newPoint.y == this.startPoint.y) {

					newDir.equalsIgnoreCase(Map.GO_LEFT);

				}
				if (newPoint.x == (this.startPoint.x + 1)
						&& newPoint.y == this.startPoint.y) {

					newDir.equalsIgnoreCase(Map.GO_RIGHT);

				}
				if (newPoint.y == (this.startPoint.y - 1)
						&& newPoint.x == this.startPoint.x) {

					newDir.equalsIgnoreCase(Map.GO_UP);

				}
				if (newPoint.y == (this.startPoint.y + 1)
						&& newPoint.x == this.startPoint.x) {

					newDir.equalsIgnoreCase(Map.GO_DOWN);

				}

				if (!this.lastDirection.equalsIgnoreCase(newDir)) {
					this.lastDirection = new String(newDir);
					hasOptions = false;
				}

				if (newPoint.y == exitRow)
					isGone = true;

				this.adp.add(newPoint);

				currentPoint = new Point(newPoint);

				// Print some debug info.
				DebugInfo.p((currentPoint.x + 1) + "X" + (currentPoint.y + 1)
						+ ",");
			}

			try {
				Thread.sleep(1);
			} catch (InterruptedException ie) {
				ie.printStackTrace();
			}
		}

		// Create list with 2 elements: String whether it's a successful or
		// unsuccessful path to the exit, and List of all points of this path.
		if (isGone) {
			key = "successful";

			// Print some debug info.
			DebugInfo.pln(TAG, "Found Exit.");
		} else {
			key = "unsuccessful";

			// Print some debug info.
			DebugInfo.pln(TAG, "There's NO Exit.");
		}

		if (this.context != null) {
			if (this.context.allPosiblePathsMap != null) {

				// In case there's no List in the map under this key.
				this.context.allPosiblePathsMap.putIfAbsent(key,
						new ArrayList<List<Point>>());

				// Get the list with paths under this key and add current
				// path.
				List<List<Point>> l = this.context.allPosiblePathsMap.get(key);
				l.add((List<Point>) this.adp);

				// Put the list with paths back to the map.
				this.context.allPosiblePathsMap.put(key, l);
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
	private Point makeStep(Point element, String lastDir) {

		Point currentPoint = null;
		Point newPoint = null;
		String mainDir = null; // Just for debug info.
		boolean hasWay = false;
		int option = 0;

		List<String> pda = randomiseArray(POSIBLE_DIR_ARRAY);

		// Check if can make a step in the all possible directions.
		for (Iterator<String> it = pda.iterator(); it.hasNext();) {

			String testDir = (String) it.next();

			newPoint = getNewPoint(element, lastDir, testDir);

			// Check for already drove path. If not, go ahead.
			boolean isSkippedTurn = lookBack(newPoint, this.adp);

			if (!isSkippedTurn) {

				hasWay = lookAround(newPoint);

				if (hasWay) {

					if (0 == option) {

						// The first found way will be the main way for the
						// current Thread.
						currentPoint = newPoint;
						mainDir = new String(testDir);

					} else if (1 <= option) {

						// Print debug info.
						DebugInfo.p(" Has Another Option ");

						// If already has another possible way, then get the
						// current path and create another thread that will look
						// for the right way.
						createCloning(this.adp, newPoint, testDir);

						// Print some debug info.
						String prevElem = (element.x + 1) + "X"
								+ (element.y + 1);
						DebugInfo.p("Proceed to " + mainDir + " from "
								+ prevElem + " - ");
					}

					// Add flag that there is 1 more possible point to move.
					option++;
				}
			}
		}

		// If there is ONLY one option, return the point as a result.
		// If no options, return the current point to notify for the end of the
		// current path.
		return currentPoint;
	}

	private Point getNewPoint(Point element, String lastDir, String testDir) {
		Point newPoint = null;

		// Check if there is a way to move LEFT.
		if (testDir.equalsIgnoreCase(Map.GO_LEFT)) {
			if (!lastDir.equalsIgnoreCase(Map.GO_RIGHT)) {
				// Get the next possible point in left of the current one.
				return newPoint = new Point((element.x - 1), element.y);
			}
		}

		// Check if there is a way to move DOWN.
		if (testDir.equalsIgnoreCase(Map.GO_DOWN)) {
			if (!lastDir.equalsIgnoreCase(Map.GO_UP)) {
				// Get the next possible point under the current one.
				return newPoint = new Point(element.x, (element.y + 1));
			}
		}

		// Check if there is a way to move RIGHT.
		if (testDir.equalsIgnoreCase(Map.GO_RIGHT)) {
			if (!lastDir.equalsIgnoreCase(Map.GO_LEFT)) {
				// Get the next possible point in right of the current one.
				return newPoint = new Point((element.x + 1), element.y);
			}
		}

		// Check if there is a way to move DOWN.
		if (testDir.equalsIgnoreCase(Map.GO_UP)) {
			if (!lastDir.equalsIgnoreCase(Map.GO_DOWN)) {
				// Get the next possible point above the current one.
				return newPoint = new Point(element.x, (element.y - 1));
			}
		}

		return newPoint;
	}

	/**
	 * @param String
	 *            [] array
	 * @return List<String>
	 */
	private List<String> randomiseArray(String[] array) {

		List<String> list = null;

		if (array != null) {
			list = new ArrayList<String>(Arrays.asList(array));
			Collections.shuffle(list);
		}

		return list;
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

			try {
				if (newPoint.equals(point)) {
					return true;
				}
			} catch (NullPointerException npe) {
				return true;
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

		char[][] map = this.warehouse.getStorageMap();

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
	private void createCloning(List<Point> adp, Point p, String dir) {

		try {

			List<Point> l = new LinkedList<Point>(adp);

			// Send command to the parent PathFinder object that you want it to
			// clone this curiosity with new parameters.
			this.context.travelThroughMap(p, l, dir); // TODO Check the instance

			// Print some debug info.
			DebugInfo.p(TAG + Thread.currentThread().getId() + ": ");

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
}
