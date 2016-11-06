/**
 * 
 */
package com.abc.cra;

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

	public String findOptimalPath(String map) {
		convertStringToArray(map);

		return null;
	}

	private void convertStringToArray(String map) {
		int stringLengh = map.length();
		char[] mapArray = new char[stringLengh];

		convertTo2D(mapArray);
	}

	private void convertTo2D(char[] mapArray) {
		int xDim = findXdim(mapArray);

	}

	private int findXdim(char[] mapArray) {
		int xDim = 0;
		double mapLenth = (double) mapArray.length;
		double sqrt = Math.sqrt(mapLenth);
		int theoriticXdim = (int) Math.round(sqrt);
		List<Integer> xDimList = new LinkedList<Integer>();

		// Find actual x dimension of this 2D array
		for (int i = 0; i < mapArray.length; i++) {
			char c = mapArray[i];

			if (c == 0xA) { // byte 0xA == the new line characters.
				xDimList.add(new Integer(xDim));
				xDim = 0;
			}
			xDim++;
		}

		int x = 0;
		for (Iterator<Integer> it = xDimList.iterator(); it.hasNext();) {
			Integer intgr = (Integer) it.next();
			int tmpVar = intgr.intValue();
			
		}

		return xDim;
	}
}
