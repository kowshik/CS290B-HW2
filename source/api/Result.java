package api;

/**
 * Represents the result obtained after the computation of a remote {@link api.Task Task}
 * 
 * @author Manasa Chandrasekhar
 * @author Kowshik Prakasam
 */

public interface Result<T> {
	/**
	 * @return Returns the start time of the computation
	 */
	long getStartTime();
	
	/**
	 * @return Returns the end time of the computation
	 */
	long getEndTime();
	
	/**
	 * @return Returns computed value
	 */
	T getValue(); 	
	
	/**
	 * 
	 * @return Returns a String object that uniquely identifies the task which produced this result
	 */
	String getTaskIdentifier();
}
