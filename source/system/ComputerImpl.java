
package system;


import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import api.Result;
import api.Task;

/**
 * Defines the remote server which is accessed by the client for execution of objects of type {@link api.Task Task}
 * 
 * @author Manasa Chandrasekhar
 * @author Kowshik Prakasam
 */
public class ComputerImpl extends UnicastRemoteObject implements Computer {

	private static final long serialVersionUID = -4634299253959618077L;
	/**
	 * Sets up the server for execution
	 * @throws RemoteException
	 */
	public ComputerImpl() throws RemoteException {
		super();
	}
	
	/**
	 * @see api.Task Task
	 */

	public Result<?> execute(Task<?> t) {
		
		long startTime=System.currentTimeMillis();
		Object value=t.execute();
		long endTime=System.currentTimeMillis();
		Result<?> r=new ResultImpl<Object>(startTime, endTime, value, t.getTaskIdentifier());
		return r;
	}

	/**
	 * 
	 * Register Computer objects to the compute space
	 */
	public static void main(String[] args) {
		String computeSpaceServer=args[0];
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		try {
			
			ComputerImpl comp = new ComputerImpl();
			Computer2Space space=(Computer2Space) Naming.lookup("//" + computeSpaceServer + "/"+Computer2Space.SERVICE_NAME);
			space.register(comp);
			System.out.println("Computer ready");
		} catch (RemoteException e) {
			System.err.println("ComputerImpl exception : ");
			e.printStackTrace();

		} catch (MalformedURLException e) {
			System.err.println("ComputerImpl exception : ");
			e.printStackTrace();
		} catch (NotBoundException e) {
			System.err.println("ComputerImpl exception : ");
			e.printStackTrace();
		}
	}



}
