package system;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.LinkedBlockingQueue;

import api.Result;
import api.Space;
import api.Task;

/**
 * Implementation of the Space Interface. Represents a raw computing resource
 * where tasks (({@link api.Task Task}) are automatically executed by registered
 * workers as soon as they are dropped in. If a worker crashes, the computation
 * would still continue (assuming there are other workers still running), since
 * each task is executed under a transaction, which would be rolled back after
 * the worker crashed, leaving the task in the space for another worker to pick
 * up. For more information, please refer <a
 * href="http://today.java.net/pub/a/today/2005/04/21/farm.html">How to build a
 * compute farm</a>.
 * 
 * @author Manasa Chandrasekhar
 * @author Kowshik Prakasam
 * 
 */
public class SpaceImpl extends UnicastRemoteObject implements Space,
		Computer2Space {

	private static final long serialVersionUID = 3093568798450948074L;
	private LinkedBlockingQueue<Task<?>> listTasks;
	private LinkedBlockingQueue<Result<?>> listResults;
	private static final int PORT_NUMBER = 2672;

	/**
	 * Default constructor
	 * 
	 * @throws RemoteException
	 */
	public SpaceImpl() throws RemoteException {
		this.listTasks = new LinkedBlockingQueue<Task<?>>();
		this.listResults = new LinkedBlockingQueue<Result<?>>();

	}

	@Override
	/**
	 *  Remote method used by the clients to add tasks to the queue of tasks in this compute space.
	 *  This method is thread-safe and blocks during concurrent calls to this method by clients running simultaneously (or) {@link system.ComputerProxy ComputerProxy} objects trying to return {@link api.Result Result} objects to this compute space.
	 *  @throws RemoteException
	 */
	public void put(Task<?> task) throws RemoteException {
		listTasks.add(task);
	}

	/**
	 * Used to add to the queue of {@link api.Result Result} objects in this
	 * compute space
	 * 
	 * @throws RemoteException
	 */
	public void putResult(Result<?> result) throws RemoteException {
		listResults.add(result);
	}

	/**
	 * Used to remove a task from the queue of {@link api.Task Task} objects in
	 * this compute space
	 * 
	 * @throws RemoteException
	 */
	public Task<?> takeTask() throws InterruptedException {
		return listTasks.take();
	}

	@Override
	/** 
	 * Remote method for the clients to fetch results from the compute space. This method is thread-safe and blocks until a {@link api.Result Result} is added to the queue by Computer Proxies.
	 * 
	 * @return A generic result from the beginning of the queue
	 * @throws RemoteException
	 */
	public Result<?> takeResult() throws RemoteException {
		try {
			return (Result<?>) listResults.take();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * Remote method for the computers to register to the compute space
	 * 
	 * @throws RemoteException
	 */
	@Override
	public void register(Computer computer) throws RemoteException {
		new ComputerProxy(computer, this);
	}

	/**
	 * Starts the compute space and binds remote objects into the RMI registry
	 * 
	 * @param args Command-line arguments can be passed (if any)
	 */
	public static void main(String[] args) {

		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		try {

			Space space = new SpaceImpl();
			Registry registry = LocateRegistry.createRegistry(PORT_NUMBER);
			registry.rebind(Space.SERVICE_NAME, space);
			System.out.println("Space instance bound");
		} catch (Exception e) {
			System.err.println("SpaceImpl exception:");
			e.printStackTrace();

		}
	}

}
