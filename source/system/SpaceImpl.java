package system;


import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import api.Result;
import api.Space;
import api.Task;

public class SpaceImpl extends UnicastRemoteObject implements Space, Computer2Space {

	/** Implementation of the Space Interface
	 * 
	 */
	
	public LinkedBlockingQueue<Task<?>> listTasks;
	public List<Result<?>> listResults;
	private static final long serialVersionUID = 1L;
	
	//public LinkedBlockingQueue<Task<?>> listTasks = new LinkedBlockingQueue<Task<?>>();
	//public Vector<Object> listResults = new Vector<Object>();

	public SpaceImpl()throws RemoteException {
		 this.listTasks = new LinkedBlockingQueue<Task<?>>();
		 this.listResults = Collections.synchronizedList(new LinkedList<Result<?>>());

		
	}
	@Override
	/** Remote method used by the clients to add tasks to the list of tasks in Space */
	public void put(Task<?> task) throws RemoteException {
		// TODO Auto-generated method stub
		//Object ob = (Task<T>) task;
		listTasks.add(task);
	}

	@Override
	/** Remote method for the clients to fetch results from the Space */
	public Result<?> takeResult() throws RemoteException {
		// TODO Auto-generated method stub
		return (Result<?>) listResults.remove(0);
		
	}

	/** Remote method for the computers to register to the Space */
	@Override
	public void register(Computer computer) throws RemoteException {
		// TODO Auto-generated method stub
		SpaceImpl sp = new SpaceImpl();
		Computerproxy cp = new Computerproxy(computer, sp);
		new Thread(cp).start();
	}
	
	
	 public static void main(String[] args) {
		 
	        if (System.getSecurityManager() == null) {
	            System.setSecurityManager(new SecurityManager());
	        }
	        try {
	           
	            Space space = new SpaceImpl();
	            Registry registry = LocateRegistry.createRegistry(1099);
	            registry.rebind(Space.SERVICE_NAME, space);
	            System.out.println("Space instance bound");
	        } catch (Exception e) {
	            System.err.println("SpaceImpl exception:");
	            e.printStackTrace();

	        }
	    }
	

}
