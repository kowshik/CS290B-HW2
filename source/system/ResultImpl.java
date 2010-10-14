package system;

import java.io.Serializable;

import api.Result;

/**
 * Implementation of the {@link api.Result Result} interface
 * 
 * @author Manasa Chandrasekhar
 * @author Kowshik Prakasam
 */
public class ResultImpl<T> implements Result<T>, Serializable {

	private static final long serialVersionUID = -7688137730920618986L;
	private long startTime;
	private long endTime;
	private T result;
	private String taskIdentifier;

	/**
	 * 
	 * @param startTime
	 *            Starting time of the related {@link api.Task Task}
	 * @param endTime
	 *            Ending time of the related {@link api.Task Task}
	 * @param result
	 *            Result value computed by any {@link api.Task Task}
	 * @param taskIdentifier
	 *            A string that uniquely identifies the {@link api.Task Task}
	 *            related to this result
	 */
	public ResultImpl(long startTime, long endTime, T result,
			String taskIdentifier) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.result = result;
		this.taskIdentifier = taskIdentifier;
	}

	/**
	 * @return Returns the start time of the task
	 * 
	 * 
	 */
	@Override
	public long getStartTime() {
		return this.startTime;
	}

	/**
	 * @return Returns the end time of the task
	 * 
	 * 
	 */
	@Override
	public long getEndTime() {
		return this.endTime;
	}

	/**
	 * @return Returns the value computed by the task
	 * 
	 * 
	 */
	@Override
	public T getValue() {
		return this.result;
	}
	/**
	 * @return Returns the unique identifier for the task
	 * 
	 * 
	 */
	@Override
	public String getTaskIdentifier() {
		return this.taskIdentifier;
	}

}
