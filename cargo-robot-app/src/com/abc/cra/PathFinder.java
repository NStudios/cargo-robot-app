/**
 * 
 */
package com.abc.cra;

import java.awt.Dimension;
import java.awt.Point;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
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

	protected java.util.Map<String, List<List<Point>>> allPosiblePathsList;

	private char[] rawMap;
	private List<Integer> xDimList;
	private Map storage;
	private Curiosity curiousity;

	/**
	 * 
	 */
	public PathFinder() {
		this.xDimList = new LinkedList<Integer>();
		this.allPosiblePathsList = new HashMap<String, List<List<Point>>>();
	}

	/**
	 * @param char[] map
	 * @return char[]
	 */
	public char[] findOptimalPath(char[] map) {
		char[][] mapArray = convertStringToArray(map);

		this.storage = new Map(mapArray);

		this.curiousity = new Curiosity(this, null, null, 0, this.storage);

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
	 * @return char[]
	 */
	@SuppressWarnings({ "rawtypes", "unused" })
	private char[] findShortestPath(Integer entrance, char[][] mapArray) {
		Point entrancePoint = new Point(entrance.intValue(), 0);
		boolean hasExit = false;

		// Get entry point in the map, get direction, and GO traveling.
		if (this.curiousity != null) {
			DebugInfo.pln(TAG, "Engage Curiosity.");
			travelThroughMap(entrancePoint, new LinkedList<Point>(),
					Map.GO_DOWN);
			System.out.println(); // Just to separate the lines
		}

		// Check the set for a successful scenario.
		if (this.allPosiblePathsList != null) {
			if (!this.allPosiblePathsList.isEmpty()) {

				hasExit = this.allPosiblePathsList.containsKey("successful"
						.toLowerCase());
			} else {
				DebugInfo.pln(TAG, "AllPossiblePathsList is empty.");
			}
		}

		if (hasExit) {
			DebugInfo.pln(TAG, "Curiousity reported - Exit Found.");

			if (this.allPosiblePathsList != null) {
				List<List<Point>> list = this.allPosiblePathsList
						.get("successful");
				if (list.isEmpty()) {
					DebugInfo.pln(TAG, "AllSuccessfulPathsList is empty.");
				}

				// Sort the list to get the shortest path.
				Collections.sort(list, new Comparator<List>() {
					public int compare(List list1, List list2) {
						return Integer.valueOf(list1.size()).compareTo(
								Integer.valueOf(list2.size()));
					}
				});
				// Collections.sort(list, new Comparator<List>() {
				// public int compare(List list1, List list2) {
				// return list2.size() - list1.size();
				// }
				// });

				// For sure will be the shortest.
				if (list != null) {
					List<Point> shortestPath = list.get(0);
					char[][] resultMap = mergeAdpWithMap(shortestPath, mapArray);

					return convertArrayToString(resultMap);
				} else {
					DebugInfo.pln(TAG, "Successful list is null.");
				}
			} else {
				DebugInfo.pln(TAG, "AllPossiblePathsList is null.");
			}
		} else {
			DebugInfo.pln(TAG, "Curiosity reported - There is no exit.");
		}

		return null;
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
			int lastDirection) {
		try {
			// Create thread that will try to find a path through the labyrinth.
			Thread pf = new Thread(new Curiosity(this, startPoint, path,
					lastDirection, this.storage));
			pf.start();
			pf.join(); // We have to wait till the new thread has done.
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param char[][] resultMap
	 * @return char[][]
	 */
	private char[] convertArrayToString(char[][] resultMap) {
		if (resultMap != null) {
			int rols = resultMap[0].length;
			int colms = resultMap.length + 1; // 1 extra column for the Newline
												// char
			int length = rols * colms;
			char[] rawArray = new char[length];
			for (int i = 0; i < rols; i++) {
				for (int j = 0; j < colms; j++) {

					int element = (i * j) + j;

					if (j < (colms - 1)) {
						char content = resultMap[j][i];
						rawArray[element] = content;

						System.out.print(content);
					} else {
						rawArray[element] = '\n';
					}
				}
				System.out.println();
			}
			return rawArray;
		}

		return null;
	}

	/**
	 * @param List
	 *            <Point> shortestPath
	 * @param char[][] mapArray
	 * @return char[][]
	 */
	private char[][] mergeAdpWithMap(List<Point> shortestPath, char[][] mapArray) {

		if (!shortestPath.isEmpty()) {

			try { // TRY block in case the Interator return some Exception.

				for (Iterator<Point> it = shortestPath.iterator(); it.hasNext();) {
					Point point = (Point) it.next();
					int xPosition = (int) point.getX();
					int yPosition = (int) point.getY();

					mapArray[xPosition][yPosition] = Map.PATH;
				}

				return mapArray;

			} catch (Exception e) {
				// In case of Exception, print the stack and return null.
				e.printStackTrace();

				return null;
			}
		} else {
			DebugInfo.pln(TAG, "shortestPathList is empty.");
		}

		// No path, no need to merge anything. Just return null.
		return null;
	}

	/**
	 * @param char[][] mapArray
	 * @return char[][]
	 */
	private char[][] convertStringToArray(char[] mapArray) {

		int xDim = findXdim(mapArray);
		int yDim = findYdim(mapArray, xDim);

		Dimension arrayDim = new Dimension(xDim, yDim);
		System.out.println(arrayDim.toString());

		return fillMapArray(arrayDim);
	}

	/**
	 * @param Dimension
	 *            dim
	 * @return char[][]
	 */
	private char[][] fillMapArray(Dimension dim) {

		int xLenght = dim.width;
		int yLenght = dim.height;

		char[][] newMapArray = new char[xLenght][yLenght];

		for (int j = 0; j < yLenght; j++) {
			for (int i = 0; i < xLenght; i++) {
				int n = (j * xLenght) + i;
				System.out.print(this.rawMap[n]);
				newMapArray[i][j] = this.rawMap[n];
			}
			System.out.println("");
		}

		return newMapArray;
	}

	/**
	 * @param char[][] mapArray
	 * @return int
	 */
	private int findXdim(char[] mapArray) {
		
		int result = 0;
		int xDimTmp = 0;
		int tmpI = 0;
		char[] newMapArray = new char[mapArray.length];

		// Find actual x dimension of this 2D array
		for (int i = 0; i < mapArray.length; i++) {
			char c = mapArray[i];

			if (c == '\n') { // byte 0xA == the new line characters.
				this.xDimList.add(new Integer(xDimTmp));
				xDimTmp = 0;
			} else {
				newMapArray[tmpI] = mapArray[i];
				tmpI++;
			}

			xDimTmp++;
		}
		result = xDimTmp;

		for (Iterator<Integer> it = this.xDimList.iterator(); it.hasNext();) {
			Integer intgr = it.next();
			int tmpVar = intgr.intValue();
			result = Math.round((result + tmpVar) / 2);
		}

		this.rawMap = new char[tmpI];
		for (int i = 0; i < this.rawMap.length; i++) {
			this.rawMap[i] = newMapArray[i];
		}

		return result;
	}

	/**
	 * @param mapArray
	 * @param xDim
	 * @return int
	 */
	private int findYdim(char[] mapArray, int xDim) {
		
		int mapLenth = mapArray.length;
		int yDim = Math.floorDiv(mapLenth, xDim);

		return yDim;
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
