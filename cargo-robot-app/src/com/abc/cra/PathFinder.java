/**
 * 
 */
package com.abc.cra;

import java.awt.Dimension;
import java.awt.Point;
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
	private List<Point[]> allPosiblePathslist;

	public PathFinder() {
		this.storage = new Map();
		this.xDimList = new LinkedList<Integer>();
		this.allPosiblePathslist = new LinkedList<Point[]>();
	}

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
			return findShortestPath(entrance);
		}

		return null;
	}

	private Integer findEntrance(char[][] mapArray) {
		for (int i = 0; i < mapArray[0].length; i++) {
			if (mapArray[i][0] == Map.EMPTY_SPACE) {
				Integer entranceElement = new Integer(i + 1);

				System.out.println("Entrance is element: " + entranceElement);

				return entranceElement;
			}
		}

		// In case of no entrance, return null
		return null;
	}

	private char[] findShortestPath(Integer entrance) {
		Point entrancePoint = new Point(entrance.intValue(), 0);

		travelThroughMap(entrancePoint, new LinkedList<Point>(), Map.GO_DOWN);

		return null;
	}

	private void travelThroughMap(Point startPoint, List<Point> path, int lastDirection) {
		Runnable runn = new Runnable() {
			@Override
			public void run() {
				moveOnMap(startPoint, path, lastDirection);
			}
		};
		Thread t = new Thread(runn);
		t.start();
	}

	protected void moveOnMap(Point point, List<Point> path, int lastDirection) {
		int exitRow = this.storage.getStorageMap().length;
		boolean hasOptions = true;
		boolean isGone = false;
		List<Point> alreadyDrovePath = path;
		Point startPoint = point;

		while (hasOptions || !isGone) {
			Point p = makeStep(startPoint, alreadyDrovePath, lastDirection);
			if (p.equals(startPoint)) {
				hasOptions = false;
			}
			if (p.y == exitRow) {
				isGone = true;
			}
			startPoint = p;
		}
	}

	private Point makeStep(Point element, List<Point> adp, int lastDirection) {
		boolean hasOption = false;
		char[][] map = this.storage.getStorageMap();
		Point newPoint = new Point(element.x, element.y);

		if (lastDirection != Map.GO_LEFT
				&& map[newPoint.x - 1][element.y] == Map.EMPTY_SPACE) {
			hasOption = true;
			Point p = new Point((newPoint.x - 1), element.y);
			newPoint = p;
		}
		if (map[newPoint.x][element.y + 1] == Map.EMPTY_SPACE) {
			Point p = new Point(newPoint.x, (element.y + 1));
			
			if (hasOption) {
				List<Point> l = new LinkedList<Point>();
				for (Iterator<Point> it = adp.iterator(); it.hasNext();) {
					Point point = (Point) it.next();
					l.add(point);
				}
				
				travelThroughMap(p, l, Map.GO_DOWN);
			} else {
				hasOption = true;
				newPoint = p;
			}
		}
		if (lastDirection != Map.GO_RIGHT
				&& map[newPoint.x + 1][element.y] == Map.EMPTY_SPACE) {
			Point p = new Point((newPoint.x + 1), (element.y));
			
			if (hasOption) {
				List<Point> l = new LinkedList<Point>();
				for (Iterator<Point> it = adp.iterator(); it.hasNext();) {
					Point point = (Point) it.next();
					l.add(point);
				}
				
				travelThroughMap(p, l, Map.GO_RIGHT);
			} else {
				hasOption = true;
				newPoint = p;
			}

		}

		return newPoint;
	}

	private char[][] convertStringToArray(char[] mapArray) {

		int xDim = findXdim(mapArray);
		int yDim = findYdim(mapArray, xDim);

		Dimension arrayDim = new Dimension(xDim, yDim);
		System.out.println(arrayDim.toString());

		return fillMapArray(arrayDim);
	}

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
			Integer intgr = (Integer) it.next();
			int tmpVar = intgr.intValue();
			result = Math.round((result + tmpVar) / 2);
		}

		this.rawMap = new char[tmpI];
		for (int i = 0; i < this.rawMap.length; i++) {
			this.rawMap[i] = newMapArray[i];
		}

		return result;
	}

	private int findYdim(char[] mapArray, int xDim) {
		int mapLenth = mapArray.length;
		int yDim = Math.floorDiv(mapLenth, xDim);

		return yDim;
	}
}
