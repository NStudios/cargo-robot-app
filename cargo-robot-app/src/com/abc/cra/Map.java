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
/**
 * @author ANDROID
 *
 */
public class Map {

	public static final char WALL = '#';
	public static final char EMPTY_SPACE = '.';
	public static final char PATH = '+';

	public static final String GO_LEFT = "left";
	public static final String GO_DOWN = "down";
	public static final String GO_RIGHT = "right";
	public static final String GO_UP = "up";

	private static final String TAG = "Map";

	private char[] rawMap;
	private char[][] warehouseMap;

	/**
	 * @param char[] map
	 */
	Map(char[] map) {

		this.rawMap = map;
		this.warehouseMap = convertStringToMaparray();

	}

	/**
	 * @param List
	 *            <Point> shortestPath
	 * @param char[][] mapArray
	 * @return char[][]
	 */
	public static char[][] mergeAdpWithMap(List<Point> shortestPath,
			char[][] mapArray) {

		if (!shortestPath.isEmpty()) {

			try { // TRY block in case the Iterator return some Exception.

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
	 * @param char[][] resultMap
	 * @return char[][]
	 */
	public static void printArrayMap(char[][] resultMap) {
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
			System.out.println();
		}
	}

	/**
	 * @param char element
	 * @return EMapElement
	 */
	protected static EMapElement checkElement(char element) {
	
		switch (element) {
	
		case WALL:
			return EMapElement.WALL;
	
		case EMPTY_SPACE:
			return EMapElement.EMPTY_SPACE;
	
		default:
			break;
		}
	
		return null;
	}

	/**
	 * @return char[][]
	 * @throws NullPointerException
	 */
	private char[][] convertStringToMaparray() throws NullPointerException {

		int xDim = findXdim();
		int yDim = findYdim(xDim);

		Dimension arrayDim = new Dimension(xDim, yDim);

		// Print some debug info.
		DebugInfo.pln(TAG, "Warehouse map dimensions - Width=" + xDim
				+ ", Height=" + yDim);
		System.out.println();

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

				// Prints each element of the map.
				System.out.print(this.rawMap[n]);

				newMapArray[i][j] = this.rawMap[n];
			}
			System.out.println(""); // Just break the line.
		}
		System.out.println(); // Print newline to separate the info.

		return newMapArray;
	}

	/**
	 * @return int
	 * @throws NullPointerException
	 */
	private int findXdim() throws NullPointerException {

		int result = 0;
		int xDimTmp = 0;
		int tmpI = 0;
		
		char[] newMapArray = new char[this.rawMap.length];
		List<Integer> xDimList = new LinkedList<Integer>();

		// Find actual x dimension of this 2D array
		for (int i = 0; i < this.rawMap.length; i++) {
			char c = this.rawMap[i];

			if (c == '\n') { // byte 0xA == the new line characters.
				xDimList.add(new Integer(xDimTmp));
				xDimTmp = 0;
			} else {
				newMapArray[tmpI] = this.rawMap[i];
				tmpI++;
			}

			xDimTmp++;
		}
		result = xDimTmp;

		for (Iterator<Integer> it = xDimList.iterator(); it.hasNext();) {
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
	 * @param xDim
	 * @return int
	 * @throws NullPointerException
	 */
	private int findYdim(int xDim) throws NullPointerException {

		int mapLenth = this.rawMap.length;
		int yDim = Math.floorDiv(mapLenth, xDim);

		return yDim;
	}

	/**
	 * @return char[][] storageMap
	 */
	protected char[][] getStorageMap() {
		return this.warehouseMap;
	}

	/**
	 * @param char[][] storageMap the storageMap to set
	 */
	protected void setStorageMap(char[][] storageMap) {
		this.warehouseMap = storageMap;
	}
}
