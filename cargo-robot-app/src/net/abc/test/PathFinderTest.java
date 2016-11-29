package net.abc.test;
import java.awt.Point;
import java.util.Iterator;
import java.util.List;

import com.abc.cra.Map;
import com.abc.cra.PathFinder;

/**
 * @author Nikola Georgiev
 * 
 * @email nikigeorgiev2000@gmail.com
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
				'#', '.', '.', '.', '.', '.', '.', '#', '\n', 
				'#', '.', '#', '#', '#', '#', '.', '#', '\n', 
				'#', '.', '#', '#', '#', '#', '.', '#', '\n', 
				'#', '.', '.', '.', '.', '.', '.', '#', '\n',
				'#', '#', '#', '#', '#', '#', '.', '#', '\n' 
		};

		List<List<Point>> result = pathFinder.findOptimalPath(map);
		
		if (result == null) {
			System.out.println("Error occured. Check the input map.");
		} else {
			System.out.println("All Successful Paths:");
			for (Iterator<List<Point>> it = result.iterator(); it.hasNext();) {
				List<Point> list = (List<Point>) it.next();
				Map.printArrayMap(Map.mergeAdpWithMap(list, pathFinder.getWarehouseMap()));;
			}
		}
	}
}
