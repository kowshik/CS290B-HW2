package api;

/**
 * Returns the result of the computation of a remote task
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
}
