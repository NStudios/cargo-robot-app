/**
 * 
 */
package com.abc.cra;

/**
 * @author Nikola Georgiev
 * 
 * @email nikigeorgiev2000@gmail.com
 *
 */
class Map {

	public static final char WALL = '#';
	public static final char EMPTY_SPACE = '.';
	public static final char PATH = '+';

	public static final int GO_LEFT = -1;
	public static final int GO_DOWN = 0;
	public static final int GO_RIGHT = 1;
	public static final int GO_UP = 2;

	protected char[][] storageMap;

	/**
	 * @param char[][] mapArray
	 */
	Map(char[][] mapArray) {
		this.storageMap = mapArray;
	}

	/**
	 * @param char element
	 * @return EMapElement
	 */
	static EMapElement checkElement(char element) {

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
	 * @return char[][] storageMap
	 */
	protected char[][] getStorageMap() {
		return this.storageMap;
	}

	/**
	 * @param char[][] storageMap the storageMap to set
	 */
	protected void setStorageMap(char[][] storageMap) {
		this.storageMap = storageMap;
	}
}
