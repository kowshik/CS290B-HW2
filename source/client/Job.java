package client;

import java.rmi.RemoteException;

import api.Space;

/**
 * Interface that defines a job to be executed remotely.
 * 
 * @author Manasa Chandrasekhar
 * @author Kowshik Prakasam
 * 
 */
public abstract class Job {

	/**
	 * Decomposes the job into a list of smaller tasks of type @{link api.Task
	 * Task}, each of which are executed remotely in a compute space
	 * 
	 * @param space
	 *            Compute space to which {@link api.Task Task} objects should be
	 *            sent for execution
	 * @throws RemoteException
	 */
	public abstract void generateTasks(Space space) throws RemoteException;

	/**
	 * Gathers {@link api.Result Result} objects from the compute space
	 * @param space Compute space containing the results obtained after remote execution of tasks
	 * @throws RemoteException
	 */
	public abstract void collectResults(Space space) throws RemoteException;
	

	/**
	 * Transforms values returned by {@link api.Result Result} objects into something relevant to the subclass implementing this interface
	 * 
	 * @return A container with values from all {@link api.Result Result}
	 *         objects
	 */
	public abstract Object getAllResults();

}
