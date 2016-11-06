/**
 * 
 */
package com.abc.cra;

import java.awt.Dimension;
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
	private List<Integer> xDimList;

	public PathFinder() {
		this.xDimList = new LinkedList<Integer>();
	}

	public char[] findOptimalPath(char[] map) {
		char[][] mapArray = convertStringToArray(map);
		
		return null;
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
