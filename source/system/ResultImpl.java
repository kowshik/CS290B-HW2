package system;

import api.Result;

public class ResultImpl<T> implements Result<T> {

	private long startTime;
	private long endTime;
	private T result;

	public ResultImpl(long startTime, long endTime, T result, String taskIdentifier) {
		this.startTime=startTime;
		this.endTime=endTime;
		this.result=result;

	}

	@Override
	public long getStartTime() {
		return this.startTime;
	}

	@Override
	public long getEndTime() {
		return this.endTime;
	}

	@Override
	public T getValue() {
		return this.result;
	}

	@Override
	public String getTaskIdentifier() {
		return this.getTaskIdentifier();
	}

}
