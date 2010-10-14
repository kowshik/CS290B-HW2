package system;

import java.rmi.RemoteException;
import java.util.Random;

import api.Result;
import api.Task;

/**
 * For every {@link system.Computer Computer} instance that registers with the
 * compute space ({@link api.Space Space}), there is a proxy maintained by the
 * space. This allows the compute space to maintain multiple threads for each
 * instance of registered {@link system.Computer Computer} objects. This class
 * is responsible for execution of {@link api.Task Task} objects in the
 * registered remote computers.
 * 
 * @author Manasa Chandrasekhar
 * @author Kowshik Prakasam
 * 
 */
public class ComputerProxy implements Runnable {

	private Computer compObj;
	private SpaceImpl space;
	private Thread t;

	/**
	 * 
	 * @param compObj
	 *            Computer registed with the compute space ({@link api.Space
	 *            Space})
	 * @param space
	 *            Implementation of ({@link api.Space Space}) which is
	 *            responsible for maintaining each instance of this class
	 */
	public ComputerProxy(Computer compObj, SpaceImpl space) {
		this.compObj = compObj;
		this.space = space;
		t = new Thread(this, "ComputerProxy " + getRandomProxyName());
		t.start();
	}

	/**
	 * 
	 * @return A random thread name made up of exactly three alphabets
	 */
	private String getRandomProxyName() {
		char first = (char) ((new Random().nextInt(26)) + 65);
		char second = (char) ((new Random().nextInt(26)) + 65);
		char third = (char) ((new Random().nextInt(26)) + 65);
		return "" + first + second + third;
	}

	/**
	 * Loops infinitely and attempts to fetch a {@link api.Task Task} object
	 * from the compute space and executes it. If the thread is interrupted, the
	 * task is returned to the compute space's queue. If the task execution is
	 * successful, then the {@link api.Result Result} produced is also added to
	 * compute space's queue of {@link api.Result Result} objects
	 */
	public void run() {
		Task<?> aTask = null;
		while (true) {
			try {
				aTask = space.takeTask();
				Result<?> res = compObj.execute(aTask);
				space.putResult(res);

			} catch (RemoteException remoteEx) {
				System.err
						.println("ComputerProxy : RemoteException occured in thread : "
								+ this.t.getName());
				System.err.println("Reassigning task to task queue");
				try {
					space.put(aTask);
				} catch (RemoteException e) {
					e.printStackTrace();
				}

			} catch (InterruptedException e) {
				System.err.println("ComputerProxy InterruptedException");
				e.printStackTrace();
			}
		}

	}
}
