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
}
