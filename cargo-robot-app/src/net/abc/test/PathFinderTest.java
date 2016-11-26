package net.abc.test;
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

		char[] result = pathFinder.findOptimalPath(map);
		
		if (result != null) {
			System.out.println("Successful");
		}
	}
}
