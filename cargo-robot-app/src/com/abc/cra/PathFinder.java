/**
 * 
 */
package com.abc.cra;

import java.awt.Point;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Nikola Georgiev
 * 
 * @email nikigeorgiev2000@gmail.com
 *
 */
public class PathFinder {

	private final String TAG = "PathFinder";

	protected java.util.Map<String, List<List<Point>>> allPosiblePathsMap;

	private Map warehouse;

	/**
	 * 
	 */
	public PathFinder() {
		this.allPosiblePathsMap = new HashMap<String, List<List<Point>>>();
	}

	/**
	 * @param char[] map
	 * @return List<List<Point>>
	 */
	public List<List<Point>> findOptimalPath(char[] map) {

		this.warehouse = new Map(map);
		
		char[][] mapArray = this.warehouse.getStorageMap();

		Integer entrance = null;
		if (mapArray != null) {
			entrance = findEntrance(mapArray);
		} else {
			DebugInfo.pln(TAG, "No Entrance.");
			return null;
		}

		if (entrance != null) {
			DebugInfo.pln(TAG, "Checking for available Exit.");

			return findShortestPath(entrance, mapArray);
		}

		return null;
	}

	/**
	 * @param char[] mapArray
	 * @return Integer
	 */
	private Integer findEntrance(char[][] mapArray) {
		for (int i = 0; i < mapArray[0].length; i++) {
			if (mapArray[i][0] == Map.EMPTY_SPACE) {
				Integer entranceElement = new Integer(i);

				String element = (entranceElement + 1) + "X1";
				DebugInfo.pln(TAG, "Entry element is " + element);

				return entranceElement;
			}
		}

		// In case of no entrance, return null
		return null;
	}

	/**
	 * @param Integer
	 *            entrance
	 * @param char[][] mapArray
	 * @return List<List<Point>>
	 */
	@SuppressWarnings("unused")
	private List<List<Point>> findShortestPath(Integer entrance,
			char[][] mapArray) {
		Point entrancePoint = new Point(entrance.intValue(), 0);
		boolean hasExit = false;

		// Print some debug info.
		DebugInfo.pln(TAG, "Engage Curiosity.");
		System.out.println();

		// Get entry point in the map, get direction, and GO traveling.
		travelThroughMap(entrancePoint, new LinkedList<Point>(), Map.GO_DOWN);

		System.out.println(); // Just to separate the lines

		// Check the set for a successful scenario.
		if (this.allPosiblePathsMap != null) {
			if (!this.allPosiblePathsMap.isEmpty()) {

				hasExit = this.allPosiblePathsMap.containsKey("successful"
						.toLowerCase());
			} else {
				DebugInfo.pln(TAG, "AllPossiblePathsList is empty.");
			}
		}

		if (hasExit) {

			// Print some debug info.
			DebugInfo.pln(TAG, "Curiousity reported - Exit Found.");

			if (this.allPosiblePathsMap != null) {
				List<List<Point>> list = this.allPosiblePathsMap
						.get("successful");
				if (list.isEmpty()) {
					DebugInfo.pln(TAG, "AllSuccessfulPathsList is empty.");
				}

				// Sort the list to get the shortest path.
				Collections.sort(list, new Comparator<List<Point>>() {
					public int compare(List<Point> list1, List<Point> list2) {
						return Integer.valueOf(list1.size()).compareTo(
								Integer.valueOf(list2.size()));
					}
				});

				// For sure will be the shortest.
				if (list != null) {

					// Print some debug info.
					DebugInfo.pln(TAG, "Successful paths - " + list.size());
					System.out.println();

					List<Point> shortestPath = list.get(0);
					char[][] resultMap = Map.mergeAdpWithMap(shortestPath, mapArray);

					// Print some debug info.
					Map.printArrayMap(resultMap);

					// Return the list with successful paths.
					return list;
				} else {
					// Print some debug info.
					DebugInfo.pln(TAG, "Successful list is null.");
				}
			} else {
				// Print some debug info.
				DebugInfo.pln(TAG, "AllPossiblePathsList is null.");
			}
		} else {
			// Print some debug info.
			DebugInfo.pln(TAG, "Curiosity reported - There is no exit.");
		}

		// If no successful scenario, return the whole list of possible ways.
		List<List<Point>> allUnsuccessfulPathsList = this.allPosiblePathsMap
				.get("unsuccessful");

		return allUnsuccessfulPathsList;
	}

	/**
	 * @param Point
	 *            startPoint
	 * @param List
	 *            <Point> path
	 * @param Int
	 *            lastDirection
	 */
	protected void travelThroughMap(Point startPoint, List<Point> path,
			String lastDirection) {

		try {

			// Create thread that will try to find a path through the labyrinth.
			Thread pf = new Thread(new Curiosity(this, startPoint, path,
					lastDirection, this.warehouse))
			;
			pf.start();
			
			// Print some debug info.
			DebugInfo.p("In direction " + lastDirection
					+ " - Engaged Curiosity" + pf.getId());
			
			// We have to wait till the new thread has done.
			pf.join(); 
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return char[][]
	 */
	public char[][] getWarehouseMap() {
		return this.warehouse.getStorageMap();
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
