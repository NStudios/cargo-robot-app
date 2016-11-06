/**
 * 
 */
package com.abc.cra;

/**
 * @author ANDROID
 *
 */
public class PathFinderTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PathFinder pathFinder = new PathFinder();
		char[] map = new char[] { 
				'#', '#', '#', '.', '#', '#', '#', '#', '\n',
				'#', '.', '.', '.', '#', '#', '#', '#', '\n', 
				'#', '.', '#', '#', '.', '.', '.', '#', '\n', 
				'#', '.', '#', '#', '.', '#', '.', '#', '\n', 
				'#', '.', '.', '.', '.', '#', '.', '#', '\n',
				'#', '#', '#', '#', '#', '#', '.', '#', '\n' 
		};

		pathFinder.findOptimalPath(map);

	}
}
