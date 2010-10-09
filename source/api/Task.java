package api;


/**
 * Models a task that can be executed on a remote machine.
 * 
 * @author Manasa Chandrasekhar
 * @author Kowshik Prakasam
 */
public interface Task<T> {
	/**
	 * Defines the computation carried out on a remote machine
	 * @return Result of the computation
	 */
	T execute();

}
