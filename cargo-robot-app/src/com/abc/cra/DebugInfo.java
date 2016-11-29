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
class DebugInfo {

	/**
	 * @param String message
	 */
	static void p(String message) {
		System.out.print(message);
	}
	
	/**
	 * @param String context
	 * @param String message
	 */
	static void pln(String context, String message) {
		System.out.println(context + ": " + message);
	}
}
