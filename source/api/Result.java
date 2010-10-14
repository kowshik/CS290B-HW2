package api;

/**
 * Describes the result obtained upon computation of a remote {@link api.Task Task}
 * 
 * @author Manasa Chandrasekhar
 * @author Kowshik Prakasam
 */

public interface Result<T> {
	/**
	 * @return Returns the start time of the underlying computation
	 */
	long getStartTime();
	
	/**
	 * @return Returns the end time of the underlying computation
	 */
	long getEndTime();
	
	/**
	 * @return Returns the value computed by the underlying computation
	 */
	T getValue(); 	
	
	/**
	 * 
	 * @return Returns a String object that uniquely identifies the task ({@link api.Task Task}) (or) computation which produced this result
	 */
	String getTaskIdentifier();
}
