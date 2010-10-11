package client;

import java.awt.geom.Point2D;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import api.Result;
import api.Space;

/**
 * Generic class that defines a job to be executed remotely.
 * 
 * @author Manasa Chandrasekhar
 * @author Kowshik Prakasam
 * 
 */
public abstract class Job {

	protected List<Result<?>> results;
	protected Map<String, Point2D> taskIdentifierMap;

	/**
	 * Default constructor
	 */
	protected Job() {
		this.results = new Vector<Result<?>>();
		this.taskIdentifierMap = new HashMap<String, Point2D>();
	}

	/**
	 * Decomposes the job into a list of smaller tasks of type @{link api.Task
	 * Task}, each of which are executed remotely in a compute space
	 * 
	 * @param space
	 *            Compute space to which {@link api.Task Task} objects should be
	 *            sent for execution
	 * @throws RemoteException
	 */
	protected abstract void generateTasks(Space space) throws RemoteException;

	/**
	 * Gathers {@link api.Result Result} objects from the compute space and populates them into the attribute : {@link #results results}
	 * @param space Compute space containing the results obtained after remote execution of tasks
	 * @throws RemoteException
	 */
	protected void collectResults(Space space) throws RemoteException {
		for (int i = 0; i < taskIdentifierMap.size(); i++) {
			Result<?> r = space.takeResult();
			this.results.add(r);
		}
	}

	/**
	 * Extracts values from {@link api.Result Result} objects
	 * 
	 * @return A container with values from all {@link api.Result Result}
	 *         objects
	 */
	protected abstract Object getAllResults();

}
