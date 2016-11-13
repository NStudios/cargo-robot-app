/**
 * 
 */
package com.abc.cra;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
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

	private char[] rawMap;
	private Map storage;
	private List<Integer> xDimList;
	private java.util.Map<String, List<List<Point>>> allPosiblePaths;

	public PathFinder() {
		this.storage = new Map();
		this.xDimList = new LinkedList<Integer>();
		this.allPosiblePaths = new HashMap<String, List<List<Point>>>();
	}

	/**
	 * @param map
	 * @return char[]
	 */
	public char[] findOptimalPath(char[] map) {
		char[][] mapArray = convertStringToArray(map);

		if (this.storage != null) {
			this.storage.setStorageMap(mapArray);
		}

		Integer entrance = null;
		if (mapArray != null) {
			entrance = findEntrance(mapArray);
		} else {
			return null;
		}

		if (entrance != null) {
			return findShortestPath(entrance, mapArray);
		}

		return null;
	}

	/**
	 * @param mapArray
	 * @return Integer
	 */
	private Integer findEntrance(char[][] mapArray) {
		for (int i = 0; i < mapArray[0].length; i++) {
			if (mapArray[i][0] == Map.EMPTY_SPACE) {
				Integer entranceElement = new Integer(i + 1);

				System.out.println("Entry element is: " + entranceElement + "X0");

				return entranceElement;
			}
		}

		// In case of no entrance, return null
		return null;
	}

	/**
	 * @param entrance
	 * @param mapArray
	 * @return char[]
	 */
	private char[] findShortestPath(Integer entrance, char[][] mapArray) {
		Point entrancePoint = new Point((entrance.intValue() - 1), 0);
		boolean hasExit = false;

		// Get entry point in the map, get direction, and GO traveling.
		travelThroughMap(entrancePoint, new LinkedList<Point>(), Map.GO_DOWN);

		// Check the set for a successful scenario.
		if (this.allPosiblePaths != null) {
			if (!this.allPosiblePaths.isEmpty()) {

				hasExit = this.allPosiblePaths.containsKey("successfull"
						.toLowerCase());
			}
		}

		if (hasExit) {
			if (this.allPosiblePaths != null) {
				if (!this.allPosiblePaths.isEmpty()) {
					List<List<Point>> list = this.allPosiblePaths
							.get("successful");

					// Sort the list to get the shortest path.
					Collections.sort(list, new Comparator<List>() {
						public int compare(List list1, List list2) {
							return Integer.valueOf(list1.size()).compareTo(
									Integer.valueOf(list2.size()));
						}
					});

					// For sure will be the shortest.
					List<Point> shortestPath = list.get(0);

					char[][] resultMap = mergePathWithMap(shortestPath,
							mapArray);

					System.out.println("Exit");

					return convertArrayToString(resultMap);
				}
			}
		}

		return null; // TODO
	}

	private char[] convertArrayToString(char[][] resultMap) {
		// TODO Auto-generated method stub
		return null;
	}

	private char[][] mergePathWithMap(List<Point> shortestPath,
			char[][] mapArray) {
		// TODO
		return null;
	}

	/**
	 * @param startPoint
	 * @param path
	 * @param lastDirection
	 */
	private void travelThroughMap(Point startPoint, List<Point> path,
			int lastDirection) {
		try {
			// Create thread that will try to find a path through the labyrinth.
			Thread pf = new Thread(new Runnable() {
				@Override
				public void run() {
					moveOnMap(startPoint, path, lastDirection);
				}
			});
			pf.start();
			pf.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param point
	 * @param path
	 * @param lastDirection
	 */
	protected void moveOnMap(Point point, List<Point> path, int lastDirection) {
		int exitRow = this.storage.getStorageMap().length;
		boolean hasOptions = true;
		boolean isGone = false;

		String key;
		List<Point> alreadyDrovePath = path;
		Point startPoint = new Point(point); // We need to use a temporal variable

		System.out.print("Curiousity path: ");
		
		// Move through the map till there is a way to make step forward or
		// found the exit
		while (hasOptions && !isGone) {
			// Make step forward and get the new possible point to move on.
			Point newPoint = makeStep(startPoint, alreadyDrovePath,
					lastDirection);

			if (newPoint.equals(startPoint)) {
				hasOptions = false;
			}
			if (newPoint.y == exitRow) {
				isGone = true;
			}
			startPoint = newPoint;
			System.out.print((newPoint.x + 1) + "X" + (newPoint.y + 1) + ",");
		}
		System.out.println("");
		
		// Create list with 2 elements: String whether it's a successful or
		// unsuccessful path to the exit; and List of all points of this path.
		if (isGone) {
			key = "successful";
		} else {
			key = "unsuccessful";
		}

		if (this.allPosiblePaths != null) {
			if (this.allPosiblePaths.isEmpty()) {
				// In case there's no List in the map under this key.
				this.allPosiblePaths.putIfAbsent(key,
						new ArrayList<List<Point>>());

				// Get the list with paths under this key and add current path.
				List<List<Point>> l = this.allPosiblePaths.get(key);
				l.add((List<Point>) alreadyDrovePath);

				// Put the list with paths back to the map
				this.allPosiblePaths.replace(key, l);
			}
		}
	}

	/**
	 * @param element
	 * @param adp
	 * @param lastDirection
	 * @return Point
	 */
	private Point makeStep(Point element, List<Point> adp, int lastDirection) {
		Point currentPoint = new Point(element.x, element.y);
		boolean hasAnotherOption = false;
		int[] possibleDirArray = new int[] { Map.GO_LEFT, Map.GO_DOWN,
				Map.GO_RIGHT, Map.GO_UP };

		// Check if can make a step in the all possible directions.
		for (int i = 0; i < possibleDirArray.length; i++) {
			Point newPoint = currentPoint; // Just to has a value;
			int newDir = possibleDirArray[i];

			switch (newDir) {
			// Check if there is a way to move LEFT
			case Map.GO_LEFT:
				// Get the next possible point in left of the current one.
				newPoint = new Point((currentPoint.x - 1), element.y);
				break;

			// Check if there is a way to move DOWN.
			case Map.GO_DOWN:
				// Get the next possible point under the current one.
				newPoint = new Point(currentPoint.x, (element.y + 1));
				break;

			// Check if there is a way to move RIGHT
			case Map.GO_RIGHT:
				// Get the next possible point in right of the current one.
				newPoint = new Point((currentPoint.x + 1), element.y);
				break;

			// Check if there is a way to move DOWN.
			case Map.GO_UP:
				// Get the next possible point above the current one.
				newPoint = new Point(currentPoint.x, (element.y - 1));
				break;

			default:
				break;
			}

			currentPoint = lookAround(adp, lastDirection, currentPoint,
					hasAnotherOption, newDir, newPoint);
		}

		// If there is ONLY one option, return the point as a result.
		// If no options, return the current point to notify for the end of the
		// current path.
		return currentPoint;
	}

	/**
	 * @param adp
	 * @param lastDirection
	 * @param newPoint
	 * @param hasAnotherOption
	 * @param dir
	 * @param point
	 * @return
	 */
	private Point lookAround(List<Point> adp, int lastDirection,
			Point newPoint, boolean hasAnotherOption, int dir, Point point) {
		char[][] map = this.storage.getStorageMap();

		boolean hasAnotherWay = engageCuriousity(lastDirection, map, dir, point);

		if (hasAnotherWay) {
			if (hasAnotherOption) {
				// If already has another possible way, then get the current
				// path and create another thread that will look for the right
				// way.
				createCloning(adp, point, dir);
			}

			newPoint = point;
			// Trigger flag that there is a possible point to move.
			hasAnotherOption = true;
		}
		return newPoint;
	}

	/**
	 * @param lastDirection
	 * @param map
	 * @param currentDirection
	 * @param point
	 */
	private boolean engageCuriousity(int lastDirection, char[][] map,
			int currentDirection, Point point) {
		char fieldContent = Map.WALL;
		try {
			fieldContent = map[point.x][point.y];
		} catch (IndexOutOfBoundsException ioobe) {
			// DO NOTHING.
		} catch (Exception e) {
			e.printStackTrace();
		}

		//  // If so, no need to check it
		if (lastDirection != currentDirection && fieldContent == Map.EMPTY_SPACE) { // If empty space, then go

			// Trigger flag that there is a possible point to move.
			return true;
		}

		return false;
	}

	/**
	 * @param adp
	 * @param p
	 */
	private void createCloning(List<Point> adp, Point p, int dir) {
		List<Point> l = new LinkedList<Point>();
		for (Iterator<Point> it = adp.iterator(); it.hasNext();) {
			Point point = it.next();
			l.add(point);
		}

		travelThroughMap(p, l, dir);
	}

	/**
	 * @param mapArray
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
	 * @param dim
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
	 * @param mapArray
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
}
