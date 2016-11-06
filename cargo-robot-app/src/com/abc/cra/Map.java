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
public class Map {
	
	public static final char WALL = '#';
	public static final char EMPTY_SPACE = '.';
	public static final int GO_LEFT = -1;
	public static final int GO_DOWN = 0;
	public static final int GO_RIGHT = 1;
	
	char[][] storageMap;

	public static EMapElement checkElement(char element) {
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
	 * @return the storageMap
	 */
	public char[][] getStorageMap() {
		return this.storageMap;
	}

	/**
	 * @param storageMap the storageMap to set
	 */
	public void setStorageMap(char[][] storageMap) {
		this.storageMap = storageMap;
	}
}
